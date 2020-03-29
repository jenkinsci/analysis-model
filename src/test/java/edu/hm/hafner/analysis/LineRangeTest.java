package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

import edu.hm.hafner.util.SerializableTest;

/**
 * Tests the class {@link LineRange}.
 *
 * @author budelmann
 */
class LineRangeTest extends SerializableTest<LineRange> {
    @Override
    protected LineRange createSerializable() {
        return new LineRange(1, 2);
    }

    @Test
    public void shouldBeTheSameInstance() {
        final LineRange lineRange = createSerializable();
        assertThat(lineRange.getStart()).isEqualTo(1);
        assertThat(lineRange.getEnd()).isEqualTo(2);
    }

}
