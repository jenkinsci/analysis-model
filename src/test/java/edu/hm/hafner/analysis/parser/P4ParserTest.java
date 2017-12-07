package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static org.junit.Assert.*;

/**
 * Tests the class {@link P4Parser}.
 */
public class P4ParserTest extends ParserTester {
    /**
     * Parses a file with four Perforce warnings.
     *
     * @throws IOException if the file could not be read
     */
    @Test
    public void testWarningsParser() throws IOException {
        Issues warnings = new P4Parser().parse(openFile());

        assertEquals(4, warnings.size());

        Iterator<Issue> iterator = warnings.iterator();

        checkP4Warning(iterator.next(),
                "//eng/Tools/Hudson/instances/PCFARM08/.owner",
                "can't add existing file",
                Priority.NORMAL);

        checkP4Warning(iterator.next(),
                "//eng/Tools/Hudson/instances/PCFARM08/jobs/EASW-FIFA DailyTasks/config.xml",
                "warning: add of existing file",
                Priority.NORMAL);

        checkP4Warning(iterator.next(),
                "//eng/Tools/Hudson/instances/PCFARM08/jobs/BFBC2-DailyTasksEurope/config.xml",
                "can't add (already opened for edit)",
                Priority.LOW);

        checkP4Warning(iterator.next(),
                "//eng/Tools/Hudson/instances/PCFARM08/config.xml#8",
                "nothing changed",
                Priority.LOW);
    }


    /**
     * Verifies the annotation content.
     *
     * @param annotation the annotation to check
     * @param fileName   the expected filename
     * @param category   the expected category
     * @param priority   the expected priorit
     */
    private void checkP4Warning(final Issue annotation, final String fileName, final String category, final Priority priority) {
        checkWarning(annotation, 0, fileName, fileName, new P4Parser().getId(), category, priority);
    }


    @Override
    protected String getWarningsFile() {
        return "perforce.txt";
    }
}

