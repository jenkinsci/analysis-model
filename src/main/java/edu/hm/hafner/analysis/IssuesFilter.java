package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class IssuesFilter {

    private List<FilterRegex> fileNameFilters = new ArrayList<FilterRegex>();
    private List<FilterRegex> packageNameFilters = new ArrayList<FilterRegex>();
    private List<FilterRegex> moduleNameFilters = new ArrayList<FilterRegex>();
    private List<FilterRegex> categoryFilters = new ArrayList<FilterRegex>();
    private List<FilterRegex> typeFilters = new ArrayList<FilterRegex>();

    /**
     * This represents the main functionality of the instance. Will evaluate all rules that where added by {@link
     * #addFilenameFilter(FilterRegex)} {@link #addPackageNameFilter(FilterRegex)} {@link
     * #addModuleNameFilter(FilterRegex)} {@link #addCategoryFilter(FilterRegex)} {@link #addTypeFilter(FilterRegex)}
     *
     * @param issues
     *         list of issues that should be evaluated
     *
     * @return filtered list of {@link Issues}
     */
    Issues apply(final Issues issues) {
        HashSet<UUID> idsToRemove = new HashSet<>();
        for (Issue issue : issues) {
            // FileName
            for (FilterRegex filter : fileNameFilters) {
                boolean matches = issue.getFileName().matches(filter.getExpression());
                if ((filter instanceof ExcludeFilterRegex && matches) || (filter instanceof IncludeFilterRegex && !matches)) {
                    idsToRemove.add(issue.getId());
                }
            }
            // PackageName
            for (FilterRegex filter : packageNameFilters) {
                boolean matches = issue.getPackageName().matches(filter.getExpression());
                if ((filter instanceof ExcludeFilterRegex && matches) || (filter instanceof IncludeFilterRegex && !matches)) {
                    idsToRemove.add(issue.getId());
                }
            }
            // ModuleNames
            for (FilterRegex filter : moduleNameFilters) {
                boolean matches = issue.getModuleName().matches(filter.getExpression());
                if ((filter instanceof ExcludeFilterRegex && matches) || (filter instanceof IncludeFilterRegex && !matches)) {
                    idsToRemove.add(issue.getId());
                }
            }
            // Category
            for (FilterRegex filter : categoryFilters) {
                boolean matches = issue.getCategory().matches(filter.getExpression());
                if ((filter instanceof ExcludeFilterRegex && matches) || (filter instanceof IncludeFilterRegex && !matches)) {
                    idsToRemove.add(issue.getId());
                }
            }
            // Type
            for (FilterRegex filter : typeFilters) {
                boolean matches = issue.getType().matches(filter.getExpression());
                if ((filter instanceof ExcludeFilterRegex && matches) || (filter instanceof IncludeFilterRegex && !matches)) {
                    idsToRemove.add(issue.getId());
                }
            }
        }

        Issues toReturn = issues.copy();
        for (UUID id : idsToRemove) {
            toReturn.remove(id);
        }

        return toReturn;
    }

    /**
     * Add a regular expression for given field.
     * @param filterRegex filter instance that implements {@link FilterRegex} interface
     */
    public void addFilenameFilter(FilterRegex filterRegex) {
        if (!fileNameFilters.contains(filterRegex)) {
            fileNameFilters.add(filterRegex);
        }
    }

    /**
     * Add a regular expression for given field.
     * @param filterRegex filter instance that implements {@link FilterRegex} interface
     */
    public void addPackageNameFilter(FilterRegex filterRegex) {
        if (!packageNameFilters.contains(filterRegex)) {
            packageNameFilters.add(filterRegex);
        }
    }

    /**
     * Add a regular expression for given field.
     * @param filterRegex filter instance that implements {@link FilterRegex} interface
     */
    public void addModuleNameFilter(FilterRegex filterRegex) {
        if (!moduleNameFilters.contains(filterRegex)) {
            moduleNameFilters.add(filterRegex);
        }
    }

    /**
     * Add a regular expression for given field.
     * @param filterRegex filter instance that implements {@link FilterRegex} interface
     */
    void addCategoryFilter(FilterRegex filterRegex) {
        if (!categoryFilters.contains(filterRegex)) {
            categoryFilters.add(filterRegex);
        }
    }

    /**
     * Add a regular expression for given field.
     * @param filterRegex filter instance that implements {@link FilterRegex} interface
     */
    public void addTypeFilter(FilterRegex filterRegex) {
        if (!typeFilters.contains(filterRegex)) {
            typeFilters.add(filterRegex);
        }
    }

    /**
     * Interface that {@link IncludeFilterRegex} and {@link ExcludeFilterRegex} comply to
     */
    interface FilterRegex {
        String getExpression();
    }
}


