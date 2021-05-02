package edu.hm.hafner.analysis; // NOPMD

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import com.google.errorprone.annotations.FormatMethod;

import edu.hm.hafner.util.Ensure;
import edu.hm.hafner.util.NoSuchElementException;
import edu.hm.hafner.util.PathUtil;
import edu.hm.hafner.util.TreeString;
import edu.hm.hafner.util.TreeStringBuilder;
import edu.hm.hafner.util.VisibleForTesting;
import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * A report contains a set of unique {@link Issue issues}: it contains no duplicate elements, i.e. it models the
 * mathematical <i>set</i> abstraction. This report provides a <i>total ordering</i> on its elements. I.e., the issues
 * in this report are ordered by their index: the first added issue is at position 0, the second added issues is at
 * position 1, and so on.
 * <p>
 * Additionally, this report provides methods to find and filter issues based on different properties. In order to
 * create issues use the provided {@link IssueBuilder builder} class.
 * </p>
 *
 * @author Ullrich Hafner
 */
@SuppressWarnings({"PMD.ExcessivePublicCount", "PMD.ExcessiveClassLength", "PMD.GodClass", "PMD.CyclomaticComplexity", "checkstyle:ClassFanOutComplexity"})
// TODO: provide a readResolve method to check the instance and improve the performance (TreeString, etc.)
public class Report implements Iterable<Issue>, Serializable {
    private static final long serialVersionUID = 4L; // release 10.0.0

    @VisibleForTesting
    static final String DEFAULT_ID = "-";

    private String id;
    private String name;
    private String originReportFile;

    private List<Report> subReports = new ArrayList<>(); // almost final

    private Set<Issue> elements = new LinkedHashSet<>();
    private List<String> infoMessages = new ArrayList<>();
    private List<String> errorMessages = new ArrayList<>();
    private Map<String, Integer> countersByKey = new HashMap<>();

    @CheckForNull @SuppressWarnings("all")
    private transient Set<String> fileNames; // Not needed anymore since  10.0.0
    @CheckForNull @SuppressWarnings("all")
    private transient Map<String, String> namesByOrigin; // Not needed anymore since  10.0.0

    private int duplicatesSize = 0;

    /**
     * Creates an empty {@link Report}.
     */
    public Report() {
        this(DEFAULT_ID, DEFAULT_ID, DEFAULT_ID);
    }

    /**
     * Creates an empty {@link Report} with the specified ID and name.
     *
     * @param id
     *         the ID of the report
     * @param name
     *         a human readable name for the report
     */
    public Report(final String id, final String name) {
        this(id, name, DEFAULT_ID);
    }

    /**
     * Creates an empty {@link Report} with the specified ID and name. Link the report with the specified source file
     * that is the origin of the issues.
     *
     * @param id
     *         the ID of the report
     * @param name
     *         a human readable name for the report
     * @param originReportFile
     *         the specified source file * that is the origin of the issues.
     */
    public Report(final String id, final String name, final String originReportFile) {
        this.id = id;
        this.name = name;
        this.originReportFile = originReportFile;
    }

    public String getId() {
        return id;
    }

    private boolean hasId() {
        return !DEFAULT_ID.equals(id) && StringUtils.isNoneBlank(id);
    }


    /**
     * Returns the effective ID of this report. This ID is the unique ID of all containing sub-reports. If this ID is
     * not unique, then the {@link #DEFAULT_ID} will be returned.
     *
     * @return the effective ID of all sub-reports
     */
    public String getEffectiveId() {
        Set<String> ids = subReports.stream().map(Report::getEffectiveId).collect(Collectors.toSet());
        ids.add(getId());
        ids.remove(DEFAULT_ID);

        if (ids.size() == 1) {
            return ids.iterator().next();
        }
        return getId();
    }

    public String getName() {
        return name;
    }

    /**
     * Sets the origin of all issues in this report. Calling this method will associate all containing issues and
     * issues in sub-reports using the specified ID and name.
     *
     * @param originId
     *         the ID of the report
     * @param originName
     *         a human readable name for the report
     */
    public void setOrigin(final String originId, final String originName) {
        Ensure.that(originId).isNotBlank("Issue origin ID '%s' must be not blank (%s)", originId, toString());
        Ensure.that(originName).isNotBlank("Issue origin name '%s' must be not blank (%s)", originName, toString());

        id = originId;
        name = originName;

        stream().forEach(issue -> issue.setOrigin(originId, originName));
    }

    /**
     * Returns the effective ID of this report. This ID is the unique ID of all containing sub-reports. If this ID is
     * not unique, then the {@link #DEFAULT_ID} will be returned.
     *
     * @return the effective ID of all sub-reports
     */
    public String getEffectiveName() {
        Set<String> names = subReports.stream().map(Report::getEffectiveName).collect(Collectors.toSet());
        names.add(getName());
        names.remove(DEFAULT_ID);

        if (names.size() == 1) {
            return names.iterator().next();
        }
        return getName();
    }

    public String getOriginReportFile() {
        return originReportFile;
    }

    /*
     * Stores the name of the report file that is the origin of the contained issues.
     *
     * @param fileName
     *         the report file name to add
     */
    public void setOriginReportFile(final String originReportFile) {
        this.originReportFile = new PathUtil().getAbsolutePath(originReportFile);
    }

    /**
     * Returns the names of all report files that are the origin for the issues of this {@link Report} (and all
     * contained sub-reports).
     *
     * @return the names of all report files
     */
    public Set<String> getOriginReportFiles() {
        Set<String> files = subReports.stream()
                .map(Report::getOriginReportFiles)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        files.add(getOriginReportFile());
        files.remove(DEFAULT_ID);
        return files;
    }

    /**
     * Creates a new {@link Report} that is an aggregation of the specified {@link Report reports}. The created report
     * will contain the issues of all specified reports, in the same order. The properties of the specified reports will
     * also be copied.
     *
     * @param reports
     *         the reports to append
     *
     * @see #copyIssuesAndProperties(Report, Report)
     */
    @SuppressWarnings("ConstructorLeaksThis")
    public Report(final Report... reports) {
        this();

        Ensure.that(reports).isNotEmpty("No reports given.");

        subReports.addAll(Arrays.asList(reports));
    }

    /**
     * Creates a new {@link Report} that is an aggregation of the specified {@link Report reports}. The created report
     * will contain the issues of all specified reports, in the same order. The properties of the specified reports will
     * also be copied.
     *
     * @param reports
     *         the initial set of issues
     *
     * @see #copyIssuesAndProperties(Report, Report)
     */
    @SuppressWarnings("ConstructorLeaksThis")
    public Report(final Collection<? extends Report> reports) {
        this();

        Ensure.that(reports).isNotEmpty("No reports given.");

        subReports.addAll(reports);
    }

    /**
     * Appends the specified issue to the end of this report. Duplicates will be skipped (the number of skipped elements
     * is available using the method {@link #getDuplicatesSize()}.
     *
     * @param issue
     *         the issue to append
     *
     * @return this
     */
    public Report add(final Issue issue) {
        if (hasId() && !issue.hasOrigin()) {
            issue.setOrigin(id, name);
        }
        if (contains(issue)) {
            duplicatesSize++; // elements are marked as duplicate if the fingerprint is different
        }
        else {
            elements.add(issue);
        }

        return this;
    }

    /**
     * Appends all of the specified issues to the end of this report, preserving the order of the array elements.
     * Duplicates will be skipped (the number of skipped elements is available using the method {@link
     * #getDuplicatesSize()}.
     *
     * @param issue
     *         the first issue to append
     * @param additionalIssues
     *         the additional issue to append
     *
     * @return this
     * @see #add(Issue)
     */
    public Report addAll(final Issue issue, final Issue... additionalIssues) {
        add(issue);
        for (Issue additional : additionalIssues) {
            add(additional);
        }
        return this;
    }

    /**
     * Appends all of the specified issues to the end of this report, preserving the order of the array elements.
     * Duplicates will be skipped (the number of skipped elements is available using the method {@link
     * #getDuplicatesSize()}.
     *
     * @param issues
     *         the issues to append
     *
     * @return this
     * @see #add(Issue)
     */
    public Report addAll(final Collection<? extends Issue> issues) {
        for (Issue additional : issues) {
            add(additional);
        }
        return this;
    }

    /**
     * Appends the specified {@link Report reports} to this report. This report will then contain the issues of all
     * specified reports, in the same order. The properties of the specified reports will also be copied.
     *
     * @param reports
     *         the reports to append
     *
     * @return this
     * @see #copyIssuesAndProperties(Report, Report)
     */
    public Report addAll(final Report... reports) {
        Ensure.that(reports).isNotEmpty("No reports given.");

        for (Report report : reports) {
            Report copyWithoutDuplicates = report.copyEmptyInstance();
            for (Issue issue : report) {
                if (contains(issue)) {
                    duplicatesSize++; // elements are marked as duplicate if the fingerprint is different
                }
                else {
                    copyWithoutDuplicates.add(issue);
                }
            }
            subReports.add(copyWithoutDuplicates);
        }

        return this;
    }

    private boolean contains(final Issue issue) {
        if (elements.contains(issue)) {
            return true;
        }
        return subReports.stream().map(r -> r.contains(issue)).reduce(Boolean::logicalOr).orElse(false);
    }


    /**
     * Called after de-serialization to improve the memory usage and to initialize fields that have been introduced
     * after the first release.
     *
     * @return this
     */
    @SuppressFBWarnings(value = "RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE", justification = "Deserialization of instances that do not have all fields yet")
    protected Object readResolve() {
        if (countersByKey == null) {
            countersByKey = new HashMap<>();
        }
        if (subReports == null) { // release 10.0.0
            subReports = new ArrayList<>();
            id = DEFAULT_ID;
            name = DEFAULT_ID;
            originReportFile = DEFAULT_ID;
        }
        return this;
    }

    /**
     * Removes the issue with the specified ID. Note that the number of reported duplicates is not affected by calling
     * this method.
     *
     * @param issueId
     *         the ID of the issue
     *
     * @return the removed element
     * @throws NoSuchElementException
     *         if there is no such issue found
     */
    Issue remove(final UUID issueId) {
        Optional<Issue> issue = removeIfContained(issueId);
        if (issue.isPresent()) {
            return issue.get();
        }
        throw new NoSuchElementException("No removed found with id %s.", issueId);
    }

    private Optional<Issue> removeIfContained(final UUID issueId) {
        Optional<Issue> issue = find(issueId);
        issue.ifPresent(value -> elements.remove(value));
        if (issue.isPresent()) {
            return issue;
        }

        for (Report subReport : subReports) {
            issue = subReport.removeIfContained(issueId);
            if (issue.isPresent()) {
                return issue;
            }
        }

        return Optional.empty();
    }

    private Optional<Issue> find(final UUID issueId) {
        return elements.stream()
                .filter(issue -> issue.getId().equals(issueId))
                .findAny();
    }

    /**
     * Returns the issue with the specified ID.
     *
     * @param issueId
     *         the ID of the issue
     *
     * @return the found issue
     * @throws NoSuchElementException
     *         if there is no such issue found
     */
    public Issue findById(final UUID issueId) {
        return stream().filter(issue -> issue.getId().equals(issueId))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("No issue found with id %s.", issueId));
    }

    /**
     * Finds all issues that match the specified criterion.
     *
     * @param criterion
     *         the filter criterion
     *
     * @return the found issues
     */
    public Set<Issue> findByProperty(final Predicate<? super Issue> criterion) {
        return filterElements(criterion).collect(Collectors.toSet());
    }

    /**
     * Finds all issues that match the specified criterion.
     *
     * @param criterion
     *         the filter criterion
     *
     * @return the found issues
     */
    public Report filter(final Predicate<? super Issue> criterion) {
        Report filtered = copyEmptyInstance();
        filtered.addAll(elements.stream().filter(criterion).collect(Collectors.toList()));
        for (Report subReport : subReports) {
            filtered.addAll(subReport.filter(criterion));
        }
        return filtered;
    }

    private Stream<Issue> filterElements(final Predicate<? super Issue> criterion) {
        return stream().filter(criterion);
    }

    @NonNull
    @Override
    public Iterator<Issue> iterator() {
        return stream().iterator();
    }

    /**
     * Creates a new sequential {@code Stream} of {@link Issue} instances.
     *
     * @return a new {@code Stream}
     */
    public Stream<Issue> stream() {
        return Stream.concat(elements.stream(), subReports.stream().flatMap(Report::stream));
    }

    /**
     * Returns the issues in this report. This will include the issues of any sub-reports.
     *
     * @return all issues in this report
     */
    public Collection<Issue> get() {
        return stream().collect(Collectors.toList());
    }

    /**
     * Returns the number of issues in this container.
     *
     * @return total number of issues
     */
    public int size() {
        return elements.size() + subReports.stream().mapToInt(Report::size).sum();
    }

    /**
     * Returns whether this report is empty.
     *
     * @return {@code true} if this report is empty, {@code false} otherwise
     * @see #isNotEmpty()
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Returns whether this report is not empty.
     *
     * @return {@code true} if this report is not empty, {@code false} otherwise
     * @see #isEmpty()
     */
    public boolean isNotEmpty() {
        return !isEmpty();
    }

    /**
     * Returns the number of issues in this report.
     *
     * @return total number of issues
     */
    public int getSize() {
        return size();
    }

    /**
     * Returns the number of duplicates. Every issue that has been added to this report, but already is part of this
     * report (based on {@link #equals(Object)}) is counted as a duplicate. Duplicates are not stored in this report.
     *
     * @return total number of duplicates
     */
    public int getDuplicatesSize() {
        return duplicatesSize + subReports.stream().mapToInt(Report::getDuplicatesSize).sum();
    }

    /**
     * Returns the number of issues with the specified {@code severity}.
     *
     * @param severity
     *         the severity of the issues
     *
     * @return total number of issues
     */
    public int getSizeOf(final String severity) {
        return getSizeOf(Severity.valueOf(severity));
    }

    /**
     * Returns the number of issues with the specified {@link Severity}.
     *
     * @param severity
     *         the severity of the issues
     *
     * @return total number of issues
     */
    public int getSizeOf(final Severity severity) {
        return stream().filter(issue -> issue.getSeverity().equals(severity)).mapToInt(e -> 1).sum();
    }

    /**
     * Returns the issue with the specified index.
     *
     * @param index
     *         the index
     *
     * @return the issue at the specified index
     * @throws IndexOutOfBoundsException
     *         if there is no element for the given index
     */
    public Issue get(final int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException("No such index " + index + " in " + this);
        }
        Iterator<Issue> all = iterator();
        for (int i = 0; i < index; i++) {
            all.next(); // skip this element
        }
        return all.next();
    }

    @Override
    public String toString() {
        return String.format("%s (%s): %d issues (%d duplicates)", getEffectiveName(), getEffectiveId(),
                size(), getDuplicatesSize());
    }

    /**
     * Prints all issues of the report.
     *
     * @param issuePrinter
     *         prints a summary of an {@link Issue}
     */
    public void print(final IssuePrinter issuePrinter) {
        forEach(issuePrinter::print);
    }

    /**
     * Returns the affected modules for all issues.
     *
     * @return the affected modules
     */
    public Set<String> getModules() {
        return getProperties(Issue::getModuleName);
    }

    /**
     * Returns whether this report contains affected files from more than one module.
     *
     * @return {@code true} if the number of modules is greater than 1, {@code false} otherwise
     */
    public boolean hasModules() {
        return hasProperty(getModules());
    }

    private boolean hasProperty(final Set<String> propertyValues) {
        return propertyValues.size() > 1 || hasMeaningfulValues(propertyValues);
    }

    private boolean hasMeaningfulValues(final Set<String> propertyValue) {
        return propertyValue.size() == 1 && !propertyValue.contains(DEFAULT_ID) && !propertyValue.contains("");
    }

    /**
     * Returns the affected packages for all issues.
     *
     * @return the affected packages
     */
    public Set<String> getPackages() {
        return getProperties(Issue::getPackageName);
    }

    /**
     * Returns whether this report contains affected files from more than one package.
     *
     * @return {@code true} if the number of packages is greater than 1, {@code false} otherwise
     */
    public boolean hasPackages() {
        return hasProperty(getPackages());
    }

    /**
     * Returns the folders for all affected files of the issues.
     *
     * @return the affected packages
     */
    public Set<String> getFolders() {
        return getProperties(Issue::getFolder);
    }

    /**
     * Returns whether this report contains more than one folder with affected files.
     *
     * @return {@code true} if the number of folders is greater than 1, {@code false} otherwise
     */
    public boolean hasFolders() {
        Set<String> packages = getPackages();
        packages.remove(DEFAULT_ID);

        return hasProperty(getFolders()) && packages.isEmpty();
    }

    /**
     * Returns the absolute paths of the affected files for all issues.
     *
     * @return the affected files
     */
    public Set<String> getAbsolutePaths() {
        return getProperties(Issue::getAbsolutePath);
    }

    /**
     * Returns the affected files for all issues.
     *
     * @return the affected files
     */
    public Set<String> getFiles() {
        return getProperties(Issue::getFileName);
    }

    /**
     * Returns whether this report contains more than one affected file.
     *
     * @return {@code true} if the number of affected files is greater than 1, {@code false} otherwise
     */
    public boolean hasFiles() {
        return hasProperty(getFiles());
    }

    /**
     * Returns the used categories for all issues.
     *
     * @return the used categories
     */
    public Set<String> getCategories() {
        return getProperties(Issue::getCategory);
    }

    /**
     * Returns whether this report contains issues with different categories.
     *
     * @return {@code true} if the number of categories is greater than 1, {@code false} otherwise
     */
    public boolean hasCategories() {
        return hasProperty(getCategories());
    }

    /**
     * Returns the used types for all issues.
     *
     * @return the used types
     */
    public Set<String> getTypes() {
        return getProperties(Issue::getType);
    }

    /**
     * Returns whether this report contains issues with different types.
     *
     * @return {@code true} if the number of types is greater than 1, {@code false} otherwise
     */
    public boolean hasTypes() {
        return hasProperty(getTypes());
    }

    /**
     * Returns the names of the tools that did report the issues.
     *
     * @return the tools
     */
    public Set<String> getTools() {
        return getProperties(Issue::getOrigin);
    }

    /**
     * Returns whether this report contains issues created by different tools.
     *
     * @return {@code true} if the number of tools is greater than 1, {@code false} otherwise
     */
    public boolean hasTools() {
        return hasProperty(getTools());
    }

    /**
     * Returns the severities of all issues.
     *
     * @return the severities
     */
    public Set<Severity> getSeverities() {
        return getProperties(Issue::getSeverity);
    }

    /**
     * Returns whether this report contains issues of different severities.
     *
     * @return {@code true} if the number of severities is greater than 1, {@code false} otherwise
     */
    public boolean hasSeverities() {
        return getSeverities().size() > 1;
    }

    /**
     * Returns the different values for a given property for all issues.
     *
     * @param propertiesMapper
     *         the properties mapper that selects the property
     * @param <T>
     *         type of the property
     *
     * @return the set of different values
     * @see #getFiles()
     */
    public <T> Set<T> getProperties(final Function<? super Issue, T> propertiesMapper) {
        return stream().map(propertiesMapper).collect(Collectors.toSet());
    }

    /**
     * Returns the number of occurrences for every existing value of a given property for all issues.
     *
     * @param propertiesMapper
     *         the properties mapper that selects the property to evaluate
     * @param <T>
     *         type of the property
     *
     * @return a mapping of: property value to the number of issues for that value
     * @see #getProperties(Function)
     */
    public <T> Map<T, Integer> getPropertyCount(final Function<? super Issue, T> propertiesMapper) {
        return stream().collect(
                Collectors.groupingBy(propertiesMapper, Collectors.reducing(0, issue -> 1, Integer::sum)));
    }

    /**
     * Groups issues by a specified property. Returns the results as a mapping of property values to a new set of {@link
     * Report} for this value.
     *
     * @param propertyName
     *         the property to  that selects the property to evaluate
     *
     * @return a mapping of: property value to the number of issues for that value
     * @see #getProperties(Function)
     */
    public Map<String, Report> groupByProperty(final String propertyName) {
        Map<String, List<Issue>> issues = stream()
                .collect(Collectors.groupingBy(Issue.getPropertyValueGetter(propertyName)));

        return issues.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> {
                            Report report = new Report();
                            report.addAll(e.getValue());
                            return report;
                        }));
    }

    /**
     * Returns a shallow copy of this issue container.
     *
     * @return a new issue container that contains the same elements in the same order
     */
    public Report copy() {
        Report copied = new Report();
        copyIssuesAndProperties(this, copied);
        return copied;
    }

    private void copyIssuesAndProperties(final Report source, final Report destination) {
        destination.addAll(source.elements);
        for (Report subReport : subReports) {
            destination.addAll(subReport.copy());
        }
        copyProperties(source, destination);
    }

    private void copyProperties(final Report source, final Report destination) {
        destination.id = source.id;
        destination.name = source.name;
        destination.originReportFile = source.originReportFile;
        destination.duplicatesSize += source.duplicatesSize;
        destination.infoMessages.addAll(source.infoMessages);
        destination.errorMessages.addAll(source.errorMessages);
        destination.countersByKey = Stream.concat(
                destination.countersByKey.entrySet().stream(), source.countersByKey.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Integer::sum));
    }

    /**
     * Returns a new empty issue container with the same properties as this container. The new issue container is empty
     * and does not contain issues.
     *
     * @return a new issue container that contains the same properties but no issues
     */
    public Report copyEmptyInstance() {
        Report empty = new Report();
        copyProperties(this, empty);
        return empty;
    }

    /**
     * Logs the specified information message. Use this method to log any useful information when composing this
     * report.
     *
     * @param format
     *         A <a href="../util/Formatter.html#syntax">format string</a>
     * @param args
     *         Arguments referenced by the format specifiers in the format string.  If there are more arguments than
     *         format specifiers, the extra arguments are ignored.  The number of arguments is variable and may be
     *         zero.
     *
     * @see #getInfoMessages()
     */
    @FormatMethod
    public void logInfo(final String format, final Object... args) {
        infoMessages.add(String.format(format, args));
    }

    /**
     * Logs the specified error message. Use this method to log any error when composing this report.
     *
     * @param format
     *         A <a href="../util/Formatter.html#syntax">format string</a>
     * @param args
     *         Arguments referenced by the format specifiers in the format string.  If there are more arguments than
     *         format specifiers, the extra arguments are ignored.  The number of arguments is variable and may be
     *         zero.
     *
     * @see #getInfoMessages()
     */
    @FormatMethod
    public void logError(final String format, final Object... args) {
        errorMessages.add(String.format(format, args));
    }

    /**
     * Logs the specified exception. Use this method to log any exception when composing this report.
     *
     * @param exception
     *         the exception to log
     * @param format
     *         A <a href="../util/Formatter.html#syntax">format string</a>
     * @param args
     *         Arguments referenced by the format specifiers in the format string.  If there are more arguments than
     *         format specifiers, the extra arguments are ignored.  The number of arguments is variable and may be
     *         zero.
     *
     * @see #getInfoMessages()
     */
    @FormatMethod
    public void logException(final Exception exception, final String format, final Object... args) {
        logError(format, args);
        Collections.addAll(errorMessages, ExceptionUtils.getRootCauseStackTrace(exception));
    }

    /**
     * Returns the info messages that have been reported since the creation of this set of issues.
     *
     * @return the info messages
     */
    public List<String> getInfoMessages() {
        return mergeMessages(infoMessages, Report::getInfoMessages);
    }

    /**
     * Returns the error messages that have been reported since the creation of this set of issues.
     *
     * @return the error messages
     */
    public List<String> getErrorMessages() {
        return mergeMessages(errorMessages, Report::getErrorMessages);
    }

    private List<String> mergeMessages(final List<String> thisMessages,
            final Function<Report, List<String>> sumMessages) {
        return Stream.concat(
                subReports.stream().map(sumMessages).flatMap(Collection::stream),
                thisMessages.stream())
                .collect(Collectors.toList());
    }

    /**
     * Returns whether error messages have been reported.
     *
     * @return {@code true} if there are error messages, {@code false} otherwise
     */
    public boolean hasErrors() {
        return !getErrorMessages().isEmpty();
    }

    @Override
    @SuppressWarnings("PMD.NPathComplexity")
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Report report = (Report) o;

        if (duplicatesSize != report.duplicatesSize) {
            return false;
        }
        if (!id.equals(report.id)) {
            return false;
        }
        if (!name.equals(report.name)) {
            return false;
        }
        if (!originReportFile.equals(report.originReportFile)) {
            return false;
        }
        if (!subReports.equals(report.subReports)) {
            return false;
        }
        if (!elements.equals(report.elements)) {
            return false;
        }
        if (!infoMessages.equals(report.infoMessages)) {
            return false;
        }
        if (!errorMessages.equals(report.errorMessages)) {
            return false;
        }
        return countersByKey.equals(report.countersByKey);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + originReportFile.hashCode();
        result = 31 * result + subReports.hashCode();
        result = 31 * result + elements.hashCode();
        result = 31 * result + infoMessages.hashCode();
        result = 31 * result + errorMessages.hashCode();
        result = 31 * result + countersByKey.hashCode();
        result = 31 * result + duplicatesSize;
        return result;
    }

    private void writeObject(final ObjectOutputStream output) throws IOException {
        output.writeInt(elements.size());
        writeIssues(output);

        output.writeObject(infoMessages);
        output.writeObject(errorMessages);
        output.writeObject(countersByKey);

        output.writeInt(duplicatesSize);

        output.writeUTF(id);
        output.writeUTF(name);
        output.writeUTF(originReportFile);
        output.writeInt(subReports.size());
        for (Report subReport : subReports) {
            output.writeObject(subReport);
        }

    }

    private void writeIssues(final ObjectOutputStream output) throws IOException {
        for (Issue issue : elements) {
            output.writeUTF(issue.getPath());
            output.writeUTF(issue.getFileName());
            output.writeInt(issue.getLineStart());
            output.writeInt(issue.getLineEnd());
            output.writeInt(issue.getColumnStart());
            output.writeInt(issue.getColumnEnd());
            output.writeObject(issue.getLineRanges());
            output.writeUTF(issue.getCategory());
            output.writeUTF(issue.getType());
            output.writeUTF(issue.getPackageName());
            output.writeUTF(issue.getModuleName());
            output.writeUTF(issue.getSeverity().getName());
            writeLongString(output, issue.getMessage());
            writeLongString(output, issue.getDescription());
            output.writeUTF(issue.getOrigin());
            output.writeUTF(issue.getOriginName());
            output.writeUTF(issue.getReference());
            output.writeUTF(issue.getFingerprint());
            output.writeObject(issue.getAdditionalProperties());
            output.writeObject(issue.getId());
        }
    }

    private void writeLongString(final ObjectOutputStream output, final String value) throws IOException {
        output.writeInt(value.length());
        output.writeChars(value);
    }

    @SuppressWarnings("unchecked")
    private void readObject(final ObjectInputStream input) throws IOException, ClassNotFoundException {
        elements = new LinkedHashSet<>();
        readIssues(input, input.readInt());

        infoMessages = (List<String>) input.readObject();
        errorMessages = (List<String>) input.readObject();
        countersByKey = (Map<String, Integer>) input.readObject();

        duplicatesSize = input.readInt();

        id = input.readUTF();
        name = input.readUTF();
        originReportFile = input.readUTF();
        subReports = new ArrayList<>();

        int subReportCount = input.readInt();
        for (int i = 0; i < subReportCount; i++) {
            Report subReport = (Report) input.readObject();
            subReports.add(subReport);
        }
    }

    @SuppressFBWarnings("OBJECT_DESERIALIZATION")
    private void readIssues(final ObjectInputStream input, final int size) throws IOException, ClassNotFoundException {
        final TreeStringBuilder builder = new TreeStringBuilder();
        for (int i = 0; i < size; i++) {
            String path = input.readUTF();
            TreeString fileName = builder.intern(input.readUTF());
            int lineStart = input.readInt();
            int lineEnd = input.readInt();
            int columnStart = input.readInt();
            int columnEnd = input.readInt();
            LineRangeList lineRanges = (LineRangeList) input.readObject();
            String category = input.readUTF();
            String type = input.readUTF();
            TreeString packageName = builder.intern(input.readUTF());
            String moduleName = input.readUTF();
            Severity severity = Severity.valueOf(input.readUTF());
            TreeString message = builder.intern(readLongString(input));
            String description = readLongString(input);
            String origin = input.readUTF();
            String originName = input.readUTF();
            String reference = input.readUTF();
            String fingerprint = input.readUTF();
            Serializable additionalProperties = (Serializable) input.readObject();
            UUID uuid = (UUID) input.readObject();

            Issue issue = new Issue(path, fileName,
                    lineStart, lineEnd, columnStart, columnEnd,
                    lineRanges, category, type, packageName, moduleName,
                    severity, message, description,
                    origin, originName, reference, fingerprint, additionalProperties, uuid);

            elements.add(issue);
        }
        builder.dedup();
    }

    private String readLongString(final ObjectInputStream input) throws IOException {
        int messageLength = input.readInt();
        if (messageLength < 0) {
            throw new IllegalStateException("Can't read requested number of characters " + messageLength);
        }
        char[] chars = new char[messageLength];
        for (int j = 0; j < chars.length; j++) {
            chars[j] = input.readChar();
        }
        return new String(chars);
    }

    /**
     * Returns a human readable name for the specified {@code origin} of this report.
     *
     * @param origin
     *         the origin to get the human readable name for
     *
     * @return the name, or an empty string if no such name has been set
     */
    public String getNameOfOrigin(final String origin) {
        if (getId().equals(origin)) {
            return getName();
        }

        for (Report subReport : subReports) {
            String nameOfSubReport = subReport.getNameOfOrigin(origin);
            if (!DEFAULT_ID.equals(nameOfSubReport)) {
                return nameOfSubReport;
            }
        }
        return DEFAULT_ID;
    }

    /**
     * Sets the specified custom counter for this report.
     *
     * @param key
     *         the unique key for this counter
     * @param value
     *         the value to set
     */
    public void setCounter(final String key, final int value) {
        countersByKey.put(Objects.requireNonNull(key), value);
    }

    /**
     * Returns the specified custom counter of this report.
     *
     * @param key
     *         the unique key for this counter
     *
     * @return the value of the specified counter, or 0 if the counter has not been set or is undefined
     */
    public int getCounter(final String key) {
        return countersByKey.getOrDefault(key, 0) + subReports.stream().mapToInt(r -> r.getCounter(key)).sum();
    }

    /**
     * Returns whether the specified custom counter of this report is defined.
     *
     * @param key
     *         the unique key for this counter
     *
     * @return {@code true} if the counter has been set, {@code false} otherwise
     */
    public boolean hasCounter(final String key) {
        return countersByKey.containsKey(key);
    }

    /**
     * Prints a summary of an {@link Issue}.
     */
    public interface IssuePrinter {
        /**
         * Prints the specified issue.
         *
         * @param issue
         *         the issue to print
         */
        void print(Issue issue);
    }

    /**
     * Prints issues to the "standard" output stream.
     */
    public static class StandardOutputPrinter implements IssuePrinter {
        private final PrintStream printStream;

        /**
         * Creates a new printer that prints to the "standard" output stream.
         */
        public StandardOutputPrinter() {
            this(System.out);
        }

        @VisibleForTesting
        StandardOutputPrinter(final PrintStream printStream) {
            this.printStream = printStream;
        }

        @Override
        public void print(final Issue issue) {
            printStream.println(issue.toString());
        }
    }

    /**
     * Builds a combined filter based on several include and exclude filters.
     *
     * @author Raphael Furch
     */
    public static class IssueFilterBuilder {
        private final Collection<Predicate<Issue>> includeFilters = new ArrayList<>();
        private final Collection<Predicate<Issue>> excludeFilters = new ArrayList<>();

        /** Type of the filter: include or exclude elements. */
        enum FilterType {
            INCLUDE,
            EXCLUDE
        }

        /**
         * Adds a new filter for each patterns string. Adds the filter either to the include or exclude list.
         *
         * @param patterns
         *         filter patterns.
         * @param propertyToFilter
         *         Function to get a string from Issue for patterns
         * @param type
         *         type of the filter
         */
        private void addNewFilter(final Collection<String> patterns, final Function<Issue, String> propertyToFilter,
                final FilterType type) {

            Collection<Predicate<Issue>> filters = new ArrayList<>();
            for (String pattern : patterns) {
                filters.add(issueToFilter -> Pattern.compile(pattern, Pattern.DOTALL)
                        .matcher(propertyToFilter.apply(issueToFilter)).find() == isIncludeFilter(type));
            }

            if (isIncludeFilter(type)) {
                includeFilters.addAll(filters);
            }
            else {
                excludeFilters.addAll(filters);
            }
        }

        private boolean isIncludeFilter(final FilterType type) {
            return type == FilterType.INCLUDE;
        }

        /**
         * Create a IssueFilter. Combine by default all includes with or and all excludes with and.
         *
         * @return a IssueFilter which has all added filter as filter criteria.
         */
        @SuppressWarnings("NoFunctionalReturnType")
        public Predicate<Issue> build() {
            return includeFilters.stream().reduce(Predicate::or).orElse(issue -> true)
                    .and(excludeFilters.stream().reduce(Predicate::and).orElse(issue -> true));
        }

        //<editor-fold desc="File name">

        /**
         * Add a new filter.
         *
         * @param pattern
         *         pattern
         *
         * @return this.
         */
        public IssueFilterBuilder setIncludeFileNameFilter(final Collection<String> pattern) {
            addNewFilter(pattern, Issue::getFileName, FilterType.INCLUDE);
            return this;
        }

        /**
         * Add a new filter.
         *
         * @param pattern
         *         pattern
         *
         * @return this.
         */
        public IssueFilterBuilder setIncludeFileNameFilter(final String... pattern) {
            return setIncludeFileNameFilter(Arrays.asList(pattern));
        }

        /**
         * Add a new filter.
         *
         * @param pattern
         *         pattern
         *
         * @return this.
         */
        public IssueFilterBuilder setExcludeFileNameFilter(final Collection<String> pattern) {
            addNewFilter(pattern, Issue::getFileName, FilterType.EXCLUDE);
            return this;
        }

        /**
         * Add a new filter.
         *
         * @param pattern
         *         pattern
         *
         * @return this.
         */
        public IssueFilterBuilder setExcludeFileNameFilter(final String... pattern) {
            return setExcludeFileNameFilter(Arrays.asList(pattern));
        }
        //</editor-fold>

        //<editor-fold desc="Package name">

        /**
         * Add a new filter.
         *
         * @param pattern
         *         pattern
         *
         * @return this.
         */
        public IssueFilterBuilder setIncludePackageNameFilter(final Collection<String> pattern) {
            addNewFilter(pattern, Issue::getPackageName, FilterType.INCLUDE);
            return this;
        }

        /**
         * Add a new filter.
         *
         * @param pattern
         *         pattern
         *
         * @return this.
         */
        public IssueFilterBuilder setIncludePackageNameFilter(final String... pattern) {
            return setIncludePackageNameFilter(Arrays.asList(pattern));
        }

        /**
         * Add a new filter.
         *
         * @param pattern
         *         pattern
         *
         * @return this.
         */
        public IssueFilterBuilder setExcludePackageNameFilter(final Collection<String> pattern) {
            addNewFilter(pattern, Issue::getPackageName, FilterType.EXCLUDE);
            return this;
        }

        /**
         * Add a new filter.
         *
         * @param pattern
         *         pattern
         *
         * @return this.
         */
        public IssueFilterBuilder setExcludePackageNameFilter(final String... pattern) {
            return setExcludePackageNameFilter(Arrays.asList(pattern));
        }
        //</editor-fold>

        //<editor-fold desc="Module name">

        /**
         * Add a new filter.
         *
         * @param pattern
         *         pattern
         *
         * @return this.
         */
        public IssueFilterBuilder setIncludeModuleNameFilter(final Collection<String> pattern) {
            addNewFilter(pattern, Issue::getModuleName, FilterType.INCLUDE);
            return this;
        }

        /**
         * Add a new filter.
         *
         * @param pattern
         *         pattern
         *
         * @return this.
         */
        public IssueFilterBuilder setIncludeModuleNameFilter(final String... pattern) {
            return setIncludeModuleNameFilter(Arrays.asList(pattern));
        }

        /**
         * Add a new filter.
         *
         * @param pattern
         *         pattern
         *
         * @return this.
         */
        public IssueFilterBuilder setExcludeModuleNameFilter(final Collection<String> pattern) {
            addNewFilter(pattern, Issue::getModuleName, FilterType.EXCLUDE);
            return this;
        }

        /**
         * Add a new filter.
         *
         * @param pattern
         *         pattern
         *
         * @return this.
         */
        public IssueFilterBuilder setExcludeModuleNameFilter(final String... pattern) {
            return setExcludeModuleNameFilter(Arrays.asList(pattern));
        }
        //</editor-fold>

        //<editor-fold desc="Category">

        /**
         * Add a new filter.
         *
         * @param pattern
         *         pattern
         *
         * @return this.
         */
        public IssueFilterBuilder setIncludeCategoryFilter(final Collection<String> pattern) {
            addNewFilter(pattern, Issue::getCategory, FilterType.INCLUDE);
            return this;
        }

        /**
         * Add a new filter.
         *
         * @param pattern
         *         pattern
         *
         * @return this.
         */
        public IssueFilterBuilder setIncludeCategoryFilter(final String... pattern) {
            return setIncludeCategoryFilter(Arrays.asList(pattern));
        }

        /**
         * Add a new filter.
         *
         * @param pattern
         *         pattern
         *
         * @return this.
         */
        public IssueFilterBuilder setExcludeCategoryFilter(final Collection<String> pattern) {
            addNewFilter(pattern, Issue::getCategory, FilterType.EXCLUDE);
            return this;
        }

        /**
         * Add a new filter.
         *
         * @param pattern
         *         pattern
         *
         * @return this.
         */
        public IssueFilterBuilder setExcludeCategoryFilter(final String... pattern) {
            return setExcludeCategoryFilter(Arrays.asList(pattern));
        }
        //</editor-fold>

        //<editor-fold desc="Type">

        /**
         * Add a new filter.
         *
         * @param pattern
         *         pattern
         *
         * @return this.
         */
        public IssueFilterBuilder setIncludeTypeFilter(final Collection<String> pattern) {
            addNewFilter(pattern, Issue::getType, FilterType.INCLUDE);
            return this;
        }

        /**
         * Add a new filter.
         *
         * @param pattern
         *         pattern
         *
         * @return this.
         */
        public IssueFilterBuilder setIncludeTypeFilter(final String... pattern) {
            return setIncludeTypeFilter(Arrays.asList(pattern));
        }

        /**
         * Add a new filter.
         *
         * @param pattern
         *         pattern
         *
         * @return this.
         */
        public IssueFilterBuilder setExcludeTypeFilter(final Collection<String> pattern) {
            addNewFilter(pattern, Issue::getType, FilterType.EXCLUDE);
            return this;
        }

        /**
         * Add a new filter.
         *
         * @param pattern
         *         pattern
         *
         * @return this.
         */
        public IssueFilterBuilder setExcludeTypeFilter(final String... pattern) {
            return setExcludeTypeFilter(Arrays.asList(pattern));
        }
        //</editor-fold>

        //<editor-fold desc="Message">

        /**
         * Add a new filter to include issues with matching issue message.
         *
         * @param pattern
         *         pattern
         *
         * @return this.
         */
        public IssueFilterBuilder setIncludeMessageFilter(final Collection<String> pattern) {
            addMessageFilter(pattern, FilterType.INCLUDE);
            return this;
        }

        /**
         * Add a new filter to include issues with matching issue message.
         *
         * @param pattern
         *         pattern
         *
         * @return this.
         */
        public IssueFilterBuilder setIncludeMessageFilter(final String... pattern) {
            return setIncludeMessageFilter(Arrays.asList(pattern));
        }

        /**
         * Add a new filter to exclude issues with matching issue message.
         *
         * @param pattern
         *         pattern
         *
         * @return this.
         */
        public IssueFilterBuilder setExcludeMessageFilter(final Collection<String> pattern) {
            addMessageFilter(pattern, FilterType.EXCLUDE);
            return this;
        }

        /**
         * Add a new filter to exclude issues with matching issue message.
         *
         * @param pattern
         *         pattern
         *
         * @return this.
         */
        public IssueFilterBuilder setExcludeMessageFilter(final String... pattern) {
            return setExcludeMessageFilter(Arrays.asList(pattern));
        }

        private void addMessageFilter(final Collection<String> pattern, final FilterType filterType) {
            addNewFilter(pattern, issue -> String.format("%s%n%s", issue.getMessage(), issue.getDescription()),
                    filterType);
        }
        //</editor-fold>
    }
}
