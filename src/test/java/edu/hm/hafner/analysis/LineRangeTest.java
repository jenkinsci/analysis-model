package edu.hm.hafner.analysis;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.LineRange.LineRangeBuilder;
import edu.hm.hafner.util.SerializableTest;

/**
 * Class for testing {@link LineRange}
 *
 * @author S. A. D.
 */
public class LineRangeTest extends SerializableTest<LineRange> {

    @Override
    protected LineRange createSerializable() {
        return new LineRangeBuilder().setStart(20).setEnd(80).build();
    }

    @Test
    @DisplayName("should be serializable: instance -> byte array -> instance")
    void shouldBeSerializable() {

        LineRange serializableInstance = createSerializable();
        byte[] bytes = toByteArray(serializableInstance);
        assertThatSerializableCanBeRestoredFrom(bytes);
    }
}
