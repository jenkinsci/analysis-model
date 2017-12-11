package edu.hm.hafner.analysis;

import javax.annotation.CheckForNull;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.junit.jupiter.api.Test;

import static edu.hm.hafner.analysis.assertj.Assertions.*;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;
import edu.hm.hafner.util.SerializableTest;

/**
 * Unit tests for {@link Issue}.
 *
 * @author Marcel Binder
 */
public class IssueTest extends SerializableTest {
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
    static final Priority PRIORITY = Priority.HIGH;
    static final String MESSAGE = "message";
    static final String MESSAGE_NOT_STRIPPED = "    message  ";
    static final String DESCRIPTION = "description";
    static final String DESCRIPTION_NOT_STRIPPED = "    description  ";
    static final String EMPTY = "";
    static final String UNDEFINED = "-";
    static final String FINGERPRINT = "fingerprint";
    static final String ORIGIN = "origin";

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
     * @param fingerprint
     *         the finger print for this issue
     *
     * @return the subject under test
     */
    @SuppressWarnings("ParameterNumber")
    protected Issue createIssue(@CheckForNull final String fileName,
            final int lineStart, final int lineEnd, final int columnStart, final int columnEnd,
            @CheckForNull final String category, @CheckForNull final String type,
            @CheckForNull final String packageName, @CheckForNull final String moduleName,
            @CheckForNull final Priority priority,
            @CheckForNull final String message, @CheckForNull final String description,
            @CheckForNull final String origin, @CheckForNull final String fingerprint) {
        return new Issue(fileName, lineStart, lineEnd, columnStart, columnEnd, category, type, packageName,
                moduleName, priority, message, description, origin, fingerprint);
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
                    .hasPriority(PRIORITY)
                    .hasMessage(MESSAGE)
                    .hasOrigin(ORIGIN)
                    .hasDescription(DESCRIPTION)
                    .hasFingerprint(FINGERPRINT);
        });
    }

    @Test
    void testDefaultIssueNullStringsNegativeIntegers() {
        Issue issue = createIssue(null, 0, 0, 0, 0,
                null, null, null, null,
                PRIORITY, null, null, null, null);

        assertIsDefaultIssue(issue);
    }

    @Test
    void testDefaultIssueEmptyStringsNegativeIntegers() {
        Issue issue = createIssue(EMPTY, -1, -1, -1, -1, EMPTY, EMPTY, EMPTY, EMPTY, PRIORITY, EMPTY, EMPTY, EMPTY,
                EMPTY);

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
        });
    }

    @Test
    void testZeroLineColumnEndsDefaultToLineColumnStarts() {
        Issue issue = createIssue(FILE_NAME, LINE_START, 0, COLUMN_START, 0, CATEGORY, TYPE, PACKAGE_NAME, MODULE_NAME,
                PRIORITY, MESSAGE, DESCRIPTION, ORIGIN, FINGERPRINT);

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
                PACKAGE_NAME, MODULE_NAME, null, MESSAGE, DESCRIPTION, ORIGIN, FINGERPRINT);

        assertThat(issue.getPriority()).isEqualTo(Priority.NORMAL);
    }

    @Test
    void testIdRandomlyGenerated() {
        Issue one = createFilledIssue();
        Issue another = createFilledIssue();

        assertThat(one.getId()).isNotEqualTo(another.getId());
    }

    protected Issue createFilledIssue() {
        return createIssue(FILE_NAME, LINE_START, LINE_END, COLUMN_START, COLUMN_END, CATEGORY, TYPE, PACKAGE_NAME,
                MODULE_NAME, PRIORITY, MESSAGE, DESCRIPTION, ORIGIN, FINGERPRINT);
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
                TYPE, PACKAGE_NAME, MODULE_NAME, PRIORITY, MESSAGE, DESCRIPTION, ORIGIN, FINGERPRINT);

        assertThat(issue).hasFileName(FILE_NAME);
    }

    @Test
    void testMessageDescriptionStripped() {
        Issue issue = createIssue(FILE_NAME_WITH_BACKSLASHES, LINE_START, LINE_END, COLUMN_START, COLUMN_END, CATEGORY,
                TYPE, PACKAGE_NAME, MODULE_NAME, PRIORITY, MESSAGE_NOT_STRIPPED, DESCRIPTION_NOT_STRIPPED, ORIGIN,
                FINGERPRINT);

        assertSoftly(softly -> {
            softly.assertThat(issue)
                    .hasMessage(MESSAGE)
                    .hasDescription(DESCRIPTION);
        });
    }

    /**
     * Ensures that an issue instance can be serialized and deserialized.
     */
    @Test
    void shouldBeSerializable() {
        Issue createdWithConstructor = createFilledIssue();

        byte[] bytes = toByteArray(createdWithConstructor);

        assertThatIssueCanBeRestoredFrom(bytes);
    }

    /**
     * Verifies that saved serialized format (from a previous release) still can be resolved with the current
     * implementation of {@link Issue}.
     */
    @Test
    void shouldReadIssueFromOldSerialization() {
        byte[] restored = readResource(SERIALIZATION_NAME);

        assertThatIssueCanBeRestoredFrom(restored);
    }

    private void assertThatIssueCanBeRestoredFrom(final byte[] restored) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(restored))) {
            Object issue = inputStream.readObject();

            assertThat(issue).isInstanceOf(Issue.class);
            assertThat(issue).isEqualTo(createFilledIssue());
        }
        catch (IOException | ClassNotFoundException e) {
            throw new AssertionError("Can' resolve issue from byte array", e);
        }
    }

    /**
     * Serializes an issues to a file. Use this method in case the issue properties have been changed and the
     * readResolve method has been adapted accordingly so that the old serialization still can be read.
     *
     * @param args
     *         not used
     *
     * @throws IOException
     *         if the file could not be written
     */
    public static void useIfSerializationChanges(final String... args) throws IOException {
        new IssueTest().createSerialization();
    }

    /**
     * Serializes an issue using an {@link ObjectOutputStream } to the file /tmp/issue.ser.
     *
     * @throws IOException
     *         if the file could not be created
     */
    protected void createSerialization() throws IOException {
        Files.write(Paths.get("/tmp/issue.ser"), toByteArray(createFilledIssue()), StandardOpenOption.CREATE_NEW);
    }
}