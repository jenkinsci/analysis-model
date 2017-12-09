package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;

import static edu.hm.hafner.analysis.IssueTest.*;
import static edu.hm.hafner.analysis.assertj.IssueAssert.*;

/**
 * Unit test for {@link IssueBuilder}.
 *
 * @author Marcel Binder
 */
class IssueBuilderTest {
    private static final Issue DEFAULT_ISSUE = new Issue(null, 0, 0, 0, 0,
            null, null, null, null, null, null, null, null, null);
    private static final Issue FILLED_ISSUE = new Issue(FILE_NAME, LINE_START, LINE_END, COLUMN_START, COLUMN_END,
            CATEGORY, TYPE, PACKAGE_NAME, MODULE_NAME, PRIORITY, MESSAGE, DESCRIPTION, ORIGIN, FINGERPRINT);

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
                .setModuleName(MODULE_NAME)
                .setPriority(PRIORITY)
                .setMessage(MESSAGE)
                .setDescription(DESCRIPTION)
                .setOrigin(ORIGIN)
                .build();

        assertThat(issue).isEqualTo(FILLED_ISSUE);
    }

    @Test
    void testCopy() {
        Issue copy = new IssueBuilder()
                .copy(FILLED_ISSUE)
                .build();

        assertThat(copy).isNotSameAs(FILLED_ISSUE);
        assertThat(copy).isEqualTo(FILLED_ISSUE);
    }

    @Test
    void testBuildNewInstance() {
        IssueBuilder builder = new IssueBuilder().copy(FILLED_ISSUE);
        Issue issue1 = builder.build();
        Issue issue2 = builder.build();

        assertThat(issue1).isNotSameAs(issue2);
        assertThat(issue1).isEqualTo(issue2);
    }
}