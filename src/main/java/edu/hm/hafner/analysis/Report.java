package edu.hm.hafner.analysis; // NOPMD

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.FormatMethod;

import edu.hm.hafner.util.Ensure;
import edu.hm.hafner.util.FilteredLog;
import edu.hm.hafner.util.Generated;
import edu.hm.hafner.util.LineRangeList;
import edu.hm.hafner.util.PathUtil;
import edu.hm.hafner.util.TreeStringBuilder;
import edu.hm.hafner.util.VisibleForTesting;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A report contains a set of unique {@link Issue issues}: it contains no duplicate elements, i.e., it models the
 * mathematical <i>set</i> abstraction. This report provides a <i>total ordering</i> on its elements. I.e., the issues
 * in this report are ordered by their index: the first added issue is at position 0, the second added issue is at
 * position 1, and so on.
 *
 * <p>
 * Additionally, this report provides methods to find and filter issues based on different properties. To create issues
 * use the provided {@link IssueBuilder builder} class.
 * </p>
 *
 * @author Ullrich Hafner
 */
@SuppressWarnings({"PMD.ExcessivePublicCount", "PMD.GodClass", "PMD.CyclomaticComplexity", "checkstyle:ClassFanOutComplexity"})
public class Report implements Iterable<Issue>, Serializable {
    @Serial
    private static final long serialVersionUID = 5L; // release 13.0.0

    @VisibleForTesting
    static final String DEFAULT_ID = "-";

    private String id;
    private String name;
    private String originReportFile;
    private String icon = StringUtils.EMPTY; // since 13.0.0
    private String parserId = DEFAULT_ID; // since 13.0.0
    private IssueType elementType; // since 13.0.0

    @SuppressWarnings("serial")
    private List<Report> subReports = new ArrayList<>(); // almost final

    @SuppressWarnings("serial")
    private Set<Issue> elements = new LinkedHashSet<>();
    @SuppressWarnings("serial")
    private List<String> infoMessages = new ArrayList<>();
    @SuppressWarnings("serial")
    private List<String> errorMessages = new ArrayList<>();
    @SuppressWarnings("serial")
    private Map<String, Integer> countersByKey = new HashMap<>();

    private int duplicatesSize;

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
     *         a human-readable name for the report
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
     *         a human-readable name for the report
     * @param originReportFile
     *         the specified source file that is the origin of the issues.
     */
    public Report(final String id, final String name, final String originReportFile) {
        this(id, name, originReportFile, IssueType.WARNING);
    }

    /**
     * Creates an empty {@link Report} with the specified ID and name. Link the report with the specified source file
     * that is the origin of the issues.
     *
     * @param id
     *         the ID of the report
     * @param name
     *         a human-readable name for the report
     * @param originReportFile
     *         the specified source file that is the origin of the issues
     * @param elementType
     *         the type of the issues in the report
     */
    public Report(final String id, final String name, final String originReportFile, final IssueType elementType) {
        this.id = id;
        this.name = name;
        this.originReportFile = originReportFile;
        this.elementType = elementType;
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

    public String getId() {
        return id;
    }

    private boolean hasId() {
        return isDefaultId(id);
    }

    public String getParserId() {
        return aggregateChildProperty(Report::getParserId, parserId);
    }

    /**
     * Returns whether this report has a parser ID. A parser ID is considered to be set if it is not the default ID.
     *
     * @return {@code true} if this report has a parser ID, {@code false} otherwise
     */
    public boolean hasParserId() {
        return isDefaultId(getParserId());
    }

    private boolean isDefaultId(final String value) {
        return !DEFAULT_ID.equals(value) && StringUtils.isNoneBlank(value);
    }

    /**
     * Returns the effective ID of this report. This ID is the unique ID of all containing sub-reports. If this ID is
     * not unique, then the {@link #DEFAULT_ID} will be returned.
     *
     * @return the effective ID of all sub-reports
     */
    public String getEffectiveId() {
        return aggregateChildProperty(Report::getEffectiveId, getId());
    }

    private String aggregateChildProperty(final Function<Report, String> idProperty, final String defaultId) {
        Set<String> ids = subReports.stream().map(idProperty).collect(Collectors.toSet());
        ids.add(defaultId);
        ids.remove(DEFAULT_ID);

        if (ids.size() == 1) {
            return ids.iterator().next();
        }
        return defaultId;
    }

    public String getName() {
        return name;
    }

    /**
     * Sets the origin of all issues in this report. Calling this method will associate all containing issues and issues
     * in sub-reports using the specified ID and name.
     *
     * @param originId
     *         the ID of the report
     * @param originName
     *         a human-readable name for the report
     */
    public void setOrigin(final String originId, final String originName) {
        var normalizedId = StringUtils.defaultIfBlank(originId, DEFAULT_ID);
        var normalizedName = StringUtils.defaultIfBlank(originName, DEFAULT_ID);

        id = normalizedId;
        name = normalizedName;
        subReports.forEach(report -> report.setOrigin(normalizedId, normalizedName));
        elements.forEach(issue -> issue.setOrigin(normalizedId, normalizedName));    }

    /**
     * Sets the origin of all issues in this report. Calling this method will associate all containing issues and issues
     * in sub-reports using the specified ID and name.
     *
     * @param originId
     *         the ID of the report
     * @param originName
     *         a human-readable name for the report
     * @param originElementType
     *         the type of the issues in the report
     */
    private void setOrigin(final String originId, final String originName, final IssueType originElementType) {
        setOrigin(originId, originName);

        elementType = originElementType;
        parserId = originId;
        subReports.forEach(report -> report.setOrigin(this.id, this.name, originElementType));
        // TODO check if we need the type for issues as well
        elements.forEach(issue -> issue.setOrigin(this.id, this.name));
    }

    /**
     * Sets the origin of all issues in this report. Calling this method will associate all containing issues and issues
     * in sub-reports using the specified ID and name.
     *
     * @param originId
     *         the ID of the report
     * @param originName
     *         a human-readable name for the report
     * @param elementType
     *         the type of the issues in the report
     * @param reportFile
     *         the report file name to add
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public void setOrigin(final String originId, final String originName, final IssueType elementType,
            final String reportFile) {
        setOrigin(originId, originName, elementType);
        setOriginReportFile(reportFile);
    }

    /**
     * Returns the effective ID of this report. This ID is the unique ID of all containing sub-reports. If this ID is
     * not unique, then the {@link #DEFAULT_ID} will be returned.
     *
     * @return the effective ID of all sub-reports
     */
    public String getEffectiveName() {
        return aggregateChildProperty(Report::getEffectiveName, getName());
    }

    public String getOriginReportFile() {
        return originReportFile;
    }

    /**
     * Stores the name of the report file that is the origin of the contained issues.
     *
     * @param originReportFile
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
     * Returns the type of the issues in the report. The type might be used to customize reports in the UI.
     *
     * @return the type of the issues in the report
     */
    public IssueType getElementType() {
        if (elements.isEmpty()) {
            var types = subReports.stream()
                    .map(Report::getElementType)
                    .collect(Collectors.toSet());
            if (types.size() > 1) {
                return IssueType.WARNING; // fallback if the element type is not unique
            }
            else if (types.isEmpty()) {
                return elementType;
            }
            return types.iterator().next();
        }
        return elementType;
    }

    /**
     * Returns the icon of the report. The icon might be used to customize reports in the UI.
     *
     * @return the name of te icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Sets the icon of the report. The icon might be used to customize reports in the UI.
     *
     * @param icon the new icon
     */
    public void setIcon(final String icon) {
        if (StringUtils.isNotBlank(icon)) {
            this.icon = icon;
        }
    }

    /**
     * Appends the specified issue to the end of this report. Duplicates will be skipped (the number of skipped elements
     * is available using the method {@link #getDuplicatesSize()}).
     *
     * @param issue
     *         the issue to append
     *
     * @return this
     */
    @CanIgnoreReturnValue
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
     * Appends all the specified issues to the end of this report, preserving the order of the array elements.
     * Duplicates will be skipped (the number of skipped elements is available using the method
     * {@link #getDuplicatesSize()}).
     *
     * @param issue
     *         the first issue to append
     * @param additionalIssues
     *         the additional issue to append
     *
     * @return this
     * @see #add(Issue)
     */
    @CanIgnoreReturnValue
    public Report addAll(final Issue issue, final Issue... additionalIssues) {
        add(issue);
        for (Issue additional : additionalIssues) {
            add(additional);
        }
        return this;
    }

    /**
     * Appends all the specified issues to the end of this report, preserving the order of the array elements.
     * Duplicates will be skipped (the number of skipped elements is available using the method
     * {@link #getDuplicatesSize()}).
     *
     * @param issues
     *         the issues to append
     *
     * @return this
     * @see #add(Issue)
     */
    @CanIgnoreReturnValue
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
    @CanIgnoreReturnValue
    public Report addAll(final Report... reports) {
        Ensure.that(reports).isNotEmpty("No reports given.");

        List<Report> reportsToAdd = new ArrayList<>();
        for (Report report : reports) {
            if (!report.elements.isEmpty() && !report.subReports.isEmpty()) {
                throw new IllegalArgumentException(
                        "Reports should either contain issues as top-level elements or as leaf elements but not both.");
            }
            if (report.subReports.isEmpty()) {
                reportsToAdd.add(report);
            }
            else {
                reportsToAdd.addAll(report.subReports);
                infoMessages.addAll(report.infoMessages);
                errorMessages.addAll(report.errorMessages);
            }
        }

        for (Report report : reportsToAdd) {
            var copyWithoutDuplicates = report.copyEmptyInstance();
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
        return elements.contains(issue) || subReportsContains(issue);
    }

    private Boolean subReportsContains(final Issue issue) {
        return subReports.stream()
                .map(r -> r.contains(issue))
                .reduce(Boolean::logicalOr)
                .orElse(false);
    }

    @VisibleForTesting
    List<Report> getSubReports() {
        return subReports;
    }

    /**
     * Called after deserialization to improve the memory usage and to initialize fields that have been introduced after
     * the first release.
     *
     * @return this
     */
    @Serial
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
        if (parserId == null) { // release 13.0.0
            parserId = DEFAULT_ID;
            icon = DEFAULT_ID;
            elementType = IssueType.WARNING;
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
        throw new NoSuchElementException("No removed found with id %s.".formatted(issueId));
    }

    private Optional<Issue> removeIfContained(final UUID issueId) {
        Optional<Issue> issue = find(issueId);
        if (issue.isPresent()) {
            elements.remove(issue.get());
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
                .orElseThrow(() -> new NoSuchElementException(
                        "No issue found with id %s.".formatted(issueId)));
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
        var filtered = copyEmptyInstance();
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

    /**
     * Returns the issues in this report that are part of modified code. This will include the issues of any
     * sub-reports.
     *
     * @return all issues in this report
     */
    public Collection<Issue> getInModifiedCode() {
        return stream().filter(Issue::isPartOfModifiedCode).collect(Collectors.toList());
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

    @Override
    public String toString() {
        var summary = String.format(Locale.ENGLISH, "%s%s%s", getNamePrefix(), getSummary(), getDuplicates());

        return summary + getSeverityDistribution();
    }

    /**
     * Returns a string representation of this report's severity distribution.
     *
     * @return a string representation of severity distribution
     */
    public String getSeverityDistribution() {
        var severityDistribution = Severity.getPredefinedValues()
                .stream()
                .map(this::reportSeverity)
                .flatMap(Optional::stream)
                .collect(Collectors.joining(", "));
        if (StringUtils.isEmpty(severityDistribution)) {
            return severityDistribution;
        }
        return " (" + severityDistribution + ")";
    }

    /**
     * Returns a string representation of this report that shows the number of issues.
     *
     * @return a string representation of this report
     */
    public String getSummary() {
        return getItemName(size());
    }

    private String getNamePrefix() {
        if (isEmptyOrDefault(getName()) && isEmptyOrDefault(getId())) {
            return StringUtils.EMPTY;
        }
        else {
            return String.format(Locale.ENGLISH, "%s (%s): ", getName(), getId());
        }
    }

    private boolean isEmptyOrDefault(final String value) {
        return StringUtils.isEmpty(value) || DEFAULT_ID.equals(value);
    }

    private Optional<String> reportSeverity(final Severity severity) {
        var size = getSizeOf(severity);
        if (size > 0) {
            return Optional.of(
                    String.format(Locale.ENGLISH, "%s: %d", StringUtils.lowerCase(severity.getName()), size));
        }
        return Optional.empty();
    }

    private String getItemName(final int size) {
        var items = getItemCount(size);
        if (size == 0) {
            return String.format("No %s", items);
        }
        return String.format(Locale.ENGLISH, "%d %s", size, items);
    }

    // Open as API?
    private String getItemCount(final int count) {
        if (count == 1) {
            return switch (getElementType()) {
                case WARNING -> "warning";
                case BUG -> "bug";
                case DUPLICATION -> "duplication";
                case VULNERABILITY -> "vulnerability";
            };
        }
        else {
            return switch (getElementType()) {
                case WARNING -> "warnings";
                case BUG -> "bugs";
                case DUPLICATION -> "duplications";
                case VULNERABILITY -> "vulnerabilities";
            };
        }
    }

    private String getDuplicates() {
        if (duplicatesSize > 0) {
            return String.format(Locale.ENGLISH, " (%d duplicates)", duplicatesSize);
        }
        return StringUtils.EMPTY;
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
     * @param propertyMapper
     *         the property mapper that selects the property
     * @param <T>
     *         type of the property
     *
     * @return the set of different values
     * @see #getFiles()
     */
    public <T> Set<T> getProperties(final Function<? super Issue, T> propertyMapper) {
        return stream().map(propertyMapper).collect(Collectors.toSet());
    }

    /**
     * Returns the number of occurrences for every existing value of a given property for all issues.
     *
     * @param propertyMapper
     *         the property mapper that selects the property to evaluate
     * @param <T>
     *         type of the property
     *
     * @return a mapping of: property value to the number of issues for that value
     * @see #getProperties(Function)
     */
    public <T> Map<T, Integer> getPropertyCount(final Function<? super Issue, T> propertyMapper) {
        return stream().collect(
                Collectors.groupingBy(propertyMapper, Collectors.reducing(0, issue -> 1, Integer::sum)));
    }

    /**
     * Groups issues by a specified property. Returns the results as a mapping of property values to a new set of
     * {@link Report} for this value.
     *
     * @param propertyName
     *         the property to that selects the property to evaluate
     *
     * @return a mapping of: property value to the number of issues for that value
     * @see #getProperties(Function)
     */
    public Map<String, Report> groupByProperty(final String propertyName) {
        var issues = stream()
                .collect(Collectors.groupingBy(Issue.getPropertyValueGetter(propertyName)));

        return issues.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> new Report().addAll(e.getValue())));
    }

    /**
     * Returns a shallow copy of this issue container.
     *
     * @return a new issue container that contains the same elements in the same order
     */
    public Report copy() {
        var copied = new Report();
        copyIssuesAndProperties(this, copied);
        return copied;
    }

    private void copyIssuesAndProperties(final Report source, final Report destination) {
        copyProperties(source, destination);
        destination.addAll(source.elements);
        for (Report subReport : subReports) {
            destination.addAll(subReport.copy());
        }
    }

    private void copyProperties(final Report source, final Report destination) {
        destination.id = source.getId();
        destination.name = source.getName();
        destination.icon = source.getIcon();
        destination.elementType = source.getElementType();
        destination.parserId = source.getParserId();
        destination.originReportFile = source.getOriginReportFile();
        destination.duplicatesSize += source.duplicatesSize; // not recursively
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
        var empty = new Report();
        copyProperties(this, empty);
        return empty;
    }

    /**
     * Merge all log messages from the specified log into the log of this report.
     *
     * @param log
     *         the log messages to merge
     */
    public void mergeLogMessages(final FilteredLog log) {
        infoMessages.addAll(log.getInfoMessages());
        errorMessages.addAll(log.getErrorMessages());
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
        infoMessages.add(format.formatted(args));
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
        errorMessages.add(format.formatted(args));
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
    @Generated
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        var issues = (Report) o;
        return duplicatesSize == issues.duplicatesSize
                && Objects.equals(id, issues.id)
                && Objects.equals(name, issues.name)
                && Objects.equals(icon, issues.icon)
                && Objects.equals(elementType, issues.elementType)
                && Objects.equals(parserId, issues.parserId)
                && Objects.equals(originReportFile, issues.originReportFile)
                && Objects.equals(subReports, issues.subReports)
                && Objects.equals(elements, issues.elements)
                && Objects.equals(infoMessages, issues.infoMessages)
                && Objects.equals(errorMessages, issues.errorMessages)
                && Objects.equals(countersByKey, issues.countersByKey);
    }

    @Override
    @Generated
    public int hashCode() {
        return Objects.hash(id, name, icon, elementType, parserId, originReportFile, subReports, elements,
                infoMessages, errorMessages, countersByKey, duplicatesSize);
    }

    @Serial
    private void writeObject(final ObjectOutputStream output) throws IOException {
        output.writeInt(elements.size());
        writeIssues(output);

        output.writeObject(infoMessages);
        output.writeObject(errorMessages);
        output.writeObject(countersByKey);

        output.writeInt(duplicatesSize);

        output.writeUTF(id);
        output.writeUTF(name);
        output.writeUTF(icon);
        output.writeUTF(parserId);
        output.writeObject(elementType);

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
    @Serial
    private void readObject(final ObjectInputStream input) throws IOException, ClassNotFoundException {
        elements = new LinkedHashSet<>();
        readIssues(input, input.readInt());

        infoMessages = (List<String>) input.readObject();
        errorMessages = (List<String>) input.readObject();
        countersByKey = (Map<String, Integer>) input.readObject();

        duplicatesSize = input.readInt();

        id = input.readUTF();
        name = input.readUTF();

        icon = input.readUTF();
        parserId = input.readUTF();
        elementType = (IssueType) input.readObject();

        originReportFile = input.readUTF();
        subReports = new ArrayList<>();

        int subReportCount = input.readInt();
        for (int i = 0; i < subReportCount; i++) {
            var subReport = (Report) input.readObject();
            subReports.add(subReport);
        }
    }

    @SuppressFBWarnings("OBJECT_DESERIALIZATION")
    @SuppressWarnings("BanSerializableRead")
    private void readIssues(final ObjectInputStream input, final int size) throws IOException, ClassNotFoundException {
        var builder = new TreeStringBuilder();
        for (int i = 0; i < size; i++) {
            var path = input.readUTF();
            var fileName = builder.intern(input.readUTF());
            int lineStart = input.readInt();
            int lineEnd = input.readInt();
            int columnStart = input.readInt();
            int columnEnd = input.readInt();
            var lineRanges = (LineRangeList) input.readObject();
            var category = input.readUTF();
            var type = input.readUTF();
            var packageName = builder.intern(input.readUTF());
            var moduleName = input.readUTF();
            var severity = Severity.valueOf(input.readUTF());
            var message = builder.intern(readLongString(input));
            var description = readLongString(input);
            var origin = input.readUTF();
            var originName = input.readUTF();
            var reference = input.readUTF();
            var fingerprint = input.readUTF();
            var additionalProperties = (Serializable) input.readObject();
            var uuid = (UUID) input.readObject();

            var issue = new Issue(path, fileName,
                    lineStart, lineEnd, columnStart, columnEnd,
                    lineRanges, null, category, type, packageName, moduleName,
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
        return String.valueOf(chars);
    }

    /**
     * Returns a human-readable name for the specified {@code origin} of this report.
     *
     * @param origin
     *         the origin to get the human-readable name for
     *
     * @return the name, or an empty string if no such name has been set
     */
    public String getNameOfOrigin(final String origin) {
        if (getId().equals(origin)) {
            return getName();
        }

        for (Report subReport : subReports) {
            var nameOfSubReport = subReport.getNameOfOrigin(origin);
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
     * Sets a reference to the execution of the static analysis tool (build ID, timestamp, etc.). This property should
     * not be set by parsers as it is overwritten by the {@link IssueDifference differencing engine} while computing new
     * and fixed issues.
     *
     * @param reference
     *         the reference
     */
    public void setReference(final String reference) {
        stream().forEach(issue -> issue.setReference(reference));
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
         * Adds a new filter for each pattern string. Adds the filter either to the include or exclude list.
         *
         * @param patterns
         *         filter patterns.
         * @param propertyToFilter
         *         Function to get a string from Issue for patterns
         * @param type
         *         type of the filter
         */
        private void addNewFilter(final Collection<String> patterns,
                final Function<Issue, String> propertyToFilter,
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
         * Creates a new {@link Issue} filter. Combines all includes with or and all excludes with and.
         *
         * @return {@link Issue} filter which has all added filters as filter criteria
         */
        @SuppressWarnings("NoFunctionalReturnType")
        public Predicate<Issue> build() {
            return includeFilters.stream().reduce(Predicate::or).orElse(issue -> true)
                    .and(excludeFilters.stream().reduce(Predicate::and).orElse(issue -> true));
        }

        //<editor-fold desc="File name">

        /**
         * Add a new filter for {@code Issue::getFileName}.
         *
         * @param patterns
         *         the patterns to match
         *
         * @return this
         */
        @CanIgnoreReturnValue
        public IssueFilterBuilder setIncludeFileNameFilter(final Collection<String> patterns) {
            addNewFilter(patterns, Issue::getFileName, FilterType.INCLUDE);
            return this;
        }

        /**
         * Add a new filter for {@code Issue::getFileName}.
         *
         * @param patterns
         *         the patterns to match
         *
         * @return this
         */
        @CanIgnoreReturnValue
        public IssueFilterBuilder setIncludeFileNameFilter(final String... patterns) {
            return setIncludeFileNameFilter(Arrays.asList(patterns));
        }

        /**
         * Add a new filter for {@code Issue::getFileName}.
         *
         * @param patterns
         *         the patterns to match
         *
         * @return this
         */
        @CanIgnoreReturnValue
        public IssueFilterBuilder setExcludeFileNameFilter(final Collection<String> patterns) {
            addNewFilter(patterns, Issue::getFileName, FilterType.EXCLUDE);
            return this;
        }

        /**
         * Add a new filter for {@code Issue::getFileName}.
         *
         * @param patterns
         *         the patterns to match
         *
         * @return this
         */
        @CanIgnoreReturnValue
        public IssueFilterBuilder setExcludeFileNameFilter(final String... patterns) {
            return setExcludeFileNameFilter(Arrays.asList(patterns));
        }

        //</editor-fold>

        //<editor-fold desc="Package name">

        /**
         * Add a new filter for {@code Issue::getPackageName}.
         *
         * @param patterns
         *         the patterns to match
         *
         * @return this
         */
        @CanIgnoreReturnValue
        public IssueFilterBuilder setIncludePackageNameFilter(final Collection<String> patterns) {
            addNewFilter(patterns, Issue::getPackageName, FilterType.INCLUDE);
            return this;
        }

        /**
         * Add a new filter for {@code Issue::getPackageName}.
         *
         * @param patterns
         *         the patterns to match
         *
         * @return this
         */
        @CanIgnoreReturnValue
        public IssueFilterBuilder setIncludePackageNameFilter(final String... patterns) {
            return setIncludePackageNameFilter(Arrays.asList(patterns));
        }

        /**
         * Add a new filter for {@code Issue::getPackageName}.
         *
         * @param patterns
         *         the patterns to match
         *
         * @return this
         */
        @CanIgnoreReturnValue
        public IssueFilterBuilder setExcludePackageNameFilter(final Collection<String> patterns) {
            addNewFilter(patterns, Issue::getPackageName, FilterType.EXCLUDE);
            return this;
        }

        /**
         * Add a new filter for {@code Issue::getPackageName}.
         *
         * @param patterns
         *         the patterns to match
         *
         * @return this
         */
        @CanIgnoreReturnValue
        public IssueFilterBuilder setExcludePackageNameFilter(final String... patterns) {
            return setExcludePackageNameFilter(Arrays.asList(patterns));
        }

        //</editor-fold>

        //<editor-fold desc="Module name">

        /**
         * Add a new filter for {@code Issue::getModuleName}.
         *
         * @param patterns
         *         the patterns to match
         *
         * @return this
         */
        @CanIgnoreReturnValue
        public IssueFilterBuilder setIncludeModuleNameFilter(final Collection<String> patterns) {
            addNewFilter(patterns, Issue::getModuleName, FilterType.INCLUDE);
            return this;
        }

        /**
         * Add a new filter for {@code Issue::getModuleName}.
         *
         * @param patterns
         *         the patterns to match
         *
         * @return this
         */
        @CanIgnoreReturnValue
        public IssueFilterBuilder setIncludeModuleNameFilter(final String... patterns) {
            return setIncludeModuleNameFilter(Arrays.asList(patterns));
        }

        /**
         * Add a new filter for {@code Issue::getModuleName}.
         *
         * @param patterns
         *         the patterns to match
         *
         * @return this
         */
        @CanIgnoreReturnValue
        public IssueFilterBuilder setExcludeModuleNameFilter(final Collection<String> patterns) {
            addNewFilter(patterns, Issue::getModuleName, FilterType.EXCLUDE);
            return this;
        }

        /**
         * Add a new filter for {@code Issue::getModuleName}.
         *
         * @param patterns
         *         the patterns to match
         *
         * @return this
         */
        @CanIgnoreReturnValue
        public IssueFilterBuilder setExcludeModuleNameFilter(final String... patterns) {
            return setExcludeModuleNameFilter(Arrays.asList(patterns));
        }

        //</editor-fold>

        //<editor-fold desc="Category">

        /**
         * Add a new filter for {@code Issue::getCategory}.
         *
         * @param patterns
         *         the patterns to match
         *
         * @return this
         */
        @CanIgnoreReturnValue
        public IssueFilterBuilder setIncludeCategoryFilter(final Collection<String> patterns) {
            addNewFilter(patterns, Issue::getCategory, FilterType.INCLUDE);
            return this;
        }

        /**
         * Add a new filter for {@code Issue::getCategory}.
         *
         * @param patterns
         *         the patterns to match
         *
         * @return this
         */
        @CanIgnoreReturnValue
        public IssueFilterBuilder setIncludeCategoryFilter(final String... patterns) {
            return setIncludeCategoryFilter(Arrays.asList(patterns));
        }

        /**
         * Add a new filter for {@code Issue::getCategory}.
         *
         * @param patterns
         *         the patterns to match
         *
         * @return this
         */
        @CanIgnoreReturnValue
        public IssueFilterBuilder setExcludeCategoryFilter(final Collection<String> patterns) {
            addNewFilter(patterns, Issue::getCategory, FilterType.EXCLUDE);
            return this;
        }

        /**
         * Add a new filter for {@code Issue::getCategory}.
         *
         * @param patterns
         *         the patterns to match
         *
         * @return this
         */
        @CanIgnoreReturnValue
        public IssueFilterBuilder setExcludeCategoryFilter(final String... patterns) {
            return setExcludeCategoryFilter(Arrays.asList(patterns));
        }

        //</editor-fold>

        //<editor-fold desc="Type">

        /**
         * Add a new filter for {@code Issue::getCategory}.
         *
         * @param patterns
         *         the patterns to match
         *
         * @return this
         */
        @CanIgnoreReturnValue
        public IssueFilterBuilder setIncludeTypeFilter(final Collection<String> patterns) {
            addNewFilter(patterns, Issue::getType, FilterType.INCLUDE);
            return this;
        }

        /**
         * Add a new filter for {@code Issue::getType}.
         *
         * @param patterns
         *         the patterns to match
         *
         * @return this
         */
        @CanIgnoreReturnValue
        public IssueFilterBuilder setIncludeTypeFilter(final String... patterns) {
            return setIncludeTypeFilter(Arrays.asList(patterns));
        }

        /**
         * Add a new filter for {@code Issue::getType}.
         *
         * @param patterns
         *         the patterns to match
         *
         * @return this
         */
        @CanIgnoreReturnValue
        public IssueFilterBuilder setExcludeTypeFilter(final Collection<String> patterns) {
            addNewFilter(patterns, Issue::getType, FilterType.EXCLUDE);
            return this;
        }

        /**
         * Add a new filter for {@code Issue::getType}.
         *
         * @param patterns
         *         the patterns to match
         *
         * @return this
         */
        @CanIgnoreReturnValue
        public IssueFilterBuilder setExcludeTypeFilter(final String... patterns) {
            return setExcludeTypeFilter(Arrays.asList(patterns));
        }

        //</editor-fold>

        //<editor-fold desc="Message">

        /**
         * Add a new filter for {@code Issue::getMessage}.
         *
         * @param patterns
         *         the patterns to match
         *
         * @return this
         */
        @CanIgnoreReturnValue
        public IssueFilterBuilder setIncludeMessageFilter(final Collection<String> patterns) {
            addMessageFilter(patterns, FilterType.INCLUDE);
            return this;
        }

        /**
         * Add a new filter for {@code Issue::getMessage}.
         *
         * @param patterns
         *         the patterns to match
         *
         * @return this
         */
        @CanIgnoreReturnValue
        public IssueFilterBuilder setIncludeMessageFilter(final String... patterns) {
            return setIncludeMessageFilter(Arrays.asList(patterns));
        }

        /**
         * Add a new filter for {@code Issue::getMessage}.
         *
         * @param patterns
         *         the patterns to match
         *
         * @return this
         */
        @CanIgnoreReturnValue
        public IssueFilterBuilder setExcludeMessageFilter(final Collection<String> patterns) {
            addMessageFilter(patterns, FilterType.EXCLUDE);
            return this;
        }

        /**
         * Add a new filter for {@code Issue::getMessage}.
         *
         * @param patterns
         *         the patterns to match
         *
         * @return this
         */
        @CanIgnoreReturnValue
        public IssueFilterBuilder setExcludeMessageFilter(final String... patterns) {
            return setExcludeMessageFilter(Arrays.asList(patterns));
        }

        private void addMessageFilter(final Collection<String> patterns, final FilterType filterType) {
            addNewFilter(patterns, issue -> "%s%n%s".formatted(issue.getMessage(), issue.getDescription()),
                    filterType);
        }
        //</editor-fold>
    }

    /**
     * Returns the type of the issues. The type is used to customize reports in the UI.
     */
    public enum IssueType {
        /** A parser that scans the output of a build tool to find warnings. */
        WARNING,
        /** A parser that scans the output of a build tool to find bugs. */
        BUG,
        /** A parser that scans the output of a build tool to find vulnerabilities. */
        VULNERABILITY,
        /** A parser that scans the output of a build tool to find vulnerabilities. */
        DUPLICATION
    }
}
