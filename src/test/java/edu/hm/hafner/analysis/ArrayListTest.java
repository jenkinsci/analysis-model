package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;

/**
 * Test for Lists
 *
 * @author Viet Phuoc Ho (v.ho@hm.edu)
 */
public class ArrayListTest extends ListTest{
    @Override
    List<Integer> create(final int numberOfInitialElements) {
        List<Integer> list = new ArrayList<>();
        for(int i = 0; i < numberOfInitialElements; i++){
            list.add(0);
        }
        return list;
    }
}
