package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

class CodeCheckerParserWithLinuxPathsTest extends AbstractParserTest {
    private static final String WARNING_TYPE = "Warning";

    CodeCheckerParserWithLinuxPathsTest() {
        super("CodeChecker_with_linux_paths.txt");
    }

    @Override
    protected CodeCheckerParser createParser() {
        return new CodeCheckerParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Report annotation, final SoftAssertions softly) {
        assertThat(annotation).hasSize(3);

        softly.assertThat(annotation.get(0))
                .hasLineStart(17)
                .hasColumnStart(8)
                .hasFileName("/path/to/projrct/csv2xlslib.Test/parsecmdTest.cpp")
                .hasMessage("class 'TheFixture' defines a default destructor but does not define a copy constructor, a copy assignment operator, a move constructor or a move assignment operator")
                .hasType(WARNING_TYPE)
                .hasCategory("cppcoreguidelines-special-member-functions")
                .hasSeverity(Severity.WARNING_LOW);

        softly.assertThat(annotation.get(1))
                .hasLineStart(425)
                .hasColumnStart(33)
                .hasFileName("/path/to/projrct/extern/lib/workbook.cpp")
                .hasMessage("Called C++ object pointer is null")
                .hasType(WARNING_TYPE)
                .hasCategory("core.CallAndMessage")
                .hasSeverity(Severity.WARNING_HIGH);

        softly.assertThat(annotation.get(2))
                .hasLineStart(212)
                .hasColumnStart(12)
                .hasFileName("/path/to/projrct/extern/lib/HPSF.cpp")
                .hasMessage("'signed char' to 'int' conversion; consider casting to 'unsigned char' first.")
                .hasType(WARNING_TYPE)
                .hasCategory("bugprone-signed-char-misuse")
                .hasSeverity(Severity.WARNING_NORMAL);

    }

}
