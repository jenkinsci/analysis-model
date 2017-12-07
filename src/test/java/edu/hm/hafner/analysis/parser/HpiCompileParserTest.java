package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;

/**
 * Tests the class {@link AntJavacParser} for output log of a HPI compile.
 */
public class HpiCompileParserTest extends ParserTester {
    private static final String WARNING_TYPE = new AntJavacParser().getId();

    /**
     * Parses a file with two deprecation warnings.
     *
     * @throws IOException if the file could not be read
     */
    @Test
    public void parseDeprecation() throws IOException {
        Issues warnings = new AntJavacParser().parse(openFile());

        assertEquals(2, warnings.size());

        Iterator<Issue> iterator = warnings.iterator();
        Issue annotation = iterator.next();
        checkWarning(annotation,
                46,
                "newInstance(org.kohsuke.stapler.StaplerRequest) in hudson.model.Descriptor has been deprecated",
                "C:/Build/Results/jobs/ADT-Base/workspace/tasks/src/main/java/hudson/plugins/tasks/TasksDescriptor.java",
                WARNING_TYPE, "Deprecation", Priority.NORMAL);
        annotation = iterator.next();
        checkWarning(annotation,
                34,
                "newInstance(org.kohsuke.stapler.StaplerRequest) in hudson.model.Descriptor has been deprecated",
                "C:/Build/Results/jobs/ADT-Base/workspace/tasks/src/main/java/hudson/plugins/tasks/TasksReporterDescriptor.java",
                WARNING_TYPE, "Deprecation", Priority.NORMAL);
    }

    private void assertEquals(final int i, final int size) {
    }

    @Override
    protected String getWarningsFile() {
        return "hpi.txt";
    }
}

