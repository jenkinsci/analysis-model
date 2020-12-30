package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.Armcc5CompilerParser;

public class Armcc5CompilerDescriptor implements Descriptor {

    private static final String ID = "Armcc5Compiler";

    /**
     *
     * Name to identify the warning.
     *
     * @return the identification string
     */
    @Override
    public String getName() {
        return ID;
    }

    /**
     *
     * Creates a new Parser.
     *
     * @return the parser
     */
    @Override
    public IssueParser createParser() {
        return new Armcc5CompilerParser();
    }

    /**
     *
     * Defines the default resultfile name and extension.
     *
     * @return the name of the resultfile
     */
    @Override
    public String getPattern() {
        return "";
    }

    /**
     *
     * Determines the checker URL.
     *
     * @return the checker URL or empty String
     */
    @Override
    public String getUrl() {
        return "";
    }

}
