package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests the class {@link ArrayList}.
 *
 * @author Simon Smyhoven
 */

class ArrayListTest extends ListTest {

    @Override
    List<Integer> create(final int numberOfInitialElements) {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < numberOfInitialElements; i++) {
            list.add((int) Math.round(Math.random()));
        }
        return list;
    }

    /**
     * tests for decorator list class {@link NullSafeList}
     */
    static class NullSafeListTest {
        @Test
        void addNullShouldThrowNullPointer() {
            List<Integer> list = new NullSafeList<>(new ArrayList());
            Assertions.assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> list.add(null));
        }

        @Test
        void addIntegerShouldThrowNothing() {
            List<Integer> list = new NullSafeList<>(new ArrayList());
            Assertions.assertThatCode(() -> list.add(4))
                    .doesNotThrowAnyException();
            Assertions.assertThat(list).hasSize(1);
        }

        @Test
        void addNullWithIndexShouldThrowNullPointer() {
            List<Integer> list = new NullSafeList<>(new ArrayList());
            Assertions.assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> list.add(0,null));
        }

        @Test
        void addIntegerWithIndexShouldThrowNothing() {
            List<Integer> list = new NullSafeList<>(new ArrayList());
            Assertions.assertThatCode(() -> list.add(0,4))
                    .doesNotThrowAnyException();
            Assertions.assertThat(list).hasSize(1);
        }

        @Test
        void addCollectionWithNullSecondShouldThrowNullPointer() {
            Collection<Integer> col = new ArrayList<>();
            col.add(1);
            col.add(null);
            List<Integer> list = new NullSafeList<>(new ArrayList());
            Assertions.assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> list.addAll(col));
            Assertions.assertThat(list).hasSize(1);
        }

        @Test
        void addCollectionWithNullFirstShouldThrowNullPointer() {
            Collection<Integer> col = new ArrayList<>();
            col.add(null);
            col.add(1);
            List<Integer> list = new NullSafeList<>(new ArrayList());
            Assertions.assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> list.addAll(col));
            Assertions.assertThat(list).hasSize(0);
        }

        @Test
        void addCollecitonWithoutNullShouldThrowNothing() {
            Collection<Integer> col = new ArrayList<>();
            col.add(1);
            col.add(2);
            List<Integer> list = new NullSafeList<>(new ArrayList());
            Assertions.assertThatCode(() -> list.addAll(col))
                    .doesNotThrowAnyException();
            Assertions.assertThat(list).hasSize(2);
        }

        @Test
        void addCollectionWithNullSecondIndexShouldThrowNullPointer() {
            Collection<Integer> col = new ArrayList<>();
            col.add(1);
            col.add(null);
            List<Integer> list = new NullSafeList<>(new ArrayList());
            Assertions.assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> list.addAll(0, col));
            Assertions.assertThat(list).hasSize(1);
        }

        @Test
        void addCollectionWithNullFirstIndexShouldThrowNullPointer() {
            Collection<Integer> col = new ArrayList<>();
            col.add(null);
            col.add(1);
            List<Integer> list = new NullSafeList<>(new ArrayList());
            Assertions.assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> list.addAll(0, col));
            Assertions.assertThat(list).hasSize(0);
        }

        @Test
        void addCollecitonWithoutNullIndexShouldThrowNothing() {
            Collection<Integer> col = new ArrayList<>();
            col.add(1);
            col.add(2);
            List<Integer> list = new NullSafeList<>(new ArrayList());
            list.add(5);
            Assertions.assertThatCode(() -> list.addAll(1, col))
                    .doesNotThrowAnyException();
            Assertions.assertThat(list).hasSize(3);

            Assertions.assertThat(list.get(0)).isEqualTo(5);
            Assertions.assertThat(list.get(1)).isEqualTo(2);
            // Because 1 is insert first and then 2 with the same index as 1
            Assertions.assertThat(list.get(2)).isEqualTo(1);
        }

        @Test
        void addNullShouldThrowNullPointer2() {
            List<Integer> list = new NullSafeList2();
            Assertions.assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> list.add(null));
        }


        @Test
        void addIntegerShouldThrowNothing2() {
            List<Integer> list = new NullSafeList2();
            Assertions.assertThatCode(() -> list.add(4))
                    .doesNotThrowAnyException();
            Assertions.assertThat(list).hasSize(1);
        }

        @Test
        void collectionNullSafeListConstructor1() {
            List<Integer> list = new ArrayList<>();

            Collection a = Collections.checkedCollection(
                    Collections.synchronizedList(
                            NullSafeCollections.nullSafeList(list)), Integer.class);

            Assertions.assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> a.add(null));
        }

        @Test
        void collectionNullSafeListConstructor2() {
            Collection a = Collections.checkedCollection(
                    Collections.synchronizedList(
                            NullSafeCollections.nullSafeList()), Integer.class);

            Assertions.assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> a.add(null));
        }

        @Test
        void collectionNullSafeListConstructor3() {
            Collection c = new ArrayList();
            Collection a = Collections.checkedCollection(
                    Collections.synchronizedList(
                            NullSafeCollections.nullSafeList(c)), Integer.class);

            Assertions.assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> a.add(null));
        }
    }
}
