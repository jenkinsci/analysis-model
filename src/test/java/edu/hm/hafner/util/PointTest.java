package edu.hm.hafner.util;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests the class {@link Point}.
 */
public class PointTest extends AbstractEqualsTest {
    private Point createPoint(final int x, final int y) {
        return new Point(x, y);
    }

    @Override
    protected Object createSut() {
        return createPoint(5, 8);
    }

    /** Verifies that the coordinates of a new point are correctly set. */
    @Test
    public void shouldCreatePoint() {
        // Given
        Point point = createPoint(3, 4);

        // When and then
        assertThat(point.getX()).isEqualTo(3);
        assertThat(point.getY()).isEqualTo(4);
    }

    /** Verifies that the equals operation correctly works. */
    @Test
    public void shouldDetectEqualPoints() {
        // Given
        Point point = createPoint(3, 4);

        // When and then
        assertThat(point).isEqualTo(new Point(3, 4));
    }

    /** Verifies that the equals operation correctly works. */
    @Test
    public void shouldDetectNotEqualPoints() {
        // Given
        Point point = createPoint(3, 4);

        // When and then
        assertThat(point).isNotEqualTo(new Point(2, 4));
        assertThat(point).isNotEqualTo(new Point(4, 4));
        assertThat(point).isNotEqualTo(new Point(3, 3));
        assertThat(point).isNotEqualTo(new Point(3, 5));
    }

    /** Verifies that the surrounding points are correctly computed and the SUT is not modified. */
    @Test
    public void shouldCreateNeighbors() {
        // Given
        Point point = createPoint(3, 4);

        // When and then
        assertThat(point.moveLeft()).isEqualTo(new Point(2, 4));
        assertThat(point.moveRight()).isEqualTo(new Point(4, 4));
        assertThat(point.moveUp()).isEqualTo(new Point(3, 3));
        assertThat(point.moveDown()).isEqualTo(new Point(3, 5));

        assertThat(point).isEqualTo(new Point(3, 4));
    }

    /** Verifies that {@link Point#toString()} is overwritten. */
    @Test
    public void shouldHaveVerboseToString() {
        assertThat(createPoint(4, 2).toString()).isEqualTo("(4, 2)");
    }
}