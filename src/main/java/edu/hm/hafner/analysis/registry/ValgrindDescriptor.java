package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.violations.ValgrindAdapter;

/**
 * A descriptor for Valgrind.
 */
public class ValgrindDescriptor extends ParserDescriptor {
    private static final String ID = "valgrind";
    private static final String NAME = "Valgrind";

    ValgrindDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new ValgrindAdapter();
    }

    @Override
    public String getHelp() {
        return "Use option --xml=yes";
    }
}