package lecture;

/**
 * A drawable object.
 *
 * @author Ullrich Hafner
 */
public interface Drawable {
    int getArea();

    void move(Pixel offset);
}
