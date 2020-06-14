package edu.hm.hafner.analysis;

import edu.hm.hafner.util.SerializableTest;

public class LineRangeTest extends SerializableTest<LineRange> {

    @Override
    protected LineRange createSerializable() {
        return new LineRange(1);
    }
}
