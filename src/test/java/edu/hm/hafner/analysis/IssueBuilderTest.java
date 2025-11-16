package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import edu.hm.hafner.util.LineRange;
import edu.hm.hafner.util.LineRangeList;
import edu.hm.hafner.util.TreeString;
import edu.hm.hafner.util.TreeStringBuilder;

import java.util.UUID;

import static edu.hm.hafner.analysis.IssueTest.*;
import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Unit test for {@link IssueBuilder}.
 *
 * @author Marcel Binder
 */
class IssueBuilderTest {
    private static final TreeStringBuilder TREE_STRING_BUILDER = new TreeStringBuilder();
    private static final String FILE_NAME = "C:/users/tester/file-name";
    static final String FILE_NAME_WITH_BACKSLASHES = "C:\\users\\tester/file-name";
    private static final Issue DEFAULT_ISSUE = new Issue(PATH_NAME, UNDEFINED_TS, 0, 0, 0, 0, new LineRangeList(), null,
            null, null, UNDEFINED_TS, null, null, EMPTY_TS, EMPTY, null, null, null, null, null);
    private static final Issue FILLED_ISSUE = new Issue(PATH_NAME, TreeString.valueOf(FILE_NAME), LINE_START,
            LINE_END, COLUMN_START, COLUMN_END,
            LINE_RANGES, null, CATEGORY, TYPE, TreeString.valueOf(PACKAGE_NAME), MODULE_NAME, SEVERITY,
            TreeString.valueOf(MESSAGE), DESCRIPTION, ORIGIN, ORIGIN_NAME, REFERENCE,
            FINGERPRINT, ADDITIONAL_PROPERTIES);
    private static final String RELATIVE_FILE = "relative.txt";

    @Test
    void shouldCreateAbsolutePath() {
        try (var builder = new IssueBuilder()) {
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
        try (var issueBuilder = new IssueBuilder()) {
            assertThat(issueBuilder.setFileName(fullPath).build()).hasBaseName("file.txt");
        }
    }

    @Test
    void shouldCreateDefaultIssueIfNothingSpecified() {
        try (var builder = new IssueBuilder()) {
            var issue = builder.build();

            assertThat(issue).isEqualTo(DEFAULT_ISSUE);
        }
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
        try (var builder = new IssueBuilder()) {
            builder.setLineStart(start).setLineEnd(end);
            assertThat(builder.build()).hasLineStart(expectedStart).hasLineEnd(expectedEnd);
        }
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
        try (var builder = new IssueBuilder()) {
            builder.setColumnStart(start).setColumnEnd(end);
            assertThat(builder.build()).hasColumnStart(expectedStart).hasColumnEnd(expectedEnd);
        }
    }

    @Test
    void shouldMapStringNumbers() {
        try (var builder = new IssueBuilder()) {
            assertThat(builder.setLineStart("nix").build()).hasLineStart(0);
            assertThat(builder.setLineStart("-1").build()).hasLineStart(0);
            assertThat(builder.setLineStart("0").build()).hasLineStart(0);
            assertThat(builder.setLineStart("1").build()).hasLineStart(1);
        }
        try (var builder = new IssueBuilder()) {
            assertThat(builder.setLineEnd("nix").build()).hasLineEnd(0);
            assertThat(builder.setLineEnd("-1").build()).hasLineEnd(0);
            assertThat(builder.setLineEnd("0").build()).hasLineEnd(0);
            assertThat(builder.setLineEnd("1").build()).hasLineEnd(1);
        }
        try (var builder = new IssueBuilder()) {
            assertThat(builder.setColumnStart("nix").build()).hasColumnStart(0);
            assertThat(builder.setColumnStart("-1").build()).hasColumnStart(0);
            assertThat(builder.setColumnStart("0").build()).hasColumnStart(0);
            assertThat(builder.setColumnStart("1").build()).hasColumnStart(1);
        }
        try (var builder = new IssueBuilder()) {
            assertThat(builder.setColumnEnd("nix").build()).hasColumnEnd(0);
            assertThat(builder.setColumnEnd("-1").build()).hasColumnEnd(0);
            assertThat(builder.setColumnEnd("0").build()).hasColumnEnd(0);
            assertThat(builder.setColumnEnd("1").build()).hasColumnEnd(1);
        }
    }

    @Test
    void shouldCreateIssueWithAllPropertiesInitialized() {
        try (var builder = new IssueBuilder()) {
            var issue = builder
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
                    .setOriginName(ORIGIN_NAME)
                    .setLineRanges(LINE_RANGES)
                    .setReference(REFERENCE)
                    .setFingerprint(FINGERPRINT)
                    .setAdditionalProperties(ADDITIONAL_PROPERTIES)
                    .build();

            assertThatIssueIsEqualToFilled(issue);
            assertThatIssueIsEqualToFilled(builder.copy(issue).build());
            assertThatIssueIsEqualToFilled(builder.build()); // same result because builder is not cleaned
            assertThatIssueIsEqualToFilled(builder.buildAndClean());

            try (var emptyBuilder = new IssueBuilder()) {
                emptyBuilder.setOrigin(ORIGIN);
                emptyBuilder.setOriginName(ORIGIN_NAME);
                assertThat(builder.build()).isEqualTo(emptyBuilder.build());
            }
        }
    }

    private void assertThatIssueIsEqualToFilled(final Issue issue) {
        assertThat(issue).isEqualTo(FILLED_ISSUE);
        assertThat(issue).hasFingerprint(FINGERPRINT);
        assertThat(issue).hasReference(REFERENCE);
    }

    @Test
    void shouldCopyAllPropertiesOfAnIssue() {
        try (var builder = new IssueBuilder()) {
            var copy = builder.copy(FILLED_ISSUE).build();

            assertThat(copy).isNotSameAs(FILLED_ISSUE);
            assertThatIssueIsEqualToFilled(copy);
        }
    }

    @Test
    void shouldCreateNewInstanceOnEveryCall() {
        try (var builder = new IssueBuilder()) {
            builder.copy(FILLED_ISSUE);
            var issue1 = builder.build();
            var issue2 = builder.build();

            assertThat(issue1).isNotSameAs(issue2);
            assertThat(issue1).isEqualTo(issue2);
        }
    }

    @Test
    void shouldCollectLineRanges() {
        try (var builder = new IssueBuilder()) {
            builder.setLineStart(1).setLineEnd(2);
            var lineRanges = new LineRangeList();
            lineRanges.add(new LineRange(3, 4));
            lineRanges.add(new LineRange(5, 6));
            builder.setLineRanges(lineRanges);

            var issue = builder.build();
            assertThat(issue).hasLineStart(1).hasLineEnd(2);
            assertThat(issue).hasOnlyLineRanges(new LineRange(3, 4), new LineRange(5, 6));

            try (var copy = new IssueBuilder()) {
                copy.copy(issue);
                assertThat(copy.build()).hasOnlyLineRanges(new LineRange(3, 4), new LineRange(5, 6));
            }
        }
    }

    @Test
    void shouldMoveLineRangeToAttributes() {
        try (var builder = new IssueBuilder()) {
            var lineRanges = new LineRangeList();
            lineRanges.add(new LineRange(1, 2));
            builder.setLineRanges(lineRanges);

            var issue = builder.build();
            assertThat(issue).hasLineStart(1).hasLineEnd(2);
            assertThat(issue).hasNoLineRanges();
        }
    }

    @Test
    void shouldMoveLineRangeToAttributesEvenIfLineEndIsSet() {
        try (var builder = new IssueBuilder()) {
            builder.setLineEnd(2);
            var lineRanges = new LineRangeList();
            lineRanges.add(new LineRange(1, 2));
            builder.setLineRanges(lineRanges);

            var issue = builder.build();
            assertThat(issue).hasLineStart(1).hasLineEnd(2);
            assertThat(issue).hasNoLineRanges();
        }
    }

    @Test
    void shouldCleanupLineRanges() {
        try (var builder = new IssueBuilder()) {
            builder.setLineStart(1).setLineEnd(2);
            var lineRanges = new LineRangeList();
            lineRanges.add(new LineRange(1, 2));
            builder.setLineRanges(lineRanges);

            var issue = builder.build();
            assertThat(issue).hasLineStart(1).hasLineEnd(2);
            assertThat(issue).hasNoLineRanges();
        }
    }

    @Test
    void shouldNotCleanupDifferentLineRanges() {
        try (var builder = new IssueBuilder()) {
            builder.setLineStart(1).setLineEnd(2);
            var lineRanges = new LineRangeList();
            lineRanges.add(new LineRange(1, 3));
            builder.setLineRanges(lineRanges);

            var issue = builder.build();
            assertThat(issue).hasLineStart(1).hasLineEnd(2);
            assertThat(issue).hasOnlyLineRanges(new LineRange(1, 3));
        }
    }

    @Test
    void shouldUseProvidedId() {
        try (var builder = new IssueBuilder()) {
            var id = UUID.randomUUID();
            builder.setId(id);

            assertThat(builder.build()).hasId(id);
            assertThat(builder.build().getId()).isNotEqualTo(id); // new random ID

            builder.setId(id);
            assertThat(builder.build()).hasId(id);
        }
    }

    @Test
    void testFileNameBackslashConversion() {
        try (var builder = new IssueBuilder()) {
            var issue = builder.setFileName(FILE_NAME_WITH_BACKSLASHES).build();

            assertThat(issue).hasFileName(FILE_NAME);
        }
    }

    @Test
    void shouldCacheFileName() {
        try (var builder = new IssueBuilder()) {
            var issue = builder.setFileName("fileName").build();
            var anotherIssue = builder.setFileName("fileName").build();

            assertThat(issue.getFileNameTreeString()).isSameAs(anotherIssue.getFileNameTreeString());
        }
    }

    @Test
    void shouldCachePackageName() {
        try (var builder = new IssueBuilder()) {
            var issue = builder.setPackageName("packageName").build();
            var anotherIssue = builder.setFileName("packageName").build();

            assertThat(issue.getPackageNameTreeString()).isSameAs(anotherIssue.getPackageNameTreeString());
        }
    }

    @Test
    void shouldCacheMessage() {
        try (var builder = new IssueBuilder()) {
            var issue = builder.setMessage("message").build();
            var anotherIssue = builder.setMessage("message").build();

            assertThat(issue.getMessageTreeString()).isSameAs(anotherIssue.getMessageTreeString());
        }
    }

    @Test
    void testMessageDescriptionStripped() {
        try (var builder = new IssueBuilder()) {
            var issue = builder.setMessage("    message  ").setDescription("    description  ").build();
            var anotherIssue = builder.setMessage("message").setDescription("description").build();

            assertThat(issue.getMessageTreeString()).isSameAs(anotherIssue.getMessageTreeString());
            assertThat(issue.getDescription()).isSameAs(anotherIssue.getDescription());
        }
    }

    @Test
    void shouldAddAdditionalLocations() {
        try (var builder = new IssueBuilder()) {
            var location1 = new Location(TREE_STRING_BUILDER.intern("header.h"), 10, 20);
            var location2 = new Location(TREE_STRING_BUILDER.intern("impl.cpp"), 50);

            builder.addAdditionalLocation(location1);
            builder.addAdditionalLocation(location2);

            var issue = builder.build();
            org.assertj.core.api.Assertions.assertThat(issue.hasAdditionalLocations()).isTrue();
            org.assertj.core.api.Assertions.assertThat(issue.getAdditionalLocations()).hasSize(2);
            org.assertj.core.api.Assertions.assertThat(issue.getAdditionalLocations()).containsExactly(location1, location2);
        }
    }

    @Test
    void shouldSetAdditionalLocations() {
        try (var builder = new IssueBuilder()) {
            var locations = java.util.List.of(
                    new Location(TREE_STRING_BUILDER.intern("header.h"), 10, 20),
                    new Location(TREE_STRING_BUILDER.intern("impl.cpp"), 50)
            );

            builder.setAdditionalLocations(locations);

            var issue = builder.build();
            org.assertj.core.api.Assertions.assertThat(issue.hasAdditionalLocations()).isTrue();
            org.assertj.core.api.Assertions.assertThat(issue.getAdditionalLocations()).hasSize(2);
        }
    }

    @Test
    void shouldCopyAdditionalLocations() {
        try (var builder = new IssueBuilder()) {
            var location1 = new Location(TREE_STRING_BUILDER.intern("header.h"), 10, 20);
            var location2 = new Location(TREE_STRING_BUILDER.intern("impl.cpp"), 50);

            builder.addAdditionalLocation(location1);
            builder.addAdditionalLocation(location2);

            var originalIssue = builder.build();

            try (var copyBuilder = new IssueBuilder()) {
                copyBuilder.copy(originalIssue);
                var copiedIssue = copyBuilder.build();

                org.assertj.core.api.Assertions.assertThat(copiedIssue.hasAdditionalLocations()).isTrue();
                org.assertj.core.api.Assertions.assertThat(copiedIssue.getAdditionalLocations()).hasSize(2);
                org.assertj.core.api.Assertions.assertThat(copiedIssue.getAdditionalLocations()).containsExactly(location1, location2);
            }
        }
    }

    @Test
    void shouldNotHaveAdditionalLocationsWhenNoneSet() {
        try (var builder = new IssueBuilder()) {
            var issue = builder.build();
            org.assertj.core.api.Assertions.assertThat(issue.hasAdditionalLocations()).isFalse();
            org.assertj.core.api.Assertions.assertThat(issue.getAdditionalLocations()).isEmpty();
        }
    }
}
