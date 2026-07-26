package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.RustAnalyzerParser;

/**
 * A descriptor for rust-analyzer flycheck diagnostics.
 *
 * @author Akash Manna
 */
class RustAnalyzerDescriptor extends ParserDescriptor {
    private static final String ID = "rust-analyzer";
    private static final String NAME = "rust-analyzer";

    RustAnalyzerDescriptor() {
        super(ID, NAME);
    }

    @Override
    protected IssueParser create(final Option... options) {
        return new RustAnalyzerParser();
    }

    @Override
    public String getPattern() {
        return "**/rust-analyzer.json";
    }

    @Override
    public String getHelp() {
        return "Use commandline <code>rust-analyzer diagnostics &lt;path&gt;</code> to generate JSON output.<br/>"
                + "See <a href='https://github.com/rust-lang/rust-analyzer'>rust-analyzer on GitHub</a> for usage details.";
    }

    @Override
    public String getUrl() {
        return "https://github.com/rust-lang/rust-analyzer";
    }
}
