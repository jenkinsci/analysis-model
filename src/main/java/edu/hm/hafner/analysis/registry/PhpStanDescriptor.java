package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.checkstyle.CheckStyleParser;

/**
 * A descriptor for PHPStan. Delegates to {@link CheckStyleParser}.
 *
 * @author Lorenz Munsch
 */
public class PhpStanDescriptor extends ParserDescriptor {
    private static final String ID = "phpstan";
    private static final String NAME = "PHPStan";

    public PhpStanDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new CheckStyleParser();
    }

    @Override
    public String getHelp() {
        return "Use the options: --no-progress --error-format checkstyle";
    }

    @Override
    public String getUrl() {
        return "https://github.com/phpstan/phpstan";
    }
}
