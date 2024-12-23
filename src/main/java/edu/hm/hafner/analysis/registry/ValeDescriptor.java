package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.ValeParser;

/**
 * Descriptor for the vale prose linter.
 */
public class ValeDescriptor extends ParserDescriptor {
    private static final String ID = "vale";
    private static final String NAME = "Vale";

    ValeDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(Option... options) {
        return new ValeParser();
    }

    @Override
    public String getPattern() {
        return "**/vale-report.json";
    }

    @Override
    public String getUrl() {
        return "https://vale.sh/";
    }

    @Override
    public String getHelp() {
        return "Reads vale report files. Use the flag --output=JSON";
    }
}
