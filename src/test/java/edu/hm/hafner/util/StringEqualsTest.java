package edu.hm.hafner.util;

/**
 * Example class that shows on how to verify that String instances comply with the contract in {@link Object#equals(Object)}.
 *
 * @author Ullrich Hafner
 */
public class StringEqualsTest extends AbstractEqualsTest {
    @Override
    protected Object createSut() {
        return "Hello World";
    }
}
