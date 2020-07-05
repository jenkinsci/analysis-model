package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class NullSafeCollection {

    public static <E> List<E> nullSafeList() {
        return new NullSafeList<>();
    }

    public static <E> List<E> listOf(E... elements) {
        List<E> innerList = Arrays.stream(elements)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return new NullSafeList<>(innerList);
    }

    public static <E> List<E> nullSafeList(final List<E> list) {
        return new NullSafeList<>(list.stream().filter(Objects::nonNull).collect(Collectors.toList()));
    }

}