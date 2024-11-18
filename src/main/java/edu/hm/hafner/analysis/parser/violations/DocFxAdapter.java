package edu.hm.hafner.analysis.parser.violations;

import java.io.Serial;

import se.bjurr.violations.lib.model.SEVERITY;
import se.bjurr.violations.lib.model.Violation;
import se.bjurr.violations.lib.parsers.DocFXParser;

import static se.bjurr.violations.lib.model.SEVERITY.*;

/**
 * Parses DocFX files.
 *
 * @author Ullrich Hafner
 */
public class DocFxAdapter extends AbstractViolationAdapter {
    @Serial
    private static final long serialVersionUID = 2162266195669804761L;

    @Override
    DocFXParser createParser() {
        return new DocFXParser();
    }

    @Override
    boolean isValid(final Violation violation) {
        SEVERITY severity = violation.getSeverity();
        return severity != INFO;
    }
}
