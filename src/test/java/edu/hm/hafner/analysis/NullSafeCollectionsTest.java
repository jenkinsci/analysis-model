package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import edu.hm.hafner.analysis.NullSafeCollections;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link NullSafeCollections}.
 * @author Michael Schober, mschober@hm.edu
 */
class NullSafeCollectionsTest {

    private final NullSafeCollections nullSafeCollections = new NullSafeCollections();
    private final LinkedList linkedList = new LinkedList();
    private final ArrayList arrayList = new ArrayList();

    @Test
    void combinationWorksLinked(){
        Collections.checkedCollection(
                Collections.synchronizedList(
                        nullSafeCollections.nullSafeList(linkedList)), Integer.class);
    }

    @Test
    void combinationWorksArray(){
        Collections.checkedCollection(
                Collections.synchronizedList(
                        nullSafeCollections.nullSafeList(arrayList)), Integer.class);
    }
}
