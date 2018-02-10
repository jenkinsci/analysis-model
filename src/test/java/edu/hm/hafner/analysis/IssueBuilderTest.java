package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;

import static edu.hm.hafner.analysis.IssueTest.*;
import static edu.hm.hafner.analysis.assertj.Assertions.*;

/**
 * Unit test for {@link IssueBuilder}.
 *
 * @author Marcel Binder
 */
class IssueBuilderTest {
    private static final Issue DEFAULT_ISSUE = new Issue(null, 0, 0, 0, 0, new LineRangeList(),
            null, null, null, null, null, null, null, null, null, null);
    private static final Issue FILLED_ISSUE = new Issue(FILE_NAME, LINE_START, LINE_END, COLUMN_START, COLUMN_END,
            LINE_RANGES, CATEGORY, TYPE, PACKAGE_NAME, MODULE_NAME, PRIORITY, MESSAGE, DESCRIPTION, ORIGIN, REFERENCE,
            FINGERPRINT);

    @Test
    void shouldCreateDefaultIssueIfNothingSpecified() {
        Issue issue = new IssueBuilder().build();

        assertThat(issue).isEqualTo(DEFAULT_ISSUE);
    }

    @Test
    void shouldCreateIssueWithAllPropertiesInitialized() {
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
                .setLineRanges(LINE_RANGES)
                .setReference(REFERENCE)
                .setFingerprint(FINGERPRINT)
                .build();

        assertThatIssueIsEqualToFilled(issue);
    }

    private void assertThatIssueIsEqualToFilled(final Issue issue) {
        assertThat(issue).isEqualTo(FILLED_ISSUE);
        assertThat(issue).hasFingerprint(FINGERPRINT);
        assertThat(issue).hasReference(REFERENCE);
    }

    @Test
    void shouldCopyAllPropertiesOfAnIssue() {
        Issue copy = new IssueBuilder()
                .copy(FILLED_ISSUE)
                .build();

        assertThat(copy).isNotSameAs(FILLED_ISSUE);
        assertThatIssueIsEqualToFilled(copy);
    }

    @Test
    void shouldCreateNewInstanceOnEveryCall() {
        IssueBuilder builder = new IssueBuilder().copy(FILLED_ISSUE);
        Issue issue1 = builder.build();
        Issue issue2 = builder.build();

        assertThat(issue1).isNotSameAs(issue2);
        assertThat(issue1).isEqualTo(issue2);
    }

    @Test
    void shouldCollectLineRanges() {
        IssueBuilder builder = new IssueBuilder();

        builder.setLineStart(1).setLineEnd(2);
        LineRangeList lineRanges = new LineRangeList();
        lineRanges.add(new LineRange(3, 4));
        lineRanges.add(new LineRange(5, 6));
        builder.setLineRanges(lineRanges);

        Issue issue = builder.build();
        assertThat(issue).hasLineStart(1).hasLineEnd(2);
        assertThat(issue.getLineRanges()).hasSize(2);
        assertThat(issue.getLineRanges()).containsExactly(new LineRange(3, 4), new LineRange(5, 6));

        IssueBuilder copy = new IssueBuilder();
        copy.copy(issue);

        assertThat(copy.build().getLineRanges()).containsExactly(new LineRange(3, 4), new LineRange(5, 6));
    }
}