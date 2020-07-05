package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

/**
 * This class provides three factory methods for {@link NullSafeList}
 *
 * @author S. A. D.
 */
public class NullSafeCollections {

    /**
     * Creates a new instance of {@link NullSafeList}.
     */
    public static <E> List<E> emptyNullSafeList() {
        return new NullSafeList<>(new ArrayList<>());
    }

    /**
     * Creates a new instance of {@link NullSafeList} from a variable amount of elements.
     *
     * @param elements
     *         variable amount of elements
     */
    public static <E> List<E> listOf(final E... elements) {
        return nullSafeList(Arrays.asList(elements));
    }

    /**
     * Creates a new instance of {@link NullSafeList} from a {@link List}.
     */
    public static <E> List<E> nullSafeList(final List<E> list) {
        return new NullSafeList<>(list.stream().filter(Objects::nonNull).collect(Collectors.toList()));
    }

}