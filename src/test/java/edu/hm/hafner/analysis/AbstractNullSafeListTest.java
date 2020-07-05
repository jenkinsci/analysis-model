package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Abstract class for testing different implementations of a NullSafeList.
 *
 * @author S. A. D.
 */
abstract class AbstractNullSafeListTest extends ListTest {
    @Override
    abstract List<Integer> create(int numberOfInitialElements);

    // https://www.codota.com/code/java/methods/org.assertj.core.api.Assertions/assertThatNullPointerException
    @Test
    void shouldThrowExceptionAdd() {
        assertThatNullPointerException().isThrownBy(() -> create(0).add(null));
    }

    @Test
    void shouldThrowExceptionCopy() {
        List<Integer> list = new ArrayList<>(create(8));
        list.add(null);
        assertThatNullPointerException().isThrownBy(() -> create(0).addAll(list));
    }

    @Test
    void shouldThrowExceptionSet() {
        assertThatNullPointerException().isThrownBy(() -> create(12).set(7, null));
    }

    @Test
    void shouldRejectNullInAddAllList() {
        List<Integer> list = new ArrayList<>(create(5));
        list.add(null);
        assertThatNullPointerException().isThrownBy(() -> create(2).addAll(5, list));

    }
}
