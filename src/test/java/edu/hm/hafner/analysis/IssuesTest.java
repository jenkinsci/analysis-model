package edu.hm.hafner.analysis;

import javax.annotation.CheckForNull;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.eclipse.collections.api.set.ImmutableSet;
import org.eclipse.collections.api.set.sorted.ImmutableSortedSet;
import org.junit.jupiter.api.Test;

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
    private static final Issue HIGH = new IssueBuilder().setMessage("issue-1")
            .setFileName("file-1")
            .setPriority(Priority.HIGH)
            .build();
    private static final Issue NORMAL_1 = new IssueBuilder().setMessage("issue-2")
            .setFileName("file-1")
            .setPriority(Priority.NORMAL)
            .build();
    private static final Issue NORMAL_2 = new IssueBuilder().setMessage("issue-3")
            .setFileName("file-1")
            .setPriority(Priority.NORMAL)
            .build();
    private static final Issue LOW_FILE_2 = new IssueBuilder().setMessage("issue-4")
            .setFileName("file-2")
            .setPriority(Priority.LOW)
            .build();
    private static final Issue ISSUE_5 = new IssueBuilder().setMessage("issue-5")
            .setFileName("file-2")
            .setPriority(Priority.LOW)
            .build();
    private static final Issue LOW_FILE_3 = new IssueBuilder().setMessage("issue-6")
            .setFileName("file-3")
            .setPriority(Priority.LOW)
            .build();
    private static final String EXTENDED_VALUE = "Extended";

    @Test
    void shouldBeEmptyWhenCreated() {
        Issues<Issue> issues = new Issues<>();

        assertThat(issues).isEmpty();
        assertThat(issues.isNotEmpty()).isFalse();
        assertThat(issues).hasSize(0);
        assertThat(issues.size()).isEqualTo(0);
        assertThat(issues).hasHighPrioritySize(0);
        assertThat(issues).hasLowPrioritySize(0);
        assertThat(issues).hasNormalPrioritySize(0);
    }

    @Test
    void shouldAddMultipleIssuesOneByOne() {
        Issues<Issue> issues = new Issues<>();

        assertThat(issues.add(HIGH)).isTrue();
        assertThat(issues.add(NORMAL_1)).isTrue();
        assertThat(issues.add(NORMAL_2)).isTrue();
        assertThat(issues.add(LOW_FILE_2)).isTrue();
        assertThat(issues.add(ISSUE_5)).isTrue();
        assertThat(issues.add(LOW_FILE_3)).isTrue();

        assertThatAllIssuesHaveBeenAdded(issues);
    }

    @Test
    void shouldAddMultipleIssuesAsCollection() {
        Issues<Issue> issues = new Issues<>();
        List<Issue> issueList = asList(HIGH, NORMAL_1, NORMAL_2, LOW_FILE_2, ISSUE_5, LOW_FILE_3);

        assertThat(issues.addAll(issueList)).isTrue();

        assertThatAllIssuesHaveBeenAdded(issues);
    }

    @Test
    void shouldIterateOverAllElementsInCorrectOrder() {
        Issues<Issue> issues = new Issues<>();

        issues.add(HIGH, NORMAL_1, NORMAL_2, LOW_FILE_2, ISSUE_5, LOW_FILE_3);
        Iterator<Issue> iterator = issues.iterator();
        assertThat(iterator.next()).isSameAs(HIGH);
        assertThat(iterator.next()).isSameAs(NORMAL_1);
        assertThat(iterator.next()).isSameAs(NORMAL_2);
        assertThat(iterator.next()).isSameAs(LOW_FILE_2);
        assertThat(iterator.next()).isSameAs(ISSUE_5);
        assertThat(iterator.next()).isSameAs(LOW_FILE_3);
    }

    @Test
    void shouldSkipAddedElements() {
        Issues<Issue> issues = new Issues<>(asList(HIGH, NORMAL_1, NORMAL_2, LOW_FILE_2, ISSUE_5, LOW_FILE_3));

        Issues<Issue> empty = new Issues<>();

        assertThat(empty.addAll(issues)).isTrue();
        assertThat(empty).hasSize(6);
        assertThat(empty.addAll(issues)).isFalse();
        assertThat(empty).hasSize(6);

        Issues<Issue> left = new Issues<>(asList(HIGH, NORMAL_1, NORMAL_2));
        Issues<Issue> right = new Issues<>(asList(LOW_FILE_2, ISSUE_5, LOW_FILE_3));

        Issues<Issue> everything = new Issues<>();
        assertThat(everything.addAll(left, right)).isTrue();
        assertThat(everything).hasSize(6);
    }

    @Test
    void shouldAddMultipleIssuesToNonEmpty() {
        Issues<Issue> issues = new Issues<>();
        issues.add(HIGH);

        issues.addAll(asList(NORMAL_1, NORMAL_2));
        issues.addAll(asList(LOW_FILE_2, ISSUE_5, LOW_FILE_3));

        assertThatAllIssuesHaveBeenAdded(issues);
    }

    private void assertThatAllIssuesHaveBeenAdded(final Issues<Issue> issues) {
        assertSoftly(softly -> {
            softly.assertThat(issues)
                    .hasSize(6)
                    .hasDuplicatesSize(0)
                    .hasHighPrioritySize(1)
                    .hasNormalPrioritySize(2)
                    .hasLowPrioritySize(3);
            softly.assertThat(issues.getFiles())
                    .containsExactly("file-1", "file-2", "file-3");
            softly.assertThat(issues.getFiles())
                    .containsExactly("file-1", "file-2", "file-3");
            softly.assertThat((Iterable<Issue>) issues)
                    .containsExactly(HIGH, NORMAL_1, NORMAL_2, LOW_FILE_2, ISSUE_5, LOW_FILE_3);
            softly.assertThat(issues.isNotEmpty()).isTrue();
            softly.assertThat(issues.size()).isEqualTo(6);

            softly.assertThat(issues.getPropertyCount(issue -> issue.getFileName())).containsEntry("file-1", 3);
            softly.assertThat(issues.getPropertyCount(issue -> issue.getFileName())).containsEntry("file-2", 2);
            softly.assertThat(issues.getPropertyCount(issue -> issue.getFileName())).containsEntry("file-3", 1);

            softly.assertThat(issues.getSizeOf(Priority.HIGH)).isEqualTo(1);
            softly.assertThat(issues.getSizeOf(Priority.NORMAL)).isEqualTo(2);
            softly.assertThat(issues.getSizeOf(Priority.LOW)).isEqualTo(3);

            softly.assertThat(issues.sizeOf(Priority.HIGH)).isEqualTo(1);
            softly.assertThat(issues.sizeOf(Priority.NORMAL)).isEqualTo(2);
            softly.assertThat(issues.sizeOf(Priority.LOW)).isEqualTo(3);
        });
    }

    @Test
    void shouldSkipDuplicates() {
        Issues<Issue> issues = new Issues<>();
        assertThat(issues.add(HIGH)).isTrue();
        assertThat(issues.add(HIGH)).isFalse();
        assertThat(issues.addAll(asList(HIGH, LOW_FILE_2))).isFalse();
        assertThat(issues.addAll(asList(NORMAL_1, NORMAL_2))).isTrue();

        assertThat(issues.iterator()).containsExactly(HIGH, LOW_FILE_2, NORMAL_1, NORMAL_2);
        assertThat(issues.iterator()).hasSize(4);

        assertThat(issues)
                .hasSize(4)
                .hasDuplicatesSize(2)
                .hasLowPrioritySize(1)
                .hasNormalPrioritySize(2)
                .hasHighPrioritySize(1);
        assertThat(issues.getFiles()).containsExactly("file-1", "file-2");
    }

    @Test
    void shouldRemoveById() {
        shouldRemoveOneIssue(HIGH, NORMAL_1, NORMAL_2);
        shouldRemoveOneIssue(NORMAL_1, HIGH, NORMAL_2);
        shouldRemoveOneIssue(NORMAL_1, NORMAL_2, HIGH);
    }

    private void shouldRemoveOneIssue(final Issue... initialElements) {
        Issues<Issue> issues = new Issues<>();
        issues.addAll(asList(initialElements));

        issues.remove(HIGH.getId());

        assertThat((Iterable<Issue>) issues).containsExactly(NORMAL_1, NORMAL_2);
    }

    @Test
    void shouldThrowExceptionWhenRemovingWithWrongKey() {
        Issues<Issue> issues = new Issues<>();

        UUID id = HIGH.getId();
        assertThatThrownBy(() -> issues.remove(id))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining(id.toString());
    }

    @Test
    void shouldFindIfOnlyOneIssue() {
        Issues<Issue> issues = new Issues<>();
        issues.addAll(Collections.singletonList(HIGH));

        Issue found = issues.findById(HIGH.getId());

        assertThat(found).isSameAs(HIGH);
    }

    @Test
    void shouldFindWithinMultipleIssues() {
        shouldFindIssue(HIGH, NORMAL_1, NORMAL_2);
        shouldFindIssue(NORMAL_1, HIGH, NORMAL_2);
        shouldFindIssue(NORMAL_1, NORMAL_2, HIGH);
    }

    private void shouldFindIssue(final Issue... elements) {
        Issues<Issue> issues = new Issues<>();
        issues.addAll(asList(elements));

        Issue found = issues.findById(HIGH.getId());

        assertThat(found).isSameAs(HIGH);
    }

    @Test
    void shouldThrowExceptionWhenSearchingWithWrongKey() {
        shouldFindNothing(HIGH);
        shouldFindNothing(HIGH, NORMAL_1);
    }

    private void shouldFindNothing(final Issue... elements) {
        Issues<Issue> issues = new Issues<>();
        issues.addAll(asList(elements));

        UUID id = NORMAL_2.getId();
        assertThatThrownBy(() -> issues.findById(id))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining(id.toString());
    }

    @Test
    void shouldReturnEmptyListIfPropertyDoesNotMatch() {
        Issues<Issue> issues = new Issues<>();
        issues.addAll(asList(HIGH, NORMAL_1, NORMAL_2));

        ImmutableSet<Issue> found = issues.findByProperty(issue -> Objects.equals(issue.getPriority(), Priority.LOW));

        assertThat(found).isEmpty();
    }

    @Test
    void testFindByPropertyResultImmutable() {
        Issues<Issue> issues = new Issues<>();
        issues.addAll(asList(HIGH, NORMAL_1, NORMAL_2));
        ImmutableSet<Issue> found = issues.findByProperty(issue -> Objects.equals(issue.getPriority(), Priority.HIGH));

        assertThat(found).hasSize(1);
        assertThat(found).containsExactly(HIGH);
    }

    @Test
    void shouldReturnIndexedValue() {
        Issues<Issue> issues = new Issues<>();
        issues.addAll(asList(HIGH, NORMAL_1, NORMAL_2));

        assertThat(issues.get(0)).isSameAs(HIGH);
        assertThat(issues.get(1)).isSameAs(NORMAL_1);
        assertThat(issues.get(2)).isSameAs(NORMAL_2);
        assertThatThrownBy(() -> issues.get(-1))
                .isInstanceOf(IndexOutOfBoundsException.class)
                .hasMessageContaining("-1");
        assertThatThrownBy(() -> issues.get(3))
                .isInstanceOf(IndexOutOfBoundsException.class)
                .hasMessageContaining("3");
    }

    @Test
    void shouldReturnFiles() {
        Issues<Issue> issues = new Issues<>();
        issues.addAll(asList(HIGH, NORMAL_1, NORMAL_2, LOW_FILE_2, ISSUE_5, LOW_FILE_3));

        assertThat(issues.getFiles()).contains("file-1", "file-1", "file-3");
    }

    @Test
    void shouldReturnSizeInToString() {
        Issues<Issue> issues = new Issues<>();
        issues.addAll(asList(HIGH, NORMAL_1, NORMAL_2));

        assertThat(issues.toString()).contains("3");
    }

    @Test
    void shouldReturnProperties() {
        Issues<Issue> issues = new Issues<>();
        issues.addAll(asList(HIGH, NORMAL_1, NORMAL_2, LOW_FILE_2, ISSUE_5, LOW_FILE_3));

        ImmutableSortedSet<String> properties = issues.getProperties(issue -> issue.getMessage());

        assertThat(properties)
                .contains(HIGH.getMessage())
                .contains(NORMAL_1.getMessage())
                .contains(NORMAL_2.getMessage());
    }

    @Test
    void testCopy() {
        Issues<Issue> original = new Issues<>();
        original.addAll(asList(HIGH, NORMAL_1, NORMAL_2));

        Issues<Issue> copy = original.copy();

        assertThat(copy).isNotSameAs(original);
        assertThat(copy.iterator()).containsExactly(HIGH, NORMAL_1, NORMAL_2);

        copy.add(LOW_FILE_2);
        assertThat(original.iterator()).containsExactly(HIGH, NORMAL_1, NORMAL_2);
        assertThat(copy.iterator()).containsExactly(HIGH, NORMAL_1, NORMAL_2, LOW_FILE_2);
    }

    @Test
    void shouldFilterByProperty() {
        assertFilterFor(IssueBuilder::setPackageName, Issues::getPackages, "packageName");
        assertFilterFor(IssueBuilder::setModuleName, Issues::getModules, "moduleName");
        assertFilterFor(IssueBuilder::setOrigin, Issues::getToolNames, "toolName");
        assertFilterFor(IssueBuilder::setCategory, Issues::getCategories, "category");
        assertFilterFor(IssueBuilder::setType, Issues::getTypes, "type");
    }

    private void assertFilterFor(final BiFunction<IssueBuilder, String, IssueBuilder> builderSetter,
            final Function<Issues<Issue>, ImmutableSortedSet<String>> propertyGetter, final String propertyName) {
        Issues<Issue> issues = new Issues<>();

        IssueBuilder builder = new IssueBuilder();

        for (int i = 1; i < 4; i++) {
            for (int j = i; j < 4; j++) {
                Issue build = builderSetter.apply(builder, "name " + i).setMessage(i + " " + j).build();
                issues.add(build);
            }
        }
        assertThat(issues).hasSize(6);

        ImmutableSortedSet<String> properties = propertyGetter.apply(issues);

        assertThat(properties).as("Wrong values for property " + propertyName).containsExactly("name 1", "name 2", "name 3");
    }

    @Test
    void shouldStoreAndRetrieveLogMessagesInCorrectOrder() {
        Issues<Issue> issues = new Issues<>();

        issues.log("%d: %s %s", 1, "Hello", "World");
        issues.log("%d: %s %s", 2, "Hello", "World");

        assertThat(issues.getLogMessages()).hasSize(2);
        assertThat(issues.getLogMessages()).containsExactly("1: Hello World", "2: Hello World");
    }

    @Test
    void shouldSupportSubTypes() {
        ExtendedIssueBuilder builder = new ExtendedIssueBuilder();

        Issues<ExtendedIssue> issues = new Issues<>();
        issues.add(builder.build());

        ExtendedIssue returnedIssue = issues.get(0);
        assertThat(returnedIssue.getAdditional()).isEqualTo(EXTENDED_VALUE);

        Issues<ExtendedIssue> filtered = issues.filter(issue -> issue.getAdditional().equals(EXTENDED_VALUE));
        assertThat(filtered).hasSize(1);
    }

    public static class ExtendedIssueBuilder extends IssueBuilder {
        @Override
        public ExtendedIssue build() {
            return new ExtendedIssue(fileName, lineStart, lineEnd, columnStart, columnEnd, category, type,
                    packageName, moduleName, priority, message, description, origin,
                    EXTENDED_VALUE);
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
            super(fileName, lineStart, lineEnd, columnStart, columnEnd, category, type, packageName, moduleName,
                    priority, message, description, origin, "FingerPrint");

            this.additional = additional;
        }

        public String getAdditional() {
            return additional;
        }
    }
}