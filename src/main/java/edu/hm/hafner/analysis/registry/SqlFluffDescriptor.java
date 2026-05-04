package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report.IssueType;
import edu.hm.hafner.analysis.parser.SqlFluffParser;

/**
 * Descriptor for SQLFluff JSON reports.
 *
 * @author Akash Manna
 */
class SqlFluffDescriptor extends ParserDescriptor {
    private static final String ID = "sqlfluff";
    private static final String NAME = "SQLFluff";

    SqlFluffDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueType getType() {
        return IssueType.WARNING;
    }

    @Override
    public IssueParser create(final Option... options) {
        return new SqlFluffParser();
    }

    @Override
    public String getPattern() {
        return "**/sqlfluff.json";
    }

    @Override
    public String getHelp() {
        return "Use commandline <code>sqlfluff lint --format json &gt; report.json</code> to generate JSON output.<br/>"
                + "See <a href='https://www.sqlfluff.com/'>SQLFluff documentation</a> for usage details.";
    }

    @Override
    public String getUrl() {
        return "https://www.sqlfluff.com/";
    }
}
