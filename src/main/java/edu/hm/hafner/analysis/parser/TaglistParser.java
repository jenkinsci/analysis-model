package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.input.ReaderInputStream;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.ParsingCanceledException;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.XmlElementUtil;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

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
    @SuppressFBWarnings("NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE")
    @SuppressWarnings("PMD.AvoidCatchingNPE")
    public Report parse(final Reader reader, final Function<String, String> preProcessor)
            throws ParsingCanceledException, ParsingException {

        try (InputStream input = new ReaderInputStream(reader, StandardCharsets.UTF_8)) {

            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder;
            docBuilder = docBuilderFactory.newDocumentBuilder();

            Document doc = docBuilder.parse(input);

            Report report = new Report();

            Element tags = XmlElementUtil.getFirstElementByTagName(doc.getDocumentElement(), "tags");

            // Will not work parallel, abuses changing object state in the flat maps.
            XmlElementUtil.getNamedChildElements(tags, "tag").stream().map(e -> {
                Context c = new Context(e);
                c.getIssueBuilder().setCategory(e.getAttribute("name"));
                return c;
            }).flatMap(c -> {
                Element files = XmlElementUtil.getFirstElementByTagName(c.getElement(), "files");
                return XmlElementUtil.getNamedChildElements(files, "file").stream().map(e -> {
                    c.setElement(e);
                    c.getIssueBuilder().setFileName(e.getAttribute("name"));
                    return c;
                });
            }).flatMap(c -> {
                Element comments = XmlElementUtil.getFirstElementByTagName(c.getElement(), "comments");
                return XmlElementUtil.getNamedChildElements(comments, "comment").stream().map(e -> {
                    c.setElement(e);

                    Element lineNumElement = XmlElementUtil.getFirstElementByTagName(e, "lineNumber");
                    String lineNum = lineNumElement.getFirstChild().getNodeValue();
                    c.getIssueBuilder().setLineStart(parseInt(lineNum));

                    Element msgElement = XmlElementUtil.getFirstElementByTagName(e, "comment");
                    String msg = msgElement.getFirstChild().getNodeValue();
                    c.getIssueBuilder().setMessage(msg);

                    return c;
                });
            }).forEach(c -> {
                report.add(c.getIssueBuilder().build());
            });

            return report;
        }
        catch (IOException | ParserConfigurationException | SAXException | NullPointerException e) {
            throw new ParsingException(e);
        }
    }

    /**
     * Utility class use in parsing with streams.
     * 
     * @author Jason Faust
     */
    private static class Context {
        private final IssueBuilder issueBuilder = new IssueBuilder();
        private Element element;

        Context(final Element element) {
            this.element = element;
        }

        public IssueBuilder getIssueBuilder() {
            return issueBuilder;
        }

        public Element getElement() {
            return element;
        }

        public void setElement(final Element element) {
            this.element = element;
        }
    }

}
