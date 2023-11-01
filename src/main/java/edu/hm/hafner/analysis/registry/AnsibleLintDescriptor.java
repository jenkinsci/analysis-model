package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.AnsibleLintParser;

/**
 * A descriptor for Ansible Lint.
 *
 * @author Lorenz Munsch
 */
public class AnsibleLintDescriptor extends ParserDescriptor {
    private static final String ID = "ansiblelint";
    private static final String NAME = "Ansible Lint";

    public AnsibleLintDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new AnsibleLintParser();
    }

    @Override
    public String getHelp() {
        return "Use the flag -p.";
    }

    @Override
    public String getIconUrl() {
        return "https://raw.githubusercontent.com/benc-uk/icon-collection/master/logos/ansible.svg";
    }
}
