package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

class CodeCheckerParserTest extends AbstractParserTest {
    private static final String WARNING_TYPE = "Warning";

    CodeCheckerParserTest() {
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

    @Test
    void shouldParseWindowsPaths(){
        Report annotation = parse("CodeChecker_with_windows_paths.txt");
        assertThat(annotation).hasSize(5);
        assertThat(annotation.get(0))
                .hasLineStart(15)
                .hasColumnStart(22)
                .hasFileName("C:/path/to/project/cmake-build-debug/_deps/checked_cmd-src/Tests/ArgumentsTest.cpp")
                .hasMessage("'strncpy' is deprecated: This function or variable may be unsafe. Consider using strncpy_s instead. To disable deprecation, use _CRT_SECURE_NO_WARNINGS. See online help for details.")
                .hasType(WARNING_TYPE)
                .hasCategory("clang-diagnostic-deprecated-declarations")
                .hasSeverity(Severity.WARNING_NORMAL);

        assertThat(annotation.get(1))
                .hasLineStart(283)
                .hasColumnStart(22)
                .hasFileName("C:/Program Files (x86)/path/to/toolchain/include/abcddef")
                .hasMessage("'auto' return without trailing return type; deduced return types are a C++14 extension")
                .hasType(WARNING_TYPE)
                .hasCategory("clang-diagnostic-error")
                .hasSeverity(Severity.ERROR);

        assertThat(annotation.get(2))
                .hasLineStart(17)
                .hasColumnStart(8)
                .hasFileName("C:/path/to/project/csv2xlslib.Test/parsecmdTest.cpp")
                .hasMessage("class 'TheFixture' defines a default destructor but does not define a copy constructor, a copy assignment operator, a move constructor or a move assignment operator")
                .hasType(WARNING_TYPE)
                .hasCategory("cppcoreguidelines-special-member-functions")
                .hasSeverity(Severity.WARNING_LOW);

        assertThat(annotation.get(3))
                .hasLineStart(49)
                .hasColumnStart(8)
                .hasFileName("C:/path/to/project/csv2xlslib.Test/parseCsvStreamTest.cpp")
                .hasMessage("class 'Given_an_input_file_with_headline' defines a default destructor but does not define a copy constructor, a copy assignment operator, a move constructor or a move assignment operator")
                .hasType(WARNING_TYPE)
                .hasCategory("cppcoreguidelines-special-member-functions")
                .hasSeverity(Severity.WARNING_LOW);

        assertThat(annotation.get(4))
                .hasLineStart(924)
                .hasColumnStart(49)
                .hasFileName("C:/path/to/project/extern/lib/formula_expr.cpp")
                .hasMessage("suspicious usage of 'sizeof(A*)'; pointer to aggregate")
                .hasType(WARNING_TYPE)
                .hasCategory("bugprone-sizeof-expression")
                .hasSeverity(Severity.WARNING_HIGH);

    }
}
