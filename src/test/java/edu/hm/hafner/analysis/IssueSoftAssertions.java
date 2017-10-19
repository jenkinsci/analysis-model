package edu.hm.hafner.analysis;

import edu.hm.hafner.analysis.assertj.SoftAssertions;

import java.util.function.Consumer;

public class IssueSoftAssertions extends SoftAssertions{
    public IssueAssert assertThat(Issue actual) {
        return proxy(IssueAssert.class, Issue.class, actual);
    }

    /*
    public static void assertSoftlyIssue(Consumer<IssueSoftAssertions> softly) {
        IssueSoftAssertions assertions = new IssueSoftAssertions();
        softly.accept(assertions);
        assertions.assertAll();
    }*/
}
