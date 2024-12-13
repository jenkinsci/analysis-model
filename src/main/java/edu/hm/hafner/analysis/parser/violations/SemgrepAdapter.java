package edu.hm.hafner.analysis.parser.violations;

import java.io.Serial;

import edu.hm.hafner.analysis.Report.IssueType;

import se.bjurr.violations.lib.parsers.SemgrepParser;

/**
 * Parses PyDocStyle results files.
 *
 * @author Ullrich Hafner
 */
public class SemgrepAdapter extends AbstractViolationAdapter {
    @Serial
    private static final long serialVersionUID = 1119003057153007718L;

    @Override
    SemgrepParser createParser() {
        return new SemgrepParser();
    }

    @Override
    public IssueType getType() {
        return IssueType.VULNERABILITY;
    }
}
