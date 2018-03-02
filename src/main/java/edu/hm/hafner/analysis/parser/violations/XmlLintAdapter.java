package edu.hm.hafner.analysis.parser.violations;

import se.bjurr.violations.lib.parsers.XMLLintParser;

/**
 * Parses XMLLint results files.
 *
 * @author Ullrich Hafner
 */
public class XmlLintAdapter extends AbstractViolationAdapter {
    /**
     * Creates a new instance of {@link XmlLintAdapter}.
     */
    public XmlLintAdapter() {
        super(Rule.TYPE);
    }

    @Override
    protected XMLLintParser createParser() {
        return new XMLLintParser();
    }
}
