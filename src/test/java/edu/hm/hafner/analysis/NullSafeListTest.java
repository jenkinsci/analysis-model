package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.*;

/**
 * Test class for NullSafeLists
 *
 * @author Viet Phuoc Ho - (v.ho@hm.edu)
 */

class NullSafeListTest extends ListTest {
    @Test
    public void Costr(){
        ArrayList<Integer> nullList = new ArrayList<>();
        nullList.add(null);
        assertThatThrownBy(() -> new NullSafeList(nullList)).isInstanceOf(NullPointerException.class);
    }
    @Test
    public void addNull(){
        List<Integer> list = create(5);
        assertThatThrownBy(() -> list.add(null)).isInstanceOf(NullPointerException.class);
    }
    @Test
    public void addNullCollection(){
        List<Integer> list = create(5);
        assertThatThrownBy(() -> list.addAll(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void addNullCollection2(){
        List<Integer> list = create(5);
        assertThatThrownBy(() -> list.addAll(new ArrayList<Integer>(){{add(null);}})).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void replaceNull(){
        List<Integer> list = create(5);
        assertThatThrownBy(() -> list.replaceAll(i -> i == 0 ? null : i)).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void setNull(){
        List<Integer> list = create(5);
        assertThatThrownBy(() -> list.set(0,null)).isInstanceOf(NullPointerException.class);
    }


    @Override
    public List<Integer> create(final int numberOfInitialElements) {
        ArrayList<Integer> list = new ArrayList<>(numberOfInitialElements);
        for(int i = 0; i < numberOfInitialElements; i++){
            list.add(0);
        }

        return new NullSafeList<>(list);
    }
}