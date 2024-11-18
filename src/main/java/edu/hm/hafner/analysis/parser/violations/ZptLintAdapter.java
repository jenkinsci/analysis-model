package edu.hm.hafner.analysis.parser.violations;

import java.io.Serial;

import se.bjurr.violations.lib.parsers.ZPTLintParser;

/**
 * Parses ZPTLint results files.
 *
 * @author Ullrich Hafner
 */
public class ZptLintAdapter extends AbstractViolationAdapter {
    @Serial
    private static final long serialVersionUID = -6204265426578715957L;

    @Override
    ZPTLintParser createParser() {
        return new ZPTLintParser();
    }
}
