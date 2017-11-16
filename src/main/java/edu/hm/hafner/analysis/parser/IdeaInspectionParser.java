package edu.hm.hafner.analysis.parser;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.XmlElementUtil;

/**
 * A parser for IntelliJ IDEA inspections.
 *
 * @author Alex Lopashev, alexlopashev@gmail.com
 */
public class IdeaInspectionParser extends AbstractParser {
    private static final long serialVersionUID = 3307389086106375473L;

    /**
     * Creates a new instance of {@link IdeaInspectionParser}.
     */
    public IdeaInspectionParser() {
        super();
    }

    @Override
    public Issues<Issue> parse(Reader reader, final IssueBuilder builder) throws ParsingException {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(new InputSource(reader));

            Element rootElement = (Element)document.getElementsByTagName("problems").item(0);
            return parseProblems(XmlElementUtil.getNamedChildElements(rootElement, "problem"), builder);
        }
        catch (IOException | ParserConfigurationException | SAXException e) {
            throw new ParsingException(e);
        }
        finally {
            IOUtils.closeQuietly(reader);
        }
    }

    private Issues<Issue> parseProblems(List<Element> elements, final IssueBuilder builder) {
        Issues<Issue> problems = new Issues<>();
        for (Element element : elements) {
            String file = getChildValue(element, "file");
            int line = Integer.parseInt(getChildValue(element, "line"));
            Element problemClass = XmlElementUtil.getFirstElementByTagName(element, "problem_class");
            String severity = problemClass.getAttribute("severity");
            String category = StringEscapeUtils.unescapeXml(getValue(problemClass));
            String description = StringEscapeUtils.unescapeXml(getChildValue(element, "description"));
            problems.add(builder.setFileName(file).setLineStart(line).setCategory(category)
                                       .setMessage(description).setPriority(getPriority(severity)).build());
        }
        return problems;
    }

    private Priority getPriority(String severity) {
        Priority priority = Priority.LOW;
        if (severity.equals("WARNING")) {
            priority = Priority.NORMAL;
        }
        else if (severity.equals("ERROR")) {
            priority = Priority.HIGH;
        }
        return priority;
    }

    private String getValue(Element element) {
        return element.getFirstChild().getNodeValue();
    }

    private String getChildValue(Element element, String childTag) {
        Element firstElement = XmlElementUtil.getFirstElementByTagName(element, childTag);
        if (firstElement != null) {
            Node child = firstElement.getFirstChild();
            if (child != null) {
                return child.getNodeValue();
            }
        }
        return "-";
    }
}

