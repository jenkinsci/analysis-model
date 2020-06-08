package edu.hm.hafner.analysis;

import java.util.List;
import org.junit.jupiter.api.Test;
import static edu.hm.hafner.analysis.assertions.Assertions.*;


public abstract class ListTest {

    abstract List<Integer> create(int numberOfInitialElements);

    @Test
    void testZeroElementsIsEmpty(){
        List<Integer> list = create(0);
        assertThat(list).isEmpty();
        assertThat(list).hasSize(0);
    }

    @Test
    void testAddToEmptyList(){
        List<Integer> list = create(0);

        assertThat(list).isEmpty();
        assertThat(list).hasSize(0);

        list.add(new Integer(1));

        assertThat(list.isEmpty()).isFalse();
        assertThat(list).hasSize(1);

        list.remove(0);
        assertThat(list).isEmpty();
        assertThat(list).hasSize(0);
    }

    @Test
    void testContainsCorrectElements(){
        List<Integer> list = create(0);
        Integer first = new Integer(1);
        Integer second= new Integer(2);

        list.add(first);
        list.add(second);

        assertThat(list.contains(first)).isTrue();
        assertThat(list.contains(second)).isTrue();

        Integer firstRemoved = list.remove(0);
        Integer secondRemoved = list.remove(1);

        assertThat(list.contains(first)).isFalse();
        assertThat(list.contains(second)).isFalse();

        assertThat(firstRemoved).isEqualTo(first);
        assertThat(secondRemoved).isEqualTo(second);
    }

}
