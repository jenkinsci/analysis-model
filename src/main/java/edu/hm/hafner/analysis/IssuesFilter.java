package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;

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


    public Issues filter(final Issues issuesToFilter) {
        Issues filteredIssues = new Issues();

        for (Issue issue : issuesToFilter) {
            if (!hasExcludedFileName(issue)) {
                filteredIssues.add(issue);
            }
        }

        return filteredIssues;
    }

    private boolean hasExcludedFileName(final Issue issue) {
        boolean excluded = false;

        for (String regex : fileNameExcludes) {
            if (Pattern.matches(regex, issue.getFileName())) {
                excluded = true;
                break;
            }
        }

        return excluded;
    }
}
