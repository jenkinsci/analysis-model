package edu.hm.hafner.analysis;

import org.assertj.core.api.AbstractAssert;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.SortedSet;

public class IssuesAssert extends AbstractAssert<IssuesAssert, Issues> {

    public IssuesAssert(final Issues actual) {
        super(actual, IssuesAssert.class);
    }

    public static IssuesAssert assertThat(final Issues actual) {
        return new IssuesAssert(actual);
    }

    public IssuesAssert hasFiles(final SortedSet<String> files) {
        isNotNull();

        if (!Objects.equals(actual.getFiles(), files)) {
            failWithMessage("Expected issues's files to be <%s> but was <%s>", files, actual.getFiles());
        }

        return this;
    }

    public IssuesAssert hasSize(final int size) {
        isNotNull();

        if (actual.size() != size) {
            failWithMessage("Expected issues's files to be <%s> but was <%s>", size, actual.size());
        }

        return this;
    }

    public IssuesAssert hasHighPrioritySize(final int size) {
        isNotNull();

        if (actual.getHighPrioritySize() != size) {
            failWithMessage("Expected issues's highPrioritySize to be <%s> but was <%s>", size, actual.getHighPrioritySize());
        }

        return this;
    }

    public IssuesAssert hasNormalPrioritySize(final int size) {
        isNotNull();

        if (actual.getNormalPrioritySize() != size) {
            failWithMessage(
                    "Expected issues's normalPrioritySize to be <%s> but was <%s>",
                    size,
                    actual.getNormalPrioritySize()
            );
        }

        return this;
    }

    public IssuesAssert hasLowPrioritySize(final int size) {
        isNotNull();

        if (actual.getLowPrioritySize() != size) {
            failWithMessage(
                    "Expected issues's lowPrioritySize to be <%s> but was <%s>",
                    size,
                    actual.getLowPrioritySize()
            );
        }

        return this;
    }
}
