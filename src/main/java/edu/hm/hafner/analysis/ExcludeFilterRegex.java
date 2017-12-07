package edu.hm.hafner.analysis;

import edu.hm.hafner.analysis.IssuesFilter.FilterRegex;

public class ExcludeFilterRegex implements FilterRegex {

    String regex;

    ExcludeFilterRegex(String regex) {
        this.regex = regex;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ExcludeFilterRegex that = (ExcludeFilterRegex) o;

        return regex != null ? regex.equals(that.regex) : that.regex == null;
    }

    @Override
    public int hashCode() {
        return regex != null ? regex.hashCode() : 0;
    }

    @Override
    public String getExpression() {
        return regex;
    }
}
