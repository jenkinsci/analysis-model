package edu.hm.hafner.analysis;

import org.assertj.core.api.Assertions;

/**
 * Class used as an entry point for custom AssertJ assertions.
 *
 * Supports assertions for
 * {@link Issue}
 * {@link Issues}
 *
 * @author slausch
 */
public class AnalysisAssertions extends Assertions {

    public static IssueAssert assertThat(Issue actual) {
        return new IssueAssert(actual);
    }

    public static IssuesAssert assertThat(Issues actual) {
        return new IssuesAssert(actual);
    }
}
