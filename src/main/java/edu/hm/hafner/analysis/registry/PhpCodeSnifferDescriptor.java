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
    private static final String NAME = "PHP Runtime";

    PhpCodeSnifferDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser() {
        return new CheckStyleParser();
    }
}
