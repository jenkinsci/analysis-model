package edu.hm.hafner.analysis;

import java.util.List;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

public abstract class ListTest {

    public static final int EMPTY = 0;
    public static final int ONE_ELEMENT = 1;
    public static final int MULTIPLE_ELEMENTS = 17;

    abstract List<Integer> create(int numberOfInitialElements);

    // isEmpty
    @Test
    void listWithZeroElementsIsEmpty(){
        List<Integer> emptyList = create(EMPTY);

        assertThat(emptyList.isEmpty()).isTrue();
    }

    @Test
    void listWithMultipleElementsIsNotEmpty(){
        List<Integer> nonEmptyList = create(MULTIPLE_ELEMENTS);

        assertThat(nonEmptyList.isEmpty()).isFalse();
    }

    // size
    @Test
    void listSizeEqualToNumberOfElements(){
        List<Integer> list = create(MULTIPLE_ELEMENTS);

        assertThat(list.size()).isEqualTo(MULTIPLE_ELEMENTS);
    }

    //remove
    @Test
    void removeOneElementFromListShouldHaveOneElementLess(){
        List<Integer> list = create(MULTIPLE_ELEMENTS);
        list.remove(ONE_ELEMENT);
        assertThat(list.size()).isEqualTo(MULTIPLE_ELEMENTS - ONE_ELEMENT);
    }

    @Test
    void removeOnlyElementFromListShouldLeadToEmptyList(){
        List<Integer> list = create(ONE_ELEMENT);
        list.remove(0);

        assertThat(list.isEmpty()).isTrue();
    }

    // get
    @Test
    void getOnlyElementShouldBeTheRemovedElement(){
        List<Integer> list = create(ONE_ELEMENT);
        Object getElement = list.get(0);
        Object removedElement = list.remove(0);

        assertThat(getElement).isEqualTo(removedElement);
    }

    @Test
    void getElementFromEmptyListBombs(){
        List<Integer> emptyList = create(EMPTY);
        assertThatThrownBy(() -> emptyList.get(0)).isExactlyInstanceOf(IndexOutOfBoundsException.class);
    }

    // add
    @Test
    void addOneElementToEmptyListIsNotEmptyAnymore(){
        List<Integer> emptyList = create(EMPTY);
        emptyList.add(create(ONE_ELEMENT).get(0));

        assertThat(emptyList.isEmpty()).isFalse();
    }

    @Test
    void addAndRemoveOneElementFromEmptyListIsStillAnEmptyList(){
        List<Integer> emptyList = create(EMPTY);
        emptyList.add(create(ONE_ELEMENT).get(0));
        emptyList.remove(0);

        assertThat(emptyList.isEmpty()).isTrue();
    }

    // contains
    @Test
    void listContainsAddedElement(){
        List<Integer> emptyList = create(EMPTY);
        Integer addedElement = create(ONE_ELEMENT).get(0);

        emptyList.add(addedElement);

        assertThat(emptyList.contains(addedElement)).isTrue();
    }

    @Test
    void emptyListDoesntContainAnything(){
        List<Integer> emptyList = create(EMPTY);

        assertThat(emptyList.contains(create(ONE_ELEMENT).get(0))).isFalse();
    }

    @Test
    void listShouldContainElementSeven(){
        List<Integer> emptyList = create(EMPTY);

        emptyList.add(Integer.valueOf(7));

        assertThat(emptyList.contains(Integer.valueOf(7))).isTrue();
    }
}