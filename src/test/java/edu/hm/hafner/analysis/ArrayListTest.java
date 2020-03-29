package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Tests the class {@link ArrayList}.
 *
 * @author budelmann
 */

class ArrayListTest extends ListTest {
    @Override
    protected List<Integer> create(final int numberOfInitialElements) {
        return Stream.iterate(0, i -> i+1).limit(numberOfInitialElements).collect(Collectors.toCollection(ArrayList::new));
    }
}
