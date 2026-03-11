package edu.hm.hafner.analysis.parser;

import java.nio.file.FileSystems;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.FileReaderFactory;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link RuffParser}.
 *
 * @author Akash Manna
 */
class RuffParserTest extends AbstractParserTest {
    RuffParserTest() {
        super("ruff.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(6);
        
        // Test first issue - unused import (should be fixable)
        softly.assertThat(report.get(0))
                .hasLineStart(1)
                .hasColumnStart(8)
                .hasLineEnd(1)
                .hasColumnEnd(10)
                .hasType("F401")
                .hasCategory("pyflakes")
                .hasMessage("`os` imported but unused [fixable]")
                .hasFileName("example.py")
                .hasSeverity(Severity.WARNING_HIGH);
        
        // Test second issue - line too long
        softly.assertThat(report.get(1))
                .hasLineStart(15)
                .hasColumnStart(89)
                .hasLineEnd(15)
                .hasColumnEnd(123)
                .hasType("E501")
                .hasCategory("pycodestyle")
                .hasMessage("Line too long (122 > 88 characters)")
                .hasFileName("example.py")
                .hasSeverity(Severity.ERROR);
        
        // Test third issue - missing docstring
        softly.assertThat(report.get(2))
                .hasLineStart(10)
                .hasColumnStart(1)
                .hasType("D103")
                .hasCategory("pydocstyle")
                .hasMessage("Missing docstring in public function")
                .hasFileName("src/module.py")
                .hasSeverity(Severity.WARNING_LOW);
        
        // Test fourth issue - trailing whitespace (fixable)
        softly.assertThat(report.get(3))
                .hasLineStart(25)
                .hasColumnStart(50)
                .hasType("W291")
                .hasCategory("pycodestyle")
                .hasMessage("Trailing whitespace [fixable]")
                .hasFileName("test.py")
                .hasSeverity(Severity.WARNING_NORMAL);
        
        // Test fifth issue - bugbear warning
        softly.assertThat(report.get(4))
                .hasLineStart(42)
                .hasColumnStart(20)
                .hasType("B008")
                .hasCategory("flake8-bugbear")
                .hasMessage("Do not perform function call `dict` in argument defaults; instead, use None")
                .hasFileName("utils.py")
                .hasSeverity(Severity.WARNING_HIGH);
        
        // Test sixth issue - import sorting (fixable)
        softly.assertThat(report.get(5))
                .hasLineStart(3)
                .hasColumnStart(1)
                .hasLineEnd(5)
                .hasColumnEnd(1)
                .hasType("I001")
                .hasCategory("isort")
                .hasMessage("Import block is un-sorted or un-formatted [fixable]")
                .hasFileName("main.py")
                .hasSeverity(Severity.WARNING_LOW);
    }

    @Override
    protected IssueParser createParser() {
        return new RuffParser();
    }

    @Test
    void accepts() {
        assertThat(new RuffParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("ruff.json")))).isTrue();
        assertThat(new RuffParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("foo.txt")))).isFalse();
    }

    @Test
    void brokenInput() {
        assertThatThrownBy(() -> parse("eclipse.txt"))
                .isInstanceOf(ParsingException.class);
    }
}
