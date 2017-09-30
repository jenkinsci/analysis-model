package edu.hm.hafner.analysis;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

/**
 * An issue reported by a static analysis tool. Use the provided {@link IssueBuilder builder} to create new instances.
 *
 * @author Ullrich Hafner
 */
public class Issue {
    public static final String DEFAULT_CATEGORY = "";

    private final String fileName;
    private final String category;
    private final String type;
    private final Priority priority;
    private final String message;

    private final String description;

    private final String packageName;

    private final int lineStart;
    private final int lineEnd;
    private final int columnStart;
    private final int columnEnd;
    private final UUID uuid;

    /** The name of the module that contains this issue. */
    private String moduleName;
    /**
     * Finger print of this issue. Used to decide if two issues are equal even if the equals method returns {@code
     * false} since some of the properties differ due to code refactorings.
     */
    private String fingerprint;

    /**
     * Creates a new instance of {@link Issue} using the specified properties.
     *
     * @param fileName    the name of the file that contains this issue
     * @param lineStart   the first line of this issue (lines start at 1; 0 indicates the whole file)
     * @param lineEnd     the last line of this issue (lines start at 1)
     * @param columnStart the first column of this issue (columns start at 1, 0 indicates the whole line)
     * @param columnEnd   the last column of this issue (columns start at 1)
     * @param category    the category of this issue (depends on the available categories of the static analysis tool)
     * @param type        the type of this issue (depends on the available types of the static analysis tool)
     * @param packageName the name of the package (or name space) that contains this issue
     * @param priority    the priority of this issue
     * @param message     the detail message of this issue
     * @param description the description for this issue
     */
    Issue(final String fileName, final int lineStart, final int lineEnd, final int columnStart, final int columnEnd, final String category,
                 final String type, final String packageName, final Priority priority, final String message, final String description) {
        this.fileName = StringUtils.replace(StringUtils.strip(fileName), "\\", "/");
        this.lineStart = lineStart;
        this.lineEnd = lineEnd;
        this.columnStart = columnStart;
        this.columnEnd = columnEnd == 0 ? columnStart : columnEnd;
        this.category = category;
        this.type = type;
        this.packageName = packageName;
        this.priority = priority;
        this.message = StringUtils.strip(message);
        this.description = StringUtils.strip(description);

        uuid = UUID.randomUUID();
    }

    public final UUID getUuid() {
        return uuid;
    }

    public String getFileName() {
        return fileName;
    }

    public String getCategory() {
        return category;
    }

    public String getType() {
        return type;
    }

    public Priority getPriority() {
        return priority;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }

    public int getLineStart() {
        return lineStart;
    }

    public int getLineEnd() {
        return lineStart;
    }

    public int getColumnStart() {
        return columnStart;
    }

    public int getColumnEnd() {
        return columnEnd;
    }

    public String getModuleName() {
        return moduleName;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    @Override
    public String toString() {
        return String.format("%s(%d,%d): %s: %s: %s", fileName, lineStart, columnStart, type, category, message);
    }

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
        if (fileName != null ? !fileName.equals(issue.fileName) : issue.fileName != null) {
            return false;
        }
        if (category != null ? !category.equals(issue.category) : issue.category != null) {
            return false;
        }
        if (type != null ? !type.equals(issue.type) : issue.type != null) {
            return false;
        }
        if (priority != issue.priority) {
            return false;
        }
        if (message != null ? !message.equals(issue.message) : issue.message != null) {
            return false;
        }
        if (description != null ? !description.equals(issue.description) : issue.description != null) {
            return false;
        }
        if (packageName != null ? !packageName.equals(issue.packageName) : issue.packageName != null) {
            return false;
        }
        return moduleName != null ? moduleName.equals(issue.moduleName) : issue.moduleName == null;
    }

    @Override
    public int hashCode() {
        int result = fileName != null ? fileName.hashCode() : 0;
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (priority != null ? priority.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (packageName != null ? packageName.hashCode() : 0);
        result = 31 * result + lineStart;
        result = 31 * result + lineEnd;
        result = 31 * result + columnStart;
        result = 31 * result + columnEnd;
        result = 31 * result + (moduleName != null ? moduleName.hashCode() : 0);
        return result;
    }
}
