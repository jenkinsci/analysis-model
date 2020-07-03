package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class NullSafeListTest extends ListTest {
    @Override
    protected List<Integer> create(final int numberOfInitialElements) {
        List<Integer> sut = new NullSafeList(new ArrayList<>());
        for (int element = 1; element <= numberOfInitialElements; element++) {
            sut.add(element);
        }
        return sut;
    }

    @Test
    void shouldHaveAllElements() {
        List<Integer> sut = create(ZERO);
        sut.add(1);
        sut.add(2);

        assertThat(sut).containsExactly(1, 2);
    }

    @Test
    void shouldThrowNullpointerAfterAddNull() {
        assertThatThrownBy(() -> create(ZERO)
                .add(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowNullpointerAfterAddNullWithIndex() {
        assertThatThrownBy(() -> create(ZERO)
                .add(0, null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowNullpointerAfterAddAllNull() {
        assertThatThrownBy(() -> create(ZERO)
                .addAll(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowNullpointerAfterAddAllNullWithIndex() {
        assertThatThrownBy(() -> create(ZERO)
                .addAll(0, null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowNullpointerAfterSetNull() {
        assertThatThrownBy(() -> create(ZERO)
                .set(0, null))
                .isInstanceOf(NullPointerException.class);
    }

    /**
     * Tests for the class {@code NullSafeListInheritance}.
     *
     * @author mbauerness
     */
    static class NullSafeListInheritanceTest extends NullSafeListTest {
        @Override
        protected List<Integer> create(final int numberOfInitialElements) {
            List<Integer> sut = new NullSafeListInheritance();
            for (int element = 1; element <= numberOfInitialElements; element++) {
                sut.add(element);
            }
            return sut;
        }
    }
}
