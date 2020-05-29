package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ArrayListTest extends ListTest{

    @Override
    List<Integer> create(final int numberOfInitialElements) {
        return Stream.iterate(0, i -> i+1)
                .limit(numberOfInitialElements)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Test
    public void listGetContains() {
        List<Integer> list = create(4);
        assertThat(list.contains(0)).isTrue();
        assertThat(list.contains(1)).isTrue();
        assertThat(list.contains(2)).isTrue();
        assertThat(list.contains(3)).isTrue();
        assertThat(list.contains(4)).isFalse();
        assertThat(list.get(0)).isEqualTo(0);
        assertThat(list.get(1)).isEqualTo(1);
        assertThat(list.get(2)).isEqualTo(2);
    }
}
