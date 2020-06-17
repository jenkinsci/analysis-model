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
    void testAddNullToEmptyList(){
        List<Integer> list = create(0);

        assertThat(list).isEmpty();
        assertThat(list).hasSize(0);

        assertThatThrownBy( ()-> list.add(null))
                .isInstanceOf(NullPointerException.class);

    }

    @Test
    void testAddNull(){
        List<Integer> list = create(0);

        list.add(1);
        list.add(new Integer(2));
        assertThat(list.contains(1)).isTrue();
        assertThat(list.contains(2)).isTrue();

        assertThatThrownBy( ()-> list.add(null))
                .isInstanceOf(NullPointerException.class);

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

        Integer firstRemoved = list.remove(1);
        assertThat(list.contains(2)).isFalse();
        assertThat(list.contains(second)).isFalse();
        Integer secondRemoved = list.remove(0);

        assertThat(list.contains(first)).isFalse();
        assertThat(list.contains(second)).isFalse();

        assertThat(firstRemoved).isEqualTo(second);
        assertThat(secondRemoved).isEqualTo(first);
    }


   @Test
   void testFoo(){
       List<Integer> list = create(0);
       Integer first = new Integer(1);
       list.add(first);
       assertThat(list.contains(first)).isTrue();
       list.remove(0);
       assertThat(list.contains(1)).isFalse();
   }

}
