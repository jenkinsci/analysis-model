package edu.hm.hafner.analysis.assertj;

import org.assertj.core.api.AbstractAssert;

import edu.hm.hafner.analysis.Issues;

/**
 * A custom AssertJ assertion to make assertions specific to {@link Issues}.
 *
 * @author Andreas Moser
 */
public class IssuesAssert extends AbstractAssert<IssuesAssert, Issues> {
    /**
     * Creates a new @link {@link IssuesAssert}.
     *
     * @param actual The actual Issues to make assertions on.
     */
    public IssuesAssert(final Issues actual) {
        super(actual, IssuesAssert.class);
    }

    /**
     * An entry point for IssuesAssert.
     *
     * @param actual The Issues to make assertions on.
     * @return A new @link {@link IssuesAssert}
     */
    public static IssuesAssert assertThat(final Issues actual) {
        return new IssuesAssert(actual);
    }

    /**
     * Verifies that the actual Issues's size is equal to the given one.
     *
     * @param size The given size to compare the actual Issues's size to.
     * @return this assertion object.
     * @throws AssertionError - if the actual Issues's size is not equal to the given one.
     */
    public IssuesAssert hasSize(final int size) {
        isNotNull();

        if (actual.getSize() != size) {
            failWithMessage("Expected issues's size to be <%s> but was <%s>", size, actual.getSize());
        }

        return this;
    }

    /**
     * Verifies that the actual Issues's highPrioritySize is equal to the given one.
     *
     * @param prioritySize The given highPrioritySize to compare the actual Issues's highPrioritySize to.
     * @return this assertion object.
     * @throws AssertionError - if the actual Issues's highPrioritySize is not equal to the given one.
     */
    public IssuesAssert hasHighPrioritySize(final int prioritySize) {
        isNotNull();

        if (actual.getHighPrioritySize() != prioritySize) {
            failWithMessage("Expected issues's high prioritySize to be <%s> but was <%s>", prioritySize, actual.getHighPrioritySize());
        }

        return this;
    }

    /**
     * Verifies that the actual Issues's normalPrioritySize is equal to the given one.
     *
     * @param prioritySize The given normalPrioritySize to compare the actual Issues's normalPrioritySize to.
     * @return this assertion object.
     * @throws AssertionError - if the actual Issues's normalPrioritySize is not equal to the given one.
     */
    public IssuesAssert hasNormalPrioritySize(final int prioritySize) {
        isNotNull();

        if (actual.getNormalPrioritySize() != prioritySize) {
            failWithMessage("Expected issues's normal prioritySize to be <%s> but was <%s>", prioritySize, actual.getNormalPrioritySize());
        }

        return this;
    }

    /**
     * Verifies that the actual Issues's lowPrioritySize is equal to the given one.
     *
     * @param prioritySize The given lowPrioritySize to compare the actual Issues's lowPrioritySize to.
     * @return this assertion object.
     * @throws AssertionError - if the actual Issues's lowPrioritySize is not equal to the given one.
     */
    public IssuesAssert hasLowPrioritySize(final int prioritySize) {
        isNotNull();

        if (actual.getLowPrioritySize() != prioritySize) {
            failWithMessage("Expected issues's low prioritySize to be <%s> but was <%s>", prioritySize, actual.getLowPrioritySize());
        }

        return this;
    }

}