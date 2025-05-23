package edu.hm.hafner.analysis;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.errorprone.annotations.MustBeClosed;

import edu.hm.hafner.util.PathUtil;

/**
 * Detects module names by parsing the name of a source file, the Maven pom.xml file or the ANT build.xml file.
 *
 * @author Ullrich Hafner
 * @author Christoph Laeubrich (support for OSGi-Bundles)
 */
public class ModuleDetectorRunner {
    private static final String BACK_SLASH = "\\";
    private static final String SLASH = "/";

    /** A list of all module detectors (e.g., Maven). */
    private final List<AbstractModuleDetector> moduleDetectors;
    /** The factory to create input streams with. */
    private final FileSystemFacade fileSystemFacade;
    /** Maps file names to module names. */
    private final Map<String, String> fileNameToModuleName;
    /** Sorted list of file name prefixes. */
    private final List<String> prefixes;

    /**
     * Creates a new instance of {@link ModuleDetectorRunner}.
     *
     * @param workspace
     *         the workspace to scan for module files
     * @param fileSystemFacade
     *         file system facade to find and load the files with
     */
    public ModuleDetectorRunner(final Path workspace, final FileSystemFacade fileSystemFacade) {
        this.fileSystemFacade = fileSystemFacade;

        moduleDetectors = Arrays.asList(
                new AntModuleDetector(this.fileSystemFacade),
                new GradleModuleDetector(this.fileSystemFacade),
                new MavenModuleDetector(this.fileSystemFacade),
                new OsgiModuleDetector(this.fileSystemFacade)
        );

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

        List<String> projects = find(workspace);

        for (AbstractModuleDetector moduleDetector : moduleDetectors) {
            moduleDetector.collectProjects(mapping, projects);
        }

        return mapping;
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
        var fullPath = originalFileName.replace('\\', '/');

        var guessedModule = StringUtils.EMPTY;
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
    private List<String> find(final Path path) {
        List<String> absoluteFileNames = new ArrayList<>();

        for (AbstractModuleDetector moduleDetector : moduleDetectors) {
            var relativeFileNames = fileSystemFacade.find(path, moduleDetector.getPattern());
            for (String relativeFileName : relativeFileNames) {
                var relativePath = normalizePath(relativeFileName);
                if (relativePath.startsWith(SLASH)) {
                    absoluteFileNames.add(relativePath);
                }
                else {
                    absoluteFileNames.add(new PathUtil().getAbsolutePath(path) + SLASH + relativePath);
                }
            }
        }

        return absoluteFileNames;
    }

    private String normalizePath(final String fileName) {
        return fileName.replace(BACK_SLASH, SLASH);
    }

    /**
     * Facade for file system operations. May be replaced by stubs in test cases.
     */
    public interface FileSystemFacade {
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
        List<String> find(Path root, String pattern);

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
