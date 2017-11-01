package edu.hm.hafner.analysis.assertj;

import org.assertj.core.api.SoftAssertions;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;

/**
 * A custom AssertJ SoftAssertion to make SoftAssertions specific to {@link Issues} and {@link Issue}.
 *
 * @author Andreas Moser
 */
public class AnalysisSoftAssertions extends SoftAssertions {

    /**
     * An entry point to use soft assertions with the {@link Issue} class.
     *
     * @param actual The Issue to make assertions on.
     * @return A new @link {@link IssueAssert}
     */
    public IssueAssert assertThat(final Issue actual) {
        return proxy(IssueAssert.class, Issue.class, actual);
    }

    /**
     * An entry point to use soft assertions with the {@link Issues} class.
     *
     * @param actual The Issues to make assertions on.
     * @return A new @link {@link IssuesAssert}
     */
    public IssuesAssert assertThat(final Issues actual) {
        return proxy(IssuesAssert.class, Issues.class, actual);
    }




}
