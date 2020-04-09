package edu.hm.hafner.analysis;

import java.util.ArrayList;

/**
 * list decorator which extends directly from {@link ArrayList}
 * @author Simon Symhoven
 */
public class NullSafeList2<E> extends ArrayList {

    NullSafeList2() {
        super();
    }

    @Override
    public boolean add(Object element) {
        if (element != null) {
            return super.add(element);
        } else {
            throw new NullPointerException();
        }
    }
}
