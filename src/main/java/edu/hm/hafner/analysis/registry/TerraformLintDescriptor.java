package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.TerraformLintParser;

/**
 * A descriptor for Terraform Lint (tflint) JSON reports.
 *
 * @author Akash Manna
 */
class TerraformLintDescriptor extends ParserDescriptor {
    private static final String ID = "tflint";
    private static final String NAME = "Terraform Lint";

    TerraformLintDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new TerraformLintParser();
    }

    @Override
    public String getPattern() {
        return "**/tflint-report.json";
    }

    @Override
    public String getHelp() {
        return "Use commandline <code>tflint --format json</code> to generate JSON output.<br/>"
                + "See <a href='https://github.com/terraform-linters/tflint'>tflint on GitHub</a> for usage details.";
    }

    @Override
    public String getUrl() {
        return "https://github.com/terraform-linters/tflint";
    }
}
