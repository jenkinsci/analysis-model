package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Filter Issues.
 * With this class you can reduce the number of Issue in Issues by defining Filter criteria. As filter you can define a Lamda Expression.
 *
 * An Issue stays if in at least one include filter returns true and non exclude filter returns true.
 * if no include filter defined all Issues gets included
 *
 * @author Sebastian Balz
 */
public class IssuesFilter {
    private final List<Predicate<Issue>> includeFilter = new ArrayList<>();
    private final List<Predicate<Issue>> exludeFilter = new ArrayList<>();

    /**
     * Add a list of include filter..
     * if a Filter returns true
     * @param addFilter list of Filter
     */
    void includeFilter(List<Predicate<Issue>> addFilter) {
        includeFilter.addAll(addFilter);
    }

    /**
     * add singe include filter.
     * @param addFilter single filter
     */
    void includeFilter(Predicate<Issue> addFilter) {
        includeFilter.add(addFilter);
    }

    /**
     * Add list of exclude filter.
     * @param addFilter filter list
     */
    void excludeFilter(List<Predicate<Issue>> addFilter) {
        exludeFilter.addAll(addFilter);
    }

    /**
     * add singe exclude filter.
     * @param addFilter single exlude filter
     */
    void excludeFilter(Predicate<Issue> addFilter) {
        exludeFilter.add(addFilter);
    }

    /**
     * checks if the Issue is in one include filter.
     * @param toCheck issue to check
     * @return true if in include
     */
    private boolean isInInclude(Issue toCheck) {
        for (Predicate<Issue> checker : includeFilter) {
            if (checker.test(toCheck)) {
                return true;
            }
        }
        return false;
    }

    /**
     * check if the Issue is in one exclude filter.
     * @param toCheck issue to check
     * @return true if in exclude
     */
    private boolean isInExclude(Issue toCheck) {
        for (Predicate<Issue> checker : exludeFilter) {
            if (checker.test(toCheck)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Create a new filtered Issues object.
     * It contains all Issues which are in an include list but not in an exclude list
     * @param toCheck origin Issues
     * @return  new filtered Issues
     */
    public Issues executeFilter(Issues toCheck) {
        if (includeFilter.size() == 0) {
            includeFilter.add((Issue i) -> true);
        }
        Issues result = new Issues();
        for (Issue current : toCheck.all()) {
            if (isInInclude(current) && !isInExclude(current)) {
                result.add(current);
            }
        }

        return result;
    }
}
