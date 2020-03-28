package edu.hm.hafner.analysis;

import java.util.List;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

public abstract class ListTest {

    abstract List<Integer> create(int numberOfInitialElements);

    @Test
    public void checkIfEmptyAndSize() {
        assertThat(create(0).isEmpty()).isEqualTo(true);
        assertThat(create(1).isEmpty()).isEqualTo(false);
        assertThat(create(8).size()).isEqualTo(8);
    }

    @Test
    public void CheckAddRemove() {
        final List<Integer> sut = create(0);
        assertThat(sut.size()).isEqualTo(0);
        sut.add(12);
        assertThat(sut.size()).isEqualTo(1);
        sut.remove(Integer.valueOf(12));
        assertThat(sut.size()).isEqualTo(0);
    }

    @Test
    public void checkGetAndContains() {
        final List<Integer> sut = create(0);
        sut.add(11);
        assertThat(sut.get(0)).isEqualTo(11);
        assertThat(sut.contains(11)).isEqualTo(true);
        sut.remove(Integer.valueOf(11));
        assertThat(sut.contains(11)).isEqualTo(false);
    }
}
