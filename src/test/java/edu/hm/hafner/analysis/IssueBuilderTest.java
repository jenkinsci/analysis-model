package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;

import static edu.hm.hafner.analysis.assertj.CustomAssertions.*;


class IssueBuilderTest {

    private static final String FILE_NAME = "FileName";
    private static final int LINE_START = 1;
    private static final int LINE_END = 2;
    private static final int COLUMN_START = 1;
    private static final int COLUMN_END = 2;
    private static final String CATEGORY = "Category";
    private static final String TYPE = "Type";
    private static final String PACKAGE_NAME = "PackageName";
    private static final Priority PRIORITY = Priority.HIGH;
    private static final String MESSAGE = "Message";
    private static final String DESCRIPTION = "Description";

    private static final Issue DEFAULT_ISSUE = new Issue(null, 0, 0, 0, 0, null, null, null, null, null, null);
    private static final Issue ISSUE_WITH_VALUES_IN_RANGE = new Issue(FILE_NAME, LINE_START, LINE_END, COLUMN_START, COLUMN_END, CATEGORY, TYPE, PACKAGE_NAME, PRIORITY, MESSAGE, DESCRIPTION);

    @Test
    public void shouldBuildIssueWithAllSetProperties() {
        Issue issue = new IssueBuilder()
                .setCategory(CATEGORY)
                .setColumnEnd(COLUMN_END)
                .setColumnStart(COLUMN_START)
                .setDescription(DESCRIPTION)
                .setFileName(FILE_NAME)
                .setLineStart(LINE_START)
                .setLineEnd(LINE_END)
                .setMessage(MESSAGE)
                .setPackageName(PACKAGE_NAME)
                .setType(TYPE)
                .setPriority(PRIORITY)
                .build();

        assertThat(issue).isEqualTo(ISSUE_WITH_VALUES_IN_RANGE);
    }

    @Test
    public void shouldBuildIssueWithDefaultProperties() {
        Issue issue = new IssueBuilder().build();

        assertThat(issue).isEqualTo(DEFAULT_ISSUE);
    }

    @Test
    public void testCopy() {
        Issue copyIssue = new IssueBuilder().copy(ISSUE_WITH_VALUES_IN_RANGE).build();

        assertThat(copyIssue).isNotSameAs(ISSUE_WITH_VALUES_IN_RANGE);
        assertThat(copyIssue).isEqualTo(ISSUE_WITH_VALUES_IN_RANGE);
    }

    @Test
    public void shouldBuildNewInstance() {
        IssueBuilder builder = new IssueBuilder();
        Issue first = builder.build();
        Issue second = builder.build();

        assertThat(first).isNotSameAs(second);
        assertThat(first).isEqualTo(second);
    }

}