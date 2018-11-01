package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.io.Reader;
import java.util.function.Function;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.ParsingCanceledException;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.XmlElementUtil;

/**
 * Parser for Eclipse Compiler output in XML format.
 * 
 * @author Jason Faust
 */
public class EclipseXMLParser extends AbstractParser {

    private static final long serialVersionUID = 1L;

    @Override
    public Report parse(final Reader reader, final Function<String, String> preProcessor)
            throws ParsingCanceledException, ParsingException {

        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder;
            docBuilder = docBuilderFactory.newDocumentBuilder();

            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xPath = xPathFactory.newXPath();

            Document doc = docBuilder.parse(new InputSource(reader));

            IssueBuilder issueBuilder = new IssueBuilder();
            Report report = new Report();

            NodeList sources = (NodeList)xPath.evaluate("/compiler/sources/source[problems]", doc,
                    XPathConstants.NODESET);
            for (Element source : XmlElementUtil.nodeListToList(sources)) {
                String fileName = xPath.evaluate("@path", source);
                issueBuilder.setFileName(fileName);

                NodeList problems = (NodeList)xPath.evaluate("problems/problem", source, XPathConstants.NODESET);
                for (Element problem : XmlElementUtil.nodeListToList(problems)) {
                    String type = xPath.evaluate("@severity", problem);
                    if (type != null) {
                        issueBuilder.setSeverity(EclipseParser.mapTypeToSeverity(type));
                    }

                    String lineNum = xPath.evaluate("@line", problem);
                    issueBuilder.setLineStart(parseInt(lineNum));

                    // Columns are a closed range, 1 based index.
                    // XML output counts from column 0, need to offset by 1
                    String colStart = xPath.evaluate("source_context/@sourceStart", problem);
                    if (colStart != null) {
                        issueBuilder.setColumnStart(parseInt(colStart) + 1);
                    }
                    String colEnd = xPath.evaluate("source_context/@sourceEnd", problem);
                    if (colEnd != null) {
                        issueBuilder.setColumnEnd(parseInt(colEnd) + 1);
                    }

                    String msg = xPath.evaluate("message/@value", problem);
                    issueBuilder.setMessage(msg);

                    report.add(issueBuilder.build());
                }
            }

            return report;
        }
        catch (IOException | ParserConfigurationException | SAXException | XPathExpressionException e) {
            throw new ParsingException(e);
        }
    }

}
