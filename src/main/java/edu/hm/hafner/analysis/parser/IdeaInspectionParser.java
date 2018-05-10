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
import org.apache.commons.text.StringEscapeUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.XmlElementUtil;

/**
 * A parser for IntelliJ IDEA inspections.
 *
 * @author Alex Lopashev, alexlopashev@gmail.com
 */
public class IdeaInspectionParser extends AbstractParser {
    private static final long serialVersionUID = 3307389086106375473L;

    @Override
    public Report parse(final Reader reader, final Function<String, String> preProcessor)
            throws ParsingException {
        try (InputStream input = new ReaderInputStream(reader, StandardCharsets.UTF_8)) {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(input);

            Element rootElement = (Element) document.getElementsByTagName("problems").item(0);
            return parseProblems(XmlElementUtil.getNamedChildElements(rootElement, "problem"));
        }
        catch (IOException | ParserConfigurationException | SAXException e) {
            throw new ParsingException(e);
        }
    }

    private Report parseProblems(final List<Element> elements) {
        Report problems = new Report();
        for (Element element : elements) {
            String file = getChildValue(element, "file");
            Element problemClass = XmlElementUtil.getFirstElementByTagName(element, "problem_class");
            if (problemClass != null) {
                IssueBuilder builder = new IssueBuilder().setFileName(file)
                        .setLineStart(Integer.parseInt(getChildValue(element, "line")))
                        .setCategory(StringEscapeUtils.unescapeXml(getValue(problemClass)))
                        .setMessage(StringEscapeUtils.unescapeXml(getChildValue(element, "description")))
                        .setPriority(getPriority(problemClass.getAttribute("severity")));
                problems.add(builder.build());
            }
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

