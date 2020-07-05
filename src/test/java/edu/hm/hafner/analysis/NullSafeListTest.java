package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class NullSafeListTest extends ListTest{
    @Override
    List<Integer> create(final int numberOfInitialElements) {
        List<Integer> list = new NullSafeList<>(new ArrayList<>());
        Stream.iterate(0, i -> i+1)
                .limit(numberOfInitialElements)
                .forEach(number -> list.add(number));
        return list;
    }

    //add
    @Test
    public void addIntegersNotNull() {
        List<Integer> sut = create(5);
        assertThat(sut).containsExactly(0,1,2,3,4);
    }

    @Test
    public void addNoIntegersButNull() {
        List<Integer> sut = create(0);
        assertThatThrownBy(() -> sut.add(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void addIntegersAndNull() {
        List<Integer> sut = create(5);
        assertThatThrownBy(() -> sut.add(null)).isInstanceOf(NullPointerException.class);
    }

    //addWithIndex

    @Test
    public void addIndexIntegersNotNull() {
        List<Integer> sut = create(4);
        sut.add(0, 5);
        assertThat(sut).containsExactly(5,0,1,2,3);
    }

    @Test
    public void addIndexNoIntegersButNull() {
        List<Integer> sut = create(0);
        assertThatThrownBy(() -> sut.add(0,null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void addIndexIntegersAndNull() {
        List<Integer> sut = create(4);
        assertThatThrownBy(() -> sut.add(0,null)).isInstanceOf(NullPointerException.class);
    }
    //addAll

    @Test
    public void addAllIntegersNotNull() {
        List<Integer> sut = create(0);
        List<Integer> added = create(3);
        sut.addAll(added);
        assertThat(sut).containsExactly(0,1,2);
    }

    @Test
    public void addAllNoIntegersButNull() {
        List<Integer> sut = create(0);
        assertThatThrownBy(() -> sut.addAll(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void addAllIntegersAndNull() {
        List<Integer> sut = create(3);
        assertThatThrownBy(() -> sut.addAll(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void addAllNullInAddedCollection() {
        List<Integer> sut = create(0);
        List<Integer> added = new ArrayList<>();
        added.add(1);
        added.add(null);
        assertThatThrownBy(() -> sut.addAll(added)).isInstanceOf(NullPointerException.class);
    }
    //addAllWithIndex

    @Test
    public void addAllIndexIntegersNotNull() {
        List<Integer> sut = create(2);
        List<Integer> added = create(3);
        sut.addAll(0, added);
        assertThat(sut).containsExactly(0,1,2,0,1);
    }

    @Test
    public void addAllIndexNoIntegersButNull() {
        List<Integer> sut = create(0);
        assertThatThrownBy(() -> sut.addAll(0,null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void addAllIndexIntegersAndNull() {
        List<Integer> sut = create(3);
        assertThatThrownBy(() -> sut.addAll(0,null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void addAllIndexNullInAddedCollection() {
        List<Integer> sut = create(0);
        List<Integer> added = new ArrayList<>();
        added.add(1);
        added.add(null);
        assertThatThrownBy(() -> sut.addAll(0,added)).isInstanceOf(NullPointerException.class);
    }
    //set

    @Test
    public void setReplaceWithInteger() {
        List<Integer> sut = create(2);
        sut.set(0,4);
        assertThat(sut).containsExactly(4,1);
    }

    @Test
    public void setReplaceWithNull() {
        List<Integer> sut = create(2);
        assertThatThrownBy(() -> sut.set(0,null)).isInstanceOf(NullPointerException.class);
    }

}
