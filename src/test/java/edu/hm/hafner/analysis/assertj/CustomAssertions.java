package edu.hm.hafner.analysis.assertj;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;

public class CustomAssertions {

    public static IssueAssert assertThat(final Issue issue) {
        return new IssueAssert(issue);
    }

}
