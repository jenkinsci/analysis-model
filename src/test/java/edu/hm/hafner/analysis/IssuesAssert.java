package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.stream.StreamSupport;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.assertj.core.description.Description;

/**
 * Custom assert to test the class {@link Issues}.
 *
 * @author Michael Schmid
 */
public class IssuesAssert extends AbstractAssert<IssuesAssert, Issues> {
    public IssuesAssert(final Issues actual) {
        super(actual, IssuesAssert.class);
    }

    public static IssuesAssert assertThat(final Issues actualIssue) {
        return new IssuesAssert(actualIssue);
    }

    /**
     * Assert that the issues object contains the parameter issue.
     * @param issue expected in the issues object
     * @return this (fluent interface)
     */
    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssuesAssert contains(final Issue... issue) {
        Assertions.assertThat(actual.iterator()).contains(issue);
        return this;
    }

    /**
     * Assert that the issues object doesn't contain the parameter issue.
     * @param issue not expected in the issues object
     * @return this (fluent interface)
     */
    public IssuesAssert doesNotContain(final Issue... issue) {
        Assertions.assertThat(actual.iterator()).doesNotContain(issue);
        return this;
    }

    /**
     * Assert that the issues object contains the issue elements in the same order as the parameter array.
     * @param issues expected order of issue elements
     * @return this (fluent interface)
     */
    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssuesAssert containsExactly(final Issue... issues) {
        Assertions.assertThat(actual.iterator()).containsExactly(issues);
        return this;
    }


    /**
     * Assert that the issues object contains the issue elements in the same order as the parameter collection.
     * @param issues expected order of issue elements
     * @return this (fluent interface)
     */
    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssuesAssert containsExactly(final Collection<Issue> issues) {
        Issue[] issuesArray = new Issue[issues.size()];
        return containsExactly(issues.toArray(issuesArray));
    }

    public IssueAssert get(final int index) {
        return IssueAssert.assertThat(actual.get(index));
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssuesAssert hasSizeOfPriorityHigh(final int size) {
        isNotNull();
        if (actual.getHighPrioritySize() != size) {
            failWithMessage("Expected issues's high priority size to be <%s> but was <%s>", size, actual.getHighPrioritySize());
        }
        return this;
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssuesAssert hasSizeOfPriorityNormal(final int size) {
        isNotNull();
        if (actual.getNormalPrioritySize() != size) {
            failWithMessage("Expected issues's normal priority size to be <%s> but was <%s>", size, actual.getNormalPrioritySize());
        }
        return this;
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssuesAssert hasSizeOfPriorityLow(final int size) {
        isNotNull();
        if (actual.getLowPrioritySize() != size) {
            failWithMessage("Expected issues's low priority size to be <%s> but was <%s>", size, actual.getLowPrioritySize());
        }
        return this;
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssuesAssert hasSize(final int size) {
        isNotNull();
        if (actual.getSize() != size) {
            failWithMessage("Expected issues's size to be <%s> but was <%s>", size, actual.getLowPrioritySize());
        }
        return this;
    }
}
