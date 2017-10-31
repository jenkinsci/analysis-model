package edu.hm.hafner.analysis;

import edu.hm.hafner.analysis.Issue;

import org.assertj.core.api.SoftAssertions;


/**
 * AssertJ for Issue. This class allows soft assertions for Issue.
 *
 * @author Sebastian Balz
 */
class IssueSoftAssert extends SoftAssertions {

    /**
     * assert.
     *
     * @param actual that
     * @return this
     */
    IssueAssert assertThat(Issue actual) {
        return proxy(IssueAssert.class, Issue.class, actual);
    }
}
