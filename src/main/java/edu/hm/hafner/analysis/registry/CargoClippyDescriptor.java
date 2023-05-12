package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.CargoClippyParser;

/**
 * A descriptor for Cargo Clippy.
 *
 * @author Ullrich Hafner
 */
class CargoClippyDescriptor extends ParserDescriptor {
    private static final String ID = "clippy";
    private static final String NAME = "Cargo Clippy";

    CargoClippyDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new CargoClippyParser();
    }

    @Override
    public String getUrl() {
        return "https://github.com/rust-lang/rust-clippy";
    }
}
