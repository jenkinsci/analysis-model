package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.assertions.SoftAssertions;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for {@link NullSafeListInheritance}.
 * @author Michael Schober, mschober@hm.edu
 */
class NullSafeListInheritanceTest {


    private List<Integer> create(int numberOfElement){
        return new NullSafeListInheritance<>(numberOfElement);
    }

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

    @Test
    void addIndex(){
        List list = create(3);
        list.add(0,1);
        list.add(0,2);
        list.add(0,3);
        Integer have = (Integer)list.get(1);
        try (SoftAssertions softly = new SoftAssertions()) {
            softly.assertThat(list).hasSize(3).contains(1).contains(2).contains(3);
        }
    }

    @Test
    void addCollection(){
        List list = create(3);
        List toAdd = new ArrayList();
        toAdd.add(2);
        toAdd.add(3);
        list.addAll(toAdd);
        try (SoftAssertions softly = new SoftAssertions()) {
            softly.assertThat(list).hasSize(2).contains(2).contains(3);
        }
    }

    @Test
    void addCollectionIndex(){
        List list = create(3);
        List toAdd = new ArrayList();
        toAdd.add(2);
        toAdd.add(3);
        list.addAll(0,toAdd);
        try (SoftAssertions softly = new SoftAssertions()) {
            softly.assertThat(list).hasSize(2).contains(2).contains(3);
        }
    }
    @Test
    void addNullBombs(){
        List list = create(3);
        assertThatThrownBy(
                () -> list.add(null)
        ).isInstanceOf(NullPointerException.class);
    }

    @Test
    void addIndexNullBombs(){
        List list = create(3);
        assertThatThrownBy(
                () -> list.add(0, null)
        ).isInstanceOf(NullPointerException.class);
    }

    @Test
    void addNullListBombs(){
        List list = create(3);
        assertThatThrownBy(
                () -> list.addAll(null)
        ).isInstanceOf(NullPointerException.class);
    }

    @Test
    void addListOfNullBombs(){
        List list = create(3);
        List toAdd = new ArrayList();
        toAdd.add(null);
        assertThatThrownBy(
                () -> list.addAll(toAdd)
        ).isInstanceOf(NullPointerException.class);
    }

    @Test
    void addIndexListOfNullBombs(){
        List list = create(3);
        List toAdd = new ArrayList();
        toAdd.add(null);
        assertThatThrownBy(
                () -> list.addAll(0, toAdd)
        ).isInstanceOf(NullPointerException.class);
    }
}
