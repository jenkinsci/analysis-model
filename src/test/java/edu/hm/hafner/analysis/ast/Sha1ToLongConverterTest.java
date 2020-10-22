package edu.hm.hafner.analysis.ast;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests the class {@link Sha1ToLongConverter}.
 *
 * @author Ullrich Hafner
 */
class Sha1ToLongConverterTest {
    /** Verifies if the SHA1 values are correctly converted to a long value. */
    @Test
    void shouldConvertSha1ToLong() {
        verify("0000000010101010101010101010101010101010", 0x111111111111111L);
        verify("00000000e0e0e0e0e0e0e0e0e0e0e0e0e0e0e0e0", 0xeeeeeeeeeeeeeeeL);
        verify("00000000f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0", 0xfffffffffffffffL);
    }

    private void verify(final String sha1, final long expected) {
        Sha1ToLongConverter converter = new Sha1ToLongConverter();

        assertThat(converter.toLong(sha1)).isEqualTo(expected);
    }
}
