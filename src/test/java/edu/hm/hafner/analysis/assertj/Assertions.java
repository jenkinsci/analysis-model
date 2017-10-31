package edu.hm.hafner.analysis.assertj;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;

/**
 * Custom assertions for {@link Issues} and {@link Issue}.
 *
 * @author Ullrich Hafner
 */
public class Assertions extends org.assertj.core.api.Assertions {
    /**
     * Creates a new {@link IssueAssert} to make assertions on actual {@link Issue}.
     *
     * @param actual the issue we want to make assertions on
     * @return a new {@link IssueAssert}
     */
    public static IssueAssert assertThat(final Issue actual) {
        return new IssueAssert(actual);
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
}
