package edu.hm.hafner.analysis.Assertions;

import java.util.function.Consumer;

import org.assertj.core.api.SoftAssertions;

import edu.hm.hafner.analysis.Issue;

public class IssueSoftAssertion extends SoftAssertions {

    public IssueAssert assertThat(Issue actual) {
        return proxy(IssueAssert.class, Issue.class, actual);
    }


    public static void assertIssueSoftly(Consumer<IssueSoftAssertion> softly) {
        IssueSoftAssertion assertions = new IssueSoftAssertion();
        softly.accept(assertions);
        assertions.assertAll();
    }
}
