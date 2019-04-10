package edu.hm.hafner.analysis;

import java.io.Serializable;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.util.PathUtil;
import edu.hm.hafner.util.TreeString;
import edu.hm.hafner.util.TreeStringBuilder;
import edu.hm.hafner.util.VisibleForTesting;
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

    private TreeStringBuilder treeStringBuilder = new TreeStringBuilder();

    private int lineStart = 0;
    private int lineEnd = 0;
    private int columnStart = 0;
    private int columnEnd = 0;
    @Nullable
    private LineRangeList lineRanges;
    @Nullable
    private String fileName;
    @Nullable
    private String directory;
    @Nullable
    private String category;
    @Nullable
    private String type;
    @Nullable
    private Severity severity;
    @Nullable
    private String message;
    @Nullable
    private String description;
    @Nullable
    private String packageName;
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
        if (StringUtils.isEmpty(fileName)) {
            this.fileName = StringUtils.EMPTY;
        }
        else {
            this.fileName = new PathUtil().createAbsolutePath(directory, fileName);
        }

        return this;
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
        this.packageName = packageName;
        return this;
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
        this.severity = Severity.guessFromString(severityString);
        return this;
    }

    public IssueBuilder setMessage(@Nullable final String message) {
        this.message = message;
        return this;
    }

    public IssueBuilder setDescription(@Nullable final String description) {
        this.description = description;
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
        fileName = copy.getFileName();
        lineStart = copy.getLineStart();
        lineEnd = copy.getLineEnd();
        columnStart = copy.getColumnStart();
        columnEnd = copy.getColumnEnd();
        lineRanges = copy.getLineRanges();
        category = copy.getCategory();
        type = copy.getType();
        severity = copy.getSeverity();
        message = copy.getMessage();
        description = copy.getDescription();
        packageName = copy.getPackageName();
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
        Issue issue = new Issue(treeStringOfFileName(fileName), lineStart, lineEnd, columnStart, columnEnd, lineRanges, category, type,
                packageName, moduleName, severity, message, description, origin, reference, fingerprint,
                additionalProperties, id);
        id = UUID.randomUUID(); // make sure that multiple invocations will create different IDs
        return issue;
    }

    @VisibleForTesting
    TreeString treeStringOfFileName(@Nullable final String fileName) {
        return treeStringBuilder.intern(normalizeFileName(fileName));
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
        return StringUtils.defaultIfEmpty(string, Issue.UNDEFINED).intern();
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