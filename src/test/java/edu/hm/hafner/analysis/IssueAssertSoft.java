package edu.hm.hafner.analysis;

import org.assertj.core.api.SoftAssertions;

/**
 * Soft assert wrapper to test the class {@link Issue}.
 *
 * @author Michael Schmid
 */
public class IssueAssertSoft extends SoftAssertions{
    public IssueAssert assertThat(final Issue actual) {
        return proxy(IssueAssert.class, Issue.class, actual);
    }
}
