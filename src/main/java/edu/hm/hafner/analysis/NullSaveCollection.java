package edu.hm.hafner.analysis;
import java.util.List;

public class NullSaveCollection {

    public static <E> List<E> nullSafeList() {
        return new NullSaveList<>();
    }

    public static <E> List<E> nullSaveList(final List<E> l) {
        return new NullSaveList<>(l);
    }

    public static <E> List<E> nullSafeList(final int intCap) {
        return new NullSaveArrayList<>(intCap);
    }
}
