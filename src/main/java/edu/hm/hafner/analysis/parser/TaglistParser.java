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
 * Parser for Taglist Maven Plugin output.
 * 
 * @author Jason Faust
 * @see <a href=
 *      "https://www.mojohaus.org/taglist-maven-plugin/">https://www.mojohaus.org/taglist-maven-plugin/</a>
 */
public class TaglistParser extends AbstractParser {

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

            NodeList tags = (NodeList)xPath.evaluate("/report/tags/tag", doc, XPathConstants.NODESET);
            for (Element tag : XmlElementUtil.nodeListToList(tags)) {
                String category = xPath.evaluate("@name", tag);
                issueBuilder.setCategory(category);

                NodeList files = (NodeList)xPath.evaluate("files/file", tag, XPathConstants.NODESET);
                for (Element file : XmlElementUtil.nodeListToList(files)) {
                    String fileName = xPath.evaluate("@name", file);
                    issueBuilder.setFileName(fileName);

                    NodeList comments = (NodeList)xPath.evaluate("comments/comment", file, XPathConstants.NODESET);
                    for (Element comment : XmlElementUtil.nodeListToList(comments)) {
                        String lineNum = xPath.evaluate("lineNumber", comment);
                        issueBuilder.setLineStart(parseInt(lineNum));

                        String message = xPath.evaluate("comment", comment);
                        issueBuilder.setMessage(message);

                        report.add(issueBuilder.build());
                    }
                }
            }

            return report;
        }
        catch (IOException | ParserConfigurationException | SAXException | XPathExpressionException e) {
            throw new ParsingException(e);
        }
    }

}
