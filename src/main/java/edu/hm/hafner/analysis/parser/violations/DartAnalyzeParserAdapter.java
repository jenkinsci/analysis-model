package edu.hm.hafner.analysis.parser.violations;

import se.bjurr.violations.lib.parsers.MachineParser;

/**
 * Parses Dart Analyze logger messages.
 *
 * @author Ullrich Hafner
 */
public class DartAnalyzeParserAdapter extends AbstractViolationAdapter {
    private static final long serialVersionUID = 50827863228591461L;

    @Override
    MachineParser createParser() {
        return new MachineParser();
    }
}
