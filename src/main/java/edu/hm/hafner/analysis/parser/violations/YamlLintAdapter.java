package edu.hm.hafner.analysis.parser.violations;

import se.bjurr.violations.lib.parsers.YAMLlintParser;

/**
 * Parses YAMLLint results files.
 *
 * @author Ullrich Hafner
 */
public class YamlLintAdapter extends AbstractViolationAdapter {
    private static final long serialVersionUID = 1233385439193697120L;

    @Override
    YAMLlintParser createParser() {
        return new YAMLlintParser();
    }
}
