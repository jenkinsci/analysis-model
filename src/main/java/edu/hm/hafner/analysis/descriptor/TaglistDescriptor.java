package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.TaglistParser;

/**
 * A Descriptor for the Tag List parser.
 *
 * @author Lorenz Munsch
 */
class TaglistDescriptor extends ParserDescriptor {

    private static final String ID = "taglist";
    private static final String NAME = "Maven Taglist Plugin";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    TaglistDescriptor() {
        super(ID, NAME, new TaglistParser());
    }
}
