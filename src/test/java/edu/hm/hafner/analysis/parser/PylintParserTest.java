package edu.hm.hafner.analysis.parser;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link PyLintParser}.
 */
class PylintParserTest extends AbstractParserTest {
    private static final String ISSUES_FILE = "pylint_parseable.txt";

    /**
     * Creates a new instance of {@link PylintParserTest}.
     */
    PylintParserTest() {
        super(ISSUES_FILE);
    }

    @SuppressWarnings("methodlength")
    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(22);

        Iterator<Issue> iterator = report.iterator();
        softly.assertThat(iterator.next())
                .hasLineStart(1)
                .hasMessage("No module named src/test/resources/non_existant.py")
                .hasFileName("src/test/resources/non_existant.py")
                .hasType("fatal")
                .hasCategory("Fatal")
                .hasSeverity(Severity.ERROR)
                .hasModuleName("-")
                .hasPackageName("-");

        softly.assertThat(iterator.next())
                .hasLineStart(16)
                .hasMessage("Exactly one space required around assignment")
                .hasFileName("src/test/resources/python_src/pypackage/pymodule.py")
                .hasType("bad-whitespace")
                .hasCategory("Convention")
                .hasSeverity(Severity.WARNING_LOW)
                .hasModuleName("-")
                .hasPackageName("-");

        softly.assertThat(iterator.next())
                .hasLineStart(24)
                .hasMessage("Unnecessary parens after 'print' keyword")
                .hasFileName("src/test/resources/python_src/pypackage/pymodule.py")
                .hasType("superfluous-parens")
                .hasCategory("Convention")
                .hasSeverity(Severity.WARNING_LOW)
                .hasModuleName("-")
                .hasPackageName("-");

        softly.assertThat(iterator.next())
                .hasLineStart(1)
                .hasMessage("Missing module docstring")
                .hasFileName("src/test/resources/python_src/pypackage/pymodule.py")
                .hasType("missing-docstring")
                .hasCategory("Convention")
                .hasSeverity(Severity.WARNING_LOW)
                .hasModuleName("-")
                .hasPackageName("-");

        softly.assertThat(iterator.next())
                .hasLineStart(5)
                .hasMessage("Constant name \"shift\" doesn't conform to UPPER_CASE naming style")
                .hasFileName("src/test/resources/python_src/pypackage/pymodule.py")
                .hasType("invalid-name")
                .hasCategory("Convention")
                .hasSeverity(Severity.WARNING_LOW)
                .hasModuleName("-")
                .hasPackageName("-");

        softly.assertThat(iterator.next())
                .hasLineStart(6)
                .hasMessage("Constant name \"choice\" doesn't conform to UPPER_CASE naming style")
                .hasFileName("src/test/resources/python_src/pypackage/pymodule.py")
                .hasType("invalid-name")
                .hasCategory("Convention")
                .hasSeverity(Severity.WARNING_LOW)
                .hasModuleName("-")
                .hasPackageName("-");

        softly.assertThat(iterator.next())
                .hasLineStart(7)
                .hasMessage("Constant name \"word\" doesn't conform to UPPER_CASE naming style")
                .hasFileName("src/test/resources/python_src/pypackage/pymodule.py")
                .hasType("invalid-name")
                .hasCategory("Convention")
                .hasSeverity(Severity.WARNING_LOW)
                .hasModuleName("-")
                .hasPackageName("-");

        softly.assertThat(iterator.next())
                .hasLineStart(8)
                .hasMessage("Constant name \"letters\" doesn't conform to UPPER_CASE naming style")
                .hasFileName("src/test/resources/python_src/pypackage/pymodule.py")
                .hasType("invalid-name")
                .hasCategory("Convention")
                .hasSeverity(Severity.WARNING_LOW)
                .hasModuleName("-")
                .hasPackageName("-");

        softly.assertThat(iterator.next())
                .hasLineStart(9)
                .hasMessage("Constant name \"encoded\" doesn't conform to UPPER_CASE naming style")
                .hasFileName("src/test/resources/python_src/pypackage/pymodule.py")
                .hasType("invalid-name")
                .hasCategory("Convention")
                .hasSeverity(Severity.WARNING_LOW)
                .hasModuleName("-")
                .hasPackageName("-");

        softly.assertThat(iterator.next())
                .hasLineStart(26)
                .hasMessage("Class name \"test\" doesn't conform to PascalCase naming style")
                .hasFileName("src/test/resources/python_src/pypackage/pymodule.py")
                .hasType("invalid-name")
                .hasCategory("Convention")
                .hasSeverity(Severity.WARNING_LOW)
                .hasModuleName("-")
                .hasPackageName("-");

        softly.assertThat(iterator.next())
                .hasLineStart(26)
                .hasMessage("Missing class docstring")
                .hasFileName("src/test/resources/python_src/pypackage/pymodule.py")
                .hasType("missing-docstring")
                .hasCategory("Convention")
                .hasSeverity(Severity.WARNING_LOW)
                .hasModuleName("-")
                .hasPackageName("-");

        softly.assertThat(iterator.next())
                .hasLineStart(27)
                .hasMessage("Missing method docstring")
                .hasFileName("src/test/resources/python_src/pypackage/pymodule.py")
                .hasType("missing-docstring")
                .hasCategory("Convention")
                .hasSeverity(Severity.WARNING_LOW)
                .hasModuleName("-")
                .hasPackageName("-");

        softly.assertThat(iterator.next())
                .hasLineStart(27)
                .hasMessage("Method should have \"self\" as first argument")
                .hasFileName("src/test/resources/python_src/pypackage/pymodule.py")
                .hasType("no-self-argument")
                .hasCategory("Error")
                .hasSeverity(Severity.WARNING_HIGH)
                .hasModuleName("-")
                .hasPackageName("-");

        softly.assertThat(iterator.next())
                .hasLineStart(30)
                .hasMessage("Dangerous default value [] as argument")
                .hasFileName("src/test/resources/python_src/pypackage/pymodule.py")
                .hasType("dangerous-default-value")
                .hasCategory("Warning")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasModuleName("-")
                .hasPackageName("-");

        softly.assertThat(iterator.next())
                .hasLineStart(30)
                .hasMessage("Missing method docstring")
                .hasFileName("src/test/resources/python_src/pypackage/pymodule.py")
                .hasType("missing-docstring")
                .hasCategory("Convention")
                .hasSeverity(Severity.WARNING_LOW)
                .hasModuleName("-")
                .hasPackageName("-");

        softly.assertThat(iterator.next())
                .hasLineStart(30)
                .hasMessage("Method could be a function")
                .hasFileName("src/test/resources/python_src/pypackage/pymodule.py")
                .hasType("no-self-use")
                .hasCategory("Refactor")
                .hasSeverity(Severity.WARNING_LOW)
                .hasModuleName("-")
                .hasPackageName("-");

        softly.assertThat(iterator.next())
                .hasLineStart(33)
                .hasMessage("Unable to import 'deadbeef'")
                .hasFileName("src/test/resources/python_src/pypackage/pymodule.py")
                .hasType("import-error")
                .hasCategory("Error")
                .hasSeverity(Severity.WARNING_HIGH)
                .hasModuleName("-")
                .hasPackageName("-");

        softly.assertThat(iterator.next())
                .hasLineStart(33)
                .hasMessage("Import \"import deadbeef\" should be placed at the top of the module")
                .hasFileName("src/test/resources/python_src/pypackage/pymodule.py")
                .hasType("wrong-import-position")
                .hasCategory("Convention")
                .hasSeverity(Severity.WARNING_LOW)
                .hasModuleName("-")
                .hasPackageName("-");

        softly.assertThat(iterator.next())
                .hasLineStart(33)
                .hasMessage("Unused import deadbeef")
                .hasFileName("src/test/resources/python_src/pypackage/pymodule.py")
                .hasType("unused-import")
                .hasCategory("Warning")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasModuleName("-")
                .hasPackageName("-");

        softly.assertThat(iterator.next())
                .hasLineStart(32)
                .hasMessage("Module 'PySide2.QtWidgets' has no 'QApplication' member, but source is unavailable.")
                .hasFileName("src/test/resources/python_src/pypackage/pymodule.py")
                .hasType("c-extension-no-member")
                .hasCategory("Informational")
                .hasSeverity(Severity.WARNING_LOW)
                .hasModuleName("-")
                .hasPackageName("-");

        softly.assertThat(iterator.next())
                .hasLineStart(34)
                .hasMessage("Unused import deadbeef")
                .hasFileName("src/test/resources/python_src/pypackage/pymodule.py")
                .hasType("unused-import")
                .hasCategory("pylint-unknown-category")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasModuleName("-")
                .hasPackageName("-");

        softly.assertThat(iterator.next())
                .hasLineStart(35)
                .hasMessage("This is a category/type that dooesn't exists in Pylint")
                .hasFileName("src/test/resources/python_src/pypackage/pymodule.py")
                .hasType("new-unknown-issue")
                .hasCategory("pylint-unknown-category")
                .hasSeverity(Severity.WARNING_LOW)
                .hasModuleName("-")
                .hasPackageName("-");
    }

    @Test
    void shouldParseAbsolutePathsFromWindows() {
        var report = parse("pylint-absolute-paths.txt");

        assertThat(report).hasSize(2);
        assertThat(report.get(0))
                .hasLineStart(1)
                .hasMessage("No module named src/test/resources/non_existant.py")
                .hasFileName("E:/JenkinsMIDEBLD/workspace/r-fix_pylint_warnings_ng_CYC-276/cyclops/archive/archive.py")
                .hasCategory("Fatal")
                .hasType("fatal")
                .hasSeverity(Severity.ERROR);
        assertThat(report.get(1))
                .hasLineStart(28)
                .hasMessage("Invalid name \"seasonCount\" (should match [a-z_][a-z0-9_]{2,30}$)")
                .hasFileName("E:/JenkinsMIDEBLD/workspace/r-fix_pylint_warnings_ng_CYC-276/cyclops/archive/archive.py")
                .hasCategory("Convention")
                .hasType("C0103")
                .hasSeverity(Severity.WARNING_LOW);
    }

    @Test
    void shouldParseReportWithoutSymbol() {
        var report = parse("pyLint.txt");

        assertThat(report).hasSize(9);

        Iterator<Issue> iterator = report.iterator();
        try (var softly = new SoftAssertions()) {
            softly.assertThat(iterator.next())
                    .hasLineStart(3)
                    .hasLineEnd(3)
                    .hasMessage("Line too long (85/80)")
                    .hasFileName("trunk/src/python/cachedhttp.py")
                    .hasType("C")
                    .hasCategory("Convention")
                    .hasSeverity(Severity.WARNING_LOW)
                    .hasModuleName("-")
                    .hasPackageName("-");

            softly.assertThat(iterator.next())
                    .hasLineStart(28)
                    .hasLineEnd(28)
                    .hasMessage("Invalid name \"seasonCount\" (should match [a-z_][a-z0-9_]{2,30}$)")
                    .hasFileName("trunk/src/python/tv.py")
                    .hasType("C0103")
                    .hasCategory("Convention")
                    .hasSeverity(Severity.WARNING_LOW)
                    .hasModuleName("-")
                    .hasPackageName("-");

            softly.assertThat(iterator.next())
                    .hasLineStart(35)
                    .hasLineEnd(35)
                    .hasMessage("Missing docstring")
                    .hasFileName("trunk/src/python/tv.py")
                    .hasType("C0111")
                    .hasCategory("Convention")
                    .hasSeverity(Severity.WARNING_LOW)
                    .hasModuleName("-")
                    .hasPackageName("-");

            softly.assertThat(iterator.next())
                    .hasLineStart(39)
                    .hasLineEnd(39)
                    .hasMessage("Method should have \"self\" as first argument")
                    .hasFileName("trunk/src/python/tv.py")
                    .hasType("E0213")
                    .hasCategory("Error")
                    .hasSeverity(Severity.WARNING_HIGH)
                    .hasModuleName("-")
                    .hasPackageName("-");

            softly.assertThat(iterator.next())
                    .hasLineStart(5)
                    .hasLineEnd(5)
                    .hasMessage("Unable to import 'deadbeef'")
                    .hasFileName("trunk/src/python/tv.py")
                    .hasType("F0401")
                    .hasCategory("Fatal")
                    .hasSeverity(Severity.ERROR)
                    .hasModuleName("-")
                    .hasPackageName("-");

            softly.assertThat(iterator.next())
                    .hasLineStart(39)
                    .hasLineEnd(39)
                    .hasMessage("Dangerous default value \"[]\" as argument")
                    .hasFileName("trunk/src/python/tv.py")
                    .hasType("W0102")
                    .hasCategory("Warning")
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasModuleName("-")
                    .hasPackageName("-");

            softly.assertThat(iterator.next())
                    .hasLineStart(1)
                    .hasLineEnd(1)
                    .hasMessage("Unused import os (unused-import)")
                    .hasFileName("trunk/src/python_package/module_name.py")
                    .hasType("W0611")
                    .hasCategory("Warning")
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasModuleName("python_package.module_name")
                    .hasPackageName("python_package");

            softly.assertThat(iterator.next())
                    .hasLineStart(1)
                    .hasLineEnd(1)
                    .hasMessage("Unused import os (unused-import)")
                    .hasFileName("trunk/src/module_name_no_package.py")
                    .hasType("W0611")
                    .hasCategory("Warning")
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasModuleName("module_name_no_package")
                    .hasPackageName("-");

            softly.assertThat(iterator.next())
                    .hasLineStart(32)
                    .hasLineEnd(32)
                    .hasMessage("Module 'PySide2.QtWidgets' has no 'QApplication' member, but source is unavailable.")
                    .hasFileName("trunk/src/python/tv.py")
                    .hasType("I1101")
                    .hasCategory("Informational")
                    .hasSeverity(Severity.WARNING_LOW)
                    .hasModuleName("-")
                    .hasPackageName("-");
        }
    }

    @Override
    protected PyLintParser createParser() {
        return new PyLintParser();
    }
}
