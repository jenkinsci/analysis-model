package edu.hm.hafner.analysis.parser.violations;

import java.io.Serial;

import se.bjurr.violations.lib.parsers.CodeClimateParser;

/**
 * Parses CodeClimate JSON files.
 *
 * @author Ullrich Hafner
 */
public class CodeClimateAdapter extends AbstractViolationAdapter {
    @Serial
    private static final long serialVersionUID = 673249539417291948L;

    @Override
    CodeClimateParser createParser() {
        return new CodeClimateParser();
    }
}
