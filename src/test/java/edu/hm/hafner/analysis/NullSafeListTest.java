package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public abstract class NullSafeListTest extends ListTest {

    private final static int FIRST_ELEMENT = 0;

    @Test
    public void addNullElementToEmptyListThrows(){
        assertThatThrownBy(() -> create(0).add(null)).isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    public void addNullElementToInitializedListThrows(){
        assertThatThrownBy(() -> create(10).add(null)).isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    public void addNullElementAtIndexOneToInitializedListThrows(){
        assertThatThrownBy(() -> create(10).add(1, null)).isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    public void addMultipleElementsWithOneNullElementThrows(){
        List<Integer> list = new ArrayList<>();
        list.add(5);
        list.add(null);
        list.add(11);
        assertThatThrownBy(() -> create(0).addAll(list)).isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    public void addMultipleElementsWithNullElementAtIndexTwoThrows(){
        List<Integer> list = new ArrayList<>();
        list.add(5);
        list.add(null);
        list.add(11);
        assertThatThrownBy(() -> create(10).addAll(2, list)).isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    public void setElementToNullThrows(){
        assertThatThrownBy(() -> create(10).set(FIRST_ELEMENT, null)).isExactlyInstanceOf(NullPointerException.class);
    }
}
