package edu.hm.hafner.analysis;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.ModuleDetector.FileSystem;

/**
 * Abstract class for all Module Detectors.
 */
abstract class AbstractModuleDetector {
    static final String SLASH = "/";
    static final String ALL_DIRECTORIES = "**/";
    static final String PLUS = ", ";

    private final FileSystem factory;

    /**
     * Collects all projects of a specific type.
     *  @param mapping the mapping of path prefixes to module names
     * @param projects the projects of a specific type
     */
    abstract void collectProjects(Map<String, String> mapping, List<String> projects);

    /**
     * Returns the names of all project files in the following structure:
     * {@code **\/build.xml}.
     *
     * @return pattern
     */
    abstract String getPattern();

    AbstractModuleDetector(final FileSystem fileSystem) {
        factory = fileSystem;
    }

    void addMapping(final Map<String, String> mapping, final String fileName, final String suffix,
            final String moduleName) {
        if (StringUtils.isNotBlank(moduleName)) {
            mapping.put(StringUtils.substringBeforeLast(fileName, suffix), moduleName);
        }
    }

    public FileSystem getFactory() {
        return factory;
    }
}
