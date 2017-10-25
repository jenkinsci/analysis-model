package edu.hm.hafner.analysis;

import org.assertj.core.api.AbstractAssert;

/**
 * Custom assertions for {@link Issues}.
 */
public class IssuesAssert extends AbstractAssert<IssuesAssert, Issues> {

    public IssuesAssert(final Issues actual) {
        super(actual, IssuesAssert.class);
    }

    public static IssuesAssert assertThat(Issues actual) {
        return new IssuesAssert(actual);
    }


    public IssuesAssert hasSize(int size) {
        isNotNull();

        if (actual.size() != size || actual.getSize() != size) {
            failWithMessage("Expected size to be <%s> but was <%s>",
                    size, actual.size());
        }

        return this;
    }

    public IssuesAssert isEmpty() {
        isNotNull();

        if (actual.size() > 0 || actual.getSize() > 0) {
            failWithMessage("Expected size to be <%s> but was <%s>",
                    0, actual.size());
        }

        return this;
    }

    public IssuesAssert hasHighPrioritySize(int highPrioritySize) {
        isNotNull();

        if (actual.getHighPrioritySize() != highPrioritySize) {
            failWithMessage("Expected highPrioritySize to be <%s> but was <%s>",
                    highPrioritySize, actual.getHighPrioritySize());
        }

        return this;
    }

    public IssuesAssert hasNormalPrioritySize(int normalPrioritySize) {
        isNotNull();

        if (actual.getNormalPrioritySize() != normalPrioritySize) {
            failWithMessage("Expected normalPrioritySize to be <%s> but was <%s>",
                    normalPrioritySize, actual.getNormalPrioritySize());
        }

        return this;
    }

    public IssuesAssert hasLowPrioritySize(int lowPrioritySize) {
        isNotNull();

        if (actual.getLowPrioritySize() != lowPrioritySize) {
            failWithMessage("Expected lowPrioritySize to be <%s> but was <%s>",
                    lowPrioritySize, actual.getLowPrioritySize());
        }

        return this;
    }

    public IssuesAssert hasNumberOfFiles(int numberOfFiles) {
        isNotNull();

        if (actual.getNumberOfFiles() != numberOfFiles) {
            failWithMessage("Expected numberOfFiles to be <%s> but was <%s>",
                    numberOfFiles, actual.getNumberOfFiles());
        }

        return this;
    }
}
