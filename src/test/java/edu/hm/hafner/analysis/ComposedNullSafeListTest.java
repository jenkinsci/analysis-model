package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class ComposedNullSafeListTest extends NullSafeListTest {

    @Override
    NullSafeList<Integer> create(final int numberOfInitialElements) {
        NullSafeList<Integer> list = new NullSafeList<>();
        // generate some "random" positive and negative values
        for (int element = -numberOfInitialElements; element < numberOfInitialElements; element += 2) {
            list.add(element * (int) (Math.random() * 10));
        }
        return list;
    }

    @Test
    public void newNullSafeListWitchContainsNullThrows(){
        List<Integer> list = new ArrayList<>();
        list.add(5);
        list.add(null);
        list.add(11);
        assertThatThrownBy(() -> new NullSafeList<>(list)).isExactlyInstanceOf(NullPointerException.class);
    }
}
