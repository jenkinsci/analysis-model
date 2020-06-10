package edu.hm.hafner.analysis;

import edu.hm.hafner.util.SerializableTest;

/**
 * Tests for the class {@link LineRange}.
 */
class LineRangeTest extends SerializableTest<LineRange> {

    @Override
    protected LineRange createSerializable() {
        return new LineRange(0);
    }
}
