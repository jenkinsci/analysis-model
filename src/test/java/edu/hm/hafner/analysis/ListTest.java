package edu.hm.hafner.analysis;

import java.util.List;

import org.junit.jupiter.api.Test;
import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Abstract class for list test factory.
 */
abstract class ListTest {

    abstract List<Integer> create(int numberOfInitialElements);

    @Test
    void shouldBeEmpty() {
        List<Integer> sutList = this.create(0);
        assertThat(sutList).isEmpty();
    }

    @Test
    void sizeIsNumberOfInitialElements() {
        List<Integer> sutList = this.create(25);
        assertThat(sutList.size()).isEqualTo(25);
    }

    @Test
    void addWorks() {
        List<Integer> sutList = this.create(25);
        sutList.add(26);
        assertThat(sutList.size()).isEqualTo(26);
    }

    @Test
    void containsWorks() {
        List<Integer> sutList = this.create(25);
        sutList.add(26);
        assertThat(sutList.contains(26));
    }
}
