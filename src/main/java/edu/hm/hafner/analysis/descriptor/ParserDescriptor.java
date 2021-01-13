package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.IssueParser;

/**
 * Interface to descripe all descriptors.
 *
 * @author Lorenz Munsch
 *
 */
public abstract class ParserDescriptor {


    private final String id;
    private final  String name;
    private final  IssueParser issueParser;

    /**
     *
     * ctor for the abstract Parser Descriptor class.
     */
    public ParserDescriptor(final String id, final String name, final IssueParser issueParser) {
        this.id = id;
        this.name = name;
        this.issueParser = issueParser;
    }

    /**
     * Get the technical id.
     *
     * @return the technical id of the parser
     */
    public String getId() {
        return id;
    }

    /**
     *
     * Name to identify the parser.
     *
     * @return the identification string
     */
    public String getName() {
        return name;
    }

    /**
     *
     * Creates a new Parser.
     *
     * @return the parser
     */
    public IssueParser createParser() {
        return issueParser;
    }

    /**
     *
     * Defines the default resultfile name and extension.
     *
     * @return the name of the resultfile
     */
    public String getPattern() {
        return "";
    }

    /**
     *
     * Determines the checker URL.
     *
     * @return the checker URL or empty String
     */
    public String getUrl() {
        return "";
    }

}
