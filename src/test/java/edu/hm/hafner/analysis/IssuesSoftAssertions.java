package edu.hm.hafner.analysis;

import org.assertj.core.api.SoftAssertions;

/**
 * Custom soft assertions for {@link Issues}.
 *
 * @author Joscha Behrmann
 */
public class IssuesSoftAssertions extends SoftAssertions {

    public IssuesAssert assertThat(Issues actual) {
        return proxy(IssuesAssert.class, Issues.class, actual);
    }
}
