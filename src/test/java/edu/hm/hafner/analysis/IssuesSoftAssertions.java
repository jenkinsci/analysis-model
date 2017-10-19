package edu.hm.hafner.analysis;

import org.assertj.core.api.SoftAssertions;

public class IssuesSoftAssertions extends SoftAssertions {

    public IssuesAssert assertThat(Issues actual) {
        return proxy(IssuesAssert.class, Issues.class, actual);
    }
}
