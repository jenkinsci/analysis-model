package edu.hm.hafner.analysis;

import edu.hm.hafner.util.SerializableTest;

public class LineRangeTest extends SerializableTest<LineRange> {

    public final int START = 2;
    public final int END = 6;

    @Override
    protected LineRange createSerializable() {
        return new LineRange(START,END);
    }
}
