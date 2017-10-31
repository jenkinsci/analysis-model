package edu.hm.hafner.analysis.assertj;

import org.assertj.core.api.AbstractAssert;

import edu.hm.hafner.analysis.Issues;

/**
 * Assertions for {@link Issues}.
 *
 * @author Marcel Binder
 */
@SuppressWarnings({"ParameterHidesMemberVariable", "NonBooleanMethodNameMayNotStartWithQuestion"})
public class IssuesAssert extends AbstractAssert<IssuesAssert, Issues> {
    private static final String EXPECTED_BUT_WAS_MESSAGE = "%nExpecting %s of:%n <%s>%nto be:%n <%s>%nbut was:%n <%s>.";

    public IssuesAssert(final Issues actual) {
        super(actual, IssuesAssert.class);
    }

    /**
     * An entry point for {@link IssuesAssert} to follow AssertJ standard {@code assertThat()}. With a static import,
     * one can write directly {@code assertThat(myIssues)} and get a specific assertion with code completion.
     *
     * @param actual the issues we want to make assertions on
     * @return a new {@link IssuesAssert}
     */
    public static IssuesAssert assertThat(final Issues actual) {
        return new IssuesAssert(actual);
    }

    public IssuesAssert hasSize(final int size) {
        isNotNull();

        if (actual.getSize() != size) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "size", actual, size, actual.size());
        }
        return this;
    }

    public IssuesAssert hasHighPrioritySize(final int highPrioritySize) {
        isNotNull();

        if (actual.getHighPrioritySize() != highPrioritySize) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "high priority size", actual, highPrioritySize, actual.getHighPrioritySize());
        }
        return this;
    }

    public IssuesAssert hasNormalPrioritySize(final int normalPrioritySize) {
        isNotNull();

        if (actual.getNormalPrioritySize() != normalPrioritySize) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "normal priority size", normalPrioritySize, actual.getNormalPrioritySize());
        }
        return this;
    }

    public IssuesAssert hasLowPrioritySize(final int lowPrioritySize) {
        isNotNull();

        if (actual.getLowPrioritySize() != lowPrioritySize) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "low priority size", lowPrioritySize, actual.getLowPrioritySize());
        }
        return this;
    }

    public IssuesAssert hasNumberOfFiles(final int numberOfFiles) {
        isNotNull();

        if (actual.getNumberOfFiles() != numberOfFiles) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "number of files", numberOfFiles, actual.getNumberOfFiles());
        }
        return this;
    }
}
