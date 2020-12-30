package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.AnsibleLintParser;

/**
 * A Descriptor for the AnsibleLint warnings.
 *
 * @author Lorenz Munsch
 */
class AnsibleLintDescriptor extends ParserDescriptor {

    private static final String ID = "ansiblelint";
    private static final String NAME = "Ansible Lint";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    AnsibleLintDescriptor() {
        super(ID, NAME, new AnsibleLintParser());
    }
}
