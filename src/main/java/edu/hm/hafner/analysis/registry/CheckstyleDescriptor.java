package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.checkstyle.CheckStyleParser;
import edu.hm.hafner.analysis.parser.checkstyle.CheckStyleRules;
import edu.hm.hafner.util.Deferred;

/**
 * A descriptor for the CheckStyle warnings.
 *
 * @author Lorenz Munsch
 */
class CheckstyleDescriptor extends ParserDescriptor {
    private static final String ID = "checkstyle";
    private static final String NAME = "CheckStyle";

    private final Deferred<CheckStyleRules> messages = new Deferred<>(CheckStyleRules::new);

    CheckstyleDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser() {
        return new CheckStyleParser();
    }

    @Override
    public String getPattern() {
        return "**/checkstyle-result.xml";
    }

    @Override
    public String getUrl() {
        return "https://checkstyle.org";
    }

    @Override
    public String getDescription(final Issue issue) {
        return messages.get().getDescription(issue.getType());
    }
}
