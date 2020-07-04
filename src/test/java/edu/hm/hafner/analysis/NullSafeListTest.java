package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests for the class {@code NullSafeList}.
 */
class NullSafeListTest extends ListTest {

    /**
     * Creates a new {@code ArrayList} wrapped in a {@code NullSafeList}.
     * @param numberOfInitialElements
     *          the amount of elements that should be stored in the list at the beginning
     * @return
     *      the NullSafeList as SUT
     */
    @Override
    protected List<Integer> create(final int numberOfInitialElements) {
        final List<Integer> sut = new NullSafeList(new ArrayList<>());
        for (int element = 1; element <= numberOfInitialElements; element++) {
            sut.add(element);
        }
        return sut;
    }

    @Test
    void shouldHaveAllElements() {
        final List<Integer> sut = create(ZERO);
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

        /**
         * Creates a new {@code NullSafeListInheritance} which wraps an {@code ArrayList}.
         * @param numberOfInitialElements
         *          the amount of elements that should be stored in the list at the beginning
         * @return
         *      the NullSafeListInheritance as SUT
         */
        @Override
        protected List<Integer> create(final int numberOfInitialElements) {
            final List<Integer> sut = new NullSafeListInheritance();
            for (int element = 1; element <= numberOfInitialElements; element++) {
                sut.add(element);
            }
            return sut;
        }
    }

    static class NullSafeCollectionsTest extends NullSafeListTest {
        /**
         * Creates a new {@code NullSafeList} which wraps an {@code ArrayList}.
         * @param numberOfInitialElements
         *          the amount of elements that should be stored in the list at the beginning
         * @return
         *      the NullSafeList as SUT
         */
        @Override
        protected List<Integer> create(final int numberOfInitialElements) {
            final List<Integer> sut = NullSafeCollections.nullSafeList(numberOfInitialElements);
            for (int element = 1; element <= numberOfInitialElements; element++) {
                sut.add(element);
            }
            return sut;
        }

        @Test
        void shouldCreateAnEmptyNullsafeArrayList() {
            final List<Integer> sut = NullSafeCollections.nullSafeList();

            assertThat(sut.isEmpty()).isTrue();
        }

        @Test
        void shouldCreateFilledNullsafeArrayList() {
            final ArrayList<Integer> tmpList = new ArrayList<>();
            for (int index = 0; index < 3; index++) {
                tmpList.add(index);
            }
            final List<Integer> sut = NullSafeCollections.nullSafeList(tmpList);

            assertThat(sut.size()).isEqualTo(3);
            assertThat(sut).containsExactly(0, 1, 2);
        }
    }
}
