package edu.hm.hafner.edu.hm.hafner.analysis.edu.hm.hafner.analysis.assertions;

import java.util.Collection;
import java.util.Objects;
import java.util.SortedSet;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.function.Predicate;
import java.util.UUID;
import org.assertj.core.api.AbstractAssert;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("UnusedReturnValue")
public class IssuesAssertions extends AbstractAssert<IssuesAssertions, Issues> {

    //<editor-fold desc="Creation">
    public IssuesAssertions(final Issues actual) {
        super(actual, IssuesAssertions.class);
    }
    public static IssuesAssertions assertThat(final Issues actualIssues){
        return new IssuesAssertions(actualIssues);
    }
    //</editor-fold>

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssuesAssertions hasElementWithUuid(final UUID uuid) {
        // check actual not null
        isNotNull();
        // check condition
        if (!uuid.equals(actual.findById(uuid).getId())) {
            failWithMessage("Element <%s> was not in list but should", uuid);
        }
        return this;
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssuesAssertions hasNoElementWithUuid(final UUID uuid) {
        // check actual not null
        isNotNull();
        // check condition
        Throwable exception = assertThrows(NoSuchElementException.class, () -> {
            actual.findById(uuid);
        });
        if (exception == null) {
            failWithMessage("Element <%s> was in list but shouldn't", uuid);
        }
        return this;
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssuesAssertions hasElement(final Issue issue) {
        // check actual not null
        isNotNull();
        // check condition
        if (getStreamOfAllIssues().noneMatch((elem) -> issue.equals(elem))) {
            failWithMessage("Element <%s> was not in list but should", issue);
        }
        return this;
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssuesAssertions hasNotElement(final Issue issue) {
        // check actual not null
       isNotNull();
        // check condition
        if (getStreamOfAllIssues().anyMatch((elem) -> issue.equals(elem))) {
            failWithMessage("Element <%s> was in list but shouldn't", issue);
        }
        // Return this for Fluent.
        return this;
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssuesAssertions hasElements(final Collection<? extends Issue> issues) {
        // check actual not null
        isNotNull();
        // check condition
        Collection<Issue> currentIssues = getStreamOfAllIssues().collect(Collectors.toList());
        if (currentIssues.size() != issues.size()) {
            failWithMessage("Expected issue's element size to be <%s> but was <%s>", issues.size(), currentIssues.size());
        }
        else if(currentIssues.stream().anyMatch(f -> !issues.contains(f))){
            failWithMessage("Expected issue's elements to be <%s> but was <%s>", issueListToString(issues), issueListToString(currentIssues));
        }
        // Return this for Fluent.
        return this;
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssuesAssertions hasAllElements(final Collection<? extends Issue> issues) {
        // check actual not null
        isNotNull();
        // check condition
        Collection<Issue> currentIssues = actual.all();
        if (currentIssues.size() != issues.size()) {
            failWithMessage("Expected issue's element size to be <%s> but was <%s>", issues.size(), currentIssues.size());
        }
        else if(currentIssues.stream().anyMatch(f -> !issues.contains(f))){
            failWithMessage("Expected issue's elements to be <%s> but was <%s>", issueListToString(issues), issueListToString(currentIssues));
        }
        // Return this for Fluent.
        return this;
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssuesAssertions hasSize(final int size) {
        // check actual not null
        isNotNull();
        // check condition
        propertyEqualsCheck(actual.getSize(), size, "size");
        // Return this for Fluent.
        return this;
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssuesAssertions hasHighPrioritySize(final int size) {
        // check actual not null
        isNotNull();
        // check condition
        propertyEqualsCheck(actual.getHighPrioritySize(), size, "high priority size");
        // Return this for Fluent.
        return this;
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssuesAssertions hasNormalPrioritySize(final int size) {
        // check actual not null
        isNotNull();
        // check condition
        propertyEqualsCheck(actual.getNormalPrioritySize(), size, "normal priority size");
        // Return this for Fluent.
        return this;
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssuesAssertions hasLowPrioritySize(final int size) {
        // check actual not null
        isNotNull();
        // check condition
        propertyEqualsCheck(actual.getLowPrioritySize(), size, "low priority size");
        // Return this for Fluent.
        return this;
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssuesAssertions hasElementAtPosition(final int index, final Issue issue) {
        // check actual not null
        isNotNull();
        // check condition
        long count = getStreamOfAllIssues().count();
        if ( count <= index) {
            failWithMessage("Expected issues's get-Method index <%d> out of range <%d>", index, count);
        }
        Issue getIssue = actual.get(index);
        if (!Objects.equals(getIssue, issue)) {
            failWithMessage("Expected issues contains <%s> at position <%d> but was <%s>", issue, index, getIssue);
        }
        // Return this for Fluent.
        return this;
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssuesAssertions hasFiles(final SortedSet<String> files) {
        // check actual not null
        isNotNull();
        // check condition
        propertyEqualsCheck(actual.getFiles(), files, "files");
        // Return this for Fluent.
        return this;
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssuesAssertions hasNumberOfFiles(final int numberOfFiles) {
        // check actual not null
        isNotNull();
        // check condition
        propertyEqualsCheck(actual.getNumberOfFiles(), numberOfFiles, "NumberOfFiles");
        // Return this for Fluent.
        return this;
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssuesAssertions isACopyOf(final Issues issues){
        // check actual not null
        isNotNull();
        // check condition
        if(actual == issues){
            failWithMessage("Issues is no copy its the same");
        }
        ImmutableSet<Issue> actualList = actual.all();
        ImmutableSet<Issue> expectedList = issues.all();
        if (actualList.size() != expectedList.size()) {
            failWithMessage("Issues has a size of <%s> but expect <%s>", actualList.size(), expectedList.size());
        }
        Iterator<Issue> actualIssueIterator = actual.iterator();
        Iterator<Issue> expectedIssueIterator = issues.iterator();
        while(actualIssueIterator.hasNext()){
            Issue actualIssue = actualIssueIterator.next();
            Issue expectedIssue = expectedIssueIterator.next();
            if(!Objects.equals(actualIssue, expectedIssue)){
                failWithMessage("Issues expect element<%s> but was <%s>", expectedIssue, actualIssue);
            }
        }

        return this;
    }

    //<editor-fold desc="Issue test">
    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssuesAssertions hasIssuesWithFileName(final int count, final String filename) {
        return hasIssuesWithProperty(count, issue -> filename.equals(issue.getFileName()));
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssuesAssertions hasIssuesWithLineStart(final int count, final int lineStart) {
        return hasIssuesWithProperty(count, issue -> lineStart == issue.getLineStart());
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssuesAssertions hasIssuesWithLineEnd(final int count, final int lineEnd) {
        return hasIssuesWithProperty(count, issue -> lineEnd == issue.getLineEnd());
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssuesAssertions hasIssuesWithColumnStart(final int count, final int columnStart) {
        return hasIssuesWithProperty(count, issue -> columnStart == issue.getColumnStart());
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssuesAssertions hasIssuesWithColumnEnd(final int count, final int columnEnd) {
        return hasIssuesWithProperty(count, issue -> columnEnd == issue.getColumnEnd());
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssuesAssertions hasIssuesWithCategory(final int count, final String category) {
        return hasIssuesWithProperty(count, issue -> Objects.equals(category, issue.getCategory()));
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssuesAssertions hasIssuesWithType(final int count, final String type) {
        return hasIssuesWithProperty(count, issue -> Objects.equals(type, issue.getType()));
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssuesAssertions hasIssuesWithPackageName(final int count, final String packageName) {
        return hasIssuesWithProperty(count, issue -> Objects.equals(packageName, issue.getPackageName()));
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssuesAssertions hasIssuesWithPriority(final int count, final Priority priority) {
        return hasIssuesWithProperty(count, issue -> priority == issue.getPriority());
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssuesAssertions hasIssuesWithDescription(final int count, final String description) {
        return hasIssuesWithProperty(count, issue -> Objects.equals(description, issue.getDescription()));
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssuesAssertions hasIssuesWithMessage(final int count, final String message) {
        return hasIssuesWithProperty(count, issue -> Objects.equals(message, issue.getMessage()));
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssuesAssertions hasIssuesWithFingerprint(final int count, final String fingerprint) {
        return hasIssuesWithProperty(count, issue -> Objects.equals(fingerprint, issue.getFingerprint()));
    }


    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssuesAssertions hasIssuesWithId(final int count, final UUID uuid) {
        return hasIssuesWithProperty(count, issue -> uuid == issue.getId());
    }
    //</editor-fold>

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    public IssuesAssertions isString(final String expected) {
        // check actual not null
        isNotNull();
        // check condition
        propertyEqualsCheck(actual.toString(), expected, "toString");
        return this;
    }

    //<editor-fold desc="Helper">
    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    private IssuesAssertions hasIssuesWithProperty( final int count, final Predicate<? super Issue> criterion) {
        // check actual not null
        isNotNull();
        // check condition
        ImmutableList<Issue> results = actual.findByProperty(criterion);
        if (results.size() != count) {
            failWithMessage("Expected issues elements by <%s> expect count of <%s> but was <%s>", criterion.toString(), count, results.size());
        }
        // Return this for Fluent.
        return this;
    }
    private Stream<Issue> getStreamOfAllIssues() {

        return actual.all().stream();
    }
    private <T> void propertyEqualsCheck(final T actualValue, final T expected, final String propertyName){
        if (!Objects.equals(actualValue, expected)) {
            failWithMessage("Expected issues's "+propertyName+" to be <%s> but was <%s>", expected, actualValue);
        }
    }
    private String issueListToString(final Collection<? extends Issue> issues){
        return "{" + issues.stream().map(Issue::toString).reduce((a,b) -> a + "," + b)+ "}";
    }
    //</editor-fold>



}
