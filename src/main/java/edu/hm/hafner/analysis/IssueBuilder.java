package edu.hm.hafner.analysis;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

public class IssueBuilder {
    private static final String UNDEFINED = "-";

    private String fileName = UNDEFINED;
    private int lineStart = 0;
    private int lineEnd = 0;
    private int columnStart = 0;
    private int columnEnd = 0;
    private String category = StringUtils.EMPTY;
    private String type = UNDEFINED;
    private Priority priority = Priority.NORMAL;
    private String message = StringUtils.EMPTY;
    private String description = StringUtils.EMPTY;
    private String packageName = UNDEFINED;

    public IssueBuilder setFileName(final String fileName) {
        this.fileName = defaultString(fileName);
        return this;
    }

    public IssueBuilder setLineStart(final int lineStart) {
        this.lineStart = defaultInteger(lineStart);
        return this;
    }

    public IssueBuilder setLineEnd(final int lineEnd) {
        this.lineEnd = defaultInteger(lineEnd);
        return this;
    }

    public IssueBuilder setColumnStart(final int columnStart) {
        this.columnStart = defaultInteger(columnStart);
        return this;
    }

    public IssueBuilder setColumnEnd(final int columnEnd) {
        this.columnEnd = defaultInteger(columnEnd);
        return this;
    }

    public IssueBuilder setCategory(final String category) {
        this.category = StringUtils.defaultString(category);
        return this;
    }

    public IssueBuilder setType(final String type) {
        this.type = defaultString(type);
        return this;
    }

    public IssueBuilder setPackageName(final String packageName) {
        this.packageName = defaultString(packageName);
        return this;
    }

    public IssueBuilder setPriority(final Priority priority) {
        this.priority = ObjectUtils.defaultIfNull(priority, Priority.NORMAL);
        return this;
    }

    public IssueBuilder setMessage(final String message) {
        this.message = StringUtils.defaultString(message);
        return this;
    }

    public IssueBuilder setDescription(final String description) {
        this.description = StringUtils.defaultString(description);
        return this;
    }

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

        return this;
    }

    public Issue build() {
        return new Issue(fileName, lineStart, lineEnd, columnStart, columnEnd, category, type, packageName, priority, message, description);
    }

    private int defaultInteger(final int integer) {
        return integer < 0 ? 0 : integer;
    }

    private String defaultString(final String string) {
        return StringUtils.defaultIfEmpty(string, UNDEFINED);
    }
}