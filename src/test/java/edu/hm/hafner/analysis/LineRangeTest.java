package edu.hm.hafner.analysis;

import edu.hm.hafner.util.SerializableTest;
/**
 * Testklasse erbt Tests der SerializableTest-Klasse und wendet sie auf ein LineRange-Objekt an
 * @author Michael Schober
 */
class LineRangeTest extends SerializableTest<LineRange> {

    @Override
    protected LineRange createSerializable() {
        return new LineRangeBuilder().build();
    }
}