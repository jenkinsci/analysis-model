package edu.hm.hafner.analysis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

import edu.hm.hafner.util.Ensure;
import edu.hm.hafner.util.NoSuchElementException;
import edu.hm.hafner.util.VisibleForTesting;
import edu.umd.cs.findbugs.annotations.NonNull;
import static java.util.stream.Collectors.*;

/**
 * A report contains a set of unique {@link Issue issues}: it contains no duplicate elements, i.e. it models the
 * mathematical <i>set</i> abstraction. Furthermore, this report provides a <i>total ordering</i> on its elements. I.e.,
 * the issues in this report are ordered by their index: the first added issue is at position 0, the second added issues
 * is at position 1, and so on.
 * <p>
 * Additionally, this report provides methods to find and filter issues based on different properties. In order to
 * create issues use the provided {@link IssueBuilder builder} class.
 * </p>
 *
 * @author Ullrich Hafner
 */
@SuppressWarnings("PMD.ExcessivePublicCount")
public class Report implements Iterable<Issue>, Serializable {
    private static final long serialVersionUID = 1L; // release 1.0.0

    @VisibleForTesting
    static final String DEFAULT_ID = "-";

    private final Set<Issue> elements = new LinkedHashSet<>();
    private final List<String> infoMessages = new ArrayList<>();
    private final List<String> errorMessages = new ArrayList<>();

    private int duplicatesSize = 0;
    private String origin = DEFAULT_ID;
    private String reference = DEFAULT_ID;

    /**
     * Creates an empty {@link Report}.
     */
    public Report() {
        // no elements to add
    }

    /**
     * Creates a new {@link Report} that will be initialized with the specified collection of {@link Issue} instances.
     *
     * @param issues
     *         the initial set of issues
     *
     * @see #add(Issue)
     */
    public Report(final Collection<? extends Issue> issues) {
        issues.forEach(this::add);
    }

    /**
     * Creates a new {@link Report} that will be initialized with the specified stream of {@link Issue} instances.
     *
     * @param issues
     *         the initial set of issues
     *
     * @see #add(Issue)
     */
    public Report(final Stream<? extends Issue> issues) {
        issues.forEach(this::add);
    }

    private void add(final Issue issue) {
        if (elements.contains(issue)) {
            duplicatesSize++; // elements are marked as duplicate if the fingerprint is different
        }
        else {
            elements.add(issue);
        }
    }

    /**
     * Appends all of the specified elements to the end of this container, preserving the order of the array elements.
     * Duplicates will be skipped (the number of skipped elements is available using the method {@link
     * #getDuplicatesSize()}.
     *
     * @param issue
     *         the issue to append
     * @param additionalIssues
     *         the additional issue to append
     */
    public void add(final Issue issue, final Issue... additionalIssues) {
        add(issue);
        for (Issue additional : additionalIssues) {
            add(additional);
        }
    }

    /**
     * Appends all of the elements in the specified collection to the end of this container, in the order that they are
     * returned by the specified collection's iterator. Duplicates will be skipped (the number of skipped elements is
     * available using the method {@link #getDuplicatesSize()}.
     *
     * @param issues
     *         the issues to append
     */
    public void addAll(final Collection<? extends Issue> issues) {
        for (Issue issue : issues) {
            add(issue);
        }
    }

    /**
     * Appends all of the elements in the specified array of issues to the end of this container, in the order that they
     * are returned by the specified collection's iterator. Duplicates will be skipped (the number of skipped elements
     * is available using the method {@link #getDuplicatesSize()}.
     *
     * @param report
     *         the issues to append
     * @param additionalIssues
     *         the additional issue to append
     */
    public void addAll(final Report report, final Report... additionalIssues) {
        copyIssuesAndProperties(report, this);
        for (Report other : additionalIssues) {
            copyIssuesAndProperties(other, this);
        }
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
    public Issue remove(final UUID issueId) {
        for (Issue element : elements) {
            if (element.getId().equals(issueId)) {
                elements.remove(element);
                return element;
            }
        }
        throw new NoSuchElementException("No issue found with id %s.", issueId);
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
        for (Issue issue : elements) {
            if (issue.getId().equals(issueId)) {
                return issue;
            }
        }
        throw new NoSuchElementException("No issue found with id %s.", issueId);
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
        return filterElements(criterion).collect(toSet());
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
        filtered.addAll(filterElements(criterion).collect(toList()));
        return filtered;
    }

    private Stream<Issue> filterElements(final Predicate<? super Issue> criterion) {
        return elements.stream().filter(criterion);
    }

    @NonNull
    @Override
    public Iterator<Issue> iterator() {
        return Lists.immutable.withAll(elements).iterator();
    }

    /**
     * Creates a new sequential {@code Stream} of {@link Issue} instances from a {@code Spliterator}.
     *
     * @return a new sequential {@code Stream}
     */
    public Stream<Issue> stream() {
        return StreamSupport.stream(Spliterators.spliterator(iterator(), 0L, Spliterator.NONNULL), false);
    }

    /**
     * Returns the number of issues in this container.
     *
     * @return total number of issues
     */
    public int size() {
        return elements.size();
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
     * report (based on {@link #equals(Object)}) is counted as a duplicate. Duplicates are not stored in this
     * report.
     *
     * @return total number of duplicates
     */
    public int getDuplicatesSize() {
        return duplicatesSize;
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
        return getSizeOf(Severity.of(severity));
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
        return elements.stream().filter(issue -> issue.getSeverity().equals(severity)).mapToInt(e -> 1).sum();
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
            throw new IndexOutOfBoundsException("No such index " + index + " in " + toString());
        }
        Iterator<Issue> all = elements.iterator();
        for (int i = 0; i < index; i++) {
            all.next(); // skip this element
        }
        return all.next();
    }

    @Override
    public String toString() {
        return String.format("%d issues (ID = %s)", size(), getOrigin());
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
     * Returns the affected packages for all issues.
     *
     * @return the affected packages
     */
    public Set<String> getPackages() {
        return getProperties(Issue::getPackageName);
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
     * Returns the used categories for all issues.
     *
     * @return the used categories
     */
    public Set<String> getCategories() {
        return getProperties(Issue::getCategory);
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
     * Returns the names of the tools that did report the issues.
     *
     * @return the tools
     */
    public Set<String> getTools() {
        return getProperties(Issue::getOrigin);
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
        return elements.stream().map(propertiesMapper).collect(toSet());
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
        return elements.stream().collect(groupingBy(propertiesMapper, reducing(0, issue -> 1, Integer::sum)));
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
        Map<String, List<Issue>> issues = elements.stream()
                .collect(groupingBy(Issue.getPropertyValueGetter(propertyName)));

        return issues.entrySet().stream()
                .collect(toMap(Entry::getKey, e -> new Report(e.getValue())));
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
        if (!destination.hasOrigin()) {
            destination.origin = source.origin;
        }
        if (!destination.hasReference()) {
            destination.reference = source.reference;
        }

        destination.addAll(source.elements);
        copyProperties(source, destination);
    }

    private void copyProperties(final Report source, final Report destination) {
        destination.duplicatesSize += source.duplicatesSize;
        destination.infoMessages.addAll(source.infoMessages);
        destination.errorMessages.addAll(source.errorMessages);
    }

    /**
     * Returns a new empty issue container with the same properties as this container. The new issue container is empty
     * and does not contain issues.
     *
     * @return a new issue container that contains the same properties but no issues
     */
    public Report copyEmptyInstance() {
        Report empty = new Report();
        empty.setOrigin(origin);
        empty.setReference(reference);
        copyProperties(this, empty);
        return empty;
    }

    /**
     * Sets the ID of the tool that did report this set of issues.
     *
     * @param origin
     *         the origin
     */
    public void setOrigin(final String origin) {
        Ensure.that(origin).isNotNull();

        this.origin = origin;
    }

    /**
     * Returns the ID of the tool that did report this set of issues.
     *
     * @return the origin
     */
    public String getOrigin() {
        return origin;
    }

    private boolean hasOrigin() {
        return !DEFAULT_ID.equals(getOrigin());
    }

    /**
     * Sets a reference to the execution of the static analysis tool (build ID, timestamp, etc.).
     *
     * @param reference
     *         the reference
     */
    public void setReference(final String reference) {
        Ensure.that(reference).isNotNull();

        this.reference = reference;
    }

    /**
     * Returns a reference to the execution of the static analysis tool (build ID, timestamp, etc.).
     *
     * @return the reference
     */
    public String getReference() {
        return reference;
    }

    private boolean hasReference() {
        return !DEFAULT_ID.equals(getReference());
    }

    /**
     * Logs the specified information message. Use this method to log any useful information when composing this set of
     * issues.
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
    public void logInfo(final String format, final Object... args) {
        infoMessages.add(String.format(format, args));
    }

    /**
     * Logs the specified error message. Use this method to log any error when composing this set of issues.
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
    public void logError(final String format, final Object... args) {
        errorMessages.add(String.format(format, args));
    }

    /**
     * Returns the info messages that have been reported since the creation of this set of issues.
     *
     * @return the info messages
     */
    public ImmutableList<String> getInfoMessages() {
        return Lists.immutable.ofAll(infoMessages);
    }

    /**
     * Returns the error messages that have been reported since the creation of this set of issues.
     *
     * @return the error messages
     */
    public ImmutableList<String> getErrorMessages() {
        return Lists.immutable.ofAll(errorMessages);
    }

    @SuppressWarnings("CheckStyle")
    @Override
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
        if (!elements.equals(report.elements)) {
            return false;
        }
        if (!infoMessages.equals(report.infoMessages)) {
            return false;
        }
        if (!errorMessages.equals(report.errorMessages)) {
            return false;
        }
        if (!origin.equals(report.origin)) {
            return false;
        }
        return reference.equals(report.reference);
    }

    @Override
    public int hashCode() {
        int result = elements.hashCode();
        result = 31 * result + infoMessages.hashCode();
        result = 31 * result + errorMessages.hashCode();
        result = 31 * result + duplicatesSize;
        result = 31 * result + origin.hashCode();
        result = 31 * result + reference.hashCode();
        return result;
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
         */
        private void addNewFilter(final Collection<String> patterns, final Function<Issue, String> propertyToFilter,
                final FilterType type) {

            Collection<Predicate<Issue>> filters = new ArrayList<>();
            for (String pattern : patterns) {
                filters.add(issueToFilter -> Pattern.compile(pattern)
                        .matcher(propertyToFilter.apply(issueToFilter)).matches() == (type == FilterType.INCLUDE));
            }

            if (type == FilterType.INCLUDE) {
                includeFilters.addAll(filters);
            }
            else {
                excludeFilters.addAll(filters);
            }
        }

        /**
         * Create a IssueFilter. Combine by default all includes with or and all excludes with and.
         *
         * @return a IssueFilter which has all added filter as filter criteria.
         */
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
        public IssueFilterBuilder setIncludeFilenameFilter(final Collection<String> pattern) {
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
        public IssueFilterBuilder setIncludeFilenameFilter(final String... pattern) {
            return setIncludeFilenameFilter(Arrays.asList(pattern));
        }

        /**
         * Add a new filter.
         *
         * @param pattern
         *         pattern
         *
         * @return this.
         */
        public IssueFilterBuilder setExcludeFilenameFilter(final Collection<String> pattern) {
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
        public IssueFilterBuilder setExcludeFilenameFilter(final String... pattern) {
            return setExcludeFilenameFilter(Arrays.asList(pattern));
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
    }
}
