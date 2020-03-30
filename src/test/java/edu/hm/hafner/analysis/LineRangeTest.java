package edu.hm.hafner.analysis;

import edu.hm.hafner.util.SerializableTest;

/**
 * Implementation of the factory pattern for LineRangeTest.
 */
class LineRangeTest extends SerializableTest<LineRange> {

    @Override
    protected LineRange createSerializable() {
        return new LineRange(5, 10);
    }
}
