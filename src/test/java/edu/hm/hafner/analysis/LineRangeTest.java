package edu.hm.hafner.analysis;

import java.util.Arrays;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests the class {@link LineRange}.
 *
 * @author Ullrich Hafner
 */
public class LineRangeTest {
    /**
     * Creates a new {@link LineRange}. The specified starting and ending lines are included in this range.
     *
     * @param from the first line of this range
     * @param to   the last line of this range
     * @return the created SUT
     */
    protected LineRange createLineRange(final int from, final int to) {
        return new LineRange(from, to);
    }

    /**
     * Creates a new {@link LineRange} containing only one line.
     *
     * @param line the single line of this range
     * @return the created SUT
     */
    protected LineRange createLineRange(final int line) {
        return new LineRange(line);
    }

    /** Verifies the properties of a three element range. */
    @Test
    public void shouldCreateThreeElements() {
        LineRange range = createLineRange(1, 3);

        assertThatRangeContainsThreeElements(range);
    }

    private void assertThatRangeContainsThreeElements(final LineRange range) {
        assertThat(range.size()).isEqualTo(3);
        assertThat(range.toString()).isEqualTo("{1, ..., 3}");
        assertThat(range.values()).containsExactly(1, 2, 3);
    }

    /** Verifies the properties of a two element range. */
    @Test
    public void shouldCreateTwoElements() {
        LineRange range = createLineRange(1, 2);
        assertThat(range.size()).isEqualTo(2);
        assertThat(range.toString()).isEqualTo("{1, 2}");
        assertThat(range.values()).containsExactly(1, 2);
    }

    /** Verifies the properties of a single element range. */
    @Test
    public void shouldCreateSingleElement() {
        LineRange range = createLineRange(1, 1);
        assertThat(range.size()).isEqualTo(1);
        assertThat(range.toString()).isEqualTo("{1}");
        assertThat(range.values()).containsExactly(1);
    }

    /** Checks that the ranges [1,2] and [3,4] do not intersect. */
    @Test
    public void shouldNotIntersect() {
        LineRange first = createLineRange(1, 2);
        LineRange second = createLineRange(3, 4);

        assertThat(first.intersects(second)).isFalse();
        assertThat(second.intersects(first)).isFalse();
    }

    /** Checks that intersecting ranges are detected. */
    @Test
    public void shouldIntersect() {
        LineRange first = createLineRange(2, 4);

        assertThat(first.intersects(createLineRange(1, 1))).isFalse();
        assertThat(first.intersects(createLineRange(2, 2))).isTrue();
        assertThat(first.intersects(createLineRange(3, 3))).isTrue();
        assertThat(first.intersects(createLineRange(4, 4))).isTrue();
        assertThat(first.intersects(createLineRange(5, 5))).isFalse();

        assertThat(first.intersects(createLineRange(1, 2))).isTrue();
        assertThat(first.intersects(createLineRange(1, 5))).isTrue();
        assertThat(first.intersects(createLineRange(4, 5))).isTrue();
    }

    /** Checks that flipping from and to makes no difference. */
    @Test
    public void shouldHandleFlippedValues() {
        LineRange flipped = createLineRange(3, 1);

        assertThatRangeContainsThreeElements(flipped);
    }

    /** Verifies that parameters are checked in the constructor. */
    @Test
    public void shouldThrowExceptionOnIllegalValues() {
        assertThatThrownBy(() -> createLineRange(0))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("0");
        assertThatThrownBy(() -> createLineRange(-1))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("-1");
        assertThatThrownBy(() -> createLineRange(0, 2))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("0");
        assertThatThrownBy(() -> createLineRange(2, 0))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("0");
    }

    /** Verifies that all points in the range [2, 4] are detected with contains. */
    @Test
    public void shouldContainValues() {
        LineRange range = createLineRange(2, 4);

        assertThat(range.contains(1)).isFalse();
        assertThat(range.contains(2)).isTrue();
        assertThat(range.contains(3)).isTrue();
        assertThat(range.contains(4)).isTrue();
        assertThat(range.contains(5)).isFalse();
    }

    /** Verifies that all points (given as range) in the range [2, 4] are detected with contains. */
    @Test
    public void shouldContainRanges() {
        LineRange range = createLineRange(2, 4);

        assertThat(range.contains(createLineRange(2, 2))).isTrue();
        assertThat(range.contains(createLineRange(2, 3))).isTrue();
        assertThat(range.contains(createLineRange(2, 4))).isTrue();
        assertThat(range.contains(createLineRange(2, 5))).isFalse();
        assertThat(range.contains(createLineRange(1, 4))).isFalse();
        assertThat(range.contains(createLineRange(5, 6))).isFalse();
    }

    /** Verifies that no internal data is returned. */
    @Test
    public void shouldNotExposeInternalRepresentation() {
        LineRange range = createLineRange(1, 3);

        int[] values = range.values();
        Arrays.fill(values, 0);

        assertThatRangeContainsThreeElements(range);
    }
}