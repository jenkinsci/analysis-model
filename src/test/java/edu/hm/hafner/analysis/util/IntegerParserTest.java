package edu.hm.hafner.analysis.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static edu.hm.hafner.analysis.util.IntegerParser.*;
import static org.assertj.core.api.Assertions.*;

/**
 * Tests the class {@link IntegerParser}.
 *
 * @author Ullrich Hafner
 */
class IntegerParserTest {
    @ParameterizedTest(name = "[{index}] Illegal number = {0}")
    @ValueSource(strings = {"text", "15x20", "\0 Null-Byte", "", " ", "23.5"})
    void shouldReturnDefaultValueOnInvalidValues(final String invalidIntegerValue) {
        assertThat(parseInt(invalidIntegerValue)).isZero();
    }

    @Test
    void shouldReturnValidInteger() {
        assertThat(parseInt("0")).isZero();
        assertThat(parseInt("1")).isOne();
        assertThat(parseInt("1010")).isEqualTo(1010);
    }
}
