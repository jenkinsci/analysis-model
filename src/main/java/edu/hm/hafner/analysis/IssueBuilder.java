package edu.hm.hafner.analysis;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;

import com.google.errorprone.annotations.CanIgnoreReturnValue;

import edu.hm.hafner.util.LineRangeList;
import edu.hm.hafner.util.PathUtil;
import edu.hm.hafner.util.TreeString;
import edu.hm.hafner.util.TreeStringBuilder;
import edu.umd.cs.findbugs.annotations.CheckForNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static edu.hm.hafner.analysis.util.IntegerParser.*;

/**
 * Creates new {@link Issue issues} using the builder pattern. All properties that have not been set in the builder will
 * be set to their default value.
 *
 * <p>Example:</p>
 *
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
@SuppressWarnings({"InstanceVariableMayNotBeInitialized", "JavaDocMethod", "PMD.TooManyFields", "PMD.GodClass"})
public class IssueBuilder implements AutoCloseable {
    private static final String EMPTY = StringUtils.EMPTY;
    private static final String UNDEFINED = "-";
    private static final TreeString UNDEFINED_TREE_STRING = TreeString.valueOf(UNDEFINED);
    private static final TreeString EMPTY_TREE_STRING = TreeString.valueOf(StringUtils.EMPTY);
    private static final PathUtil PATH_UTIL = new PathUtil();

    private final TreeStringBuilder fileNameBuilder = new TreeStringBuilder();
    private final TreeStringBuilder packageNameBuilder = new TreeStringBuilder();
    private final TreeStringBuilder messageBuilder = new TreeStringBuilder();

    private int lineStart;
    private int lineEnd;
    private int columnStart;
    private int columnEnd;

    @CheckForNull
    private LineRangeList lineRanges;

    @CheckForNull
    private List<FileLocation> additionalFileLocations;

    @CheckForNull
    private String pathName;
    private TreeString fileName = UNDEFINED_TREE_STRING;
    private TreeString packageName = UNDEFINED_TREE_STRING;

    @CheckForNull
    private String directory;
    @CheckForNull
    private String category;
    @CheckForNull
    private String type;
    @CheckForNull
    private Severity severity;

    private TreeString message = EMPTY_TREE_STRING;
    private String description = EMPTY;

    @CheckForNull
    private String moduleName;
    @CheckForNull
    private String origin;
    @CheckForNull
    private String originName;
    @CheckForNull
    private String reference;
    @CheckForNull
    private String fingerprint;
    @CheckForNull
    private Serializable additionalProperties;

    private UUID id = UUID.randomUUID();

    /**
     * Sets the unique ID of the issue. If not set, then an ID will be generated.
     *
     * @param id
     *         the ID
     *
     * @return this
     */
    @CanIgnoreReturnValue
    public IssueBuilder setId(final UUID id) {
        this.id = id;
        return this;
    }

    /**
     * Sets additional properties from the statical analysis tool. This object could be used to store tool-specific
     * information.
     *
     * @param additionalProperties
     *         the instance
     *
     * @return this
     */
    @CanIgnoreReturnValue
    public IssueBuilder setAdditionalProperties(@CheckForNull final Serializable additionalProperties) {
        this.additionalProperties = additionalProperties;
        return this;
    }

    /**
     * Sets the fingerprint for this issue. Used to decide if two issues are equal even if the equals method returns
     * {@code false} since some properties differ due to code refactorings. The fingerprint is created by
     * analyzing the content of the affected file.
     *
     * @param fingerprint
     *         the fingerprint to set
     *
     * @return this
     */
    @CanIgnoreReturnValue
    public IssueBuilder setFingerprint(@CheckForNull final String fingerprint) {
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
    @CanIgnoreReturnValue
    public IssueBuilder setFileName(@CheckForNull final String fileName) {
        this.fileName = internFileName(fileName);

        return this;
    }

    TreeString internFileName(@CheckForNull final String unsafeFileName) {
        if (unsafeFileName == null || StringUtils.isEmpty(unsafeFileName)) {
            return UNDEFINED_TREE_STRING;
        }
        else {
            if (directory != null && PATH_UTIL.isAbsolute(normalizeFileName(unsafeFileName))) {
                return fileNameBuilder.intern(normalizeFileName(unsafeFileName));
            }
            return fileNameBuilder.intern(normalizeFileName(
                    PATH_UTIL.createAbsolutePath(directory, unsafeFileName)));
        }
    }

    /**
     * Sets the current work directory. This directory is used as a prefix for all subsequent issue file names. If the
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
    @CanIgnoreReturnValue
    public IssueBuilder setDirectory(@CheckForNull final String directory) {
        this.directory = directory;
        return this;
    }

    /**
     * Sets the path of the affected file. Note that this path is not the parent folder of the affected file. This path
     * is the folder that contains all the affected files of a {@link Report}. The path of an affected file is stored
     * in the {@code path} and {@code fileName} properties so that issues can be tracked even if the root folder changes
     * (due to different build environments).
     *
     * @param pathName
     *         the path that contains all affected files
     *
     * @return this
     */
    @CanIgnoreReturnValue
    public IssueBuilder setPathName(@CheckForNull final String pathName) {
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
    @CanIgnoreReturnValue
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
    @CanIgnoreReturnValue
    public IssueBuilder setLineStart(@CheckForNull final String lineStart) {
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
    @CanIgnoreReturnValue
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
    @CanIgnoreReturnValue
    public IssueBuilder setLineEnd(@CheckForNull final String lineEnd) {
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
    @CanIgnoreReturnValue
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
    @CanIgnoreReturnValue
    public IssueBuilder setColumnStart(@CheckForNull final String columnStart) {
        this.columnStart = parseInt(columnStart);
        return this;
    }

    /**
     * Sets the last column of this issue (columns start at 1).
     *
     * @param columnEnd
     *         the last column
     *
     * @return this
     */
    @CanIgnoreReturnValue
    public IssueBuilder setColumnEnd(final int columnEnd) {
        this.columnEnd = columnEnd;
        return this;
    }

    /**
     * Sets the last column of this issue (columns start at 1).
     *
     * @param columnEnd
     *         the last column
     *
     * @return this
     */
    @CanIgnoreReturnValue
    public IssueBuilder setColumnEnd(@CheckForNull final String columnEnd) {
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
    @CanIgnoreReturnValue
    public IssueBuilder setCategory(@CheckForNull final String category) {
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
    @CanIgnoreReturnValue
    public IssueBuilder setType(@CheckForNull final String type) {
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
    @CanIgnoreReturnValue
    public IssueBuilder setPackageName(@CheckForNull final String packageName) {
        this.packageName = internPackageName(packageName);

        return this;
    }

    TreeString internPackageName(@CheckForNull final String unsafePackageName) {
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
    @CanIgnoreReturnValue
    public IssueBuilder setModuleName(@CheckForNull final String moduleName) {
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
    @CanIgnoreReturnValue
    public IssueBuilder setOrigin(@CheckForNull final String origin) {
        this.origin = origin;
        return this;
    }

    /**
     * Sets the name of the tool that did report this issue.
     *
     * @param originName
     *         the ID of the originating tool
     *
     * @return this
     */
    @CanIgnoreReturnValue
    public IssueBuilder setOriginName(@CheckForNull final String originName) {
        this.originName = originName;
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
    @CanIgnoreReturnValue
    public IssueBuilder setReference(@CheckForNull final String reference) {
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
    @CanIgnoreReturnValue
    public IssueBuilder setSeverity(@CheckForNull final Severity severity) {
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
    @CanIgnoreReturnValue
    public IssueBuilder guessSeverity(@CheckForNull final String severityString) {
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
    @CanIgnoreReturnValue
    public IssueBuilder setMessage(@CheckForNull final String message) {
        if (StringUtils.isBlank(message)) {
            this.message = EMPTY_TREE_STRING;
        }
        else {
            this.message = messageBuilder.intern(StringUtils.stripToEmpty(message));
        }
        return this;
    }

    /**
     * Sets an additional description for this issue. Static analysis tools might provide some additional information
     * about this issue. This description may contain valid HTML.
     *
     * @param description
     *         the description (as HTML content)
     *
     * @return this
     */
    @CanIgnoreReturnValue
    public IssueBuilder setDescription(@CheckForNull final String description) {
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
    @CanIgnoreReturnValue
    public IssueBuilder setLineRanges(final LineRangeList lineRanges) {
        this.lineRanges = new LineRangeList(lineRanges);
        return this;
    }

    /**
     * Sets additional file locations for this issue. Some warnings span multiple files, such as GNU CC's reorder
     * warning for C++ where the warning shows up in the initializer list but references the header file.
     *
     * @param additionalFileLocations
     *         the additional file locations
     *
     * @return this
     */
    @CanIgnoreReturnValue
    public IssueBuilder setAdditionalFileLocations(final List<FileLocation> additionalFileLocations) {
        this.additionalFileLocations = new ArrayList<>(additionalFileLocations);
        return this;
    }

    /**
     * Adds an additional file location to this issue.
     *
     * @param fileLocation
     *         the file location to add
     *
     * @return this
     */
    @CanIgnoreReturnValue
    public IssueBuilder addAdditionalFileLocation(final FileLocation fileLocation) {
        if (this.additionalFileLocations == null) {
            this.additionalFileLocations = new ArrayList<>();
        }
        this.additionalFileLocations.add(fileLocation);
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
        additionalFileLocations = new ArrayList<>(copy.getAdditionalFileLocations());
        category = copy.getCategory();
        type = copy.getType();
        severity = copy.getSeverity();
        message = copy.getMessageTreeString();
        description = copy.getDescription();
        packageName = copy.getPackageNameTreeString();
        moduleName = copy.getModuleName();
        origin = copy.getOrigin();
        originName = copy.getOriginName();
        reference = copy.getReference();
        fingerprint = copy.getFingerprint();
        additionalProperties = copy.getAdditionalProperties();
        return this;
    }

    /**
     * Creates a new {@link Issue} based on the specified properties. After building the issue, this
     * {@link IssueBuilder} creates a new ID, but all other {@link Issue} properties will remain unchanged.
     *
     * @return the created issue
     */
    public Issue build() {
        var issue = buildWithConstructor();
        id = UUID.randomUUID(); // make sure that multiple invocations will create different IDs
        return issue;
    }

    /**
     * Creates a new {@link Issue} based on the specified properties. After building the issue, this
     * {@link IssueBuilder} will be reset to its defaults.
     *
     * @return the created issue
     */
    public Issue buildAndClean() {
        var issue = buildWithConstructor();
        clean();
        return issue;
    }

    private Issue buildWithConstructor() {
        cleanupLineRanges();

        return new Issue(pathName, fileName, lineStart, lineEnd, columnStart, columnEnd, lineRanges,
                additionalFileLocations, category, type, packageName, moduleName, severity, message, description,
                origin, originName, reference, fingerprint, additionalProperties, id);
    }

    /**
     * Sets lineStart and lineEnd if they are not set, and then removes the first element of
     * lineRanges if its start and end are the same as lineStart and lineEnd.
     */
    private void cleanupLineRanges() {
        if (lineRanges != null && !lineRanges.isEmpty()) {
            var firstRange = lineRanges.get(0);
            if (lineStart == 0) {
                this.lineStart = firstRange.getStart();
                this.lineEnd = firstRange.getEnd();
            }
            if (firstRange.getStart() == lineStart
                    && firstRange.getEnd() == lineEnd) {
                lineRanges.remove(0);
            }
        }
    }

    /**
     * Creates a new {@link Issue} based on the specified properties. The returned issue is wrapped in an {@link
     * Optional}. After building the issue, this {@link IssueBuilder} will be reset to its defaults.
     *
     * @return the created issue
     * @see #buildAndClean()
     */
    public Optional<Issue> buildOptional() {
        return Optional.of(buildAndClean());
    }

    @SuppressWarnings("PMD.NullAssignment")
    private void clean() {
        id = UUID.randomUUID(); // make sure that multiple invocations will create different IDs

        lineStart = 0;
        lineEnd = 0;
        columnStart = 0;
        columnEnd = 0;

        lineRanges = new LineRangeList();
        additionalFileLocations = null;

        fileName = UNDEFINED_TREE_STRING;
        packageName = UNDEFINED_TREE_STRING;

        category = null;
        type = null;
        severity = null;

        message = EMPTY_TREE_STRING;
        description = EMPTY;

        moduleName = null;
        additionalProperties = null;
    }

    private static String normalizeFileName(@CheckForNull final String platformFileName) {
        return defaultString(Strings.CS.replace(
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
    private static String defaultString(@CheckForNull final String string) {
        return StringUtils.defaultIfEmpty(string, UNDEFINED).intern();
    }

    /**
     * Reduce the memory print of internal string instances.
     */
    @Override
    public void close() {
        fileNameBuilder.dedup();
        packageNameBuilder.dedup();
        messageBuilder.dedup();
    }
}
