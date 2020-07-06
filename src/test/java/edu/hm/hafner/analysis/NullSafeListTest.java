package edu.hm.hafner.analysis;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Test NullSafeList
 * @author Johannes Jäger
 */
public class NullSafeListTest extends ListTest {
    @Override
    List<Integer> create(final int numberOfInitialElements) {
        final List<Integer> list = new NullSafeList<>(new ArrayList<>(numberOfInitialElements));
        if (numberOfInitialElements > 0) {
            IntStream.range(0, numberOfInitialElements).forEach(value -> list.add(value));
        }
        return list;
    }
}
