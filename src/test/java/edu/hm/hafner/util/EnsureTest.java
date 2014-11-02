package edu.hm.hafner.util;

import org.junit.Test;

import com.google.common.collect.Lists;

import static edu.hm.hafner.util.ExceptionTester.*;

/**
 * Tests the class {@link Ensure}.
 *
 * @author Ulli Hafner
 */
public class EnsureTest {
    private static final String SOME_STRING = "-";
    private static final String EMPTY_STRING = "";
    private static final String ERROR_MESSAGE = "Expected Error.";

    /**
     * Checks whether no exception is thrown if we adhere to all contracts.
     */
    @Test
    public void shouldNotThrowExceptionIfContractIsValid() {
        Ensure.that(false).isFalse();
        Ensure.that(true).isTrue();
        Ensure.that(EMPTY_STRING).isNotNull();
        Ensure.that(EMPTY_STRING, EMPTY_STRING).isNotNull();
        Ensure.that(null, (Object)null).isNull();
        Ensure.that(new String[]{EMPTY_STRING}).isNotEmpty();
        Ensure.that(SOME_STRING).isNotEmpty();
        Ensure.that(SOME_STRING).isNotBlank();
        Ensure.that(EMPTY_STRING).isInstanceOf(String.class);
    }

    /**
     * Checks whether we throw an exception if a contract is violated.
     */
    @Test
    public void shouldThrowExceptionIfContractIsViolated() {
        expect(() -> {
            Ensure.that(new IllegalArgumentException()).isNeverThrown(ERROR_MESSAGE);
        }).toThrow(AssertionError.class);
        expect(() -> {
            Ensure.that(true).isFalse();
        }).toThrow(AssertionError.class);
        expect(() -> {
            Ensure.that(true).isFalse(ERROR_MESSAGE);
        }).toThrow(AssertionError.class);
        expect(() -> {
            Ensure.that(false).isTrue();
        }).toThrow(AssertionError.class);
        expect(() -> {
            Ensure.that(false).isTrue(ERROR_MESSAGE);
        }).toThrow(AssertionError.class);
        expect(() -> {
            Ensure.thatStatementIsNeverReached();
        }).toThrow(AssertionError.class);
        expect(() -> {
            Ensure.thatStatementIsNeverReached(ERROR_MESSAGE);
        }).toThrow(AssertionError.class);
        expect(() -> {
            Ensure.that(SOME_STRING).isNull();
        }).toThrow(AssertionError.class);
        expect(() -> {
            Ensure.that(SOME_STRING).isNull(ERROR_MESSAGE);
        }).toThrow(AssertionError.class);
        expect(() -> {
            Ensure.that(SOME_STRING, SOME_STRING).isNull(ERROR_MESSAGE);
        }).toThrow(AssertionError.class);
    }

    /**
     * Checks whether we throw an exception if a contract is violated.
     */
    @Test
    public void shouldThrowNpeIfContractIsViolated() {
        expect(() -> {
            Ensure.that((Object)null).isNotNull(ERROR_MESSAGE);
        }).toThrow(NullPointerException.class);
        expect(() -> {
            Ensure.that(SOME_STRING, (Object)null).isNotNull(ERROR_MESSAGE);
        }).toThrow(NullPointerException.class);
        expect(() -> {
            Ensure.that((Object)null, SOME_STRING).isNotNull(ERROR_MESSAGE);
        }).toThrow(NullPointerException.class);
        expect(() -> {
            Ensure.that(null, null).isNotNull(ERROR_MESSAGE);
        }).toThrow(NullPointerException.class);
        expect(() -> {
            Ensure.that((Object)null).isNotNull();
        }).toThrow(NullPointerException.class);
        expect(() -> {
            Ensure.that(SOME_STRING, (Object)null).isNotNull();
        }).toThrow(NullPointerException.class);
        expect(() -> {
            Ensure.that((Object)null, SOME_STRING).isNotNull();
        }).toThrow(NullPointerException.class);
        expect(() -> {
            Ensure.that(null, null).isNotNull();
        }).toThrow(NullPointerException.class);
        expect(() -> {
            Ensure.that((Object[])null).isNotEmpty(ERROR_MESSAGE);
        }).toThrow(NullPointerException.class).withMessage(ERROR_MESSAGE);
        expect(() -> {
            Ensure.that((String)null).isNotEmpty(ERROR_MESSAGE);
        }).toThrow(NullPointerException.class);
    }

    /**
     * Checks whether we throw an exception if something is empty.
     */
    @Test
    public void shouldThrowExceptionIfEmpty() {
        expect(() -> {
            Ensure.that(new String[0]).isNotEmpty(ERROR_MESSAGE);
        }).toThrow(AssertionError.class);
        expect(() -> {
            Ensure.that(Lists.newArrayList(EMPTY_STRING, null, EMPTY_STRING)).isNotEmpty(ERROR_MESSAGE);
        }).toThrow(AssertionError.class);
        expect(() -> {
            Ensure.that(new String[]{EMPTY_STRING, null, EMPTY_STRING}).isNotEmpty(ERROR_MESSAGE);
        }).toThrow(AssertionError.class);
        expect(() -> {
            Ensure.that(EMPTY_STRING).isNotEmpty(ERROR_MESSAGE);
        }).toThrow(AssertionError.class);
        expect(() -> {
            Ensure.that(" ").isNotBlank(ERROR_MESSAGE);
        }).toThrow(AssertionError.class);
        expect(() -> {
            Ensure.that(EMPTY_STRING).isInstanceOf(Integer.class, ERROR_MESSAGE);
        }).toThrow(AssertionError.class);
    }

    /**
     * Verifies that the message format is correctly interpreted.
     */
    @Test
    public void shouldThrowExceptionWithCorrectMessage() {
        expect(() -> {
            Ensure.that(EMPTY_STRING).isInstanceOf(Integer.class, "This error uses '%s' to print the number %d.", "String.format", 42);
        }).toThrow(AssertionError.class).withMessage("This error uses 'String.format' to print the number 42.");
    }
}