package edu.hm.hafner.analysis;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;


/**
 * Abstract class for testing.
 *
 * @author S. A. D.
 */
public abstract class ListTest {

    abstract List<Integer> create(int numberOfInitialElements);

    @Test
    void shouldBeEmpty() {
        List<Integer> list = create(0);
        assertThat(list.isEmpty()).isTrue();
    }

    @Test
    void shouldBeFilled() {
        List<Integer> list = create(7);
        assertThat(list.isEmpty()).isFalse();
    }

    @Test
    void shouldHaveSizeZero() {
        List<Integer> list = create(0);
        assertThat(list.size()).isEqualTo(0);
    }

    @Test
    void shouldHaveSizeSeven() {
        List<Integer> list = create(7);
        assertThat(list.size()).isEqualTo(7);
    }

    @Test
    void shouldRemoveValue() {
        List<Integer> list = create(7);
        int old = list.get(0);

        list.remove(0);
        assertThat(list.size()).isEqualTo(6);
        //test only works if initial values are all different
        assertThat(list.get(0)).isNotEqualTo(old);
    }

    @Test
    void shouldAddValues() {
        List<Integer> list = create(0);
        assertThat(list.size()).isEqualTo(0);

        int valueToaAdd = 42;

        list.add(valueToaAdd);
        assertThat(list.size()).isEqualTo(1);
        assertThat(list.get(0)).isEqualTo(valueToaAdd);
    }

    @Test
    void shouldGetValue() {
        List<Integer> list = create(0);
        list.add(42);
        assertThat(list.get(0)).isEqualTo(42);
    }

    @Test
    void shouldContainValue () {
        List<Integer> list = create(0);
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(6);

        assertThat(list.contains(4)).isTrue();
        assertThat(list.contains(6)).isTrue();
        assertThat(list.contains(9)).isFalse();
    }
}
