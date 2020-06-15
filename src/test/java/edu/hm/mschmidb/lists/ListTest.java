package edu.hm.mschmidb.lists;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

public class ListTest {
    @Test
    void shouldBeEmpty() {
        assertThat(createList(0).isEmpty()).isTrue();
    }

    @Test
    void shouldNotBeEmpty() {
        assertThat(createList(1).isEmpty()).isFalse();
    }

    @Test
    void shouldHaveRightSize() {
        for(int i = 0; i < 10; i++) {
            assertThat(createList(i).size()).isEqualTo(i);
        }
    }

    @Test
    void shouldAddAndRemoveItems() {
        List<Integer> sut = createList(0);

        sut.add(1);
        assertThat(sut.contains(1)).isTrue();

        sut.remove(0);
        assertThat(sut.contains(1)).isFalse();
    }

    @Test
    void shouldAddAndRemoveCollection() {
        List<Integer> sut = createList(0);

        sut.addAll(new ArrayList<Integer>(){{add(1); add(2); add(3);}});
        assertThat(sut.size()).isEqualTo(3);
        assertThat(sut.contains(1)).isTrue();
        assertThat(sut.contains(2)).isTrue();
        assertThat(sut.contains(3)).isTrue();

        sut.removeAll(new ArrayList<Integer>(){{add(1); add(2); add(3);}});
        assertThat(sut.size()).isEqualTo(0);
        assertThat(sut.contains(1)).isFalse();
        assertThat(sut.contains(2)).isFalse();
        assertThat(sut.contains(3)).isFalse();
    }

    @Test
    void shouldNotAddNullItems() {
        List<Integer> sut = createList(0);

        assertThatNullPointerException().isThrownBy(() -> sut.add(null));
    }

    @Test
    void shouldNotAddNullCollection() {
        List<Integer> sut = createList(0);

        assertThatNullPointerException().isThrownBy(() -> sut.addAll(new ArrayList<Integer>(){{add(null);}}));
    }

    private List<Integer> createList(final int numInitialElements) {
        List<Integer> newList = new NullSafeListComp<Integer>();
        for (int i = 0; i < numInitialElements; i++) {
            newList.add(i);
        }
        return newList;
    }
}
