package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Tests for the class {@link ArrayList}.
 *
 * @author Daniel Soukup
 *
 */
class ArrayListTest extends ListTest {

    @Override
    public List<Integer> create(final int numberOfInitalElements) {
        return Stream.iterate(0, n -> n + 1).limit(numberOfInitalElements).collect(Collectors.toCollection(ArrayList::new));
    }
}
