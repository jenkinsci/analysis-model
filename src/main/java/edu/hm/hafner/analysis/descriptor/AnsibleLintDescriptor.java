package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.AnsibleLintParser;

/**
 * A Descriptor for the AnsibleLint warnings.
 *
 * @author Lorenz Munsch
 */
public class AnsibleLintDescriptor extends ParserDescriptor {

    private static final String ID = "ansible_lint";
    private static final String NAME = "AnsibleLint";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    public AnsibleLintDescriptor() {
        super(ID, NAME, new AnsibleLintParser());
    }
}
