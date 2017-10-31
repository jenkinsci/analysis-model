package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;

import static edu.hm.hafner.analysis.assertj.Assertions.*;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;

/**
 * Unit tests for {@link Issue}.
 *
 * @author Marcel Binder
 */
class IssueTest {
    static final String FILE_NAME = "C:/users/tester/file-name";
    static final String FILE_NAME_WITH_BACKSLASHES = "C:\\users\\tester/file-name";
    static final int LINE_START = 1;
    static final int LINE_END = 2;
    static final int COLUMN_START = 3;
    static final int COLUMN_END = 4;
    static final String CATEGORY = "category";
    static final String TYPE = "type";
    static final String PACKAGE_NAME = "package-name";
    static final String MODULE_NAME = "module-name";
    static final Priority PRIORITY = Priority.HIGH;
    static final String MESSAGE = "message";
    static final String MESSAGE_NOT_STRIPPED = "    message  ";
    static final String DESCRIPTION = "description";
    static final String DESCRIPTION_NOT_STRIPPED = "    description  ";
    static final String EMPTY = "";
    static final String UNDEFINED = "-";
    static final String FINGERPRINT = "fingerprint";
    static final String ORIGIN = "origin";

    @Test
    void testIssue() {
        Issue issue = createFilledIssue();

        assertSoftly(softly -> {
            softly.assertThat(issue.getId()).isNotNull();
            softly.assertThat(issue)
                  .hasFileName(FILE_NAME)
                  .hasCategory(CATEGORY)
                  .hasLineStart(LINE_START)
                  .hasLineEnd(LINE_END)
                  .hasColumnStart(COLUMN_START)
                  .hasColumnEnd(COLUMN_END)
                  .hasType(TYPE)
                  .hasPackageName(PACKAGE_NAME)
                  .hasModuleName(MODULE_NAME)
                  .hasPriority(PRIORITY)
                  .hasMessage(MESSAGE)
                  .hasFingerprint(UNDEFINED)
                  .hasOrigin(ORIGIN)
                  .hasDescription(DESCRIPTION);
        });
    }

    @Test
    void testDefaultIssueNullStringsNegativeIntegers() {
        Issue issue = new Issue(null, 0, 0, 0, 0, null, null,
                null, null, PRIORITY, null, null, null);

        assertIsDefaultIssue(issue);
    }

    @Test
    void testDefaultIssueEmptyStringsNegativeIntegers() {
        Issue issue = new Issue(EMPTY, -1, -1, -1, -1,
                EMPTY, EMPTY, EMPTY, EMPTY, PRIORITY, EMPTY, EMPTY, EMPTY);

        assertIsDefaultIssue(issue);
    }

    private void assertIsDefaultIssue(final Issue issue) {
        assertSoftly(softly -> {
            softly.assertThat(issue.getId()).isNotNull();
            softly.assertThat(issue)
                  .hasFileName(UNDEFINED)
                  .hasCategory(EMPTY)
                  .hasLineStart(0)
                  .hasLineEnd(0)
                  .hasColumnStart(0)
                  .hasColumnEnd(0)
                  .hasType(UNDEFINED)
                  .hasPackageName(UNDEFINED)
                  .hasMessage(EMPTY)
                  .hasDescription(EMPTY)
                  .hasFingerprint(UNDEFINED);
        });
    }

    @Test
    void testZeroLineColumnEndsDefaultToLineColumnStarts() {
        Issue issue = new Issue(FILE_NAME, LINE_START, 0, COLUMN_START, 0, CATEGORY, TYPE, PACKAGE_NAME,
                MODULE_NAME, PRIORITY, MESSAGE, DESCRIPTION, ORIGIN);

        assertSoftly(softly -> {
            softly.assertThat(issue)
                  .hasLineStart(LINE_START)
                  .hasLineEnd(LINE_START)
                  .hasColumnStart(COLUMN_START)
                  .hasColumnEnd(COLUMN_START);
        });
    }

    @Test
    void testNullPriorityDefaultsToNormal() {
        Issue issue = new Issue(FILE_NAME, LINE_START, LINE_END, COLUMN_START, COLUMN_END, CATEGORY, TYPE, PACKAGE_NAME,
                MODULE_NAME, null, MESSAGE, DESCRIPTION, ORIGIN);

        assertThat(issue.getPriority()).isEqualTo(Priority.NORMAL);
    }

    @Test
    void testIdRandomlyGenerated() {
        Issue one = createFilledIssue();
        Issue another = createFilledIssue();

        assertThat(one.getId()).isNotEqualTo(another.getId());
    }

    @Test
    void testFingerprint() {
        Issue issue = createFilledIssue();

        issue.setFingerprint(FINGERPRINT);

        assertThat(issue).hasFingerprint(FINGERPRINT);
    }

    private Issue createFilledIssue() {
        return new Issue(FILE_NAME, LINE_START, LINE_END, COLUMN_START, COLUMN_END, CATEGORY, TYPE, PACKAGE_NAME, MODULE_NAME, PRIORITY, MESSAGE, DESCRIPTION, ORIGIN);
    }

    @Test
    void testToString() {
        Issue issue = createFilledIssue();

        assertSoftly(softly -> {
            softly.assertThat(issue.toString()).contains(FILE_NAME);
            softly.assertThat(issue.toString()).contains(Integer.toString(LINE_START));
            softly.assertThat(issue.toString()).contains(Integer.toString(COLUMN_START));
            softly.assertThat(issue.toString()).contains(CATEGORY);
            softly.assertThat(issue.toString()).contains(TYPE);
            softly.assertThat(issue.toString()).contains(MESSAGE);
        });
    }

    @Test
    void testFileNameBackslashConversion() {
        Issue issue = new Issue(FILE_NAME_WITH_BACKSLASHES, LINE_START, LINE_END, COLUMN_START, COLUMN_END, CATEGORY, TYPE, PACKAGE_NAME, MODULE_NAME, PRIORITY, MESSAGE, DESCRIPTION, ORIGIN);

        assertThat(issue).hasFileName(FILE_NAME);
    }

    @Test
    void testMessageDescriptionStripped() {
        Issue issue = new Issue(FILE_NAME_WITH_BACKSLASHES, LINE_START, LINE_END, COLUMN_START, COLUMN_END, CATEGORY, TYPE, PACKAGE_NAME, MODULE_NAME, PRIORITY, MESSAGE_NOT_STRIPPED, DESCRIPTION_NOT_STRIPPED, ORIGIN);

        assertSoftly(softly -> {
            softly.assertThat(issue)
                  .hasMessage(MESSAGE)
                  .hasDescription(DESCRIPTION);
        });
    }
}