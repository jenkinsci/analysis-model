package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.PyLintParser;
import edu.hm.hafner.analysis.parser.pylint.PyLintDescriptions;
import edu.hm.hafner.analysis.util.Deferred;

/**
 * A descriptor for the PyLint.
 *
 * @author Lorenz Munsch
 */
public class PyLintDescriptor extends ParserDescriptor {
    private static final String ID = "pylint";
    private static final String NAME = "Pylint";

    private final Deferred<PyLintDescriptions> messages = new Deferred<>(PyLintDescriptions::new);

    public PyLintDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new PyLintParser();
    }

    @Override
    public String getHelp() {
        return "<p>Start Pylint using this custom message template (can also be configured via a pylintrc configuration file):"
                + "<p><code>pylint --msg-template='{path}:{line}: [{msg_id}, {obj}] {msg} ({symbol})' modules_or_packages > pylint.log</code></p>"
                + "</p>";
    }

    @Override
    public String getDescription(final Issue issue) {
        return messages.get().getDescription(issue.getType());
    }
}
