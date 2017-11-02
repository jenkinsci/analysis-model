package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static org.junit.Assert.*;

/**
 * Tests the class {@link YuiCompressorParser}.
 *
 * @author Emidio Stani
 */
public class YuiCompressorParserTest extends ParserTester {
    private static final String TYPE = new YuiCompressorParser().getId();

    /**
     * Parses a file with 3 warnings.
     *
     * @throws IOException if the file could not be read
     */
    @Test
    public void parseDeprecation() throws IOException {
        Issues warnings = new YuiCompressorParser().parse(openFile());

        assertEquals(3, warnings.size());

        Iterator<Issue> iterator = warnings.iterator();
        Issue annotation = iterator.next();

        checkWarning(annotation,
                0,
                "Try to use a single 'var' statement per scope."
                        + " [match){returnstringResult;}for( ---> var  <--- i=0;match&&i<match]",
                "unknown.file",
                TYPE, "Use single 'var' per scope", Priority.LOW);

        annotation = iterator.next();
        checkWarning(annotation,
                0,
                "The variable replacement has already been declared in the same scope..."
                        + " [replace(variable,replacement);}var  ---> replacement <--- =globalStoredVars[name];if(replacement!=]",
                "unknown.file",
                TYPE, "Duplicate variable", Priority.HIGH);

        annotation = iterator.next();
        checkWarning(annotation,
                0,
                "Using 'eval' is not recommended. Moreover, using 'eval' reduces the level of compression!"
                        + " [function(condition,label){if( ---> eval <--- (condition)){this.doGotolabel(label]",
                "unknown.file",
                TYPE, "Use eval", Priority.HIGH);

    }

    @Override
    protected String getWarningsFile() {
        return "yui.txt";
    }
}
