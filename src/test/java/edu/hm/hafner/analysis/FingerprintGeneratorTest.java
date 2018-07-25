package edu.hm.hafner.analysis;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import edu.hm.hafner.analysis.FullTextFingerprint.FileSystem;
import static edu.hm.hafner.analysis.assertj.Assertions.*;
import edu.hm.hafner.util.ResourceTest;
import static org.mockito.Mockito.*;

/**
 * Tests the class {@link FingerprintGenerator}.
 *
 * @author Ullrich Hafner
 */
class FingerprintGeneratorTest extends ResourceTest {
    private static final String AFFECTED_FILE_NAME = "file.txt";
    private static final Charset CHARSET_AFFECTED_FILE = StandardCharsets.UTF_8;
    private static final String ID = "ID";

    @Test
    void shouldNotChangeIssuesWithFingerPrint() {
        FingerprintGenerator generator = new FingerprintGenerator();

        IssueBuilder builder = new IssueBuilder().setFileName(AFFECTED_FILE_NAME);
        Report report = createIssues();
        report.add(builder.build());
        assertThat(report.get(0).hasFingerprint()).isFalse();
        String alreadySet = "already-set";
        report.add(builder.setFingerprint(alreadySet).setMessage(AFFECTED_FILE_NAME).build());
        generator.run(createFullTextFingerprint("fingerprint-one.txt", "fingerprint-two.txt"),
                report, CHARSET_AFFECTED_FILE);

        assertThat(report.get(0).hasFingerprint()).isTrue();
        assertThat(report.get(1).getFingerprint()).isEqualTo(alreadySet);
        assertThat(report).hasId(ID);
    }

    @Test
    void shouldAssignIdenticalFingerprint() {
        Report report = createTwoIssues();
        FingerprintGenerator generator = new FingerprintGenerator();
        FullTextFingerprint fingerprint = createFullTextFingerprint("fingerprint-one.txt", "fingerprint-one.txt");

        generator.run(fingerprint, report, CHARSET_AFFECTED_FILE);

        Issue referenceIssue = report.get(0);
        Issue currentIssue = report.get(1);
        assertThat(report).hasId(ID);

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
            when(fileSystem.readLinesFromFile(AFFECTED_FILE_NAME, CHARSET_AFFECTED_FILE))
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
        FullTextFingerprint fingerprint = createFullTextFingerprint("fingerprint-one.txt", "fingerprint-two.txt");

        generator.run(fingerprint, report, CHARSET_AFFECTED_FILE);

        assertThat(report).hasId(ID);
        Issue referenceIssue = report.get(0);
        Issue currentIssue = report.get(1);

        assertThat(referenceIssue).isNotEqualTo(currentIssue);
        assertThat(referenceIssue.getFingerprint()).isNotEqualTo(currentIssue.getFingerprint());
    }

    @ParameterizedTest(name = "[{index}] Illegal filename")
    @ValueSource(strings = {"/does/not/exist", "!<>$&/&(", "\0 Null-Byte"})
    void shouldUseFallbackFingerprintOnError(final String fileName) {
        Report report = new Report();
        report.add(new IssueBuilder().setFileName(fileName).build());

        FingerprintGenerator generator = new FingerprintGenerator();
        generator.run(new FullTextFingerprint(), report, CHARSET_AFFECTED_FILE);

        assertThat(report.get(0)).hasFingerprint(FingerprintGenerator.createDefaultFingerprint(report.get(0)));
    }


    private FullTextFingerprint createFullTextFingerprint(final String firstFile, final String secondFile) {
        FileSystem fileSystem = stubFileSystem(firstFile, secondFile);
        return new FullTextFingerprint(fileSystem);
    }

    private Report createTwoIssues() {
        Report report = createIssues();
        IssueBuilder builder = new IssueBuilder();
        builder.setFileName(AFFECTED_FILE_NAME);
        builder.setLineStart(5);
        report.add(builder.setPackageName("a").build());
        report.add(builder.setPackageName("b").build());
        return report;
    }

    private Report createIssues() {
        Report report = new Report();
        report.setId(ID);
        return report;
    }
}