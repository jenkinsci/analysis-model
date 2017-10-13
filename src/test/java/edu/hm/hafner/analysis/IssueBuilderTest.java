package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;

import static edu.hm.hafner.analysis.assertj.IssueAssert.*;

/**
 * Unit test for {@link IssueBuilder}.
 */
class IssueBuilderTest {
    private static final String FILE_NAME = "file-name";
    private static final int LINE_START = 1;
    private static final int LINE_END = 2;
    private static final int COLUMN_START = 3;
    private static final int COLUMN_END = 4;
    private static final String CATEGORY = "category";
    private static final String TYPE = "type";
    private static final String PACKAGE_NAME = "package-name";
    private static final Priority PRIORITY = Priority.HIGH;
    private static final String MESSAGE = "message";
    private static final String DESCRIPTION = "description";

    private static final Issue DEFAULT_ISSUE = new Issue(null, 0, 0, 0, 0, null, null, null, null, null, null);
    private static final Issue FILLED_ISSUE = new Issue(FILE_NAME, LINE_START, LINE_END, COLUMN_START, COLUMN_END, CATEGORY, TYPE, PACKAGE_NAME, PRIORITY, MESSAGE, DESCRIPTION);

    @Test
    void testDefaultValues() {
        Issue issue = new IssueBuilder().build();

        assertThat(issue).isEqualTo(DEFAULT_ISSUE);
    }

    @Test
    void testFilledIssue() {
        Issue issue = new IssueBuilder()
                .setFileName(FILE_NAME)
                .setLineStart(LINE_START)
                .setLineEnd(LINE_END)
                .setColumnStart(COLUMN_START)
                .setColumnEnd(COLUMN_END)
                .setCategory(CATEGORY)
                .setType(TYPE)
                .setPackageName(PACKAGE_NAME)
                .setPriority(PRIORITY)
                .setMessage(MESSAGE)
                .setDescription(DESCRIPTION)
                .build();

        assertThat(issue).isEqualTo(FILLED_ISSUE);
    }

    @Test
    void testCopy() {
        Issue copy = new IssueBuilder()
                .copy(FILLED_ISSUE)
                .build();

        assertThat(copy).isNotSameAs(FILE_NAME);
        assertThat(copy).isEqualTo(FILLED_ISSUE);
    }
}