package edu.hm.hafner.analysis;

import java.util.List;

import org.junit.Test;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

public abstract class ListTest {

    abstract List<Integer> create(int numberOfInitialElements);

    @Test
    public void listIsEmpty()  {
        assertThat(create(0).isEmpty()).isTrue();
    }

    @Test
    public void listSizeTest() {
        assertThat(create(10).size()).isEqualTo(10);
    }

    @Test
    public void listRemoveTest()   {
        List<Integer> sut = create(10);

        sut.remove(5);
        assertThat(sut.size()).isEqualTo(9);
    }

    @Test
    public void containsRemoveTest()  {
        List<Integer> sut = create(0);

        sut.add(5);
        assertThat(sut.get(0)).isEqualTo(5);

        assertThat(sut.contains(5)).isTrue();

        sut.remove(0);
        assertThat(sut.size()).isEqualTo(0);

    }
}
