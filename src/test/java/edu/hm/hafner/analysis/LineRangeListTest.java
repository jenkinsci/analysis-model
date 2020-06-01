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
        LineRange range = new LineRangeBuilder().withLineRange(1350, Integer.MAX_VALUE).build();
        list.add(range);
        assertThat(list).containsExactly(range);
    }

    @Test
    void shouldStoreRangeWithOneLines() {
        LineRangeList list = new LineRangeList();
        LineRange range = new LineRangeBuilder().withLineRange(0, 0).build();
        list.add(range);
        assertThat(list).containsExactly(range);
    }

    @Test
    void shouldStoreRangeWithTwoLines() {
        LineRangeList list = new LineRangeList();
        LineRange range = new LineRangeBuilder().withLineRange(128, 129).build();
        list.add(range);
        assertThat(list).containsExactly(range);
    }

    @Test
    void shouldSupportSetOperations() {
        LineRangeList list = new LineRangeList();
        LineRange range = new LineRangeBuilder().withLineRange(1, 2).build();
        list.add(range);

        assertThat(list.get(0)).isEqualTo(range);
        assertThat(list.get(0)).isNotSameAs(range);
        assertThat(list).hasSize(1);

        LineRange other = new LineRangeBuilder().withLineRange(3, 4).build();
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
            list.add(new LineRangeBuilder().withLineRange(i * 2, i * 2 + 1).build());
        }
        list.trim();
        assertThat(list).hasSize(100);

        for (int i = 0; i < 100; i++) {
            assertThat(list.get(i)).isEqualTo(new LineRangeBuilder().withLineRange(i * 2, i * 2 + 1).build());
            assertThat(list.contains(new LineRangeBuilder().withLineRange(i * 2, i * 2 + 1).build())).isTrue();
        }

        assertThat(list).hasSize(100);
    }

    @Test
    void shouldProvideContains() {
        LineRangeList last = createThreeElements();
        last.remove(new LineRangeBuilder().withLineRange(4, 5).build());
        assertThat(last).containsExactly(
                new LineRangeBuilder().withLineRange(0, 1).build(),
                new LineRangeBuilder().withLineRange(2, 3).build());

        LineRangeList middle = createThreeElements();
        middle.remove(new LineRangeBuilder().withLineRange(2, 3).build());
        assertThat(middle).containsExactly(
                new LineRangeBuilder().withLineRange(0, 1).build(),
                new LineRangeBuilder().withLineRange(4, 5).build());

        LineRangeList first = createThreeElements();
        assertThat(first.contains(new LineRangeBuilder().withLineRange(0, 1).build())).isTrue();
        assertThat(first.contains(new LineRangeBuilder().withLineRange(2, 3).build())).isTrue();
        assertThat(first.contains(new LineRangeBuilder().withLineRange(4, 5).build())).isTrue();

        first.remove(new LineRangeBuilder().withLineRange(0, 1).build());
        assertThat(first).containsExactly(
                new LineRangeBuilder().withLineRange(2, 3).build(),
                new LineRangeBuilder().withLineRange(4, 5).build());

        assertThat(first.contains(new LineRangeBuilder().withLineRange(2, 3).build())).isTrue();
        assertThat(first.contains(new LineRangeBuilder().withLineRange(0, 1).build())).isFalse();
    }

    private LineRangeList createThreeElements() {
        LineRangeList range = new LineRangeList();
        range.add(new LineRangeBuilder().withLineRange(0, 1).build());
        range.add(new LineRangeBuilder().withLineRange(2, 3).build());
        range.add(new LineRangeBuilder().withLineRange(4, 5).build());
        assertThat(range).containsExactly(
                new LineRangeBuilder().withLineRange(0, 1).build(),
                new LineRangeBuilder().withLineRange(2, 3).build(),
                new LineRangeBuilder().withLineRange(4, 5).build());
        return range;
    }
}
