package edu.hm.hafner.analysis;

import java.io.IOException;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import static edu.hm.hafner.analysis.assertj.Assertions.*;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;
import static java.util.Collections.*;

import edu.hm.hafner.util.SerializableTest;
import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Unit tests for {@link Issue}.
 *
 * @author Marcel Binder
 */
public class IssueTest extends SerializableTest<Issue> {
    private static final String SERIALIZATION_NAME = "issue.ser";

    static final String FILE_NAME = "C:/users/tester/file-name";
    static final String FILE_NAME_WITH_BACKSLASHES = "C:\\users\\tester/file-name";

    static final int LINE_START = 1;
    static final int LINE_END = 2;
    static final int COLUMN_START = 3;
    static final int COLUMN_END = 4;
    static final String CATEGORY = "category";
    static final String TYPE = "type";
    static final String PACKAGE_NAME = "package-name";
    static final String MODULE_NAME = "module-name";
    static final Severity SEVERITY = Severity.WARNING_HIGH;
    static final String MESSAGE = "message";
    static final String MESSAGE_NOT_STRIPPED = "    message  ";
    static final String DESCRIPTION = "description";
    static final String DESCRIPTION_NOT_STRIPPED = "    description  ";
    static final String EMPTY = "";
    static final String UNDEFINED = "-";
    static final String FINGERPRINT = "fingerprint";
    static final String ORIGIN = "origin";
    static final String REFERENCE = "reference";
    static final String ADDITIONAL_PROPERTIES = "additional";
    static final LineRangeList LINE_RANGES = new LineRangeList(singletonList(new LineRange(5, 6)));

    /**
     * Creates a new subject under test (i.e. a sub-type of {@link Issue}) using the specified properties.
     *
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
    protected Issue createIssue(@CheckForNull final String fileName,
            final int lineStart, final int lineEnd, final int columnStart, final int columnEnd,
            @CheckForNull final String category, @CheckForNull final String type,
            @CheckForNull final String packageName, @CheckForNull final String moduleName,
            @CheckForNull final Severity priority, @CheckForNull final String message,
            @CheckForNull final String description, @CheckForNull final String origin,
            @CheckForNull final String reference, @CheckForNull final String fingerprint,
            final String additionalProperties) {
        return new Issue(fileName, lineStart, lineEnd, columnStart, columnEnd, LINE_RANGES, category, type, packageName,
                moduleName, priority, message, description, origin, reference, fingerprint, additionalProperties,
                UUID.randomUUID());
    }

    @Test
    void shouldEnsureThatEndIsGreaterOrEqualStart() {
        Issue issue = new Issue(FILE_NAME, 2, 1, 2, 1, LINE_RANGES, CATEGORY,
                TYPE, PACKAGE_NAME, MODULE_NAME, SEVERITY,
                MESSAGE, DESCRIPTION, ORIGIN, REFERENCE, FINGERPRINT, ADDITIONAL_PROPERTIES, UUID.randomUUID());
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

        assertSoftly(softly -> {
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
            softly.assertThat(issue.hasModuleName()).isTrue();
        });

        assertSoftly(softly -> {
            softly.assertThat(Issue.getPropertyValueAsString(issue, "fileName"))
                    .isEqualTo(issue.getFileName());
            softly.assertThat(Issue.getPropertyValueAsString(issue, "category"))
                    .isEqualTo(issue.getCategory());
            softly.assertThat(Issue.getPropertyValueAsString(issue, "lineStart"))
                    .isEqualTo(String.valueOf(issue.getLineStart()));
            softly.assertThat(Issue.getPropertyValueAsString(issue, "severity"))
                    .isEqualTo(issue.getSeverity().toString());
        });
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
        issue.setPackageName(packageName);
        String fileName = "new-file";
        issue.setFileName(fileName);
        String fingerprint = "new-fingerprint";
        issue.setFingerprint(fingerprint);

        assertSoftly(softly -> {
            softly.assertThat(issue)
                    .hasOrigin(origin)
                    .hasReference(reference)
                    .hasModuleName(moduleName)
                    .hasPackageName(packageName)
                    .hasFileName(fileName)
                    .hasFingerprint(fingerprint);
        });
    }

    @Test
    void testDefaultIssueNullStringsNegativeIntegers() {
        Issue issue = createIssue(null, 0, 0, 0, 0,
                null, null, null, null,
                SEVERITY, null, null, null, null, null, null);

        assertIsDefaultIssue(issue);
    }

    @Test
    void testDefaultIssueEmptyStringsNegativeIntegers() {
        Issue issue = createIssue(EMPTY, -1, -1, -1, -1,
                EMPTY, EMPTY, EMPTY, EMPTY, SEVERITY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY);

        assertIsDefaultIssue(issue);
    }

    private void assertIsDefaultIssue(final Issue issue) {
        assertSoftly(softly -> {
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
            softly.assertThat(issue.hasModuleName()).isFalse();
        });
    }

    @Test
    void testZeroLineColumnEndsDefaultToLineColumnStarts() {
        Issue issue = createIssue(FILE_NAME, LINE_START, 0, COLUMN_START, 0, CATEGORY, TYPE,
                PACKAGE_NAME, MODULE_NAME, SEVERITY, MESSAGE, DESCRIPTION, ORIGIN, REFERENCE, FINGERPRINT,
                ADDITIONAL_PROPERTIES);

        assertSoftly(softly -> {
            softly.assertThat(issue)
                    .hasLineStart(LINE_START)
                    .hasLineEnd(LINE_START)
                    .hasColumnStart(COLUMN_START)
                    .hasColumnEnd(COLUMN_START);
        });
    }

    @Test
    void testNullPriorityDefaultsToNormal() {
        Issue issue = createIssue(FILE_NAME, LINE_START, LINE_END, COLUMN_START, COLUMN_END, CATEGORY, TYPE,
                PACKAGE_NAME, MODULE_NAME, null, MESSAGE, DESCRIPTION, ORIGIN, REFERENCE, FINGERPRINT,
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
        return createIssue(FILE_NAME, LINE_START, LINE_END, COLUMN_START, COLUMN_END, CATEGORY, TYPE, PACKAGE_NAME,
                MODULE_NAME, SEVERITY, MESSAGE, DESCRIPTION, ORIGIN, REFERENCE, FINGERPRINT, ADDITIONAL_PROPERTIES);
    }

    @Test
    void testToString() {
        Issue issue = createFilledIssue();

        assertSoftly(softly -> {
            softly.assertThat(issue.toString())
                    .contains(FILE_NAME)
                    .contains(Integer.toString(LINE_START))
                    .contains(Integer.toString(COLUMN_START))
                    .contains(CATEGORY)
                    .contains(TYPE)
                    .contains(MESSAGE);
        });
    }

    @Test
    void testFileNameBackslashConversion() {
        Issue issue = createIssue(FILE_NAME_WITH_BACKSLASHES, LINE_START, LINE_END, COLUMN_START, COLUMN_END, CATEGORY,
                TYPE, PACKAGE_NAME, MODULE_NAME, SEVERITY, MESSAGE, DESCRIPTION, ORIGIN, REFERENCE, FINGERPRINT,
                ADDITIONAL_PROPERTIES);

        assertThat(issue).hasFileName(FILE_NAME);
    }

    @Test
    void testMessageDescriptionStripped() {
        Issue issue = createIssue(FILE_NAME_WITH_BACKSLASHES, LINE_START, LINE_END, COLUMN_START, COLUMN_END, CATEGORY,
                TYPE, PACKAGE_NAME, MODULE_NAME, SEVERITY, MESSAGE_NOT_STRIPPED, DESCRIPTION_NOT_STRIPPED, ORIGIN,
                REFERENCE, FINGERPRINT, ADDITIONAL_PROPERTIES);

        assertSoftly(softly -> {
            softly.assertThat(issue)
                    .hasMessage(MESSAGE)
                    .hasDescription(DESCRIPTION);
        });
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