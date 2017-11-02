package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static org.junit.Assert.*;

/**
 * Tests the class {@link SphinxBuildParser}.
 */
public class SphinxBuildParserTest extends ParserTester {
    private static final String WARNING_TYPE = new SphinxBuildParser().getId();
    private static final String SPHINX_BUILD_ERROR = "ERROR";
    private static final String SPHINX_BUILD_WARNING = "WARNING";


    /**
     * Parses a file with six SphinxBuild warnings.
     *
     * @throws IOException if the file could not be read
     */
    @Test
    public void testWarningsParser() throws IOException {
        Issues warnings = new SphinxBuildParser().parse(openFile());

        assertEquals(6, warnings.size());

        Iterator<Issue> iterator = warnings.iterator();
        Issue annotation = iterator.next();
        checkWarning(annotation,
                12,
                "Unknown directive type \"blockdiag\".",
                "/src/be/doc/_sub/00_outline.rst",
                WARNING_TYPE, SPHINX_BUILD_ERROR, Priority.HIGH);

        annotation = iterator.next();
        checkWarning(annotation,
                10,
                "unknown document: ../builder/builder",
                "/src/infrastructure/jenkins-obs/jenkins-obs.rst",
                WARNING_TYPE, SPHINX_BUILD_WARNING, Priority.NORMAL);
        annotation = iterator.next();
        checkWarning(annotation,
                26,
                "undefined label: building_documentation_with_the_sphinx_container (if the link has no caption the label must precede a section header)",
                "/src/infrastructure/sphinx/plantuml.rst",
                WARNING_TYPE, SPHINX_BUILD_WARNING, Priority.NORMAL);
        annotation = iterator.next();
        checkWarning(annotation,
                0,
                "Could not obtain image size. :scale: option is ignored.",
                "/src/feblock/doc/3_3_6_2_WriteCommandCallbackIF.rst",
                WARNING_TYPE, SPHINX_BUILD_WARNING, Priority.NORMAL);
        annotation = iterator.next();
        checkWarning(annotation,
                0,
                "document isn't included in any toctree",
                "/src/infrastructure/self-test/self-test-vault/src/self-test/index.rst",
                WARNING_TYPE, SPHINX_BUILD_WARNING, Priority.NORMAL);
        annotation = iterator.next();
        checkWarning(annotation,
                21,
                "Unknown target name: \"threadid\".",
                "/src/be/doc/_sub/_classThread/04_Interface.rst",
                WARNING_TYPE, SPHINX_BUILD_ERROR, Priority.HIGH);

    }


    @Override
    protected String getWarningsFile() {
        return "sphinxbuild.txt";
    }
}

