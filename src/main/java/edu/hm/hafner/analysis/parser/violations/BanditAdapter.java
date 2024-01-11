package edu.hm.hafner.analysis.parser.violations;

import se.bjurr.violations.lib.parsers.CLangParser;

/**
 * Parses Bandit files.
 *
 * @author Ullrich Hafner
 */
public class BanditAdapter extends AbstractViolationAdapter {
    private static final long serialVersionUID = 2441144477814669681L;

    @Override
    CLangParser createParser() {
        return new CLangParser();
    }
}
