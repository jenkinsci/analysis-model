package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.ParsingCanceledException;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.parser.jcreport.File;
import edu.hm.hafner.analysis.parser.jcreport.Item;
import edu.hm.hafner.analysis.parser.jcreport.JcReportParser;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the {@link JcReportParser}.
 *
 * @author Johann Vierthaler, johann.vierthaler@web.de
 */
@SuppressWarnings("NullAway")
class JcReportParserTest extends AbstractParserTest {
    JcReportParserTest() {
        super("jcreport/testCorrect.xml");
    }

    /**
     * Reads a file with 5 warnings.
     */
    @Test
    void testGetWarningList() {
        var warnings = parseDefaultFile();

        assertThat(warnings).hasSize(6).hasDuplicatesSize(1);
    }

    /**
     * This test assures that all properties within Report-, File- and Item-Objects are parsed correctly. Not all
     * properties are needed to create a warning. So it was decided to keep them anyway in case Jenkins is modified to
     * contain more information in the Warning-Objects. For reasons of simplicity only a Report with 1 file and 1 item
     * was created.
     */
    @Test
    void testReportParserProperties() {
        var factory = createReaderFactory("jcreport/testReportProps.xml");
        var testReportProps = new JcReportParser().createReport(factory);

        assertThat(testReportProps.getFiles()).hasSize(1);

        var file = testReportProps.getFiles().get(0);
        assertThat(file.getClassname()).isEqualTo("SomeClass");
        assertThat(file.getLevel()).isEqualTo("SomeLevel");
        assertThat(file.getLoc()).isEqualTo("173");
        assertThat(file.getName()).isEqualTo("SomeDirectory/SomeClass.java");
        assertThat(file.getPackageName()).isEqualTo("SomePackage");
        assertThat(file.getSrcdir()).isEqualTo("SomeDirectory");

        var item = file.getItems().get(0);
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
     * @throws ParsingCanceledException
     *         -> thrown by jcrp.parse();
     */
    @Test
    void testSAXEception() throws ParsingCanceledException {
        assertThatThrownBy(() -> parse("jcreport/testCorrupt.xml"))
                .isInstanceOf(ParsingException.class);
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(6).hasDuplicatesSize(1);

        softly.assertThat(report.get(0))
                .hasFileName("SomeDirectory/SomeClass.java")
                .hasSeverity(Severity.ERROR)
                .hasMessage("SomeMessage")
                .hasPackageName("SomePackage")
                .hasLineStart(50);
    }

    @Override
    protected JcReportParser createParser() {
        return new JcReportParser();
    }
}
