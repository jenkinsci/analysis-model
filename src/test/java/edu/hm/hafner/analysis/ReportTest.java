package edu.hm.hafner.analysis;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import org.eclipse.collections.impl.block.factory.Predicates;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.util.SerializableTest;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import static edu.hm.hafner.analysis.assertions.Assertions.*;
import static java.util.Arrays.*;

/**
 * Unit tests for {@link Report}.
 *
 * @author Marcel Binder
 * @author Ullrich Hafner
 */
class ReportTest extends SerializableTest<Report> {
    private static final String SERIALIZATION_NAME = "report.ser";

    private static final Issue HIGH = new IssueBuilder().setMessage("issue-1")
            .setFileName("file-1")
            .setSeverity(Severity.WARNING_HIGH)
            .build();
    private static final Issue NORMAL_1 = new IssueBuilder().setMessage("issue-2")
            .setFileName("file-1")
            .setSeverity(Severity.WARNING_NORMAL)
            .build();
    private static final Issue NORMAL_2 = new IssueBuilder().setMessage("issue-3")
            .setFileName("file-1")
            .setSeverity(Severity.WARNING_NORMAL)
            .build();
    private static final Issue LOW_2_A = new IssueBuilder().setMessage("issue-4")
            .setFileName("file-2")
            .setSeverity(Severity.WARNING_LOW)
            .build();
    private static final Issue LOW_2_B = new IssueBuilder().setMessage("issue-5")
            .setFileName("file-2")
            .setSeverity(Severity.WARNING_LOW)
            .build();
    private static final Issue LOW_FILE_3 = new IssueBuilder().setMessage("issue-6")
            .setFileName("file-3")
            .setSeverity(Severity.WARNING_LOW)
            .build();
    private static final String NO_NAME = "";

    @Test
    void shouldProvideOriginMappings() {
        Report report = new Report();

        assertThat(report.getNameOfOrigin("id")).isEqualTo(NO_NAME);

        report.setNameOfOrigin("id", "name");
        assertThat(report.getNameOfOrigin("id")).isEqualTo("name");

        report.setNameOfOrigin("second", "another name");
        assertThat(report.getNameOfOrigin("id")).isEqualTo("name");
        assertThat(report.getNameOfOrigin("second")).isEqualTo("another name");

        report.setNameOfOrigin("id", "changed");
        assertThat(report.getNameOfOrigin("id")).isEqualTo("changed");
        assertThat(report.getNameOfOrigin("second")).isEqualTo("another name");
    }

    @Test
    void shouldMergeOriginMappings() {
        Report first = new Report();

        first.setNameOfOrigin("first", "first name");
        assertThat(first.getNameOfOrigin("first")).isEqualTo("first name");

        Report second = new Report();

        second.setNameOfOrigin("second", "second name");
        assertThat(second.getNameOfOrigin("second")).isEqualTo("second name");

        assertThat(first.getNameOfOrigin("second")).isEqualTo(NO_NAME);
        assertThat(second.getNameOfOrigin("first")).isEqualTo(NO_NAME);

        first.addAll(second);
        assertThat(first.getNameOfOrigin("first")).isEqualTo("first name");
        assertThat(first.getNameOfOrigin("second")).isEqualTo("second name");

        Report third = first.copyEmptyInstance();
        assertThat(third.getNameOfOrigin("first")).isEqualTo("first name");
        assertThat(third.getNameOfOrigin("second")).isEqualTo("second name");
    }

    @Test
    void shouldStoreFileNames() {
        Report report = new Report();

        assertThat(report.getFileNames()).isEmpty();

        report.addFileName("one");

        report.addFileName("one");
        assertThat(report.getFileNames()).containsExactly("one");

        report.addFileName("two");
        assertThat(report.getFileNames()).containsExactlyInAnyOrder("one", "two");
    }

    @Test
    void shouldVerifyExistenceOfProperties() {
        Report report = new Report();

        assertThat(report.hasTools()).isFalse();
        assertThat(report.hasModules()).isFalse();
        assertThat(report.hasPackages()).isFalse();
        assertThat(report.hasFiles()).isFalse();
        assertThat(report.hasFolders()).isFalse();
        assertThat(report.hasCategories()).isFalse();
        assertThat(report.hasTypes()).isFalse();
        assertThat(report.hasSeverities()).isFalse();

        IssueBuilder builder = new IssueBuilder();

        report.add(builder.build()); // add the first issue
        assertThat(report.hasTools()).isFalse();
        assertThat(report.hasModules()).isFalse();
        assertThat(report.hasPackages()).isFalse();
        assertThat(report.hasFiles()).isFalse();
        assertThat(report.hasFolders()).isFalse();
        assertThat(report.hasCategories()).isFalse();
        assertThat(report.hasTypes()).isFalse();
        assertThat(report.hasSeverities()).isFalse();

        verifyOrigin(report);
        verifyModule(report);
        verifyPackage(report);
        verifyFile(report);
        verifyFolder(report);
        verifyCategory(report);
        verifyType(report);
        verifySeverity(report);
    }

    private void verifySeverity(final Report report) {
        Issue additional = new IssueBuilder().setSeverity(Severity.WARNING_HIGH).build();
        report.add(additional);
        assertThat(report.hasTools()).isFalse();
        assertThat(report.hasModules()).isFalse();
        assertThat(report.hasPackages()).isFalse();
        assertThat(report.hasFiles()).isFalse();
        assertThat(report.hasFolders()).isFalse();
        assertThat(report.hasCategories()).isFalse();
        assertThat(report.hasTypes()).isFalse();
        assertThat(report.hasSeverities()).isTrue();
        report.remove(additional.getId());
    }

    private void verifyType(final Report report) {
        Issue additional = new IssueBuilder().setType("type").build();
        report.add(additional);
        assertThat(report.hasTools()).isFalse();
        assertThat(report.hasModules()).isFalse();
        assertThat(report.hasPackages()).isFalse();
        assertThat(report.hasFiles()).isFalse();
        assertThat(report.hasFolders()).isFalse();
        assertThat(report.hasCategories()).isFalse();
        assertThat(report.hasTypes()).isTrue();
        assertThat(report.hasSeverities()).isFalse();
        report.remove(additional.getId());
    }

    private void verifyCategory(final Report report) {
        Issue additional = new IssueBuilder().setCategory("category").build();
        report.add(additional);
        assertThat(report.hasTools()).isFalse();
        assertThat(report.hasModules()).isFalse();
        assertThat(report.hasPackages()).isFalse();
        assertThat(report.hasFiles()).isFalse();
        assertThat(report.hasFolders()).isFalse();
        assertThat(report.hasCategories()).isTrue();
        assertThat(report.hasTypes()).isFalse();
        assertThat(report.hasSeverities()).isFalse();
        report.remove(additional.getId());
    }

    private void verifyFile(final Report report) {
        Issue additional = new IssueBuilder().setFileName("file").build();
        report.add(additional);
        assertThat(report.hasTools()).isFalse();
        assertThat(report.hasModules()).isFalse();
        assertThat(report.hasPackages()).isFalse();
        assertThat(report.hasFiles()).isTrue();
        assertThat(report.hasFolders()).isFalse();
        assertThat(report.hasCategories()).isFalse();
        assertThat(report.hasTypes()).isFalse();
        assertThat(report.hasSeverities()).isFalse();
        report.remove(additional.getId());
    }

    @SuppressFBWarnings("DMI")
    private void verifyFolder(final Report report) {
        IssueBuilder builder = new IssueBuilder();
        Issue additional = builder.setFileName("/tmp/file.txt").build();
        report.add(additional);
        assertThat(report.hasTools()).isFalse();
        assertThat(report.hasModules()).isFalse();
        assertThat(report.hasPackages()).isFalse();
        assertThat(report.hasFiles()).isTrue();
        assertThat(report.hasFolders()).isTrue();
        assertThat(report.hasCategories()).isFalse();
        assertThat(report.hasTypes()).isFalse();
        assertThat(report.hasSeverities()).isFalse();

        Issue withPackageName = builder.setPackageName("something").build();
        report.add(withPackageName);
        assertThat(report.hasPackages()).isTrue();
        assertThat(report.hasFolders()).isFalse();

        report.remove(withPackageName.getId());
        report.remove(additional.getId());
    }

    private void verifyPackage(final Report report) {
        Issue additional = new IssueBuilder().setPackageName("package").build();
        report.add(additional);
        assertThat(report.hasTools()).isFalse();
        assertThat(report.hasModules()).isFalse();
        assertThat(report.hasPackages()).isTrue();
        assertThat(report.hasFiles()).isFalse();
        assertThat(report.hasFolders()).isFalse();
        assertThat(report.hasCategories()).isFalse();
        assertThat(report.hasTypes()).isFalse();
        assertThat(report.hasSeverities()).isFalse();
        report.remove(additional.getId());
    }

    private void verifyModule(final Report report) {
        Issue additional = new IssueBuilder().setModuleName("module").build();
        report.add(additional);
        assertThat(report.hasTools()).isFalse();
        assertThat(report.hasModules()).isTrue();
        assertThat(report.hasPackages()).isFalse();
        assertThat(report.hasFiles()).isFalse();
        assertThat(report.hasFolders()).isFalse();
        assertThat(report.hasCategories()).isFalse();
        assertThat(report.hasTypes()).isFalse();
        assertThat(report.hasSeverities()).isFalse();
        report.remove(additional.getId());
    }

    private void verifyOrigin(final Report report) {
        Issue additional = new IssueBuilder().setOrigin("origin").build();
        report.add(additional);
        assertThat(report.hasTools()).isTrue();
        assertThat(report.hasModules()).isFalse();
        assertThat(report.hasPackages()).isFalse();
        assertThat(report.hasFiles()).isFalse();
        assertThat(report.hasFolders()).isFalse();
        assertThat(report.hasCategories()).isFalse();
        assertThat(report.hasTypes()).isFalse();
        assertThat(report.hasSeverities()).isFalse();
        report.remove(additional.getId());
    }

    @Test
    void shouldFilterPriorities() {
        Report report = new Report();
        report.addAll(allIssuesAsList());

        assertThat(report.getSeverities())
                .containsExactlyInAnyOrder(Severity.WARNING_HIGH, Severity.WARNING_NORMAL, Severity.WARNING_LOW);
        
        report.add(new IssueBuilder().setSeverity(Severity.ERROR).build());
        assertThat(report.getSeverities()).containsExactlyInAnyOrder(
                Severity.WARNING_HIGH, Severity.WARNING_NORMAL, Severity.WARNING_LOW, Severity.ERROR);

        assertThat(report.getSizeOf(Severity.ERROR)).isEqualTo(1);
        assertThat(report.getSizeOf(Severity.WARNING_HIGH)).isEqualTo(1);
        assertThat(report.getSizeOf(Severity.WARNING_NORMAL)).isEqualTo(2);
        assertThat(report.getSizeOf(Severity.WARNING_LOW)).isEqualTo(3);

        assertThat(report.getSizeOf(Severity.ERROR.getName())).isEqualTo(1);
        assertThat(report.getSizeOf(Severity.WARNING_HIGH.getName())).isEqualTo(1);
        assertThat(report.getSizeOf(Severity.WARNING_NORMAL.getName())).isEqualTo(2);
        assertThat(report.getSizeOf(Severity.WARNING_LOW.getName())).isEqualTo(3);
    }

    @Test
    @SuppressWarnings("NullAway")
    void shouldGroupIssuesByProperty() {
        Report report = new Report();
        report.addAll(allIssuesAsList());

        Map<String, Report> byPriority = report.groupByProperty("severity");
        assertThat(byPriority).hasSize(3);
        assertThat(byPriority.get(Severity.WARNING_HIGH.toString())).hasSize(1);
        assertThat(byPriority.get(Severity.WARNING_NORMAL.toString())).hasSize(2);
        assertThat(byPriority.get(Severity.WARNING_LOW.toString())).hasSize(3);

        Map<String, Report> byFile = report.groupByProperty("fileName");
        assertThat(byFile).hasSize(3);
        assertThat(byFile.get("file-1")).hasSize(3);
        assertThat(byFile.get("file-2")).hasSize(2);
        assertThat(byFile.get("file-3")).hasSize(1);
    }

    /**
     * Ensures that each method that creates a copy of another issue instance also copies the corresponding properties.
     */
    @Test
    void shouldProvideNoWritingIterator() {
        Report report = new Report();
        report.addAll(HIGH, NORMAL_1, NORMAL_2, LOW_2_A, LOW_2_B, LOW_FILE_3);
        Iterator<Issue> iterator = report.iterator();
        iterator.next();
        assertThatThrownBy(iterator::remove).isInstanceOf(UnsupportedOperationException.class);
    }

    /**
     * Ensures that each method that creates a copy of another issue instance also copies the corresponding properties.
     */
    @Test
    void shouldCopyProperties() {
        Report expected = new Report();
        expected.addAll(HIGH, NORMAL_1, NORMAL_2, LOW_2_A, LOW_2_B, LOW_FILE_3);
        expected.logInfo("Hello");
        expected.logInfo("World!");
        expected.logError("Boom!");

        Report copy = expected.copy();
        assertThat(copy).isEqualTo(expected);
        assertThatAllIssuesHaveBeenAdded(copy);

        Report report = new Report();
        report.addAll(expected);
        assertThat(report).isEqualTo(expected);
        assertThatAllIssuesHaveBeenAdded(report);

        Report empty = expected.copyEmptyInstance();
        assertThat(empty).isEmpty();
        assertThat(empty.getErrorMessages()).isEqualTo(expected.getErrorMessages());
        assertThat(empty.getInfoMessages()).isEqualTo(expected.getInfoMessages());
        assertThat(empty.getDuplicatesSize()).isEqualTo(expected.getDuplicatesSize());

        Report filtered = expected.filter(Predicates.alwaysTrue());
        assertThat(filtered).isEqualTo(expected);
        assertThatAllIssuesHaveBeenAdded(filtered);
    }

    /** Verifies some additional variants of the {@link Report#addAll(Report[])}. */
    @Test
    void shouldVerifyPathInteriorCoverageOfAddAll() {
        Report first = new Report().add(HIGH);
        first.logInfo("1 info");
        first.logError("1 error");
        Report second = new Report().addAll(NORMAL_1, NORMAL_2);
        second.logInfo("2 info");
        second.logError("2 error");
        Report third = new Report().addAll(LOW_2_A, LOW_2_B, LOW_FILE_3);
        third.logInfo("3 info");
        third.logError("3 error");

        Report report = new Report();
        report.addAll(first);
        assertThat((Iterable<Issue>) report).containsExactly(HIGH);
        assertThat(report.getInfoMessages()).containsExactly("1 info");
        assertThat(report.getErrorMessages()).containsExactly("1 error");

        report.addAll(second, third);
        assertThatAllIssuesHaveBeenAdded(report);
        assertThat(report.getInfoMessages()).containsExactly("1 info", "2 info", "3 info");
        assertThat(report.getErrorMessages()).containsExactly("1 error", "2 error", "3 error");

        Report altogether = new Report();
        altogether.addAll(first, second, third);
        assertThatAllIssuesHaveBeenAdded(report);
        assertThat(report.getInfoMessages()).containsExactly("1 info", "2 info", "3 info");
        assertThat(report.getErrorMessages()).containsExactly("1 error", "2 error", "3 error");

        Report inConstructor = new Report(first, second, third);
        assertThatAllIssuesHaveBeenAdded(inConstructor);
        assertThat(inConstructor.getInfoMessages()).containsExactly("1 info", "2 info", "3 info");
        assertThat(inConstructor.getErrorMessages()).containsExactly("1 error", "2 error", "3 error");
    }
    
    @Test
    void shouldBeEmptyWhenCreated() {
        Report report = new Report();

        assertThat(report).isEmpty();
        assertThat(report.isNotEmpty()).isFalse();
        assertThat(report).hasSize(0);
        assertThat(report.size()).isEqualTo(0);
        assertThatReportHasSeverities(report, 0, 0, 0, 0);
    }

    private void assertThatReportHasSeverities(final Report report, final int expectedSizeError,
            final int expectedSizeHigh, final int expectedSizeNormal, final int expectedSizeLow) {
        assertThat(report.getSizeOf(Severity.ERROR)).isEqualTo(expectedSizeError);
        assertThat(report.getSizeOf(Severity.WARNING_HIGH)).isEqualTo(expectedSizeHigh);
        assertThat(report.getSizeOf(Severity.WARNING_NORMAL)).isEqualTo(expectedSizeNormal);
        assertThat(report.getSizeOf(Severity.WARNING_LOW)).isEqualTo(expectedSizeLow);
    }

    @Test
    void shouldAddMultipleIssuesOneByOne() {
        Report report = new Report();

        report.add(HIGH);
        report.add(NORMAL_1);
        report.add(NORMAL_2);
        report.add(LOW_2_A);
        report.add(LOW_2_B);
        report.add(LOW_FILE_3);

        assertThatAllIssuesHaveBeenAdded(report);
    }

    @Test
    void shouldAddMultipleIssuesAsCollection() {
        Report report = new Report();
        List<Issue> issueList = allIssuesAsList();

        report.addAll(issueList);

        assertThatAllIssuesHaveBeenAdded(report);
    }

    @Test
    void shouldIterateOverAllElementsInCorrectOrder() {
        Report report = new Report();

        report.add(HIGH);
        report.addAll(NORMAL_1, NORMAL_2);
        report.addAll(LOW_2_A, LOW_2_B, LOW_FILE_3);
        Iterator<Issue> iterator = report.iterator();
        assertThat(iterator.next()).isSameAs(HIGH);
        assertThat(iterator.next()).isSameAs(NORMAL_1);
        assertThat(iterator.next()).isSameAs(NORMAL_2);
        assertThat(iterator.next()).isSameAs(LOW_2_A);
        assertThat(iterator.next()).isSameAs(LOW_2_B);
        assertThat(iterator.next()).isSameAs(LOW_FILE_3);
    }

    @Test
    void shouldSkipAddedElements() {
        Report report = new Report().addAll(allIssuesAsList());

        Report fromEmpty = new Report();

        fromEmpty.addAll(report);
        assertThatAllIssuesHaveBeenAdded(fromEmpty);
        fromEmpty.addAll(report);
        assertThat(fromEmpty).hasSize(6)
                .hasDuplicatesSize(6);
        assertThatReportHasSeverities(report, 0, 1, 2, 3);

        Report left = new Report().addAll(HIGH, NORMAL_1, NORMAL_2);
        Report right = new Report().addAll(LOW_2_A, LOW_2_B, LOW_FILE_3);

        Report everything = new Report();
        everything.addAll(left, right);
        assertThat(everything).hasSize(6);
    }

    @Test
    void shouldAddMultipleIssuesToNonEmpty() {
        Report report = new Report();
        report.add(HIGH);

        report.addAll(asList(NORMAL_1, NORMAL_2));
        report.addAll(asList(LOW_2_A, LOW_2_B, LOW_FILE_3));

        assertThatAllIssuesHaveBeenAdded(report);
    }

    private void assertThatAllIssuesHaveBeenAdded(final Report report) {
        try (SoftAssertions softly = new SoftAssertions()) {
            softly.assertThat(report)
                    .hasSize(6)
                    .hasDuplicatesSize(0);
            assertThatReportHasSeverities(report, 0, 1, 2, 3);

            softly.assertThat(report.getFiles())
                    .containsExactly("file-1", "file-2", "file-3");
            softly.assertThat(report.getFiles())
                    .containsExactly("file-1", "file-2", "file-3");
            softly.assertThat((Iterable<Issue>) report)
                    .containsExactly(HIGH, NORMAL_1, NORMAL_2, LOW_2_A, LOW_2_B, LOW_FILE_3);
            softly.assertThat(report.isNotEmpty()).isTrue();
            softly.assertThat(report.size()).isEqualTo(6);

            softly.assertThat(report.getPropertyCount(Issue::getFileName)).containsEntry("file-1", 3);
            softly.assertThat(report.getPropertyCount(Issue::getFileName)).containsEntry("file-2", 2);
            softly.assertThat(report.getPropertyCount(Issue::getFileName)).containsEntry("file-3", 1);
        }
    }

    @Test
    void shouldSkipDuplicates() {
        Report report = new Report();
        report.add(HIGH);
        assertThat(report).hasSize(1).hasDuplicatesSize(0);
        report.add(HIGH);
        assertThat(report).hasSize(1).hasDuplicatesSize(1);
        report.addAll(asList(HIGH, LOW_2_A));
        assertThat(report).hasSize(2).hasDuplicatesSize(2);
        report.addAll(asList(NORMAL_1, NORMAL_2));
        assertThat(report).hasSize(4).hasDuplicatesSize(2);

        assertThat(report.iterator()).toIterable().containsExactly(HIGH, LOW_2_A, NORMAL_1, NORMAL_2);
        assertThatReportHasSeverities(report, 0, 1, 2, 1);
        assertThat(report.getFiles()).containsExactly("file-1", "file-2");
    }

    @Test
    void shouldRemoveById() {
        shouldRemoveOneIssue(HIGH, NORMAL_1, NORMAL_2);
        shouldRemoveOneIssue(NORMAL_1, HIGH, NORMAL_2);
        shouldRemoveOneIssue(NORMAL_1, NORMAL_2, HIGH);
    }

    private void shouldRemoveOneIssue(final Issue... initialElements) {
        Report report = new Report();
        report.addAll(asList(initialElements));

        assertThat(report.remove(HIGH.getId())).isEqualTo(HIGH);

        assertThat((Iterable<Issue>) report).containsExactly(NORMAL_1, NORMAL_2);
    }

    @Test
    void shouldThrowExceptionWhenRemovingWithWrongKey() {
        Report report = new Report();

        UUID id = HIGH.getId();
        assertThatThrownBy(() -> report.remove(id))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining(id.toString());
    }

    @Test
    void shouldFindIfOnlyOneIssue() {
        Report report = new Report();
        report.addAll(Collections.singletonList(HIGH));

        Issue found = report.findById(HIGH.getId());

        assertThat(found).isSameAs(HIGH);
    }

    @Test
    void shouldFindWithinMultipleIssues() {
        shouldFindIssue(HIGH, NORMAL_1, NORMAL_2);
        shouldFindIssue(NORMAL_1, HIGH, NORMAL_2);
        shouldFindIssue(NORMAL_1, NORMAL_2, HIGH);
    }

    private void shouldFindIssue(final Issue... elements) {
        Report report = new Report();
        report.addAll(asList(elements));

        Issue found = report.findById(HIGH.getId());

        assertThat(found).isSameAs(HIGH);
    }

    @Test
    void shouldThrowExceptionWhenSearchingWithWrongKey() {
        shouldFindNothing(HIGH);
        shouldFindNothing(HIGH, NORMAL_1);
    }

    private void shouldFindNothing(final Issue... elements) {
        Report report = new Report();
        report.addAll(asList(elements));

        UUID id = NORMAL_2.getId();
        assertThatThrownBy(() -> report.findById(id))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining(id.toString());
    }

    @Test
    void shouldReturnEmptyListIfPropertyDoesNotMatch() {
        Report report = new Report();
        report.addAll(asList(HIGH, NORMAL_1, NORMAL_2));

        Set<Issue> found = report.findByProperty(Issue.bySeverity(Severity.WARNING_LOW));

        assertThat(found).isEmpty();
    }

    @Test
    void testFindByProperty() {
        Report report = new Report();
        report.addAll(asList(HIGH, NORMAL_1, NORMAL_2));
        Set<Issue> found = report.findByProperty(Issue.bySeverity(Severity.WARNING_HIGH));

        assertThat(found).hasSize(1);
        assertThat(found).containsExactly(HIGH);
    }

    @Test
    void shouldReturnIndexedValue() {
        Report report = new Report();
        report.addAll(asList(HIGH, NORMAL_1, NORMAL_2));

        assertThat(report.get(0)).isSameAs(HIGH);
        assertThat(report.get(1)).isSameAs(NORMAL_1);
        assertThat(report.get(2)).isSameAs(NORMAL_2);
    }
    
    @Test @SuppressFBWarnings("RV")
    void shouldThrowExceptionOnWrongIndex() {
        Report report = new Report();
        report.addAll(asList(HIGH, NORMAL_1, NORMAL_2));

        assertThatThrownBy(() -> report.get(-1))
                .isInstanceOf(IndexOutOfBoundsException.class)
                .hasMessageContaining("-1");
        assertThatThrownBy(() -> report.get(3))
                .isInstanceOf(IndexOutOfBoundsException.class)
                .hasMessageContaining("3");
    }

    @Test
    void shouldReturnFiles() {
        Report report = new Report();
        report.addAll(allIssuesAsList());

        assertThat(report.getFiles()).contains("file-1", "file-1", "file-3");
    }

    private List<Issue> allIssuesAsList() {
        return asList(HIGH, NORMAL_1, NORMAL_2, LOW_2_A, LOW_2_B, LOW_FILE_3);
    }

    @Test
    void shouldReturnSizeInToString() {
        Report report = new Report();
        report.addAll(asList(HIGH, NORMAL_1, NORMAL_2));

        assertThat(report.toString()).contains("3");
    }

    @Test
    void shouldReturnProperties() {
        Report report = new Report();
        report.addAll(allIssuesAsList());

        Set<String> properties = report.getProperties(Issue::getMessage);

        assertThat(properties)
                .contains(HIGH.getMessage())
                .contains(NORMAL_1.getMessage())
                .contains(NORMAL_2.getMessage());
    }

    @Test
    void testCopy() {
        Report original = new Report();
        original.addAll(asList(HIGH, NORMAL_1, NORMAL_2));

        Report copy = original.copy();

        assertThat(copy).isNotSameAs(original);
        assertThat(copy.iterator()).toIterable().containsExactly(HIGH, NORMAL_1, NORMAL_2);

        copy.add(LOW_2_A);
        assertThat(original.iterator()).toIterable().containsExactly(HIGH, NORMAL_1, NORMAL_2);
        assertThat(copy.iterator()).toIterable().containsExactly(HIGH, NORMAL_1, NORMAL_2, LOW_2_A);
    }

    @Test
    void shouldFilterByProperty() {
        assertFilterFor(IssueBuilder::setPackageName, Report::getPackages, "packageName", Issue::byPackageName);
        assertFilterFor(IssueBuilder::setModuleName, Report::getModules, "moduleName", Issue::byModuleName);
        assertFilterFor(IssueBuilder::setOrigin, Report::getTools, "toolName", Issue::byOrigin);
        assertFilterFor(IssueBuilder::setCategory, Report::getCategories, "category", Issue::byCategory);
        assertFilterFor(IssueBuilder::setType, Report::getTypes, "type", Issue::byType);
        assertFilterFor(IssueBuilder::setFileName, Report::getFiles, "fileName", Issue::byFileName);
    }

    private void assertFilterFor(final BiFunction<IssueBuilder, String, IssueBuilder> builderSetter,
            final Function<Report, Set<String>> propertyGetter, final String propertyName, 
            final Function<String, Predicate<Issue>> predicate) {
        Report report = new Report();

        IssueBuilder builder = new IssueBuilder();

        for (int i = 1; i < 4; i++) {
            for (int j = i; j < 4; j++) {
                Issue build = builderSetter.apply(builder, "name " + i).setMessage(i + " " + j).build();
                report.add(build);
            }
        }
        assertThat(report).hasSize(6);

        Set<String> properties = propertyGetter.apply(report);

        assertThat(properties).as("Wrong values for property " + propertyName)
                .containsExactlyInAnyOrder("name 1", "name 2", "name 3");

        assertThat(report.filter(predicate.apply("name 1"))).hasSize(3);
        assertThat(report.filter(predicate.apply("name 2"))).hasSize(2);
        assertThat(report.filter(predicate.apply("name 3"))).hasSize(1);
    }

    @Test
    void shouldStoreAndRetrieveLogAndErrorMessagesInCorrectOrder() {
        Report report = new Report();

        assertThat(report.getInfoMessages()).hasSize(0);
        assertThat(report.getErrorMessages()).hasSize(0);

        report.logInfo("%d: %s %s", 1, "Hello", "World");
        report.logInfo("%d: %s %s", 2, "Hello", "World");

        assertThat(report.getInfoMessages()).hasSize(2);
        assertThat(report.getInfoMessages()).containsExactly("1: Hello World", "2: Hello World");

        report.logError("%d: %s %s", 1, "Hello", "World");
        report.logError("%d: %s %s", 2, "Hello", "World");

        assertThat(report.getInfoMessages()).hasSize(2);
        assertThat(report.getInfoMessages()).containsExactly("1: Hello World", "2: Hello World");
        assertThat(report.getErrorMessages()).hasSize(2);
        assertThat(report.getErrorMessages()).containsExactly("1: Hello World", "2: Hello World");
    }

    @Override
    protected Report createSerializable() {
        return new Report().addAll(HIGH, NORMAL_1, NORMAL_2, LOW_2_A, LOW_2_B, LOW_FILE_3);
    }

    /**
     * Verifies that saved serialized format (from a previous release) still can be resolved with the current
     * implementation of {@link Issue}.
     */
    @Test
    void shouldReadIssueFromOldSerialization() {
        byte[] restored = readAllBytes(SERIALIZATION_NAME);

        assertThatSerializableCanBeRestoredFrom(restored);
    }

    /** Verifies that equals checks all properties. */
    @Test
    void shouldBeNotEqualsAPropertyChanges() {
        Report report = new Report();
        report.addAll(HIGH, NORMAL_1, NORMAL_2, LOW_2_A, LOW_2_B, LOW_FILE_3);

        Report other = new Report();
        other.addAll(report);
        other.addAll(HIGH, NORMAL_1, NORMAL_2, LOW_2_A, LOW_2_B, LOW_FILE_3);

        assertThat(report).isNotEqualTo(other); // there should be duplicates
        assertThat(report).hasDuplicatesSize(0);
        assertThat(other).hasDuplicatesSize(6);
    }

    /**
     * Serializes an issues to a file. Use this method in case the issue properties have been changed and the
     * readResolve method has been adapted accordingly so that the old serialization still can be read.
     *
     * @param args
     *         not used
     *
     * @throws IOException
     *         if the file could not be written
     */
    public static void main(final String... args) throws IOException {
        new ReportTest().createSerializationFile();
    }

    @Override
    protected Class<?> getTestResourceClass() {
        return ReportTest.class;
    }
}
