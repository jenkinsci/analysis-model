package edu.hm.hafner.analysis.Assertions;

import java.util.function.Consumer;

import org.assertj.core.api.SoftAssertions;

import edu.hm.hafner.analysis.Issue;

/**
 * Soft assertion for issue.
 */
public class IssueSoftAssertion extends SoftAssertions {

    /**
     * Custom assert function for soft assertion.
     *
     * @param actual current issue
     * @return custom assertion
     */
    public IssueAssert assertThat(Issue actual) {
        return proxy(IssueAssert.class, Issue.class, actual);
    }

    /**
     * Lambda function for soft assertions.
     *
     * @param softly lambda consumer.
     */
    public static void assertIssueSoftly(Consumer<IssueSoftAssertion> softly) {
        IssueSoftAssertion assertions = new IssueSoftAssertion();
        softly.accept(assertions);
        assertions.assertAll();
    }
}
