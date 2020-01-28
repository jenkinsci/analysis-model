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

    public IssueBuilder setId(final UUID id) {
        this.id = id;
        return this;
    }

    public IssueBuilder setAdditionalProperties(@Nullable final Serializable additionalProperties) {
        this.additionalProperties = additionalProperties;
        return this;
    }

    public IssueBuilder setFingerprint(@Nullable final String fingerprint) {
        this.fingerprint = fingerprint;
        return this;
    }

    public IssueBuilder setFileName(@Nullable final String fileName) {
        this.fileName = internFileName(fileName);

        return this;
    }

    TreeString internFileName(@Nullable final String unsafeFileName) {
        if (StringUtils.isEmpty(unsafeFileName)) {
            return UNDEFINED_TREE_STRING;
        }
        else {
            return fileNameBuilder.intern(normalizeFileName(
                    new PathUtil().createAbsolutePath(directory, unsafeFileName)));
        }
    }

    public IssueBuilder setDirectory(@Nullable final String directory) {
        this.directory = directory;
        return this;
    }

    public IssueBuilder setLineStart(final int lineStart) {
        this.lineStart = lineStart;
        return this;
    }

    public IssueBuilder setLineStart(@Nullable final String lineStart) {
        this.lineStart = parseInt(lineStart);
        return this;
    }

    public IssueBuilder setLineEnd(final int lineEnd) {
        this.lineEnd = lineEnd;
        return this;
    }

    public IssueBuilder setLineEnd(@Nullable final String lineEnd) {
        this.lineEnd = parseInt(lineEnd);
        return this;
    }

    public IssueBuilder setColumnStart(final int columnStart) {
        this.columnStart = columnStart;
        return this;
    }

    public IssueBuilder setColumnStart(@Nullable final String columnStart) {
        this.columnStart = parseInt(columnStart);
        return this;
    }

    public IssueBuilder setColumnEnd(final int columnEnd) {
        this.columnEnd = columnEnd;
        return this;
    }

    public IssueBuilder setColumnEnd(@Nullable final String columnEnd) {
        this.columnEnd = parseInt(columnEnd);
        return this;
    }

    public IssueBuilder setCategory(@Nullable final String category) {
        this.category = category;
        return this;
    }

    public IssueBuilder setType(@Nullable final String type) {
        this.type = type;
        return this;
    }

    public IssueBuilder setPackageName(@Nullable final String packageName) {
        this.packageName = internPackageName(packageName);

        return this;
    }

    TreeString internPackageName(@Nullable final String unsafePackageName) {
        if (StringUtils.isBlank(unsafePackageName)) {
            return UNDEFINED_TREE_STRING;
        }
        else {
            return packageNameBuilder.intern(unsafePackageName);
        }
    }

    public IssueBuilder setModuleName(@Nullable final String moduleName) {
        this.moduleName = moduleName;
        return this;
    }

    public IssueBuilder setOrigin(@Nullable final String origin) {
        this.origin = origin;
        return this;
    }

    public IssueBuilder setReference(@Nullable final String reference) {
        this.reference = reference;
        return this;
    }

    public IssueBuilder setSeverity(@Nullable final Severity severity) {
        this.severity = severity;
        return this;
    }

    public IssueBuilder guessSeverity(@Nullable final String severityString) {
        severity = Severity.guessFromString(severityString);
        return this;
    }

    public IssueBuilder setMessage(@Nullable final String message) {
        if (StringUtils.isBlank(message)) {
            this.message = EMPTY_TREE_STRING;
        }
        else {
            this.message = messageBuilder.intern(StringUtils.stripToEmpty(message));
        }
        return this;
    }

    public IssueBuilder setDescription(@Nullable final String description) {
        this.description = StringUtils.stripToEmpty(description);
        return this;
    }

    public IssueBuilder setLineRanges(final LineRangeList lineRanges) {
        this.lineRanges = new LineRangeList(lineRanges);
        return this;
    }

    /**
     * Initializes this builder with an exact copy of aal properties of the specified issue.
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
        Issue issue = new Issue(fileName, lineStart, lineEnd, columnStart, columnEnd, lineRanges,
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
