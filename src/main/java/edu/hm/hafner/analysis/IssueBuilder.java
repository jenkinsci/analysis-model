package edu.hm.hafner.analysis;

/**
 * Creates new {@link Issue issues} using the builder pattern. All properties that have not been set in the builder will
 * be set to their default value. <p>Example:</p>
 * <p>
 * <blockquote><pre>
 * Issue issue = new IssueBuilder()
 *                      .setFileName("affected.file")
 *                      .setLineStart(0)
 *                      .setCategory("JavaDoc")
 *                      .setMessage("Missing JavaDoc")
 *                      .setPriority(Priority.LOW);
 * </pre></blockquote>
 *
 * @author Ullrich Hafner
 */
@SuppressWarnings({"InstanceVariableMayNotBeInitialized", "JavaDocMethod"})
public class IssueBuilder {
    protected String fileName;
    protected int lineStart = 0;
    protected int lineEnd = 0;
    protected int columnStart = 0;
    protected int columnEnd = 0;
    protected String category;
    protected String type;
    protected Priority priority;
    protected String message;
    protected String description;
    protected String packageName;
    protected String moduleName;
    protected String origin;
    protected String fingerprint;

    public IssueBuilder setFingerprint(final String fingerprint) {
        this.fingerprint = fingerprint;
        return this;
    }

    public IssueBuilder setFileName(final String fileName) {
        this.fileName = fileName;
        return this;
    }

    public IssueBuilder setLineStart(final int lineStart) {
        this.lineStart = lineStart;
        return this;
    }

    public IssueBuilder setLineEnd(final int lineEnd) {
        this.lineEnd = lineEnd;
        return this;
    }

    public IssueBuilder setColumnStart(final int columnStart) {
        this.columnStart = columnStart;
        return this;
    }

    public IssueBuilder setColumnEnd(final int columnEnd) {
        this.columnEnd = columnEnd;
        return this;
    }

    public IssueBuilder setCategory(final String category) {
        this.category = category;
        return this;
    }

    public IssueBuilder setType(final String type) {
        this.type = type;
        return this;
    }

    public IssueBuilder setPackageName(final String packageName) {
        this.packageName = packageName;
        return this;
    }

    public IssueBuilder setModuleName(final String moduleName) {
        this.moduleName = moduleName;
        return this;
    }

    public IssueBuilder setOrigin(final String origin) {
        this.origin = origin;
        return this;
    }

    public IssueBuilder setPriority(final Priority priority) {
        this.priority = priority;
        return this;
    }

    public IssueBuilder setMessage(final String message) {
        this.message = message;
        return this;
    }

    public IssueBuilder setDescription(final String description) {
        this.description = description;
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
        category = copy.getCategory();
        type = copy.getType();
        priority = copy.getPriority();
        message = copy.getMessage();
        description = copy.getDescription();
        packageName = copy.getPackageName();
        moduleName = copy.getModuleName();
        origin = copy.getOrigin();
        fingerprint = copy.getFingerprint();

        return this;
    }

    /**
     * Creates a new {@link Issue} based on the specified properties.
     *
     * @return the created issue
     */
    public Issue build() {
        return new Issue(fileName, lineStart, lineEnd, columnStart, columnEnd, category, type,
                packageName, moduleName, priority, message, description, origin, fingerprint);
    }
}