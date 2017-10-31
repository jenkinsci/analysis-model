package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.assertj.AnalysisSoftAssertions;
import static edu.hm.hafner.analysis.assertj.AnalysisSoftAssertions.*;
import static org.assertj.core.api.Assertions.*;

/**
 * Unit test for {@link Issue}.
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
    static final Priority PRIORITY = Priority.HIGH;
    static final String MESSAGE = "message";
    static final String MESSAGE_NOT_STRIPPED = "    message  ";
    static final String DESCRIPTION = "description";
    static final String DESCRIPTION_NOT_STRIPPED = "    description  ";
    static final String EMPTY = "";
    static final String UNDEFINED = "-";
    static final String FINGERPRINT = "fingerprint";

    @Test
    void testIssue() {
        Issue issue = new Issue(FILE_NAME, LINE_START, LINE_END, COLUMN_START, COLUMN_END, CATEGORY, TYPE, PACKAGE_NAME, PRIORITY, MESSAGE, DESCRIPTION);

        AnalysisSoftAssertions softly = new AnalysisSoftAssertions();
        softly.assertThat(issue.getId()).isNotNull();
        softly.assertThat(issue).hasFileName(FILE_NAME);
        softly.assertThat(issue).hasCategory(CATEGORY);
        softly.assertThat(issue).hasLineStart(LINE_START);
        softly.assertThat(issue).hasLineEnd(LINE_END);
        softly.assertThat(issue).hasColumnStart(COLUMN_START);
        softly.assertThat(issue).hasColumnEnd(COLUMN_END);
        softly.assertThat(issue).hasType(TYPE);
        softly.assertThat(issue).hasPackageName(PACKAGE_NAME);
        softly.assertThat(issue).hasPriority(PRIORITY);
        softly.assertThat(issue).hasMessage(MESSAGE);
        softly.assertThat(issue).hasDescription(DESCRIPTION);
        softly.assertThat(issue).hasFingerprint(UNDEFINED);
        softly.assertAll();
    }

    @Test
    void testDefaultIssueNullStringsNegativeIntegers() {
        Issue issue = new Issue(null, 0, 0, 0, 0, null, null, null, PRIORITY, null, null);

        assertIsDefaultIssue(issue);
    }

    @Test
    void testDefaultIssueEmptyStringsNegativeIntegers() {
        Issue issue = new Issue(EMPTY, -1, -1, -1, -1, EMPTY, EMPTY, EMPTY, PRIORITY, EMPTY, EMPTY);

        assertIsDefaultIssue(issue);
    }

    private void assertIsDefaultIssue(final Issue issue) {
        AnalysisSoftAssertions softly = new AnalysisSoftAssertions();
        softly.assertThat(issue.getId()).isNotNull();
        softly.assertThat(issue).hasFileName(UNDEFINED);
        softly.assertThat(issue).hasCategory(EMPTY);
        softly.assertThat(issue).hasLineStart(0);
        softly.assertThat(issue).hasLineEnd(0);
        softly.assertThat(issue).hasColumnStart(0);
        softly.assertThat(issue).hasColumnEnd(0);
        softly.assertThat(issue).hasType(UNDEFINED);
        softly.assertThat(issue).hasPackageName(UNDEFINED);
        softly.assertThat(issue).hasMessage(EMPTY);
        softly.assertThat(issue).hasDescription(EMPTY);
        softly.assertThat(issue).hasFingerprint(UNDEFINED);
        softly.assertAll();
    }

    @Test
    void testZeroLineColumnEndsDefaultToLineColumnStarts() {
        Issue issue = new Issue(FILE_NAME, LINE_START, 0, COLUMN_START, 0, CATEGORY, TYPE, PACKAGE_NAME, null, MESSAGE, DESCRIPTION);

        AnalysisSoftAssertions softly = new AnalysisSoftAssertions();
        softly.assertThat(issue).hasLineStart(LINE_START);
        softly.assertThat(issue).hasLineEnd(LINE_START);
        softly.assertThat(issue).hasColumnStart(COLUMN_START);
        softly.assertThat(issue).hasColumnEnd(COLUMN_START);
        softly.assertAll();
    }

    @Test
    void testNullPriorityDefaultsToNormal() {
        Issue issue = new Issue(FILE_NAME, LINE_START, LINE_END, COLUMN_START, COLUMN_END, CATEGORY, TYPE, PACKAGE_NAME, null, MESSAGE, DESCRIPTION);

        assertThat(issue.getPriority()).isEqualTo(Priority.NORMAL);
    }

    @Test
    void testIdRandomlyGenerated() {
        Issue issue1 = new Issue(FILE_NAME, LINE_START, LINE_END, COLUMN_START, COLUMN_END, CATEGORY, TYPE, PACKAGE_NAME, PRIORITY, MESSAGE, DESCRIPTION);
        Issue issue2 = new Issue(FILE_NAME, LINE_START, LINE_END, COLUMN_START, COLUMN_END, CATEGORY, TYPE, PACKAGE_NAME, PRIORITY, MESSAGE, DESCRIPTION);

        assertThat(issue1.getId()).isNotEqualTo(issue2.getId());
    }

    @Test
    void testFingerprint() {
        Issue issue = new Issue(FILE_NAME, LINE_START, LINE_END, COLUMN_START, COLUMN_END, CATEGORY, TYPE, PACKAGE_NAME, PRIORITY, MESSAGE, DESCRIPTION);

        issue.setFingerprint(FINGERPRINT);

        assertThat(issue.getFingerprint()).isEqualTo(FINGERPRINT);
    }

    @Test
    void testToString() {
        Issue issue = new Issue(FILE_NAME, LINE_START, LINE_END, COLUMN_START, COLUMN_END, CATEGORY, TYPE, PACKAGE_NAME, PRIORITY, MESSAGE, DESCRIPTION);

        assertSoftly(softly -> {
            assertThat(issue.toString()).contains(FILE_NAME);
            assertThat(issue.toString()).contains(Integer.toString(LINE_START));
            assertThat(issue.toString()).doesNotContain(Integer.toString(LINE_END));
            assertThat(issue.toString()).contains(Integer.toString(COLUMN_START));
            assertThat(issue.toString()).doesNotContain(Integer.toString(COLUMN_END));
            assertThat(issue.toString()).contains(CATEGORY);
            assertThat(issue.toString()).contains(TYPE);
            assertThat(issue.toString()).doesNotContain(PACKAGE_NAME);
            assertThat(issue.toString()).doesNotContain(PRIORITY.name());
            assertThat(issue.toString()).contains(MESSAGE);
            assertThat(issue.toString()).doesNotContain(DESCRIPTION);
        });
    }

    @Test
    void testFileNameBackslashConversion() {
        Issue issue = new Issue(FILE_NAME_WITH_BACKSLASHES, LINE_START, LINE_END, COLUMN_START, COLUMN_END, CATEGORY, TYPE, PACKAGE_NAME, PRIORITY, MESSAGE, DESCRIPTION);

        assertThat(issue.getFileName()).isEqualTo(FILE_NAME);
    }

    @Test
    void testMessageDescriptionStripped() {
        Issue issue = new Issue(FILE_NAME, LINE_START, LINE_END, COLUMN_START, COLUMN_END, CATEGORY, TYPE, PACKAGE_NAME, PRIORITY, MESSAGE_NOT_STRIPPED, DESCRIPTION_NOT_STRIPPED);

        assertSoftly(softly -> {
            assertThat(issue.getMessage()).isEqualTo(MESSAGE);
            assertThat(issue.getDescription()).isEqualTo(DESCRIPTION);
        });
    }
}