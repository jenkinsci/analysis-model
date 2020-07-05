package edu.hm.hafner.analysis;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class NullSafeListTest extends ListTest {

    @Override
    List<Integer> create(final int numberOfInitialElements) {
        int[] s = new Random().ints(numberOfInitialElements, 0, 8).toArray();
        return new NullSafeList<>(Arrays.stream(s).boxed().collect(Collectors.toList()));
    }

    // https://www.codota.com/code/java/methods/org.assertj.core.api.Assertions/assertThatNullPointerException
    @Test
    void shouldThrowExceptionAdd() {
        assertThatNullPointerException().isThrownBy(() -> create(0).add(null));
    }

    @Test
    void shouldThrowExceptionCopy() {
        int[] s = new Random().ints(8, 0, 8).toArray();
        List<Integer> list = Arrays.stream(s).boxed().collect(Collectors.toList());
        list.add(null);
        assertThatNullPointerException().isThrownBy(() -> create(0).addAll(list));
    }

    @Test
    void shouldThrowExceptionSet() {
        assertThatNullPointerException().isThrownBy(() -> create(12).set(7, null));
    }

    @Test
    void shouldRejectNullInAddAllList() {
        int[] s = new Random().ints(5, 0, 8).toArray();
        List<Integer> list = Arrays.stream(s).boxed().collect(Collectors.toList());
        list.add(null);
        assertThatNullPointerException().isThrownBy(() -> create(2).addAll(5, list));

    }
}
