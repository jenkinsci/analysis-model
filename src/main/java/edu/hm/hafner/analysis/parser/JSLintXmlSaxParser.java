package edu.hm.hafner.analysis.parser;

import org.apache.commons.lang3.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import edu.hm.hafner.util.IntegerParser;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;

/**
 * Handles parsing.
 */
public class JSLintXmlSaxParser extends DefaultHandler {
    private static final String ISSUE = "issue";
    private static final String ERROR = "error";
    private final Report report;
    private String fileName = StringUtils.EMPTY;

    /** Categories. */
    static final String CATEGORY_PARSING = "Parsing";
    static final String CATEGORY_UNDEFINED_VARIABLE = "Undefined Variable";
    static final String CATEGORY_FORMATTING = "Formatting";

    /**
     * Creates a new instance of {@link JSLintXmlSaxParser}.
     *
     * @param report
     *         the issues
     */
    public JSLintXmlSaxParser(final Report report) {
        super();

        this.report = report;
    }

    @Override
    public void startElement(final String namespaceURI,
            final String localName, final String key, final Attributes atts) {

        if (isLintDerivate(key)) {
            return; // Start element, good to skip
        }

        if ("file".equals(key)) {
            fileName = atts.getValue("name");
            return;
        }

        if (ISSUE.equals(key) || ERROR.equals(key)) {
            createWarning(atts);
        }
    }

    private void createWarning(final Attributes attributes) {
        String category = StringUtils.EMPTY;
        Severity priority = Severity.WARNING_NORMAL;

        String message = extractFrom(attributes, "reason", "message");
        if (message.startsWith("Expected")) {
            priority = Severity.WARNING_HIGH;
            category = CATEGORY_PARSING;
        }
        else if (message.endsWith(" is not defined.")) {
            priority = Severity.WARNING_HIGH;
            category = CATEGORY_UNDEFINED_VARIABLE;
        }
        else if (message.contains("Mixed spaces and tabs")) {
            priority = Severity.WARNING_LOW;
            category = CATEGORY_FORMATTING;
        }
        else if (message.contains("Unnecessary semicolon")) {
            category = CATEGORY_FORMATTING;
        }
        else if (message.contains("is better written in dot notation")) {
            category = CATEGORY_FORMATTING;
        }

        int lineNumber = parseInt(attributes.getValue("line"));

        String column = extractFrom(attributes, "column", "char");
        IssueBuilder builder = new IssueBuilder().setFileName(fileName)
                .setLineStart(lineNumber)
                .setCategory(category)
                .setMessage(message)
                .setSeverity(priority);

        if (StringUtils.isNotBlank(column)) {
            builder.setColumnStart(parseInt(column));
        }
        report.add(builder.build());
    }

    private int parseInt(final String line) {
        return new IntegerParser().parseInt(line);
    }

    private String extractFrom(final Attributes atts, final String first, final String second) {
        String value = atts.getValue(first);
        if (StringUtils.isEmpty(value)) {
            value = atts.getValue(second);
        }
        return value;
    }

    private boolean isLintDerivate(final String key) {
        return key != null && key.contains("lint");
    }
}
