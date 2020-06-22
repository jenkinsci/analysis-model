package edu.hm.hafner.analysis;

import java.util.List;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 *
 * Abstract class with defined test cases for different list types
 * @author Matthias König
 */

public abstract class ListTest {
    abstract List<Integer> create(int numberOfInitialElements);

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
}