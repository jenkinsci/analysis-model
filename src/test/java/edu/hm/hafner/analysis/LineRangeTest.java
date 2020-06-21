package edu.hm.hafner.analysis;

import edu.hm.hafner.util.SerializableTest;

/**
 * Testklasse fuer die Klasse LineRange.
 *
 * @author Andreas Kienle, akienle@hm.edu
 * @version Last modified on 21/06/2020
 */
class LineRangeTest extends SerializableTest<LineRange> {
    @Override
    protected LineRange createSerializable() {
        return new LineRange(5);
    }
}
