package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.pmd.PmdMessages;
import edu.hm.hafner.analysis.parser.pmd.PmdParser;
import edu.hm.hafner.util.Deferred;

/**
 * A descriptor for the Pmd warnings.
 *
 * @author Lorenz Munsch
 */
class PmdDescriptor extends ParserDescriptor {
    private static final String ID = "pmd";
    private static final String NAME = "PMD";

    private final Deferred<PmdMessages> messages = new Deferred<>(PmdMessages::new);

    PmdDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new PmdParser();
    }

    @Override
    public String getPattern() {
        return "**/pmd.xml";
    }

    @Override
    public String getUrl() {
        return "https://pmd.github.io";
    }

    @Override
    public String getIconUrl() {
        return "https://raw.githubusercontent.com/pmd/pmd/master/docs/images/logo/pmd_logo_small.png";
    }

    @Override
    public String getDescription(final Issue issue) {
        return messages.get().getMessage(issue.getCategory(), issue.getType());
    }
}
