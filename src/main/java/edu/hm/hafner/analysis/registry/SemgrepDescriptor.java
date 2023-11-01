package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.violations.SemgrepAdapter;

/**
 * A descriptor for Semgrep.
 *
 * @author Ullrich Hafner
 */
public class SemgrepDescriptor extends ParserDescriptor {
    private static final String ID = "semgrep";
    private static final String NAME = "Semgrep";

    public SemgrepDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new SemgrepAdapter();
    }

    @Override
    public String getHelp() {
        return "Use <code>--json</code>";
    }

    @Override
    public String getUrl() {
        return "https://semgrep.dev/";
    }

    @Override
    public String getIconUrl() {
        return "https://raw.githubusercontent.com/returntocorp/semgrep/develop/semgrep.svg";
    }
}
