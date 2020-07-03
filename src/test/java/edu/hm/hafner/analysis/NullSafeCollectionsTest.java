package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.jupiter.api.Test;

/**
 *
 * Test class for {@link NullSafeCollections}
 *
 * @author Matthias König
 */

class NullSafeCollectionsTest {

    private final NullSafeCollections nullSafeCollections = new NullSafeCollections();

    @Test
    void combinationIsWorking() {
        Collections.checkedCollection(
                Collections.synchronizedList(
                        nullSafeCollections.nullSafeList()), Integer.class);
    }

    @Test
    void combinationIsWorkingWithInput() {
        Collections.checkedCollection(
                Collections.synchronizedList(
                        nullSafeCollections.nullSafeListWithInput(new ArrayList<>())), Integer.class);
    }

    @Test
    void combinationIsWorkingWithCapacity() {
        Collections.checkedCollection(
                Collections.synchronizedList(
                        nullSafeCollections.nullSafeListWithCapacity(10)), Integer.class);
    }
}