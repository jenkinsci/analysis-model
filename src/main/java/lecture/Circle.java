package lecture;

import java.awt.Color;

/**
 * A simple circle.
 *
 * @author Ullrich Hafner
 */
public class Circle extends Shape {
    private final Pixel center;
    private final int radius;

    public Circle(final Color color, final Pixel center, final int radius) {
        super(color);

        this.center = center;
        this.radius = radius;
    }

    public int getArea() {
        return (int)(radius * radius * Math.PI);
    }
}
