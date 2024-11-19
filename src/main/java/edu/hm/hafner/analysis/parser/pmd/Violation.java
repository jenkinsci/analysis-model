package edu.hm.hafner.analysis.parser.pmd;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Java Bean class for a violation of the PMD format.
 *
 * @author Ullrich Hafner
 */
@SuppressWarnings("all")
public class Violation {
    /** Type of warning. */
    @CheckForNull
    private String rule;
    /** Category of warning. */
    @CheckForNull
    private String ruleset;

    @CheckForNull
    private String externalInfoUrl;
    @CheckForNull
    private String javaPackage;
    private int priority;
    @CheckForNull
    private String message;
    private int beginline;
    private int endline;
    private int begincolumn;
    private int endcolumn;

    // CHECKSTYLE:OFF
    @CheckForNull
    public String getRule() {
        return rule;
    }

    public void setRule(final String rule) {
        this.rule = rule;
    }

    @CheckForNull
    public String getRuleset() {
        return ruleset;
    }

    public void setRuleset(final String ruleset) {
        this.ruleset = ruleset;
    }

    @CheckForNull
    public String getExternalInfoUrl() {
        return externalInfoUrl;
    }

    public void setExternalInfoUrl(final String externalInfoUrl) {
        this.externalInfoUrl = externalInfoUrl;
    }

    @CheckForNull
    public String getPackage() {
        return javaPackage;
    }

    public void setPackage(final String packageName) {
        javaPackage = packageName;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(final int priority) {
        this.priority = priority;
    }

    @CheckForNull
    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public int getBeginline() {
        return beginline;
    }

    public void setBeginline(final int beginline) {
        this.beginline = beginline;
    }

    public int getEndline() {
        return endline;
    }

    public void setEndline(final int endline) {
        this.endline = endline;
    }

    public int getEndcolumn() {
        return endcolumn;
    }

    public void setEndcolumn(final int endcolumn) {
        this.endcolumn = endcolumn;
    }

    public int getBegincolumn() {
        return begincolumn;
    }

    public void setBegincolumn(final int begincolumn) {
        this.begincolumn = begincolumn;
    }
}
