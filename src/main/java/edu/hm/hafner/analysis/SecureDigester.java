package edu.hm.hafner.analysis;

import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.digester3.Digester;
import org.xml.sax.InputSource;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import static org.apache.xerces.impl.Constants.*;

/**
 * A secure {@link Digester} implementation that does not resolve external entities.
 *
 * @author Ullrich Hafner
 */
public final class SecureDigester extends Digester {
    /**
     * Creates a new {@link Digester} instance that does not resolve external entities.
     *
     * @param classWithClassLoader the class to get the class loader from
     */
    public SecureDigester(final Class<?> classWithClassLoader) {
        super();

        setClassLoader(classWithClassLoader.getClassLoader());

        SAXParserFactory factory = getFactory();
        setFeature(factory, XERCES_FEATURE_PREFIX, CREATE_ENTITY_REF_NODES_FEATURE, false);
        setFeature(factory, XERCES_FEATURE_PREFIX, LOAD_DTD_GRAMMAR_FEATURE, false);
        setFeature(factory, XERCES_FEATURE_PREFIX, LOAD_EXTERNAL_DTD_FEATURE, false);
        setFeature(factory, SAX_FEATURE_PREFIX, DISALLOW_DOCTYPE_DECL_FEATURE, true);
        setFeature(factory, SAX_FEATURE_PREFIX, EXTERNAL_GENERAL_ENTITIES_FEATURE, false);
        setFeature(factory, SAX_FEATURE_PREFIX, EXTERNAL_PARAMETER_ENTITIES_FEATURE, false);
        setFeature(factory, SAX_FEATURE_PREFIX, RESOLVE_DTD_URIS_FEATURE, false);
        setFeature(factory, SAX_FEATURE_PREFIX, USE_ENTITY_RESOLVER2_FEATURE, false);
        factory.setXIncludeAware(false);
        setValidating(false);
        setEntityResolver((publicId, systemId) -> new InputSource());
    }

    @SuppressFBWarnings
    @SuppressWarnings("illegalcatch")
    private void setFeature(final SAXParserFactory factory, final String prefix, final String feature,
            final boolean value) {
        try {
            factory.setFeature(prefix + feature, value);
        }
        catch (Exception ignored) {
            // ignore and continue
        }
    }
}
