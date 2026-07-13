package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report.IssueType;
import edu.hm.hafner.analysis.parser.OpenApiValidatorParser;

/**
 * A descriptor for IBM OpenAPI Validator JSON reports.
 *
 * @author Akash Manna
 */
class OpenApiValidatorDescriptor extends ParserDescriptor {
    private static final String ID = "openapi-validator";
    private static final String NAME = "OpenAPI Validator";

    OpenApiValidatorDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueType getType() {
        return IssueType.WARNING;
    }
  
    @Override
    protected IssueParser create(final Option... options) {
        return new OpenApiValidatorParser();
    }

    @Override
    public String getPattern() {
        return "**/openapi-validator-report.json";
    }

    @Override
    public String getHelp() {
        return "Use commandline <code>lint-openapi --json your-api.yaml &gt; openapi-validator-report.json</code> to generate JSON output.<br/>"
                + "See <a href='https://github.com/IBM/openapi-validator'>IBM OpenAPI Validator on GitHub</a> for usage details.";
    }

    @Override
    public String getUrl() {
        return "https://github.com/IBM/openapi-validator";
    }
}
