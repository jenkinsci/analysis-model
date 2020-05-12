package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests the class {@link LineRangeList}.
 *
 * @author Kohsuke Kawaguchi
 */
@SuppressWarnings({"PMD", "all"})
//CHECKSTYLE:OFF
class LineRangeListTest {
    @Test
    void shouldStoreBigValues() {
        LineRangeList list = new LineRangeList();
        LineRange range = createLineRange(1350, Integer.MAX_VALUE);
        list.add(range);
        assertThat(list).containsExactly(range);
    }

    @Test
    void shouldStoreRangeWithOneLines() {
        LineRangeList list = new LineRangeList();
        LineRange range = createLineRange(0, 0);
        list.add(range);
        assertThat(list).containsExactly(range);
    }

    @Test
    void shouldStoreRangeWithTwoLines() {
        LineRangeList list = new LineRangeList();
        LineRange range = createLineRange(128, 129);
        list.add(range);
        assertThat(list).containsExactly(range);
    }

    @Test
    void shouldSupportSetOperations() {
        LineRangeList list = new LineRangeList();
        LineRange range = createLineRange(1, 2);
        list.add(range);

        assertThat(list.get(0)).isEqualTo(range);
        assertThat(list.get(0)).isNotSameAs(range);
        assertThat(list).hasSize(1);

        LineRange other = createLineRange(3, 4);
        assertThat(list.set(0, other)).isEqualTo(range);
        assertThat(list.get(0)).isEqualTo(other);
        assertThat(list.get(0)).isNotSameAs(other);
        assertThat(list).hasSize(1);

        assertThat(list.remove(0)).isEqualTo(other);
        assertThat(list).hasSize(0);
    }

    /** Tests the internal buffer resize operation. */
    @Test
    void shouldResizeCorrectly() {
        LineRangeList list = new LineRangeList();
        for (int i = 0; i < 100; i++) {
            list.add(createLineRange(i * 2, i * 2 + 1));
        }
        list.trim();
        assertThat(list).hasSize(100);

        for (int i = 0; i < 100; i++) {
            assertThat(list.get(i)).isEqualTo(createLineRange(i * 2, i * 2 + 1));
            assertThat(list.contains(createLineRange(i * 2, i * 2 + 1))).isTrue();
        }

        assertThat(list).hasSize(100);
    }

    @Test
    void shouldProvideContains() {
        LineRangeList last = createThreeElements();
        last.remove(createLineRange(4, 5));
        assertThat(last).containsExactly(createLineRange(0, 1), createLineRange(2, 3));

        LineRangeList middle = createThreeElements();
        middle.remove(createLineRange(2, 3));
        assertThat(middle).containsExactly(createLineRange(0, 1), createLineRange(4, 5));

        LineRangeList first = createThreeElements();
        assertThat(first.contains(createLineRange(0, 1))).isTrue();
        assertThat(first.contains(createLineRange(2, 3))).isTrue();
        assertThat(first.contains(createLineRange(4, 5))).isTrue();

        first.remove(createLineRange(0, 1));
        assertThat(first).containsExactly(createLineRange(2, 3), createLineRange(4, 5));

        assertThat(first.contains(createLineRange(2, 3))).isTrue();
        assertThat(first.contains(createLineRange(0, 1))).isFalse();
    }

    private LineRangeList createThreeElements() {
        LineRangeList range = new LineRangeList();
        range.add(createLineRange(0, 1));
        range.add(createLineRange(2, 3));
        range.add(createLineRange(4, 5));
        assertThat(range).containsExactly(createLineRange(0, 1), createLineRange(2, 3), createLineRange(4, 5));
        return range;
    }

    private LineRange createLineRange(int start, int end) {
        return new LineRangeBuilder().setStart(start).setEnd(end).build();
    }
}
