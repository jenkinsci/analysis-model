package edu.hm.hafner.analysis;

import org.assertj.core.api.AbstractAssert;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.SortedSet;

public class IssuesAssert extends AbstractAssert<IssuesAssert, Issues> {

    public IssuesAssert(final Issues actual) {
        super(actual, IssuesAssert.class);
    }

    public static IssuesAssert assertThat(final Issues actual) {
        return new IssuesAssert(actual);
    }

    public IssuesAssert hasFiles(final SortedSet<String> files) {
        isNotNull();

        if (!Objects.equals(actual.getFiles(), files)) {
            failWithMessage("Expected issues's files to be <%s> but was <%s>", files, actual.getFiles());
        }

        return this;
    }
}
