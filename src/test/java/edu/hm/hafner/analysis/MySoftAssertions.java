package edu.hm.hafner.analysis;

import org.assertj.core.api.SoftAssertions;

public class MySoftAssertions extends SoftAssertions{

    public IssueAssert assertThat(Issue actual) {
        return proxy(IssueAssert.class, Issue.class, actual);
    }

}
