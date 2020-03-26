package edu.hm.hafner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Unit tests for {@link ArrayList}.
 */
public class ArrayListTest extends ListTest {
    @Override
    protected List<Integer> create(final int numberOfInitialElements) {
        return Stream.iterate(0, n -> n + 1)
                .limit(numberOfInitialElements)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
