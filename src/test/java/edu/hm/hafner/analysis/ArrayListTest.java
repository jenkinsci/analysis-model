package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Tests the class {@link ArrayList}.
 *
 * @author Simon Smyhoven
 */

class ArrayListTest extends ListTest {

    @Override
    List<Integer> create(final int numberOfInitialElements) {
        Random r = new Random();
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < numberOfInitialElements; i++) {
            list.add(r.nextInt());
        }
        return list;
    }
}
