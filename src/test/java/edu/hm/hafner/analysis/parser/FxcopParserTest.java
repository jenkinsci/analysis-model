package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import edu.hm.hafner.analysis.IssueAssert;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.parser.fxcop.FxCopParser;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests the class {@link FxCopParser}.
 *
 * @author Ullrich Hafner
 */
public class FxcopParserTest extends ParserTester {
    /**
     * Verifies that the FXCop parser works as expected.
     */
    @Test
    public void testJenkins14172() {
        InputStream file = null;
        try {
            Issues result = new FxCopParser().parse(openFile("issue14172.xml"));

            assertThat(result.size()).isEqualTo(44);
        }
        finally {
            IOUtils.closeQuietly(file);
        }
    }

    /**
     * Verifies that the FXCop parser works as expected.
     */
    @Test
    public void testFXCop() {
        Issues result = new FxCopParser().parse(openFile());

        assertThat(result.size()).isEqualTo(2);

        Iterator<Issue> iterator = result.iterator();
        IssueAssert.assertThat(iterator.next()).hasPriority(Priority.HIGH)
                .hasCategory("Microsoft.Globalization")
                .hasLineStart(299)
                .hasLineEnd(299)
                .hasMessage("<a href=\"http://msdn2.microsoft.com/library/ms182190(VS.90).aspx\">SpecifyIFormatProvider</a> - Because the behavior of 'decimal.ToString(string)' could vary based on the current user's locale settings, replace this call in 'FilmFacadeBase.Price.get()' with a call to 'decimal.ToString(string, IFormatProvider)'. If the result of 'decimal.ToString(string, IFormatProvider)' will be displayed to the user, specify 'CultureInfo.CurrentCulture' as the 'IFormatProvider' parameter. Otherwise, if the result will be stored and accessed by software, such as when it is persisted to disk or to a database, specify 'CultureInfo.InvariantCulture'.")
                .hasFileName("c:/Hudson/data/jobs/job1/workspace/test/Space/TestBase.cs");
        IssueAssert.assertThat(iterator.next()).hasPriority(Priority.HIGH)
                .hasCategory("Microsoft.Naming")
                .hasLineStart(37)
                .hasLineEnd(37)
                .hasMessage("<a href=\"http://msdn2.microsoft.com/library/bb264474(VS.90).aspx\">CompoundWordsShouldBeCasedCorrectly</a> - In member 'MyControl.InitialParameters(bool)', the discrete term 'javascript' in parameter name 'javascript' should be expressed as a compound word, 'javaScript'.")
                .hasFileName("c:/Hudson/data/jobs/job1/workspace/web/UserControls/MyControl.ascx.cs");
    }

    @Override
    protected String getWarningsFile() {
        return "fxcop.xml";
    }
}

