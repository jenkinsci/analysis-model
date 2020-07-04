package edu.hm.hafner.analysis;

import java.util.List;
import static edu.hm.hafner.analysis.assertions.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Abstract test class for the {@code List} interface.
 */
public abstract class ListTest {

    /**
     * Global variable for the number zero.
     */
    protected static final int ZERO = 0;

    /**
     * Global variable for the number one.
     */
    protected static final int ONE = 1;


    public ListTest() {
    }

    protected abstract List<Integer> create(int numberOfInitialElements);

    //isEmpty, get, remove, size, add, contains

    // size()
    @Test
    void shouldHaveSizeZero() {
        final List<Integer> sut = create(ZERO);
        assertThat(sut.size()).isZero();
    }

    @Test
    void shouldHaveSizeOne() {
        final List<Integer> sut = create(ONE);
        assertThat(sut.size()).isOne();
    }

    // empty
    @Test
    void shouldBeEmpty() {
        final List<Integer> sut = create(ZERO);
        assertThat(sut.isEmpty()).isTrue();
    }

    @Test
    void shouldNotBeEmpty() {
        final List<Integer> sut = create(ONE);
        assertThat(sut.isEmpty()).isFalse();
    }

    // contains
    @Test
    void shouldNotContainBecauseNoElement() {
        final List<Integer> sut = create(ZERO);
        assertThat(sut.contains(ZERO)).isFalse();
    }

    // add
    @Test
    void shouldAddOneElementIncreaseSize() {
        final List<Integer> sut = create(ONE);
        sut.add(ONE);

        assertThat(sut.size()).isEqualTo(2);
        assertThat(sut.isEmpty()).isFalse();
    }

    @Test
    void shouldHaveOneElementAfterAddingToEmptyList() {
        final List<Integer> sut = create(ZERO);
        sut.add(ONE);

        assertThat(sut.size()).isOne();
        assertThat(sut.isEmpty()).isFalse();
    }

    // clear
    @Test
    void shouldBeEmptyAndSizeZeroAfterClear() {
        final List<Integer> sut = create(ONE);
        sut.clear();

        assertThat(sut.isEmpty()).isTrue();
        assertThat(sut.size()).isZero();
    }

    // equals
    @Test
    void shouldBeEqualBecauseSameObject() {
        final List<Integer> sut = create(ZERO);

        assertThat(sut.equals(sut)).isTrue();
    }

    @Test
    void shouldNotBeEqualBecauseFalseObjecttype() {
        final List<Integer> sut = create(ZERO);

        assertThat(sut.equals("Test")).isFalse();
    }

    @Test
    void shouldNotBeEqualBecauseDifferentElement() {
        final List<Integer> sut1 = create(ZERO);
        sut1.add(ONE);
        final List<Integer> sut2 = create(ZERO);
        sut2.add(ZERO);

        assertThat(sut1.equals(sut2)).isFalse();
    }

    //get
    @Test
    void shouldGetElementOne() {
        final List<Integer> sut = create(0);
        sut.add(ONE);

        assertThat(sut.get(0)).isOne();
    }

    @Test
    void shouldThrowIndexOutOfBoundsAfterGetOnEmptyList() {
        assertThatThrownBy(() -> create(ZERO)
                .get(0))
                .isInstanceOf(IndexOutOfBoundsException.class);
    }

    // remove
    @Test
    void shouldBeEmptyAfterRemove() {
        final List<Integer> sut = create(ONE);
        sut.remove(ZERO);

        assertThat(sut.isEmpty()).isTrue();
    }

    @Test
    void shouldNotBeEmptyAfterRemove() {
        final List<Integer> sut = create(ONE);
        sut.add(ZERO);
        sut.remove(ZERO);

        assertThat(sut.isEmpty()).isFalse();
        assertThat(sut.size()).isOne();
        assertThat(sut.get(0)).isZero();
    }

    @Test
    void shouldThrowIndexOutOfBoundsAfterRemoveOnEmptyList() {
        assertThatThrownBy(() -> create(ZERO)
                .remove(ZERO))
                .isInstanceOf(IndexOutOfBoundsException.class);
    }

    // combinations

    // isEmpty and size
    @Test
    void shouldBeEmptyAndSizeZero() {
        final List<Integer> sut = create(ZERO);
        assertThat(sut.isEmpty()).isTrue();
        assertThat(sut.size()).isZero();
    }

    @Test
    void shouldNotBeEmptyAndSizeOne() {
        final List<Integer> sut = create(ONE);
        assertThat(sut.isEmpty()).isFalse();
        assertThat(sut.size()).isOne();
    }

    // add, get, size and isEmpty
    @Test
    void shouldHaveElementOneAfterAddingToNoEmptyList() {
        final List<Integer> sut = create(ONE);
        sut.add(ONE);

        assertThat(sut.size()).isEqualTo(2);
        assertThat(sut.isEmpty()).isFalse();
        assertThat(sut.get(1)).isEqualTo(ONE);
    }

    @Test
    void shouldHaveElementOneAfterAddingToEmptyList() {
        final List<Integer> sut = create(ZERO);
        sut.add(ONE);

        assertThat(sut.size()).isOne();
        assertThat(sut.isEmpty()).isFalse();
        assertThat(sut.get(0)).isEqualTo(ONE);
    }
}
