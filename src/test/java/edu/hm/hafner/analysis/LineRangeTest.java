package edu.hm.hafner.analysis;

import edu.hm.hafner.analysis.LineRange.LineRangeBuilder;
import edu.hm.hafner.util.SerializableTest;

/**
 * Implementation of Serializable Factory test for {@link LineRange}.
 *
 * @author Simon Symhoven
 */
class LineRangeTest extends SerializableTest<LineRange> {
    @Override
    protected LineRange createSerializable() {
        return new LineRangeBuilder().build();
    }
}
