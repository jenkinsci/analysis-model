package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class NullSafeListTest extends ListTest {

    private final static int FIRST_ELEMENT = 0;

    @Override
    List<Integer> create(final int numberOfInitialElements) {
        List<Integer> list = new ArrayList<>(numberOfInitialElements);
        // generate some "random" positiv and negative values
        for (int element = -numberOfInitialElements; element < numberOfInitialElements; element += 2) {
            list.add(element * (int) (Math.random() * 10));
        }
        return list;
    }

    @Test
    public void addNullElementToEmptyListThrows(){
        assertThatThrownBy(() -> getEmptyNullSafeList().add(null)).isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    public void addNullElementToInitializedListThrows(){
        assertThatThrownBy(() -> getInitializedNullSafeList().add(null)).isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    public void addMultipleElementsWithOneNullElementThrows(){
        List list = new ArrayList<Integer>();
        list.add(5);
        list.add(null);
        list.add(11);
        assertThatThrownBy(() -> getEmptyNullSafeList().addAll(list)).isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    public void setElementToNullThrows(){
        assertThatThrownBy(() -> getInitializedNullSafeList().set(FIRST_ELEMENT, null)).isExactlyInstanceOf(NullPointerException.class);
    }


    private NullSafeList getEmptyNullSafeList(){
        return new NullSafeList();
    }

    private NullSafeList getInitializedNullSafeList(){
        NullSafeList nullSafeList = new NullSafeList();
        nullSafeList.add(-10);
        nullSafeList.add(0);
        nullSafeList.add(11);
        nullSafeList.add(Integer.MAX_VALUE);
        return nullSafeList;
    }

}
