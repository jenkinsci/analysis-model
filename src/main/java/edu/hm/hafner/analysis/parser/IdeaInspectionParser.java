package edu.hm.hafner.analysis.parser;

import javax.annotation.Nonnull;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.function.Function;

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
public class IdeaInspectionParser extends AbstractParser<Issue> {
    private static final long serialVersionUID = 3307389086106375473L;

    @Override
    public Issues<Issue> parse(@Nonnull final Reader reader, @Nonnull final IssueBuilder builder,
            final Function<String, String> preProcessor) throws ParsingException {
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

    private Issues<Issue> parseProblems(final List<Element> elements, final IssueBuilder builder) {
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

    private Priority getPriority(final String severity) {
        Priority priority = Priority.LOW;
        if ("WARNING".equals(severity)) {
            priority = Priority.NORMAL;
        }
        else if ("ERROR".equals(severity)) {
            priority = Priority.HIGH;
        }
        return priority;
    }

    private String getValue(final Element element) {
        return element.getFirstChild().getNodeValue();
    }

    private String getChildValue(final Element element, final String childTag) {
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

