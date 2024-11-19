package edu.hm.hafner.analysis.parser.violations;

import java.io.Serial;

import se.bjurr.violations.lib.parsers.XMLLintParser;

/**
 * Parses XMLLint results files.
 *
 * @author Ullrich Hafner
 */
public class XmlLintAdapter extends AbstractViolationAdapter {
    @Serial
    private static final long serialVersionUID = -4191378552617352099L;

    @Override
    XMLLintParser createParser() {
        return new XMLLintParser();
    }
}
