package edu.hm.hafner.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

class NullSafeArrayListTest extends ListTest {

    @SuppressWarnings("unused")
    private static Stream<Consumer<List<Integer>>> testSource() {
        return Stream.of(
                list -> list.add(null),
                list -> list.addAll(null),
                list -> list.addAll(getListContainingNull()),
                list -> list.addAll(5, getListContainingNull()),
                list -> list.replaceAll(i -> i == 0 ? null : i),
                list -> list.set(0, null));
    }

    @ParameterizedTest
    @MethodSource("testSource")
    void shouldThrowNPEOnNullInserted(final Consumer<List<Integer>> c) {
        List<Integer> sut = create(15);

        assertThatThrownBy(() -> c.accept(sut)).isInstanceOf(NullPointerException.class);
    }

    private static List<Integer> getListContainingNull() {
        List<Integer> listContainingNull = new ArrayList<>();
        listContainingNull.add(null);
        return listContainingNull;
    }

    @Override
    protected List<Integer> create(final int numberOfInitialElements) {
        List<Integer> list = new NullSafeArrayList<>();

        IntStream.range(0, numberOfInitialElements).forEach(list::add);

        return list;
    }
}
