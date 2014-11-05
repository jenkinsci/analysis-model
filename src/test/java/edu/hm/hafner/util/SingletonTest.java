package edu.hm.hafner.util;

import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import static edu.hm.hafner.util.ExceptionTester.*;
import static org.assertj.core.api.Assertions.*;

/**
 * Tests the class {@link Singleton}.
 *
 * @author Ulli Hafner
 */
public class SingletonTest {
    /**
     * Verifies that {@link Singleton#get(Iterable)} returns the element of a collection of size 1.
     */
    @Test
    public void shouldGetElementOfCollectionOfSize1() {
        String string = "String";

        assertThat(Singleton.get(asSet(string))).isEqualTo(string);
        assertThat(Singleton.get(asList(string))).isEqualTo(string);

        Integer integer = 42;

        assertThat(Singleton.get(asSet(integer))).isEqualTo(integer);
        assertThat(Singleton.get(asList(integer))).isEqualTo(integer);
    }

    /**
     * Verifies that exception is thrown for empty collections.
     */
    @Test
    public void shouldThrowExceptionForEmptyCollections() {
        expect(() -> {
            Singleton.get(Sets.newHashSet());
        }).toThrow(AssertionError.class);

        expect(() -> {
            Singleton.get(Lists.newArrayList());
        }).toThrow(AssertionError.class);
    }

    /**
     * Verifies that exception is thrown for collections of size > 1.
     */
    @Test
    public void shouldThrowExceptionForTooLargeCollections() {
        expect(() -> {
            Singleton.get(asSet("1", "2"));
        }).toThrow(AssertionError.class);
        expect(() -> {
            Singleton.get(asList("1", "2"));
        }).toThrow(AssertionError.class);
        expect(() -> {
            Singleton.get(asList(1, 2, 3));
        }).toThrow(AssertionError.class);
    }

    /**
     * Verifies the handling of null.
     */
    @Test
    @SuppressWarnings("NullArgumentToVariableArgMethod")
    public void shouldThrowExceptionWhenCollectionIsNullOrContainsNull() {
        expect(() -> {
            Singleton.get(asSet(null));
        }).toThrow(NullPointerException.class);
        expect(() -> {
            Singleton.get(null);
        }).toThrow(NullPointerException.class);
    }

    private Set<Object> asSet(final Object... elements) {
        return Sets.newHashSet(elements);
    }

    private List<Object> asList(final Object... elements) {
        return Lists.newArrayList(elements);
    }
}

