package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.ParsingCanceledException;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.assertj.SoftAssertions;
import edu.hm.hafner.analysis.parser.jcreport.File;
import edu.hm.hafner.analysis.parser.jcreport.Item;
import edu.hm.hafner.analysis.parser.jcreport.JcReportParser;
import edu.hm.hafner.analysis.parser.jcreport.Report;
import static edu.hm.hafner.analysis.assertj.Assertions.*;

/**
 * Tests the JcReportParser-Class.
 *
 * @author Johann Vierthaler, johann.vierthaler@web.de
 */
public class JcReportParserTest extends AbstractParserTest {

    /**
     * Creates a new instance of {@link AbstractParserTest}.
     */
    protected JcReportParserTest() {
        super("jcreport/testCorrect.xml");
    }


    /**
     * Gets Collection with size of 5.
     *
     * @throws UnsupportedEncodingException if encoding is not available
     */
    @Test
    public void testGetWarningList() throws UnsupportedEncodingException {
        JcReportParser jcrp = new JcReportParser();
        InputStreamReader readCorrectXml = getReader("testCorrect.xml");
        Issues<Issue> warnings = jcrp.parse(readCorrectXml);

        assertThat(warnings).hasSize(5).hasDuplicatesSize(2);
    }

    /**
     * This test assures that all properties within Report-, File- and Item-Objects are parsed correctly. Not all
     * properties are needed to create a warning. So it was decided to keep them anyway in case Jenkins is modified to
     * contain more information in the Warning-Objects. For reasons of simplicity only a Report with 1 file and 1 item
     * was created.
     *
     * @throws UnsupportedEncodingException if encoding is not available
     */
    @Test
    public void testReportParserProperties() throws UnsupportedEncodingException {
        InputStreamReader readCorrectXml = getReader("testReportProps.xml");
        Report testReportProps = new JcReportParser().createReport(readCorrectXml);

        assertThat(testReportProps.getFiles().size()).isEqualTo(1);

        File file = testReportProps.getFiles().get(0);
        assertThat(file.getClassname()).isEqualTo("SomeClass");
        assertThat(file.getLevel()).isEqualTo("SomeLevel");
        assertThat(file.getLoc()).isEqualTo("173");
        assertThat(file.getName()).isEqualTo("SomeDirectory/SomeClass.java");
        assertThat(file.getPackageName()).isEqualTo("SomePackage");
        assertThat(file.getSrcdir()).isEqualTo("SomeDirectory");

        Item item = file.getItems().get(0);
        assertThat(item.getColumn()).isEqualTo("0");
        assertThat(item.getEndcolumn()).isEqualTo("3");
        assertThat(item.getFindingtype()).isEqualTo("SomeType");
        assertThat(item.getLine()).isEqualTo("50");
        assertThat(item.getEndline()).isEqualTo("70");
        assertThat(item.getMessage()).isEqualTo("SomeMessage");
        assertThat(item.getSeverity()).isEqualTo("CriticalError");

    }

    /**
     * Test the SAXException when file is corrupted. When a SAXException is triggered a new IOException is thrown. This
     * explains the expected = IOException.class.
     *
     * @throws ParsingCanceledException -> thrown by jcrp.parse();
     */
    @Test
    public void testSAXEception() throws ParsingCanceledException, IOException {

        assertThatThrownBy(() -> new JcReportParser().parse(getReader("testCorrupt.xml")))
                .isInstanceOf(ParsingException.class);

    }

    private InputStreamReader getReader(final String fileName) throws UnsupportedEncodingException {
        return new InputStreamReader(JcReportParserTest.class.getResourceAsStream("jcreport/" + fileName), "UTF-8");
    }

    @Override
    protected void assertThatIssuesArePresent(final Issues<Issue> issues, final SoftAssertions softly) {
        assertThat(issues).hasSize(5).hasDuplicatesSize(2);

        softly.assertThat(issues.get(0))
                .hasFileName("SomeDirectory/SomeClass.java")
                .hasPriority(Priority.HIGH)
                .hasMessage("SomeMessage")
                .hasPackageName("SomePackage")
                .hasLineStart(50);
    }

    @Override
    protected AbstractParser createParser() {
        return new JcReportParser();
    }
}
