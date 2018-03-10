package edu.hm.hafner.analysis;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.NoSuchFileException;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

import edu.hm.hafner.util.ResourceTest;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Tests the class {@link FullTextFingerprint}.
 *
 * @author Ullrich Hafner
 */
@SuppressFBWarnings("DMI")
class FullTextFingerprintTest extends ResourceTest {
    /**
     * Verifies that the context of a warning starts 3 lines above the affected line and ends 3 lines below the affected
     * line.
     */
    @Test
    void shouldExtractCorrectLines() {
        String affectedFile = new String(readAllBytes("context.txt"), StandardCharsets.UTF_8);

        FullTextFingerprint fingerprint = new FullTextFingerprint();

        assertThat(fingerprint.extractContext(-1, asIterator(affectedFile)))
                .as("Fingerprint for illegal line numbers should be empty").isEmpty();

        assertThat(fingerprint.extractContext(0, asIterator(affectedFile)))
                .as("Wrong fingerprint for whole file").isEqualTo("1234567");

        assertThat(fingerprint.extractContext(1, asIterator(affectedFile))).isEqualTo("1234");
        assertThat(fingerprint.extractContext(2, asIterator(affectedFile))).isEqualTo("12345");
        assertThat(fingerprint.extractContext(3, asIterator(affectedFile))).isEqualTo("123456");
        assertThat(fingerprint.extractContext(4, asIterator(affectedFile))).isEqualTo("1234567");
        assertThat(fingerprint.extractContext(5, asIterator(affectedFile))).isEqualTo("2345678");
        assertThat(fingerprint.extractContext(27, asIterator(affectedFile))).isEqualTo("4567890");
        assertThat(fingerprint.extractContext(28, asIterator(affectedFile))).isEqualTo("567890");
        assertThat(fingerprint.extractContext(29, asIterator(affectedFile))).isEqualTo("67890");
        assertThat(fingerprint.extractContext(30, asIterator(affectedFile))).isEqualTo("7890");

        // actually illegal but we use the remaining lines:
        assertThat(fingerprint.extractContext(31, asIterator(affectedFile))).isEqualTo("890");
        assertThat(fingerprint.extractContext(32, asIterator(affectedFile))).isEqualTo("90");
        assertThat(fingerprint.extractContext(33, asIterator(affectedFile))).isEqualTo("0");

        assertThat(fingerprint.extractContext(34, asIterator(affectedFile)))
                .as("Fingerprint for line numbers out of range should be empty").isEmpty();
    }

    /**
     * Verifies that the fingerprint of line 10 is the same as the fingerprint of line 20. All other lines should have a
     * different fingerprint.
     */
    @Test
    void shouldAssignIdenticalFingerprints() {
        String affectedFile = new String(readAllBytes("context.txt"), StandardCharsets.UTF_8);

        FullTextFingerprint code = new FullTextFingerprint();

        String fingerprint = code.createFingerprint(10, getTextLinesAsStream(affectedFile), getCharset());

        for (int line = 0; line < 34; line++) {
            if (line == 10 || line == 20) {
                assertThat(fingerprint).isEqualTo(
                        code.createFingerprint(line, getTextLinesAsStream(affectedFile), getCharset()));
            }
            else {
                assertThat(fingerprint).isNotEqualTo(
                        code.createFingerprint(line, getTextLinesAsStream(affectedFile), getCharset()));
            }
        }
    }

    @Test
    void shouldThrowNoSuchFileExceptionIfFileDoesNotExist() {
        FullTextFingerprint fingerprint = new FullTextFingerprint();

        assertThatExceptionOfType(NoSuchFileException.class)
                .isThrownBy(() -> fingerprint.compute("/does/not/exist", 1, getCharset()));
    }

    private Charset getCharset() {
        return Charset.forName("UTF-8");
    }

    private Iterator<String> asIterator(final String affectedFile) {
        return getTextLinesAsStream(affectedFile).iterator();
    }
}