package edu.hm.mschober;

import edu.hm.hafner.analysis.LineRange;
import edu.hm.hafner.util.SerializableTest;

/**
 * @author Michael Schober, mschober@hm.edu
 * @version 2020-31-03
 */
public class LineRangeTest extends SerializableTest<LineRange> {
    @Override
    protected LineRange createSerializable() {
        return new LineRange(1,2);
    }
}
