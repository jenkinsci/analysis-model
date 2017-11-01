package edu.hm.hafner.analysis.assertions;

import org.assertj.core.api.SoftAssertions;
import edu.hm.hafner.analysis.Issue;

/**
 * SoftAssertion for {@link Issue}
 * @author Raphael Furch
 */
public class IssueSoftAssertions extends SoftAssertions {

    public IssueAssertions assertThat(final Issue actual) {
        return proxy(IssueAssertions.class, Issue.class, actual);
    }


}
