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
        LineRange range = new LineRangeBuilder()
                .setStart(1350)
                .setEnd(Integer.MAX_VALUE)
                .build();
        list.add(range);
        assertThat(list).containsExactly(range);
    }

    @Test
    void shouldStoreRangeWithOneLines() {
        LineRangeList list = new LineRangeList();
        LineRange range = new LineRangeBuilder()
                .build();
        list.add(range);
        assertThat(list).containsExactly(range);
    }

    @Test
    void shouldStoreRangeWithTwoLines() {
        LineRangeList list = new LineRangeList();
        LineRange range = new LineRangeBuilder()
                .setStart(128)
                .setEnd(129)
                .build();
        list.add(range);
        assertThat(list).containsExactly(range);
    }

    @Test
    void shouldSupportSetOperations() {
        LineRangeList list = new LineRangeList();
        LineRange range = new LineRangeBuilder()
                .setStart(1)
                .setEnd(2)
                .build();
        list.add(range);

        assertThat(list.get(0)).isEqualTo(range);
        assertThat(list.get(0)).isNotSameAs(range);
        assertThat(list).hasSize(1);

        LineRange other = new LineRangeBuilder()
                .setStart(3)
                .setEnd(4)
                .build();
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
            list.add(new LineRangeBuilder().setStart(i * 2).setEnd(i * 2 + 1).build());
        }
        list.trim();
        assertThat(list).hasSize(100);

        for (int i = 0; i < 100; i++) {
            assertThat(list.get(i)).isEqualTo(new LineRangeBuilder().setStart(i * 2).setEnd(i * 2 + 1).build());
            assertThat(list.contains(new LineRangeBuilder().setStart(i * 2).setEnd(i * 2 + 1).build())).isTrue();
        }

        assertThat(list).hasSize(100);
    }

    @Test
    void shouldProvideContains() {
        LineRangeList last = createThreeElements();
        last.remove(new LineRangeBuilder().setStart(4).setEnd(5).build());
        assertThat(last).containsExactly(new LineRangeBuilder().setStart(1).setEnd(0).build(), new LineRangeBuilder().setStart(2).setEnd(3).build());

        LineRangeList middle = createThreeElements();
        middle.remove(new LineRangeBuilder().setStart(2).setEnd(3).build());
        assertThat(middle).containsExactly(new LineRangeBuilder().setStart(0).setEnd(1).build(), new LineRangeBuilder().setStart(4).setEnd(5).build());

        LineRangeList first = createThreeElements();
        assertThat(first.contains(new LineRangeBuilder().setStart(0).setEnd(1).build())).isTrue();
        assertThat(first.contains(new LineRangeBuilder().setStart(2).setEnd(3).build())).isTrue();
        assertThat(first.contains(new LineRangeBuilder().setStart(4).setEnd(5).build())).isTrue();

        first.remove(new LineRangeBuilder().setStart(0).setEnd(1).build());
        assertThat(first).containsExactly(new LineRangeBuilder().setStart(2).setEnd(3).build(), new LineRangeBuilder().setStart(4).setEnd(5).build());

        assertThat(first.contains(new LineRangeBuilder().setStart(2).setEnd(3).build())).isTrue();
        assertThat(first.contains(new LineRangeBuilder().setStart(0).setEnd(1).build())).isFalse();
    }

    private LineRangeList createThreeElements() {
        LineRangeList range = new LineRangeList();
        range.add(new LineRangeBuilder().setStart(0).setEnd(1).build());
        range.add(new LineRangeBuilder().setStart(2).setEnd(3).build());
        range.add(new LineRangeBuilder().setStart(4).setEnd(5).build());
        assertThat(range).containsExactly(new LineRangeBuilder().setStart(0).setEnd(1).build(), new LineRangeBuilder().setStart(2).setEnd(3).build(), new LineRangeBuilder().setStart(4).setEnd(5).build());
        return range;
    }
}
