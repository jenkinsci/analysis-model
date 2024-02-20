package edu.hm.hafner.analysis;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import edu.hm.hafner.analysis.FullTextFingerprint.FileSystem;
import edu.hm.hafner.util.ResourceTest;

import static edu.hm.hafner.analysis.assertions.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests the class {@link FingerprintGenerator}.
 *
 * @author Ullrich Hafner
 */
class FingerprintGeneratorTest extends ResourceTest {
    private static final String AFFECTED_FILE_NAME = "file.txt";
    private static final Charset CHARSET_AFFECTED_FILE = StandardCharsets.UTF_8;

    @Test
    void shouldSkipFingerprintingIfEncodingIsWrong() throws IOException {
        FingerprintGenerator generator = new FingerprintGenerator();

        Report report;
        try (IssueBuilder issueBuilder = new IssueBuilder()) {
            issueBuilder.setFileName(AFFECTED_FILE_NAME);
            report = createIssues();
            report.add(issueBuilder.build());
        }

        FileSystem fileSystem = mock(FileSystem.class);
        when(fileSystem.readLinesFromFile(anyString(), any()))
                .thenThrow(new UncheckedIOException(new MalformedInputException(1)));

        generator.run(new FullTextFingerprint(fileSystem), report, CHARSET_AFFECTED_FILE);

        assertThatIssueHasDefaultFingerprint(report);
        assertThat(report.getErrorMessages()).contains(
                String.format("- 'file.txt', provided encoding '%s' seems to be wrong", CHARSET_AFFECTED_FILE));
    }

    @Test
    void shouldNotChangeIssuesWithFingerPrint() {
        try (IssueBuilder issueBuilder = new IssueBuilder()) {
            FingerprintGenerator generator = new FingerprintGenerator();

            issueBuilder.setFileName(AFFECTED_FILE_NAME);
            Report report = createIssues();
            report.add(issueBuilder.build());
            assertThat(report.get(0).hasFingerprint()).isFalse();

            String alreadySet = "already-set";
            report.add(issueBuilder.setFingerprint(alreadySet).setMessage(AFFECTED_FILE_NAME).build());
            generator.run(createFullTextFingerprint("fingerprint-two.txt"),
                    report, CHARSET_AFFECTED_FILE);

            assertThat(report.get(0).hasFingerprint()).isTrue();
            assertThat(report.get(1).getFingerprint()).isEqualTo(alreadySet);
        }
    }

    @Test
    void shouldSetDefaultFingerprintIfNoFileIsGiven() {
        try (IssueBuilder issueBuilder = new IssueBuilder()) {
            FingerprintGenerator generator = new FingerprintGenerator();

            Report report = new Report();
            report.add(issueBuilder.build());

            generator.run(new FullTextFingerprint(), report, CHARSET_AFFECTED_FILE);

            assertThatIssueHasDefaultFingerprint(report);
            assertThat(report.getErrorMessages()).isEmpty();
        }
    }

    @Test
    void shouldAssignIdenticalFingerprint() {
        Report report = createTwoIssues();
        FingerprintGenerator generator = new FingerprintGenerator();
        FullTextFingerprint fingerprint = createFullTextFingerprint("fingerprint-one.txt");

        generator.run(fingerprint, report, CHARSET_AFFECTED_FILE);

        Issue referenceIssue = report.get(0);
        Issue currentIssue = report.get(1);

        assertThat(referenceIssue).isNotEqualTo(currentIssue);
        assertThat(referenceIssue.getFingerprint()).isEqualTo(currentIssue.getFingerprint());

        assertThat(referenceIssue.getFingerprint())
                .as("Fingerprint is not set")
                .isNotEmpty()
                .isNotEqualTo("-");
    }

    @SuppressWarnings("MustBeClosedChecker")
    private FileSystem stubFileSystem(final String firstFile, final String secondFile) {
        try {
            FileSystem fileSystem = mock(FileSystem.class);
            when(fileSystem.readLinesFromFile(anyString(), any()))
                    .thenReturn(asStream(firstFile)).thenReturn(asStream(secondFile));
            return fileSystem;
        }
        catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    @Test
    void shouldAssignDifferentFingerprint() {
        Report report = createTwoIssues();
        FingerprintGenerator generator = new FingerprintGenerator();
        FullTextFingerprint fingerprint = createFullTextFingerprint("fingerprint-two.txt");

        generator.run(fingerprint, report, CHARSET_AFFECTED_FILE);

        Issue referenceIssue = report.get(0);
        Issue currentIssue = report.get(1);

        assertThat(referenceIssue).isNotEqualTo(currentIssue);
        assertThat(referenceIssue.getFingerprint()).isNotEqualTo(currentIssue.getFingerprint());
    }

    @ParameterizedTest(name = "[{index}] Illegal filename {0}")
    @ValueSource(strings = {"/does/not/exist", "!<>$&/&(", "\0 Null-Byte"})
    void shouldUseFallbackFingerprintOnError(final String fileName) {
        var report = createReportWithOneIssueFor(fileName);

        FingerprintGenerator generator = new FingerprintGenerator();
        generator.run(new FullTextFingerprint(), report, CHARSET_AFFECTED_FILE);

        assertThatIssueHasDefaultFingerprint(report);
    }

    @ParameterizedTest(name = "[{index}] Skip non source code file {0}")
    @ValueSource(strings = {"library.o", "program.exe", "library.dll", "program.so", "library.a", "program.lib",
            "library.jar", "library.war", "program.zip", "library.7z", "program.tar.gz", "library.tar.bz2",
            "UPPER_CASE.EXE", "prefix::suffix.txt"})
    void shouldUseFallbackFingerprintOnNonSourceFiles(final String fileName) {
        var report = createReportWithOneIssueFor(fileName);

        FingerprintGenerator generator = new FingerprintGenerator();
        generator.run(createFullTextFingerprint("fingerprint-two.txt"),
                report, CHARSET_AFFECTED_FILE);

        assertThatIssueHasDefaultFingerprint(report);
    }

    @ParameterizedTest(name = "[{index}] Skip illegal source code files on Windows {0}")
    @ValueSource(strings = {"prefix::suffix.txt", ":"})
    void shouldUseFallbackFingerprintOnIllegalFilenamesOnWindows(final String fileName) {
        assumeThatTestIsRunningOnWindows();

        var report = createReportWithOneIssueFor(fileName);

        FingerprintGenerator generator = new FingerprintGenerator();
        generator.run(createFullTextFingerprint("fingerprint-two.txt"),
                report, CHARSET_AFFECTED_FILE);

        assertThatIssueHasDefaultFingerprint(report);
    }

    private Report createReportWithOneIssueFor(final String fileName) {
        Report report;
        try (IssueBuilder issueBuilder = new IssueBuilder()) {
            report = new Report();
            report.add(issueBuilder.setFileName(fileName).build());
        }
        return report;
    }

    private void assertThatIssueHasDefaultFingerprint(final Report report) {
        assertThat(report.get(0)).hasFingerprint(FingerprintGenerator.createDefaultFingerprint(report.get(0)));
    }

    private FullTextFingerprint createFullTextFingerprint(final String secondFile) {
        FileSystem fileSystem = stubFileSystem("fingerprint-one.txt", secondFile);

        return new FullTextFingerprint(fileSystem);
    }

    private Report createTwoIssues() {
        try (IssueBuilder builder = new IssueBuilder()) {
            Report report = createIssues();
            builder.setFileName(AFFECTED_FILE_NAME);
            builder.setLineStart(5);
            report.add(builder.setPackageName("a").build());
            report.add(builder.setPackageName("b").build());
            return report;
        }
    }

    private Report createIssues() {
        return new Report();
    }
}
