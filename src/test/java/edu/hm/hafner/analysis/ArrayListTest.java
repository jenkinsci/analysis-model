package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;

/**
 * Testklasse zur Ueberpruefung der ArrayList API.
 * @author Michael Schober
 */
public class ArrayListTest extends ListTest {

    @Override
    List<Integer> create(final int numberOfInitialElements) {
        return new ArrayList<>(numberOfInitialElements);
    }
}
