package edu.hm.hafner.analysis;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;

import org.apache.commons.io.input.ReaderInputStream;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.helpers.DefaultHandler;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import static javax.xml.XMLConstants.*;
import static org.apache.xerces.impl.Constants.*;

/**
 * Factory for XML Parsers that prevent XML External Entity attacks. Those attacks occur when untrusted XML input
 * containing a reference to an external entity is processed by a weakly configured XML parser.
 *
 * @author Ullrich Hafner
 * @see <a href="https://cheatsheetseries.owasp.org/cheatsheets/XML_External_Entity_Prevention_Cheat_Sheet.html">XML External Entity Prevention Cheat Sheet</a>
 * @see <a href="https://rules.sonarsource.com/java/RSPEC-2755">XML parsers should not be vulnerable to XXE attacks</a>
 */
public class SecureXmlParserFactory {
    private static final String[] ENABLED_PROPERTIES = {
//            XERCES_FEATURE_PREFIX + DISALLOW_DOCTYPE_DECL_FEATURE,   - If this feature is activated we cannot parse any XML documents that use a DOCTYPE anymore
            FEATURE_SECURE_PROCESSING
    };
    private static final String[] DISABLED_PROPERTIES = {
            SAX_FEATURE_PREFIX + EXTERNAL_GENERAL_ENTITIES_FEATURE,
            SAX_FEATURE_PREFIX + EXTERNAL_PARAMETER_ENTITIES_FEATURE,
            SAX_FEATURE_PREFIX + RESOLVE_DTD_URIS_FEATURE,
            SAX_FEATURE_PREFIX + USE_ENTITY_RESOLVER2_FEATURE,
            XERCES_FEATURE_PREFIX + CREATE_ENTITY_REF_NODES_FEATURE,
            XERCES_FEATURE_PREFIX + LOAD_DTD_GRAMMAR_FEATURE,
            XERCES_FEATURE_PREFIX + LOAD_EXTERNAL_DTD_FEATURE
    };
    private static final String[] DISABLED_ATTRIBUTES = {
            ACCESS_EXTERNAL_DTD,
            ACCESS_EXTERNAL_SCHEMA,
            ACCESS_EXTERNAL_STYLESHEET
    };
    private static final String CLEAR_ATTRIBUTE = "";
    private static final String SUPPORTING_EXTERNAL_ENTITIES = "javax.xml.stream.isSupportingExternalEntities";

    /**
     * Creates a new instance of a {@link DocumentBuilder} that does not resolve external entities.
     *
     * @return a new instance of a {@link DocumentBuilder}
     */
    public DocumentBuilder createDocumentBuilder() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setXIncludeAware(false);
            factory.setExpandEntityReferences(false);
            factory.setFeature(FEATURE_SECURE_PROCESSING, true);
            setFeatures(factory);
            clearAttributes(factory);

            return factory.newDocumentBuilder();
        }
        catch (ParserConfigurationException exception) {
            throw new IllegalArgumentException("Can't create instance of DocumentBuilder", exception);
        }
    }

    private void setFeatures(final DocumentBuilderFactory factory) {
        for (String enabledProperty : ENABLED_PROPERTIES) {
            setFeature(factory, enabledProperty, true);
        }
        for (String disabledProperty : DISABLED_PROPERTIES) {
            setFeature(factory, disabledProperty, false);
        }
    }

    private void setFeature(final DocumentBuilderFactory factory, final String enabledProperty, final boolean value) {
        try {
            factory.setFeature(enabledProperty, value);
        }
        catch (ParserConfigurationException ignored) {
            // ignore and continue
        }
    }

    private void clearAttributes(final DocumentBuilderFactory factory) {
        for (String securityAttribute: DISABLED_ATTRIBUTES) {
            try {
                factory.setAttribute(securityAttribute, CLEAR_ATTRIBUTE);
            }
            catch (IllegalArgumentException e) {
                // ignore and continue
            }
        }
    }

    private void clearAttributes(final TransformerFactory transformerFactory) {
        for (String securityAttribute: DISABLED_ATTRIBUTES) {
            try {
                transformerFactory.setAttribute(securityAttribute, CLEAR_ATTRIBUTE);
            }
            catch (IllegalArgumentException e) {
                // ignore and continue
            }
        }
    }
    /**
     * Creates a new instance of a {@link SAXParser} that does not resolve external entities.
     *
     * @return a new instance of a {@link SAXParser}
     */
    public SAXParser createSaxParser() {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            configureSaxParserFactory(factory);

            SAXParser parser = factory.newSAXParser();
            secureParser(parser);
            return parser;
        }
        catch (ParserConfigurationException | SAXException exception) {
            throw new IllegalArgumentException("Can't create instance of SAXParser", exception);
        }
    }

    /**
     * Secure the {@link SAXParser} so that it does not resolve external entities.
     *
     * @param parser the parser to secure
     */
    private void secureParser(final SAXParser parser) {
        for (String securityAttribute: DISABLED_ATTRIBUTES) {
            try {
                parser.setProperty(securityAttribute, CLEAR_ATTRIBUTE);
            }
            catch (SAXNotRecognizedException | SAXNotSupportedException e) {
                // ignore and continue
            }
        }
    }

    /**
     * Configures a {@link SAXParserFactory} so that it does not resolve external entities.
     *
     * @param factory
     *         the facotry to configure
     */
    public void configureSaxParserFactory(final SAXParserFactory factory) {
        factory.setValidating(false);
        factory.setXIncludeAware(false);

        for (String enabledProperty : ENABLED_PROPERTIES) {
            try {
                factory.setFeature(enabledProperty, true);
            }
            catch (ParserConfigurationException | SAXException ignored) {
                // ignore and continue
            }
        }
        for (String disabledProperty : DISABLED_PROPERTIES) {
            try {
                factory.setFeature(disabledProperty, false);
            }
            catch (ParserConfigurationException | SAXException ignored) {
                // ignore and continue
            }
        }
    }

    /**
     * Creates a new instance of a {@link XMLStreamReader} that does not resolve external entities.
     *
     * @param reader
     *         the reader to wrap
     *
     * @return a new instance of a {@link XMLStreamReader}
     */
    public XMLStreamReader createXmlStreamReader(final Reader reader) {
        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
            factory.setProperty(SUPPORTING_EXTERNAL_ENTITIES, false);
            return factory.createXMLStreamReader(reader);
        }
        catch (XMLStreamException exception) {
            throw new IllegalArgumentException("Can't create instance of XMLStreamReader", exception);
        }
    }

    /**
     * Creates a {@link SAXParser} that does not resolve external entities and parses the provided content with the
     * given SAX {@link DefaultHandler}.
     *
     * @param reader
     *         the content that should be parsed
     * @param charset
     *         the charset to use when reading the content
     * @param handler
     *         the SAX handler to parse the file
     *
     * @throws ParsingException
     *         if the file could not be parsed
     */
    @SuppressFBWarnings(value = "XXE_SAXPARSER", justification = "The parser is secured in the called method")
    public void parse(final Reader reader, final Charset charset, final DefaultHandler handler) {
        try {
            createSaxParser().parse(createInputSource(reader, charset), handler);
        }
        catch (SAXException | IOException exception) {
            throw new ParsingException(exception);
        }
    }

    /**
     * Parses the provided content into a {@link Document}.
     *
     * @param reader
     *         the content that should be parsed
     * @param charset
     *         the charset to use when reading the content
     *
     * @return the file content as document
     * @throws ParsingException
     *         if the file could not be parsed
     */
    @SuppressFBWarnings(value = "XXE_DOCUMENT", justification = "The parser is secured in the called method")
    public Document readDocument(final Reader reader, final Charset charset) {
        try {
            return createDocumentBuilder().parse(createInputSource(reader, charset));
        }
        catch (SAXException | IOException exception) {
            throw new ParsingException(exception);
        }
    }

    private InputSource createInputSource(final Reader reader, final Charset charset) {
        return new InputSource(new ReaderInputStream(reader, charset));
    }

    /**
     * Creates a {@link Transformer} that does not resolve external entities and stylesheets.
     *
     * @return the created {@link Transformer}
     */
    @SuppressFBWarnings(value = {"XXE_DTD_TRANSFORM_FACTORY", "XXE_XSLT_TRANSFORM_FACTORY"}, justification = "The transformer is secured in the called method")
    public Transformer createTransformer() {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();

            clearAttributes(transformerFactory);

            return transformerFactory.newTransformer();
        }
        catch (TransformerConfigurationException exception) {
            throw new ParsingException(exception);
        }
    }
}
