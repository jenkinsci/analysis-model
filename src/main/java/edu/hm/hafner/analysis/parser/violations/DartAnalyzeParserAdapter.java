package edu.hm.hafner.analysis.parser.violations;

import java.io.Serial;

import se.bjurr.violations.lib.parsers.MachineParser;

/**
 * Parses Dart Analyze logger messages.
 *
 * @author Ullrich Hafner
 */
public class DartAnalyzeParserAdapter extends AbstractViolationAdapter {
    @Serial
    private static final long serialVersionUID = 50827863228591461L;

    @Override
    MachineParser createParser() {
        return new MachineParser();
    }
}
