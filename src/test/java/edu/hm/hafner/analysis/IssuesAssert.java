package edu.hm.hafner.analysis;

import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.description.Description;

public class IssuesAssert extends AbstractAssert<IssuesAssert, Issues> {
    public IssuesAssert(final Issues actual) {
        super(actual, IssuesAssert.class);
    }

    public static IssuesAssert assertThat(final Issues actual) {
        return new IssuesAssert(actual);
    }

    public IssuesAssert assertContains(final Issue issue) {
        Iterable<Issue> iterable = () -> actual.iterator();
        if(!StreamSupport.stream(iterable.spliterator(), false).filter(a -> a.equals(issue)).findFirst().isPresent()) {
            failWithMessage("Issues's element does not contain <%s>", issue);
        }
        return this;
    }

    public IssuesAssert assertDoesNotContain(final Issue issue) {
        Iterable<Issue> iterable = () -> actual.iterator();
        if(StreamSupport.stream(iterable.spliterator(), false).filter(a -> a.equals(issue)).findFirst().isPresent()) {
            failWithMessage("Issues's element contains <%s>", issue);
        }
        return this;
    }

    public IssueAssert get(final int index) {
        Iterable<Issue> iterable = () -> actual.iterator();
        Optional<Issue> optionalResult = StreamSupport.stream(iterable.spliterator(), false).skip(index).findFirst();
        if(!optionalResult.isPresent()) {
            failWithMessage("Issues's element <%s> does not exist", index);
        }
        return IssueAssert.assertThat(optionalResult.get());
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssuesAssert hasSizeOfPriorityHigh(final int size) {
        isNotNull();
        if (actual.getHighPrioritySize() != size) {
            failWithMessage("Expected issues's high priority size to be <%s> but was <%s>", size, actual.getHighPrioritySize());
        }
        return this;
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssuesAssert hasSizeOfPriorityNormal(final int size) {
        isNotNull();
        if (actual.getNormalPrioritySize() != size) {
            failWithMessage("Expected issues's normal priority size to be <%s> but was <%s>", size, actual.getNormalPrioritySize());
        }
        return this;
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssuesAssert hasSizeOfPriorityLow(final int size) {
        isNotNull();
        if (actual.getLowPrioritySize() != size) {
            failWithMessage("Expected issues's low priority size to be <%s> but was <%s>", size, actual.getLowPrioritySize());
        }
        return this;
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssuesAssert hasSize(final int size) {
        isNotNull();
        if (actual.getSize() != size) {
            failWithMessage("Expected issues's size to be <%s> but was <%s>", size, actual.getLowPrioritySize());
        }
        return this;
    }
}
