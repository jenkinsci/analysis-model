package edu.hm.hafner.analysis;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.InvalidPathException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.xml.sax.SAXException;

import edu.hm.hafner.analysis.ModuleDetector.FileSystem;

/**
 * Detects module names by parsing the name of a source file, the ANT build.xml.
 */
public class AntModuleDetector extends AbstractModuleDetector {
    static final String ANT_PROJECT = "build.xml";

    AntModuleDetector(final FileSystem fileSystem) {
        super(fileSystem);
    }

    @Override
    String getPattern() {
        return ALL_DIRECTORIES + ANT_PROJECT;
    }

    @Override
    void collectProjects(final Map<String, String> mapping, final ArrayList<String> projects) {
        for (String fileName : projects) {
            if (fileName.endsWith(ANT_PROJECT)) {
                addMapping(mapping, fileName, ANT_PROJECT, parseBuildXml(fileName));
            }
        }
    }

    /**
     * Returns the project name stored in the Ant build.xml.
     *
     * @param buildXml
     *         Ant build.xml file name
     *
     * @return the project name or an empty string if the name could not be resolved
     */
    private String parseBuildXml(final String buildXml) {
        try (InputStream file = getFactory().open(buildXml)) {
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
}
