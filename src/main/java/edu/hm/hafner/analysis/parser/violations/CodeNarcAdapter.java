package edu.hm.hafner.analysis.parser.violations;

import java.io.Serial;

import se.bjurr.violations.lib.parsers.CodeNarcParser;

/**
 * Parses CodeNarc files.
 *
 * @author Ullrich Hafner
 */
public class CodeNarcAdapter extends AbstractViolationAdapter {
    @Serial
    private static final long serialVersionUID = -5699747899173867285L;

    @Override
    CodeNarcParser createParser() {
        return new CodeNarcParser();
    }
}
