package edu.hm.hafner.util;

import org.junit.Test;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests the class {@link Ensure}.
 *
 * @author Ullrich Hafner
 */
@SuppressWarnings("NullArgumentToVariableArgMethod")
public class EnsureTest {
    private static final String SOME_STRING = "-";
    private static final String EMPTY_STRING = "";
    private static final String ERROR_MESSAGE = "assertThatThrownBy Error.";

    /**
     * Checks whether no exception is thrown if we adhere to all contracts.
     */
    @Test
    public void shouldNotThrowExceptionIfContractIsValid() {
        assertThatCode(() -> {
            Ensure.that(false).isFalse();
            Ensure.that(true).isTrue();
            Ensure.that(EMPTY_STRING).isNotNull();
            Ensure.that(EMPTY_STRING, EMPTY_STRING).isNotNull();
            Ensure.that(null, (Object)null).isNull();
            Ensure.that(new String[]{EMPTY_STRING}).isNotEmpty();
            Ensure.that(SOME_STRING).isNotEmpty();
            Ensure.that(SOME_STRING).isNotBlank();
            Ensure.that(EMPTY_STRING).isInstanceOf(String.class);
            Ensure.that(ImmutableSet.of(EMPTY_STRING)).isNotEmpty();
            Ensure.that(ImmutableSet.of(EMPTY_STRING)).contains(EMPTY_STRING);
            Ensure.that(ImmutableSet.of(EMPTY_STRING)).doesNotContain(SOME_STRING);
        }).doesNotThrowAnyException();
    }

    /**
     * Checks whether we throw an exception if a contract is violated.
     */
    @Test
    @SuppressWarnings("Convert2MethodRef")
    public void shouldThrowExceptionIfContractIsViolated() {
        assertThatThrownBy(() -> {
            Ensure.that(new IllegalArgumentException(ERROR_MESSAGE)).isNeverThrown(ERROR_MESSAGE);
        }).isInstanceOf(AssertionError.class).hasMessage(ERROR_MESSAGE);
        assertThatThrownBy(() -> {
            Ensure.that(true).isFalse();
        }).isInstanceOf(AssertionError.class);
        assertThatThrownBy(() -> {
            Ensure.that(true).isFalse(ERROR_MESSAGE);
        }).isInstanceOf(AssertionError.class).hasMessage(ERROR_MESSAGE);
        assertThatThrownBy(() -> {
            Ensure.that(false).isTrue();
        }).isInstanceOf(AssertionError.class);
        assertThatThrownBy(() -> {
            Ensure.that(false).isTrue(ERROR_MESSAGE);
        }).isInstanceOf(AssertionError.class).hasMessage(ERROR_MESSAGE);
        assertThatThrownBy(() -> {
            Ensure.thatStatementIsNeverReached();
        }).isInstanceOf(AssertionError.class);
        assertThatThrownBy(() -> {
            Ensure.thatStatementIsNeverReached(ERROR_MESSAGE);
        }).isInstanceOf(AssertionError.class).hasMessage(ERROR_MESSAGE);
        assertThatThrownBy(() -> {
            Ensure.that(SOME_STRING).isNull();
        }).isInstanceOf(AssertionError.class);
        assertThatThrownBy(() -> {
            Ensure.that(SOME_STRING).isNull(ERROR_MESSAGE);
        }).isInstanceOf(AssertionError.class).hasMessage(ERROR_MESSAGE);
        assertThatThrownBy(() -> {
            Ensure.that(SOME_STRING, SOME_STRING).isNull(ERROR_MESSAGE);
        }).isInstanceOf(AssertionError.class).hasMessage(ERROR_MESSAGE);
        assertThatThrownBy(() -> {
            Ensure.that(ImmutableSet.of()).contains(EMPTY_STRING);
        }).isInstanceOf(AssertionError.class);
        assertThatThrownBy(() -> {
            Ensure.that(ImmutableSet.of(EMPTY_STRING)).contains(SOME_STRING);
        }).isInstanceOf(AssertionError.class);
        assertThatThrownBy(() -> {
            Ensure.that(ImmutableSet.of(EMPTY_STRING)).doesNotContain(EMPTY_STRING);
        }).isInstanceOf(AssertionError.class);
    }

    /**
     * Checks whether we throw an exception if a contract is violated.
     */
    @Test
    public void shouldThrowNpeIfContractIsViolated() {
        assertThatThrownBy(() -> {
            Ensure.that((Object)null).isNotNull(ERROR_MESSAGE);
        }).isInstanceOf(NullPointerException.class).hasMessage(ERROR_MESSAGE);
        assertThatThrownBy(() -> {
            Ensure.that(SOME_STRING, (Object)null).isNotNull(ERROR_MESSAGE);
        }).isInstanceOf(NullPointerException.class).hasMessage(ERROR_MESSAGE);
        assertThatThrownBy(() -> {
            Ensure.that(null, SOME_STRING).isNotNull(ERROR_MESSAGE);
        }).isInstanceOf(NullPointerException.class).hasMessage(ERROR_MESSAGE);
        assertThatThrownBy(() -> {
            Ensure.that(null, null).isNotNull(ERROR_MESSAGE);
        }).isInstanceOf(NullPointerException.class).hasMessage(ERROR_MESSAGE);
        assertThatThrownBy(() -> {
            Ensure.that((Object)null).isNotNull();
        }).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> {
            Ensure.that(SOME_STRING, (Object)null).isNotNull();
        }).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> {
            Ensure.that(null, SOME_STRING).isNotNull();
        }).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> {
            Ensure.that(null, null).isNotNull();
        }).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> {
            Ensure.that((Object[])null).isNotEmpty(ERROR_MESSAGE);
        }).isInstanceOf(NullPointerException.class).hasMessage(ERROR_MESSAGE);
        assertThatThrownBy(() -> {
            Ensure.that((String)null).isNotEmpty(ERROR_MESSAGE);
        }).isInstanceOf(NullPointerException.class).hasMessage(ERROR_MESSAGE);
        assertThatThrownBy(() -> {
            Ensure.that((Object[])null).isNotEmpty();
        }).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> {
            Ensure.that((String)null).isNotEmpty();
        }).isInstanceOf(NullPointerException.class);
    }

    /**
     * Checks whether we throw an exception if something is empty.
     */
    @Test
    public void shouldThrowExceptionIfEmpty() {
        assertThatThrownBy(() -> {
            Ensure.that(new String[0]).isNotEmpty(ERROR_MESSAGE);
        }).isInstanceOf(AssertionError.class);
        assertThatThrownBy(() -> {
            Ensure.that(Lists.newArrayList(EMPTY_STRING, null, EMPTY_STRING)).isNotEmpty(ERROR_MESSAGE);
        }).isInstanceOf(AssertionError.class);
        assertThatThrownBy(() -> {
            Ensure.that(new String[]{EMPTY_STRING, null, EMPTY_STRING}).isNotEmpty(ERROR_MESSAGE);
        }).isInstanceOf(AssertionError.class);
        assertThatThrownBy(() -> {
            Ensure.that(EMPTY_STRING).isNotEmpty(ERROR_MESSAGE);
        }).isInstanceOf(AssertionError.class);
        assertThatThrownBy(() -> {
            Ensure.that(" ").isNotBlank(ERROR_MESSAGE);
        }).isInstanceOf(AssertionError.class);
        assertThatThrownBy(() -> {
            Ensure.that(EMPTY_STRING).isInstanceOf(Integer.class, ERROR_MESSAGE);
        }).isInstanceOf(AssertionError.class);
        assertThatThrownBy(() -> {
            Ensure.that(new String[0]).isNotEmpty();
        }).isInstanceOf(AssertionError.class);
        assertThatThrownBy(() -> {
            Ensure.that(Lists.newArrayList(EMPTY_STRING, null, EMPTY_STRING)).isNotEmpty();
        }).isInstanceOf(AssertionError.class);
        assertThatThrownBy(() -> {
            Ensure.that(new String[]{EMPTY_STRING, null, EMPTY_STRING}).isNotEmpty();
        }).isInstanceOf(AssertionError.class);
        assertThatThrownBy(() -> {
            Ensure.that(EMPTY_STRING).isNotEmpty();
        }).isInstanceOf(AssertionError.class);
        assertThatThrownBy(() -> {
            Ensure.that(" ").isNotBlank();
        }).isInstanceOf(AssertionError.class);
        assertThatThrownBy(() -> {
            Ensure.that(EMPTY_STRING).isInstanceOf(Integer.class, ERROR_MESSAGE);
        }).isInstanceOf(AssertionError.class);
    }

    /**
     * Verifies that the message format is correctly interpreted.
     */
    @Test
    public void shouldThrowExceptionWithCorrectMessage() {
        assertThatThrownBy(() -> {
            Ensure.that(EMPTY_STRING).isInstanceOf(Integer.class, "This error uses '%s' to print the number %d.", "String.format", 42);
        }).isInstanceOf(AssertionError.class).hasMessage("This error uses 'String.format' to print the number 42.");
    }
}