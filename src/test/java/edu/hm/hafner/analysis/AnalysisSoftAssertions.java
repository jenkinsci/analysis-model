package edu.hm.hafner.analysis;

import org.assertj.core.api.SoftAssertions;

/**
 * Class used as an entry point for custom AssertJ soft assertions.
 * <p>
 * Supports soft assertions for
 * {@link Issue}
 * {@link Issues}
 */
public class AnalysisSoftAssertions extends SoftAssertions {

    public IssueAssert assertThat(Issue actual) {
        return proxy(IssueAssert.class, Issue.class, actual);
    }

    public IssuesAssert assertThat(Issues actual) {
        return proxy(IssuesAssert.class, Issues.class, actual);
    }

}
