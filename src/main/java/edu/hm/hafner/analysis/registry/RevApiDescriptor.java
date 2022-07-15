package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.RevApiParser;

/**
 * Parser for Revapi Json reports.
 *
 * @author Dominik Jantschar
 */
public class RevApiDescriptor extends ParserDescriptor {
    private static final String ID = "revapi";
    private static final String NAME = "Revapi";


    RevApiDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new RevApiParser();
    }

    @Override
    public String getDescription(final Issue issue) {
        return super.getDescription(issue);
    }

    @Override
    public String getPattern() {
        return "**/target/revapi-result.json";
    }
}