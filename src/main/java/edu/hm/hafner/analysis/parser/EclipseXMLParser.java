package edu.hm.hafner.analysis.parser;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.util.XmlElementUtil;

import static edu.hm.hafner.analysis.parser.EclipseParser.*;

/**
 * Parser for Eclipse Compiler output in XML format.
 * 
 * @author Jason Faust
 */
public class EclipseXMLParser extends IssueParser {
    private static final long serialVersionUID = 1L;

    @Override
    public boolean accepts(final ReaderFactory readerFactory) {
        return isXmlFile(readerFactory);
    }

    @Override
    public Report parse(final ReaderFactory readerFactory) throws ParsingException {
        try {
            Document doc = readerFactory.readDocument();

            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xPath = xPathFactory.newXPath();

            IssueBuilder issueBuilder = new IssueBuilder();
            Report report = new Report();

            NodeList sources = (NodeList)xPath.evaluate("/compiler/sources/source[problems]", doc,
                    XPathConstants.NODESET);
            for (Element source : XmlElementUtil.nodeListToList(sources)) {
                String fileName = xPath.evaluate("@path", source);
                issueBuilder.setFileName(fileName);

                NodeList problems = (NodeList)xPath.evaluate("problems/problem", source, XPathConstants.NODESET);
                for (Element problem : XmlElementUtil.nodeListToList(problems)) {
                    String msg = xPath.evaluate("message/@value", problem);

                    issueBuilder.guessSeverity(xPath.evaluate("@severity", problem))
                            .setLineStart(xPath.evaluate("@line", problem))
                            .setMessage(msg);

                    extractCatagory(issueBuilder, msg);

                    // Use columns to make issue 'unique', range isn't useful for counting in the physical source.
                    StringBuilder range = new StringBuilder();
                    String colStart = xPath.evaluate("source_context/@sourceStart", problem);
                    if (colStart != null) {
                        range.append(colStart);
                    }
                    range.append('-');
                    String colEnd = xPath.evaluate("source_context/@sourceEnd", problem);
                    if (colEnd != null) {
                        range.append(colEnd);
                    }
                    issueBuilder.setAdditionalProperties(range.toString());

                    report.add(issueBuilder.build());
                }
            }

            return report;
        }
        catch (XPathExpressionException e) {
            throw new ParsingException(e);
        }
    }

}
