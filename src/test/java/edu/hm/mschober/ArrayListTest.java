package edu.hm.mschober;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Michael Schober, mschober@hm.edu
 * @version 2020-31-03
 */

public class ArrayListTest extends ListTest {

    @Override
    List<Integer> create(final int numberOfInitialElements) {
        return new ArrayList(numberOfInitialElements);
    }
}
