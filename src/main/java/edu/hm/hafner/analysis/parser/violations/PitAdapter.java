package edu.hm.hafner.analysis.parser.violations;

import se.bjurr.violations.lib.model.SEVERITY;
import se.bjurr.violations.lib.model.Violation;
import se.bjurr.violations.lib.parsers.PiTestParser;

import edu.hm.hafner.analysis.Priority;

/**
 * Parses PIT results files.
 *
 * @author Ullrich Hafner
 */
public class PitAdapter extends AbstractViolationAdapter {
    private static final long serialVersionUID = -7811207963029906228L;

    /**
     * Creates a new instance of {@link PitAdapter}.
     */
    public PitAdapter() {
        super(Rule.TYPE);
    }

    @Override
    protected PiTestParser createParser() {
        return new PiTestParser();
    }

    @Override
    protected boolean isValid(final Violation violation) {
        return "false".equals(violation.getSpecifics().get("detected"));
    }

    @Override
    protected Priority convertSeverity(final SEVERITY severity, final Violation violation) {
        return "KILLED".equals(violation.getSpecifics().get("status")) ? Priority.HIGH : Priority.NORMAL;
    }
}
