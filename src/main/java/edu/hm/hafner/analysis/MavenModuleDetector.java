package edu.hm.hafner.analysis;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.InvalidPathException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.xml.sax.SAXException;

import edu.hm.hafner.analysis.ModuleDetector.FileSystem;

/**
 * Detects module names by parsing the name of a source file, the Maven pom.xml.
 */
public class MavenModuleDetector extends AbstractModuleDetector {
    static final String MAVEN_POM = "pom.xml";

    MavenModuleDetector(final FileSystem fileSystem) {
        super(fileSystem);
    }

    @Override
    String getPattern() {
        return ALL_DIRECTORIES + MAVEN_POM;
    }

    @Override
    public void collectProjects(final Map<String, String> mapping, final List<String> projects) {
        for (String fileName : projects) {
            if (fileName.endsWith(MAVEN_POM)) {
                addMapping(mapping, fileName, MAVEN_POM, parsePom(fileName));
            }
        }
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
        try (InputStream file = getFactory().open(pom)) {
            var digester = new SecureDigester(ModuleDetector.class);
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
}
