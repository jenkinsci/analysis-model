package edu.hm.hafner.analysis;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.util.SerializableTest;

/**
 * Tests the class {@link LineRange}.
 * @author Johannes Jäger
 */
class LineRangeTest extends SerializableTest<LineRange> {

    @Override
    protected LineRange createSerializable() {
        return new LineRange(1);
    }
}
