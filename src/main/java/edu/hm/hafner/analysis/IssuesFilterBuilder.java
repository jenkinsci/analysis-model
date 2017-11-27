package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Issue Filter Builder. Builder to create a new IssueFilter
 */
public class IssuesFilterBuilder {

    private List<Predicate<Issue>> include = new ArrayList<>();
    private List<Predicate<Issue>> exclude = new ArrayList<>();

    /**
     * Split several regexe.
     *
     * @param regexe
     *         input
     *
     * @return list of the singel regexe
     */
    private String[] cutRegexIntoSingeRegex(String regexe) {
        List<String> out = new ArrayList<>();

        return regexe.split("[ ,]");

    }

    /**
     * add a include filter for the Filename.
     *
     * @param regex
     *         filter definition
     *
     * @return this
     */
    IssuesFilterBuilder addIncludeFileName(String regex) {
        for (String singeRegex : cutRegexIntoSingeRegex(regex)) {
            include.add((Issue i) -> i.getFileName().matches(singeRegex));
        }
        return this;
    }

    /**
     * add a include filter for the package name.
     *
     * @param regex
     *         filter definition
     *
     * @return this
     */
    IssuesFilterBuilder addIncludePackageName(String regex) {
        for (String singeRegex : cutRegexIntoSingeRegex(regex)) {
            include.add((Issue i) -> i.getPackageName().matches(singeRegex));
        }
        return this;
    }

    /**
     * add a include filter for the module name.
     *
     * @param regex
     *         filter definition
     *
     * @return this
     */
    IssuesFilterBuilder addIncludeModuleName(String regex) {
        for (String singeRegex : cutRegexIntoSingeRegex(regex)) {
            include.add((Issue i) -> i.getModuleName().matches(singeRegex));
        }
        return this;
    }

    /**
     * add a include filter for the category.
     *
     * @param regex
     *         filter definition
     *
     * @return this
     */
    IssuesFilterBuilder addIncludeCategory(String regex) {
        for (String singeRegex : cutRegexIntoSingeRegex(regex)) {
            include.add((Issue i) -> i.getCategory().matches(singeRegex));
        }
        return this;
    }

    /**
     * add a include filter for the Type.
     *
     * @param regex
     *         filter definition
     *
     * @return this
     */
    IssuesFilterBuilder addIncludeType(String regex) {
        for (String singeRegex : cutRegexIntoSingeRegex(regex)) {
            include.add((Issue i) -> i.getType().matches(singeRegex));
        }
        return this;
    }

    /**
     * add a exclude filter for the Filename.
     *
     * @param regex
     *         filter definition
     *
     * @return this
     */
    IssuesFilterBuilder addExcludeFileName(String regex) {
        for (String singeRegex : cutRegexIntoSingeRegex(regex)) {
            exclude.add((Issue i) -> i.getFileName().matches(singeRegex));
        }
        return this;
    }

    /**
     * add a exclude filter for the package.
     *
     * @param regex
     *         filter definition
     *
     * @return this
     */
    IssuesFilterBuilder addExcludePackageName(String regex) {
        for (String singeRegex : cutRegexIntoSingeRegex(regex)) {
            exclude.add((Issue i) -> i.getPackageName().matches(singeRegex));
        }
        return this;
    }

    /**
     * add a exclude filter for the module.
     *
     * @param regex
     *         filter definition
     *
     * @return this
     */
    IssuesFilterBuilder addExcludeModuleName(String regex) {
        for (String singeRegex : cutRegexIntoSingeRegex(regex)) {
            exclude.add((Issue i) -> i.getModuleName().matches(singeRegex));
        }
        return this;
    }

    /**
     * add a exclude filter for the category.
     *
     * @param regex
     *         filter definition
     *
     * @return this
     */
    IssuesFilterBuilder addExcludeCategory(String regex) {
        for (String singeRegex : cutRegexIntoSingeRegex(regex)) {
            exclude.add((Issue i) -> i.getCategory().matches(singeRegex));
        }
        return this;
    }

    /**
     * add a exclude filter for the type.
     *
     * @param regex
     *         filter definition
     *
     * @return this
     */
    IssuesFilterBuilder addExcludeType(String regex) {
        for (String singeRegex : cutRegexIntoSingeRegex(regex)) {
            exclude.add((Issue i) -> i.getType().matches(singeRegex));
        }
        return this;
    }

    /**
     * Build a new IssueFilter with the given parameter.
     *
     * @return filter obj
     */
    IssuesFilter build() {
        // if no definition include all

        // add filter rules
        IssuesFilter filter = new IssuesFilter();
        filter.includeFilter(include);
        filter.excludeFilter(exclude);

        return filter;
    }
}
