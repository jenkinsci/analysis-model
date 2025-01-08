package edu.hm.hafner.analysis.parser;

import java.io.Serial;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.w3c.dom.Element;

import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.util.IntegerParser;
import edu.hm.hafner.analysis.util.XmlElementUtil;

/**
 * A parser for IntelliJ IDEA inspections.
 *
 * @author Alex Lopashev, alexlopashev@gmail.com
 */
public class IdeaInspectionParser extends IssueParser {
    @Serial
    private static final long serialVersionUID = 3307389086106375473L;
    private static final String PATH_PREFIX = "file://";
    private static final String WARNING = "WARNING";
    private static final String ERROR = "ERROR";

    @Override
    public Report parseReport(final ReaderFactory readerFactory) throws ParsingException {
        var document = readerFactory.readDocument();

        var rootElement = (Element) document.getElementsByTagName("problems").item(0);
        return parseProblems(XmlElementUtil.getChildElementsByName(rootElement, "problem"));
    }

    private Report parseProblems(final List<Element> elements) {
        var problems = new Report();
        try (var issueBuilder = new IssueBuilder()) {
            for (Element element : elements) {
                var file = getChildValue(element, "file");
                var problemClass = XmlElementUtil.getFirstChildElementByName(element, "problem_class");
                if (problemClass.isPresent()) {
                    var problem = problemClass.get();
                    issueBuilder.setFileName(stripPathPrefix(file))
                            .setLineStart(IntegerParser.parseInt(getChildValue(element, "line")))
                            .setCategory(StringEscapeUtils.unescapeXml(getValue(problem)))
                            .setMessage(StringEscapeUtils.unescapeXml(getChildValue(element, "description")))
                            .setModuleName(StringEscapeUtils.unescapeXml(getChildValue(element, "module")))
                            .setSeverity(getPriority(problem.getAttribute("severity")));
                    problems.add(issueBuilder.buildAndClean());
                }
            }
        }
        return problems;
    }

    private Severity getPriority(final String severity) {
        var priority = Severity.WARNING_LOW;
        if (WARNING.equals(severity)) {
            priority = Severity.WARNING_NORMAL;
        }
        else if (ERROR.equals(severity)) {
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
        var firstElement = XmlElementUtil.getFirstChildElementByName(element, childTag);
        if (firstElement.isPresent()) {
            var child = firstElement.get().getFirstChild();
            if (child != null) {
                return child.getNodeValue();
            }
        }
        return "-";
    }
}
