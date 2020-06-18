package edu.hm.hafner.analysis;

import edu.hm.hafner.util.SerializableTest;

public class LineRangeTest extends SerializableTest<LineRange> {

    @java.lang.Override
    protected LineRange createSerializable() {
        return createFilledLineRange();
    }

    protected LineRange createFilledLineRange() {
        return new LineRange(0, 20);
    }

}
