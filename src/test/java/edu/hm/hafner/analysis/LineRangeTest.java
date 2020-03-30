package edu.hm.hafner.analysis;

import edu.hm.hafner.util.SerializableTest;

/**
 * Konkreter SerializableTest für die Klasse LineRange.
 */
public class LineRangeTest extends SerializableTest<LineRange> {
    @Override
    protected LineRange createSerializable() {
        return new LineRange(0, 1);
    }
}
