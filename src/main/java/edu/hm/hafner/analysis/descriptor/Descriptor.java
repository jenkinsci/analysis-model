package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.IssueParser;

public interface Descriptor {

    /**
     *
     * Name to identify the warning.
     *
     * @return the identification string
     */
    String getName();

    /**
     *
     * Creates a new Parser.
     *
     * @return the parser
     */
    IssueParser createParser();

    /**
     *
     * Defines the default resultfile name and extension.
     *
     * @return the name of the resultfile
     */
    String getPattern();

    /**
     *
     * Determines the checker URL.
     *
     * @return the checker URL or empty String
     */
    String getUrl();

}
