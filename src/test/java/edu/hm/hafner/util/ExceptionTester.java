package edu.hm.hafner.util;

import org.junit.Assert;

/**
 * Provides static method to verify that a method throws an exception. <br/> Inspired by the recipe in "Functional
 * Programming in Java by Venkat Subramaniam".
 *
 * @author Ullrich Hafner
 */
public final class ExceptionTester {
    /**
     * Runs the specified block and catches the expected exception. If the block does not throw an exception then this
     * method fails the current unit test by calling {@link Assert#fail()}. The caught exception is stored in an
     * instance of {@link ExceptionReference} and can be checked for its type using {@link
     * ExceptionReference#toThrow(Class)}.
     *
     * @param block the block to be executed
     * @return the wrapped exception
     */
    public static ExceptionReference expect(final Runnable block) {
        try {
            block.run();
        }
        catch (Throwable exception) { // NOCHECKSTYLE NOPMD
            return new ExceptionReference(exception);
        }
        Assert.fail("No exception thrown.");
        return null;
    }

    /**
     * Stores the exception thrown by a code block.
     */
    public static class ExceptionReference {
        private final Throwable actual;

        ExceptionReference(final Throwable actual) {
            this.actual = actual;
        }

        /**
         * Checks if the thrown exception is of the specified type. If the type does not match then this method fails
         * the current unit test by calling {@link Assert#fail()}.
         *
         * @param expected the expected exception type
         * @return this
         */
        public ExceptionReference toThrow(final Class<? extends Throwable> expected) {
            if (!expected.isInstance(actual)) {
                Assert.fail(String.format("Wrong exception type: Expected %s: Actual %s",
                        expected.getCanonicalName(), actual.getClass().getCanonicalName()));
            }
            return this;
        }

        /**
         * Checks if the exception message equals to the specified text. If the message does not match then this method
         * fails the current unit test by calling {@link Assert#fail()}.
         *
         * @param expected the expected exception type
         * @return this
         */
        public ExceptionReference withMessage(final String expected) {
            Assert.assertEquals("Wrong exception message:", expected, actual.getMessage());
            return this;
        }
    }

    private ExceptionTester() {
        // prevents instantiation
    }
}
