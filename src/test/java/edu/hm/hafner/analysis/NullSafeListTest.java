package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

public class NullSafeListTest extends ListTest {

    @Override
    List<Integer> create(final int numberOfInitialElements) {
        List<Integer> list = new NullSafeList<>(new ArrayList<>());
        Stream.iterate(0, x -> x + 1).limit(numberOfInitialElements).forEach(list::add);
        return list;
    }

    @Test
    void addingNullException()    {
        List<Integer> sut = create(0);

        assertThatThrownBy(() -> sut.add(null)).isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void indexAddingNullException()    {
        List<Integer> sut = create(0);

        assertThatThrownBy(() -> sut.add(0, null)).isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void addAllCollectionNullException()    {
        List<Integer> sut = create(0);

        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(null);

        assertThatThrownBy(() -> sut.addAll(list)).isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void indexAddAllCollectionNullException()    {
        List<Integer> sut = create(2);

        assertThat(sut).hasSize(2);

        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(null);

        assertThatThrownBy(() -> sut.addAll(1, list)).isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void addAllNullException()    {
        List<Integer> sut = create(2);

        assertThat(sut).hasSize(2);

        assertThatThrownBy(() -> sut.addAll(null)).isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void indexAddAllNullException()    {
        List<Integer> sut = create(2);

        assertThat(sut).hasSize(2);

        assertThatThrownBy(() -> sut.addAll(1, null)).isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void setNullException()    {
        List<Integer> sut = create(0);

        assertThatThrownBy(() -> sut.set(0, null)).isExactlyInstanceOf(NullPointerException.class);
    }


}
