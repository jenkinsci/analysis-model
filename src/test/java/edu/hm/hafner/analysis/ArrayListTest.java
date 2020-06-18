package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ArrayListTest extends ListTest {

    @Override
    List<Integer> create(final int numberOfInitialElements) {
        ArrayList<Integer> list = new ArrayList<>();
        Stream.iterate(0, x -> x + 1).limit(numberOfInitialElements).forEach(list::add);
        return list;
    }

}
