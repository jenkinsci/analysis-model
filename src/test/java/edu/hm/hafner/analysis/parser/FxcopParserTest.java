package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.parser.fxcop.FxCopParser;
import static org.junit.Assert.*;

/**
 * Tests the class {@link FxCopParser}.
 *
 * @author Ullrich Hafner
 */
public class FxcopParserTest extends ParserTester {
    /**
     * Verifies that the FXCop parser works as expected.
     *
     * @throws IOException if the file could not be read
     */
    @Test
    public void testJenkins14172() throws IOException {
        InputStream file = null;
        try {
            Issues result = new FxCopParser().parse(openFile("issue14172.xml"));

            assertEquals(44, result.size());
        }
        finally {
            IOUtils.closeQuietly(file);
        }
    }

    /**
     * Verifies that the FXCop parser works as expected.
     *
     * @throws IOException if the file could not be read
     */
    @Test
    public void testFXCop() throws IOException {
        Issues result = new FxCopParser().parse(openFile());

        assertEquals(2, result.size());

        Iterator<Issue> iterator = result.iterator();
        checkWarning(iterator.next(), 299,
                "<a href=\"http://msdn2.microsoft.com/library/ms182190(VS.90).aspx\">SpecifyIFormatProvider</a> - Because the behavior of 'decimal.ToString(string)' could vary based on the current user's locale settings, replace this call in 'FilmFacadeBase.Price.get()' with a call to 'decimal.ToString(string, IFormatProvider)'. If the result of 'decimal.ToString(string, IFormatProvider)' will be displayed to the user, specify 'CultureInfo.CurrentCulture' as the 'IFormatProvider' parameter. Otherwise, if the result will be stored and accessed by software, such as when it is persisted to disk or to a database, specify 'CultureInfo.InvariantCulture'.",
                "c:/Hudson/data/jobs/job1/workspace/test/Space/TestBase.cs", "Microsoft.Globalization", Priority.HIGH);
        checkWarning(iterator.next(), 37,
                "<a href=\"http://msdn2.microsoft.com/library/bb264474(VS.90).aspx\">CompoundWordsShouldBeCasedCorrectly</a> - In member 'MyControl.InitialParameters(bool)', the discrete term 'javascript' in parameter name 'javascript' should be expressed as a compound word, 'javaScript'.",
                "c:/Hudson/data/jobs/job1/workspace/web/UserControls/MyControl.ascx.cs", "Microsoft.Naming", Priority.HIGH);
    }

    @Override
    protected String getWarningsFile() {
        return "fxcop.xml";
    }
}

