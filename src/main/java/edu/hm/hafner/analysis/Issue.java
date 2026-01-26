package edu.hm.hafner.analysis;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.util.Ensure;
import edu.hm.hafner.util.LineRange;
import edu.hm.hafner.util.LineRangeList;
import edu.hm.hafner.util.PathUtil;
import edu.hm.hafner.util.TreeString;
import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.io.Serial;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * An issue reported by a static analysis tool. Use the provided {@link IssueBuilder builder} to create new instances.
 *
 * @author Ullrich Hafner
 */
@SuppressWarnings({"PMD.GodClass", "PMD.CyclomaticComplexity", "NoFunctionalReturnType"})
public class Issue implements Serializable {
    @Serial
    private static final long serialVersionUID = 14L; // release 14.0.0

    private static final PathUtil PATH_UTIL = new PathUtil();

    static final String UNDEFINED = "-";

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
        return issue -> getPropertyValueAsString(issue, propertyName);
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
     * Returns a predicate that checks if the folder of an issue is equal to the specified folder.
     *
     * @param folder
     *         the folder to match
     *
     * @return the predicate
     */
    public static Predicate<Issue> byFolder(final String folder) {
        return issue -> issue.getFolder().equals(folder);
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
     * Returns a predicate that checks if the type of issue is equal to the specified type.
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

    @Deprecated
    private final int lineStart = 0;                // replaced by Location
    @Deprecated
    private final int lineEnd = 0;                  // replaced by Location
    @Deprecated
    private final int columnStart = 0;              // replaced by Location
    @Deprecated
    private final int columnEnd = 0;                // replaced by Location
    @Deprecated
    private LineRangeList lineRanges = null;  // replaced by Location
    @Deprecated
    private TreeString fileName;                    // replaced by Location

    @CheckForNull
    @SuppressWarnings("PMD.LooseCoupling")
    private ArrayList<Location> locations; // fixed

    private final UUID id;                  // fixed

    @CheckForNull
    private final Serializable additionalProperties;  // fixed

    private String reference;       // mutable, not part of equals
    private String origin;          // mutable
    private String originName;      // mutable

    private String moduleName;      // mutable
    private TreeString packageName; // mutable
    private String pathName;        // mutable, not part of equals, @since 8.0.0

    private final TreeString message;   // fixed
    private String description;   // fixed

    private String fingerprint;     // mutable, not part of equals
    private boolean partOfModifiedCode;     // mutable, not part of equals

    /**
     * Creates a new instance of {@link Issue} using the specified properties.
     *
     * @param pathName
     *         the path that contains the affected file and the other files related to this issue
     * @param locations
     *         file locations related to this issue, the first location is the primary one
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
     * @param originName
     *         the name of the tool that did report this issue
     * @param reference
     *         an arbitrary reference to the execution of the static analysis tool (build ID, timestamp, etc.)
     * @param fingerprint
     *         the fingerprint for this issue
     * @param additionalProperties
     *         additional properties from the statical analysis tool
     * @param id
     *         the ID of this issue
     */
    @SuppressWarnings("ParameterNumber")
    Issue(@CheckForNull final String pathName, final List<Location> locations,
            @CheckForNull final String category, @CheckForNull final String type,
            final TreeString packageName, @CheckForNull final String moduleName,
            @CheckForNull final Severity severity, final TreeString message, final String description,
            @CheckForNull final String origin, @CheckForNull final String originName,
            @CheckForNull final String reference, @CheckForNull final String fingerprint,
            @CheckForNull final Serializable additionalProperties, final UUID id) {
        this.pathName = normalizeFileName(pathName);
        this.locations = new ArrayList<>(locations);
        this.category = StringUtils.defaultString(category).intern();
        this.type = defaultString(type);

        this.packageName = packageName;
        this.moduleName = defaultString(moduleName);

        this.severity = severity == null ? Severity.WARNING_NORMAL : severity;
        this.message = message;
        this.description = description.intern();

        this.origin = stripToEmpty(origin);
        this.originName = stripToEmpty(originName);
        this.reference = stripToEmpty(reference);

        this.fingerprint = defaultString(fingerprint);
        this.additionalProperties = additionalProperties;

        this.id = id;
    }

    /**
     * Called after deserialization to improve the memory usage and to restore new fields with default values.
     *
     * @return this
     */
    @Serial
    @SuppressFBWarnings(value = "RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE", justification = "Deserialization of instances that do not have all fields yet")
    protected Object readResolve() {
        category = category.intern();
        type = type.intern();
        moduleName = moduleName.intern();
        origin = origin.intern();
        reference = reference.intern();

        if (pathName == null) { // new in version 8.0.0
            pathName = UNDEFINED;
        }
        else {
            pathName = pathName.intern();
        }
        if (description == null) { // String in version 8.0.0
            description = UNDEFINED;
        }
        else {
            description = description.intern();
        }
        if (originName == null) { // new in version 10.0.0
            originName = StringUtils.EMPTY;
        }
        else {
            originName = originName.intern();
        }
        if (locations == null) { // new in version 14.0.0
            // Create a Location from the old fields to retain backward compatibility
            locations = toLocations(fileName, lineStart, lineEnd, columnStart, columnEnd, lineRanges);
            fileName = null; // free memory
            lineRanges = null; // free memory
        }
        return this;
    }

    static ArrayList<Location> toLocations(final TreeString oldFileName,
            final int oldLineStart, final int oldLineEnd, final int oldColumnStart, final int oldColumnEnd,
            final LineRangeList oldLineRanges) {
        var converted = new ArrayList<Location>();
        converted.add(new Location(oldFileName, oldLineStart, oldLineEnd, oldColumnStart, oldColumnEnd));
        oldLineRanges.stream()
                .map(lr -> new Location(oldFileName, lr.getStart(), lr.getEnd()))
                .forEach(converted::add);
        return converted;
    }

    private String normalizeFileName(@CheckForNull final String platformFileName) {
        if (platformFileName == null || UNDEFINED.equals(platformFileName) || StringUtils.isBlank(platformFileName)) {
            return UNDEFINED;
        }
        return PATH_UTIL.getAbsolutePath(platformFileName);
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
     * Returns the name of the affected file. This file name is a path relative to the path of the affected files
     * (returned by {@link #getPath()}).
     *
     * @return the name of the file that contains this issue
     * @see #getPath()
     */
    public String getFileName() {
        return getPrimaryLocation().getFileName();
    }

    /**
     * Returns the tree-string containing the name of the affected file. This file name is a path relative to the path
     * of the affected files (returned by {@link #getPath()}).
     *
     * @return the cached tree-string containing the name of the file that contains this issue
     */
    TreeString getFileNameTreeString() {
        return getPrimaryLocation().getFileNameTreeString();
    }

    /**
     * Returns the folder that contains the affected file of this issue. Note that this path is not an absolute path, it
     * is relative to the path of the affected files (returned by {@link #getPath()}).
     *
     * @return the folder of the file that contains this issue
     */
    public String getFolder() {
        try {
            var folder = FilenameUtils.getPath(getFileName());
            if (StringUtils.isBlank(folder)) {
                return UNDEFINED;
            }
            return PATH_UTIL.getRelativePath(folder);
        }
        catch (IllegalArgumentException ignore) {
            return UNDEFINED; // fallback
        }
    }

    /**
     * Returns the base name of the file that contains this issue (i.e., the file name without the full path).
     *
     * @return the base name of the file that contains this issue
     */
    public String getBaseName() {
        try {
            return FilenameUtils.getName(getFileName());
        }
        catch (IllegalArgumentException ignore) {
            return getFileName(); // fallback
        }
    }

    /**
     * Returns the absolute path of the affected file.
     *
     * @return the base name of the file that contains this issue
     */
    public String getAbsolutePath() {
        if (UNDEFINED.equals(pathName)) {
            return getFileName();
        }
        else {
            return PATH_UTIL.createAbsolutePath(pathName, getFileName());
        }
    }

    /**
     * Returns the path of the affected file. Note that this path is not the parent folder of the affected file. This
     * path is the folder that contains all the affected files of a {@link Report}. If this path is not defined, then
     * the default value {@link #UNDEFINED} is returned.
     *
     * @return the base name of the file that contains this issue
     */
    public String getPath() {
        return pathName;
    }

    /**
     * Sets the name of the file that contains this issue.
     *
     * @param pathName
     *         the path that contains the affected file
     * @param fileName
     *         the file name to set
     */
    @SuppressWarnings("checkstyle:HiddenField")
    void setFileName(final String pathName, final TreeString fileName) {
        this.pathName = normalizeFileName(pathName);
        var oldFileName = getFileNameTreeString();
        var changedLocations = locations.stream().map(l -> createLocation(l, oldFileName, fileName)).toList();
        locations.clear();
        locations.addAll(changedLocations);
    }

    private Location createLocation(final Location existing, final TreeString oldFileName, final TreeString newFileName) {
        if (existing.getFileNameTreeString().equals(oldFileName)) {
            return new Location(newFileName,
                    existing.getLineStart(), existing.getLineEnd(), existing.getColumnStart(), existing.getColumnEnd());
        }
        return existing;
    }

    /**
     * Returns whether this issue has a file name set.
     *
     * @return {@code true} if this issue has a file name set
     * @see #getFileName()
     */
    public boolean hasFileName() {
        return !UNDEFINED.equals(getFileName());
    }

    /**
     * Returns the category of this issue (depends on the available categories of the static analysis tool). Examples
     * for categories are "Deprecation", "Design", or "Javadoc".
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
     * Returns the severity of this issue.
     *
     * @return the severity
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

    TreeString getMessageTreeString() {
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
        return getPrimaryLocation().getLineStart();
    }

    /**
     * Returns the last line of this issue (lines start at 1).
     *
     * @return the last line
     */
    public int getLineEnd() {
        return getPrimaryLocation().getLineEnd();
    }

    /**
     * Returns additional line ranges for this issue. Not that the primary range given by {@code lineStart} and {@code
     * lineEnd} is not included.
     *
     * @return the last line
     * @deprecated use {@link #getLocations()} instead
     */
    @Deprecated
    public Iterable<? extends LineRange> getLineRanges() {
        return locations.stream()
                .skip(1) // primary location is not included
                .map(location -> new LineRange(location.getLineStart(), location.getLineEnd()))
                .toList();
    }

    /**
     * Returns all locations of this issue. The first location is the primary location.
     * Some warnings span multiple files, such as GNU CC's reorder warning for C++ where
     * the warning shows up in the initializer list but references the header file.
     * More involved cases are MicroFocus Fortify and Synopsis Coverity, which trace execution
     * potentially through multiple classes or translation units.
     *
     * @return the locations
     */
    public Iterable<? extends Location> getLocations() {
        return List.copyOf(locations);
    }

    /**
     * Returns all locations of this issue that are within the affected file of the primary location.
     *
     * @return the locations in the affected file
     */
    public Iterable<? extends Location> getAffectedLocations() {
        return locations.stream()
                .filter(l -> getFileName().equals(l.getFileName()))
                .toList();
    }

    /**
     * Returns the primary (first) location of this issue.
     *
     * @return the primary location
     */
    public Location getPrimaryLocation() {
        return locations.get(0);
    }

    /**
     * Returns whether this issue has additional file locations beyond the primary one.
     *
     * @return {@code true} if this issue has additional file locations, {@code false} otherwise
     */
    public boolean hasAdditionalLocations() {
        return locations.size() > 1;
    }

    /**
     * Returns whether the locations of this issue contain the specified line.
     * If this issue has no lines defined, then this method will return {@code true}.
     *
     * @param line
     *         the line to check
     *
     * @return {@code true} if the specified line is within the line ranges of this issue, {@code false} otherwise
     */
    public boolean affectsLine(final int line) {
        if (getLineStart() == 0 || line == 0) {
            return true; // the whole file is marked, so every line is affected
        }
        if (getLineStart() <= line && line <= lineEnd) {
            return true; // the line is within the primary range of this issue
        }
        for (Location location : getAffectedLocations()) {
            if (location.contains(line)) {
                return true; // the line is within an additional line range of this issue
            }
        }
        return false;
    }

    /**
     * Returns the first column of this issue (columns start at 1, 0 indicates the whole line).
     *
     * @return the first column
     */
    public int getColumnStart() {
        return getPrimaryLocation().getColumnStart();
    }

    /**
     * Returns the last column of this issue (columns start at 1).
     *
     * @return the last column
     */
    public int getColumnEnd() {
        return getPrimaryLocation().getColumnEnd();
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
     * Returns the tree-string containing the name of the package or name space (or similar concept) that contains this
     * issue.
     *
     * @return the package name
     */
    TreeString getPackageNameTreeString() {
        return packageName;
    }

    /**
     * Sets the name of the package or name space (or similar concept) that contains this issue.
     *
     * @param packageName
     *         the name of the package
     */
    void setPackageName(final TreeString packageName) {
        this.packageName = packageName;
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
     * Sets the name of the module or project (or similar concept) that contains this issue.
     *
     * @param moduleName
     *         the module name to set
     */
    void setModuleName(@CheckForNull final String moduleName) {
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
     * @return the ID of the origin
     */
    public String getOrigin() {
        return origin;
    }

    boolean hasOrigin() {
        return StringUtils.isNoneBlank(origin);
    }

    /**
     * Returns the name of the tool that did report this issue.
     *
     * @return the name of the origin
     */
    public String getOriginName() {
        return originName;
    }

    /**
     * Sets the ID of the tool that did report this issue.
     *
     * @param origin
     *         the origin
     */
    void setOrigin(final String origin) {
        Ensure.that(origin).isNotBlank("Issue origin ID '%s' must be not blank (%s)", id, toString());

        this.origin = origin.intern();
    }

    /**
     * Sets the ID and the name of the tool that did report this issue.
     *
     * @param originId
     *         the ID of the origin
     * @param name
     *         the name of the origin
     */
    void setOrigin(final String originId, final String name) {
        setOrigin(originId);

        Ensure.that(name).isNotBlank("Issue origin name '%s' must be not blank (%s)", name, toString());

        this.originName = name.intern();
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
     * Sets a reference to the execution of the static analysis tool (build ID, timestamp, etc.). This property should
     * not be set by parsers as it is overwritten by the {@link IssueDifference differencing engine} while computing new
     * and fixed issues.
     *
     * @param reference
     *         the reference
     */
    void setReference(@CheckForNull final String reference) {
        this.reference = stripToEmpty(reference);
    }

    /**
     * Returns the fingerprint for this issue. Used to decide if two issues are equal even if the equals method returns
     * {@code false} since some properties differ due to code refactorings. The fingerprint is created by
     * analyzing the content of the affected file.
     *
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
     * Sets the fingerprint for this issue to the given value.
     *
     * @param fingerprint
     *         the fingerprint to set
     *
     * @see #getFingerprint()
     */
    void setFingerprint(@CheckForNull final String fingerprint) {
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
     * Returns whether this issue affects a code line that has been modified recently.
     *
     * @return {@code true} if this issue affects a code line that has been modified recently.
     * @see IssuesInModifiedCodeMarker
     */
    public boolean isPartOfModifiedCode() {
        return partOfModifiedCode;
    }

    /**
     * Marks the issue as part of a source control diff.
     */
    void markAsPartOfModifiedCode() {
        partOfModifiedCode = true;
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
    public boolean equals(@CheckForNull final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        var issue = (Issue) o;

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
        if (!description.equals(issue.description)) {
            return false;
        }
        if (additionalProperties != null ? !additionalProperties.equals(issue.additionalProperties)
                : issue.additionalProperties != null) {
            return false;
        }
        if (!origin.equals(issue.origin)) {
            return false;
        }
        if (!originName.equals(issue.originName)) {
            return false;
        }
        if (!moduleName.equals(issue.moduleName)) {
            return false;
        }
        if (!packageName.equals(issue.packageName)) {
            return false;
        }
        return Objects.equals(locations, issue.locations);
    }

    @Override
    public int hashCode() {
        int result = category.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + severity.hashCode();
        result = 31 * result + message.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + (additionalProperties == null ? 0 : additionalProperties.hashCode());
        result = 31 * result + origin.hashCode();
        result = 31 * result + originName.hashCode();
        result = 31 * result + moduleName.hashCode();
        result = 31 * result + packageName.hashCode();
        result = 31 * result + (locations == null ? 0 : locations.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "%s%s(%d,%d): %s: %s: %s",
                isPartOfModifiedCode() ? "*" : StringUtils.EMPTY, getBaseName(),
                getLineStart(), getColumnStart(), type, category, message);
    }
}
