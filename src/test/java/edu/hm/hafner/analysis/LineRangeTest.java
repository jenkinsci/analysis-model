package edu.hm.hafner.analysis;

import edu.hm.hafner.util.SerializableTest;

/**
 * Tests, if class LineRange is Serializable {@link LineRange}.
 *
 * @author Tobias Karius
 */
class LineRangeTest extends SerializableTest<LineRange> {
    @Override
    protected LineRange createSerializable() {
        return new LineRange(1, 2);
    }
}
