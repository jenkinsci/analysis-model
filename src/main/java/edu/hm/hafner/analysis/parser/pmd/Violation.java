package edu.hm.hafner.analysis.parser.pmd;

/**
 * Java Bean class for a violation of the PMD format.
 *
 * @author Ullrich Hafner
 */
@SuppressWarnings("all")
public class Violation {
    /** Type of warning. */
    private String rule;
    /** Category of warning. */
    private String ruleset;

    private String externalInfoUrl;
    private String javaPackage;
    private int priority;
    private String message;
    private int beginline;
    private int endline;
    private int begincolumn;
    private int endcolumn;

    // CHECKSTYLE:OFF
    public String getRule() {
        return rule;
    }
    public void setRule(final String rule) {
        this.rule = rule;
    }
    public String getRuleset() {
        return ruleset;
    }
    public void setRuleset(final String ruleset) {
        this.ruleset = ruleset;
    }
    public String getExternalInfoUrl() {
        return externalInfoUrl;
    }
    public void setExternalInfoUrl(final String externalInfoUrl) {
        this.externalInfoUrl = externalInfoUrl;
    }
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

