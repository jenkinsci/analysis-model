package edu.hm.hafner.analysis.assertions;

import org.assertj.core.api.SoftAssertions;
import edu.hm.hafner.analysis.Issues;

/**
 * SoftAssertion for {@link Issues}
 * @author Raphael Furch
 */
public class IssuesSoftAssertions extends SoftAssertions {
    public IssuesAssertions assertThat(final Issues actual) {
        return proxy(IssuesAssertions.class, Issues.class, actual);
    }
}
