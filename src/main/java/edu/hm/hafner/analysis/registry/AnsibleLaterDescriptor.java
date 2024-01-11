package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.violations.AnsibleLaterAdapter;

/**
 * A descriptor for Ansible Later.
 *
 * @author Ullrich Hafner
 */
class AnsibleLaterDescriptor extends ParserDescriptor {
    private static final String ID = "ansible-later";
    private static final String NAME = "Ansible Later";

    AnsibleLaterDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new AnsibleLaterAdapter();
    }

    @Override
    public String getHelp() {
        return "Use -p flag.";
    }

    @Override
    public String getUrl() {
        return "https://github.com/thegeeklab/ansible-later";
    }

    @Override
    public String getIconUrl() {
        return "https://raw.githubusercontent.com/benc-uk/icon-collection/master/logos/ansible.svg";
    }
}
