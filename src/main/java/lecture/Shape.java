package lecture;

import java.awt.Color;

/**
 * Base class for shapes.
 *
 * @author Ullrich Hafner
 */
public abstract class Shape {
    private Color color;

    protected Shape(final Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public abstract int getArea();

    public void move(Pixel offset) {
        // empty default implementation
    }
}
