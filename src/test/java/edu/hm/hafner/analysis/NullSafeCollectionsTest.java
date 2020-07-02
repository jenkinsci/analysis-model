package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class NullSafeCollectionsTest extends NullSafeListTest {

    @Test
    void decoratorTest() {
        List<Integer> list = new ArrayList<>();
        List<Integer> sut = Collections.checkedList(
                Collections.synchronizedList(
                        NullSafeCollections.nullSafeList(list)), Integer.class);

        assertThatThrownBy(() -> sut.add(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Null is not an accepted value for an element.");
    }

}