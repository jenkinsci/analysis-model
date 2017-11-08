package edu.hm.hafner.analysis;

import javax.annotation.CheckForNull;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.SortedSet;

import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableList;

import static edu.hm.hafner.analysis.assertj.Assertions.assertThat;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;
import static java.util.Arrays.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

/**
 * Unit tests for {@link Issues}.
 *
 * @author Marcel Binder
 */
class IssuesTest {
    private static final Issue ISSUE_1 = new IssueBuilder().setMessage("issue-1").setFileName("file-1").setPriority(Priority.HIGH).build();
    private static final Issue ISSUE_2 = new IssueBuilder().setMessage("issue-2").setFileName("file-1").build();
    private static final Issue ISSUE_3 = new IssueBuilder().setMessage("issue-3").setFileName("file-1").build();
    private static final Issue ISSUE_4 = new IssueBuilder().setMessage("issue-4").setFileName("file-2").setPriority(Priority.LOW).build();
    private static final Issue ISSUE_5 = new IssueBuilder().setMessage("issue-5").setFileName("file-2").setPriority(Priority.LOW).build();
    private static final Issue ISSUE_6 = new IssueBuilder().setMessage("issue-6").setFileName("file-3").setPriority(Priority.LOW).build();

    @Test
    void testEmptyIssues() {
        Issues<Issue> issues = new Issues<>();

        assertThat(issues).hasSize(0);
    }

    @Test
    void testAddMultipleIssuesOneByOne() {
        Issues<Issue> issues = new Issues<>();

        assertThat(issues.add(ISSUE_1)).isEqualTo(ISSUE_1);
        assertThat(issues.add(ISSUE_2)).isEqualTo(ISSUE_2);
        assertThat(issues.add(ISSUE_3)).isEqualTo(ISSUE_3);
        assertThat(issues.add(ISSUE_4)).isEqualTo(ISSUE_4);
        assertThat(issues.add(ISSUE_5)).isEqualTo(ISSUE_5);
        assertThat(issues.add(ISSUE_6)).isEqualTo(ISSUE_6);

        assertAllIssuesAdded(issues);
    }

    @Test
    void testAddMultipleIssuesToEmpty() {
        Issues<Issue> issues = new Issues<>();
        List<Issue> issueList = asList(ISSUE_1, ISSUE_2, ISSUE_3, ISSUE_4, ISSUE_5, ISSUE_6);

        assertThat(issues.addAll(issueList)).isEqualTo(issueList);

        assertAllIssuesAdded(issues);
    }

    @Test
    void testAddMultipleIssuesToNonEmpty() {
        Issues<Issue> issues = new Issues<>();
        issues.add(ISSUE_1);

        issues.addAll(asList(ISSUE_2, ISSUE_3));
        issues.addAll(asList(ISSUE_4, ISSUE_5, ISSUE_6));

        assertAllIssuesAdded(issues);
    }

    private void assertAllIssuesAdded(final Issues<Issue> issues) {
        assertSoftly(softly -> {
            softly.assertThat(issues)
                  .hasSize(6)
                  .hasHighPrioritySize(1)
                  .hasNormalPrioritySize(2)
                  .hasLowPrioritySize(3);
            softly.assertThat(issues.getFiles()).containsExactly("file-1", "file-2", "file-3");
            softly.assertThat((Iterable<Issue>) issues)
                  .containsExactly(ISSUE_1, ISSUE_2, ISSUE_3, ISSUE_4, ISSUE_5, ISSUE_6);
            softly.assertThat(issues.all())
                  .containsExactly(ISSUE_1, ISSUE_2, ISSUE_3, ISSUE_4, ISSUE_5, ISSUE_6);
        });
    }

    @Test
    void testAddDuplicate() {
        Issues<Issue> issues = new Issues<>();
        issues.addAll(asList(ISSUE_1, ISSUE_1));

        assertThat(issues.all()).contains(ISSUE_1, ISSUE_1);
        assertThat(issues)
                .hasSize(2)
                .hasLowPrioritySize(0)
                .hasNormalPrioritySize(0)
                .hasHighPrioritySize(2);
        assertThat(issues.getFiles()).containsExactly("file-1");
    }

    @Test
    void testRemove() {
        Issues<Issue> issues = new Issues<>();
        issues.addAll(asList(ISSUE_1, ISSUE_2, ISSUE_3, ISSUE_4, ISSUE_5, ISSUE_6));

        Issue removed = issues.remove(ISSUE_1.getId());

        assertThat(removed).isEqualTo(ISSUE_1);
        assertThat(issues.all()).containsOnly(ISSUE_2, ISSUE_3, ISSUE_4, ISSUE_5, ISSUE_6);
        assertThat((Iterable<Issue>) issues).containsOnly(ISSUE_2, ISSUE_3, ISSUE_4, ISSUE_5, ISSUE_6);
        assertThat(issues).hasSize(5);
        assertThat(issues).hasHighPrioritySize(0);
        assertThat(issues).hasNormalPrioritySize(2);
        assertThat(issues).hasLowPrioritySize(3);
        assertThat(issues.getFiles()).containsExactly("file-1", "file-2", "file-3");
    }

    @Test
    void testRemoveNotExisting() {
        Issues<Issue> issues = new Issues<>();

        assertThatThrownBy(() -> issues.remove(ISSUE_1.getId()))
                .isInstanceOf(NoSuchElementException.class)
                .hasNoCause()
                .hasMessageContaining(ISSUE_1.getId().toString());
    }

    @Test
    void testRemoveDuplicate() {
        Issues<Issue> issues = new Issues<>();
        issues.addAll(asList(ISSUE_1, ISSUE_1));

        issues.remove(ISSUE_1.getId());

        assertThat(issues.all()).containsOnly(ISSUE_1);
        assertThat(issues).hasSize(1);
        assertThat(issues).hasHighPrioritySize(1);
        assertThat(issues.getFiles()).containsExactly("file-1");
    }

    @Test
    void testFindByIdSingleIssue() {
        Issues<Issue> issues = new Issues<>();
        issues.addAll(ImmutableList.of(ISSUE_1));

        Issue found = issues.findById(ISSUE_1.getId());

        assertThat(found).isEqualTo(ISSUE_1);
    }

    @Test
    void testFindByIdSingleIssueNotExisting() {
        Issues<Issue> issues = new Issues<>();
        issues.addAll(ImmutableList.of(ISSUE_1));

        assertThatThrownBy(() -> issues.findById(ISSUE_2.getId()))
                .isInstanceOf(NoSuchElementException.class)
                .hasNoCause()
                .hasMessageContaining(ISSUE_2.getId().toString());
    }

    @Test
    void testFindByIdMultipleIssues() {
        Issues<Issue> issues = new Issues<>();
        issues.addAll(ImmutableList.of(ISSUE_1, ISSUE_2));

        Issue found = issues.findById(ISSUE_2.getId());

        assertThat(found).isEqualTo(ISSUE_2);
    }

    @Test
    void testFindByIdMultipleIssuesNotExisting() {
        Issues<Issue> issues = new Issues<>();
        issues.addAll(ImmutableList.of(ISSUE_1, ISSUE_2));

        assertThatThrownBy(() -> issues.findById(ISSUE_3.getId()))
                .isInstanceOf(NoSuchElementException.class)
                .hasNoCause()
                .hasMessageContaining(ISSUE_3.getId().toString());
    }

    @Test
    void testFindNoneByProperty() {
        Issues<Issue> issues = new Issues<>();
        issues.addAll(asList(ISSUE_1, ISSUE_2, ISSUE_3));

        ImmutableList<Issue> found = issues.findByProperty(issue -> Objects.equals(issue.getPriority(), Priority.LOW));

        assertThat(found).isEmpty();
    }

    @Test
    void testFindByPropertyResultImmutable() {
        Issues<Issue> issues = new Issues<>();
        issues.addAll(asList(ISSUE_1, ISSUE_2, ISSUE_3));
        ImmutableList<Issue> found = issues.findByProperty(issue -> Objects.equals(issue.getPriority(), Priority.HIGH));

        //noinspection deprecation
        assertThatThrownBy(() -> found.add(ISSUE_4))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasNoCause();
    }

    @Test
    void testIterator() {
        Issues<Issue> issues = new Issues<>();
        issues.addAll(asList(ISSUE_1, ISSUE_2, ISSUE_3));

        assertThat((Iterable<Issue>) issues).containsExactly(ISSUE_1, ISSUE_2, ISSUE_3);
    }

    @Test
    void testGet() {
        Issues<Issue> issues = new Issues<>();
        issues.addAll(asList(ISSUE_1, ISSUE_2, ISSUE_3));

        assertThat(issues.get(0)).isEqualTo(ISSUE_1);
        assertThat(issues.get(1)).isEqualTo(ISSUE_2);
        assertThat(issues.get(2)).isEqualTo(ISSUE_3);
        assertThatThrownBy(() -> issues.get(-1))
                .isInstanceOf(IndexOutOfBoundsException.class)
                .hasNoCause()
                .hasMessageContaining("-1");
        assertThatThrownBy(() -> issues.get(3))
                .isInstanceOf(IndexOutOfBoundsException.class)
                .hasNoCause()
                .hasMessageContaining("3");
    }

    @Test
    void testGetFiles() {
        Issues<Issue> issues = new Issues<>();
        issues.addAll(asList(ISSUE_1, ISSUE_2, ISSUE_3, ISSUE_4, ISSUE_5, ISSUE_6));

        assertThat(issues.getFiles()).contains("file-1", "file-1", "file-3");
    }

    @Test
    void testToString() {
        Issues<Issue> issues = new Issues<>();
        issues.addAll(asList(ISSUE_1, ISSUE_2, ISSUE_3));

        assertThat(issues.toString()).contains("3");
    }

    @Test
    void testGetProperties() {
        Issues<Issue> issues = new Issues<>();
        issues.addAll(asList(ISSUE_1, ISSUE_2, ISSUE_3, ISSUE_4, ISSUE_5, ISSUE_6));

        SortedSet<String> properties = issues.getProperties(issue -> issue.getMessage());

        assertThat(properties)
                .contains(ISSUE_1.getMessage())
                .contains(ISSUE_2.getMessage())
                .contains(ISSUE_3.getMessage());
    }

    @Test
    void testCopy() {
        Issues<Issue> original = new Issues<>();
        original.addAll(asList(ISSUE_1, ISSUE_2, ISSUE_3));

        Issues<Issue> copy = original.copy();

        assertThat(copy).isNotSameAs(original);
        assertThat(copy.all()).contains(ISSUE_1, ISSUE_2, ISSUE_3);

        copy.add(ISSUE_4);
        assertThat(original.all()).contains(ISSUE_1, ISSUE_2, ISSUE_3);
        assertThat(copy.all()).contains(ISSUE_1, ISSUE_2, ISSUE_3, ISSUE_4);
    }

    @Test
    void should() {
        ExtendedIssueBuilder builder = new ExtendedIssueBuilder();
        ExtendedIssue issue = builder.build();

        Issues<ExtendedIssue> issues = new Issues<>();
        issues.add(issue);

        ExtendedIssue returnedIssue = issues.get(0);
    }

    public static class ExtendedIssueBuilder extends IssueBuilder {
        @Override
        public ExtendedIssue build() {
            return new ExtendedIssue(fileName, lineStart, lineEnd, columnStart, columnEnd, category, type,
                    packageName, moduleName, priority, message, description, origin,
                    "Bla Bla");
        }
    }

    public static class ExtendedIssue extends Issue {
        private final String additional;

        /**
         * Creates a new instance of {@link Issue} using the specified properties.
         *
         * @param fileName
         *         the name of the file that contains this issue
         * @param lineStart
         *         the first line of this issue (lines start at 1; 0 indicates the whole file)
         * @param lineEnd
         *         the last line of this issue (lines start at 1)
         * @param columnStart
         *         the first column of this issue (columns start at 1, 0 indicates the whole line)
         * @param columnEnd
         *         the last column of this issue (columns start at 1)
         * @param category
         *         the category of this issue (depends on the available categories of the static analysis tool)
         * @param type
         *         the type of this issue (depends on the available types of the static analysis tool)
         * @param packageName
         *         the name of the package (or name space) that contains this issue
         * @param moduleName
         *         the name of the moduleName (or project) that contains this issue
         * @param priority
         *         the priority of this issue
         * @param message
         *         the detail message of this issue
         * @param description
         *         the description for this issue
         */
        public ExtendedIssue(@CheckForNull final String fileName, final int lineStart, final int lineEnd,
                final int columnStart, final int columnEnd, @CheckForNull final String category,
                @CheckForNull final String type, @CheckForNull final String packageName,
                @CheckForNull final String moduleName, @CheckForNull final Priority priority,
                @CheckForNull final String message, @CheckForNull final String description,
                @CheckForNull final String origin, final String additional) {
            super(fileName, lineStart, lineEnd, columnStart, columnEnd, category, type, packageName, moduleName, priority, message, description, origin);

            this.additional = additional;
        }

        public String getAdditional() {
            return additional;
        }
    }
}