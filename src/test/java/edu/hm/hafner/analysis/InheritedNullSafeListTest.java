package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class InheritedNullSafeListTest extends NullSafeListTest {

    @Override
    NullSafeArrayList<Integer> create(final int numberOfInitialElements) {
        NullSafeArrayList<Integer> list = new NullSafeArrayList<>(new ArrayList<>());
        // generate some "random" positive and negative values
        for (int element = -numberOfInitialElements; element < numberOfInitialElements; element += 2) {
            list.add(element * (int) (Math.random() * 10));
        }
        return list;
    }

    @Test
    public void newNullSafeArrayListWitchContainsNullThrows(){
        List<Integer> list = new ArrayList<>();
        list.add(5);
        list.add(null);
        list.add(11);
        assertThatThrownBy(() -> new NullSafeArrayList<>(list)).isExactlyInstanceOf(NullPointerException.class);
    }
}
