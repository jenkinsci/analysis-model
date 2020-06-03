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

        LineRangeBuilder builder = new LineRangeBuilder();
        builder.setStart(0);
        builder.setEnd(Integer.MAX_VALUE);
        LineRange range = builder.build();

        list.add(range);
        assertThat(list).containsExactly(range);
    }

    @Test
    void shouldStoreRangeWithOneLines() {
        LineRangeList list = new LineRangeList();

        LineRangeBuilder builder = new LineRangeBuilder();
        builder.setStart(0);
        builder.setEnd(0);
        LineRange range = builder.build();

        list.add(range);
        assertThat(list).containsExactly(range);
    }

    @Test
    void shouldStoreRangeWithTwoLines() {
        LineRangeList list = new LineRangeList();

        LineRangeBuilder builder = new LineRangeBuilder();
        builder.setStart(128);
        builder.setEnd(129);
        LineRange range = builder.build();

        list.add(range);
        assertThat(list).containsExactly(range);
    }

    @Test
    void shouldSupportSetOperations() {
        LineRangeList list = new LineRangeList();

        LineRangeBuilder builder = new LineRangeBuilder();
        builder.setStart(1);
        builder.setEnd(2);

        LineRange range = builder.build();
        list.add(range);

        assertThat(list.get(0)).isEqualTo(range);
        assertThat(list.get(0)).isNotSameAs(range);
        assertThat(list).hasSize(1);

        builder.setStart(3);
        builder.setEnd(4);

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

        LineRangeBuilder builder = new LineRangeBuilder();

        for (int i = 0; i < 100; i++) {
            builder.setStart(i * 2);
            builder.setEnd(i * 2 + 1);
            list.add(builder.build());
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
        LineRangeBuilder builder = new LineRangeBuilder();
        builder.setStart(4);
        builder.setEnd(5);
        last.remove(builder.build());
        assertThat(last).containsExactly(new LineRange(0, 1), new LineRange(2, 3));

        LineRangeList middle = createThreeElements();

        builder.setStart(2);
        builder.setEnd(3);
        middle.remove(builder.build());
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

    private LineRangeList createThreeElements() {
        LineRangeList range = new LineRangeList();

        LineRangeBuilder builder = new LineRangeBuilder();
        builder.setEnd(1);
        builder.setStart(0);

        range.add(builder.build());

        builder.setEnd(3);
        builder.setStart(2);
        range.add(builder.build());

        builder.setStart(4);
        builder.setEnd(5);

        range.add(builder.build());

        assertThat(range).containsExactly(new LineRange(0, 1), new LineRange(2, 3), new LineRange(4, 5));
        return range;
    }
}
