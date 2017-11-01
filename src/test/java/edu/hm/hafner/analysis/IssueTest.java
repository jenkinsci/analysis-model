package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.assertj.IssueSoftAssertions;
import static edu.hm.hafner.analysis.assertj.CustomAssertions.*;


class IssueTest {

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
    private static final String UNDEFINED_STRING = "-";
    private static final String FINGERPRINT = "fingerprint";

    @Test
    public void shouldConstructWithAllGivenValues() {
        Issue issue = getStandardIssue();

        IssueSoftAssertions softly = new IssueSoftAssertions();
        softly.assertThat(issue).hasId();
        softly.assertThat(issue).hasFilename(FILE_NAME);
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
        softly.assertAll();
    }

    @Test
    public void shouldSetAllNullStringsToUndefinedOrEmpty() {
        Issue issue = new Issue(null, LINE_START, LINE_END, COLUMN_START, COLUMN_END, null, null, null, PRIORITY, null, null);

        IssueSoftAssertions softly = new IssueSoftAssertions();
        softly.assertThat(issue).hasFilename(UNDEFINED_STRING);
        softly.assertThat(issue).hasCategory("");
        softly.assertThat(issue).hasType(UNDEFINED_STRING);
        softly.assertThat(issue).hasPackageName(UNDEFINED_STRING);
        softly.assertThat(issue).hasPriority(PRIORITY);
        softly.assertThat(issue).hasMessage("");
        softly.assertThat(issue).hasDescription("");
        softly.assertAll();
    }


    @Test
    public void shouldSetAllNegativeIntsToZero() {
        Issue issue = new Issue(FILE_NAME, -1, -1, -1, -1, CATEGORY, TYPE, PACKAGE_NAME, PRIORITY, MESSAGE, DESCRIPTION);

        IssueSoftAssertions softly = new IssueSoftAssertions();
        softly.assertThat(issue).hasLineStart(0);
        softly.assertThat(issue).hasLineEnd(0);
        softly.assertThat(issue).hasColumnStart(0);
        softly.assertThat(issue).hasColumnEnd(0);
        softly.assertAll();
    }

    @Test
    public void shouldSetPriorityToLow() {
        Issue issue = new Issue(null, 0, 0, 0, 0, null, null, null, Priority.LOW, null, null);

        assertThat(issue).hasPriority(Priority.LOW);
    }

    @Test
    public void shouldSetPriorityToHigh() {
        Issue issue = new Issue(null, 0, 0, 0, 0, null, null, null, Priority.HIGH, null, null);

        assertThat(issue).hasPriority(Priority.HIGH);
    }

    @Test
    public void shouldHaveDifferentIdsForMultipleIssues() {
        Issue firstIssue = getStandardIssue();
        Issue secondIssue = getStandardIssue();

        assertThat(firstIssue).hasNotSameId(secondIssue);
    }

    @Test
    public void shouldReplaceBackSlashesWithSlashInFilename() {
        Issue issue = new Issue("some\\file", LINE_START, LINE_END, COLUMN_START, COLUMN_END, CATEGORY, TYPE, PACKAGE_NAME, PRIORITY, MESSAGE, DESCRIPTION);

        assertThat(issue).hasFilename("some/file");
    }

    @Test
    public void shouldContainCorrectFieldsInToString() {
        Issue issue = getStandardIssue();

        IssueSoftAssertions softly = new IssueSoftAssertions();
        softly.assertThat(issue.toString()).contains(FILE_NAME);
        softly.assertThat(issue.toString()).contains(Integer.toString(LINE_START));
        softly.assertThat(issue.toString()).contains(Integer.toString(COLUMN_START));
        softly.assertThat(issue.toString()).contains(TYPE);
        softly.assertThat(issue.toString()).contains(CATEGORY);
        softly.assertThat(issue.toString()).contains(MESSAGE);
        softly.assertAll();
    }

    @Test
    public void shouldSetFingerprintCorrectly() {
        Issue issue = getStandardIssue();

        issue.setFingerprint(FINGERPRINT);

        assertThat(issue).hasFingerprint(FINGERPRINT);
    }

    private Issue getStandardIssue() {
        return new Issue(FILE_NAME, LINE_START, LINE_END, COLUMN_START, COLUMN_END, CATEGORY, TYPE, PACKAGE_NAME, PRIORITY, MESSAGE, DESCRIPTION);
    }
}