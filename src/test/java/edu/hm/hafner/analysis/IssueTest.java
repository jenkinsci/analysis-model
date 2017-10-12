package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.SoftAssertions.*;

/**
 * Unit test for {@link Issue}.
 */
class IssueTest {
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
    private static final String EMPTY = "";
    private static final String UNDEFINED = "-";
    private static final String FINGERPRINT = "fingerprint";

    @Test
    void testIssue() {
        Issue issue = new Issue(FILE_NAME, LINE_START, LINE_END, COLUMN_START, COLUMN_END, CATEGORY, TYPE, PACKAGE_NAME, PRIORITY, MESSAGE, DESCRIPTION);

        assertSoftly(softly -> {
            softly.assertThat(issue.getId()).isNotNull();
            softly.assertThat(issue.getFileName()).isEqualTo(FILE_NAME);
            softly.assertThat(issue.getCategory()).isEqualTo(CATEGORY);
            softly.assertThat(issue.getLineStart()).isEqualTo(LINE_START);
            softly.assertThat(issue.getLineEnd()).isEqualTo(LINE_END);
            softly.assertThat(issue.getColumnStart()).isEqualTo(COLUMN_START);
            softly.assertThat(issue.getColumnEnd()).isEqualTo(COLUMN_END);
            softly.assertThat(issue.getType()).isEqualTo(TYPE);
            softly.assertThat(issue.getPackageName()).isEqualTo(PACKAGE_NAME);
            softly.assertThat(issue.getPriority()).isEqualTo(PRIORITY);
            softly.assertThat(issue.getMessage()).isEqualTo(MESSAGE);
            softly.assertThat(issue.getDescription()).isEqualTo(DESCRIPTION);
            softly.assertThat(issue.getFingerprint()).isEqualTo(UNDEFINED);
        });
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

    void assertIsDefaultIssue(final Issue issue) {
        assertSoftly(softly -> {
            softly.assertThat(issue.getId()).isNotNull();
            softly.assertThat(issue.getFileName()).isEqualTo(UNDEFINED);
            softly.assertThat(issue.getCategory()).isEqualTo(EMPTY);
            softly.assertThat(issue.getLineStart()).isEqualTo(0);
            softly.assertThat(issue.getLineEnd()).isEqualTo(0);
            softly.assertThat(issue.getColumnStart()).isEqualTo(0);
            softly.assertThat(issue.getColumnEnd()).isEqualTo(0);
            softly.assertThat(issue.getType()).isEqualTo(UNDEFINED);
            softly.assertThat(issue.getPackageName()).isEqualTo(UNDEFINED);
            softly.assertThat(issue.getMessage()).isEqualTo(EMPTY);
            softly.assertThat(issue.getDescription()).isEqualTo(EMPTY);
            softly.assertThat(issue.getFingerprint()).isEqualTo(UNDEFINED);
        });
    }

    @Test
    void testZeroLineColumnEndsDefaultToLineColumnStarts() {
        Issue issue = new Issue(FILE_NAME, LINE_START, 0, COLUMN_START, 0, CATEGORY, TYPE, PACKAGE_NAME, null, MESSAGE, DESCRIPTION);

        assertSoftly(softly -> {
            softly.assertThat(issue.getLineStart()).isEqualTo(LINE_START);
            softly.assertThat(issue.getLineEnd()).isEqualTo(LINE_START);
            softly.assertThat(issue.getColumnStart()).isEqualTo(COLUMN_START);
            softly.assertThat(issue.getColumnEnd()).isEqualTo(COLUMN_START);
        });
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
}