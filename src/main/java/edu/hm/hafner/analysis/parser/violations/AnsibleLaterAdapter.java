package edu.hm.hafner.analysis.parser.violations;

import java.io.Serial;

import se.bjurr.violations.lib.parsers.AnsibleLaterParser;

/**
 * Parses Ansible-Later files.
 *
 * @author Ullrich Hafner
 */
public class AnsibleLaterAdapter extends AbstractViolationAdapter {
    @Serial
    private static final long serialVersionUID = 2441144477814669681L;

    @Override
    AnsibleLaterParser createParser() {
        return new AnsibleLaterParser();
    }
}
