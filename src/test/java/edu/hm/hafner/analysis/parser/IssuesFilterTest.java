package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.assertj.IssueAssert;
import edu.hm.hafner.analysis.assertj.IssuesAssert;

class IssuesFilterTest {
    private static final Issue ISSUE_1 = new IssueBuilder()
            .setType("type-1")
            .setMessage("issue-1")
            .setDescription("issue-1")
            .setFileName("file-1")
            .setCategory("category-1")
            .setPriority(Priority.HIGH)
            .setColumnStart(1)
            .setColumnEnd(1)
            .build();
    private static final Issue ISSUE_2 = new IssueBuilder()
            .setType("type-2")
            .setMessage("issue-2")
            .setDescription("issue-2")
            .setFileName("file-2")
            .setCategory("category-2")
            .setPriority(Priority.HIGH)
            .setColumnStart(2)
            .setColumnEnd(2)
            .build();
    private static final Issue ISSUE_3 = new IssueBuilder()
            .setType("type-3")
            .setMessage("issue-3")
            .setDescription("issue-3")
            .setFileName("file-3")
            .setCategory("category-3")
            .setPriority(Priority.NORMAL)
            .setColumnStart(3)
            .setColumnEnd(3)
            .build();
    private static final Issue ISSUE_4 = new IssueBuilder()
            .setType("type-4")
            .setMessage("issue-4")
            .setDescription("issue-4")
            .setFileName("file-4")
            .setCategory("category-4")
            .setPriority(Priority.NORMAL)
            .setColumnStart(4)
            .setColumnEnd(4)
            .build();

    @Test
    void testEmptyIssuesEmptyFilter(){
        Issues issues = new Issues();

        Issues filteredIssues = new IssuesFilter(issues).filter();
        IssuesAssert.assertThat(filteredIssues).hasSize(0);
    }

    @Test
    void testOneIssueEmptyFilter(){
        Issues issues = new Issues();
        issues.add(ISSUE_1);

        Issues filteredIssues = new IssuesFilter(issues).filter();
        IssuesAssert.assertThat(filteredIssues).hasSize(1);
        IssueAssert.assertThat(filteredIssues.get(0)).hasCategory(ISSUE_1.getCategory())
                .hasColumnStart(ISSUE_1.getColumnStart())
                .hasColumnEnd(ISSUE_1.getColumnEnd())
                .hasDescription(ISSUE_1.getDescription())
                .hasFileName(ISSUE_1.getFileName())
                .hasFingerprint(ISSUE_1.getFingerprint())
                .hasMessage(ISSUE_1.getMessage())
                .hasPriority(ISSUE_1.getPriority());
    }

    @Test
    void testFilterIncludeFilenameAllIssuesPass(){
        Issues issues = new Issues();
        issues.add(ISSUE_1);
        issues.add(ISSUE_2);

        Issues filteredIssues = new IssuesFilter(issues).includeFilename("file-\\d").filter();
        IssuesAssert.assertThat(filteredIssues).hasSize(2);
        IssueAssert.assertThat(filteredIssues.get(0)).hasCategory(ISSUE_1.getCategory())
                .hasColumnStart(ISSUE_1.getColumnStart())
                .hasColumnEnd(ISSUE_1.getColumnEnd())
                .hasDescription(ISSUE_1.getDescription())
                .hasFileName(ISSUE_1.getFileName())
                .hasFingerprint(ISSUE_1.getFingerprint())
                .hasMessage(ISSUE_1.getMessage())
                .hasPriority(ISSUE_1.getPriority());
        IssueAssert.assertThat(filteredIssues.get(1)).hasCategory(ISSUE_2.getCategory())
                .hasColumnStart(ISSUE_2.getColumnStart())
                .hasColumnEnd(ISSUE_2.getColumnEnd())
                .hasDescription(ISSUE_2.getDescription())
                .hasFileName(ISSUE_2.getFileName())
                .hasFingerprint(ISSUE_2.getFingerprint())
                .hasMessage(ISSUE_2.getMessage())
                .hasPriority(ISSUE_2.getPriority());
    }

    @Test
    void testFilterIncludeFilenameOneIssuePass(){
        Issues issues = new Issues();
        issues.add(ISSUE_1);
        issues.add(ISSUE_2);

        Issues filteredIssues = new IssuesFilter(issues).includeFilename(".*-2").filter();
        IssuesAssert.assertThat(filteredIssues).hasSize(1);
        IssueAssert.assertThat(filteredIssues.get(0)).hasCategory(ISSUE_2.getCategory())
                .hasColumnStart(ISSUE_2.getColumnStart())
                .hasColumnEnd(ISSUE_2.getColumnEnd())
                .hasDescription(ISSUE_2.getDescription())
                .hasFileName(ISSUE_2.getFileName())
                .hasFingerprint(ISSUE_2.getFingerprint())
                .hasMessage(ISSUE_2.getMessage())
                .hasPriority(ISSUE_2.getPriority());
    }

    @Test
    void testFilterIncludeCategoryAllIssuesPassFilter(){
        Issues issues = new Issues();
        issues.add(ISSUE_3);
        issues.add(ISSUE_4);

        Issues filteredIssues = new IssuesFilter(issues).includeCategory("category-\\d").filter();
        IssuesAssert.assertThat(filteredIssues).hasSize(2);
        IssueAssert.assertThat(filteredIssues.get(0)).hasCategory(ISSUE_3.getCategory())
                .hasColumnStart(ISSUE_3.getColumnStart())
                .hasColumnEnd(ISSUE_3.getColumnEnd())
                .hasDescription(ISSUE_3.getDescription())
                .hasFileName(ISSUE_3.getFileName())
                .hasFingerprint(ISSUE_3.getFingerprint())
                .hasMessage(ISSUE_3.getMessage())
                .hasPriority(ISSUE_3.getPriority());
        IssueAssert.assertThat(filteredIssues.get(1)).hasCategory(ISSUE_4.getCategory())
                .hasColumnStart(ISSUE_4.getColumnStart())
                .hasColumnEnd(ISSUE_4.getColumnEnd())
                .hasDescription(ISSUE_4.getDescription())
                .hasFileName(ISSUE_4.getFileName())
                .hasFingerprint(ISSUE_4.getFingerprint())
                .hasMessage(ISSUE_4.getMessage())
                .hasPriority(ISSUE_4.getPriority());
    }

    @Test
    void testFilterIncludeCategoryOneIssuePassFilter() {
        Issues issues = new Issues();
        issues.add(ISSUE_3);
        issues.add(ISSUE_4);

        Issues filteredIssues = new IssuesFilter(issues).includeCategory(".*-3").filter();
        IssuesAssert.assertThat(filteredIssues).hasSize(1);
        IssueAssert.assertThat(filteredIssues.get(0)).hasCategory(ISSUE_3.getCategory())
                .hasColumnStart(ISSUE_3.getColumnStart())
                .hasColumnEnd(ISSUE_3.getColumnEnd())
                .hasDescription(ISSUE_3.getDescription())
                .hasFileName(ISSUE_3.getFileName())
                .hasFingerprint(ISSUE_3.getFingerprint())
                .hasMessage(ISSUE_3.getMessage())
                .hasPriority(ISSUE_3.getPriority());
    }

    @Test
    void testFilterIncludePriorityAllIssuesPass(){
        Issues issues = new Issues();
        issues.add(ISSUE_1);
        issues.add(ISSUE_2);

        Issues filteredIssues = new IssuesFilter(issues).includePriority("HIGH").filter();
        IssuesAssert.assertThat(filteredIssues).hasSize(2);
        IssueAssert.assertThat(filteredIssues.get(0)).hasCategory(ISSUE_1.getCategory())
                .hasColumnStart(ISSUE_1.getColumnStart())
                .hasColumnEnd(ISSUE_1.getColumnEnd())
                .hasDescription(ISSUE_1.getDescription())
                .hasFileName(ISSUE_1.getFileName())
                .hasFingerprint(ISSUE_1.getFingerprint())
                .hasMessage(ISSUE_1.getMessage())
                .hasPriority(ISSUE_1.getPriority());
        IssueAssert.assertThat(filteredIssues.get(1)).hasCategory(ISSUE_2.getCategory())
                .hasColumnStart(ISSUE_2.getColumnStart())
                .hasColumnEnd(ISSUE_2.getColumnEnd())
                .hasDescription(ISSUE_2.getDescription())
                .hasFileName(ISSUE_2.getFileName())
                .hasFingerprint(ISSUE_2.getFingerprint())
                .hasMessage(ISSUE_2.getMessage())
                .hasPriority(ISSUE_2.getPriority());
    }

    @Test
    void testFilterIncludePriorityOneIssuesPass(){
        Issues issues = new Issues();
        issues.add(ISSUE_1);
        issues.add(ISSUE_3);

        Issues filteredIssues = new IssuesFilter(issues).includePriority("HIGH").filter();
        IssuesAssert.assertThat(filteredIssues).hasSize(1);
        IssueAssert.assertThat(filteredIssues.get(0)).hasCategory(ISSUE_1.getCategory())
                .hasColumnStart(ISSUE_1.getColumnStart())
                .hasColumnEnd(ISSUE_1.getColumnEnd())
                .hasDescription(ISSUE_1.getDescription())
                .hasFileName(ISSUE_1.getFileName())
                .hasFingerprint(ISSUE_1.getFingerprint())
                .hasMessage(ISSUE_1.getMessage())
                .hasPriority(ISSUE_1.getPriority());
    }

    @Test
    void testFilterIncludePriorityNoIssuesPass(){
        Issues issues = new Issues();
        issues.add(ISSUE_4);
        issues.add(ISSUE_3);

        Issues filteredIssues = new IssuesFilter(issues).includePriority("HIGH").filter();
        IssuesAssert.assertThat(filteredIssues).hasSize(0);
    }

    @Test
    void testFilterIncludeMessageOneIssuePass(){
        Issues issues = new Issues();
        issues.add(ISSUE_1);
        issues.add(ISSUE_2);

        Issues filteredIssues = new IssuesFilter(issues).includeMessage(".*-2").filter();
        IssuesAssert.assertThat(filteredIssues).hasSize(1);
        IssueAssert.assertThat(filteredIssues.get(0)).hasCategory(ISSUE_2.getCategory())
                .hasColumnStart(ISSUE_2.getColumnStart())
                .hasColumnEnd(ISSUE_2.getColumnEnd())
                .hasDescription(ISSUE_2.getDescription())
                .hasFileName(ISSUE_2.getFileName())
                .hasFingerprint(ISSUE_2.getFingerprint())
                .hasMessage(ISSUE_2.getMessage())
                .hasPriority(ISSUE_2.getPriority());
    }

    @Test
    void testFilterIncludeMessageAllIssuesPass(){
        Issues issues = new Issues();
        issues.add(ISSUE_1);
        issues.add(ISSUE_2);

        Issues filteredIssues = new IssuesFilter(issues).includeMessage("issue-\\d").filter();
        IssuesAssert.assertThat(filteredIssues).hasSize(2);
        IssueAssert.assertThat(filteredIssues.get(0)).hasCategory(ISSUE_1.getCategory())
                .hasColumnStart(ISSUE_1.getColumnStart())
                .hasColumnEnd(ISSUE_1.getColumnEnd())
                .hasDescription(ISSUE_1.getDescription())
                .hasFileName(ISSUE_1.getFileName())
                .hasFingerprint(ISSUE_1.getFingerprint())
                .hasMessage(ISSUE_1.getMessage())
                .hasPriority(ISSUE_1.getPriority());
        IssueAssert.assertThat(filteredIssues.get(1)).hasCategory(ISSUE_2.getCategory())
                .hasColumnStart(ISSUE_2.getColumnStart())
                .hasColumnEnd(ISSUE_2.getColumnEnd())
                .hasDescription(ISSUE_2.getDescription())
                .hasFileName(ISSUE_2.getFileName())
                .hasFingerprint(ISSUE_2.getFingerprint())
                .hasMessage(ISSUE_2.getMessage())
                .hasPriority(ISSUE_2.getPriority());
    }

    @Test
    void testFilterIncludeDescriptionAllIssuesPass(){
        Issues issues = new Issues();
        issues.add(ISSUE_1);
        issues.add(ISSUE_2);

        Issues filteredIssues = new IssuesFilter(issues).includeDescription("issue-\\d").filter();
        IssuesAssert.assertThat(filteredIssues).hasSize(2);
        IssueAssert.assertThat(filteredIssues.get(0)).hasCategory(ISSUE_1.getCategory())
                .hasColumnStart(ISSUE_1.getColumnStart())
                .hasColumnEnd(ISSUE_1.getColumnEnd())
                .hasDescription(ISSUE_1.getDescription())
                .hasFileName(ISSUE_1.getFileName())
                .hasFingerprint(ISSUE_1.getFingerprint())
                .hasMessage(ISSUE_1.getMessage())
                .hasPriority(ISSUE_1.getPriority());
        IssueAssert.assertThat(filteredIssues.get(1)).hasCategory(ISSUE_2.getCategory())
                .hasColumnStart(ISSUE_2.getColumnStart())
                .hasColumnEnd(ISSUE_2.getColumnEnd())
                .hasDescription(ISSUE_2.getDescription())
                .hasFileName(ISSUE_2.getFileName())
                .hasFingerprint(ISSUE_2.getFingerprint())
                .hasMessage(ISSUE_2.getMessage())
                .hasPriority(ISSUE_2.getPriority());
    }

    @Test
    void testFilterIncludeDescriptionOneIssuePass(){
        Issues issues = new Issues();
        issues.add(ISSUE_1);
        issues.add(ISSUE_2);

        Issues filteredIssues = new IssuesFilter(issues).includeDescription(".*-2").filter();
        IssuesAssert.assertThat(filteredIssues).hasSize(1);
        IssueAssert.assertThat(filteredIssues.get(0)).hasCategory(ISSUE_2.getCategory())
                .hasColumnStart(ISSUE_2.getColumnStart())
                .hasColumnEnd(ISSUE_2.getColumnEnd())
                .hasDescription(ISSUE_2.getDescription())
                .hasFileName(ISSUE_2.getFileName())
                .hasFingerprint(ISSUE_2.getFingerprint())
                .hasMessage(ISSUE_2.getMessage())
                .hasPriority(ISSUE_2.getPriority());
    }

    @Test
    void testFilterIncludeTypeAllIssuesPass(){
        Issues issues = new Issues();
        issues.add(ISSUE_1);
        issues.add(ISSUE_2);

        Issues filteredIssues = new IssuesFilter(issues).includeType("type-\\d").filter();
        IssuesAssert.assertThat(filteredIssues).hasSize(2);
        IssueAssert.assertThat(filteredIssues.get(0)).hasCategory(ISSUE_1.getCategory())
                .hasColumnStart(ISSUE_1.getColumnStart())
                .hasColumnEnd(ISSUE_1.getColumnEnd())
                .hasDescription(ISSUE_1.getDescription())
                .hasFileName(ISSUE_1.getFileName())
                .hasFingerprint(ISSUE_1.getFingerprint())
                .hasMessage(ISSUE_1.getMessage())
                .hasPriority(ISSUE_1.getPriority());
        IssueAssert.assertThat(filteredIssues.get(1)).hasCategory(ISSUE_2.getCategory())
                .hasColumnStart(ISSUE_2.getColumnStart())
                .hasColumnEnd(ISSUE_2.getColumnEnd())
                .hasDescription(ISSUE_2.getDescription())
                .hasFileName(ISSUE_2.getFileName())
                .hasFingerprint(ISSUE_2.getFingerprint())
                .hasMessage(ISSUE_2.getMessage())
                .hasPriority(ISSUE_2.getPriority());
    }

    @Test
    void testFilterIncludeTypeOneIssuePass(){
        Issues issues = new Issues();
        issues.add(ISSUE_1);
        issues.add(ISSUE_2);

        Issues filteredIssues = new IssuesFilter(issues).includeType(".*-2").filter();
        IssuesAssert.assertThat(filteredIssues).hasSize(1);
        IssueAssert.assertThat(filteredIssues.get(0)).hasCategory(ISSUE_2.getCategory())
                .hasColumnStart(ISSUE_2.getColumnStart())
                .hasColumnEnd(ISSUE_2.getColumnEnd())
                .hasDescription(ISSUE_2.getDescription())
                .hasFileName(ISSUE_2.getFileName())
                .hasFingerprint(ISSUE_2.getFingerprint())
                .hasMessage(ISSUE_2.getMessage())
                .hasPriority(ISSUE_2.getPriority());
    }

    @Test
    void testFilterIncludeColumnStartAllIssuesPass(){
        Issues issues = new Issues();
        issues.add(ISSUE_1);
        issues.add(ISSUE_2);

        Issues filteredIssues = new IssuesFilter(issues).includeColumnStart("\\d+").filter();
        IssuesAssert.assertThat(filteredIssues).hasSize(2);
        IssueAssert.assertThat(filteredIssues.get(0)).hasCategory(ISSUE_1.getCategory())
                .hasColumnStart(ISSUE_1.getColumnStart())
                .hasColumnEnd(ISSUE_1.getColumnEnd())
                .hasDescription(ISSUE_1.getDescription())
                .hasFileName(ISSUE_1.getFileName())
                .hasFingerprint(ISSUE_1.getFingerprint())
                .hasMessage(ISSUE_1.getMessage())
                .hasPriority(ISSUE_1.getPriority());
        IssueAssert.assertThat(filteredIssues.get(1)).hasCategory(ISSUE_2.getCategory())
                .hasColumnStart(ISSUE_2.getColumnStart())
                .hasColumnEnd(ISSUE_2.getColumnEnd())
                .hasDescription(ISSUE_2.getDescription())
                .hasFileName(ISSUE_2.getFileName())
                .hasFingerprint(ISSUE_2.getFingerprint())
                .hasMessage(ISSUE_2.getMessage())
                .hasPriority(ISSUE_2.getPriority());
    }

    @Test
    void testFilterIncludeColumnStartOneIssuePass(){
        Issues issues = new Issues();
        issues.add(ISSUE_1);
        issues.add(ISSUE_2);

        Issues filteredIssues = new IssuesFilter(issues).includeColumnStart("2").filter();
        IssuesAssert.assertThat(filteredIssues).hasSize(1);
        IssueAssert.assertThat(filteredIssues.get(0)).hasCategory(ISSUE_2.getCategory())
                .hasColumnStart(ISSUE_2.getColumnStart())
                .hasColumnEnd(ISSUE_2.getColumnEnd())
                .hasDescription(ISSUE_2.getDescription())
                .hasFileName(ISSUE_2.getFileName())
                .hasFingerprint(ISSUE_2.getFingerprint())
                .hasMessage(ISSUE_2.getMessage())
                .hasPriority(ISSUE_2.getPriority());
    }

    @Test
    void testFilterIncludeColumnEndAllIssuesPass(){
        Issues issues = new Issues();
        issues.add(ISSUE_1);
        issues.add(ISSUE_2);

        Issues filteredIssues = new IssuesFilter(issues).includeColumnEnd("\\d+").filter();
        IssuesAssert.assertThat(filteredIssues).hasSize(2);
        IssueAssert.assertThat(filteredIssues.get(0)).hasCategory(ISSUE_1.getCategory())
                .hasColumnStart(ISSUE_1.getColumnStart())
                .hasColumnEnd(ISSUE_1.getColumnEnd())
                .hasDescription(ISSUE_1.getDescription())
                .hasFileName(ISSUE_1.getFileName())
                .hasFingerprint(ISSUE_1.getFingerprint())
                .hasMessage(ISSUE_1.getMessage())
                .hasPriority(ISSUE_1.getPriority());
        IssueAssert.assertThat(filteredIssues.get(1)).hasCategory(ISSUE_2.getCategory())
                .hasColumnStart(ISSUE_2.getColumnStart())
                .hasColumnEnd(ISSUE_2.getColumnEnd())
                .hasDescription(ISSUE_2.getDescription())
                .hasFileName(ISSUE_2.getFileName())
                .hasFingerprint(ISSUE_2.getFingerprint())
                .hasMessage(ISSUE_2.getMessage())
                .hasPriority(ISSUE_2.getPriority());
    }

    @Test
    void testFilterIncludeColumnEndOneIssuePass(){
        Issues issues = new Issues();
        issues.add(ISSUE_1);
        issues.add(ISSUE_2);

        Issues filteredIssues = new IssuesFilter(issues).includeColumnEnd("2").filter();
        IssuesAssert.assertThat(filteredIssues).hasSize(1);
        IssueAssert.assertThat(filteredIssues.get(0)).hasCategory(ISSUE_2.getCategory())
                .hasColumnStart(ISSUE_2.getColumnStart())
                .hasColumnEnd(ISSUE_2.getColumnEnd())
                .hasDescription(ISSUE_2.getDescription())
                .hasFileName(ISSUE_2.getFileName())
                .hasFingerprint(ISSUE_2.getFingerprint())
                .hasMessage(ISSUE_2.getMessage())
                .hasPriority(ISSUE_2.getPriority());
    }

    @Test
    void testFilterExcludeFilenameAllExcluded(){
        Issues issues = new Issues();
        issues.add(ISSUE_1);
        issues.add(ISSUE_2);
        issues.add(ISSUE_3);

        Issues filteredIssues = new IssuesFilter(issues).excludeFilename(".*").filter();
        IssuesAssert.assertThat(filteredIssues).hasSize(0);
    }

    @Test
    void testFilterExcludeFilenameOneExcluded(){
        Issues issues = new Issues();
        issues.add(ISSUE_1);
        issues.add(ISSUE_2);
        issues.add(ISSUE_3);

        Issues filteredIssues = new IssuesFilter(issues).excludeFilename(".*-3").filter();
        IssuesAssert.assertThat(filteredIssues).hasSize(2);
        IssueAssert.assertThat(filteredIssues.get(0)).hasCategory(ISSUE_1.getCategory())
                .hasColumnStart(ISSUE_1.getColumnStart())
                .hasColumnEnd(ISSUE_1.getColumnEnd())
                .hasDescription(ISSUE_1.getDescription())
                .hasFileName(ISSUE_1.getFileName())
                .hasFingerprint(ISSUE_1.getFingerprint())
                .hasMessage(ISSUE_1.getMessage())
                .hasPriority(ISSUE_1.getPriority());
        IssueAssert.assertThat(filteredIssues.get(1)).hasCategory(ISSUE_2.getCategory())
                .hasColumnStart(ISSUE_2.getColumnStart())
                .hasColumnEnd(ISSUE_2.getColumnEnd())
                .hasDescription(ISSUE_2.getDescription())
                .hasFileName(ISSUE_2.getFileName())
                .hasFingerprint(ISSUE_2.getFingerprint())
                .hasMessage(ISSUE_2.getMessage())
                .hasPriority(ISSUE_2.getPriority());
    }

    @Test
    void testFilterExcludePriorityAllExcluded(){
        Issues issues = new Issues();
        issues.add(ISSUE_1);
        issues.add(ISSUE_2);

        Issues filteredIssues = new IssuesFilter(issues).excludePriority("HIGH").filter();
        IssuesAssert.assertThat(filteredIssues).hasSize(0);
    }

    @Test
    void testFilterExcludePriorityOneExcluded(){
        Issues issues = new Issues();
        issues.add(ISSUE_1);
        issues.add(ISSUE_2);
        issues.add(ISSUE_3);

        Issues filteredIssues = new IssuesFilter(issues).excludePriority("NORMAL").filter();
        IssuesAssert.assertThat(filteredIssues).hasSize(2);
        IssueAssert.assertThat(filteredIssues.get(0)).hasCategory(ISSUE_1.getCategory())
                .hasColumnStart(ISSUE_1.getColumnStart())
                .hasColumnEnd(ISSUE_1.getColumnEnd())
                .hasDescription(ISSUE_1.getDescription())
                .hasFileName(ISSUE_1.getFileName())
                .hasFingerprint(ISSUE_1.getFingerprint())
                .hasMessage(ISSUE_1.getMessage())
                .hasPriority(ISSUE_1.getPriority());
        IssueAssert.assertThat(filteredIssues.get(1)).hasCategory(ISSUE_2.getCategory())
                .hasColumnStart(ISSUE_2.getColumnStart())
                .hasColumnEnd(ISSUE_2.getColumnEnd())
                .hasDescription(ISSUE_2.getDescription())
                .hasFileName(ISSUE_2.getFileName())
                .hasFingerprint(ISSUE_2.getFingerprint())
                .hasMessage(ISSUE_2.getMessage())
                .hasPriority(ISSUE_2.getPriority());
    }

    @Test
    void testFilterExcludeDescriptionAllExcluded(){
        Issues issues = new Issues();
        issues.add(ISSUE_1);
        issues.add(ISSUE_2);

        Issues filteredIssues = new IssuesFilter(issues).excludeDescription("issue-\\d").filter();
        IssuesAssert.assertThat(filteredIssues).hasSize(0);
    }

    @Test
    void testFilterExcludeDescriptionOneExcluded(){
        Issues issues = new Issues();
        issues.add(ISSUE_1);
        issues.add(ISSUE_2);
        issues.add(ISSUE_3);

        Issues filteredIssues = new IssuesFilter(issues).excludeDescription(".*-3").filter();
        IssuesAssert.assertThat(filteredIssues).hasSize(2);
        IssueAssert.assertThat(filteredIssues.get(0)).hasCategory(ISSUE_1.getCategory())
                .hasColumnStart(ISSUE_1.getColumnStart())
                .hasColumnEnd(ISSUE_1.getColumnEnd())
                .hasDescription(ISSUE_1.getDescription())
                .hasFileName(ISSUE_1.getFileName())
                .hasFingerprint(ISSUE_1.getFingerprint())
                .hasMessage(ISSUE_1.getMessage())
                .hasPriority(ISSUE_1.getPriority());
        IssueAssert.assertThat(filteredIssues.get(1)).hasCategory(ISSUE_2.getCategory())
                .hasColumnStart(ISSUE_2.getColumnStart())
                .hasColumnEnd(ISSUE_2.getColumnEnd())
                .hasDescription(ISSUE_2.getDescription())
                .hasFileName(ISSUE_2.getFileName())
                .hasFingerprint(ISSUE_2.getFingerprint())
                .hasMessage(ISSUE_2.getMessage())
                .hasPriority(ISSUE_2.getPriority());
    }

    @Test
    void testFilterExcludeCategoryAllExcluded(){
        Issues issues = new Issues();
        issues.add(ISSUE_1);
        issues.add(ISSUE_2);

        Issues filteredIssues = new IssuesFilter(issues).excludeCategory("category-\\d").filter();
        IssuesAssert.assertThat(filteredIssues).hasSize(0);
    }

    @Test
    void testFilterExcludeCategoryOneExcluded(){
        Issues issues = new Issues();
        issues.add(ISSUE_1);
        issues.add(ISSUE_2);
        issues.add(ISSUE_3);

        Issues filteredIssues = new IssuesFilter(issues).excludeCategory(".*-3").filter();
        IssuesAssert.assertThat(filteredIssues).hasSize(2);
        IssueAssert.assertThat(filteredIssues.get(0)).hasCategory(ISSUE_1.getCategory())
                .hasColumnStart(ISSUE_1.getColumnStart())
                .hasColumnEnd(ISSUE_1.getColumnEnd())
                .hasDescription(ISSUE_1.getDescription())
                .hasFileName(ISSUE_1.getFileName())
                .hasFingerprint(ISSUE_1.getFingerprint())
                .hasMessage(ISSUE_1.getMessage())
                .hasPriority(ISSUE_1.getPriority());
        IssueAssert.assertThat(filteredIssues.get(1)).hasCategory(ISSUE_2.getCategory())
                .hasColumnStart(ISSUE_2.getColumnStart())
                .hasColumnEnd(ISSUE_2.getColumnEnd())
                .hasDescription(ISSUE_2.getDescription())
                .hasFileName(ISSUE_2.getFileName())
                .hasFingerprint(ISSUE_2.getFingerprint())
                .hasMessage(ISSUE_2.getMessage())
                .hasPriority(ISSUE_2.getPriority());
    }

    @Test
    void testFilterExcludeMessageAllExcluded(){
        Issues issues = new Issues();
        issues.add(ISSUE_1);
        issues.add(ISSUE_2);

        Issues filteredIssues = new IssuesFilter(issues).excludeMessage("issue-\\d").filter();
        IssuesAssert.assertThat(filteredIssues).hasSize(0);
    }

    @Test
    void testFilterExcludeMessageOneExcluded(){
        Issues issues = new Issues();
        issues.add(ISSUE_1);
        issues.add(ISSUE_2);
        issues.add(ISSUE_3);

        Issues filteredIssues = new IssuesFilter(issues).excludeMessage(".*-3").filter();
        IssuesAssert.assertThat(filteredIssues).hasSize(2);
        IssueAssert.assertThat(filteredIssues.get(0)).hasCategory(ISSUE_1.getCategory())
                .hasColumnStart(ISSUE_1.getColumnStart())
                .hasColumnEnd(ISSUE_1.getColumnEnd())
                .hasDescription(ISSUE_1.getDescription())
                .hasFileName(ISSUE_1.getFileName())
                .hasFingerprint(ISSUE_1.getFingerprint())
                .hasMessage(ISSUE_1.getMessage())
                .hasPriority(ISSUE_1.getPriority());
        IssueAssert.assertThat(filteredIssues.get(1)).hasCategory(ISSUE_2.getCategory())
                .hasColumnStart(ISSUE_2.getColumnStart())
                .hasColumnEnd(ISSUE_2.getColumnEnd())
                .hasDescription(ISSUE_2.getDescription())
                .hasFileName(ISSUE_2.getFileName())
                .hasFingerprint(ISSUE_2.getFingerprint())
                .hasMessage(ISSUE_2.getMessage())
                .hasPriority(ISSUE_2.getPriority());
    }

    @Test
    void testFilterExcludeTypeAllExcluded(){
        Issues issues = new Issues();
        issues.add(ISSUE_1);
        issues.add(ISSUE_2);

        Issues filteredIssues = new IssuesFilter(issues).excludeType("type-\\d").filter();
        IssuesAssert.assertThat(filteredIssues).hasSize(0);
    }

    @Test
    void testFilterExcludeTypeOneExcluded(){
        Issues issues = new Issues();
        issues.add(ISSUE_1);
        issues.add(ISSUE_2);
        issues.add(ISSUE_3);

        Issues filteredIssues = new IssuesFilter(issues).excludeType(".*-3").filter();
        IssuesAssert.assertThat(filteredIssues).hasSize(2);
        IssueAssert.assertThat(filteredIssues.get(0)).hasCategory(ISSUE_1.getCategory())
                .hasColumnStart(ISSUE_1.getColumnStart())
                .hasColumnEnd(ISSUE_1.getColumnEnd())
                .hasDescription(ISSUE_1.getDescription())
                .hasFileName(ISSUE_1.getFileName())
                .hasFingerprint(ISSUE_1.getFingerprint())
                .hasMessage(ISSUE_1.getMessage())
                .hasPriority(ISSUE_1.getPriority());
        IssueAssert.assertThat(filteredIssues.get(1)).hasCategory(ISSUE_2.getCategory())
                .hasColumnStart(ISSUE_2.getColumnStart())
                .hasColumnEnd(ISSUE_2.getColumnEnd())
                .hasDescription(ISSUE_2.getDescription())
                .hasFileName(ISSUE_2.getFileName())
                .hasFingerprint(ISSUE_2.getFingerprint())
                .hasMessage(ISSUE_2.getMessage())
                .hasPriority(ISSUE_2.getPriority());
    }

    @Test
    void testFilterExcludeColumnStartAllExcluded(){
        Issues issues = new Issues();
        issues.add(ISSUE_1);
        issues.add(ISSUE_2);

        Issues filteredIssues = new IssuesFilter(issues).excludeColumnStart("\\d+").filter();
        IssuesAssert.assertThat(filteredIssues).hasSize(0);
    }

    @Test
    void testFilterExcludeColumnStartOneExcluded(){
        Issues issues = new Issues();
        issues.add(ISSUE_1);
        issues.add(ISSUE_2);
        issues.add(ISSUE_3);

        Issues filteredIssues = new IssuesFilter(issues).excludeColumnStart("3").filter();
        IssuesAssert.assertThat(filteredIssues).hasSize(2);
        IssueAssert.assertThat(filteredIssues.get(0)).hasCategory(ISSUE_1.getCategory())
                .hasColumnStart(ISSUE_1.getColumnStart())
                .hasColumnEnd(ISSUE_1.getColumnEnd())
                .hasDescription(ISSUE_1.getDescription())
                .hasFileName(ISSUE_1.getFileName())
                .hasFingerprint(ISSUE_1.getFingerprint())
                .hasMessage(ISSUE_1.getMessage())
                .hasPriority(ISSUE_1.getPriority());
        IssueAssert.assertThat(filteredIssues.get(1)).hasCategory(ISSUE_2.getCategory())
                .hasColumnStart(ISSUE_2.getColumnStart())
                .hasColumnEnd(ISSUE_2.getColumnEnd())
                .hasDescription(ISSUE_2.getDescription())
                .hasFileName(ISSUE_2.getFileName())
                .hasFingerprint(ISSUE_2.getFingerprint())
                .hasMessage(ISSUE_2.getMessage())
                .hasPriority(ISSUE_2.getPriority());
    }

    @Test
    void testFilterExcludeColumnEndAllExcluded(){
        Issues issues = new Issues();
        issues.add(ISSUE_1);
        issues.add(ISSUE_2);

        Issues filteredIssues = new IssuesFilter(issues).excludeColumnEnd("\\d+").filter();
        IssuesAssert.assertThat(filteredIssues).hasSize(0);
    }

    @Test
    void testFilterExcludeColumnEndOneExcluded(){
        Issues issues = new Issues();
        issues.add(ISSUE_1);
        issues.add(ISSUE_2);
        issues.add(ISSUE_3);

        Issues filteredIssues = new IssuesFilter(issues).excludeColumnEnd("3").filter();
        IssuesAssert.assertThat(filteredIssues).hasSize(2);
        IssueAssert.assertThat(filteredIssues.get(0)).hasCategory(ISSUE_1.getCategory())
                .hasColumnStart(ISSUE_1.getColumnStart())
                .hasColumnEnd(ISSUE_1.getColumnEnd())
                .hasDescription(ISSUE_1.getDescription())
                .hasFileName(ISSUE_1.getFileName())
                .hasFingerprint(ISSUE_1.getFingerprint())
                .hasMessage(ISSUE_1.getMessage())
                .hasPriority(ISSUE_1.getPriority());
        IssueAssert.assertThat(filteredIssues.get(1)).hasCategory(ISSUE_2.getCategory())
                .hasColumnStart(ISSUE_2.getColumnStart())
                .hasColumnEnd(ISSUE_2.getColumnEnd())
                .hasDescription(ISSUE_2.getDescription())
                .hasFileName(ISSUE_2.getFileName())
                .hasFingerprint(ISSUE_2.getFingerprint())
                .hasMessage(ISSUE_2.getMessage())
                .hasPriority(ISSUE_2.getPriority());
    }

    @Test
    void testFilterIncludeAndExcludeTheSame(){
        Issues issues = new Issues();
        issues.add(ISSUE_1);

        Issues filteredIssues = new IssuesFilter(issues).includeFilename("file-\\d").excludeColumnStart("1").filter();
        IssuesAssert.assertThat(filteredIssues).hasSize(0);
    }
}