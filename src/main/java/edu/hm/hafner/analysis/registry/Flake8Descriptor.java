package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.FlawfinderParser;
import edu.hm.hafner.analysis.parser.FlexSdkParser;
import edu.hm.hafner.analysis.parser.violations.Flake8Adapter;

/**
 * A descriptor for Flake8.
 *
 * @author Lorenz Munsch
 */
class Flake8Descriptor extends ParserDescriptor {
    private static final String ID = "flake8";
    private static final String NAME = "Flex SDK Compiler";

    Flake8Descriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser() {
        return new Flake8Adapter();
    }

    @Override
    public String getHelp() {
        return "<p>Run flake8 as <code>flake8 --format=pylint</code></p>";
    }
}
