package edu.hm.hafner.analysis.parser;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

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

    static final String TRANSLATION_TYPE_OBSOLETE = "obsolete";
    static final String TRANSLATION_TYPE_UNFINISHED = "unfinished";
    static final String TRANSLATION_TYPE_VANISHED = "vanished";

    static final String TRANSLATION_TYPE_OBSOLETE_MESSAGE =
            "This translation can be removed as the source doesn't exists anymore.";
    static final String TRANSLATION_TYPE_UNFINISHED_MESSAGE =
            "This source string misses a translation.";
    static final String TRANSLATION_TYPE_VANISHED_MESSAGE =
            "The source string can not be found within the sources. "
            + "Remove this translation if it is not used anymore or improve your call to \"tr()\", "
            + "so \"lupdate\" can find it.";

    private final Report report;
    private final IssueBuilder builder = new IssueBuilder();
    private Locator documentLocator;
    private final Deque<String> elementTypeStack = new ArrayDeque<>();
    private final Map<String, String> expectedElementTypeParents = new HashMap<>();
    private String contextName = "";
    private String sourceValue = "";
    private boolean translationTagFound = false;
    private boolean emitIssue = false;
    private int lastColumnNumber;

    /**
     * Creates a new instance of {@link QtTranslationSaxParser}.
     *
     * @param report
     *         the issues
     * @param fileName
     *         path to the translation file
     */
    public QtTranslationSaxParser(final Report report, final String fileName) {
        super();

        expectedElementTypeParents.put(ROOT, null);
        expectedElementTypeParents.put(CONTEXT, ROOT);
        expectedElementTypeParents.put(CONTEXT_NAME, CONTEXT);
        expectedElementTypeParents.put(MESSAGE, CONTEXT);
        expectedElementTypeParents.put(TRANSLATION, MESSAGE);
        expectedElementTypeParents.put(SOURCE, MESSAGE);

        this.report = report;
        this.builder.setFileName(fileName);
    }

    @Override
    public void setDocumentLocator(final Locator locator) {
        this.documentLocator = locator;
    }

    @Override
    public void startElement(final String namespaceURI,
            final String localName, final String key, final Attributes atts) {
        verifyElementTypeRelation(key);

        elementTypeStack.push(key);

        switch (key) {
            case CONTEXT_NAME:
                if (!contextName.isEmpty()) {
                    throw new ParsingException("Element type \"%s\" can be only used once within element type \"%s\" (Line %d).", CONTEXT_NAME, CONTEXT, this.documentLocator
                            .getLineNumber());
                }
                break;
            case SOURCE:
                if (!sourceValue.isEmpty()) {
                    throw new ParsingException("Element type \"%s\" can be only used once within element type \"%s\" (Line %d).", SOURCE, MESSAGE, this.documentLocator
                            .getLineNumber());
                }
                break;
            case TRANSLATION:
                if (translationTagFound) {
                    throw new ParsingException("Element type \"%s\" can be only used once within element type \"%s\" (Line %d).", TRANSLATION, MESSAGE, this.documentLocator
                            .getLineNumber());
                }
                translationTagFound = true;

                String translationType = atts.getValue(TRANSLATION_ATTR_TYPE);
                if (translationType != null) {
                    applyTranslationType(translationType);
                    emitIssue = true;
                }
                break;
            case MESSAGE:
                builder.setLineStart(this.documentLocator.getLineNumber());
                builder.setColumnStart(lastColumnNumber);
                break;
            default:
                break;
        }

        lastColumnNumber = this.documentLocator.getColumnNumber();
    }

    @Override
    public void endElement(final String uri, final String localName, final String qName) {
        elementTypeStack.pop();
        lastColumnNumber = this.documentLocator.getColumnNumber();

        if (CONTEXT.equals(qName)) {
            contextName = "";
            return;
        }

        if (!MESSAGE.equals(qName)) {
            return;
        }

        if (contextName.isEmpty()) {
            throw new ParsingException("Missing or empty element type \"%s\" within element type \"%s\" (Line %d).", CONTEXT_NAME, MESSAGE, this.documentLocator
                    .getLineNumber());
        }
        if (sourceValue.isEmpty()) {
            throw new ParsingException("Missing or empty element type \"%s\" within element type \"%s\" (Line %d).", SOURCE, MESSAGE, this.documentLocator
                    .getLineNumber());
        }
        if (!translationTagFound) {
            throw new ParsingException("Missing element type \"%s\" within element type \"%s\" (Line %d).", TRANSLATION, MESSAGE, this.documentLocator
                    .getLineNumber());
        }

        if (emitIssue) {
            builder.setLineEnd(this.documentLocator.getLineNumber());
            builder.setColumnEnd(this.documentLocator.getColumnNumber());

            report.add(builder.build());
        }
        // prepare for next message block
        emitIssue = false;
        translationTagFound = false;
        sourceValue = "";
    }

    @Override
    public void characters(final char[] ch, final int start, final int length) {
        lastColumnNumber = this.documentLocator.getColumnNumber();
        if (CONTEXT_NAME.equals(elementTypeStack.getFirst())) {
            contextName = new String(ch, start, length);
        }
        if (SOURCE.equals(elementTypeStack.getFirst())) {
            sourceValue = new String(ch, start, length);
        }
    }

    private void verifyElementTypeRelation(final String element) {
        String parent =  expectedElementTypeParents.getOrDefault(element, "");
        if (parent == null) {
            if (!elementTypeStack.isEmpty()) {
                throw new ParsingException("Element type \"%s\" does not expects to be a root element (Line %d).", element, this.documentLocator
                        .getLineNumber());
            }
            return;
        }

        if (!parent.isEmpty() && !elementTypeStack.getFirst().equals(parent)) {
            throw new ParsingException("Element type \"%s\" expects to be a child element of element type \"%s\" (Line %d).", element, parent, this.documentLocator
                    .getLineNumber());
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
                throw new ParsingException("Unknown translation state \"%s\" (Line %d).", translationType, this.documentLocator
                        .getLineNumber());
        }
        builder.setCategory(translationType);
    }
}