package edu.hm.hafner.analysis.parser.violations;

import se.bjurr.violations.lib.model.SEVERITY;
import se.bjurr.violations.lib.model.Violation;
import se.bjurr.violations.lib.parsers.PiTestParser;

import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Priority;

/**
 * Parses PIT results files.
 *
 * @author Ullrich Hafner
 */
public class PitAdapter extends AbstractViolationAdapter {
    private static final long serialVersionUID = -7811207963029906228L;
    private static final String STATUS = "status";
    private static final String DETECTED = "detected";

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
        return "false".equals(getSpecifics(violation, DETECTED));
    }

    @Override
    protected Priority convertSeverity(final SEVERITY severity, final Violation violation) {
        return "SURVIVED".equals(getSpecifics(violation, STATUS)) ? Priority.HIGH : Priority.NORMAL;
    }

    private String getSpecifics(final Violation violation, final String key) {
        return violation.getSpecifics().get(key);
    }

    @Override
    protected void extractAdditionalProperties(final IssueBuilder builder, final Violation violation) {
        builder.setCategory(getSpecifics(violation, STATUS));
    }
}
