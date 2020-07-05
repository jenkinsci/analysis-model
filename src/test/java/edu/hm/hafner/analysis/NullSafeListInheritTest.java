package edu.hm.hafner.analysis;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import sun.awt.windows.WPrinterJob;

public class NullSafeListInheritTest extends NullSafeListTest {

    @Override
    List<Integer> create(final int numberOfInitialElements) {
        int[] s = new Random().ints(numberOfInitialElements, 0, 8).toArray();
        System.out.println(Arrays.stream(s).boxed().collect(Collectors.toList()));
        return new NullSafeListInherit<>(Arrays.stream(s).boxed().collect(Collectors.toList()));

    }
}
