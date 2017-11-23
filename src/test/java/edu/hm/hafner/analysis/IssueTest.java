package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.assertj.AnalysisSoftAssertions;
import edu.hm.hafner.analysis.assertj.IssueAssert;

/**
 * Created by Sarah on 11.11.2017.
 */

class IssueTest {
    static final String FILE_NAME = "C:/users/tester/testX/file-name";
    static final int LINE_START = 1;
    static final int LINE_END = 2;
    static final int COLUMN_START = 3;
    static final int COLUMN_END = 4;
    static final String CATEGORY = "category";
    static final String TYPE = "type";
    static final String PACKAGE_NAME = "package-name";
    static final Priority HIGH_PRIORITY = Priority.HIGH;
    static final String MESSAGE = "message";
    static final String DESCRIPTION = "description";
    static final String EMPTY_STRING = "";
    static final String UNDEFINED = "-";

    @Test
    void testCreateOneIssue() {
        Issue issue = new Issue(FILE_NAME, LINE_START, LINE_END, COLUMN_START, COLUMN_END, CATEGORY, TYPE, PACKAGE_NAME, HIGH_PRIORITY, MESSAGE, DESCRIPTION);

        AnalysisSoftAssertions softly = new AnalysisSoftAssertions();
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
                .hasPriority(HIGH_PRIORITY)
                .hasMessage(MESSAGE)
                .hasFingerprint(UNDEFINED)
                .hasDescription(DESCRIPTION);
    }

    @Test
    void testCreateDefaultIssueWithNullStringsAndZeroIntegers() {
        Issue issue = new Issue(null, 0, 0, 0, 0, null, null, null, HIGH_PRIORITY, null, null);
        AnalysisSoftAssertions softly = new AnalysisSoftAssertions();
        softly.assertThat(issue.getId()).isNotNull();
        softly.assertThat(issue)
                .hasFileName(UNDEFINED)
                .hasCategory(EMPTY_STRING)
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasColumnStart(0)
                .hasColumnEnd(0)
                .hasType(UNDEFINED)
                .hasPackageName(UNDEFINED)
                .hasMessage(EMPTY_STRING)
                .hasDescription(EMPTY_STRING)
                .hasFingerprint(UNDEFINED);
    }

    @Test
    void testCreateDefaultIssueWithEmptyStringsAndNegativeIntegers() {
        Issue issue = new Issue(EMPTY_STRING, -1, -1, -1, -1, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, HIGH_PRIORITY, EMPTY_STRING, EMPTY_STRING);
        AnalysisSoftAssertions softly = new AnalysisSoftAssertions();
        softly.assertThat(issue.getId()).isNotNull();
        softly.assertThat(issue)
                .hasFileName(UNDEFINED)
                .hasCategory(EMPTY_STRING)
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasColumnStart(0)
                .hasColumnEnd(0)
                .hasType(UNDEFINED)
                .hasPackageName(UNDEFINED)
                .hasMessage(EMPTY_STRING)
                .hasDescription(EMPTY_STRING)
                .hasFingerprint(UNDEFINED);
    }

    @Test
    void testIssueWithZeroLineColumnEndsDefaultToLineColumnStarts() {
        Issue issue = new Issue(FILE_NAME, LINE_START, 0, COLUMN_START, 0, CATEGORY, TYPE, PACKAGE_NAME, null, MESSAGE, DESCRIPTION);

        AnalysisSoftAssertions softly = new AnalysisSoftAssertions();
        softly.assertThat(issue)
                .hasLineStart(LINE_START)
                .hasLineEnd(LINE_START)
                .hasColumnStart(COLUMN_START)
                .hasColumnEnd(COLUMN_START);
    }

    @Test
    void testIssueWithNullPriorityDefaultsToNormal() {
        Issue issue = new Issue(FILE_NAME, LINE_START, LINE_END, COLUMN_START, COLUMN_END, CATEGORY, TYPE, PACKAGE_NAME, null, MESSAGE, DESCRIPTION);

        IssueAssert.assertThat(issue).hasPriority(Priority.NORMAL);
    }

    @Test
    void testIssueToString() {
        Issue issue = new Issue(FILE_NAME, LINE_START, LINE_END, COLUMN_START, COLUMN_END, CATEGORY, TYPE, PACKAGE_NAME, HIGH_PRIORITY, MESSAGE, DESCRIPTION);
        AnalysisSoftAssertions softly = new AnalysisSoftAssertions();
        softly.assertThat(issue.toString()).contains(FILE_NAME);
        softly.assertThat(issue.toString()).contains(Integer.toString(LINE_START));
        softly.assertThat(issue.toString()).contains(Integer.toString(COLUMN_START));
        softly.assertThat(issue.toString()).contains(CATEGORY);
        softly.assertThat(issue.toString()).contains(TYPE);
        softly.assertThat(issue.toString()).contains(MESSAGE);
    }

    @Test
    void testRandomGeneratedId() {
        Issue issue1 = new Issue(FILE_NAME, LINE_START, LINE_END, COLUMN_START, COLUMN_END, CATEGORY, TYPE, PACKAGE_NAME, HIGH_PRIORITY, MESSAGE, DESCRIPTION);
        Issue issue2 = new Issue(FILE_NAME, LINE_START, LINE_END, COLUMN_START, COLUMN_END, CATEGORY, TYPE, PACKAGE_NAME, HIGH_PRIORITY, MESSAGE, DESCRIPTION);

        IssueAssert.assertThat(issue1).hasUuid(issue2.getId());
    }

    @Test
    void testFingerprint() {
        String fingerprint = "fingerprint";
        Issue issue = new Issue(FILE_NAME, LINE_START, LINE_END, COLUMN_START, COLUMN_END, CATEGORY, TYPE, PACKAGE_NAME, HIGH_PRIORITY, MESSAGE, DESCRIPTION);

        issue.setFingerprint(fingerprint);

        IssueAssert.assertThat(issue).hasFingerprint(fingerprint);
    }


    @Test
    void testMessageAndDescriptionAreStripped() {
        Issue issue = new Issue(FILE_NAME, LINE_START, LINE_END, COLUMN_START, COLUMN_END, CATEGORY, TYPE, PACKAGE_NAME, HIGH_PRIORITY, "    message  ", "    description  ");

        AnalysisSoftAssertions softly = new AnalysisSoftAssertions();
        softly.assertThat(issue)
                .hasMessage(MESSAGE)
                .hasDescription(DESCRIPTION);
    }

    @Test
    void testConversionOfBackslashesInFilename() {
        Issue issue = new Issue("C:\\users\\tester\\testX\\file-name", LINE_START, LINE_END, COLUMN_START, COLUMN_END, CATEGORY, TYPE, PACKAGE_NAME, HIGH_PRIORITY, MESSAGE, DESCRIPTION);

        IssueAssert.assertThat(issue).hasFileName(FILE_NAME);
    }
}
