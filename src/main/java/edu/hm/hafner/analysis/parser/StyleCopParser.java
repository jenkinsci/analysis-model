package edu.hm.hafner.analysis.parser;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Function;

import org.apache.commons.io.input.ReaderInputStream;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.ParsingCanceledException;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.XmlElementUtil;

/**
 * Parses a StyleCop (http://code.msdn.microsoft.com/sourceanalysis/) xml report file.
 *
 * @author Sebastian Seidl
 */
public class StyleCopParser extends AbstractParser {
    private static final long serialVersionUID = 7846052338159003458L;

    @Override
    public Report parse(final Reader reader, final Function<String, String> preProcessor)
            throws ParsingException, ParsingCanceledException {
        try (InputStream input = new ReaderInputStream(reader, StandardCharsets.UTF_8)) {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();

            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(input);

            // Pre v4.3 uses SourceAnalysisViolations as the parent node name
            NodeList mainNode = doc.getElementsByTagName("SourceAnalysisViolations");
            if (mainNode.getLength() == 0) {
                // v4.3 uses StyleCopViolations as the parent node name
                mainNode = doc.getElementsByTagName("StyleCopViolations");
            }

            Element rootElement = (Element) mainNode.item(0);
            return parseViolations(XmlElementUtil.getNamedChildElements(rootElement, "Violation"));
        }
        catch (IOException | ParserConfigurationException | SAXException e) {
            throw new ParsingException(e);
        }
    }

    private Report parseViolations(final List<Element> elements) {
        Report report = new Report();
        for (Element element : elements) {
            IssueBuilder builder = new IssueBuilder().setFileName(getString(element, "Source"))
                    .setLineStart(getLineNumber(element))
                    .setCategory(getCategory(element))
                    .setType(getString(element, "Rule"))
                    .setMessage(element.getTextContent())
                    .setPriority(Priority.NORMAL);

            report.add(builder.build());
        }
        return report;
    }

    /**
     * Returns the Category of a StyleCop Violation.
     *
     * @param element
     *         The Element which represents the violation
     *
     * @return Category of violation
     */
    private String getCategory(final Element element) {
        String ruleNameSpace = getString(element, "RuleNamespace");

        int i = ruleNameSpace.lastIndexOf('.');
        if (i == -1) {
            return getString(element, "RuleId");
        }
        else {
            return ruleNameSpace.substring(i + 1);
        }
    }

    /***
     * Returns the value for the named attribute if it exists.
     *
     * @param element
     *            the element to check for an attribute
     * @param name
     *            the name of the attribute
     * @return the value of the attribute; "" if there is no such attribute.
     */
    private String getString(final Element element, final String name) {
        if (element.hasAttribute(name)) {
            return element.getAttribute(name);
        }
        else {
            return StringUtils.EMPTY;
        }
    }

    /***
     * Returns the LineNumber for the given violation.
     *
     * @param violation
     *            the xml Element "violation" to get the Linenumber from.
     * @return the lineNumber of the violation. 0 if there is no LineNumber or the LineNumber cant't be parsed into an
     *         Integer.
     */
    private int getLineNumber(final Element violation) {
        if (violation.hasAttribute("LineNumber")) {
            return parseInt(violation.getAttribute("LineNumber"));
        }
        else {
            return 0;
        }
    }
}
