package edu.hm.hafner.analysis.parser;

import java.util.Optional;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
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
            XPathExpression sourcePath = xPath.compile("/compiler/sources/source[problems]");
            XPathExpression fileNamePath = xPath.compile("./@path");
            XPathExpression problemsPath = xPath.compile("problems/problem");

            IssueBuilder issueBuilder = new IssueBuilder();
            Report report = new Report();

            NodeList sources = (NodeList)sourcePath.evaluate(doc, XPathConstants.NODESET);
            for (Element source : XmlElementUtil.nodeListToList(sources)) {
                String fileName = fileNamePath.evaluate(source);
                issueBuilder.setFileName(fileName);

                NodeList problems = (NodeList)problemsPath.evaluate(source, XPathConstants.NODESET);
                for (Element problem : XmlElementUtil.nodeListToList(problems)) {

                    String msg = extractMessage(problem);

                    issueBuilder.guessSeverity(extractSeverity(problem))
                            .setLineStart(extractLineStart(problem))
                            .setMessage(msg);

                    extractCatagory(issueBuilder, msg);

                    // Use columns to make issue 'unique', range isn't useful for counting in the physical source.
                    issueBuilder.setAdditionalProperties(extractColumnRange(problem));

                    report.add(issueBuilder.build());
                }
            }

            return report;
        }
        catch (XPathExpressionException e) {
            throw new ParsingException(e);
        }
    }

    private String extractMessage(Element problem) {
        // XPath is "message/@value"
        return XmlElementUtil.nodeListToList(problem.getChildNodes())
                .stream()
                .filter(e -> "message".equals(e.getNodeName()))
                .findFirst()
                .map(e -> e.getAttribute("value"))
                .orElseGet(() -> "");
    }

    private String extractSeverity(Element problem) {
        // XPath is "./@severity"
        return problem.getAttribute("severity");
    }

    private String extractLineStart(Element problem) {
        // XPath is "./@line"
        return problem.getAttribute("line");
    }

    private String extractColumnRange(Element problem) {
        // XPath is "source_context/@sourceStart" and "source_context/@sourceEnd"
        Optional<Element> ctx = XmlElementUtil.nodeListToList(problem.getChildNodes())
                .stream()
                .filter(e -> "source_context".equals(e.getNodeName()))
                .findFirst();

        StringBuilder range = new StringBuilder();
        ctx.map(e -> e.getAttribute("sourceStart")).ifPresent(range::append);
        range.append('-');
        ctx.map(e -> e.getAttribute("sourceEnd")).ifPresent(range::append);

        return range.toString();
    }

}
