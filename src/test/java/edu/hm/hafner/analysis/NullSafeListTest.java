package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Test for NullSafeList.
 */
class NullSafeListTest extends ListTest {

    @Override
    List<Integer> create(final int numberOfInitialElements) {
        return new NullSafeList<>(IntStream
                .generate(() -> new Random(100).nextInt())
                .limit(numberOfInitialElements)
                .boxed()
                .collect(Collectors.toCollection(ArrayList::new)));
    }

    @Test
    void addMethodShouldWork() {
        List<Integer> sut = create(0);

        assertThatThrownBy(() -> sut.add(null)).isInstanceOf(NullPointerException.class);
        assertThatCode(() -> sut.add(5)).doesNotThrowAnyException();
    }

    @Test
    void addAllMethodShouldWork() {
        List<Integer> sut = create(0);

        ArrayList<Integer> list = IntStream
                .generate(() -> new Random(100).nextInt())
                .limit(50)
                .boxed()
                .collect(Collectors.toCollection(ArrayList::new));

        assertThatCode(() -> sut.addAll(list)).doesNotThrowAnyException();
        assertThatThrownBy(() -> {
            list.add(null);
            sut.addAll(list);
        }).isInstanceOf(NullPointerException.class);
    }

    @Test
    void setMethodShouldWork() {
        List<Integer> sut = create(0);
        sut.add(43);

        assertThatCode(() -> sut.set(0, 42)).doesNotThrowAnyException();
        assertThatThrownBy(() -> sut.set(0, null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void addAtIndexShouldWork() {
        List<Integer> sut = create(0);

        assertThatCode(() -> sut.add(0, 42)).doesNotThrowAnyException();
        assertThatThrownBy(() -> sut.add(0, null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void addAllAtIndexShouldWork() {
        List<Integer> sut = create(0);
        sut.add(42);

        ArrayList<Integer> list = IntStream
                .generate(() -> new Random(100).nextInt())
                .limit(50)
                .boxed()
                .collect(Collectors.toCollection(ArrayList::new));

        assertThatCode(() -> sut.addAll(list)).doesNotThrowAnyException();
        assertThatThrownBy(() -> {
            list.add(null);
            sut.addAll(list);
        }).isInstanceOf(NullPointerException.class);
    }
}
