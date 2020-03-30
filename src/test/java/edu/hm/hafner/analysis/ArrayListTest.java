package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Konkreter ListTest für ArrayList.
 */
public class ArrayListTest extends ListTest {

    @Override
    List<Integer> create(final int numberOfInitialElements) {
        return IntStream
                .generate(() -> new Random().nextInt(100))
                .limit(numberOfInitialElements)
                .boxed()
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
