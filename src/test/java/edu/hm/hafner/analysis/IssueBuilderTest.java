package edu.hm.hafner.analysis;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static edu.hm.hafner.analysis.IssueTest.*;
import static edu.hm.hafner.analysis.assertj.Assertions.*;

/**
 * Unit test for {@link IssueBuilder}.
 *
 * @author Marcel Binder
 */
class IssueBuilderTest {
    private static final Issue DEFAULT_ISSUE = new Issue(null, 0, 0, 0, 0, new LineRangeList(),
            null, null, null, null, null, null, null, null, null, null, null);
    private static final Issue FILLED_ISSUE = new Issue(FILE_NAME, LINE_START, LINE_END, COLUMN_START, COLUMN_END,
            LINE_RANGES, CATEGORY, TYPE, PACKAGE_NAME, MODULE_NAME, SEVERITY, MESSAGE, DESCRIPTION, ORIGIN, REFERENCE,
            FINGERPRINT, ADDITIONAL_PROPERTIES);

    @ParameterizedTest(name = "{index} => Full Path: {0} - Expected Base Name: file.txt")
    @ValueSource(strings = {
            "/path/to/file.txt", 
            "./file.txt", 
            "file.txt", 
            "C:\\Programme\\Folder\\file.txt", 
            "C:\\file.txt"
    })
    void shouldGetBaseName(final String fullPath) {
        IssueBuilder issueBuilder = new IssueBuilder();

        assertThat(issueBuilder.setFileName(fullPath).build()).hasBaseName("file.txt");
    }
    
    @Test
    void shouldCreateDefaultIssueIfNothingSpecified() {
        Issue issue = new IssueBuilder().build();

        assertThat(issue).isEqualTo(DEFAULT_ISSUE);
    }

    @ParameterizedTest(name = "{index} => Input: [{0} - {1}] - Expected Output: [{2} - {3}]")
    @CsvSource({
            "1, 1, 1, 1",
            "1, 2, 1, 2",
            "2, 1, 1, 2",
            "0, 1, 1, 1",
            "0, 0, 0, 0",
            "0, -1, 0, 0",
            "1, -1, 1, 1",
            "1, 0, 1, 1",
            "-1, 0, 0, 0",
            "-1, 1, 1, 1",
            "-1, -1, 0, 0"})
    void shouldHaveValidLineRange(
            final int start, final int end, final int expectedStart, final int expectedEnd) {
        IssueBuilder builder = new IssueBuilder();

        builder.setLineStart(start).setLineEnd(end);
        assertThat(builder.build()).hasLineStart(expectedStart).hasLineEnd(expectedEnd);
    }

    @ParameterizedTest(name = "{index} => Input: [{0} - {1}] - Expected Output: [{2} - {3}]")
    @CsvSource({
            "1, 1, 1, 1",
            "1, 2, 1, 2",
            "2, 1, 1, 2",
            "0, 1, 1, 1",
            "0, 0, 0, 0",
            "0, -1, 0, 0",
            "1, -1, 1, 1",
            "1, 0, 1, 1",
            "-1, 0, 0, 0",
            "-1, 1, 1, 1",
            "-1, -1, 0, 0"})
    void shouldHaveValidColumnRange(
            final int start, final int end, final int expectedStart, final int expectedEnd) {
        IssueBuilder builder = new IssueBuilder();

        builder.setColumnStart(start).setColumnEnd(end);
        assertThat(builder.build()).hasColumnStart(expectedStart).hasColumnEnd(expectedEnd);
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
                .setSeverity(SEVERITY)
                .setMessage(MESSAGE)
                .setDescription(DESCRIPTION)
                .setOrigin(ORIGIN)
                .setLineRanges(LINE_RANGES)
                .setReference(REFERENCE)
                .setFingerprint(FINGERPRINT)
                .setAdditionalProperties(ADDITIONAL_PROPERTIES)
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

    @Test
    void shouldUseProvidedId() {
        UUID id = UUID.randomUUID();

        IssueBuilder builder = new IssueBuilder();
        builder.setId(id);

        assertThat(builder.build()).hasId(id);
        assertThat(builder.build().getId()).isNotEqualTo(id); // new random ID

        builder.setId(id);
        assertThat(builder.build()).hasId(id);
    }
}