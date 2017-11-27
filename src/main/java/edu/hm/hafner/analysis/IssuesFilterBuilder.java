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
public class IssuesFilterBuilder {

    List<Predicate<Issue>> include = new ArrayList();
    List<Predicate<Issue>> exclude = new ArrayList();

    private String[] cutRegexsIntoSingeRegex(String regexe) {
        List<String> out = new ArrayList<>();

        return regexe.split(" |,");

    }

    IssuesFilterBuilder addIncludFileName(String regex) {
        for (String singeRegex : cutRegexsIntoSingeRegex(regex)) {
            include.add((Issue i) ->i.getFileName().matches(singeRegex));
        }
        return this;
    }

    IssuesFilterBuilder addIncludPackageName(String regex) {
        for (String singeRegex : cutRegexsIntoSingeRegex(regex)) {
            include.add((Issue i) ->i.getPackageName().matches(singeRegex));
        }
        return this;
    }

    IssuesFilterBuilder addIncludModuleName(String regex) {
        for (String singeRegex : cutRegexsIntoSingeRegex(regex)) {
            include.add((Issue i) ->i.getModuleName().matches(singeRegex));
        }
        return this;
    }

    IssuesFilterBuilder addIncluCategory(String regex) {
        for (String singeRegex : cutRegexsIntoSingeRegex(regex)) {
            include.add((Issue i) ->i.getCategory().matches(singeRegex));
        }
        return this;
    }

    IssuesFilterBuilder addIncludType(String regex) {
        for (String singeRegex : cutRegexsIntoSingeRegex(regex)) {
            include.add((Issue i) ->i.getType().matches(singeRegex));
        }
        return this;
    }

    IssuesFilterBuilder addExcludFileName(String regex) {
        for (String singeRegex : cutRegexsIntoSingeRegex(regex)) {
            exclude.add((Issue i) ->i.getFileName().matches(singeRegex));
        }
        return this;
    }

    IssuesFilterBuilder addExcludPackageName(String regex) {
        for (String singeRegex : cutRegexsIntoSingeRegex(regex)) {
            exclude.add((Issue i) ->i.getPackageName().matches(singeRegex));
        }
        return this;
    }

    IssuesFilterBuilder addExcludModuleName(String regex) {
        for (String singeRegex : cutRegexsIntoSingeRegex(regex)) {
            exclude.add((Issue i) ->i.getModuleName().matches(singeRegex));
        }
        return this;
    }

    IssuesFilterBuilder addExcluCategory(String regex) {
        for (String singeRegex : cutRegexsIntoSingeRegex(regex)) {
            exclude.add((Issue i) ->i.getCategory().matches(singeRegex));
        }
        return this;
    }

    IssuesFilterBuilder addExcludType(String regex) {
        for (String singeRegex : cutRegexsIntoSingeRegex(regex)) {
            exclude.add((Issue i) ->i.getType().matches(singeRegex));
        }
        return this;
    }

    IssuesFilter build() {
        // if no definition include all

        // add filter rules
        IssuesFilter filter = new IssuesFilter();
        filter.includFilter(include);
        filter.excludFilter(exclude);

        return filter;
    }


    Issues executeFilter(Issues toCheck) {
        return toCheck;
    }
}
