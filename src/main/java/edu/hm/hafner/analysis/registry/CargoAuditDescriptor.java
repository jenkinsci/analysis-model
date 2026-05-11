package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report.IssueType;
import edu.hm.hafner.analysis.parser.CargoAuditParser;

/**
 * A descriptor for Cargo Audit security vulnerability scanner.
 *
 * @author Akash Manna
 */
class CargoAuditDescriptor extends ParserDescriptor {
    private static final String ID = "cargo-audit";
    private static final String NAME = "Cargo Audit";

    CargoAuditDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueType getType() {
        return IssueType.VULNERABILITY;
    }

    @Override
    public String getUrl() {
        return "https://github.com/rustsec/cargo-audit";
    }

    @Override
    public String getPattern() {
        return "**/cargo-audit.json";
    }

    @Override
    public IssueParser create(final Option... options) {
        return new CargoAuditParser();
    }

    @Override
    public String getHelp() {
        return "Use commandline <code>cargo audit --json -o cargo-audit.json</code> to generate JSON output.<br/>"
                + "See <a href='https://github.com/rustsec/cargo-audit'>cargo-audit on GitHub</a> for usage details.";
    }
}
