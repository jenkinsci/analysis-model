package edu.hm.hafner.analysis;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public abstract class ListTest {

    abstract protected List<Integer> create(int numberOfInitialElements);

    @Test
    void shouldBeEmpty() {
        assertThat(create(0).isEmpty()).isTrue();
    }

    @Test
    void shouldNotBeEmpty() {
        assertThat(create(1).isEmpty()).isFalse();
    }

    @Test
    void shouldHaveRightSize() {
        for(int i = 0; i < 10; i++) {
            assertThat(create(i).size()).isEqualTo(i);
        }
    }

    @Test
    void shouldAddAndRemoveItems() {
        List<Integer> sut = create(0);

        sut.add(1);
        assertThat(sut.contains(1)).isTrue();

        sut.remove(0);
        assertThat(sut.contains(1)).isFalse();
    }
}
