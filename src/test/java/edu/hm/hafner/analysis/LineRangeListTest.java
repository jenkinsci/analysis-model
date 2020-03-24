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
        LineRange range = new LineRange(1350, Integer.MAX_VALUE);
        list.add(range);
        assertThat(list).containsExactly(range);
    }

    @Test
    void shouldStoreRangeWithOneLines() {
        LineRangeList list = new LineRangeList();
        LineRange range = new LineRange(0, 0);
        list.add(range);
        assertThat(list).containsExactly(range);
    }

    @Test
    void shouldStoreRangeWithTwoLines() {
        LineRangeList list = new LineRangeList();
        LineRange range = new LineRange(128, 129);
        list.add(range);
        assertThat(list).containsExactly(range);
    }

    @Test
    void shouldSupportSetOperations() {
        LineRangeList list = new LineRangeList();
        LineRange range = new LineRange(1, 2);
        list.add(range);

        assertThat(list.get(0)).isEqualTo(range);
        assertThat(list.get(0)).isNotSameAs(range);
        assertThat(list).hasSize(1);

        LineRange other = new LineRange(3, 4);
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
            list.add(new LineRange(i * 2, i * 2 + 1));
        }
        list.trim();
        assertThat(list).hasSize(100);

        for (int i = 0; i < 100; i++) {
            assertThat(list.get(i)).isEqualTo(new LineRange(i * 2, i * 2 + 1));
            assertThat(list.contains(new LineRange(i * 2, i * 2 + 1))).isTrue();
        }

        assertThat(list).hasSize(100);
    }

    @Test
    void shouldProvideContains() {
        LineRangeList last = createThreeElements();
        last.remove(new LineRange(4, 5));
        assertThat(last).containsExactly(new LineRange(0, 1), new LineRange(2, 3));

        LineRangeList middle = createThreeElements();
        middle.remove(new LineRange(2, 3));
        assertThat(middle).containsExactly(new LineRange(0, 1), new LineRange(4, 5));

        LineRangeList first = createThreeElements();
        assertThat(first.contains(new LineRange(0, 1))).isTrue();
        assertThat(first.contains(new LineRange(2, 3))).isTrue();
        assertThat(first.contains(new LineRange(4, 5))).isTrue();

        first.remove(new LineRange(0, 1));
        assertThat(first).containsExactly(new LineRange(2, 3), new LineRange(4, 5));

        assertThat(first.contains(new LineRange(2, 3))).isTrue();
        assertThat(first.contains(new LineRange(0, 1))).isFalse();
    }

    @Test
    void shouldBombUnfinishedLineRangeBuilder() {
        LineRangeBuilder sut = new LineRangeBuilder();

        assertThatThrownBy(()-> {
            LineRange a = sut.build();
        }).isInstanceOf(IllegalStateException.class);

        sut.setStart(0);
        assertThatThrownBy(()-> {
            LineRange a = sut.build();
        }).isInstanceOf(IllegalStateException.class);

        sut.setEnd(1);
        assertThatCode(()-> {
            LineRange a = sut.build();
        }).doesNotThrowAnyException();
    }

    private LineRangeList createThreeElements() {
        LineRangeList range = new LineRangeList();
        range.add(new LineRange(0, 1));
        range.add(new LineRange(2, 3));
        range.add(new LineRange(4, 5));
        assertThat(range).containsExactly(new LineRange(0, 1), new LineRange(2, 3), new LineRange(4, 5));
        return range;
    }

    private LineRange createLineRange(final int start, final int end) {
        return new LineRangeBuilder()
                .setStart(start)
                .setEnd(end)
                .build();
    }
}
