package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests the class {@link ArrayList}.
 *
 * @author S. A. D.
 */
public class ArrayListTest extends ListTest {

    @Override
    List<Integer> create(final int numberOfInitialElements) {
        ArrayList<Integer> list = new ArrayList<>();

        for (int i = 0; i < numberOfInitialElements; i++) {
            list.add(i);
        }
        return list;
    }
}
