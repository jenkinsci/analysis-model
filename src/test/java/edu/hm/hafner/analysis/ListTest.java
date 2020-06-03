package edu.hm.hafner.analysis;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

/**
 *A Abstract Test Class for List.
 *
 * @author Maier Leonhard
 *
 */
abstract class ListTest {

    public abstract List<Integer> create(int numberOfInitalElements);

    @Test
    void isEmpty() {
        List<Integer> test = create(0);
        assertThat(test).hasSize(0);
        assertThat(test.isEmpty());
        test.add(0);
        assertThat(test.isEmpty());
        test.add(1);
        assertThat(!test.isEmpty());
        test.remove(0);
        assertThat(test.isEmpty());
    }

    @Test
    void size() {
        List<Integer> test = create(0);
        assertThat(test).hasSize(0);
        assertThat(test.size() == 0);
        test.add(1);
        assertThat(test.size() == 1);
    }

    @Test
    void remove() {
        List<Integer> test = create(10);
        test.add(20);
        assertThat(test.contains(20));
        test.remove(10);
        assertThat(!test.contains(20));
        assertThat(test).hasSize(10);
    }


    @Test
    void get() {
        List<Integer> test = create(5);
        assertThat(test).hasSize(5);
        test.add(77);
        test.add(6);
        test.add(55);
        test.add(4);
        assertThat(test.contains(77) && test.contains(6) && test.contains(55) && test.contains(4));
        assertThat(test.get(0)==77 && test.get(1)== 6 && test.get(2)==55 && test.get(3) == 4);
    }

    @Test
    void add() {
        List<Integer> test = create(5);
        assertThat(test).hasSize(5);
        test.add(77);
        test.add(6);
        test.add(55);
        test.add(4);
        assertThat(test.contains(77) && test.contains(6) && test.contains(55) && test.contains(4));
    }

    @Test
    void contain() {
        List<Integer> test = create(5);
        assertThat(test).hasSize(5);
        test.add(77);
        test.add(6);
        test.add(55);
        test.add(4);
        assertThat(test.contains(77) && test.contains(6) && test.contains(55) && test.contains(4));
        assertThat(test.contains(77)&&test.contains(6)&&test.contains(55)&&test.contains(4));
    }

    @Test
    void set() {
        List<Integer> test = create(5);
        assertThat(test).hasSize(5);
        test.add(77);
        test.add(6);
        test.add(55);
        test.add(4);
        assertThat(test.contains(77) && test.contains(6) && test.contains(55) && test.contains(4));
        test.set(1,66);
        test.set(3,44);
        assertThat(test.contains(77) && test.contains(66) && test.contains(55) && test.contains(44));
    }

}