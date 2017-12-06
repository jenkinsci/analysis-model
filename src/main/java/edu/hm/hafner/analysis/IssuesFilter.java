package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;

/**
 * Represents a class to filter issues by its fileName, packageName, moduleName, category, type.
 *
 * @author Andreas Moser
 */
public class IssuesFilter {
    private Collection<String> fileNameIncludes;
    private Collection<String> fileNameExcludes;

    private Collection<String> packageNameIncludes;
    private Collection<String> packageNameExcludes;

    private Collection<String> moduleNameIncludes;
    private Collection<String> moduleNameExcludes;

    private Collection<String> categoryIncludes;
    private Collection<String> categoryExcludes;

    private Collection<String> typeIncludes;
    private Collection<String> typeExcludes;

    /**
     * Creates a new IssuesFilter. All collections are empty.
     */
    public IssuesFilter() {
        fileNameIncludes = new ArrayList<>();
        fileNameExcludes = new ArrayList<>();

        packageNameIncludes = new ArrayList<>();
        packageNameExcludes = new ArrayList<>();

        moduleNameIncludes = new ArrayList<>();
        moduleNameExcludes = new ArrayList<>();

        categoryIncludes = new ArrayList<>();
        categoryExcludes = new ArrayList<>();

        typeIncludes = new ArrayList<>();
        typeExcludes = new ArrayList<>();
    }

    public IssuesFilter setFileNameIncludes(final Collection<String> fileNameIncludes) {
        this.fileNameIncludes = fileNameIncludes;
        return this;
    }

    public IssuesFilter setFileNameExcludes(final Collection<String> fileNameExcludes) {
        this.fileNameExcludes = fileNameExcludes;
        return this;
    }

    public IssuesFilter setPackageNameIncludes(final Collection<String> packageNameIncludes) {
        this.packageNameIncludes = packageNameIncludes;
        return this;
    }

    public IssuesFilter setPackageNameExcludes(final Collection<String> packageNameExcludes) {
        this.packageNameExcludes = packageNameExcludes;
        return this;
    }

    public IssuesFilter setModuleNameIncludes(final Collection<String> moduleNameIncludes) {
        this.moduleNameIncludes = moduleNameIncludes;
        return this;
    }

    public IssuesFilter setModuleNameExcludes(final Collection<String> moduleNameExcludes) {
        this.moduleNameExcludes = moduleNameExcludes;
        return this;
    }

    public IssuesFilter setCategoryIncludes(final Collection<String> categoryIncludes) {
        this.categoryIncludes = categoryIncludes;
        return this;
    }

    public IssuesFilter setCategoryExcludes(final Collection<String> categoryExcludes) {
        this.categoryExcludes = categoryExcludes;
        return this;
    }

    public IssuesFilter setTypeIncludes(final Collection<String> typeIncludes) {
        this.typeIncludes = typeIncludes;
        return this;
    }

    public IssuesFilter setTypeExcludes(final Collection<String> typeExcludes) {
        this.typeExcludes = typeExcludes;
        return this;
    }

    /**
     * Filters the given issues object depending on the previously set include and exclude patterns of this filter
     * class.
     *
     * @param issuesToFilter
     *         The issues object to filter.
     *
     * @return filteredIssues The filtered issues object.
     */
    public Issues filter(final Issues issuesToFilter) {
        Issues filteredIssues = new Issues();

        for (Issue issue : issuesToFilter) {
            if (isIssueIncluded(issue) && !isIssueExcluded(issue)) {
                filteredIssues.add(issue);
            }
        }

        return filteredIssues;
    }

    /**
     * Checks if the issue is included depending on the previously set include patterns.
     *
     * @param issue
     *         The issue to check.
     *
     * @return true if the issue is included, false otherwise.
     */
    private boolean isIssueIncluded(final Issue issue) {
        if (isIncludeFilterActive()) {
            return isFileNameIncluded(issue) || isPackageNameIncluded(issue) || isModuleNameIncluded(issue)
                    || isCategoryIncluded(issue) || isTypeIncluded(issue);
        }

        return true;
    }

    /**
     * Checks is the issue is excluded.
     *
     * @param issue
     *         The issue to check.
     *
     * @return true if the issue is excluded, false otherwise.
     */
    private boolean isIssueExcluded(final Issue issue) {
        return isFileNameExcluded(issue) || isPackageNameExcluded(issue) || isModuleNameExcluded(issue)
                || isCategoryExcluded(issue) || isTypeExcluded(issue);
    }


    /**
     * Checks if the issue is included depending on its file name.
     *
     * @param issue
     *         The issue to check.
     *
     * @return true if the issue is included, false otherwise.
     */
    private boolean isFileNameIncluded(final Issue issue) {
        boolean included = false;

        for (String regex : fileNameIncludes) {
            if (Pattern.matches(regex, issue.getFileName())) {
                included = true;
                break;
            }
        }

        return included;
    }

    /**
     * Checks if the issue is included depending on its package name.
     *
     * @param issue
     *         The issue to check.
     *
     * @return true if the issue is included, false otherwise.
     */
    private boolean isPackageNameIncluded(final Issue issue) {
        boolean included = false;

        for (String regex : packageNameIncludes) {
            if (Pattern.matches(regex, issue.getPackageName())) {
                included = true;
                break;
            }
        }

        return included;
    }

    /**
     * Checks if the issue is included depending on its module name.
     *
     * @param issue
     *         The issue to check.
     *
     * @return true if the issue is included, false otherwise.
     */
    private boolean isModuleNameIncluded(final Issue issue) {
        boolean included = false;

        for (String regex : moduleNameIncludes) {
            if (Pattern.matches(regex, issue.getModuleName())) {
                included = true;
                break;
            }
        }

        return included;
    }

    /**
     * Checks if the issue is included depending on its category.
     *
     * @param issue
     *         The issue to check.
     *
     * @return true if the issue is included, false otherwise.
     */
    private boolean isCategoryIncluded(final Issue issue) {
        boolean included = false;

        for (String regex : categoryIncludes) {
            if (Pattern.matches(regex, issue.getCategory())) {
                included = true;
                break;
            }
        }

        return included;
    }

    /**
     * Checks if the issue is included depending on its type.
     *
     * @param issue
     *         The issue to check.
     *
     * @return true if the issue is included, false otherwise.
     */
    private boolean isTypeIncluded(final Issue issue) {
        boolean included = false;

        for (String regex : typeIncludes) {
            if (Pattern.matches(regex, issue.getType())) {
                included = true;
                break;
            }
        }

        return included;
    }

    /**
     * Checks if the issue is excluded depending on its file name.
     *
     * @param issue
     *         The issues to check.
     *
     * @return true if the issue is excluded, false otherwise.
     */
    private boolean isFileNameExcluded(final Issue issue) {
        boolean excluded = false;

        for (String regex : fileNameExcludes) {
            if (Pattern.matches(regex, issue.getFileName())) {
                excluded = true;
                break;
            }
        }

        return excluded;
    }

    /**
     * Checks if the issue is excluded depending on its package name.
     *
     * @param issue
     *         The issues to check.
     *
     * @return true if the issue is excluded, false otherwise.
     */
    private boolean isPackageNameExcluded(final Issue issue) {
        boolean excluded = false;

        for (String regex : packageNameExcludes) {
            if (Pattern.matches(regex, issue.getPackageName())) {
                excluded = true;
                break;
            }
        }


        return excluded;
    }

    /**
     * Checks if the issue is excluded depending on its module name.
     *
     * @param issue
     *         The issues to check.
     *
     * @return true if the issue is excluded, false otherwise.
     */
    private boolean isModuleNameExcluded(final Issue issue) {
        boolean excluded = false;

        for (String regex : moduleNameExcludes) {
            if (Pattern.matches(regex, issue.getModuleName())) {
                excluded = true;
                break;
            }
        }


        return excluded;
    }

    /**
     * Checks if the issue is excluded depending on its category.
     *
     * @param issue
     *         The issues to check.
     *
     * @return true if the issue is excluded, false otherwise.
     */
    private boolean isCategoryExcluded(final Issue issue) {
        boolean excluded = false;

        for (String regex : categoryExcludes) {
            if (Pattern.matches(regex, issue.getCategory())) {
                excluded = true;
                break;
            }
        }


        return excluded;
    }

    /**
     * Checks if the issue is excluded depending on its type.
     *
     * @param issue
     *         The issues to check.
     *
     * @return true if the issue is excluded, false otherwise.
     */
    private boolean isTypeExcluded(final Issue issue) {
        boolean excluded = false;

        for (String regex : typeExcludes) {
            if (Pattern.matches(regex, issue.getType())) {
                excluded = true;
                break;
            }
        }


        return excluded;
    }

    /**
     * Checks if any include filter is set and so the include filter is active.
     *
     * @return true if any include filter is set, false otherwise.
     */
    private boolean isIncludeFilterActive() {
        return fileNameIncludes.size() > 0 || packageNameIncludes.size() > 0 || moduleNameIncludes.size() > 0
                || categoryIncludes.size() > 0 || typeIncludes.size() > 0;
    }
}
