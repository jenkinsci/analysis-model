package edu.hm.hafner.analysis.assertions;

import java.util.function.Consumer;

import org.assertj.core.api.SoftAssertions;

import edu.hm.hafner.analysis.Issue;

/**
 * Implementation of Soft Assertions for Issue.
 *
 * @author Mark Tripolt
 */
public class IssueSoftAssertions extends SoftAssertions {


    /**
     * Custom assertThat function for soft assertions.
     *
     * @param actual issue
     * @return custom assertion
     */
    public IssueAssert assertThat(final Issue actual) {
        return proxy(IssueAssert.class, Issue.class, actual);
    }

    /**
     * Custom lambda function for soft assertions.
     *
     * @param softly consumer
     */
    public static void assertIssueSoftly(final Consumer<IssueSoftAssertions> softly) {
        IssueSoftAssertions assertions = new IssueSoftAssertions();
        softly.accept(assertions);
        assertions.assertAll();
    }


}
