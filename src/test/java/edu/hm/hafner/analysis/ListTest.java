package edu.hm.hafner.analysis;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * ListTest.
 * @author Fabian Diener
 */

public abstract class ListTest {

    abstract List<Integer> create(int numberOfInitialElements);

    @Test
    public void listIsEmpty(){
        //arrange + act
        List<Integer> sut = create(0);
        //assert
        assertThat(sut.isEmpty()).isTrue();
        assertThat(sut.size()).isEqualTo(0);
    }

    @Test
    public void listIsntEmpty(){
        //arrange + act
        List<Integer> sut = create(4);
        //assert
        assertThat(sut.isEmpty()).isFalse();
        assertThat(sut.size()).isEqualTo(4);
    }

    @Test
    public void listFillAndClear(){
        //arrange
        List<Integer> sut = create(0);
        //act+assert
        assertThat(sut.isEmpty()).isTrue();
        assertThat(sut.size()).isEqualTo(0);

        sut.add(1);
        sut.add(2);
        sut.add(3);

        assertThat(sut.isEmpty()).isFalse();
        assertThat(sut.size()).isEqualTo(3);
        assertThat(sut.contains(1)).isTrue();
        assertThat(sut.contains(2)).isTrue();
        assertThat(sut.contains(3)).isTrue();

        sut.clear();

        assertThat(sut.isEmpty()).isTrue();
        assertThat(sut.size()).isEqualTo(0);
    }

    @Test
    public void listRemove(){
        //arrange
        List<Integer> sut = create(0);
        //act+assert
        assertThat(sut.isEmpty()).isTrue();
        assertThat(sut.size()).isEqualTo(0);

        sut.add(1);
        sut.add(2);
        sut.add(3);

        assertThat(sut.isEmpty()).isFalse();
        assertThat(sut.get(0).compareTo(1));
        assertThat(sut.get(1).compareTo(2));
        assertThat(sut.get(2).compareTo(3));

        sut.remove(2);
        sut.remove(1);
        sut.remove(0);

        assertThat(sut.contains(1)).isFalse();
        assertThat(sut.contains(2)).isFalse();
        assertThat(sut.contains(3)).isFalse();

        assertThat(sut.isEmpty()).isTrue();
        assertThat(sut.size()).isEqualTo(0);
    }


}
