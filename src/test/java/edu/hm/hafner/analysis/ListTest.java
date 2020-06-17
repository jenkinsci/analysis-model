package edu.hm.hafner.analysis;

import java.util.List;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;


/**
 * ListTest.
 * @author DJCoding
 */
abstract class ListTest {

    /**
     * create method for a List.
     * @param numberOfInitialElements list size.
     * @return new List with x elements.
     */
    public abstract List<Integer> create(int numberOfInitialElements);

    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    @Test
    public void isEmpty() {
        assertThat(create(0).isEmpty());
    }
    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    @Test
    public void size() {
        assertThat(10 == create(10).size());
        assertThat(124 == create(124).size());
    }
    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    @Test
    public void remove() {
        List<Integer> sut = create(10);
        sut.add(5);
        sut.add(3);
        sut.add(9);
        assertThat(sut.contains(9));
        assertThat(sut.contains(5));
        assertThat(sut.contains(3));

        sut.remove(5);
        assertThat(!sut.contains(5));
    }
    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    @Test
    public void get() {
        List<Integer> sut = create(10);
        sut.set(1, 9);
        sut.set(0, 2);
        assertThat(sut.contains(9));
        assertThat(sut.contains(2));
        assertThat(sut.get(1) == 9);
    }
    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    @Test
    public void set() {
        List<Integer> sut = create(10);
        sut.set(1, 1);
        sut.set(0, 2);
        assertThat(sut.contains(1));
        assertThat(sut.contains(2));
        assertThat(sut.get(1) == 1);
    }
    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    @Test
    public void add() {
        List<Integer> sut = create(10);
        sut.add(5);
        sut.add(3);
        sut.add(9);
        assertThat(sut.contains(9));
        assertThat(sut.contains(5));
        assertThat(sut.contains(3));
    }
    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    @Test
    public void contains() {
        List<Integer> sut = create(10);
        sut.add(100);
        sut.add(142);
        sut.add(9752);
        assertThat(sut.contains(9752));
        assertThat(sut.contains(142));
        assertThat(sut.contains(100));

    }

    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    @Test
    public void clear() {
        List<Integer> sut = create(10);
        sut.add(1000);
        sut.add(14);
        sut.add(52);
        assertThat(sut.contains(1000));
        assertThat(sut.contains(14));
        assertThat(sut.contains(52));

        sut.clear();
        assertThat(sut.size() == 0);
    }

}