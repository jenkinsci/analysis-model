package edu.hm.hafner.analysis.parser.jcreport;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * This the Item-Class The Java-Bean-Conformity was chosen due to the digesters style of assigning. It represents the
 * Item-Tags within the report.xml. Items have properties, that are mandatory to create a warning.
 *
 * @author Johann Vierthaler, johann.vierthaler@web.de
 */
@SuppressWarnings("PMD.DataClass")
public class Item {

    @CheckForNull
    private String column;
    @CheckForNull
    private String findingtype;
    @CheckForNull
    private String line;
    @CheckForNull
    private String message;
    @CheckForNull
    private String origin;
    @CheckForNull
    private String severity;
    @CheckForNull
    private String endcolumn;

    /**
     * Although this property is not used. It was decided to keep it available when Jenkins is modified and needs access
     * to this field;
     */
    @CheckForNull
    private String endline;

    /**
     * Getter for column-Field.
     *
     * @return column string
     */
    @CheckForNull
    public String getColumn() {
        return column;
    }

    /**
     * Setter for Column-Field.
     *
     * @param column setter
     */
    public void setColumn(@CheckForNull final String column) {
        this.column = column;
    }

    /**
     * Getter for findingtype-Field.
     *
     * @return findingtype getter
     */
    @CheckForNull
    public String getFindingtype() {
        return findingtype;
    }

    /**
     * Setter for findingtype-Field.
     *
     * @param findingtype setter
     */
    public void setFindingtype(@CheckForNull final String findingtype) {
        this.findingtype = findingtype;
    }

    /**
     * Getter for line-Field.
     *
     * @return line getter
     */
    @CheckForNull
    public String getLine() {
        return line;
    }

    /**
     * Setter for line-Field.
     *
     * @param line setter
     */
    public void setLine(@CheckForNull final String line) {
        this.line = line;
    }

    /**
     * Getter for message-Field.
     *
     * @return message getter
     */
    @CheckForNull
    public String getMessage() {
        return message;
    }

    /**
     * Setter for message-Field.
     *
     * @param message setter
     */
    public void setMessage(@CheckForNull final String message) {
        this.message = message;
    }

    /**
     * Getter for origin-Field.
     *
     * @return origin getter
     */
    @CheckForNull
    public String getOrigin() {
        return origin;
    }

    /**
     * Setter for origin-Field.
     *
     * @param origin setter
     */
    public void setOrigin(@CheckForNull final String origin) {
        this.origin = origin;
    }

    /**
     * Getter for severity-Field.
     *
     * @return severity getter
     */
    @CheckForNull
    public String getSeverity() {
        return severity;
    }

    /**
     * Setter for severtiy-Field.
     *
     * @param severity setter
     */
    public void setSeverity(@CheckForNull final String severity) {
        this.severity = severity;
    }


    /**
     * Getter for endline-Field.
     *
     * @return endline getter
     */
    @CheckForNull
    public String getEndline() {
        return endline;
    }

    /**
     * Setter for endline-Field.
     *
     * @param endline setter
     */
    public void setEndline(@CheckForNull final String endline) {
        this.endline = endline;
    }

    /**
     * Getter for endcolumn-Field.
     *
     * @return endcolumn getter
     */
    @CheckForNull
    public String getEndcolumn() {
        return endcolumn;
    }

    /**
     * Setter for endcolumn-Field.
     *
     * @param endcolumn setter
     */
    public void setEndcolumn(@CheckForNull final String endcolumn) {
        this.endcolumn = endcolumn;
    }
}
