package edu.hm.hafner.analysis;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.InvalidPathException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.ModuleDetector.FileSystem;

/**
 * Detects module names by parsing the name of OSGi specific source files.
 */
public class OsgiModuleDetector extends AbstractModuleDetector {
    static final String BUNDLE_PROPERTIES = "OSGI-INF/l10n/bundle.properties";
    static final String OSGI_BUNDLE = "META-INF/MANIFEST.MF";
    static final String PLUGIN_PROPERTIES = "plugin.properties";
    private static final String BUNDLE_NAME = "Bundle-Name";
    private static final String BUNDLE_SYMBOLIC_NAME = "Bundle-SymbolicName";
    private static final String BUNDLE_VENDOR = "Bundle-Vendor";
    private static final String REPLACEMENT_CHAR = "%";

    OsgiModuleDetector(final FileSystem fileSystem) {
        super(fileSystem);
    }

    @Override
    String getPattern() {
        return ALL_DIRECTORIES + OSGI_BUNDLE;
    }

    @Override
    public void collectProjects(final Map<String, String> mapping, final List<String> projects) {
        for (String fileName : projects) {
            if (fileName.endsWith(OSGI_BUNDLE)) {
                addMapping(mapping, fileName, OSGI_BUNDLE, parseManifest(fileName));
            }
        }
    }

    /**
     * Scans a Manifest file for OSGi Bundle Information.
     *
     * @param manifestFile
     *         file name of MANIFEST.MF
     *
     * @return the project name or an empty string if the name could not be resolved
     */
    private String parseManifest(final String manifestFile) {
        try (InputStream file = getFactory().open(manifestFile)) {
            Manifest manifest = new Manifest(file);
            Attributes attributes = manifest.getMainAttributes();
            Properties properties = readProperties(StringUtils.substringBefore(manifestFile, OSGI_BUNDLE));
            String name = getLocalizedValue(attributes, properties, BUNDLE_NAME);
            if (StringUtils.isNotBlank(name)) {
                return name;
            }
            return getSymbolicName(attributes, properties);
        }
        catch (IOException | InvalidPathException ignored) {
            // ignore
        }
        return StringUtils.EMPTY;
    }

    private Properties readProperties(final String path) {
        Properties properties = new Properties();
        readProperties(path, properties, PLUGIN_PROPERTIES);
        readProperties(path, properties, BUNDLE_PROPERTIES);

        return properties;
    }

    @SuppressWarnings("OverlyBroadCatchBlock")
    private void readProperties(final String path, final Properties properties, final String fileName) {
        try (InputStream file = getFactory().open(path + SLASH + fileName)) {
            properties.load(file);
        }
        catch (IOException | InvalidPathException ignored) {
            // ignore if properties are not present or not readable
        }
    }

    private String getLocalizedValue(final Attributes attributes, final Properties properties,
            final String bundleName) {
        String value = attributes.getValue(bundleName);
        if (StringUtils.startsWith(StringUtils.trim(value), REPLACEMENT_CHAR)) {
            return properties.getProperty(StringUtils.substringAfter(value, REPLACEMENT_CHAR));
        }
        return value;
    }

    private String getSymbolicName(final Attributes attributes, final Properties properties) {
        String symbolicName = StringUtils.substringBefore(attributes.getValue(BUNDLE_SYMBOLIC_NAME), ";");
        if (StringUtils.isNotBlank(symbolicName)) {
            String vendor = getLocalizedValue(attributes, properties, BUNDLE_VENDOR);
            if (StringUtils.isNotBlank(vendor)) {
                return symbolicName + " (" + vendor + ")";
            }
            else {
                return symbolicName;
            }
        }
        return StringUtils.EMPTY;
    }
}
