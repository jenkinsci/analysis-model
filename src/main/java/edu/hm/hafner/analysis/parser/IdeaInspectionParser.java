package edu.hm.hafner.analysis.parser;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.util.XmlElementUtil;

/**
 * A parser for IntelliJ IDEA inspections.
 *
 * @author Alex Lopashev, alexlopashev@gmail.com
 */
public class IdeaInspectionParser extends IssueParser {
    private static final long serialVersionUID = 3307389086106375473L;
    private static final String PATH_PREFIX = "file://";

    @Override
    public Report parse(final ReaderFactory readerFactory) throws ParsingException {
        Document document = readerFactory.readDocument();

        Element rootElement = (Element) document.getElementsByTagName("problems").item(0);
        return parseProblems(XmlElementUtil.getChildElementsByName(rootElement, "problem"));
    }

    private Report parseProblems(final List<Element> elements) {
        Report problems = new Report();
        try (IssueBuilder issueBuilder = new IssueBuilder()) {
            for (Element element : elements) {
                String file = getChildValue(element, "file");
                Optional<Element> problemClass = XmlElementUtil.getFirstChildElementByName(element, "problem_class");
                if (problemClass.isPresent()) {
                    Element problem = problemClass.get();
                    issueBuilder.setFileName(stripPathPrefix(file))
                            .setLineStart(Integer.parseInt(getChildValue(element, "line")))
                            .setCategory(StringEscapeUtils.unescapeXml(getValue(problem)))
                            .setMessage(StringEscapeUtils.unescapeXml(getChildValue(element, "description")))
                            .setSeverity(getPriority(problem.getAttribute("severity")));
                    problems.add(issueBuilder.buildAndClean());
                }
            }
        }
        return problems;
    }

    private Severity getPriority(final String severity) {
        Severity priority = Severity.WARNING_LOW;
        if ("WARNING".equals(severity)) {
            priority = Severity.WARNING_NORMAL;
        }
        else if ("ERROR".equals(severity)) {
            priority = Severity.WARNING_HIGH;
        }
        return priority;
    }

    private String stripPathPrefix(final String file) {
        return StringUtils.removeStart(file, PATH_PREFIX);
    }

    private String getValue(final Element element) {
        return element.getFirstChild().getNodeValue();
    }

    private String getChildValue(final Element element, final String childTag) {
        Optional<Element> firstElement = XmlElementUtil.getFirstChildElementByName(element, childTag);
        if (firstElement.isPresent()) {
            Node child = firstElement.get().getFirstChild();
            if (child != null) {
                return child.getNodeValue();
            }
        }
        return "-";
    }
}

