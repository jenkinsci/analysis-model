package lecture;

import java.awt.Color;

/**
 * A simple rectangle.
 *
 * @author Ullrich Hafner
 */
public class Rectangle extends Shape {
    private final Pixel start;
    private final int width;
    private final int height;

    public Rectangle(final Pixel start, final int width, final int height) {
        super(Color.BLACK);

        this.start = start;
        this.width = width;
        this.height = height;
    }

    public int getArea() {
        return width * height;
    }
}
