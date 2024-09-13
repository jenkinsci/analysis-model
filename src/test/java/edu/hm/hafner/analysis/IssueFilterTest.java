package edu.hm.hafner.analysis;

import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Report.IssueFilterBuilder;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Unit test for {@link IssueFilterBuilder}.
 *
 * @author Raphael Furch
 */
@SuppressWarnings("resource")
class IssueFilterTest {
    private static final Issue ISSUE1 = new IssueBuilder()
            .setFileName("FileName1")
            .setPackageName("PackageName1")
            .setModuleName("ModuleName1")
            .setCategory("CategoryName1")
            .setType("Type1")
            .setMessage("Message1")
            .build();
    private static final Issue ISSUE2 = new IssueBuilder()
            .setFileName("FileName2")
            .setPackageName("PackageName2")
            .setModuleName("ModuleName2")
            .setCategory("CategoryName2")
            .setType("Type2")
            .setMessage("Message2")
            .build();

    private static final Issue ISSUE3 = new IssueBuilder()
            .setFileName("FileName3")
            .setPackageName("PackageName3")
            .setModuleName("ModuleName3")
            .setCategory("CategoryName3")
            .setType("Type3")
            .setMessage("Message3")
            .build();

    /**
     * Verifies that apostrophes can be used in the regular expression.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-57448">Issue 57448</a>
     */
    @Test
    void shouldHandleApostrophe() {
        var predicate = new IssueFilterBuilder().setExcludeMessageFilter(
                "'tools.jar' was not found, kapt may work unreliably").build();

        var report = new Report();
        report.add(new IssueBuilder().setMessage("'tools.jar' was not found, kapt may work unreliably").build());

        var filtered = report.filter(predicate);
        assertThat(filtered).hasSize(0);
    }

    @Test
    void shouldUseFindRatherThanMatch() {
        var predicate = new IssueFilterBuilder().setIncludeMessageFilter("something").build();

        var report = new Report();
        report.add(new IssueBuilder().setLineStart(1).setMessage("something").build());
        report.add(new IssueBuilder().setLineStart(2).setMessage(" something").build());
        report.add(new IssueBuilder().setLineStart(3).setMessage("something ").build());
        report.add(new IssueBuilder().setLineStart(4).setMessage(" something ").build());
        report.add(new IssueBuilder().setLineStart(5).setMessage("Before something After").build());
        report.add(new IssueBuilder().setLineStart(6).setMessage("Before something").build());
        report.add(new IssueBuilder().setLineStart(7).setMessage("something After").build());

        var filtered = report.filter(predicate);
        assertThat(filtered).hasSize(7);
    }

    @Test
    void shouldMatchMultiLinesInMessage() {
        var predicate = new IssueFilterBuilder().setExcludeMessageFilter(".*something.*").build();

        var report = new Report();
        report.add(new IssueBuilder().setMessage("something").build());
        report.add(new IssueBuilder().setMessage("something\nelse").build());
        report.add(new IssueBuilder().setMessage("else\nsomething").build());

        var filtered = report.filter(predicate);
        assertThat(filtered).hasSize(0);
    }

    /**
     * Verifies that the message filter reads the description and the message.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-56526">Issue 56526</a>
     */
    @Test
    void shouldMatchMultiLinesInDescription() {
        var predicate = new IssueFilterBuilder().setExcludeMessageFilter(".*something.*").build();

        var report = new Report();
        report.add(new IssueBuilder().setDescription("something").build());
        report.add(new IssueBuilder().setDescription("something\nelse").build());
        report.add(new IssueBuilder().setDescription("else\nsomething").build());

        var filtered = report.filter(predicate);
        assertThat(filtered).hasSize(0);
    }

    /**
     * Verifies that the message filter reads the description and the message.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-56526">Issue 56526</a>
     */
    @Test
    void shouldMatchMessageOrDescriptionInIncludeFilter() {
        var predicate = new IssueFilterBuilder().setIncludeMessageFilter(".*something.*").build();

        var report = new Report();
        report.add(new IssueBuilder().setDescription("something").setMessage("else").build());
        report.add(new IssueBuilder().setDescription("else").setMessage("something").build());
        report.add(new IssueBuilder().setDescription("else").setMessage("else").build());
        report.add(new IssueBuilder().setDescription("something").setMessage("something").build());

        var filtered = report.filter(predicate);
        assertThat(filtered).hasSize(3);
    }

    @Test
    void shouldNothingChangeWhenNoFilterIsAdded() {
        var filter = new IssueFilterBuilder().build();
        applyFilterAndCheckResult(filter, getIssues(), ISSUE1, ISSUE2, ISSUE3);
    }

    @Test
    void shouldPassAllWhenUselessFilterIsAdded() {
        var filter = new IssueFilterBuilder()
                .setIncludeFileNameFilter("[a-zA-Z1]*")
                .setIncludeFileNameFilter("[a-zA-Z2]*")
                .setIncludeFileNameFilter("[a-zA-Z3]*")
                .build();
        applyFilterAndCheckResult(filter, getIssues(), ISSUE1, ISSUE2, ISSUE3);
    }

    @Test
    void shouldPassAllWhenUselessFilterIsAddedAsList() {
        var filter = new IssueFilterBuilder()
                .setIncludeFileNameFilter("[a-zA-Z1]*", "[a-zA-Z2]*", "[a-zA-Z3]*")
                .build();
        applyFilterAndCheckResult(filter, getIssues(), ISSUE1, ISSUE2, ISSUE3);
    }

    @Test
    void shouldPassNoWhenMasterFilterIsAdded() {
        var filter = new IssueFilterBuilder()
                .setExcludeFileNameFilter("[a-zA-Z_1-3]*")
                .build();
        applyFilterAndCheckResult(filter, getIssues());
    }

    @Test
    void shouldPassNoWhenMasterFilterIsAddedAsList() {
        var filter = new IssueFilterBuilder()
                .setExcludeFileNameFilter("[a-zA-Z1]*", "[a-zA-Z2]*", "[a-zA-Z3]*")
                .build();
        applyFilterAndCheckResult(filter, getIssues());
    }

    @Test
    void shouldFindIssue1ByAFileNameIncludeMatch() {
        var filter = new IssueFilterBuilder()
                .setIncludeFileNameFilter("FileName1")
                .build();
        applyFilterAndCheckResult(filter, getIssues(), ISSUE1);
    }

    @Test
    void shouldFindIssue1ByAFileNameExcludeMatch() {
        var filter = new IssueFilterBuilder()
                .setExcludeFileNameFilter("FileName1")
                .build();
        applyFilterAndCheckResult(filter, getIssues(), ISSUE2, ISSUE3);
    }

    @Test
    void shouldFindIssue2ByAPackageNameIncludeMatch() {
        var filter = new IssueFilterBuilder()
                .setIncludePackageNameFilter("PackageName2")
                .build();

        applyFilterAndCheckResult(filter, getIssues(), ISSUE2);
    }

    @Test
    void shouldFindIssue2ByAPackageNameExcludeMatch() {
        var filter = new IssueFilterBuilder()
                .setExcludePackageNameFilter("PackageName2")
                .build();
        applyFilterAndCheckResult(filter, getIssues(), ISSUE1, ISSUE3);
    }

    @Test
    void shouldFindIssue3ByAModuleNameIncludeMatch() {
        var filter = new IssueFilterBuilder()
                .setIncludeModuleNameFilter("ModuleName3")
                .build();

        applyFilterAndCheckResult(filter, getIssues(), ISSUE3);
    }

    @Test
    void shouldFindIssue3ByAModuleNameExcludeMatch() {
        var filter = new IssueFilterBuilder()
                .setExcludeModuleNameFilter("ModuleName3")
                .build();
        applyFilterAndCheckResult(filter, getIssues(), ISSUE1, ISSUE2);
    }

    @Test
    void shouldFindIssue1ByACategoryIncludeMatch() {
        var filter = new IssueFilterBuilder()
                .setIncludeCategoryFilter("CategoryName1")
                .build();
        applyFilterAndCheckResult(filter, getIssues(), ISSUE1);
    }

    @Test
    void shouldFindIssue1ByACategoryExcludeMatch() {
        var filter = new IssueFilterBuilder()
                .setExcludeCategoryFilter("CategoryName1")
                .build();
        applyFilterAndCheckResult(filter, getIssues(), ISSUE2, ISSUE3);
    }

    @Test
    void shouldFindIssue2ByATypeIncludeMatch() {
        var filter = new IssueFilterBuilder()
                .setIncludeTypeFilter("Type2")
                .build();
        applyFilterAndCheckResult(filter, getIssues(), ISSUE2);
    }

    @Test
    void shouldFindIssue2ByACategoryExcludeMatch() {
        var filter = new IssueFilterBuilder()
                .setExcludeTypeFilter("Type2")
                .build();
        applyFilterAndCheckResult(filter, getIssues(), ISSUE1, ISSUE3);
    }

    @Test
    void shouldFindIntersectionFromIncludeAndExcludeBySameProperty() {
        var filter = new IssueFilterBuilder()
                .setIncludeFileNameFilter("FileName1")
                .setIncludeFileNameFilter("FileName2")
                .setExcludeFileNameFilter("FileName2")
                .build();
        applyFilterAndCheckResult(filter, getIssues(), ISSUE1);
    }

    @Test
    void shouldFindIntersectionFromIncludeAndExcludeByOtherProperty() {
        var filter = new IssueFilterBuilder()
                .setIncludeFileNameFilter("FileName1")
                .setIncludeFileNameFilter("FileName2")
                .setExcludeTypeFilter("Type2")
                .build();
        applyFilterAndCheckResult(filter, getIssues(), ISSUE1);
    }

    @Test
    void shouldFindNoIntersectionFromEmptyIncludeAndExclude() {
        var filter = new IssueFilterBuilder()
                .setIncludeFileNameFilter("FileNameNotExisting")
                .setExcludeTypeFilter("Type2")
                .build();
        applyFilterAndCheckResult(filter, getIssues());
    }

    @Test
    void shouldFindIssue1ByAMessageIncludeMatch() {
        var filter = new IssueFilterBuilder()
                .setIncludeMessageFilter("Message1")
                .build();
        applyFilterAndCheckResult(filter, getIssues(), ISSUE1);
    }

    @Test
    void shouldRemoveIssue2ByAMessageExcludeMatch() {
        var filter = new IssueFilterBuilder()
                .setExcludeMessageFilter("Message2")
                .build();
        applyFilterAndCheckResult(filter, getIssues(), ISSUE1, ISSUE3);
    }

    /**
     * Applies filter and checks if result is equal to expected values.
     *
     * @param criterion
     *         the filter criterion
     * @param report
     *         the issues to filter.
     * @param expectedOutput
     *         the expected filter result.
     */
    private void applyFilterAndCheckResult(final Predicate<? super Issue> criterion, final Report report,
            final Issue... expectedOutput) {
        assertThat(report.filter(criterion).iterator()).toIterable().containsExactly(expectedOutput);
    }

    /**
     * Get issues containing issue 1, 2 and 3.
     *
     * @return issues.
     */
    private Report getIssues() {
        return new Report().addAll(ISSUE1, ISSUE2, ISSUE3);
    }
}
