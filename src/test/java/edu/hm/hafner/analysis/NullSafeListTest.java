package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class NullSafeListTest extends ListTest {

    @Override
        public List<Integer> create(final int numberOfInitalElements) {
            ArrayList<Integer> test = new ArrayList<>(numberOfInitalElements);
            for (int index = 0; index < numberOfInitalElements; index++){
                test.add(index);
            }
            return new NullSaveList<>(test);
}

    @Test
    void addNull() {
        List<Integer> test = create(10);
        assertThatThrownBy(() -> test.add(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void NullColection() {
        List<Integer> test = create(10);
        assertThatThrownBy(() -> test.addAll(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void NullConstr() {
        ArrayList test = new ArrayList();
        test.add(null);

        assertThatThrownBy(() -> new NullSaveList<>(test)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void NullReplace() {
        List<Integer> test = create(10);
        assertThatThrownBy(() -> test.replaceAll(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void NullRetain() {
        List<Integer> test = create(10);
        assertThatThrownBy(() -> test.retainAll(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void NullAddAll() {
        List<Integer> test = create(10);
        assertThatThrownBy(() -> test.addAll(null)).isInstanceOf(NullPointerException.class);
    }


    @Test
    void NullAddAllI() {
        List<Integer> test = create(10);
        assertThatThrownBy(() -> test.add(1,null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void NullRemoveAll() {
        List<Integer> test = create(10);
        assertThatThrownBy(() -> test.removeAll(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void NullRetainAll() {
        List<Integer> test = create(10);
        assertThatThrownBy(() -> test.retainAll(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void NullRaplaceAll() {
        List<Integer> test = create(10);
        assertThatThrownBy(() -> test.replaceAll(null)).isInstanceOf(NullPointerException.class);
    }


    @Test
    void NullSet() {
        List<Integer> test = create(10);
        assertThatThrownBy(() -> test.set(1,null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void NullAddI() {
        List<Integer> test = create(10);
        assertThatThrownBy(() -> test.add(1,null)).isInstanceOf(NullPointerException.class);
    }


}

