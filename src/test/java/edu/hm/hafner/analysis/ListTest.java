package edu.hm.hafner.analysis;

import java.util.List;
import org.junit.jupiter.api.Test;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

public abstract class ListTest {
    abstract List<Integer> create(int numberOfInitialElements);

    @Test
    void zeroElementsIsEmpty(){
        List<Integer> list = create(0);
        assertThat(list).isEmpty();
        assertThat(list.isEmpty()).isTrue();
        assertThat(list).hasSize(0);
        assertThat(list.size()).isEqualTo(0);
    }

    @Test
    void removeOnlyElementResultsInEmpty(){
        List<Integer> list = create(1);
        list.remove(0);
        assertThat(list).isEmpty();
        assertThat(list.isEmpty()).isTrue();
        assertThat(list).hasSize(0);
        assertThat(list.size()).isEqualTo(0);
    }

    @Test
    void AddToEmpty(){
        List<Integer> list = create(0);
        Integer check = 42;

        assertThat(list).isEmpty();
        assertThat(list.isEmpty()).isTrue();
        assertThat(list).hasSize(0);
        assertThat(list.size()).isEqualTo(0);
        assertThat(list.contains(check)).isFalse();

        list.add(check);

        assertThat(list).isNotEmpty();
        assertThat(list.isEmpty()).isFalse();
        assertThat(list).hasSize(1);
        assertThat(list.size()).isEqualTo(1);
        assertThat(list.get(0)).isEqualTo(check);
        assertThat(list.contains(check)).isTrue();
    }

    @Test
    void removeFirstElement(){
        List<Integer> list = create(2);
        Integer removed = list.get(0);
        Integer should = list.get(1);

        list.remove(0);

        assertThat(list.get(0)).isNotEqualTo(removed);
        assertThat(list.get(0)).isEqualTo(should);
    }

    @Test
    void removeLastElement(){
        List<Integer> list = create(2);

        list.remove(1);

        assertThat(list.contains(1)).isFalse();
    }

    @Test
    void Contains(){
        List<Integer> list = create(3);
        Integer checkFront = list.get(0);
        Integer checkMiddle = list.get(1);
        Integer checkBack = list.get(2);

        assertThat(list.contains(checkFront)).isTrue();
        assertThat(list.contains(checkMiddle)).isTrue();
        assertThat(list.contains(checkBack)).isTrue();

        list.remove(checkFront);
        list.remove(checkMiddle);
        list.remove(checkBack);

        assertThat(list.contains(checkFront)).isFalse();
        assertThat(list.contains(checkMiddle)).isFalse();
        assertThat(list.contains(checkBack)).isFalse();

    }
}
