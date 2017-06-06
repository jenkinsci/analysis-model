package lecture;

import org.junit.Test;

/**
 * Tests the class {@link Rectangle}.
 *
 * @author Ullrich Hafner
 */
public class RectangleTest {
    @Test
    public void should() {
        new Rectangle(new Pixel(0, 0), 20, 40);
    }
}