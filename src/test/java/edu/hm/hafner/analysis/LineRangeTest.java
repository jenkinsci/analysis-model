package edu.hm.hafner.analysis;

import edu.hm.hafner.util.SerializableTest;

/**
 * Test Class for LineRange
 *
 * @author Viet Phuoc Ho v.ho@hm.edu
 *
 */
class LineRangeTest extends SerializableTest<LineRange> {

    @Override
    protected LineRange createSerializable() {
        return new LineRange(5, 5);
    }
}
