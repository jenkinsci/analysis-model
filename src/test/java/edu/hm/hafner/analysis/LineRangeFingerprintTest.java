package edu.hm.hafner.analysis;

import java.io.BufferedReader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.util.SerializableTest;
import static org.assertj.core.api.Assertions.*;

/**
 * Tests the class {@link LineRangeFingerprint}.
 *
 * @author Ullrich Hafner
 */
class LineRangeFingerprintTest extends SerializableTest {
    private static final String NOT_EXISTING_FILE_NAME = "/does/not/exist";

    /**
     * Verifies that the context of a warning starts 3 lines above the affected line and ends 3 lines below the affected
     * line.
     */
    @Test
    void shouldExtractCorrectLines() {
        String affectedFile = new String(readResource("context.txt"));

        LineRangeFingerprint fingerprint = new LineRangeFingerprint();

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
        String affectedFile = new String(readResource("context.txt"));

        LineRangeFingerprint code = new LineRangeFingerprint();

        String fingerprint = code.createFingerprint(10, asStream(affectedFile));

        for (int line = 0; line < 34; line++) {
            if (line == 10 || line == 20) {
                assertThat(fingerprint).isEqualTo(code.createFingerprint(line, asStream(affectedFile)));
            }
            else {
                assertThat(fingerprint).isNotEqualTo(code.createFingerprint(line, asStream(affectedFile)));
            }
        }
    }

    @Test
    void shouldReturnFallbackOnError() {
        LineRangeFingerprint fingerprint = new LineRangeFingerprint();

        assertThat(fingerprint.compute(NOT_EXISTING_FILE_NAME, 1, Charset.defaultCharset()))
                .isEqualTo(fingerprint.getFallbackFingerprint(NOT_EXISTING_FILE_NAME));
    }

    private Iterator<String> asIterator(final String affectedFile) {
        return asStream(affectedFile).iterator();
    }

    private Stream<String> asStream(final String affectedFile) {
        return new BufferedReader(new StringReader(affectedFile)).lines();
    }
}