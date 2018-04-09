package edu.hm.hafner.analysis;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static edu.hm.hafner.analysis.assertj.Assertions.*;
import static org.mockito.Mockito.*;

import edu.hm.hafner.analysis.FullTextFingerprint.FileSystem;
import edu.hm.hafner.util.ResourceTest;

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
        Issues<Issue> issues = createIssues();
        issues.add(builder.build());
        assertThat(issues.get(0).hasFingerprint()).isFalse();
        String alreadySet = "already-set";
        issues.add(builder.setFingerprint(alreadySet).setMessage(AFFECTED_FILE_NAME).build());
        generator.run(createFullTextFingerprint("fingerprint-one.txt", "fingerprint-two.txt"),
                issues, CHARSET_AFFECTED_FILE);

        assertThat(issues.get(0).hasFingerprint()).isTrue();
        assertThat(issues.get(1).getFingerprint()).isEqualTo(alreadySet);
        assertThat(issues).hasOrigin(ID);
    }

    @Test
    void shouldAssignIdenticalFingerprint() {
        Issues<Issue> issues = createTwoIssues();
        FingerprintGenerator generator = new FingerprintGenerator();
        FullTextFingerprint fingerprint = createFullTextFingerprint("fingerprint-one.txt", "fingerprint-one.txt");

        generator.run(fingerprint, issues, CHARSET_AFFECTED_FILE);

        Issue referenceIssue = issues.get(0);
        Issue currentIssue = issues.get(1);
        assertThat(issues).hasOrigin(ID);

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
        Issues<Issue> issues = createTwoIssues();
        FingerprintGenerator generator = new FingerprintGenerator();
        FullTextFingerprint fingerprint = createFullTextFingerprint("fingerprint-one.txt", "fingerprint-two.txt");

        generator.run(fingerprint, issues, CHARSET_AFFECTED_FILE);

        assertThat(issues).hasOrigin(ID);
        Issue referenceIssue = issues.get(0);
        Issue currentIssue = issues.get(1);

        assertThat(referenceIssue).isNotEqualTo(currentIssue);
        assertThat(referenceIssue.getFingerprint()).isNotEqualTo(currentIssue.getFingerprint());
    }

    @ParameterizedTest(name = "[{index}] Illegal filename = {0}")
    @ValueSource(strings = {"/does/not/exist", "!<>$$&%/&(", "\0 Null-Byte"})
    void shouldUseFallbackFingerprintOnError(final String fileName) {
        Issues<Issue> issues = new Issues<>();
        issues.add(new IssueBuilder().setFileName(fileName).build());

        FingerprintGenerator generator = new FingerprintGenerator();
        generator.run(new FullTextFingerprint(), issues, CHARSET_AFFECTED_FILE);

        assertThat(issues.get(0)).hasFingerprint(FingerprintGenerator.createDefaultFingerprint(fileName));
    }


    private FullTextFingerprint createFullTextFingerprint(final String firstFile, final String secondFile) {
        FileSystem fileSystem = stubFileSystem(firstFile, secondFile);
        return new FullTextFingerprint(fileSystem);
    }

    private Issues<Issue> createTwoIssues() {
        Issues<Issue> issues = createIssues();
        IssueBuilder builder = new IssueBuilder();
        builder.setFileName(AFFECTED_FILE_NAME);
        builder.setLineStart(5);
        issues.add(builder.setPackageName("a").build());
        issues.add(builder.setPackageName("b").build());
        return issues;
    }

    private Issues<Issue> createIssues() {
        Issues<Issue> issues = new Issues<>();
        issues.setOrigin(ID);
        return issues;
    }
}