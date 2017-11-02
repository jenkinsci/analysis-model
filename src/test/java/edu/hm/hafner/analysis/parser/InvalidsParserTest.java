package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static org.junit.Assert.*;

/**
 * Tests the class {@link InvalidsParser}.
 */
public class InvalidsParserTest extends ParserTester {
    /**
     * Parses a file with two deprecation warnings.
     *
     * @throws IOException if the file could not be read
     */
    @Test
    public void testParser() throws IOException {
        Issues warnings = new InvalidsParser().parse(openFile());

        assertEquals(3, warnings.size());

        Iterator<Issue> iterator = warnings.iterator();
        Issue annotation = iterator.next();
        String type = "Oracle Invalid";
        checkWarning(annotation,
                45,
                "Encountered the symbol \"END\" when expecting one of the following:",
                "ENV_UTIL#.PACKAGE BODY", type, "PLW-05004", Priority.NORMAL);
        assertEquals("wrong schema detected", "E", annotation.getPackageName());
        annotation = iterator.next();
        checkWarning(annotation,
                5,
                "Encountered the symbol \"END\" when expecting one of the following:",
                "ENV_ABBR#B.TRIGGER", type, "PLW-07202", Priority.LOW);
        assertEquals("wrong schema detected", "E", annotation.getPackageName());
        annotation = iterator.next();
        checkWarning(annotation,
                0,
                "referenced name javax/management/MBeanConstructorInfo could not be found",
                "/b77ce675_LoggerDynamicMBean.JAVA CLASS", type, "ORA-29521", Priority.HIGH);
        assertEquals("wrong schema detected", "E", annotation.getPackageName());
    }

    @Override
    protected String getWarningsFile() {
        return "invalids.txt";
    }
}

