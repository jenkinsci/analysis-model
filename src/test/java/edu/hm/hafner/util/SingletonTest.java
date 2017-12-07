package edu.hm.hafner.util;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests the class {@link Singleton}.
 *
 * @author Ullrich Hafner
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
        assertThatThrownBy(() -> {
            Singleton.get(Sets.newHashSet());
        }).isInstanceOf(AssertionError.class);

        assertThatThrownBy(() -> {
            Singleton.get(Lists.newArrayList());
        }).isInstanceOf(AssertionError.class);
    }

    /**
     * Verifies that exception is thrown for collections of size > 1.
     */
    @Test
    public void shouldThrowExceptionForTooLargeCollections() {
        assertThatThrownBy(() -> {
            Singleton.get(asSet("1", "2"));
        }).isInstanceOf(AssertionError.class);
        assertThatThrownBy(() -> {
            Singleton.get(asList("1", "2"));
        }).isInstanceOf(AssertionError.class);
        assertThatThrownBy(() -> {
            Singleton.get(asList(1, 2, 3));
        }).isInstanceOf(AssertionError.class);
    }

    /**
     * Verifies the handling of null.
     */
    @Test
    @SuppressWarnings("ConstantConditions")
    public void shouldThrowExceptionWhenCollectionIsNullOrContainsNull() {
        assertThatThrownBy(() -> {
            Singleton.get(Sets.newHashSet(new Object[] {null}));
        }).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> {
            Singleton.get(null);
        }).isInstanceOf(NullPointerException.class);
    }

    private Iterable<Object> asSet(final Object... elements) {
        return Sets.newHashSet(elements);
    }

    private Iterable<Object> asList(final Object... elements) {
        return Lists.newArrayList(elements);
    }
}

