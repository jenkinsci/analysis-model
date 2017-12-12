package edu.hm.hafner.analysis;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.FullTextFingerprint.FileSystem;
import static edu.hm.hafner.analysis.assertj.Assertions.*;
import edu.hm.hafner.util.SerializableTest;
import static org.mockito.Mockito.*;

/**
 * Tests the class {@link FingerprintGenerator}.
 *
 * @author Ullrich Hafner
 */
class FingerprintGeneratorTest extends SerializableTest {
    private static final String AFFECTED_FILE_NAME = "file.txt";
    private static final Charset CHARSET_AFFECTED_FILE = Charset.forName("UTF-8");
    private static final int ADDITIONAL_LINES = 5;

    @Test
    void shouldReturnCopyOfIssues() {
        FingerprintGenerator generator = new FingerprintGenerator();

        Issues<Issue> original = new Issues<>();
        Issues<Issue> copy = generator.run(original, new IssueBuilder(), CHARSET_AFFECTED_FILE);

        assertThat(copy).isNotSameAs(original);
    }

    @Test
    void shouldAssignIdenticalFingerprint() {
        Issues<Issue> issues = createTwoIssues();
        FileSystem fileSystem = stubFileSystem("fingerprint-one.txt", "fingerprint-one.txt");
        FingerprintGenerator generator = new FingerprintGenerator(fileSystem);

        Issues<Issue> enhanced = generator.run(issues, new IssueBuilder(), CHARSET_AFFECTED_FILE);

        Issue referenceIssue = enhanced.get(0);
        Issue currentIssue = enhanced.get(1);

        assertThat(referenceIssue).isNotEqualTo(currentIssue);
        assertThat(referenceIssue.getFingerprint()).isEqualTo(currentIssue.getFingerprint());

        assertThat(referenceIssue.getFingerprint())
                .as("Fingerprint is not set")
                .isNotEmpty()
                .isNotEqualTo("-");
    }

    private FileSystem stubFileSystem(final String firstFile, final String secondFile) {
        try {
            FileSystem mock = mock(FileSystem.class);
            when(mock.readLinesFromFile(AFFECTED_FILE_NAME, CHARSET_AFFECTED_FILE))
                    .thenReturn(asStream(firstFile)).thenReturn(asStream(secondFile));
            return mock;
        }
        catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    @Test
    void shouldAssignDifferentFingerprint() {
        Issues<Issue> issues = createTwoIssues();
        FileSystem fileSystem = stubFileSystem("fingerprint-one.txt", "fingerprint-two.txt");
        FingerprintGenerator generator = new FingerprintGenerator(fileSystem);

        Issues<Issue> enhanced = generator.run(issues, new IssueBuilder(), Charset.forName("UTF-8"));

        Issue referenceIssue = enhanced.get(0);
        Issue currentIssue = enhanced.get(1);

        assertThat(referenceIssue).isNotEqualTo(currentIssue);
        assertThat(referenceIssue.getFingerprint()).isNotEqualTo(currentIssue.getFingerprint());
    }

    private Issues<Issue> createTwoIssues() {
        Issues<Issue> issues = new Issues<>();
        IssueBuilder builder = new IssueBuilder();
        builder.setFileName(AFFECTED_FILE_NAME);
        builder.setLineStart(5);
        issues.add(builder.setPackageName("a").build());
        issues.add(builder.setPackageName("b").build());
        return issues;
    }

    private Stream<String> asStream(final String affectedFile) {
        return readResourceToStream(affectedFile, CHARSET_AFFECTED_FILE);
    }
}