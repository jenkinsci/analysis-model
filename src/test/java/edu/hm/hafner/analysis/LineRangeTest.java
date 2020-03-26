package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.util.SerializableTest;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests the {@link LineRange} class.
 *
 * @author walli545
 */
class LineRangeTest extends SerializableTest<LineRange> {

    private static final int DEFAULT_START = 5;
    private static final int DEFAULT_END = 10;

    @Override
    protected LineRange createSerializable() {
        return new LineRange(DEFAULT_START, DEFAULT_END);
    }

    @Test
    void shouldCreateLineRangeWithDefaultRange() {
        final LineRange lineRange = createSerializable();
        assertThat(lineRange.getStart()).isEqualTo(DEFAULT_START);
        assertThat(lineRange.getEnd()).isEqualTo(DEFAULT_END);
    }
}
