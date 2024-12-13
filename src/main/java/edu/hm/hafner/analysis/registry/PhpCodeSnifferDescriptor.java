package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.checkstyle.CheckStyleParser;

/**
 * A descriptor for PHP_CodeSniffer. Delegates to {@link CheckStyleParser}.
 *
 * @author Lorenz Munsch
 */
class PhpCodeSnifferDescriptor extends ParserDescriptor {
    private static final String ID = "php-code-sniffer";
    private static final String NAME = "PHP_CodeSniffer";

    PhpCodeSnifferDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new CheckStyleParser();
    }

    @Override
    public String getHelp() {
        return "Use option --report=checkstyle.";
    }

    @Override
    public String getUrl() {
        return "https://github.com/squizlabs/PHP_CodeSniffer";
    }
}
