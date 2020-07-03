package edu.hm.hafner.analysis;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Test class for java list
 * @author Matthias König
 */

public abstract class ListTest {
    abstract List<Integer> create(int numberOfInitialElements);

    /**
     * generic tests for a java list
     */
    @Test
    void ifListIsEmpty() {
        List<Integer> integerList = this.create(0);
        assertThat(integerList.isEmpty()).isTrue();
    }

    @Test
    void ifListIsNotEmpty() {
        List<Integer> integerList = this.create(10);
        assertThat(integerList.isEmpty()).isFalse();
    }

    @Test
    void numberOfElements() {
        List<Integer> integerList = this.create(20);
        assertThat(integerList.size()).isEqualTo(20);
    }

    @Test
    void removeElement() {
        List<Integer> integerList = this.create(15);
        assertThat(integerList.remove(5)).isEqualTo(5);
        assertThat(integerList.size()).isEqualTo(14);
    }

    @Test
    void getCorrectElement() {
        List<Integer> integerList = this.create(15);
        assertThat(integerList.get(6)).isEqualTo(6);
    }

    @Test
    void addElement() {
        List<Integer> integerList = this.create(15);
        integerList.add(5);
        assertThat(integerList.size()).isEqualTo(16);
    }

    @Test
    void elementExists() {
        List<Integer> integerList = this.create(15);
        assertThat(integerList.contains(5)).isTrue();
    }



    @Test
    void addOneAndRemoveTwoElements() {
        List<Integer> integerList = this.create(15);
        integerList.add(5);
        integerList.remove(2);
        integerList.remove(3);
        assertThat(integerList.size()).isEqualTo(14);
    }

    @Test
    void addAndRemoveElementsAndCheckContains() {
        List<Integer> integerList = this.create(8);
        integerList.add(5);
        integerList.add(2);
        integerList.remove(4);
        integerList.remove(3);
        assertThat(integerList.size()).isEqualTo(8);
        assertThat(integerList.contains(5)).isTrue();
        assertThat(integerList.contains(2)).isTrue();
        assertThat(integerList.contains(3)).isFalse();
        assertThat(integerList.contains(4)).isFalse();
    }

    @Test
    void removeAllDifferentValues() {
        List<Integer> integerList = this.create(8);
        List<Integer> integerListNew = this.create(5);
        integerList.removeAll(integerListNew);
        assertThat(integerList.isEmpty()).isFalse();
        assertThat(integerList.size()).isEqualTo(3);
        assertThat(integerList.contains(1)).isFalse();
        assertThat(integerList.contains(2)).isFalse();
        assertThat(integerList.contains(3)).isFalse();
        assertThat(integerList.contains(4)).isFalse();
        assertThat(integerList.contains(5)).isTrue();
        assertThat(integerList.contains(6)).isTrue();
        assertThat(integerList.contains(7)).isTrue();
        assertThat(integerList.contains(8)).isFalse();
    }

    @Test
    void removeAllSameValues() {
        List<Integer> integerList = this.create(8);
        List<Integer> integerListNew = this.create(8);
        assertThat(integerList.containsAll(integerListNew)).isTrue();
    }

    @Test
    void addAndRemoveDifferentValues() {
        List<Integer> integerList = this.create(10);
        integerList.add(5);
        integerList.add(9);
        integerList.add(11);
        integerList.remove(2);
        assertThat(integerList.isEmpty()).isFalse();
        assertThat(integerList.size()).isEqualTo(12);
        assertThat((integerList.contains(5))).isTrue();
        assertThat((integerList.contains(9))).isTrue();
        assertThat((integerList.contains(11))).isTrue();
        assertThat((integerList.contains(2))).isFalse();
        assertThat((integerList.get(2))).isEqualTo(3);
    }

    @Test
    void checkRightPosition() {
        List<Integer> integerList = this.create(10);
        integerList.add(1);
        integerList.add(5);
        integerList.remove(1);
        integerList.remove(2);
        integerList.remove(7);
        assertThat((integerList.get(1))).isEqualTo(2);
        assertThat((integerList.get(2))).isEqualTo(4);
        assertThat((integerList.get(7))).isEqualTo(1);
    }

    @Test
    void setNewValue() {
        List<Integer> integerList = this.create(5);
        integerList.set(2,5);
        assertThat((integerList.get(2))).isEqualTo(5);
    }

    @Test
    void addAndRemoveAndSetNewValue() {
        List<Integer> integerList = this.create(5);
        integerList.add(8);
        integerList.add(9);
        integerList.set(2,5);
        integerList.remove(5);

        assertThat(integerList.isEmpty()).isFalse();
        assertThat((integerList.size())).isEqualTo(6);
        assertThat((integerList.get(2))).isEqualTo(5);
        assertThat((integerList.get(4))).isEqualTo(4);
        assertThat((integerList.get(5))).isEqualTo(9);
    }

    /**
     * tests for {@link NullSafeList} and {@link NullSafeArrayList}
     */
    @Test
    void shouldThrowExceptionBecauseOfNullList() {
        List<Integer> list = null;
        assertThatThrownBy(() -> { throw new NullPointerException(); })
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldNotAddElementAndThrowException() {
        List<Integer> list = this.create(10);
        assertThatThrownBy(() -> list.add(null))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldNotAddElementAtIndexAndThrowException() {
        List<Integer> list = this.create(10);
        assertThatThrownBy(() -> list.add(1,null))
                .isExactlyInstanceOf(NullPointerException.class);
    }


    @Test
    void shouldNotAddCollectionAndThrowException() {
        List<Integer> list = this.create(10);
        List<Integer> nullList = null;
        assertThatThrownBy(() -> {
            assert nullList == null;
            list.addAll(nullList);
        })
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldNotAddCollectionAtIndexAndThrowException() {
        List<Integer> list = this.create(10);
        List<Integer> nullList = null;
        assertThatThrownBy(() -> {
            assert nullList == null;
            list.addAll(2,nullList);
        })
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldNotSetElementAndThrowException() {
        List<Integer> list = this.create(10);
        assertThatThrownBy(() -> list.set(3,null))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldNotInitializeListAndThrowException() {
        List<Integer> list = null;
        assertThatThrownBy(() -> new NullSafeList<>(list))
                .isExactlyInstanceOf(NullPointerException.class);
    }
}