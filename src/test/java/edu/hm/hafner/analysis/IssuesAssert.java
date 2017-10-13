package edu.hm.hafner.analysis;

import java.util.SortedSet;

import org.assertj.core.api.AbstractAssert;

public class IssuesAssert extends AbstractAssert<IssuesAssert, Issues> {

    public IssuesAssert(final Issues issues, final Class<?> selfType) {
        super(issues, selfType);
    }

    public IssuesAssert hasSize(int size) {
        isNotNull();

        if (actual.getSize() != size) {
            failWithMessage("Expected size to be <%s> but was <%s>", size, actual.getSize());
        }
        return this;
    }

    public IssuesAssert hasHighPrioritySize(int highPrioritySize) {
        isNotNull();

        if (actual.getHighPrioritySize() != highPrioritySize) {
            failWithMessage("Expected highPrioritySize to be <%s> but was <%s>", highPrioritySize, actual.getHighPrioritySize());
        }
        return this;
    }

    public IssuesAssert hasNormalPrioritySize(int normalPrioritySize) {
        isNotNull();

        if (actual.getNormalPrioritySize() != normalPrioritySize) {
            failWithMessage("Expected normalPrioritySize to be <%s> but was <%s>", normalPrioritySize, actual.getNormalPrioritySize());
        }
        return this;
    }

    public IssuesAssert hasLowPrioritySize(int lowPrioritySize) {
        isNotNull();

        if (actual.getLowPrioritySize() != lowPrioritySize) {
            failWithMessage("Expected lowPrioritySize to be <%s> but was <%s>", lowPrioritySize, actual.getLowPrioritySize());
        }
        return this;
    }

    public IssuesAssert hasNumberOfFiles(int numberOfFiles) {
        isNotNull();

        if (actual.getNumberOfFiles() != numberOfFiles) {
            failWithMessage("Expected numberOfFiles to be <%s> but was <%s>", numberOfFiles, actual.getNumberOfFiles());
        }
        return this;
    }

    public IssuesAssert hasIssueonIndex(Issue issue, int index) {
        isNotNull();

        if (actual.get(index) != issue) {
            failWithMessage("Expected issue to be <%s> on the index %s but was <%s>", issue, index, actual.get(index));
        }
        return this;
    }

    public IssuesAssert hasFiles(SortedSet<String> files) {
        isNotNull();

        if (actual.getFiles().equals(files)) {
            failWithMessage("Expected sets are not the same");
        }
        return this;
    }

    /*
    public IssuesAssert hasProperties(<R> SortedSet<R> properties) {
        isNotNull();

        if (actual.getFiles().equals(files)) {
            failWithMessage("Expected sets are not the same");
        }
        return this;
    }*/

}
