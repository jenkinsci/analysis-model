package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * ***************************************************************** Hochschule Muenchen Fakultaet 07 (Informatik)		**
 * Praktikum fuer Softwareentwicklung 1 IF1B  WS15/16	** *****************************************************************
 * Autor: Sebastian Balz					** Datum 26.11.2017											** Software Win 7 JDK8 Win 10 JDK8 Ubuntu 15.4 OpenJDK7	**
 * edu.hm.hafner.analysis                ** ***************************************************************** **
 * *****************************************************************
 */
public class IssuesFilter {
    List<Predicate<Issue>> includeFilter = new ArrayList<>();
    List<Predicate<Issue>> exludeFilter = new ArrayList<>();

    void includFilter(List<Predicate<Issue>> addFilter) {
        includeFilter.addAll(addFilter);
    }


    void excludFilter(List<Predicate<Issue>> addFilter) {
        exludeFilter.addAll(addFilter);
    }


    private boolean isInInclude(Issue toCheck) {
        for (Predicate<Issue> checker : includeFilter) {
            if (checker.test(toCheck)) {
                return true;
            }
        }
        return false;
    }
    private boolean isInExclude(Issue toCheck) {
        for (Predicate<Issue> checker : exludeFilter) {
            if (checker.test(toCheck)) {
                return true;
            }
        }
        return false;
    }
    Issues executeFilter(Issues toCheck) {
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
