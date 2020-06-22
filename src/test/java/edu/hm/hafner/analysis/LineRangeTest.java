package edu.hm.hafner.analysis;

import edu.hm.hafner.util.SerializableTest;

/**
 * @author Matthias König
 */

class LineRangeTest extends SerializableTest<LineRange> {

    @Override
    protected LineRange createSerializable() {
        return new LineRange(23,75);
    }
}