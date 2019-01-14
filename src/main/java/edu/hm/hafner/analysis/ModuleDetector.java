package edu.hm.hafner.analysis;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.apache.commons.lang3.StringUtils;
import org.xml.sax.SAXException;

import com.google.errorprone.annotations.MustBeClosed;

import edu.hm.hafner.util.PathUtil;

/**
 * Detects module names by parsing the name of a source file, the Maven pom.xml file or the ANT build.xml file.
 *
 * @author Ullrich Hafner
 * @author Christoph Laeubrich (support for OSGi-Bundles)
 */
public class ModuleDetector {
    private static final String PLUS = ", ";
    private static final String BACK_SLASH = "\\";
    private static final String SLASH = "/";
    private static final String ALL_DIRECTORIES = "**/";

    private static final String BUNDLE_VENDOR = "Bundle-Vendor";
    private static final String BUNDLE_SYMBOLIC_NAME = "Bundle-SymbolicName";
    private static final String BUNDLE_NAME = "Bundle-Name";
    private static final String REPLACEMENT_CHAR = "%";

    static final String MAVEN_POM = "pom.xml";
    static final String ANT_PROJECT = "build.xml";
    static final String OSGI_BUNDLE = "META-INF/MANIFEST.MF";

    private static final String PATTERN = ALL_DIRECTORIES + MAVEN_POM
            + PLUS + ALL_DIRECTORIES + ANT_PROJECT
            + PLUS + ALL_DIRECTORIES + OSGI_BUNDLE;
    static final String PLUGIN_PROPERTIES = "plugin.properties";
    static final String BUNDLE_PROPERTIES = "OSGI-INF/l10n/bundle.properties";

    /** The factory to create input streams with. */
    private final FileSystem factory;
    /** Maps file names to module names. */
    private final Map<String, String> fileNameToModuleName;
    /** Sorted list of file name prefixes. */
    private final List<String> prefixes;

    /**
     * Creates a new instance of {@link ModuleDetector}.
     *
     * @param workspace
     *         the workspace to scan for Maven pom.xml or ant build.xml files
     * @param fileSystem
     *         file system facade to find and load files with
     */
    public ModuleDetector(final Path workspace, final FileSystem fileSystem) {
        factory = fileSystem;
        fileNameToModuleName = createFilesToModuleMapping(workspace);
        prefixes = new ArrayList<>(fileNameToModuleName.keySet());
        Collections.sort(prefixes);
    }

    /**
     * Returns a mapping of path prefixes to module names.
     *
     * @param workspace
     *         the workspace to start scanning for files
     *
     * @return the mapping of path prefixes to module names
     */
    private Map<String, String> createFilesToModuleMapping(final Path workspace) {
        Map<String, String> mapping = new HashMap<>();

        String[] projects = find(workspace);
        for (String fileName : projects) {
            if (fileName.endsWith(ANT_PROJECT)) {
                addMapping(mapping, fileName, ANT_PROJECT, parseBuildXml(fileName));
            }
        }
        for (String fileName : projects) {
            if (fileName.endsWith(MAVEN_POM)) {
                addMapping(mapping, fileName, MAVEN_POM, parsePom(fileName));
            }
        }
        for (String fileName : projects) {
            if (fileName.endsWith(OSGI_BUNDLE)) {
                addMapping(mapping, fileName, OSGI_BUNDLE, parseManifest(fileName));
            }
        }

        return mapping;
    }

    private void addMapping(final Map<String, String> mapping, final String fileName, final String suffix,
            final String moduleName) {
        if (StringUtils.isNotBlank(moduleName)) {
            mapping.put(StringUtils.substringBeforeLast(fileName, suffix), moduleName);
        }
    }

    /**
     * Uses the path prefixes of pom.xml or build.xml files to guess a module name for the specified file.
     *
     * @param originalFileName
     *         file name to guess a module for, must be an absolute path
     *
     * @return a module name or an empty string
     */
    public String guessModuleName(final String originalFileName) {
        String fullPath = originalFileName.replace('\\', '/');

        String guessedModule = StringUtils.EMPTY;
        for (String path : prefixes) {
            if (fullPath.startsWith(path) && fileNameToModuleName.containsKey(path)) {
                guessedModule = fileNameToModuleName.get(path);
            }
        }
        return guessedModule;
    }

    /**
     * Finds files of the matching pattern.
     *
     * @param path
     *         root path to scan in
     *
     * @return the found files (as absolute paths)
     */
    private String[] find(final Path path) {
        String[] relativeFileNames = factory.find(path, PATTERN);
        String[] absoluteFileNames = new String[relativeFileNames.length];

        for (int file = 0; file < absoluteFileNames.length; file++) {
            String relativePath = normalizePath(relativeFileNames[file]);
            if (relativePath.startsWith(SLASH)) {
                absoluteFileNames[file] = relativePath;
            }
            else {
                absoluteFileNames[file] = new PathUtil().getAbsolutePath(path) + SLASH + relativePath;
            }
        }
        return absoluteFileNames;
    }

    private String normalizePath(final String fileName) {
        return fileName.replace(BACK_SLASH, SLASH);
    }

    /**
     * Returns the project name stored in the build.xml.
     *
     * @param buildXml
     *         Ant build.xml file name
     *
     * @return the project name or an empty string if the name could not be resolved
     */
    private String parseBuildXml(final String buildXml) {
        try (InputStream file = factory.open(buildXml)) {
            SecureDigester digester = new SecureDigester(ModuleDetector.class);

            digester.push(new StringBuilder());
            String xPath = "project";
            digester.addCallMethod(xPath, "append", 1);
            digester.addCallParam(xPath, 0, "name");

            StringBuilder result = digester.parse(file);
            return result.toString();
        }
        catch (IOException | SAXException | InvalidPathException ignored) {
            // ignore
        }
        return StringUtils.EMPTY;
    }

    /**
     * Returns the project name stored in the POM.
     *
     * @param pom
     *         Maven POM file name
     *
     * @return the project name or an empty string if the name could not be resolved
     */
    private String parsePom(final String pom) {
        String name = parsePomAttribute(pom, "name");

        return StringUtils.defaultIfBlank(name, parsePomAttribute(pom, "artifactId"));
    }

    @SuppressWarnings("OverlyBroadCatchBlock")
    private String parsePomAttribute(final String pom, final String tagName) {
        try (InputStream file = factory.open(pom)) {
            SecureDigester digester = new SecureDigester(ModuleDetector.class);
            digester.push(new StringBuilder());
            digester.addCallMethod("project/" + tagName, "append", 0);

            StringBuilder result = digester.parse(file);
            return result.toString();
        }
        catch (IOException | SAXException | InvalidPathException ignored) {
            // ignore
        }
        return StringUtils.EMPTY;
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
        try (InputStream file = factory.open(manifestFile)) {
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

    private String getLocalizedValue(final Attributes attributes, final Properties properties,
            final String bundleName) {
        String value = attributes.getValue(bundleName);
        if (StringUtils.startsWith(StringUtils.trim(value), REPLACEMENT_CHAR)) {
            return properties.getProperty(StringUtils.substringAfter(value, REPLACEMENT_CHAR));
        }
        return value;
    }

    private Properties readProperties(final String path) {
        Properties properties = new Properties();
        readProperties(path, properties, PLUGIN_PROPERTIES);
        readProperties(path, properties, BUNDLE_PROPERTIES);

        return properties;
    }

    @SuppressWarnings("OverlyBroadCatchBlock")
    private void readProperties(final String path, final Properties properties, final String fileName) {
        try (InputStream file = factory.open(path + SLASH + fileName)) {
            properties.load(file);
        }
        catch (IOException | InvalidPathException ignored) {
            // ignore if properties are not present or not readable
        }
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

    /**
     * Facade for file system operations. May be replaced by stubs in test cases.
     */
    public interface FileSystem {
        /**
         * Returns all file names that match the specified pattern.
         *
         * @param root
         *         root directory to start the search from
         * @param pattern
         *         the Ant pattern to search for
         *
         * @return the found file names
         */
        String[] find(Path root, String pattern);

        /**
         * Creates an {@link InputStream} from the specified filename.
         *
         * @param fileName
         *         the file name
         *
         * @return the input stream
         * @throws IOException
         *         if the stream could not be opened
         * @throws InvalidPathException
         *         if the file name is invalid
         */
        @MustBeClosed
        InputStream open(String fileName) throws IOException, InvalidPathException;
    }
}

