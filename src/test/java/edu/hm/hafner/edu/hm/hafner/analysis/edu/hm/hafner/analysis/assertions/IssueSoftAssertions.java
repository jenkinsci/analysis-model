package edu.hm.hafner.edu.hm.hafner.analysis.edu.hm.hafner.analysis.assertions;

import org.assertj.core.api.SoftAssertions;

import edu.hm.hafner.analysis.Issue;

public class IssueSoftAssertions extends SoftAssertions {

    public IssueAssertions assertThat(Issue actual) {
        return proxy(IssueAssertions.class, Issue.class, actual);
    }


}
