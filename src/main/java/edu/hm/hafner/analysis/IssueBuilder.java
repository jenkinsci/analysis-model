package edu.hm.hafner.analysis;

import java.io.Serializable;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.util.PathUtil;
import edu.hm.hafner.util.TreeString;
import edu.hm.hafner.util.TreeStringBuilder;
import edu.umd.cs.findbugs.annotations.Nullable;

import static edu.hm.hafner.util.IntegerParser.*;

/**
 * Creates new {@link Issue issues} using the builder pattern. All properties that have not been set in the builder will
 * be set to their default value.
 * <p>Example:</p>
 * <blockquote><pre>
 * Issue issue = new IssueBuilder()
 *                      .setFileName("affected.file")
 *                      .setLineStart(0)
 *                      .setCategory("JavaDoc")
 *                      .setMessage("Missing JavaDoc")
 *                      .setSeverity(Severity.WARNING_LOW);
 * </pre></blockquote>
 *
 * @author Ullrich Hafner
 */
@SuppressWarnings({"InstanceVariableMayNotBeInitialized", "JavaDocMethod", "PMD.TooManyFields"})
public class IssueBuilder {
    private static final String EMPTY = StringUtils.EMPTY;
    private static final String UNDEFINED = "-";
    private static final TreeString UNDEFINED_TREE_STRING = TreeString.valueOf(UNDEFINED);
    private static final TreeString EMPTY_TREE_STRING = TreeString.valueOf(StringUtils.EMPTY);

    private final TreeStringBuilder fileNameBuilder = new TreeStringBuilder();
    private final TreeStringBuilder packageNameBuilder = new TreeStringBuilder();
    private final TreeStringBuilder messageBuilder = new TreeStringBuilder();

    private int lineStart = 0;
    private int lineEnd = 0;
    private int columnStart = 0;
    private int columnEnd = 0;

    @Nullable
    private LineRangeList lineRanges;

    @Nullable
    private String pathName;
    private TreeString fileName = UNDEFINED_TREE_STRING;
    private TreeString packageName = UNDEFINED_TREE_STRING;

    @Nullable
    private String directory;
    @Nullable
    private String category;
    @Nullable
    private String type;
    @Nullable
    private Severity severity;

    private TreeString message = EMPTY_TREE_STRING;
    private String description = EMPTY;

    @Nullable
    private String moduleName;
    @Nullable
    private String origin;
    @Nullable
    private String reference;
    @Nullable
    private String fingerprint;
    @Nullable
    private Serializable additionalProperties;

    private UUID id = UUID.randomUUID();

    /**
     * Sets the unique ID of the issue. If not set then an ID will be generated.
     *
     * @param id
     *         the ID
     *
     * @return this
     */
    public IssueBuilder setId(final UUID id) {
        this.id = id;
        return this;
    }

    /**
     * Sets additional properties from the statical analysis tool. This object could be used to store tool specific
     * information.
     *
     * @param additionalProperties
     *         the instance
     *
     * @return this
     */
    public IssueBuilder setAdditionalProperties(@Nullable final Serializable additionalProperties) {
        this.additionalProperties = additionalProperties;
        return this;
    }

    /**
     * Sets the fingerprint for this issue. Used to decide if two issues are equal even if the equals method returns
     * {@code false} since some of the properties differ due to code refactorings. The fingerprint is created by
     * analyzing the content of the affected file.
     *
     * @param fingerprint
     *         the fingerprint to set
     *
     * @return this
     */
    public IssueBuilder setFingerprint(@Nullable final String fingerprint) {
        this.fingerprint = fingerprint;
        return this;
    }

    /**
     * Sets the name of the affected file. This file name is a path relative to the path of the affected files (see
     * {@link #setPathName(String)}).
     *
     * @param fileName
     *         the file name
     *
     * @return this
     */
    public IssueBuilder setFileName(@Nullable final String fileName) {
        this.fileName = internFileName(fileName);

        return this;
    }

    TreeString internFileName(@Nullable final String unsafeFileName) {
        if (unsafeFileName == null || StringUtils.isEmpty(unsafeFileName)) {
            return UNDEFINED_TREE_STRING;
        }
        else {
            return fileNameBuilder.intern(normalizeFileName(
                    new PathUtil().createAbsolutePath(directory, unsafeFileName)));
        }
    }

    /**
     * Sets the current work directory. This directory is used as prefix for all subsequent issue file names. If the
     * path is set as well, then the final path of an issue is the concatenation of {@code path}, {@code directory}, and
     * {@code fileName}. Note that this directory is not visible later on, the issue does only store the path in the
     * {@code path} and {@code fileName} properties. I.e., the created issue will get a new file name that is composed
     * of {@code directory} and {@code fileName}.
     *
     * @param directory
     *         the directory that contains all affected files
     *
     * @return this
     */
    public IssueBuilder setDirectory(@Nullable final String directory) {
        this.directory = directory;
        return this;
    }

    /**
     * Sets the path of the affected file. Note that this path is not the parent folder of the affected file. This path
     * is the folder that contains all of the affected files of a {@link Report}. The path of an affected file is stored
     * in the {@code path} and {@code fileName} properties so that issues can be tracked even if the root folder changes
     * (due to different build environments).
     *
     * @param pathName
     *         the path that contains all affected files
     *
     * @return this
     */
    public IssueBuilder setPathName(@Nullable final String pathName) {
        this.pathName = pathName;
        return this;
    }

    /**
     * Sets the first line of this issue (lines start at 1; 0 indicates the whole file).
     *
     * @param lineStart
     *         the first line
     *
     * @return this
     */
    public IssueBuilder setLineStart(final int lineStart) {
        this.lineStart = lineStart;
        return this;
    }

    /**
     * Sets the first line of this issue (lines start at 1; 0 indicates the whole file).
     *
     * @param lineStart
     *         the first line
     *
     * @return this
     */
    public IssueBuilder setLineStart(@Nullable final String lineStart) {
        this.lineStart = parseInt(lineStart);
        return this;
    }

    /**
     * Sets the last line of this issue (lines start at 1).
     *
     * @param lineEnd
     *         the last line
     *
     * @return this
     */
    public IssueBuilder setLineEnd(final int lineEnd) {
        this.lineEnd = lineEnd;
        return this;
    }

    /**
     * Sets the last line of this issue (lines start at 1).
     *
     * @param lineEnd
     *         the last line
     *
     * @return this
     */
    public IssueBuilder setLineEnd(@Nullable final String lineEnd) {
        this.lineEnd = parseInt(lineEnd);
        return this;
    }

    /**
     * Sets the first column of this issue (columns start at 1, 0 indicates the whole line).
     *
     * @param columnStart
     *         the first column
     *
     * @return this
     */
    public IssueBuilder setColumnStart(final int columnStart) {
        this.columnStart = columnStart;
        return this;
    }

    /**
     * Sets the first column of this issue (columns start at 1, 0 indicates the whole line).
     *
     * @param columnStart
     *         the first column
     *
     * @return this
     */
    public IssueBuilder setColumnStart(@Nullable final String columnStart) {
        this.columnStart = parseInt(columnStart);
        return this;
    }

    /**
     * Sets the the last column of this issue (columns start at 1).
     *
     * @param columnEnd
     *         the last column
     *
     * @return this
     */
    public IssueBuilder setColumnEnd(final int columnEnd) {
        this.columnEnd = columnEnd;
        return this;
    }

    /**
     * Sets the the last column of this issue (columns start at 1).
     *
     * @param columnEnd
     *         the last column
     *
     * @return this
     */
    public IssueBuilder setColumnEnd(@Nullable final String columnEnd) {
        this.columnEnd = parseInt(columnEnd);
        return this;
    }

    /**
     * Sets the category of this issue (depends on the available categories of the static analysis tool). Examples for
     * categories are "Deprecation", "Design", or "JavaDoc".
     *
     * @param category
     *         the category
     *
     * @return this
     */
    public IssueBuilder setCategory(@Nullable final String category) {
        this.category = category;
        return this;
    }

    /**
     * Sets the type of this issue (depends on the available types of the static analysis tool). The type typically is
     * the associated rule of the static analysis tool that reported this issue.
     *
     * @param type
     *         the type
     *
     * @return this
     */
    public IssueBuilder setType(@Nullable final String type) {
        this.type = type;
        return this;
    }

    /**
     * Sets the name of the package or name space (or similar concept) that contains this issue.
     *
     * @param packageName
     *         the package or namespace name
     *
     * @return this
     */
    public IssueBuilder setPackageName(@Nullable final String packageName) {
        this.packageName = internPackageName(packageName);

        return this;
    }

    TreeString internPackageName(@Nullable final String unsafePackageName) {
        if (unsafePackageName == null || StringUtils.isBlank(unsafePackageName)) {
            return UNDEFINED_TREE_STRING;
        }
        else {
            return packageNameBuilder.intern(unsafePackageName);
        }
    }

    /**
     * Sets the name of the module or project (or similar concept) that contains this issue.
     *
     * @param moduleName
     *         the module name
     *
     * @return this
     */
    public IssueBuilder setModuleName(@Nullable final String moduleName) {
        this.moduleName = moduleName;
        return this;
    }

    /**
     * Sets the ID of the tool that did report this issue.
     *
     * @param origin
     *         the ID of the originating tool
     *
     * @return this
     */
    public IssueBuilder setOrigin(@Nullable final String origin) {
        this.origin = origin;
        return this;
    }

    /**
     * Sets a reference to the execution of the static analysis tool (build ID, timestamp, etc.).
     *
     * @param reference
     *         the reference
     *
     * @return this
     */
    public IssueBuilder setReference(@Nullable final String reference) {
        this.reference = reference;
        return this;
    }

    /**
     * Sets the severity of this issue.
     *
     * @param severity
     *         the severity
     *
     * @return this
     */
    public IssueBuilder setSeverity(@Nullable final Severity severity) {
        this.severity = severity;
        return this;
    }

    /**
     * Guesses a severity for the issues: converts a String severity to one of the predefined severities. If the
     * provided String does not match (even partly) then the default severity will be returned.
     *
     * @param severityString
     *         the severity given as a string representation
     *
     * @return this
     */
    public IssueBuilder guessSeverity(@Nullable final String severityString) {
        severity = Severity.guessFromString(severityString);
        return this;
    }

    /**
     * Sets the detailed message for this issue.
     *
     * @param message
     *         the message
     *
     * @return this
     */
    public IssueBuilder setMessage(@Nullable final String message) {
        if (StringUtils.isBlank(message)) {
            this.message = EMPTY_TREE_STRING;
        }
        else {
            this.message = messageBuilder.intern(StringUtils.stripToEmpty(message));
        }
        return this;
    }

    /**
     * Sets  an additional description for this issue. Static analysis tools might provide some additional information
     * about this issue. This description may contain valid HTML.
     *
     * @param description
     *         the description (as HTML content)
     *
     * @return this
     */
    public IssueBuilder setDescription(@Nullable final String description) {
        this.description = StringUtils.stripToEmpty(description);
        return this;
    }

    /**
     * Sets additional line ranges for this issue. Not that the primary range given by {@code lineStart} and {@code *
     * lineEnd} is not included.
     *
     * @param lineRanges
     *         the additional line ranges
     *
     * @return this
     */
    public IssueBuilder setLineRanges(final LineRangeList lineRanges) {
        this.lineRanges = new LineRangeList(lineRanges);
        return this;
    }

    /**
     * Initializes this builder with an exact copy of all properties of the specified issue.
     *
     * @param copy
     *         the issue to copy the properties from
     *
     * @return the initialized builder
     */
    public IssueBuilder copy(final Issue copy) {
        fileName = copy.getFileNameTreeString();
        lineStart = copy.getLineStart();
        lineEnd = copy.getLineEnd();
        columnStart = copy.getColumnStart();
        columnEnd = copy.getColumnEnd();
        lineRanges = new LineRangeList();
        lineRanges.addAll(copy.getLineRanges());
        category = copy.getCategory();
        type = copy.getType();
        severity = copy.getSeverity();
        message = copy.getMessageTreeString();
        description = copy.getDescription();
        packageName = copy.getPackageNameTreeString();
        moduleName = copy.getModuleName();
        origin = copy.getOrigin();
        reference = copy.getReference();
        fingerprint = copy.getFingerprint();
        additionalProperties = copy.getAdditionalProperties();
        return this;
    }

    /**
     * Creates a new {@link Issue} based on the specified properties.
     *
     * @return the created issue
     */
    public Issue build() {
        Issue issue = new Issue(pathName, fileName, lineStart, lineEnd, columnStart, columnEnd, lineRanges,
                category, type, packageName, moduleName, severity,
                message, description, origin, reference, fingerprint,
                additionalProperties, id);
        id = UUID.randomUUID(); // make sure that multiple invocations will create different IDs
        return issue;
    }

    private static String normalizeFileName(@Nullable final String platformFileName) {
        return defaultString(StringUtils.replace(
                StringUtils.strip(platformFileName), "\\", "/"));
    }

    /**
     * Creates a default String representation for undefined input parameters.
     *
     * @param string
     *         the string to check
     *
     * @return the valid string or a default string if the specified string is not valid
     */
    private static String defaultString(@Nullable final String string) {
        return StringUtils.defaultIfEmpty(string, UNDEFINED).intern();
    }

    /**
     * Creates a new {@link Issue} based on the specified properties. The returned issue is wrapped in an {@link
     * Optional}.
     *
     * @return the created issue
     */
    public Optional<Issue> buildOptional() {
        return Optional.of(build());
    }
}
