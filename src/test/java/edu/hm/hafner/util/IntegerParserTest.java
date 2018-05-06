package edu.hm.hafner.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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
        assertThat(new IntegerParser().parseInt(invalidIntegerValue)).isZero();
    }

    @Test
    void shouldReturnValidInteger() {
        IntegerParser integerParser = new IntegerParser();
        assertThat(integerParser.parseInt("0")).isZero();
        assertThat(integerParser.parseInt("1")).isOne();
        assertThat(integerParser.parseInt("1010")).isEqualTo(1010);
    }
}