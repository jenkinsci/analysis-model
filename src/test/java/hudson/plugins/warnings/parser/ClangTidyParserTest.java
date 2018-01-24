package hudson.plugins.warnings.parser;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;

import static org.junit.Assert.*;

import hudson.plugins.analysis.util.model.FileAnnotation;
import hudson.plugins.analysis.util.model.Priority;

/**
 * Tests the class {@link ClangTidyParser}.
 *
 * @author Ryan Schaefer
 */
public class ClangTidyParserTest extends ParserTester {
    private static final String TYPE = new ClangTidyParser().getGroup();

    /**
     * Verifies that all messages are correctly parsed.
     *
     * @throws IOException
     *             in case of an error
     */
    @Test
    public void testWarningsParser() throws IOException {
        Collection<FileAnnotation> warnings = new ClangTidyParser().parse(openFile());

        assertEquals(WRONG_NUMBER_OF_WARNINGS_DETECTED, 6, warnings.size());

        Iterator<FileAnnotation> iterator = warnings.iterator();

        FileAnnotation annotation = iterator.next();
        checkWarning(annotation,
                1,
                8,
                "implicit conversion changes signedness: 'int' to 'uint32_t' (aka 'unsigned int')",
                "src/../src/main.cpp",
                TYPE,
                "clang-diagnostic-sign-conversion",
                Priority.NORMAL);

        annotation = iterator.next();
        checkWarning(annotation,
                10,
                20,
                "implicit conversion changes signedness: 'int' to 'uint32_t' (aka 'unsigned int')",
                "/src/main.cpp",
                TYPE,
                "clang-diagnostic-sign-conversion",
                Priority.NORMAL);

        annotation = iterator.next();
        checkWarning(annotation,
                83,
                20,
                "implicit conversion changes signedness: 'int' to 'uint32_t' (aka 'unsigned int')",
                "/path/to/project/src/test2.cpp",
                TYPE,
                "clang-diagnostic-sign-conversion",
                Priority.NORMAL);

        annotation = iterator.next();
        checkWarning(annotation,
                25,
                15,
                "suggest braces around initialization of subobject",
                "/path/to/project/src/test2.cpp",
                TYPE,
                "clang-diagnostic-missing-braces",
                Priority.NORMAL);

        annotation = iterator.next();
        checkWarning(annotation,
                29,
                15,
                "suggest braces around initialization of subobject",
                "/path/to/project/src/test2.cpp",
                TYPE,
                "clang-diagnostic-missing-braces",
                Priority.NORMAL);

        annotation = iterator.next();
        checkWarning(annotation,
                4,
                10,
                "'dbus/dbus.h' file not found",
                "/path/to/project/src/error_test.cpp",
                TYPE,
                "clang-diagnostic-error",
                Priority.HIGH);
    }

    @Override
    protected String getWarningsFile() {
        return "ClangTidy.txt";
    }
}
