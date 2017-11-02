package edu.hm.hafner.analysis.parser;

import java.io.IOException;

import org.junit.Test;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.Issues;
import static org.junit.Assert.*;

/**
 * Tests the class {@link JSLintParser}.
 *
 * @author Ullrich Hafner
 */
public class CssLintParserTest extends ParserTester {
    /**
     * Tests parsing of CSS-Lint files.
     *
     * @throws IOException in case of an error
     */
    @Test
    public void testCssLint() throws IOException {
        Issues results = createParser().parse(openFile());
        assertEquals(51, results.size());
    }

    /**
     * Creates the parser.
     *
     * @return the warnings parser
     */
    protected AbstractParser createParser() {
        return new CssLintParser();
    }

    @Override
    protected String getWarningsFile() {
        return "jslint/csslint.xml";
    }
}
