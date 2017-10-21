package edu.hm.hafner.analysis;

import java.util.Objects;

import org.assertj.core.api.AbstractAssert;

public class IssueAssert extends AbstractAssert<IssueAssert, Issue> {

    public IssueAssert(final Issue actual) {
        super(actual, IssueAssert.class);
    }

    public static IssueAssert assertThat(Issue actual) {
        return new IssueAssert(actual);
    }

    public IssueAssert hasFileName(String fileName) {
        isNotNull();

        if (!Objects.equals(actual.getFileName(), fileName)) {
            failWithMessage("Expected fileName to be <%s> but was <%s>", fileName, actual.getFileName());
        }

        return this;
    }

    public IssueAssert hasLineStart(int lineStart) {
        isNotNull();

        if (actual.getLineStart() != lineStart) {
            failWithMessage("Expected lineStart to be <%x> but was <%x>", lineStart, actual.getLineStart());
        }

        return this;
    }

    public IssueAssert hasLineEnd(int lineEnd) {
        isNotNull();

        if (actual.getLineEnd() != lineEnd) {
            failWithMessage("Expected lineStart to be <%x> but was <%x>", lineEnd, actual.getLineEnd());
        }

        return this;
    }


}
