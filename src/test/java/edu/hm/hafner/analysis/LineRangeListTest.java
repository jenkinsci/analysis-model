package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.LineRange.LineRangeBuilder;

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
//        LineRange range = new LineRange(1350, Integer.MAX_VALUE);
        LineRange range = new LineRangeBuilder().withStart(1350).withEnd(Integer.MAX_VALUE).build();

        list.add(range);
        assertThat(list).containsExactly(range);
    }

    @Test
    void shouldStoreRangeWithOneLines() {
        LineRangeList list = new LineRangeList();
//        LineRange range = new LineRange(0, 0);
        LineRange range = new LineRangeBuilder().withStart(0).withEnd(0).build();
        list.add(range);
        assertThat(list).containsExactly(range);
    }

    @Test
    void shouldStoreRangeWithTwoLines() {
        LineRangeList list = new LineRangeList();
//        LineRange range = new LineRange(128, 129);
        LineRange range = new LineRangeBuilder().withStart(128).withEnd(129).build();

        list.add(range);
        assertThat(list).containsExactly(range);
    }

    @Test
    void shouldStoreOneLineWithBuilder(){
        LineRangeList list = new LineRangeList();
        LineRange range = new LineRangeBuilder().withLine(0).build();
        list.add(range);

        assertThat(list).containsExactly(range);
    }

    @Test
    void shouldSupportSetOperations() {
        LineRangeList list = new LineRangeList();
//        LineRange range = new LineRange(1, 2);
        LineRange range = new LineRangeBuilder().withStart(1).withEnd(2).build();

        list.add(range);

        assertThat(list.get(0)).isEqualTo(range);
        assertThat(list.get(0)).isNotSameAs(range);
        assertThat(list).hasSize(1);

//        LineRange other = new LineRange(3, 4);
        LineRange other = new LineRangeBuilder().withStart(3).withEnd(4).build();

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
//            list.add(new LineRange(i * 2, i * 2 + 1));
            list.add(new LineRangeBuilder().withStart(i*2).withEnd(i*2 + 1).build());
        }
        list.trim();
        assertThat(list).hasSize(100);

        for (int i = 0; i < 100; i++) {
//            assertThat(list.get(i)).isEqualTo(new LineRange(i * 2, i * 2 + 1));
            assertThat(list.get(i)).isEqualTo(new LineRangeBuilder().withStart(i*2).withEnd(i*2 + 1).build());
//            assertThat(list.contains(new LineRange(i * 2, i * 2 + 1))).isTrue();
            assertThat(list.contains(new LineRangeBuilder().withStart(i*2).withEnd(i*2 + 1).build())).isTrue();
        }

        assertThat(list).hasSize(100);
    }

    @Test
    void shouldProvideContains() {
        LineRangeList last = createThreeElements();
//        last.remove(new LineRange(4, 5));
        last.remove(new LineRangeBuilder().withStart(4).withEnd(5).build());

//        assertThat(last).containsExactly(new LineRange(0, 1), new LineRange(2, 3));
        assertThat(last).containsExactly(new LineRangeBuilder().withStart(0).withEnd(1).build(), new LineRangeBuilder().withStart(2).withEnd(3).build());

        LineRangeList middle = createThreeElements();
//        middle.remove(new LineRange(2, 3));
        middle.remove(new LineRangeBuilder().withStart(2).withEnd(3).build());

//        assertThat(middle).containsExactly(new LineRange(0, 1), new LineRange(4, 5));
        assertThat(middle).containsExactly(new LineRangeBuilder().withStart(0).withEnd(1).build(), new LineRangeBuilder().withStart(4).withEnd(5).build());

        LineRangeList first = createThreeElements();
//        assertThat(first.contains(new LineRange(0, 1))).isTrue();
//        assertThat(first.contains(new LineRange(2, 3))).isTrue();
//        assertThat(first.contains(new LineRange(4, 5))).isTrue();
        assertThat(first.contains(new LineRangeBuilder().withStart(0).withEnd(1).build())).isTrue();
        assertThat(first.contains(new LineRangeBuilder().withStart(2).withEnd(3).build())).isTrue();
        assertThat(first.contains(new LineRangeBuilder().withStart(4).withEnd(5).build())).isTrue();

//        first.remove(new LineRange(0, 1));
        first.remove(new LineRangeBuilder().withStart(0).withEnd(1).build());

//        assertThat(first).containsExactly(new LineRange(2, 3), new LineRange(4, 5));
        assertThat(first).containsExactly(new LineRangeBuilder().withStart(2).withEnd(3).build(), new LineRangeBuilder().withStart(4).withEnd(5).build());

//        assertThat(first.contains(new LineRange(2, 3))).isTrue();
        assertThat(first.contains(new LineRangeBuilder().withStart(2).withEnd(3).build())).isTrue();

//        assertThat(first.contains(new LineRange(0, 1))).isFalse();
        assertThat(first.contains(new LineRangeBuilder().withStart(0).withEnd(1).build())).isFalse();
    }

    private LineRangeList createThreeElements() {
        LineRangeList range = new LineRangeList();
//        range.add(new LineRange(0, 1));
//        range.add(new LineRange(2, 3));
//        range.add(new LineRange(4, 5));
        range.add(new LineRangeBuilder().withStart(0).withEnd(1).build());
        range.add(new LineRangeBuilder().withStart(2).withEnd(3).build());
        range.add(new LineRangeBuilder().withStart(4).withEnd(5).build());
        assertThat(range).containsExactly(new LineRange(0, 1), new LineRange(2, 3), new LineRange(4, 5));
        return range;
    }
}
