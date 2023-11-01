package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.CargoCheckParser;

/**
 * A descriptor for {@code rustc} compiler messages emitted by {@code cargo check
 *  * --message-format json}.
 *
 * @author Lorenz Munsch
 */
public class CargoDescriptor extends ParserDescriptor {
    private static final String ID = "cargo";
    private static final String NAME = "Cargo Check";

    public CargoDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new CargoCheckParser();
    }

    @Override
    public String getHelp() {
        return "Use commandline <code>cargo check --message-format json</code>";
    }
}
