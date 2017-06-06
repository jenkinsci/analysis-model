package lecture;

/**
 * Shape utilities.
 *
 * @author Ullrich Hafner
 */
public class Shapes {
    public static int area(final Shape... shapes) {
        Circle circle = null;
        Rectangle rectangle = null;
        Shape shape = null;

        shape = circle;
        shape = rectangle;

        if (shape instanceof Circle) {
            circle = (Circle)shape;

        }

        int area = 0;
        for (Shape aShape : shapes) {
            area += aShape.getArea();
        }
        return area;
    }
}
