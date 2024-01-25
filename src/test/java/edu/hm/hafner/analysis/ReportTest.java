package edu.hm.hafner.analysis;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
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
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.bridge.SLF4JBridgeHandler;

import edu.hm.hafner.analysis.Report.IssueFilterBuilder;
import edu.hm.hafner.analysis.Report.IssuePrinter;
import edu.hm.hafner.analysis.Report.StandardOutputPrinter;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.parser.checkstyle.CheckStyleParser;
import edu.hm.hafner.util.FilteredLog;
import edu.hm.hafner.util.PathUtil;
import edu.hm.hafner.util.SerializableTest;
import edu.hm.hafner.util.TreeString;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import nl.jqno.equalsverifier.EqualsVerifier;

import static edu.hm.hafner.analysis.assertions.Assertions.*;
import static java.util.Arrays.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link Report}.
 *
 * @author Marcel Binder
 * @author Ullrich Hafner
 */
@SuppressWarnings({"PMD.GodClass", "PMD.ExcessiveImports", "PMD.ExcessiveClassLength", "checkstyle:ClassDataAbstractionCoupling"})
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
    private static final int VALUE = 1234;
    private static final String KEY = "key";

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String UNDEFINED = Report.DEFAULT_ID;
    private static final String SOURCE_FILE = "report.xml";
    private static final String CHECKSTYLE_ID = "checkstyle";
    private static final String CHECKSTYLE_NAME = "CheckStyle";
    private static final String SPOTBUGS_ID = "spotbugs";
    private static final String SPOTBUGS_NAME = "SpotBugs";

    @BeforeAll
    static void beforeAll() {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    @Test
    void shouldFindIssuesInModifiedCode() {
        Report report = new Report();
        report.addAll(allIssuesAsList());

        assertThat(report).hasNoInModifiedCode();

        report.get(0).markAsPartOfModifiedCode();
        assertThat(report).hasInModifiedCode(report.get(0));
        report.get(2).markAsPartOfModifiedCode();
        assertThat(report).hasInModifiedCode(report.get(0), report.get(2));
    }

    @Test
    void shouldMergeLog() {
        var issues = new Report();

        issues.logInfo("info");
        issues.logError("error");

        assertThat(issues).hasOnlyInfoMessages("info");
        assertThat(issues).hasOnlyErrorMessages("error");

        var log = new FilteredLog("title");
        log.logInfo("filtered info");
        log.logError("filtered error");

        issues.mergeLogMessages(log);

        assertThat(issues).hasOnlyInfoMessages("info", "filtered info");
        assertThat(issues).hasOnlyErrorMessages("error", "title", "filtered error");
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

        try (IssueBuilder builder = new IssueBuilder()) {
            report.add(builder.build()); // add the first issue
        }
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
        try (IssueBuilder issueBuilder = new IssueBuilder()) {
            Issue additional;
            additional = issueBuilder.setSeverity(Severity.WARNING_HIGH).build();
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
    }

    private void verifyType(final Report report) {
        try (IssueBuilder issueBuilder = new IssueBuilder()) {
            Issue additional = issueBuilder.setType("type").build();
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
    }

    private void verifyCategory(final Report report) {
        try (IssueBuilder issueBuilder = new IssueBuilder()) {
            Issue additional = issueBuilder.setCategory("category").build();
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
    }

    private void verifyFile(final Report report) {
        try (IssueBuilder issueBuilder = new IssueBuilder()) {
            Issue additional = issueBuilder.setFileName("file").build();
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
    }

    @SuppressFBWarnings("DMI")
    private void verifyFolder(final Report report) {
        try (IssueBuilder builder = new IssueBuilder()) {
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
    }

    private void verifyPackage(final Report report) {
        try (IssueBuilder issueBuilder = new IssueBuilder()) {
            Issue additional = issueBuilder.setPackageName("package").build();
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
    }

    private void verifyModule(final Report report) {
        try (IssueBuilder issueBuilder = new IssueBuilder()) {
            Issue additional = issueBuilder.setModuleName("module").build();
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
    }

    private void verifyOrigin(final Report report) {
        try (IssueBuilder issueBuilder = new IssueBuilder()) {
            Issue additional = issueBuilder.setOrigin("origin").build();
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
        Report expected = new Report(ID, NAME, SOURCE_FILE);
        expected.addAll(HIGH, NORMAL_1, NORMAL_2, LOW_2_A, LOW_2_B, LOW_FILE_3);
        expected.logInfo("Hello");
        expected.logInfo("World!");
        expected.logError("Boom!");
        expected.setCounter(KEY, VALUE);

        Report copy = expected.copy();
        assertThat(copy).isEqualTo(expected);
        assertThatAllIssuesHaveBeenAdded(copy);

        Report report = new Report();
        report.addAll(expected);
        assertThatAllIssuesHaveBeenAdded(report);

        Report empty = expected.copyEmptyInstance();
        assertThat(empty).isEmpty();
        assertThat(empty.getErrorMessages()).isEqualTo(expected.getErrorMessages());
        assertThat(empty.getInfoMessages()).isEqualTo(expected.getInfoMessages());
        assertThat(empty.getDuplicatesSize()).isEqualTo(expected.getDuplicatesSize());
        assertThat(empty.getCounter(KEY)).isEqualTo(VALUE);
        assertThat(empty.hasCounter(KEY)).isTrue();
        assertThat(empty.getCounter("other")).isZero();
        assertThat(empty.hasCounter("other")).isFalse();

        Report filtered = expected.filter(issue -> true);
        assertThat(filtered).isEqualTo(expected);
        assertThatAllIssuesHaveBeenAdded(filtered);
    }

    @Test
    void shouldSumCountersOnMerge() {
        Report first = new Report();
        first.addAll(HIGH, NORMAL_1, NORMAL_2);
        first.addAll(HIGH, NORMAL_1, NORMAL_2); // 3 duplicates
        first.setCounter(KEY, 10);
        first.setCounter("one", 100);

        Report second = new Report();
        second.addAll(LOW_2_A, LOW_2_B);
        second.addAll(LOW_2_A, LOW_2_B); // 2 duplicates
        second.setCounter(KEY, 1);
        first.setCounter("two", 1000);

        first.addAll(second);

        assertThat(first.getDuplicatesSize()).isEqualTo(3 + 2);
        assertThat(first.getCounter(KEY)).isEqualTo(10 + 1);
        assertThat(first.getCounter("one")).isEqualTo(100);
        assertThat(first.getCounter("two")).isEqualTo(1000);
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

        fromEmpty.addAll(report.get());
        assertThatAllIssuesHaveBeenAdded(fromEmpty);
        fromEmpty.addAll(report.get());
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
            softly.assertThat(report.getAbsolutePaths())
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

    @Test
    @SuppressFBWarnings("RV")
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
        try (IssueBuilder builder = new IssueBuilder()) {
            Report report = new Report();

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
        Report report = new Report(ID, NAME, SOURCE_FILE).addAll(HIGH, NORMAL_1, NORMAL_2);
        report.addAll(HIGH, NORMAL_1, NORMAL_2); // 6 duplicates
        report.setCounter(KEY, VALUE);
        report.logInfo("info1");
        report.logInfo("info2");
        report.logError("error1");
        report.logError("error2");

        Report subReport = new Report(ID, NAME, SOURCE_FILE);
        subReport.addAll(LOW_2_A, LOW_2_B, LOW_FILE_3);
        subReport.addAll(LOW_2_A, LOW_2_B, LOW_FILE_3);
        subReport.setCounter("subreport", 10); // FIXME: addition?
        subReport.logInfo("sub.info1");
        subReport.logInfo("sub.info2");
        subReport.logError("sub.error1");
        subReport.logError("sub.error2");

        report.addAll(subReport);

        return report;
    }

    @Override
    protected void assertThatRestoredInstanceEqualsOriginalInstance(final Report original, final Report restored) {
        assertThat(original).isEqualTo(restored);
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

    @Test
    void shouldWriteLongMessages() {
        try (IssueBuilder builder = new IssueBuilder()) {
            Report report = new Report();

            report.add(builder.setMessage(createLongMessage()).build());

            byte[] bytes = toByteArray(report);
            Report restored = restore(bytes);

            assertThat(report).isEqualTo(restored);
        }
    }

    private String createLongMessage() {
        char[] chars = new char[100_000];

        return new String(chars);
    }

    /** Verifies that equals checks all properties. */
    @Test
    void shouldBeNotEqualsAPropertyChanges() {
        Report report = new Report();
        report.addAll(HIGH, NORMAL_1, NORMAL_2, LOW_2_A, LOW_2_B, LOW_FILE_3);

        Report other = new Report();
        other.addAll(report);

        assertThat(report).isNotEqualTo(other); // there should be duplicates
    }

    @Test
    void shouldPrintAllIssues() {
        Report report = readCheckStyleReport();

        IssuePrinter printer = mock(IssuePrinter.class);
        report.print(printer);

        for (Issue issue : report) {
            verify(printer).print(issue);
        }
    }

    @Test
    void shouldPrintAllIssuesToPrintStream() {
        Report report = readCheckStyleReport();

        try (PrintStream printStream = mock(PrintStream.class)) {
            report.print(new StandardOutputPrinter(printStream));

            for (Issue issue : report) {
                verify(printStream).println(issue.toString());
            }
        }
    }

    private Report readCheckStyleReport() {
        String fileName = "parser/checkstyle/all-severities.xml";
        Report report = new CheckStyleParser().parseFile(read(fileName));
        report.add(new IssueBuilder().setSeverity(Severity.WARNING_HIGH).setMessage("Severity High warning").build());
        assertThat(report).hasSize(4);
        assertThat(report.getSeverities()).hasSize(4);
        assertThat(report).hasOriginReportFile(new PathUtil().getAbsolutePath(getResourceAsFile(fileName)));
        return report;
    }

    private ReaderFactory read(final String fileName) {
        return new FileReaderFactory(getResourceAsFile(fileName), StandardCharsets.UTF_8);
    }

    @Test
    void shouldCreateReportWithOptions() {
        assertThat(new Report()).hasId(UNDEFINED)
                .hasName(UNDEFINED)
                .hasOriginReportFile(UNDEFINED)
                .hasNoOriginReportFiles();
        assertThat(new Report(ID, NAME)).hasId(ID)
                .hasName(NAME)
                .hasOriginReportFile(UNDEFINED)
                .hasNoOriginReportFiles();
        assertThat(new Report(ID, NAME, SOURCE_FILE)).hasId(ID)
                .hasName(NAME)
                .hasOriginReportFile(SOURCE_FILE)
                .hasOnlyOriginReportFiles(SOURCE_FILE);
    }

    @Test
    void shouldSetOriginAndReference() {
        try (IssueBuilder builder = new IssueBuilder()) {
            Report report = new Report();
            Issue checkstyleWarning = builder.setFileName("A.java")
                    .setCategory("Style")
                    .setLineStart(1)
                    .buildAndClean();
            report.add(checkstyleWarning);

            assertThat(checkstyleWarning).hasOrigin("");
            assertThat(checkstyleWarning).hasOriginName("");

            report.setOrigin("origin", "Name");

            assertThat(checkstyleWarning).hasOrigin("origin");
            assertThat(checkstyleWarning).hasOriginName("Name");
            assertThat(checkstyleWarning).hasReference("");

            var reference = "reference";
            report.setReference(reference);
            assertThat(checkstyleWarning).hasReference(reference);
        }
    }

    @Test
    void shouldCopyMessagesRecursively() {
        Report checkStyle = new Report(CHECKSTYLE_ID, CHECKSTYLE_NAME, "checkstyle.xml");
        checkStyle.logInfo("Info message from %s", CHECKSTYLE_NAME);
        checkStyle.logError("Error message from %s", CHECKSTYLE_NAME);

        Report spotBugs = new Report(SPOTBUGS_ID, SPOTBUGS_NAME, "spotbugs.xml");
        spotBugs.logInfo("Info message from %s", SPOTBUGS_NAME);
        spotBugs.logError("Error message from %s", SPOTBUGS_NAME);

        Report wrappedCheckStyle = new Report();
        wrappedCheckStyle.addAll(checkStyle);
        assertThat(wrappedCheckStyle.getNameOfOrigin(CHECKSTYLE_ID)).isEqualTo(CHECKSTYLE_NAME);
        assertThat(wrappedCheckStyle.getEffectiveId()).isEqualTo(CHECKSTYLE_ID);
        assertThat(wrappedCheckStyle.getEffectiveName()).isEqualTo(CHECKSTYLE_NAME);
        wrappedCheckStyle.logInfo("Info (Wrapped) message from %s", CHECKSTYLE_NAME);
        wrappedCheckStyle.logError("Error (Wrapped) message from %s", CHECKSTYLE_NAME);

        Report wrappedSpotBugs = new Report();
        wrappedSpotBugs.addAll(spotBugs);
        assertThat(wrappedSpotBugs.getNameOfOrigin(SPOTBUGS_ID)).isEqualTo(SPOTBUGS_NAME);
        assertThat(wrappedSpotBugs.getEffectiveId()).isEqualTo(SPOTBUGS_ID);
        assertThat(wrappedSpotBugs.getEffectiveName()).isEqualTo(SPOTBUGS_NAME);
        wrappedSpotBugs.logInfo("Info (Wrapped) message from %s", SPOTBUGS_NAME);
        wrappedSpotBugs.logError("Error (Wrapped) message from %s", SPOTBUGS_NAME);

        Report aggregated = new Report();
        aggregated.addAll(wrappedCheckStyle, wrappedSpotBugs);
        assertThat(aggregated.getEffectiveId()).isEqualTo(Report.DEFAULT_ID);
        assertThat(aggregated.getEffectiveName()).isEqualTo(Report.DEFAULT_ID);
        aggregated.logInfo("Info (Aggregated) message");
        aggregated.logError("Error (Aggregated) message");

        assertThat(aggregated.getNameOfOrigin(CHECKSTYLE_ID)).isEqualTo(CHECKSTYLE_NAME);
        assertThat(aggregated.getNameOfOrigin(SPOTBUGS_ID)).isEqualTo(SPOTBUGS_NAME);

        assertThat(aggregated.getInfoMessages()).contains(
                "Info message from CheckStyle",
                "Info message from SpotBugs",
                "Info (Wrapped) message from CheckStyle",
                "Info (Wrapped) message from SpotBugs",
                "Info (Aggregated) message");
        assertThat(aggregated.getErrorMessages()).contains(
                "Error message from CheckStyle",
                "Error message from SpotBugs",
                "Error (Wrapped) message from CheckStyle",
                "Error (Wrapped) message from SpotBugs",
                "Error (Aggregated) message");
        assertThat(aggregated).hasOriginReportFiles("checkstyle.xml", "spotbugs.xml");
    }

    @Test
    void shouldCopyIdRecursively() {
        Report first = new Report();
        Report second = new Report();

        Report aggregated = new Report();
        aggregated.addAll(first, second);
        aggregated.setOrigin(ID, NAME);

        assertThat(aggregated).hasId(ID);
        assertThat(aggregated).hasName(NAME);
        assertThat(aggregated.getSubReports()).hasSize(2).allSatisfy(report -> {
            assertThat(report).hasId(ID);
            assertThat(report).hasName(NAME);
        });
    }

    @Test
    void shouldAddSubReports() {
        try (IssueBuilder builder = new IssueBuilder()) {
            Report checkStyle = new Report(CHECKSTYLE_ID, CHECKSTYLE_NAME, "checkstyle.xml");
            Issue checkstyleWarning = builder.setFileName("A.java")
                    .setCategory("Style")
                    .setLineStart(1)
                    .buildAndClean();
            checkStyle.add(checkstyleWarning);
            checkStyle.add(builder.setFileName("A.java").setCategory("Style").setLineStart(1).buildAndClean());
            checkStyle.logInfo("Info message from %s", CHECKSTYLE_NAME);
            checkStyle.logError("Error message from %s", CHECKSTYLE_NAME);

            assertThat(checkStyle).hasSize(1)
                    .hasDuplicatesSize(1)
                    .hasId(CHECKSTYLE_ID)
                    .hasEffectiveId(CHECKSTYLE_ID)
                    .hasName(CHECKSTYLE_NAME)
                    .hasEffectiveName(CHECKSTYLE_NAME)
                    .hasOriginReportFile("checkstyle.xml")
                    .hasOnlyOriginReportFiles("checkstyle.xml");
            assertThat(checkStyle.get(0)).isSameAs(checkstyleWarning);
            assertThat(checkStyle.findById(checkstyleWarning.getId())).isSameAs(checkstyleWarning);

            Report spotBugs = new Report(SPOTBUGS_ID, SPOTBUGS_NAME, "spotbugs.xml");
            Issue spotBugsWarning = builder.setFileName("A.java").setCategory("Style").setLineStart(1).buildAndClean();
            spotBugs.add(spotBugsWarning);
            spotBugs.add(builder.setFileName("A.java").setCategory("Style").setLineStart(1).buildAndClean());

            assertThat(spotBugs).hasSize(1)
                    .hasDuplicatesSize(1)
                    .hasId(SPOTBUGS_ID)
                    .hasEffectiveId(SPOTBUGS_ID)
                    .hasName(SPOTBUGS_NAME)
                    .hasEffectiveName(SPOTBUGS_NAME)
                    .hasOriginReportFile("spotbugs.xml")
                    .hasOnlyOriginReportFiles("spotbugs.xml");
            assertThat(spotBugs.get(0)).isSameAs(spotBugsWarning);
            assertThat(spotBugs.findById(spotBugsWarning.getId())).isSameAs(spotBugsWarning);
            spotBugs.logInfo("Info message from %s", SPOTBUGS_NAME);
            spotBugs.logError("Error message from %s", SPOTBUGS_NAME);

            Report container = new Report();
            container.setOrigin("container", "Aggregation");
            container.addAll(checkStyle, spotBugs);
            verifyContainer(container, checkstyleWarning, spotBugsWarning);

            assertThat(checkStyle).hasId(CHECKSTYLE_ID).hasName(CHECKSTYLE_NAME).hasErrors();
            assertThat(spotBugs).hasId(SPOTBUGS_ID).hasName(SPOTBUGS_NAME).hasErrors();

            IssueFilterBuilder filterBuilder = new IssueFilterBuilder();
            Predicate<Issue> predicate = filterBuilder.setIncludeCategoryFilter("Style").build();

            assertThat(checkStyle.filter(predicate)).hasSize(1).hasId(CHECKSTYLE_ID).hasName(CHECKSTYLE_NAME);

            Report filtered = container.filter(predicate);
            verifyContainer(filtered, checkstyleWarning, spotBugsWarning);

            Report copy = container.copy();
            verifyContainer(copy, checkstyleWarning, spotBugsWarning);

            Report copyOfCopy = copy.copy();
            verifyContainer(copyOfCopy, checkstyleWarning, spotBugsWarning);

            assertThat(container.stream().map(Issue::getOrigin).collect(Collectors.toSet()))
                    .containsOnly(CHECKSTYLE_ID, SPOTBUGS_ID);
            assertThat(container.stream().map(Issue::getOriginName).collect(Collectors.toSet()))
                    .containsOnly(CHECKSTYLE_NAME, SPOTBUGS_NAME);
            checkStyle.setOrigin("nothing", "Nothing");
            spotBugs.setOrigin("nothing", "Nothing");
            assertThat(checkStyle).hasId("nothing").hasName("Nothing");
            assertThat(spotBugs).hasId("nothing").hasName("Nothing");
            assertThat(container.stream().map(Issue::getOrigin).collect(Collectors.toSet()))
                    .containsOnly("nothing");
            assertThat(container.stream().map(Issue::getOriginName).collect(Collectors.toSet()))
                    .containsOnly("Nothing");
        }
    }

    private void verifyContainer(final Report container, final Issue checkstyleWarning, final Issue spotBugsWarning) {
        assertThat(container).hasSize(2)
                .hasDuplicatesSize(2)
                .hasId("container")
                .hasEffectiveId("container")
                .hasName("Aggregation")
                .hasEffectiveName("Aggregation")
                .hasOriginReportFile("-")
                .hasOnlyOriginReportFiles("checkstyle.xml", "spotbugs.xml");
        assertThat(container.getInfoMessages()).contains(
                "Info message from CheckStyle", "Info message from SpotBugs");
        assertThat(container.getErrorMessages()).contains(
                "Error message from CheckStyle", "Error message from SpotBugs");
        assertThat(container).hasErrors();

        assertThat(container.get(0)).isSameAs(checkstyleWarning);
        assertThat(container.get(1)).isSameAs(spotBugsWarning);
        assertThat(container).hasOnlySeverities(Severity.WARNING_NORMAL);
        assertThat(container.getSizeOf(Severity.WARNING_NORMAL)).isEqualTo(2);
        assertThat(container.getNameOfOrigin(CHECKSTYLE_ID)).isEqualTo(CHECKSTYLE_NAME);
        assertThat(container.getNameOfOrigin(SPOTBUGS_ID)).isEqualTo(SPOTBUGS_NAME);
        assertThat(container.getNameOfOrigin("container")).isEqualTo("Aggregation");

        assertThat(container.findById(checkstyleWarning.getId())).isSameAs(checkstyleWarning);
        assertThat(container.findById(spotBugsWarning.getId())).isSameAs(spotBugsWarning);
    }

    @Test
    void shouldObeyEqualsContract() {
        EqualsVerifier.simple()
                .forClass(Report.class)
                .withPrefabValues(Report.class, new Report("left", "Left"), new Report("right", "Right"))
                .withPrefabValues(TreeString.class, TreeString.valueOf("One"), TreeString.valueOf("Two"))
                .withPrefabValues(LineRangeList.class, new LineRangeList(new LineRange(2, 2)), new LineRangeList(new LineRange(1, 1)))
                .verify();
    }

    @Override
    protected Class<?> getTestResourceClass() {
        return ReportTest.class;
    }

    static final class ReportWriter {
        private ReportWriter() {
            // prevents instantiation
        }

        /**
         * Serializes a {@link Report} to a file. Use this method in case the report properties have been changed and
         * the readResolve method has been adapted accordingly so that the old serialization still can be read.
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
    }
}
