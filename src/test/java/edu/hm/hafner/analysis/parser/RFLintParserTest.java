package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static org.junit.Assert.*;

/**
 * Tests the class {@link RFLintParser}. Created by traitanit on 3/27/2017 AD.
 */
public class RFLintParserTest extends ParserTester {
    private static final String WARNING_TYPE = new RFLintParser().getId();

    /**
     * Parses a txt file, containing 6 warnings.
     *
     * @throws IOException if the file could not be read
     */
    @Test
    public void rfLintTest() throws IOException {
        Issues warnings = new RFLintParser().parse(openFile());

        assertEquals(6, warnings.size());
        Iterator<Issue> iterator = warnings.iterator();
        Issue warning;

        warning = iterator.next();
        checkWarning(warning,
                25,
                "Line is too long (exceeds 100 characters)",
                "./Login_to_web.robot",
                WARNING_TYPE, "WARNING", Priority.NORMAL);
        warning = iterator.next();
        checkWarning(warning,
                40,
                "No keyword documentation",
                "./Login_to_web.robot",
                WARNING_TYPE, "ERROR", Priority.HIGH);
        warning = iterator.next();
        checkWarning(warning,
                24,
                "Line is too long (exceeds 100 characters)",
                "./Merchant_Signup.robot",
                WARNING_TYPE, "WARNING", Priority.NORMAL);
        warning = iterator.next();
        checkWarning(warning,
                378,
                "No keyword documentation",
                "./Merchant_Signup.robot",
                WARNING_TYPE, "ERROR", Priority.HIGH);
        warning = iterator.next();
        checkWarning(warning,
                73,
                "Too few steps (1) in keyword",
                "./merchant_common_keyword.txt",
                WARNING_TYPE, "WARNING", Priority.NORMAL);
        warning = iterator.next();
        checkWarning(warning,
                123,
                "Ignore Error",
                "./merchant_common_keyword.txt",
                WARNING_TYPE, "IGNORE", Priority.LOW);

    }

    @Override
    protected String getWarningsFile() {
        return "rflint.txt";
    }
}
