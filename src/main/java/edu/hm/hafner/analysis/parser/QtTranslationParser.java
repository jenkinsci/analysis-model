package edu.hm.hafner.analysis.parser;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Parser for translation files of Qt.
 *
 * @author Heiko Thiel
 */
public class QtTranslationParser extends IssueParser {
    private static final long serialVersionUID = 1L;

    static final String TRANSLATION_TYPE_OBSOLETE = "obsolete";
    static final String TRANSLATION_TYPE_UNFINISHED = "unfinished";
    static final String TRANSLATION_TYPE_UNFINISHED_NOT_EMPTY = "unfinished_not_empty";
    static final String TRANSLATION_TYPE_VANISHED = "vanished";

    static final String TRANSLATION_TYPE_OBSOLETE_MESSAGE =
            "This translation can be removed because the source no longer exists.";
    static final String TRANSLATION_TYPE_UNFINISHED_MESSAGE =
            "This source string is missing a translation.";
    static final String TRANSLATION_TYPE_UNFINISHED_NOT_EMPTY_MESSAGE =
            "This source string contains a translation, but is still marked as unfinished.";
    static final String TRANSLATION_TYPE_VANISHED_MESSAGE =
            "The source string cannot be found within the sources. "
                    + "Remove this translation if it is no longer used, or improve your call to \"tr()\" "
                    + "so that \"lupdate\" can find it.";

    @Override
    public Report parse(final ReaderFactory readerFactory) throws ParsingException {
        Report report = new Report();
        readerFactory.parse(new QtTranslationSaxParser(report, readerFactory.getFileName()));
        return report;
    }

    /**
     * Handles the parsing of a translation file from Qt.
     */
    static class QtTranslationSaxParser extends DefaultHandler {
        private static final String CONTEXT = "context";
        private static final String CONTEXT_NAME = "name";
        private static final String MESSAGE = "message";
        private static final String MESSAGE_NUMERUS_ATTRIBUTE = "numerus";
        private static final String MESSAGE_NUMERUS_ENABLED_VALUE = "yes";
        private static final String NUMERUSFORM = "numerusform";
        private static final String ROOT = "TS";
        private static final String SOURCE = "source";
        private static final String TRANSLATION = "translation";
        private static final String TRANSLATION_ATTR_TYPE = "type";

        // The locator will be initialized within setDocumentLocator, which will be called by SAXParser.
        @SuppressFBWarnings("NP_NONNULL_FIELD_NOT_INITIALIZED_IN_CONSTRUCTOR")
        @SuppressWarnings("NullAway")
        private Locator documentLocator;

        private final Report report;
        private final IssueBuilder builder = new IssueBuilder();
        private final Deque<String> elementTypeStack = new ArrayDeque<>();
        private final Map<String, String> expectedElementTypeParents = new HashMap<>();
        private String contextName = "";
        private String sourceValue = "";
        private String translationType = "";
        private boolean hasTranslatedString = false;
        private boolean translationTagFound = false;
        private boolean messageIsNumerus = false;
        private int lastColumnNumber;

        /**
         * Creates a new instance of {@link QtTranslationSaxParser}.
         *
         * @param report
         *         the issues
         * @param fileName
         *         path to the translation file
         */
        QtTranslationSaxParser(final Report report, final String fileName) {
            super();

            expectedElementTypeParents.put(ROOT, null);
            expectedElementTypeParents.put(CONTEXT, ROOT);
            expectedElementTypeParents.put(CONTEXT_NAME, CONTEXT);
            expectedElementTypeParents.put(MESSAGE, CONTEXT);
            expectedElementTypeParents.put(NUMERUSFORM, TRANSLATION);
            expectedElementTypeParents.put(TRANSLATION, MESSAGE);
            expectedElementTypeParents.put(SOURCE, MESSAGE);

            this.report = report;
            builder.setFileName(fileName);
        }

        @Override
        public void setDocumentLocator(final Locator locator) {
            documentLocator = locator;
        }

        @Override
        public void startElement(final String namespaceURI,
                final String localName, final String key, final Attributes atts) {
            verifyElementTypeRelation(key);

            elementTypeStack.push(key);

            switch (key) {
                case CONTEXT_NAME:
                    throwParsingExceptionBecauseOfDuplicatedOccurrence(!contextName.isEmpty(), key);
                    break;
                case SOURCE:
                    throwParsingExceptionBecauseOfDuplicatedOccurrence(!sourceValue.isEmpty(), key);
                    break;
                case TRANSLATION:
                    throwParsingExceptionBecauseOfDuplicatedOccurrence(translationTagFound, key);
                    translationTagFound = true;

                    translationType = atts.getValue(TRANSLATION_ATTR_TYPE);
                    if (translationType != null) {
                        applyTranslationType();
                    }
                    break;
                case MESSAGE:
                    builder.setLineStart(documentLocator.getLineNumber());
                    builder.setColumnStart(lastColumnNumber);

                    messageIsNumerus = MESSAGE_NUMERUS_ENABLED_VALUE.equals(atts.getValue(MESSAGE_NUMERUS_ATTRIBUTE));
                    break;
                case NUMERUSFORM:
                    if (!messageIsNumerus) {
                        throw new ParsingException(
                                "Element type \"%s\" is allowed only if the attribute \"%s\" within the parent element \"%s\" is set to \"%s\" (line %d).",
                                NUMERUSFORM,
                                MESSAGE_NUMERUS_ATTRIBUTE,
                                MESSAGE,
                                MESSAGE_NUMERUS_ENABLED_VALUE,
                                documentLocator.getLineNumber());
                    }
                    break;
                default:
                    break;
            }

            lastColumnNumber = documentLocator.getColumnNumber();
        }

        @Override
        public void endElement(final String uri, final String localName, final String qName) {
            elementTypeStack.pop();
            lastColumnNumber = documentLocator.getColumnNumber();

            if (CONTEXT.equals(qName)) {
                contextName = "";
                return;
            }

            if (!MESSAGE.equals(qName)) {
                return;
            }

            throwParsingExceptionBecauseOfMissingElementType(contextName.isEmpty(), CONTEXT_NAME);
            throwParsingExceptionBecauseOfMissingElementType(sourceValue.isEmpty(), SOURCE);
            throwParsingExceptionBecauseOfMissingElementType(!translationTagFound, TRANSLATION);

            if (translationType != null) {
                // In case the translation type is "unfinished" and we found a translation,
                // change the type to TRANSLATION_TYPE_UNFINISHED_NOT_EMPTY.
                // This case is not handled within {@link #applyTranslationType(String)}
                // because it is not an official state within a translation file of Qt.
                if (TRANSLATION_TYPE_UNFINISHED.equals(translationType) && hasTranslatedString) {
                    builder.setSeverity(Severity.WARNING_LOW);
                    builder.setMessage(TRANSLATION_TYPE_UNFINISHED_NOT_EMPTY_MESSAGE);
                    builder.setCategory(TRANSLATION_TYPE_UNFINISHED_NOT_EMPTY);
                }

                builder.setLineEnd(documentLocator.getLineNumber());
                builder.setColumnEnd(documentLocator.getColumnNumber());

                report.add(builder.build());
            }
            // prepare for next message block
            hasTranslatedString = false;
            translationTagFound = false;
            sourceValue = "";
        }

        @Override @SuppressWarnings("PMD.UnusedAssignment") // false positive in PMD 7.0.0
        public void characters(final char[] ch, final int start, final int length) {
            lastColumnNumber = documentLocator.getColumnNumber();
            if (CONTEXT_NAME.equals(elementTypeStack.getFirst())) {
                contextName = new String(ch, start, length);
            }
            if (SOURCE.equals(elementTypeStack.getFirst())) {
                sourceValue = new String(ch, start, length);
            }
            if (TRANSLATION.equals(elementTypeStack.getFirst())) {
                // In case the message is numerus, the decision is made by a separate element.
                // Keep the value to true if it is already set.
                hasTranslatedString |= !messageIsNumerus;
            }
            if (NUMERUSFORM.equals(elementTypeStack.getFirst())) {
                hasTranslatedString = true;
            }
        }

        private void verifyElementTypeRelation(final String element) {
            String parent = expectedElementTypeParents.getOrDefault(element, "");
            if (parent == null) {
                if (!elementTypeStack.isEmpty()) {
                    throw new ParsingException("Element type \"%s\" does not expect to be a root element (line %d).",
                            element,
                            documentLocator.getLineNumber());
                }
                return;
            }

            if (!parent.isEmpty() && !elementTypeStack.getFirst().equals(parent)) {
                throw new ParsingException(
                        "Element type \"%s\" expects to be a child element of element type \"%s\" (line %d).",
                        element,
                        parent,
                        documentLocator.getLineNumber());
            }
        }

        @SuppressWarnings("NullAway")
        private void throwParsingExceptionBecauseOfDuplicatedOccurrence(final boolean shouldThrow,
                final String element) {
            if (shouldThrow) {
                throw new ParsingException(
                        "Element type \"%s\" can be used only once within element type \"%s\" (line %d).",
                        element,
                        expectedElementTypeParents.get(element),
                        documentLocator.getLineNumber());
            }
        }

        @SuppressWarnings("NullAway")
        private void throwParsingExceptionBecauseOfMissingElementType(final boolean shouldThrow, final String element) {
            if (shouldThrow) {
                throw new ParsingException(
                        "Missing or empty element type \"%s\" within element type \"%s\" (line %d).",
                        element,
                        expectedElementTypeParents.get(element),
                        documentLocator.getLineNumber());
            }
        }

        private void applyTranslationType() {
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
                    throw new ParsingException("Unknown translation state \"%s\" (line %d).",
                            translationType,
                            documentLocator.getLineNumber());
            }
            builder.setCategory(translationType);
        }

        @Override
        public void endDocument() throws SAXException {
            builder.close();
        }
    }
}
