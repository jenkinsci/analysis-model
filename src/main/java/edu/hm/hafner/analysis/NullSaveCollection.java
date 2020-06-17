package edu.hm.hafner.analysis;
import java.util.List;


/**
 * NullSaveCollection is the Collection of all types nullSaveLists.
 *
 * @author Maier Leonhard
 */
public class NullSaveCollection {

    /**
     * Creates a new instance of NullSaveCollection.
     *
     * @return NullSaveList
     *
     */
    public static <E> List<E> nullSafeList() {
        return new NullSaveList<>();
    }

    /**
     * Creates a new instance of NullSaveCollection.
     *
     * @param l
     *      a new List
     *
     * @return NullSaveList
     *
     */
    public static <E> List<E> nullSaveList(final List<E> l) {
        return new NullSaveList<>(l);
    }


    /**
     * Creates a new instance of NullSaveCollection.
     *
     * @param intCap
     *      the capacity
     *
     * @return NullSaveArrayList
     *
     */
    public static <E> List<E> nullSafeList(final int intCap) {
        return new NullSaveArrayList<>(intCap);
    }
}
