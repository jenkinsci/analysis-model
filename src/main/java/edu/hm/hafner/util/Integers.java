package edu.hm.hafner.util;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.ArrayUtils;

/**
 * Integer utilities.
 *
 * @author Ullrich Hafner
 */
public final class Integers {

    /**
     * Shuffles the specified array values.
     *
     * @param values the array values to shuffle
     * @see Collections#shuffle(List)
     */
    public static void shuffle(final int[] values) {
        Ensure.that(values).isNotNull();

        List<Integer> list = Arrays.asList(ArrayUtils.toObject(values));
        Collections.shuffle(list, new SecureRandom());
        int[] copy = ArrayUtils.toPrimitive(list.toArray(new Integer[values.length]));
        System.arraycopy(copy, 0, values, 0, values.length);
    }

    /**
     * Reads an integer value from the console.
     *
     * @param message message presented to the user on the console
     * @return the integer value read
     */
    public static int read(final String message) {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                try {
                    System.out.println(message);

                    return scanner.nextInt();
                }
                catch (InputMismatchException exception) {
                    System.out.println("Dieser Text kann nicht in einen Integer umgewandelt werden, bitte nochmal versuchen");
                }
            }
        }
    }


    private Integers() {
        // prevents instantiation
    }
}
