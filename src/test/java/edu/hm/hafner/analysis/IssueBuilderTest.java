package edu.hm.hafner.analysis;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.google.errorprone.annotations.CanIgnoreReturnValue;

import edu.hm.hafner.util.LineRange;
import edu.hm.hafner.util.LineRangeList;
import edu.hm.hafner.util.TreeString;
import edu.hm.hafner.util.TreeStringBuilder;

import java.util.ArrayList;
import java.util.List;
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
    private static final String FILE_NAME_WITH_BACKSLASHES = "C:\\users\\tester/file-name";

    private static final Issue DEFAULT_ISSUE = createDefaultIssue();
    private static final Issue FILLED_ISSUE = createFilledIssue();

    private static Issue createDefaultIssue() {
        return new Issue(StringUtils.EMPTY, List.of(new Location(UNDEFINED_TS)), StringUtils.EMPTY,
                Issue.UNDEFINED, UNDEFINED_TS, UNDEFINED, Severity.WARNING_NORMAL,
                TREE_STRING_BUILDER.intern(StringUtils.EMPTY), EMPTY, EMPTY,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, null, UUID.randomUUID());
    }

    private static Issue createFilledIssue() {
        return new Issue(PATH_NAME, createLocations(), CATEGORY, TYPE, TreeString.valueOf(PACKAGE_NAME), MODULE_NAME, SEVERITY,
                TreeString.valueOf(MESSAGE), DESCRIPTION, ORIGIN, ORIGIN_NAME, REFERENCE,
                FINGERPRINT, ADDITIONAL_PROPERTIES, UUID.randomUUID());
    }

    private static List<Location> createLocations() {
        var locations = new ArrayList<Location>();
        locations.add(new Location(TreeString.valueOf(FILE_NAME), LINE_START, LINE_END, COLUMN_START, COLUMN_END));
        locations.add(new Location(TreeString.valueOf(FILE_NAME), 5, 6));
        return locations;
    }

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
                    .setCategory(CATEGORY)
                    .setType(TYPE)
                    .setPackageName(PACKAGE_NAME)
                    .setModuleName(MODULE_NAME)
                    .setSeverity(SEVERITY)
                    .setMessage(MESSAGE)
                    .setDescription(DESCRIPTION)
                    .setOrigin(ORIGIN)
                    .setOriginName(ORIGIN_NAME)
                    .setLocations(createLocations())
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
    @SuppressWarnings("deprecation") // make sure that deprecated methods work as expected
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
    @SuppressWarnings("deprecation") // make sure that deprecated methods work as expected
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
    @SuppressWarnings("deprecation") // make sure that deprecated methods work as expected
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
    @SuppressWarnings("deprecation") // make sure that deprecated methods work as expected
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

            assertThat(issue.getInternalPackageName()).isSameAs(anotherIssue.getInternalPackageName());
        }
    }

    @Test
    void shouldCacheMessage() {
        try (var builder = new IssueBuilder()) {
            var issue = builder.setMessage("message").build();
            var anotherIssue = builder.setMessage("message").build();

            assertThat(issue.getInternalMessage()).isSameAs(anotherIssue.getInternalMessage());
        }
    }

    @Test
    void testMessageDescriptionStripped() {
        try (var builder = new IssueBuilder()) {
            var issue = builder.setMessage("    message  ").setDescription("    description  ").build();
            var anotherIssue = builder.setMessage("message").setDescription("description").build();

            assertThat(issue.getInternalMessage()).isSameAs(anotherIssue.getInternalMessage());
            assertThat(issue.getDescription()).isSameAs(anotherIssue.getDescription());
        }
    }

    @Test
    void shouldAddAdditionalLocations() {
        try (var builder = new IssueBuilder()) {
            var first = new Location(TREE_STRING_BUILDER.intern("header.h"), 10, 20);
            var second = new Location(TREE_STRING_BUILDER.intern("impl.cpp"), 50);

            builder.addLocation(first);
            builder.addLocation(second);

            assertThatIssueContains(builder.build(), first, second);
        }
    }

    @CanIgnoreReturnValue
    private Issue assertThatIssueContains(final Issue issue, final Location location1, final Location location2) {
        assertThat(issue)
                .hasLocations(location1, location2)
                .hasPrimaryLocation(location1)
                .hasSecondaryLocations()
                .hasSecondaryLocations(location2);
        return issue;
    }

    @Test
    void shouldSetAdditionalLocations() {
        try (var builder = new IssueBuilder()) {
            var locations = List.of(
                    new Location(TREE_STRING_BUILDER.intern("header.h"), 10, 20),
                    new Location(TREE_STRING_BUILDER.intern("impl.cpp"), 50)
            );

            builder.setLocations(locations);

            assertThatIssueContains(builder.build(), locations.get(0), locations.get(1));
        }
    }

    @Test
    void shouldCopyAdditionalLocations() {
        try (var builder = new IssueBuilder()) {
            var firstLocation = new Location(TREE_STRING_BUILDER.intern("header.h"), 10, 20);
            builder.addLocation(firstLocation);

            var secondLocation = new Location(TREE_STRING_BUILDER.intern("impl.cpp"), 50);
            builder.addLocation(secondLocation);

            var originalIssue = assertThatIssueContains(builder.build(), firstLocation, secondLocation);

            try (var copyBuilder = new IssueBuilder()) {
                copyBuilder.copy(originalIssue);

                assertThatIssueContains(copyBuilder.build(), firstLocation, secondLocation);
            }
        }
    }

    @Test
    void shouldNotHaveAdditionalLocationsWhenNoneSet() {
        try (var builder = new IssueBuilder()) {
            var issue = builder.build();

            // No additional locations when none are set
            assertThat(issue).hasNoSecondaryLocations();
            assertThat(issue.getLocations()).hasSize(1);  // Primary location always exists
        }
    }
}
