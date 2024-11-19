package edu.hm.hafner.analysis.parser;

import java.io.Serial;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
 * Parses a StyleCop XML report files.
 *
 * @author Sebastian Seidl
 */
public class StyleCopParser extends IssueParser {
    @Serial
    private static final long serialVersionUID = 7846052338159003458L;

    @Override
    public Report parse(final ReaderFactory readerFactory) throws ParsingException {
        var document = readerFactory.readDocument();

        // Pre v4.3 uses SourceAnalysisViolations as the parent node name
        var mainNode = document.getElementsByTagName("SourceAnalysisViolations");
        if (mainNode.getLength() == 0) {
            // v4.3 uses StyleCopViolations as the parent node name
            mainNode = document.getElementsByTagName("StyleCopViolations");
        }

        var rootElement = (Element) mainNode.item(0);
        return parseViolations(XmlElementUtil.getChildElementsByName(rootElement, "Violation"));
    }

    private Report parseViolations(final List<Element> elements) {
        try (var issueBuilder = new IssueBuilder()) {
            var report = new Report();
            for (Element element : elements) {
                issueBuilder.setFileName(getString(element, "Source"))
                        .setLineStart(getLineNumber(element))
                        .setCategory(getCategory(element))
                        .setType(getString(element, "Rule"))
                        .setMessage(element.getTextContent())
                        .setSeverity(Severity.WARNING_NORMAL);

                report.add(issueBuilder.buildAndClean());
            }
            return report;
        }
    }

    /**
     * Returns the Category of a StyleCop Violation.
     *
     * @param element
     *         The Element which represents the violation
     *
     * @return category of violation
     */
    private String getCategory(final Element element) {
        var ruleNameSpace = getString(element, "RuleNamespace");

        int i = ruleNameSpace.lastIndexOf('.');
        if (i == -1) {
            return getString(element, "RuleId");
        }
        else {
            return ruleNameSpace.substring(i + 1);
        }
    }

    /**
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

    /**
     * Returns the LineNumber for the given violation.
     *
     * @param violation
     *            the xml Element "violation" to get the Linenumber from.
     * @return the lineNumber of the violation. 0 if there is no LineNumber or the LineNumber cant't be parsed into an
     *         Integer.
     */
    private int getLineNumber(final Element violation) {
        if (violation.hasAttribute("LineNumber")) {
            return IntegerParser.parseInt(violation.getAttribute("LineNumber"));
        }
        else {
            return 0;
        }
    }
}
