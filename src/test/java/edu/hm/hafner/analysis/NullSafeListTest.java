package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

/**
 * Tests for {@link NullSafeList}.
 *
 * @author Daniel Soukup
 */
class NullSafeListTest extends ListTest {

    @Override
    public List<Integer> create(final int numberOfInitalElements) {
        final List<Integer> list = new ArrayList<>();
        for (int i = 0; i < numberOfInitalElements; i++) {
            list.add(i);
        }
        return new NullSafeList<>(list);
    }

    @Test
    void shouldThrowNPEAfterAddingNull() {
        List<Integer> sut = create(0);

        assertThatThrownBy(() -> sut.add(null))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowNPEAfterAddingACollectionWithNull() {
        List<Integer> sut = create(0);

        List<Integer> toBeAdded = new ArrayList<>();
        toBeAdded.add(null);

        assertThatThrownBy(() -> sut.addAll(toBeAdded))
                .isExactlyInstanceOf(NullPointerException.class);
    }

}
