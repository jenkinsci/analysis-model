package edu.hm.hafner.util;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

/**
 * FIXME: write comment.
 *
 * @author Ullrich Hafner
 */
public class Integers {
    public static void shuffle(final int values[]) {
        Ensure.that(values).isNotNull();

        List<Integer> list = Arrays.asList(ArrayUtils.toObject(values));
        Collections.shuffle(list, new SecureRandom());
        int[] copy = ArrayUtils.toPrimitive(list.toArray(new Integer[values.length]));
        System.arraycopy(copy, 0, values, 0, values.length);
    }
}
