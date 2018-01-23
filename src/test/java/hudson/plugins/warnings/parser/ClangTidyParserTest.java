package hudson.plugins.warnings.parser;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;

import static org.junit.Assert.*;

import hudson.plugins.analysis.util.model.FileAnnotation;
import hudson.plugins.analysis.util.model.Priority;

/**
 * Tests the class {@link ClangParser}.
 *
 * @author Neil Davis
 */
public class ClangTidyParserTest extends ParserTester {
    private static final String TYPE = new ClangTidyParser().getGroup();

    /**
     * Verifies that all messages are correctly parsed.
     *
     * @throws IOException
     *             in case of an error
     */
    @Test
    public void testWarningsParser() throws IOException {
        Collection<FileAnnotation> warnings = new ClangTidyParser().parse(openFile());

        assertEquals(WRONG_NUMBER_OF_WARNINGS_DETECTED, 5, warnings.size());
    }

    @Override
    protected String getWarningsFile() {
        return "ClangTidy.txt";
    }
}
