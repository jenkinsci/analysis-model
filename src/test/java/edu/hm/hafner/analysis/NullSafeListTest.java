package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Implementation of the ListTest class for the NullSafeList.
 */
class NullSafeListTest extends ListTest {
    @Override
    public List<Integer> create(final int numberOfElements) {
        return NullSafeCollections.nullSafeList(Stream.iterate(0, x -> x + 1)
                .limit(numberOfElements)
                .collect(Collectors.toList()));
    }

    @Test
    void throwsWhenAddingNull() {
        List<Integer> sut = create(10);

        assertThatThrownBy(() -> sut.add(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void throwsWhenAddingNullWithIndex() {
        List<Integer> sut = create(20);

        assertThatThrownBy(() -> sut.add(10, null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void addWithIndexDelegates() {
        List<Integer> sut = create(20);
        sut.add(10, 1000);
        assertThat(sut).contains(1000);
        assertThat(sut.get(10)).isEqualTo(1000);
    }

    @Test
    void throwsWhenAddingListContainingNull() {
        List<Integer> sut = create(10);
        List<Integer> toAdd = Stream.iterate(10, x -> x + 1).limit(20).collect(Collectors.toList());
        toAdd.add(null);

        assertThatThrownBy(() -> sut.addAll(toAdd)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void throwsWhenAddingListWithIndexContainingNull() {
        List<Integer> sut = create(10);
        List<Integer> toAdd = Stream.iterate(10, x -> x + 1).limit(20).collect(Collectors.toList());
        toAdd.add(null);

        assertThatThrownBy(() -> sut.addAll(5, toAdd)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void addingListWorks() {
        List<Integer> sut = create(10);
        List<Integer> toAdd = Stream.iterate(10, x -> x + 1).limit(20).collect(Collectors.toList());

        sut.addAll(toAdd);
        assertThat(sut).size().isEqualTo(30);
        assertThat(sut).contains(11, 12, 13);
    }

    @Test
    void addingListWithIndexWorks() {
        List<Integer> sut = create(10);
        List<Integer> toAdd = Stream.iterate(10, x -> x + 1).limit(20).collect(Collectors.toList());

        sut.addAll(5, toAdd);
        assertThat(sut).size().isEqualTo(30);
        assertThat(sut.get(5)).isEqualTo(10);
    }

    @Test
    void throwsWhenSettingNull() {
        List<Integer> sut = create(10);
        assertThatThrownBy(() -> sut.set(5, null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void settingWorks() {
        List<Integer> sut = create(10);
        sut.set(5, 42);

        assertThat(sut.get(5)).isEqualTo(42);
    }
}
