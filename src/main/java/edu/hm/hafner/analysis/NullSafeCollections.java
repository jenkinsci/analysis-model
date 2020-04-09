package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Collection factory for NullSafeLists
 * @author Simon Symhoven
 */
class NullSafeCollections {

    /**
     * constructor with specified list type
     * @param list
     *              which should add to the null safe list
     * @return a null safe list of {@link NullSafeList}
     */
    static List nullSafeList(final List list) {
        return new NullSafeList<>(list);
    }

    /**
     * basic constructor
     * @return a null safe list of {@link NullSafeList}
     */
    static List nullSafeList() {
        return new NullSafeList<>(new ArrayList());
    }

    /**
     * constructor with collection to add
     * @param c
     *          Collection which sould add to the null safe list
     * @return  a null safe list of {@link NullSafeList}
     */
    static List nullSafeList(Collection c) {
        return new NullSafeList<>(new ArrayList(c));
    }
}
