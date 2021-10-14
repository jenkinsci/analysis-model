package edu.hm.hafner.analysis;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.InvalidPathException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.ModuleDetector.FileSystem;

/**
 * Detects module names by parsing gradle source files.
 */
public class GradleModuleDetector extends AbstractModuleDetector {
    static final String BUILD_GRADLE = "build.gradle";
    static final String BUILD_GRADLE_KTS = "build.gradle.kts";
    static final String SETTINGS_GRADLE = "settings.gradle";
    static final String SETTINGS_GRADLE_KTS = "settings.gradle.kts";

    /**
     * Detects variations of {@code rootProject.setName($string)}, as well as the Groovy setter shorthand {@code
     * rootProject.name = $string}.
     */
    private static final Pattern RE_GRADLE_SET_PROJECT_NAME =
            Pattern.compile("^\\s*rootProject\\.(name\\s*=|setName\\(?)\\s*['\"]([^'\"]*)['\"]\\)?");

    GradleModuleDetector(final FileSystem fileSystem) {
        super(fileSystem);
    }

    @Override
    String getPattern() {
        return ALL_DIRECTORIES + SETTINGS_GRADLE + PLUS + ALL_DIRECTORIES + SETTINGS_GRADLE_KTS;
    }

    @Override
    public void collectProjects(final Map<String, String> mapping, final List<String> projects) {
        for (String fileName : projects) {
            if (fileName.endsWith(BUILD_GRADLE) || fileName.endsWith(BUILD_GRADLE_KTS)) {
                addMapping(mapping, fileName, BUILD_GRADLE, parseGradle(fileName));
            }
            else if (fileName.endsWith(SETTINGS_GRADLE) || fileName.endsWith(SETTINGS_GRADLE_KTS)) {
                addMapping(mapping, fileName, SETTINGS_GRADLE, parseGradleSettings(fileName));
            }
        }
    }

    /**
     * Returns the project name estimated from the build.gradle file path.
     *
     * @param buildScript
     *         Gradle build.gradle file path
     *
     * @return the project name or an empty string if the name could not be resolved
     */
    private String parseGradle(final String buildScript) {
        String basePath = FilenameUtils.getPathNoEndSeparator(buildScript);
        String parentDirName = FilenameUtils.getName(basePath);
        return StringUtils.trimToEmpty(parentDirName);
    }

    /**
     * Returns the root project name from the settings.gradle file.
     *
     * @param settingsFile
     *         Gradle settings.gradle file path
     *
     * @return the root project override, or an empty string if the name could not be resolved
     */
    private String parseGradleSettings(final String settingsFile) {
        String name = null;

        try (InputStream input = getFactory().open(settingsFile);
                Scanner scan = new Scanner(input, "UTF-8")) {
            while (scan.hasNextLine()) {
                String line = scan.findInLine(RE_GRADLE_SET_PROJECT_NAME);

                if (line != null) {
                    name = scan.match().group(2);
                    break;
                }

                scan.nextLine();
            }
        }
        catch (IOException | InvalidPathException ignored) {
            // ignore
        }

        return StringUtils.defaultIfBlank(name, StringUtils.EMPTY);
    }
}
