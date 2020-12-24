package edu.hm.hafner.analysis.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.apache.commons.lang3.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.helpers.DefaultHandler;

import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;

/**
 * Handles the parsing of a translation file from Qt.
 */
public class QtTranslationSaxParser extends DefaultHandler {
    private static final String CONTEXT = "context";
    private static final String CONTEXT_NAME = "name";
    private static final String MESSAGE = "message";
    private static final String ROOT = "TS";
    private static final String SOURCE = "source";
    private static final String TRANSLATION = "translation";
    private static final String TRANSLATION_ATTR_TYPE = "type";

    public static final String TRANSLATION_TYPE_OBSOLETE = "obsolete";
    public static final String TRANSLATION_TYPE_UNFINISHED = "unfinished";
    public static final String TRANSLATION_TYPE_VANISHED = "vanished";

    public static final String TRANSLATION_TYPE_OBSOLETE_MESSAGE =
            "This translation can be removed as the source doesn't exists anymore.";
    public static final String TRANSLATION_TYPE_UNFINISHED_MESSAGE =
            "This source string misses a translation.";
    public static final String TRANSLATION_TYPE_VANISHED_MESSAGE =
            "The source string can not be found within the sources. " +
            "Remove this translation if it is not used anymore or improve your call to \"tr()\", " +
            "so \"lupdate\" can find it.";

    private final Report report;
    private final IssueBuilder builder = new IssueBuilder();
    private Locator locator = null;
    private final Stack<String> elementTypeStack = new Stack<>();
    private final Map<String, String> expectedElementTypeParents = new HashMap<>();
    private String currentElement = null;
    private String contextName = null;
    private String sourceValue = null;
    private boolean translationTagFound = false;
    private boolean emitIssue = false;
    private int lastColumnNumber;

    /**
     * Creates a new instance of {@link QtTranslationSaxParser}.
     *
     * @param report
     *         the issues
     */
    public QtTranslationSaxParser(final Report report, final String filename) {
        super();

        expectedElementTypeParents.put(ROOT, null);
        expectedElementTypeParents.put(CONTEXT, ROOT);
        expectedElementTypeParents.put(CONTEXT_NAME, CONTEXT);
        expectedElementTypeParents.put(MESSAGE, CONTEXT);
        expectedElementTypeParents.put(TRANSLATION, MESSAGE);
        expectedElementTypeParents.put(SOURCE, MESSAGE);

        this.report = report;
        this.builder.setFileName(filename);
    }

    @Override
    public void setDocumentLocator(final Locator locator) {
        this.locator = locator;
    }

    @Override
    public void startElement(final String namespaceURI,
            final String localName, final String key, final Attributes atts) {
        verifyElementTypeRelation(key);

        elementTypeStack.push(key);

        if (CONTEXT_NAME.equals(key) && contextName != null) {
            throw new ParsingException("Element type \"%s\" can be only used once within element type \"%s\" (Line %d).", CONTEXT_NAME, CONTEXT, this.locator.getLineNumber());
        }

        if (SOURCE.equals(key) && sourceValue != null) {
            throw new ParsingException("Element type \"%s\" can be only used once within element type \"%s\" (Line %d).", SOURCE, MESSAGE, this.locator.getLineNumber());
        }

        if (TRANSLATION.equals(key)) {
            if (translationTagFound) {
                throw new ParsingException("Element type \"%s\" can be only used once within element type \"%s\" (Line %d).", TRANSLATION, MESSAGE, this.locator.getLineNumber());
            }
            translationTagFound = true;

            String translationType = atts.getValue(TRANSLATION_ATTR_TYPE);
            if (translationType != null) {
                applyTranslationType(translationType);
                emitIssue = true;
            }
        }

        if (MESSAGE.equals(key)) {
            builder.setLineStart(this.locator.getLineNumber());
            builder.setColumnStart(lastColumnNumber);
        }

        currentElement = key;

        lastColumnNumber = this.locator.getColumnNumber();
    }

    @Override
    public void endElement(final String uri, final String localName, final String qName) {
        elementTypeStack.pop();
        currentElement = null;
        lastColumnNumber = this.locator.getColumnNumber();

        if (CONTEXT.equals(qName)) {
            contextName = null;
            return;
        }

        if (!MESSAGE.equals(qName)) {
            return;
        }

        if (StringUtils.isBlank(contextName)) {
            throw new ParsingException("Missing or empty element type \"%s\" within element type \"%s\" (Line %d).", CONTEXT_NAME, MESSAGE, this.locator.getLineNumber());
        }
        if (StringUtils.isBlank(sourceValue)) {
            throw new ParsingException("Missing or empty element type \"%s\" within element type \"%s\" (Line %d).", SOURCE, MESSAGE, this.locator.getLineNumber());
        }
        if (!translationTagFound) {
            throw new ParsingException("Missing element type \"%s\" within element type \"%s\" (Line %d).", TRANSLATION, MESSAGE, this.locator.getLineNumber());
        }

        if (emitIssue) {
            builder.setLineEnd(this.locator.getLineNumber());
            builder.setColumnEnd(this.locator.getColumnNumber());

            report.add(builder.build());
        }
        // prepare for next message block
        emitIssue = false;
        translationTagFound = false;
        sourceValue = null;
    }

    @Override
    public void characters(final char[] ch, final int start, final int length) {
        lastColumnNumber = this.locator.getColumnNumber();
        if (CONTEXT_NAME.equals(currentElement)) {
            contextName = new String(ch, start, length);
        }
        if (SOURCE.equals(currentElement)) {
            sourceValue = new String(ch, start, length);
        }
    }

    private void verifyElementTypeRelation(final String element) {
        String parent =  expectedElementTypeParents.getOrDefault(element, "");
        if (parent == null) {
            if (!elementTypeStack.isEmpty()) {
                throw new ParsingException("Element type \"%s\" does not expects to be a root element (Line %d).", element, this.locator.getLineNumber());
            }
            return;
        }

        if (!parent.isEmpty() && !elementTypeStack.lastElement().equals(parent)) {
            throw new ParsingException("Element type \"%s\" expects to be a child element of element type \"%s\" (Line %d).", element, parent, this.locator.getLineNumber());
        }
    }

    private void applyTranslationType(final String translationType) {
        switch (translationType) {
            case TRANSLATION_TYPE_OBSOLETE:
                builder.setSeverity(Severity.WARNING_NORMAL);
                builder.setMessage(TRANSLATION_TYPE_OBSOLETE_MESSAGE);
                break;
            case TRANSLATION_TYPE_UNFINISHED:
                builder.setSeverity(Severity.WARNING_LOW);
                builder.setMessage(TRANSLATION_TYPE_UNFINISHED_MESSAGE);
                break;
            case TRANSLATION_TYPE_VANISHED:
                builder.setSeverity(Severity.WARNING_NORMAL);
                builder.setMessage(TRANSLATION_TYPE_VANISHED_MESSAGE);
                break;
            default:
                throw new ParsingException("Unknown translation state \"%s\" (Line %d).", translationType, this.locator.getLineNumber());
        }
        builder.setCategory(translationType);
    }
}