package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;
import static edu.hm.hafner.analysis.assertions.Assertions.*;

public abstract class NullSafeListTest extends ListTest{
    @Test
    void BombOnAddNull(){
        List<Integer> list = create(0);

        assertThatThrownBy(() -> { list.add(null); })
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void BombOnAddAllNull(){
        List<Integer> list = create(0);

        assertThatThrownBy(() -> { list.addAll(null);})
                .isExactlyInstanceOf(NullPointerException.class);
    }


    @Test
    void BombOnAddAllIncludingNull(){
        List<Integer> list = create(0);
        Collection<Integer> a = Arrays.asList(1, null, 3);

        assertThatThrownBy(() -> { list.addAll(a);})
                .isExactlyInstanceOf(NullPointerException.class);
    }
}
