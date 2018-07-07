package edu.hm.hafner.analysis;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.util.Ensure;
import edu.hm.hafner.util.TreeString;
import edu.hm.hafner.util.TreeStringBuilder;
import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * An issue reported by a static analysis tool. Use the provided {@link IssueBuilder builder} to create new instances.
 *
 * @author Ullrich Hafner
 */
@SuppressWarnings("PMD.TooManyFields")
public class Issue implements Serializable {
    private static final long serialVersionUID = 1L; // release 1.0.0
    private static final String UNDEFINED = "-";

    /**
     * Returns the value of the property with the specified name for a given issue instance.
     *
     * @param issue
     *         the issue to get the property for
     * @param propertyName
     *         the name of the property
     *
     * @return the function that obtains the value
     */
    public static String getPropertyValueAsString(final Issue issue, final String propertyName) {
        try {
            return PropertyUtils.getProperty(issue, propertyName).toString();
        }
        catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ignored) {
            return propertyName;
        }
    }

    /**
     * Returns a function that can dynamically obtain the value of the property with the specified name of an issue
     * instance.
     *
     * @param propertyName
     *         the name of the property
     *
     * @return the function that obtains the value
     */
    public static Function<Issue, String> getPropertyValueGetter(final String propertyName) {
        return issue -> Issue.getPropertyValueAsString(issue, propertyName);
    }

    /**
     * Returns a predicate that checks if the package name of an issue is equal to the specified package name.
     *
     * @param packageName
     *         the package name to match
     *
     * @return the predicate
     */
    public static Predicate<Issue> byPackageName(final String packageName) {
        return issue -> issue.getPackageName().equals(packageName);
    }

    /**
     * Returns a predicate that checks if the module name of an issue is equal to the specified module name.
     *
     * @param moduleName
     *         the module name to match
     *
     * @return the predicate
     */
    public static Predicate<Issue> byModuleName(final String moduleName) {
        return issue -> issue.getModuleName().equals(moduleName);
    }

    /**
     * Returns a predicate that checks if the file name of an issue is equal to the specified file name.
     *
     * @param fileName
     *         the file name to match
     *
     * @return the predicate
     */
    public static Predicate<Issue> byFileName(final String fileName) {
        return issue -> issue.getFileName().equals(fileName);
    }

    /**
     * Returns a predicate that checks if the severity of an issue is equal to the specified severity.
     *
     * @param severity
     *         the severity to match
     *
     * @return the predicate
     */
    public static Predicate<Issue> bySeverity(final Severity severity) {
        return issue -> issue.getSeverity().equals(severity);
    }

    /**
     * Returns a predicate that checks if the category of an issue is equal to the specified category.
     *
     * @param category
     *         the category to match
     *
     * @return the predicate
     */
    public static Predicate<Issue> byCategory(final String category) {
        return issue -> issue.getCategory().equals(category);
    }

    /**
     * Returns a predicate that checks if the origin of an issue is equal to the specified origin.
     *
     * @param origin
     *         the origin to match
     *
     * @return the predicate
     */
    public static Predicate<Issue> byOrigin(final String origin) {
        return issue -> issue.getOrigin().equals(origin);
    }

    /**
     * Returns a predicate that checks if the type of an issue is equal to the specified type.
     *
     * @param type
     *         the type to match
     *
     * @return the predicate
     */
    public static Predicate<Issue> byType(final String type) {
        return issue -> issue.getType().equals(type);
    }

    private String category; // almost final
    private String type;     // almost final

    private final Severity severity;
    private final TreeString message;
    private final int lineStart;            // fixed
    private final int lineEnd;              // fixed
    private final int columnStart;          // fixed

    private final int columnEnd;            // fixed

    private final LineRangeList lineRanges; // fixed

    private final UUID id;                  // fixed

    private final TreeString description;   // fixed
    @CheckForNull
    private final Serializable additionalProperties;  // fixed
    private String reference;       // mutable, not part of equals
    private String origin;          // mutable
    private String moduleName;      // mutable

    private TreeString packageName; // mutable

    private TreeString fileName;    // mutable

    private String fingerprint;     // mutable, not part of equals

    /**
     * Creates a new instance of {@link Issue} using the properties of the other issue instance. The new issue has the
     * same ID as the copy.
     *
     * @param copy
     *         the other issue to copy the properties from
     */
    protected Issue(final Issue copy) {
        this(copy.getFileName(), copy.getLineStart(), copy.getLineEnd(), copy.getColumnStart(), copy.getColumnEnd(),
                copy.getLineRanges(), copy.getCategory(), copy.getType(), copy.getPackageName(), copy.getModuleName(),
                copy.getSeverity(), copy.getMessage(), copy.getDescription(), copy.getOrigin(), copy.getReference(),
                copy.getFingerprint(), copy.getAdditionalProperties(), copy.getId());
    }

    /**
     * Creates a new instance of {@link Issue} using the specified properties. The new issue will get a new generated
     * ID.
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
     * @param lineRanges
     *         additional line ranges of this issue
     * @param category
     *         the category of this issue (depends on the available categories of the static analysis tool)
     * @param type
     *         the type of this issue (depends on the available types of the static analysis tool)
     * @param packageName
     *         the name of the package (or name space) that contains this issue
     * @param moduleName
     *         the name of the moduleName (or project) that contains this issue
     * @param severity
     *         the severity of this issue
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
     */
    @SuppressWarnings("ParameterNumber")
    protected Issue(@CheckForNull final String fileName,
            final int lineStart, final int lineEnd, final int columnStart, final int columnEnd,
            @CheckForNull final LineRangeList lineRanges,
            @CheckForNull final String category, @CheckForNull final String type,
            @CheckForNull final String packageName, @CheckForNull final String moduleName,
            @CheckForNull final Severity severity,
            @CheckForNull final String message, @CheckForNull final String description,
            @CheckForNull final String origin, @CheckForNull final String reference,
            @CheckForNull final String fingerprint, @CheckForNull final Serializable additionalProperties) {
        this(fileName, lineStart, lineEnd, columnStart, columnEnd, lineRanges, category, type, packageName, moduleName,
                severity, message, description, origin, reference, fingerprint, additionalProperties,
                UUID.randomUUID());
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
     * @param lineRanges
     *         additional line ranges of this issue
     * @param category
     *         the category of this issue (depends on the available categories of the static analysis tool)
     * @param type
     *         the type of this issue (depends on the available types of the static analysis tool)
     * @param packageName
     *         the name of the package (or name space) that contains this issue
     * @param moduleName
     *         the name of the moduleName (or project) that contains this issue
     * @param severity
     *         the severity of this issue
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
     * @param id
     *         the ID of this issue
     */
    @SuppressWarnings("ParameterNumber")
    protected Issue(@CheckForNull final String fileName, final int lineStart, final int lineEnd, final int columnStart,
            final int columnEnd, @CheckForNull final LineRangeList lineRanges, @CheckForNull final String category,
            @CheckForNull final String type, @CheckForNull final String packageName,
            @CheckForNull final String moduleName, @CheckForNull final Severity severity,
            @CheckForNull final String message, @CheckForNull final String description,
            @CheckForNull final String origin, @CheckForNull final String reference,
            @CheckForNull final String fingerprint, @CheckForNull final Serializable additionalProperties,
            final UUID id) {
        TreeStringBuilder builder = new TreeStringBuilder();

        this.fileName = builder.intern(defaultString(normalizeFileName(fileName)));

        int providedLineStart = defaultInteger(lineStart);
        int providedLineEnd = defaultInteger(lineEnd) == 0 ? providedLineStart : defaultInteger(lineEnd);
        if (providedLineStart == 0) {
            this.lineStart = providedLineEnd;
            this.lineEnd = providedLineEnd;
        }
        else {
            this.lineStart = Math.min(providedLineStart, providedLineEnd);
            this.lineEnd = Math.max(providedLineStart, providedLineEnd);
        }

        int providedColumnStart = defaultInteger(columnStart);
        int providedColumnEnd = defaultInteger(columnEnd) == 0 ? providedColumnStart : defaultInteger(columnEnd);
        if (providedColumnStart == 0) {
            this.columnStart = providedColumnEnd;
            this.columnEnd = providedColumnEnd;
        }
        else {
            this.columnStart = Math.min(providedColumnStart, providedColumnEnd);
            this.columnEnd = Math.max(providedColumnStart, providedColumnEnd);
        }
        this.lineRanges = new LineRangeList();
        if (lineRanges != null) {
            this.lineRanges.addAll(lineRanges);
        }
        this.category = StringUtils.defaultString(category).intern();
        this.type = defaultString(type);

        this.packageName = builder.intern(defaultString(packageName));
        this.moduleName = defaultString(moduleName);

        this.severity = ObjectUtils.defaultIfNull(severity, Severity.WARNING_NORMAL);
        this.message = builder.intern(StringUtils.stripToEmpty(message));
        this.description = builder.intern(StringUtils.stripToEmpty(description));

        this.origin = stripToEmpty(origin);
        this.reference = stripToEmpty(reference);

        this.fingerprint = defaultString(fingerprint);
        this.additionalProperties = additionalProperties;

        this.id = id;
    }

    /**
     * Called after de-serialization to improve the memory usage.
     *
     * @return this
     */
    protected Object readResolve() {
        category = category.intern();
        type = type.intern();
        moduleName = moduleName.intern();
        origin = origin.intern();
        reference = reference.intern();

        return this;
    }

    private String normalizeFileName(@CheckForNull final String platformFileName) {
        return StringUtils.replace(StringUtils.strip(platformFileName), "\\", "/");
    }

    /**
     * Creates a default Integer representation for undefined input parameters.
     *
     * @param integer
     *         the integer to check
     *
     * @return the valid string or a default string if the specified string is not valid
     */
    private int defaultInteger(final int integer) {
        return integer < 0 ? 0 : integer;
    }

    /**
     * Creates a default String representation for undefined input parameters.
     *
     * @param string
     *         the string to check
     *
     * @return the valid string or a default string if the specified string is not valid
     */
    private String defaultString(@CheckForNull final String string) {
        return StringUtils.defaultIfEmpty(string, UNDEFINED).intern();
    }

    /**
     * Strips whitespace from the start and end of a String returning an empty String if {@code null} input.
     *
     * @param string
     *         the string to check
     *
     * @return the stripped string or the empty string if the specified string is {@code null}
     */
    private String stripToEmpty(@CheckForNull final String string) {
        return StringUtils.stripToEmpty(string).intern();
    }

    /**
     * Returns the unique ID of this issue.
     *
     * @return the unique ID
     */
    public UUID getId() {
        return id;
    }

    /**
     * Returns the name of the file that contains this issue. Typically this file name is the absolute name.
     *
     * @return the name of the file that contains this issue
     */
    public String getFileName() {
        return fileName.toString();
    }

    /**
     * Returns the base name of the file that contains this issue (i.e. the file name without the full path). 
     *
     * @return the base name of the file that contains this issue
     */
    public String getBaseName() {
        try {
            Path baseName = Paths.get(getFileName()).getFileName();
            if (baseName == null) {
                return getFileName(); // fallback
            }
            return baseName.toString();
        }
        catch (InvalidPathException e) {
            return getFileName();
        }
    }

    /**
     * Sets the name of the file that contains this issue.
     *
     * @param fileName
     *         the file name to set
     */
    public void setFileName(@CheckForNull final String fileName) {
        this.fileName = TreeString.valueOf(StringUtils.stripToEmpty(fileName));
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
    public Severity getSeverity() {
        return severity;
    }

    /**
     * Returns the detailed message for this issue.
     *
     * @return the message
     */
    public String getMessage() {
        return message.toString();
    }

    /**
     * Returns an additional description for this issue. Static analysis tools might provide some additional information
     * about this issue. This description may contain valid HTML.
     *
     * @return the description
     */
    public String getDescription() {
        return description.toString();
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
     * Returns additional line ranges for this issue. Not that the primary range given by {@code lineStart} and {@code
     * lineEnd} is not included.
     *
     * @return the last line
     */
    public LineRangeList getLineRanges() {
        return new LineRangeList(lineRanges);
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
        return packageName.toString();
    }

    /**
     * Sets the name of the package or name space (or similar concept) that contains this issue.
     *
     * @param packageName
     *         the name of the package
     */
    public void setPackageName(@CheckForNull final String packageName) {
        this.packageName = TreeString.valueOf(StringUtils.stripToEmpty(packageName));
    }

    /**
     * Returns whether this issue has a package name set.
     *
     * @return {@code true} if this issue has a package name set
     * @see #getPackageName()
     */
    public boolean hasPackageName() {
        return !UNDEFINED.equals(getPackageName());
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
     * Sets the the name of the module or project (or similar concept) that contains this issue.
     *
     * @param moduleName
     *         the module name to set
     */
    public void setModuleName(@CheckForNull final String moduleName) {
        this.moduleName = stripToEmpty(moduleName);
    }

    /**
     * Returns whether this issue has a module name set.
     *
     * @return {@code true} if this issue has a module name set
     * @see #getModuleName()
     */
    public boolean hasModuleName() {
        return !UNDEFINED.equals(getModuleName());
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
     * Sets the ID of the tool that did report this issue.
     *
     * @param origin
     *         the origin
     */
    public void setOrigin(final String origin) {
        Ensure.that(origin).isNotBlank("Issue origin '%s' must be not blank (%s)", id, toString());

        this.origin = origin.intern();
    }

    /**
     * Returns a reference to the execution of the static analysis tool (build ID, timestamp, etc.).
     *
     * @return the reference
     */
    public String getReference() {
        return reference;
    }

    /**
     * Sets a reference to the execution of the static analysis tool (build ID, timestamp, etc.).
     *
     * @param reference
     *         the reference
     */
    public void setReference(@CheckForNull final String reference) {
        this.reference = stripToEmpty(reference);
    }

    /**
     * Returns the finger print for this issue. Used to decide if two issues are equal even if the equals method returns
     * {@code false} since some of the properties differ due to code refactorings. The fingerprint is created by
     * analyzing the content of the affected file.
     * <p>
     * Note: the fingerprint is not part of the equals method since the fingerprint might change due to an unrelated
     * refactoring of the source code.
     * </p>
     *
     * @return the fingerprint of this issue
     */
    public String getFingerprint() {
        return fingerprint;
    }

    /**
     * Sets the finger print for this issue to the given value.
     *
     * @param fingerprint
     *         the fingerprint to set
     *
     * @see #getFingerprint()
     */
    public void setFingerprint(@CheckForNull final String fingerprint) {
        this.fingerprint = StringUtils.stripToEmpty(fingerprint);
    }

    /**
     * Returns whether this issue already has a fingerprint set.
     *
     * @return {@code true} if this issue already has a fingerprint set
     */
    public boolean hasFingerprint() {
        return !UNDEFINED.equals(fingerprint);
    }

    /**
     * Returns additional properties for this issue. A static analysis tool may store additional properties in this
     * untyped object. This object will be serialized and is used in {@code equals} and {@code hashCode}.
     *
     * @return the additional properties
     */
    @CheckForNull
    public Serializable getAdditionalProperties() {
        return additionalProperties;
    }

    @SuppressWarnings("all")
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
        if (!category.equals(issue.category)) {
            return false;
        }
        if (!type.equals(issue.type)) {
            return false;
        }
        if (!severity.equals(issue.severity)) {
            return false;
        }
        if (!message.equals(issue.message)) {
            return false;
        }
        if (!lineRanges.equals(issue.lineRanges)) {
            return false;
        }
        if (!description.equals(issue.description)) {
            return false;
        }
        if (additionalProperties != null ? !additionalProperties.equals(issue.additionalProperties) :
                issue.additionalProperties != null) {
            return false;
        }
        if (!origin.equals(issue.origin)) {
            return false;
        }
        if (!moduleName.equals(issue.moduleName)) {
            return false;
        }
        if (!packageName.equals(issue.packageName)) {
            return false;
        }
        return fileName.equals(issue.fileName);
    }

    @SuppressWarnings("all")
    @Override
    public int hashCode() {
        int result = category.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + severity.hashCode();
        result = 31 * result + message.hashCode();
        result = 31 * result + lineStart;
        result = 31 * result + lineEnd;
        result = 31 * result + columnStart;
        result = 31 * result + columnEnd;
        result = 31 * result + lineRanges.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + (additionalProperties != null ? additionalProperties.hashCode() : 0);
        result = 31 * result + origin.hashCode();
        result = 31 * result + moduleName.hashCode();
        result = 31 * result + packageName.hashCode();
        result = 31 * result + fileName.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("%s(%d,%d): %s: %s: %s", fileName, lineStart, columnStart, type, category, message);
    }

}
