package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Test for NullSafeList as ArrayList.
 * @author DJCoding
 */
class NullSafeListTest extends ListTest {

    @Override
    public List<Integer> create(final int numberOfInitialElements) {
        List<Integer> list = new NullSafeList<>();
        for (int index = 0; index < numberOfInitialElements; index++) {
            list.add(index);
        }
        return list;
    }
    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    @Test
    public void addNull() {
        List<Integer> sut = create(5);
        assertThatThrownBy(() -> sut.add(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> sut.add(2, null)).isInstanceOf(NullPointerException.class);
    }
    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    @Test
    public void addInteger() {
        List<Integer> sut = create(5);
        sut.add(new Integer(0));
        assertThat(sut.contains(0));
    }
    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    @Test
    public void addAllNull() {
        List<Integer> sut = create(5);
        List<Integer> nullList = new ArrayList<>();
        nullList.add(null);
        assertThatThrownBy(() -> sut.addAll(nullList)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> sut.addAll(3, nullList)).isInstanceOf(NullPointerException.class);
    }
    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    @Test
    public void setNull() {
        List<Integer> sut = create(5);
        assertThatThrownBy(() -> sut.set(0, null)).isInstanceOf(NullPointerException.class);
    }
}
