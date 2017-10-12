package edu.hm.hafner.analysis;

import java.util.Objects;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.description.Description;

class IssueAssert extends AbstractAssert<IssueAssert, Issue> {


    public IssueAssert(final Issue actual) {
        super(actual, Issue.class);
    }

    public static IssueAssert assertThat(final Issue actual) {
        return new IssueAssert(actual);
    }

    public IssueAssert hasFileName(final String fileName) {
        isNotNull();
        if (!Objects.equals(actual.getFileName(), fileName)) {
            failWithMessage("Expected issue's filename to be <%s> but was <%s>", fileName, actual.getFileName());
        }
        return this;
    }

    public IssueAssert hasCategory(final String category) {
        isNotNull();
        if (!Objects.equals(actual.getFileName(), category)) {
            failWithMessage("Expected issue's category to be <%s> but was <%s>", category, actual.getCategory());
        }
        return this;
    }
}