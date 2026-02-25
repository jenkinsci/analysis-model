package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link NixParser}.
 * 
 * @author Akash Manna
 */
class NixParserTest extends AbstractParserTest {
    NixParserTest() {
        super("nix.txt");
    }

    @Override
    protected NixParser createParser() {
        return new NixParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(4);

        softly.assertThat(report.get(0))
                .hasSeverity(Severity.ERROR)
                .hasLineStart(18)
                .hasLineEnd(18)
                .hasColumnStart(23)
                .hasMessage("undefined variable 'stdnev'")
                .hasFileName("/home/user/project/flake.nix");

        softly.assertThat(report.get(1))
                .hasSeverity(Severity.ERROR)
                .hasLineStart(25)
                .hasLineEnd(25)
                .hasColumnStart(1)
                .hasMessage("syntax error, unexpected '}', expecting ';'")
                .hasFileName("/nix/store/hash-source/default.nix");

        softly.assertThat(report.get(2))
                .hasSeverity(Severity.ERROR)
                .hasLineStart(10)
                .hasLineEnd(10)
                .hasColumnStart(5)
                .hasMessage("attribute 'packages.x86_64-linux.default' missing")
                .hasFileName("/home/build/flake.nix");

        softly.assertThat(report.get(3))
                .hasSeverity(Severity.ERROR)
                .hasLineStart(15)
                .hasLineEnd(15)
                .hasColumnStart(3)
                .hasMessage("builder for '/nix/store/abc123-mypackage-1.0.0.drv' failed with exit code 2")
                .hasFileName("/nix/store/xyz789-source/package.nix");
    }
}
