package edu.hm.hafner.analysis.assertj;

import org.assertj.core.api.AbstractAssert;

import edu.hm.hafner.analysis.Issues;

public class IssuesAssert extends AbstractAssert<IssuesAssert, Issues> {
    public IssuesAssert(Issues actual) {
        super(actual, IssueAssert.class);
    }

    public static IssuesAssert assertThat(Issues actual) {
        return new IssuesAssert(actual);
    }

    public IssuesAssert hasSize(int size) {
        isNotNull();

        if (actual.size() != size) {
            failWithMessage("Expected issues' size to be <%s> but was <%s>", size, actual.size());
        }
        return this;
    }

    public IssuesAssert hasHighPrioritySize(int highPrioritySize) {
        isNotNull();

        if (actual.getHighPrioritySize() != highPrioritySize) {
            failWithMessage("Expected issues' high priority size to be <%d> but was <%d>", highPrioritySize, actual.size());
        }
        return this;
    }

    public IssuesAssert hasNormalPrioritySize(int normalPrioritySize) {
        isNotNull();

        if (actual.getNormalPrioritySize() != normalPrioritySize) {
            failWithMessage("Expected issues' normal priority size to be <%d> but was <%d>", normalPrioritySize, actual.size());
        }
        return this;
    }

    public IssuesAssert hasLowPrioritySize(int lowPrioritySize) {
        isNotNull();

        if (actual.getLowPrioritySize() != lowPrioritySize) {
            failWithMessage("Expected issues' low priority size to be <%d> but was <%d>", lowPrioritySize, actual.size());
        }
        return this;
    }

    public IssuesAssert hasNumberOfFiles(int numberOfFiles) {
        isNotNull();

        if (actual.getNumberOfFiles() != numberOfFiles) {
            failWithMessage("Expected issues' number of size to be <%d> but was <%d>", numberOfFiles, actual.size());
        }
        return this;
    }
}
