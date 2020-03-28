package edu.hm.hafner.analysis;

import edu.hm.hafner.util.SerializableTest;

 class LineRangeTest extends SerializableTest<LineRange> {

    public final static int START = 2;
    public final static int END = 6;

    @Override
    protected LineRange createSerializable() {
        return new LineRange(START,END);
    }
}
