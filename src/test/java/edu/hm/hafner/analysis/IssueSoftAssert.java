package edu.hm.hafner.analysis;

import org.assertj.core.api.SoftAssertions;

/**
 * Custom soft assertions for {@link Issue}.
 */
public class IssueSoftAssert extends SoftAssertions {

    public IssueAssert assertThat(Issue actual) {
        return proxy(IssueAssert.class, Issue.class, actual);
    }
}
