package edu.hm.hafner.analysis;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import edu.hm.hafner.analysis.LineRange.LineRangeBuilder;
import edu.hm.hafner.util.TreeString;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import static edu.hm.hafner.analysis.IssueTest.*;
import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Unit test for {@link IssueBuilder}.
 *
 * @author Marcel Binder
 */
@SuppressFBWarnings("DMI")
class IssueBuilderTest {
    private static final String FILE_NAME = "C:/users/tester/file-name";
    static final String FILE_NAME_WITH_BACKSLASHES = "C:\\users\\tester/file-name";
    private static final Issue DEFAULT_ISSUE = new Issue(PATH_NAME, UNDEFINED_TS, 0, 0, 0, 0, new LineRangeList(),
            null, null, UNDEFINED_TS, null, null, EMPTY_TS, EMPTY, null, null, null, null);
    private static final Issue FILLED_ISSUE = new Issue(PATH_NAME, TreeString.valueOf(FILE_NAME), LINE_START,
            LINE_END, COLUMN_START, COLUMN_END,
            LINE_RANGES, CATEGORY, TYPE, TreeString.valueOf(PACKAGE_NAME), MODULE_NAME, SEVERITY,
            TreeString.valueOf(MESSAGE), DESCRIPTION, ORIGIN, REFERENCE,
            FINGERPRINT, ADDITIONAL_PROPERTIES);
    private static final String RELATIVE_FILE = "relative.txt";

    @SuppressFBWarnings("DMI")
    @Test
    void shouldCreateAbsolutePath() {
        IssueBuilder builder = new IssueBuilder();

        builder.setFileName(RELATIVE_FILE);

        assertThat(builder.build())
                .hasFileName(RELATIVE_FILE)
                .hasBaseName(RELATIVE_FILE)
                .hasFolder(UNDEFINED)
                .hasPath(UNDEFINED);

        builder.setDirectory("/tmp");
        builder.setFileName(RELATIVE_FILE);

        assertThat(builder.build())
                .hasFileName("/tmp/" + RELATIVE_FILE)
                .hasBaseName(RELATIVE_FILE)
                .hasFolder("tmp");

        builder.setFileName("/tmp/absolute.txt");
        assertThat(builder.build()).hasFileName("/tmp/absolute.txt");

        builder.setFileName("C:\\tmp\\absolute.txt");
        assertThat(builder.build()).hasFileName("C:/tmp/absolute.txt");

        builder.setFileName(null);
        assertThat(builder.build())
                .hasFileName(UNDEFINED)
                .hasBaseName(UNDEFINED)
                .hasFolder(UNDEFINED);

        builder.setPathName("/path/to/source");
        builder.setDirectory("");
        builder.setFileName(RELATIVE_FILE);
        assertThat(builder.build())
                .hasFileName(RELATIVE_FILE)
                .hasBaseName(RELATIVE_FILE)
                .hasFolder(UNDEFINED)
                .hasPath("/path/to/source");
    }

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
    void shouldMapStringNumbers() {
        IssueBuilder builder = new IssueBuilder();

        assertThat(builder.setLineStart("nix").build()).hasLineStart(0);
        assertThat(builder.setLineStart("-1").build()).hasLineStart(0);
        assertThat(builder.setLineStart("0").build()).hasLineStart(0);
        assertThat(builder.setLineStart("1").build()).hasLineStart(1);

        builder = new IssueBuilder();
        assertThat(builder.setLineEnd("nix").build()).hasLineEnd(0);
        assertThat(builder.setLineEnd("-1").build()).hasLineEnd(0);
        assertThat(builder.setLineEnd("0").build()).hasLineEnd(0);
        assertThat(builder.setLineEnd("1").build()).hasLineEnd(1);

        builder = new IssueBuilder();
        assertThat(builder.setColumnStart("nix").build()).hasColumnStart(0);
        assertThat(builder.setColumnStart("-1").build()).hasColumnStart(0);
        assertThat(builder.setColumnStart("0").build()).hasColumnStart(0);
        assertThat(builder.setColumnStart("1").build()).hasColumnStart(1);

        builder = new IssueBuilder();
        assertThat(builder.setColumnEnd("nix").build()).hasColumnEnd(0);
        assertThat(builder.setColumnEnd("-1").build()).hasColumnEnd(0);
        assertThat(builder.setColumnEnd("0").build()).hasColumnEnd(0);
        assertThat(builder.setColumnEnd("1").build()).hasColumnEnd(1);
    }

    @Test
    @SuppressFBWarnings("DMI")
    void shouldCreateIssueWithAllPropertiesInitialized() {
        IssueBuilder builder = new IssueBuilder();
        Issue issue = builder
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

        Optional<Issue> optional = builder.buildOptional();
        assertThat(optional).contains(issue);
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
        lineRanges.add(createLineRange(3, 4));
        lineRanges.add(createLineRange(5, 6));
        builder.setLineRanges(lineRanges);

        Issue issue = builder.build();
        assertThat(issue).hasLineStart(1).hasLineEnd(2);
        assertThat(issue).hasOnlyLineRanges(createLineRange(3, 4), createLineRange(5, 6));

        IssueBuilder copy = new IssueBuilder();
        copy.copy(issue);

        assertThat(copy.build()).hasOnlyLineRanges(createLineRange(3, 4), createLineRange(5, 6));
    }

    private LineRange createLineRange(final int start, final int end) {
        return new LineRangeBuilder()
                .setStart(start)
                .setEnd(end)
                .build();
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

    @Test
    void testFileNameBackslashConversion() {
        IssueBuilder builder = new IssueBuilder();

        Issue issue = builder.setFileName(FILE_NAME_WITH_BACKSLASHES).build();

        assertThat(issue).hasFileName(FILE_NAME);
    }

    @Test
    void shouldCacheFileName() {
        IssueBuilder builder = new IssueBuilder();

        Issue issue = builder.setFileName("fileName").build();
        Issue anotherIssue = builder.setFileName("fileName").build();

        assertThat(issue.getFileNameTreeString()).isSameAs(anotherIssue.getFileNameTreeString());
    }

    @Test
    void shouldCachePackageName() {
        IssueBuilder builder = new IssueBuilder();

        Issue issue = builder.setPackageName("packageName").build();
        Issue anotherIssue = builder.setFileName("packageName").build();

        assertThat(issue.getPackageNameTreeString()).isSameAs(anotherIssue.getPackageNameTreeString());
    }

    @Test
    void shouldCacheMessage() {
        IssueBuilder builder = new IssueBuilder();

        Issue issue = builder.setMessage("message").build();
        Issue anotherIssue = builder.setMessage("message").build();

        assertThat(issue.getMessageTreeString()).isSameAs(anotherIssue.getMessageTreeString());
    }

    @Test
    void testMessageDescriptionStripped() {
        IssueBuilder builder = new IssueBuilder();

        Issue issue = builder.setMessage("    message  ").setDescription("    description  ").build();
        Issue anotherIssue = builder.setMessage("message").setDescription("description").build();

        assertThat(issue.getMessageTreeString()).isSameAs(anotherIssue.getMessageTreeString());
        assertThat(issue.getDescription()).isSameAs(anotherIssue.getDescription());
    }
}
