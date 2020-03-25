package edu.hm.hafner.analysis;

import java.util.List;

import org.junit.jupiter.api.Test;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

public abstract class ListTest {

    public abstract List<Integer> create(int numberOfInitalElements);

    @Test
    void shouldBeEmpty(){
        List<Integer> SUT = create(0);

        assertThat(SUT)
                .isEmpty();
    }

    @Test
    void shouldBeSizeOf5(){
        List<Integer> SUT = create(0);

        SUT.add(10);
        SUT.add(10);
        SUT.add(10);
        SUT.add(10);
        SUT.add(10);

        assertThat(SUT)
                .hasSize(5);
    }


    @Test
    void shouldBeEmptyAfterAddingAndRemovingTen(){
        List<Integer> SUT = create(0);

        SUT.add(10);
        SUT.remove(0);

        assertThat(SUT)
                .isEmpty();
    }

    @Test
    void shouldContain10(){
        List<Integer> SUT = create(0);

        SUT.add(10);
        SUT.add(10);
        SUT.remove(0);

        assertThat(SUT)
                .contains(10);
    }

    @Test
    void shouldBe5(){
        List<Integer> SUT = create(0);

        SUT.add(10);
        SUT.add(5);
        SUT.remove(0);

        assertThat(SUT.get(0))
                .isEqualTo(5);
    }

}
