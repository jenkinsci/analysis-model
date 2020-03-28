package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests the class {@link ArrayList}.
 *
 * @author Simon Smyhoven
 */

class ArrayListTest extends ListTest {
    @Override
    List<Integer> create(final int numberOfInitialElements) {
        return new ArrayList(numberOfInitialElements);
    }
}
