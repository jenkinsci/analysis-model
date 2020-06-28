package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class NullSafeArrayListTest extends NullSafeListTest {

    @Override
    List<Integer> create(final int numberOfInitialElements) {
        ArrayList<Integer> arrayList = new NullSafeArrayList<>();
        Stream.iterate(0, x -> x + 1).limit(numberOfInitialElements).forEach(arrayList::add);
        return arrayList;
    }
}
