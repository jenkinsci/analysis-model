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

    private LineRange createNewLineRange(int start, int end){
        return new LineRangeBuilder()
                   .setStart(start)
                   .setEnd(end)
                   .build();
    }

    @Test
    void shouldStoreBigValues() {
        LineRangeList list = new LineRangeList();
        LineRange range = createNewLineRange(1350, Integer.MAX_VALUE);
        list.add(range);
        assertThat(list).containsExactly(range);
    }

    @Test
    void shouldStoreRangeWithOneLines() {
        LineRangeList list = new LineRangeList();
        LineRange range = createNewLineRange(0, 0);
        list.add(range);
        assertThat(list).containsExactly(range);
    }

    @Test
    void shouldStoreRangeWithTwoLines() {
        LineRangeList list = new LineRangeList();
        LineRange range = createNewLineRange(128, 129);
        list.add(range);
        assertThat(list).containsExactly(range);
    }

    @Test
    void shouldSupportSetOperations() {
        LineRangeList list = new LineRangeList();
        LineRange range = createNewLineRange(1, 2);
        list.add(range);

        assertThat(list.get(0)).isEqualTo(range);
        assertThat(list.get(0)).isNotSameAs(range);
        assertThat(list).hasSize(1);

        LineRange other = createNewLineRange(3, 4);
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
            list.add(createNewLineRange(i * 2, i * 2 + 1));
        }
        list.trim();
        assertThat(list).hasSize(100);

        for (int i = 0; i < 100; i++) {
            assertThat(list.get(i)).isEqualTo(createNewLineRange(i * 2, i * 2 + 1));
            assertThat(list.contains(createNewLineRange(i * 2, i * 2 + 1))).isTrue();
        }

        assertThat(list).hasSize(100);
    }

    @Test
    void shouldProvideContains() {
        LineRangeList last = createThreeElements();
        last.remove(createNewLineRange(4, 5));
        assertThat(last).containsExactly(createNewLineRange(0, 1), createNewLineRange(2, 3));

        LineRangeList middle = createThreeElements();
        middle.remove(createNewLineRange(2, 3));
        assertThat(middle).containsExactly(createNewLineRange(0, 1), createNewLineRange(4, 5));

        LineRangeList first = createThreeElements();
        assertThat(first.contains(createNewLineRange(0, 1))).isTrue();
        assertThat(first.contains(createNewLineRange(2, 3))).isTrue();
        assertThat(first.contains(createNewLineRange(4, 5))).isTrue();

        first.remove(createNewLineRange(0, 1));
        assertThat(first).containsExactly(createNewLineRange(2, 3), createNewLineRange(4, 5));

        assertThat(first.contains(createNewLineRange(2, 3))).isTrue();
        assertThat(first.contains(createNewLineRange(0, 1))).isFalse();
    }

    private LineRangeList createThreeElements() {
        LineRangeList range = new LineRangeList();
        range.add(createNewLineRange(0, 1));
        range.add(createNewLineRange(2, 3));
        range.add(createNewLineRange(4, 5));
        assertThat(range).containsExactly(createNewLineRange(0, 1), createNewLineRange(2, 3), createNewLineRange(4, 5));
        return range;
    }
}
