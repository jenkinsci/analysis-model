package edu.hm.hafner;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ArrayListTest extends ListTest {

    @Override
    protected List<Integer> create(final int numberOfInitialElements) {
        List<Integer> sut = new ArrayList<>();
        for (int element = 1; element <= numberOfInitialElements; element++) {
            sut.add(element);
        }
        return sut;
    }

    @Test
    void shouldThrowIllegalArgumentOnNegativeInitialAmount() {
        assertThatThrownBy(() -> new ArrayList<Integer>(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }


}
