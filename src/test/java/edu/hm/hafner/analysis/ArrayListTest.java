package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ArrayListTest extends ListTest {
    @Override
    List<Integer> create(final int numberOfInitialElements) {
        return new ArrayList<>(Stream.iterate(0, i -> i+1).limit(numberOfInitialElements).collect(Collectors.toCollection(ArrayList::new)));
    }
}
