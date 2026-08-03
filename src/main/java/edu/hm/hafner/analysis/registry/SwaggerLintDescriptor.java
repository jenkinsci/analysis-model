package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report.IssueType;
import edu.hm.hafner.analysis.parser.SwaggerLintParser;

/**
 * A descriptor for Swagger Lint (swaggerlint) JSON reports.
 *
 * @author Akash Manna
 */
class SwaggerLintDescriptor extends ParserDescriptor {
    private static final String ID = "swagger-lint";
    private static final String NAME = "Swagger Lint";

    SwaggerLintDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueType getType() {
        return IssueType.WARNING;
    }

    @Override
    protected IssueParser create(final Option... options) {
        return new SwaggerLintParser();
    }

    @Override
    public String getPattern() {
        return "**/swagger-lint-report.json";
    }

    @Override
    public String getHelp() {
        return "Use commandline <code>swaggerlint /path/to/swagger.json 2>&amp;1 | tee swagger-lint-report.json</code>"
                + " or programmatically via Node.js and <code>JSON.stringify</code> to generate JSON output.<br/>"
                + "See <a href='https://github.com/antonk52/swaggerlint'>swaggerlint on GitHub</a> for usage details.";
    }

    @Override
    public String getUrl() {
        return "https://github.com/antonk52/swaggerlint";
    }
}
