package edu.hm.hafner.analysis.assertj;

import org.assertj.core.api.AbstractAssert;

import edu.hm.hafner.analysis.Issues;

public class IssuesAssert extends AbstractAssert<IssuesAssert, Issues> {
    //TODO javadoc

    public IssuesAssert(Issues actual) {
        super(actual, IssuesAssert.class);
    }

    public static IssuesAssert assertThat(Issues actual) {
        return new IssuesAssert(actual);
    }

    public IssuesAssert hasSize(final int size) {
        isNotNull();

        if (actual.getSize() != size) {
            failWithMessage("Expected issues's size to be <%s> but was <%s>", size, actual.getSize());
        }

        return this;
    }

    public IssuesAssert hasHighPrioritySize(final int prioritySize) {
        isNotNull();

        if (actual.getHighPrioritySize() != prioritySize) {
            failWithMessage("Expected issues's high prioritySize to be <%s> but was <%s>", prioritySize, actual.getHighPrioritySize());
        }

        return this;
    }

    public IssuesAssert hasNormalPrioritySize(final int prioritySize) {
        isNotNull();

        if (actual.getNormalPrioritySize() != prioritySize) {
            failWithMessage("Expected issues's normal prioritySize to be <%s> but was <%s>", prioritySize, actual.getNormalPrioritySize());
        }

        return this;
    }


    public IssuesAssert hasLowPrioritySize(final int prioritySize) {
        isNotNull();

        if (actual.getLowPrioritySize() != prioritySize) {
            failWithMessage("Expected issues's low prioritySize to be <%s> but was <%s>", prioritySize, actual.getLowPrioritySize());
        }

        return this;
    }

}