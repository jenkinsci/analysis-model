package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.ParsingCanceledException;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.parser.jcreport.File;
import edu.hm.hafner.analysis.parser.jcreport.Item;
import edu.hm.hafner.analysis.parser.jcreport.JcReportParser;
import edu.hm.hafner.analysis.parser.jcreport.Report;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the JcReportParser-Class.
 *
 * @author Johann Vierthaler, johann.vierthaler@web.de
 */
public class JcReportParserTest {
    /**
     * Parses Report with 5 Warnings.
     *
     * @throws ParsingCanceledException -> thrown by jcrp.parse();
     * @throws IOException              -> thrown by jcrp.parse();
     */
    @Test
    public void testParserWithValidFile() throws ParsingCanceledException, IOException {
        JcReportParser parser = new JcReportParser();
        InputStreamReader readCorrectXml = getReader("testCorrect.xml");

        Issues warnings = parser.parse(readCorrectXml);

        assertEquals(7, warnings.size(), "Should be 7: ");
        assertEquals("SomeDirectory/SomeClass.java", warnings.get(0).getFileName(), "Wrong Parse FileName: ");
        assertEquals(Priority.HIGH, warnings.get(0).getPriority(), "Wrong Parse Priority: ");
        assertEquals("SomeMessage", warnings.get(0).getMessage(), "Wrong Parse Message: ");
        assertEquals("SomePackage", warnings.get(0).getPackageName(), "Wrong Parse PackageName: ");
        assertEquals(50, warnings.get(0).getLineStart(), "Wrong Parse LineNumberParse: ");
    }

    /**
     * Gets Collection with size of 5.
     *
     * @throws ParsingCanceledException -> thrown by jcrp.parse();
     * @throws IOException              -> thrown by jcrp.parse();
     */
    @Test
    public void testGetWarningList() throws ParsingCanceledException, IOException {
        JcReportParser jcrp = new JcReportParser();
        InputStreamReader readCorrectXml = getReader("testCorrect.xml");

        Issues warnings = jcrp.parse(readCorrectXml);

        assertEquals(7, warnings.size());
    }

    /**
     * This test assures that all properties within Report-, File- and Item-Objects are parsed correctly. Not all
     * properties are needed to create a warning. So it was decided to keep them anyway in case Jenkins is modified to
     * contain more information in the Warning-Objects. For reasons of simplicity only a Report with 1 file and 1 item
     * was created.
     *
     * @throws IOException -> createReport can cause an IOException.
     */
    @Test
    public void testReportParserProperties() throws IOException {
        InputStreamReader readCorrectXml = getReader("testReportProps.xml");
        Report testReportProps = new JcReportParser().createReport(readCorrectXml);

        assertEquals(1, testReportProps.getFiles().size());

        File file = testReportProps.getFiles().get(0);
        assertEquals("SomeClass", file.getClassname(), "Should be 'SomeClass'");
        assertEquals("SomeLevel", file.getLevel(), "Should be 'SomeLevel'");
        assertEquals("173", file.getLoc(), "Should be '173'");
        assertEquals("SomeDirectory/SomeClass.java", file.getName(), "Should be 'SomeDirectory/SomeClass.java'");
        assertEquals("SomePackage", file.getPackageName(), "Should be 'SomePackage'");
        assertEquals("SomeDirectory", file.getSrcdir(), "Should be 'SomeDirectory'");

        Item item = file.getItems().get(0);
        assertEquals("0", item.getColumn(), "Should be '0'");
        assertEquals("3", item.getEndcolumn(), "Should be '3'");
        assertEquals("SomeType", item.getFindingtype(), "Should be 'SomeType'");
        assertEquals("50", item.getLine(), "Should be '50'");
        assertEquals("70", item.getEndline(), "Should be '70'");
        assertEquals("SomeMessage", item.getMessage(), "Should be 'SomeMessage'");
        assertEquals("CriticalError", item.getSeverity(), "Should be 'CriticalError'");
    }

    /**
     * Test the SAXException when file is corrupted. When a SAXException is triggered a new IOException is thrown. This
     * explains the expected = IOException.class.
     *
     * @throws ParsingCanceledException -> thrown by jcrp.parse();
     * @throws IOException              -> thrown by jcrp.parse();
     */
    @Test
    public void testSAXEception() throws ParsingCanceledException, IOException {
        assertThrows(ParsingException.class,
                () -> new JcReportParser().parse(getReader("testCorrupt.xml")));
    }

    private InputStreamReader getReader(final String fileName) throws UnsupportedEncodingException {
        return new InputStreamReader(JcReportParserTest.class.getResourceAsStream("jcreport/" + fileName), "UTF-8");
    }
}