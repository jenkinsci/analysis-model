package edu.hm.hafner.analysis;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

public class NullSafeListTest extends ListTest{
    @Override
    protected List<Integer> create(final int numberOfInitialElements) {
        return null;
    }

    @Test
    void addSeperateValues() {
        List<Integer> sut = create(0);
        assertThat(sut).isEmpty();
        
    }

    @Test
    void addNullBomb() {
        List<Integer> sut = create(0);
        assertThatThrownBy(() -> sut.add(null)).isExactlyInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> sut.add(0, null)).isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void addAllCollectionContainingNullBomb() {
        List<Integer> sut = create(0);
        assertThatThrownBy(() -> sut.addAll(Arrays.asList(1, 2, null))).isExactlyInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> sut.addAll(0, Arrays.asList(1, 2, null))).isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void addNPEBeforeOutOfBoundsBomb() {
        List<Integer> sut = create(0);
        assertThatThrownBy(() -> sut.add(-1, 0)).isExactlyInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> sut.addAll(-1, Collections.emptyList())).isExactlyInstanceOf(IndexOutOfBoundsException.class);

        assertThatThrownBy(() -> sut.add(-1, null)).isExactlyInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> sut.addAll(-1, Arrays.asList(1, null))).isExactlyInstanceOf(NullPointerException.class);
    }

}
