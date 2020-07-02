package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import edu.hm.hafner.analysis.NullSafeCollections;
import edu.hm.hafner.analysis.assertions.SoftAssertions;

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
    void sameListListInsert(){
        final NullSafeList nullSafeList = new NullSafeList(linkedList);
        final NullSafeList have = nullSafeCollections.nullSafeList(linkedList);
        try (SoftAssertions softly = new SoftAssertions()) {
            softly.assertThat(have).isEqualTo(nullSafeList);
        }
    }

    @Test
    void sameListListNoneInsert(){
        final NullSafeList nullSafeList = new NullSafeList(arrayList);
        final NullSafeList have = nullSafeCollections.nullSafeList();
        try (SoftAssertions softly = new SoftAssertions()) {
            softly.assertThat(have).isEqualTo(nullSafeList);
        }
    }

    @Test
    void sameListSizeInsert(){
        final NullSafeList nullSafeList = new NullSafeList(new ArrayList<Integer>(4));
        final NullSafeList have = nullSafeCollections.nullSafeList(4);
        try (SoftAssertions softly = new SoftAssertions()) {
            softly.assertThat(have).isEqualTo(nullSafeList);
        }
    }

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
