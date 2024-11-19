package edu.hm.hafner.analysis.parser.ccm;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Entity representing the Metric from CCM.exe output.
 *
 * <p>
 *     It has the {@link #complexity}, {@link #unit}, {@link #classification} and {@link #file} fields.
 * </p>
 *
 * @author Bruno P. Kinoshita - http://www.kinoshita.eti.br
 * @since 1.0
 */
@SuppressWarnings("all")
public class Metric {
    /**
     * Total CC of the method.
     */
    private int complexity;

    /**
     * String containing Class_Name::Method_Name
     */
    @CheckForNull
    private String unit;

    /**
     * CCM outputs a String with a classification such as "complex, high risk", "untestable, very high risk", etc. As
     * there is no documentation on which values are used to determine a method's CC classification CCM Plugin only
     * outputs this value. But does not use the information as a constraint in any place.
     */
    @CheckForNull
    private String classification;

    /**
     * The file name (e.g.:\ascx\request\open\form.ascx.cs).
     */
    @CheckForNull
    private String file;

    /**
     * The start line number of the measurement
     */
    private int startLineNumber;

    /**
     * The end line number of the measurement
     */
    private int endLineNumber;

    public int getComplexity() {
        return complexity;
    }

    public void setComplexity(int complexity) {
        this.complexity = complexity;
    }

    @CheckForNull
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @CheckForNull
    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    @CheckForNull
    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public int getStartLineNumber() {
        return startLineNumber;
    }

    public void setStartLineNumber(int startLineNumber) {
        this.startLineNumber = startLineNumber;
    }

    public int getEndLineNumber() {
        return endLineNumber;
    }

    public void setEndLineNumber(int endLineNumber) {
        this.endLineNumber = endLineNumber;
    }

}
