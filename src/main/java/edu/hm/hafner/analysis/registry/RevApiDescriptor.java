package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.RevApiParser;

public class RevApiDescriptor extends ParserDescriptor {
    private static final String ID = "revapi";
    private static final String NAME = "RevApi";

    RevApiDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new RevApiParser();
    }
}