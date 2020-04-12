package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

/**
 * Abstract Test Pattern fuer Null-Safe List Implementierungen.
 *
 * @author budelmann
 */
public abstract class NullSafeListTest extends ListTest{

    @Test
    void addSeperateValues() {
        List<Integer> sut = create(0);
        Integer first = 1;
        Integer second = 2;
        assertThat(sut).isEmpty();
        sut.add(first);
        sut.add(1, second);
        assertThat(sut).isNotEmpty();
        assertThat(sut).containsOnly(first, second);
    }

    @Test
    void addAllCollections() {
        List<Integer> sut = create(0);
        List<Integer> coll = Arrays.asList(1,2,3);

        sut.addAll(coll);
        assertThat(sut).hasSize(3);
        assertThat(sut).containsExactly(1,2,3);
        sut.clear();
        assertThat(sut).isEmpty();

        sut.add(0, 4);
        sut.addAll(1, coll);
        assertThat(sut).containsExactly(4,1,2,3);
    }

    @Test
    void setTestAndBomb() {
        List<Integer> sut = create(0);
        List<Integer> coll = Arrays.asList(1,2,3);

        sut.addAll(coll);
        sut.set(1,4);
        assertThat(sut).containsExactly(1,4,3);
        assertThatThrownBy(() -> sut.set(0, null)).isExactlyInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> sut.set(-1, null)).isExactlyInstanceOf(NullPointerException.class);
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


    /**
     * Tests the class {@link NullSafeList}.
     *
     * @author budelmann
     */
    public static class NullSafeDecoratorTest extends NullSafeListTest {
        @Override
        protected List<Integer> create(final int numberOfInitialElements) {
            return new NullSafeList<>(new ArrayList<>(Stream.iterate(0, t -> t+1).limit(numberOfInitialElements).collect(
                    Collectors.toList())));
        }
    }


    /**
     * Tests the class {@link NullSafeListInheritance}.
     *
     * @author budelmann
     */
    public static class NullSafeInheritanceTest extends NullSafeListTest {
        @Override
        protected List<Integer> create(final int numberOfInitialElements) {
            List<Integer> list = new NullSafeListInheritance<>();
            list.addAll(Stream.iterate(0, t -> t+1).limit(numberOfInitialElements).collect(
                    Collectors.toList()));
            return list;
        }
    }
}
