package edu.hm.hafner.analysis;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.UUID;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * An issue reported by a static analysis tool. Use the provided {@link IssueBuilder builder} to create new instances.
 *
 * @author Ullrich Hafner
 */
// Add module that is the same for a parser scan
public class Issue implements Serializable {
    private static final String UNDEFINED = "-";

    private final String fileName;
    private final String category;
    private final String type;
    private final Priority priority;
    private final String message;

    private final String description;

    private final String packageName;
    private final String moduleName;
    private final String origin;

    private final int lineStart;
    private final int lineEnd;
    private final int columnStart;
    private final int columnEnd;

    private final UUID uuid;

    private String fingerprint;

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Issue issue = (Issue) o;

        if (lineStart != issue.lineStart) {
            return false;
        }
        if (lineEnd != issue.lineEnd) {
            return false;
        }
        if (columnStart != issue.columnStart) {
            return false;
        }
        if (columnEnd != issue.columnEnd) {
            return false;
        }
        if (!fileName.equals(issue.fileName)) {
            return false;
        }
        if (!category.equals(issue.category)) {
            return false;
        }
        if (!type.equals(issue.type)) {
            return false;
        }
        if (priority != issue.priority) {
            return false;
        }
        if (!message.equals(issue.message)) {
            return false;
        }
        if (!description.equals(issue.description)) {
            return false;
        }
        if (!packageName.equals(issue.packageName)) {
            return false;
        }
        if (!moduleName.equals(issue.moduleName)) {
            return false;
        }
        return origin.equals(issue.origin);
    }

    @Override
    public int hashCode() {
        int result = fileName.hashCode();
        result = 31 * result + category.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + priority.hashCode();
        result = 31 * result + message.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + packageName.hashCode();
        result = 31 * result + moduleName.hashCode();
        result = 31 * result + origin.hashCode();
        result = 31 * result + lineStart;
        result = 31 * result + lineEnd;
        result = 31 * result + columnStart;
        result = 31 * result + columnEnd;
        return result;
    }

    /**
     * Creates a new instance of {@link Issue} using the specified properties.
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
     */
    @SuppressWarnings("ParameterNumber")
    protected Issue(@CheckForNull final String fileName,
            final int lineStart, final int lineEnd, final int columnStart, final int columnEnd,
            @CheckForNull final String category, @CheckForNull final String type,
            @CheckForNull final String packageName, @CheckForNull final String moduleName,
            @CheckForNull final Priority priority,
            @CheckForNull final String message, @CheckForNull final String description,
            @CheckForNull final String origin) {
        this.fileName = defaultString(StringUtils.replace(StringUtils.strip(fileName), "\\", "/"));

        this.lineStart = defaultInteger(lineStart);
        this.lineEnd = lineEnd == 0 ? lineStart : defaultInteger(lineEnd);
        this.columnStart = defaultInteger(columnStart);
        this.columnEnd = columnEnd == 0 ? columnStart : defaultInteger(columnEnd);

        this.category = StringUtils.defaultString(category);
        this.type = defaultString(type);

        this.packageName = defaultString(packageName);
        this.moduleName = defaultString(moduleName);

        this.priority = ObjectUtils.defaultIfNull(priority, Priority.NORMAL);
        this.message = StringUtils.stripToEmpty(message);
        this.description = StringUtils.stripToEmpty(description);

        this.origin = StringUtils.stripToEmpty(origin);

        uuid = UUID.randomUUID();
    }


    private int defaultInteger(final int integer) {
        return integer < 0 ? 0 : integer;
    }

    private String defaultString(@CheckForNull final String string) {
        return StringUtils.defaultIfEmpty(string, UNDEFINED);
    }

    /**
     * Returns the unique ID of this issue.
     *
     * @return the unique ID
     */
    public final UUID getId() {
        return uuid;
    }

    /**
     * Returns the name of the file that contains this issue.
     *
     * @return the name of the file that contains this issue
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Returns the category of this issue (depends on the available categories of the static analysis tool). Examples
     * for categories are "Deprecation", "Design", or "JavaDoc".
     *
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Returns the type of this issue (depends on the available types of the static analysis tool). The type typically
     * is the associated rule of the static analysis tool that reported this issue.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Returns the priority of this issue.
     *
     * @return the priority
     */
    public Priority getPriority() {
        return priority;
    }

    /**
     * Returns the detailed message for this issue.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns an additional description for this issue. Static analysis tools might provide some additional information
     * about this issue. This description may contain valid HTML.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the first line of this issue (lines start at 1; 0 indicates the whole file).
     *
     * @return the first line
     */
    public int getLineStart() {
        return lineStart;
    }

    /**
     * Returns the last line of this issue (lines start at 1).
     *
     * @return the last line
     */
    public int getLineEnd() {
        return lineEnd;
    }

    /**
     * Returns the first column of this issue (columns start at 1, 0 indicates the whole line).
     *
     * @return the first column
     */
    public int getColumnStart() {
        return columnStart;
    }

    /**
     * Returns the last column of this issue (columns start at 1).
     *
     * @return the last column
     */
    public int getColumnEnd() {
        return columnEnd;
    }

    /**
     * Returns the name of the package or name space (or similar concept) that contains this issue.
     *
     * @return the package name
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * Returns the name of the module or project (or similar concept) that contains this issue.
     *
     * @return the module
     */
    public String getModuleName() {
        return moduleName;
    }

    /**
     * Returns the ID of the tool that did report this issue.
     *
     * @return the origin
     */
    public String getOrigin() {
        return origin;
    }

    /**
     * Returns the finger print for this issue. Used to decide if two issues are equal even if the equals method returns
     * {@code false} since some of the properties differ due to code refactorings. The fingerprint is created by
     * analyzing the content of the affected file.
     *
     * @return the fingerprint of this issue
     */
    // TODO: should the fingerprint be part of equals/hashcode?
    public String getFingerprint() {
        return defaultString(fingerprint);
    }

    /**
     * Sets the finger print for this issue.
     *
     * @param fingerprint
     *         the fingerprint for this issue
     *
     * @see #getFingerprint()
     */
    public void setFingerprint(final String fingerprint) {
        this.fingerprint = fingerprint;
    }

    @Override
    public String toString() {
        return String.format("%s(%d,%d): %s: %s: %s", fileName, lineStart, columnStart, type, category, message);
    }

}
