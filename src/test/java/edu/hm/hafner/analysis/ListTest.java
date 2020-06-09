package edu.hm.hafner.analysis;

import java.util.List;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.assertions.SoftAssertions;

/**
 * Testklasse zur Ueberpruefung der Listen API.
 * @author Michael Schober
 */
public abstract class ListTest {

    abstract List<Integer> create(int numberOfInitialElements);

    @Test
    void checkIsEmpty(){
        List list = create(0);
        try (SoftAssertions softly = new SoftAssertions()) {
            softly.assertThat(list).isEmpty();
        }
    }

    @Test
    void checkAddSize(){
        List list = create(3);
        list.add(1);
        list.add(2);
        list.add(3);
        try (SoftAssertions softly = new SoftAssertions()) {
            softly.assertThat(list).hasSize(3).contains(1).contains(2).contains(3);
        }
    }

    @Test
    void checkRemoveSize(){
        List list = create(3);
        list.add(1);
        list.add(2);
        list.add(3);
        list.remove(2);
        try (SoftAssertions softly = new SoftAssertions()) {
            softly.assertThat(list).hasSize(2).contains(1).contains(2);
        }
    }

    @Test
    void checkGet(){
        List list = create(3);
        list.add(1);
        list.add(2);
        list.add(3);
        Integer have = (Integer)list.get(1);
        try (SoftAssertions softly = new SoftAssertions()) {
            softly.assertThat(have).isEqualTo(2);
        }
    }
}
