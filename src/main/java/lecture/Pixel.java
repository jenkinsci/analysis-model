package lecture;

public class Pixel {
    private final int x;
    private final int y;

    public Pixel(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Pixel pixel = (Pixel)o;

        if (x != pixel.x) {
            return false;
        }
        return y == pixel.y;
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", x, y);
    }

    @Override
    public int hashCode() {
        return 31 * x + y;
    }
}
