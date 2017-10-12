package edu.hm.hafner.analysis.assertj;

import org.assertj.core.api.SoftAssertions;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;

public class AnalysisSoftAssertions extends SoftAssertions {
    public IssueAssert assertThat(final Issue actual) {
        return proxy(IssueAssert.class, Issue.class, actual);
    }

    public IssuesAssert assertThat(final Issues actual) {
        return proxy(IssuesAssert.class, Issues.class, actual);
    }
}
