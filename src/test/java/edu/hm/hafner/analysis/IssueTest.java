package edu.hm.hafner.analysis;

import java.io.IOException;
import java.util.UUID;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.util.SerializableTest;
import edu.hm.hafner.util.TreeString;
import edu.hm.hafner.util.TreeStringBuilder;
import edu.umd.cs.findbugs.annotations.Nullable;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import static edu.hm.hafner.analysis.assertions.Assertions.*;
import static java.util.Collections.*;

/**
 * Unit tests for {@link Issue}.
 *
 * @author Marcel Binder
 */
@SuppressFBWarnings("DMI")
class IssueTest extends SerializableTest<Issue> {
    private static final String SERIALIZATION_NAME = "issue.ser";
    private static final TreeStringBuilder TREE_STRING_BUILDER = new TreeStringBuilder();

    private static final String BASE_NAME = "file.txt";
    static final String FILE_NAME = "some/relative/path/to/" + BASE_NAME;
    static final TreeString FILE_NAME_TS = TREE_STRING_BUILDER.intern(FILE_NAME);
    static final String PATH_NAME = "/path/to/affected/files";

    static final int LINE_START = 1;
    static final int LINE_END = 2;
    static final int COLUMN_START = 3;
    static final int COLUMN_END = 4;
    static final String CATEGORY = "category";
    static final String TYPE = "type";
    static final String PACKAGE_NAME = "package-name";
    static final TreeString PACKAGE_NAME_TS = TREE_STRING_BUILDER.intern(PACKAGE_NAME);
    static final String MODULE_NAME = "module-name";
    static final Severity SEVERITY = Severity.WARNING_HIGH;
    static final String MESSAGE = "message";
    static final TreeString MESSAGE_TS = TREE_STRING_BUILDER.intern(MESSAGE);
    static final String DESCRIPTION = "description";
    static final String EMPTY = "";
    static final TreeString EMPTY_TS = TREE_STRING_BUILDER.intern(EMPTY);
    static final String UNDEFINED = "-";
    static final TreeString UNDEFINED_TS = TREE_STRING_BUILDER.intern(UNDEFINED);
    static final String FINGERPRINT = "fingerprint";
    static final String ORIGIN = "origin";
    static final String REFERENCE = "reference";
    static final String ADDITIONAL_PROPERTIES = "additional";
    static final LineRangeList LINE_RANGES = new LineRangeList(singletonList(new LineRangeBuilder().withLineRange(5, 6).build()));
    private static final String WINDOWS_PATH = "C:/Windows";

    /**
     * Creates a new subject under test (i.e. a sub-type of {@link Issue}) using the specified properties.
     *
     * @param pathName
     *         the path that contains the affected file
     * @param fileName
     *         the name of the file that contains this issue
     * @param lineStart
     *         the first line of this issue (lines start at 1; 0 indicates the whole file)
     * @param lineEnd
     *         the last line of this issue (lines start at 1)
     * @param columnStart
     *         the first column of this issue (columns start at 1, 0 indicates the whole line)
     * @param columnEnd
     *         the last column of this issue (columns start at 1)
     * @param category
     *         the category of this issue (depends on the available categories of the static analysis tool)
     * @param type
     *         the type of this issue (depends on the available types of the static analysis tool)
     * @param packageName
     *         the name of the package (or name space) that contains this issue
     * @param moduleName
     *         the name of the moduleName (or project) that contains this issue
     * @param priority
     *         the priority of this issue
     * @param message
     *         the detail message of this issue
     * @param description
     *         the description for this issue
     * @param origin
     *         the ID of the tool that did report this issue
     * @param reference
     *         an arbitrary reference to the execution of the static analysis tool (build ID, timestamp, etc.)
     * @param fingerprint
     *         the finger print for this issue
     * @param additionalProperties
     *         additional properties from the statical analysis tool
     *
     * @return the subject under test
     */
    @SuppressWarnings("ParameterNumber")
    protected Issue createIssue(final String pathName, final TreeString fileName,
            final int lineStart, final int lineEnd, final int columnStart, final int columnEnd,
            @Nullable final String category, @Nullable final String type,
            final TreeString packageName, @Nullable final String moduleName,
            @Nullable final Severity priority, final TreeString message,
            final String description, @Nullable final String origin,
            @Nullable final String reference, @Nullable final String fingerprint,
            final String additionalProperties) {
        return new Issue(pathName, fileName, lineStart, lineEnd, columnStart, columnEnd, LINE_RANGES, category, type,
                packageName,
                moduleName, priority, message, description, origin, reference, fingerprint, additionalProperties,
                UUID.randomUUID());
    }

    @Test
    void shouldSplitFileNameElements() {
        Issue issue = new Issue(PATH_NAME, FILE_NAME_TS, 2, 1, 2, 1, LINE_RANGES, CATEGORY,
                TYPE, PACKAGE_NAME_TS, MODULE_NAME, SEVERITY,
                MESSAGE_TS, DESCRIPTION, ORIGIN, REFERENCE, FINGERPRINT, ADDITIONAL_PROPERTIES, UUID.randomUUID());

        try (SoftAssertions softly = new SoftAssertions()) {
            softly.assertThat(issue)
                    .hasFileName(FILE_NAME)
                    .hasPath(PATH_NAME)
                    .hasAbsolutePath(PATH_NAME + "/" + FILE_NAME)
                    .hasBaseName(BASE_NAME);
        }

        TreeString newName = TreeString.valueOf("new.txt");
        issue.setFileName("/new", newName);
        try (SoftAssertions softly = new SoftAssertions()) {
            softly.assertThat(issue)
                    .hasFileName("new.txt")
                    .hasPath("/new")
                    .hasAbsolutePath("/new/new.txt")
                    .hasBaseName("new.txt");
        }

        Issue other = new Issue(PATH_NAME, newName, 2, 1, 2, 1, LINE_RANGES, CATEGORY,
                TYPE, PACKAGE_NAME_TS, MODULE_NAME, SEVERITY,
                MESSAGE_TS, DESCRIPTION, ORIGIN, REFERENCE, FINGERPRINT, ADDITIONAL_PROPERTIES, UUID.randomUUID());
        assertThat(issue).as("Equals should not consider pathName in computation").isEqualTo(other);

        Issue emptyPath = new Issue("", FILE_NAME_TS, 2, 1, 2, 1, LINE_RANGES, CATEGORY,
                TYPE, PACKAGE_NAME_TS, MODULE_NAME, SEVERITY,
                MESSAGE_TS, DESCRIPTION, ORIGIN, REFERENCE, FINGERPRINT, ADDITIONAL_PROPERTIES, UUID.randomUUID());

        try (SoftAssertions softly = new SoftAssertions()) {
            softly.assertThat(emptyPath)
                    .hasFileName(FILE_NAME)
                    .hasPath(UNDEFINED)
                    .hasAbsolutePath(FILE_NAME)
                    .hasBaseName(BASE_NAME);
        }
    }

    @Test
    void shouldConvertWindowsNames() {
        Issue issue = new Issue("C:\\Windows", FILE_NAME_TS, 2, 1, 2, 1, LINE_RANGES, CATEGORY,
                TYPE, PACKAGE_NAME_TS, MODULE_NAME, SEVERITY,
                MESSAGE_TS, DESCRIPTION, ORIGIN, REFERENCE, FINGERPRINT, ADDITIONAL_PROPERTIES, UUID.randomUUID());

        try (SoftAssertions softly = new SoftAssertions()) {
            softly.assertThat(issue)
                    .hasFileName(FILE_NAME)
                    .hasPath(WINDOWS_PATH)
                    .hasAbsolutePath(WINDOWS_PATH + "/" + FILE_NAME)
                    .hasBaseName(BASE_NAME);
        }

        issue.setFileName("c:\\Another\\Path", FILE_NAME_TS);
        assertThat(issue).hasPath("C:/Another/Path");
    }

    @Test
    void shouldEnsureThatEndIsGreaterOrEqualStart() {
        Issue issue = new Issue(PATH_NAME, FILE_NAME_TS, 2, 1, 2, 1, LINE_RANGES, CATEGORY,
                TYPE, PACKAGE_NAME_TS, MODULE_NAME, SEVERITY,
                MESSAGE_TS, DESCRIPTION, ORIGIN, REFERENCE, FINGERPRINT, ADDITIONAL_PROPERTIES, UUID.randomUUID());
        assertThat(issue).hasLineStart(1).hasLineEnd(2);
        assertThat(issue).hasColumnStart(1).hasColumnEnd(2);
    }

    /**
     * Creates an issue with all properties set to a specific value. Verifies that each getter returns the correct
     * result.
     */
    @Test
    void shouldSetAllPropertiesInConstructor() {
        Issue issue = createFilledIssue();

        try (SoftAssertions softly = new SoftAssertions()) {
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
                    .hasSeverity(SEVERITY)
                    .hasMessage(MESSAGE)
                    .hasOrigin(ORIGIN)
                    .hasDescription(DESCRIPTION)
                    .hasFingerprint(FINGERPRINT);
            softly.assertThat(issue.hasFingerprint()).isTrue();
            softly.assertThat(issue.hasPackageName()).isTrue();
            softly.assertThat(issue.hasFileName()).isTrue();
            softly.assertThat(issue.hasModuleName()).isTrue();
        }

        try (SoftAssertions softly = new SoftAssertions()) {
            softly.assertThat(Issue.getPropertyValueAsString(issue, "fileName"))
                    .isEqualTo(issue.getFileName());
            softly.assertThat(Issue.getPropertyValueAsString(issue, "category"))
                    .isEqualTo(issue.getCategory());
            softly.assertThat(Issue.getPropertyValueAsString(issue, "lineStart"))
                    .isEqualTo(String.valueOf(issue.getLineStart()));
            softly.assertThat(Issue.getPropertyValueAsString(issue, "severity"))
                    .isEqualTo(issue.getSeverity().toString());
        }
    }

    @Test
    void shouldChangeMutableProperties() {
        Issue issue = createFilledIssue();

        String origin = "new-origin";
        issue.setOrigin(origin);
        String reference = "new-reference";
        issue.setReference(reference);
        String moduleName = "new-module";
        issue.setModuleName(moduleName);
        String packageName = "new-package";
        issue.setPackageName(TREE_STRING_BUILDER.intern(packageName));
        TreeString fileName = TREE_STRING_BUILDER.intern("new-file");
        String pathName = "new-path";
        issue.setFileName(pathName, fileName);
        String fingerprint = "new-fingerprint";
        issue.setFingerprint(fingerprint);

        try (SoftAssertions softly = new SoftAssertions()) {
            softly.assertThat(issue)
                    .hasOrigin(origin)
                    .hasReference(reference)
                    .hasModuleName(moduleName)
                    .hasPackageName(packageName)
                    .hasFileName(fileName.toString())
                    .hasPath(pathName)
                    .hasAbsolutePath(pathName + "/" + fileName)
                    .hasFingerprint(fingerprint);
        }
    }

    @Test
    @SuppressWarnings("NullAway")
    void testDefaultIssueNullStringsNegativeIntegers() {
        Issue issue = createIssue(null, UNDEFINED_TS, 0, 0, 0, 0,
                null, null, UNDEFINED_TS, null,
                SEVERITY, EMPTY_TS, EMPTY, null, null, null, null);

        assertIsDefaultIssue(issue);
    }

    @Test
    void testDefaultIssueEmptyStringsNegativeIntegers() {
        Issue issue = createIssue(EMPTY, UNDEFINED_TS, -1, -1, -1, -1,
                EMPTY, EMPTY, UNDEFINED_TS, EMPTY, SEVERITY, EMPTY_TS, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY);

        assertIsDefaultIssue(issue);
    }

    private void assertIsDefaultIssue(final Issue issue) {
        try (SoftAssertions softly = new SoftAssertions()) {
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
            softly.assertThat(issue.hasFingerprint()).isFalse();
            softly.assertThat(issue.hasPackageName()).isFalse();
            softly.assertThat(issue.hasFileName()).isFalse();
            softly.assertThat(issue.hasModuleName()).isFalse();
        }
    }

    @Test
    void testZeroLineColumnEndsDefaultToLineColumnStarts() {
        Issue issue = createIssue(PATH_NAME, FILE_NAME_TS, LINE_START, 0, COLUMN_START, 0, CATEGORY, TYPE,
                PACKAGE_NAME_TS, MODULE_NAME, SEVERITY, MESSAGE_TS, DESCRIPTION, ORIGIN, REFERENCE, FINGERPRINT,
                ADDITIONAL_PROPERTIES);

        try (SoftAssertions softly = new SoftAssertions()) {
            softly.assertThat(issue)
                    .hasLineStart(LINE_START)
                    .hasLineEnd(LINE_START)
                    .hasColumnStart(COLUMN_START)
                    .hasColumnEnd(COLUMN_START);
        }
    }

    @Test
    void testNullPriorityDefaultsToNormal() {
        Issue issue = createIssue(PATH_NAME, FILE_NAME_TS, LINE_START, LINE_END, COLUMN_START, COLUMN_END, CATEGORY,
                TYPE,
                PACKAGE_NAME_TS, MODULE_NAME, null, MESSAGE_TS, DESCRIPTION, ORIGIN, REFERENCE, FINGERPRINT,
                ADDITIONAL_PROPERTIES);

        assertThat(issue.getSeverity()).isEqualTo(Severity.WARNING_NORMAL);
    }

    @Test
    void testIdRandomlyGenerated() {
        Issue one = createFilledIssue();
        Issue another = createFilledIssue();

        assertThat(one.getId()).isNotEqualTo(another.getId());
    }

    /**
     * Creates an issue that contains valid properties.
     *
     * @return a correctly filled issue
     */
    protected Issue createFilledIssue() {
        return createIssue(PATH_NAME, FILE_NAME_TS, LINE_START, LINE_END, COLUMN_START, COLUMN_END, CATEGORY, TYPE,
                PACKAGE_NAME_TS,
                MODULE_NAME, SEVERITY, MESSAGE_TS, DESCRIPTION, ORIGIN, REFERENCE, FINGERPRINT, ADDITIONAL_PROPERTIES);
    }

    @Test
    void testToString() {
        Issue issue = createFilledIssue();

        try (SoftAssertions softly = new SoftAssertions()) {
            softly.assertThat(issue.toString())
                    .contains(FILE_NAME)
                    .contains(Integer.toString(LINE_START))
                    .contains(Integer.toString(COLUMN_START))
                    .contains(CATEGORY)
                    .contains(TYPE)
                    .contains(MESSAGE);
        }
    }

    @Override
    protected Issue createSerializable() {
        return createFilledIssue();
    }

    /**
     * Verifies that saved serialized format (from a previous release) still can be resolved with the current
     * implementation of {@link Issue}.
     */
    @Test
    void shouldReadIssueFromOldSerialization() {
        byte[] restored = readAllBytes(SERIALIZATION_NAME);

        assertThatSerializableCanBeRestoredFrom(restored);
    }

    /**
     * Serializes an issue to a file. Use this method in case the issue properties have been changed and the readResolve
     * method has been adapted accordingly so that the old serialization still can be read.
     *
     * @param args
     *         not used
     *
     * @throws IOException
     *         if the file could not be written
     */
    public static void main(final String... args) throws IOException {
        new IssueTest().createSerializationFile();
    }
}
