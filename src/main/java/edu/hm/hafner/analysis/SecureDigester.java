package edu.hm.hafner.analysis;

import org.apache.commons.digester3.Digester;
import org.xml.sax.InputSource;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

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
        setValidating(false);
        disableFeature("external-general-entities");
        disableFeature("external-parameter-entities");
        setEntityResolver((publicId, systemId) -> new InputSource());
    }

    @SuppressWarnings("all") @SuppressFBWarnings
    private void disableFeature(final String feature) {
        try {
            setFeature("http://xml.org/sax/features/" + feature, false);
        }
        catch (Exception ignored) {
            // ignore and continue
        }
    }
}
