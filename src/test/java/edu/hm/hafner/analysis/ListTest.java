package edu.hm.hafner.analysis;

import java.util.List;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

/**
 * Abstract Test Pattern for List Implementations.
 *
 * @author budelmann
 */
abstract class ListTest {

    /**
     * Creates a concrete List in a subclass.
     *
     * @param numberOfInitialElements
     *          The number of Initial Elements for the List.
     * @return a new List with Integer.
     */
    protected abstract List<Integer> create(int numberOfInitialElements);

    @Test
    public void checkIfEmptyAndSize() {
        assertThat(create(0).isEmpty());
        assertThat(create(1)).isNotEmpty();
        assertThat(create(8)).hasSize(8);
    }

    @Test
    public void CheckAddRemove() {
        final List<Integer> sut = create(0);
        assertThat(sut).hasSize(0);
        sut.add(12);
        assertThat(sut).hasSize(1);
        sut.remove(Integer.valueOf(12));
        assertThat(sut).hasSize(0);
    }

    @Test
    public void checkGetAndContains() {
        final List<Integer> sut = create(0);
        sut.add(11);
        assertThat(sut).isNotEmpty();
        assertThat(sut).containsOnlyOnce(11);
        sut.remove(Integer.valueOf(11));
        assertThat(sut).doesNotContain(11);
    }
}

