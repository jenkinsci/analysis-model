package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;

public class ArrayListTest extends ListTest {

    private final int randomNumber = (int) Math.random();
    public final int NUMBER_OF_INTEGERS = randomNumber < 1_000? randomNumber : 1_000;

    @Override
    List<Integer> create(int numberOfInitialElements) {
        return new ArrayList<>(NUMBER_OF_INTEGERS);
    }
}
