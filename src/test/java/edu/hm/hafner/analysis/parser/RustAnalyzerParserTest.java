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
import edu.hm.hafner.analysis.registry.ParserRegistry;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link RustAnalyzerParser}.
 *
 * @author Akash Manna
 */
class RustAnalyzerParserTest extends AbstractParserTest {
    RustAnalyzerParserTest() {
        super("rust-analyzer.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(2).hasDuplicatesSize(0);

        softly.assertThat(report.get(0))
                .hasFileName("packages/secspc/src/main.rs")
                .hasMessage("unused import: `secsp_analysis::input::FileId`")
                .hasCategory("unused_imports")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(14)
                .hasLineEnd(14)
                .hasColumnStart(5)
                .hasColumnEnd(34);

        softly.assertThat(report.get(1))
                .hasFileName("packages/secspc/src/main.rs")
                .hasMessage("redundant closure found")
                .hasCategory("clippy::redundant_closure")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(68)
                .hasLineEnd(68)
                .hasColumnStart(14)
                .hasColumnEnd(34);
    }

    @Override
    protected IssueParser createParser() {
        return new RustAnalyzerParser();
    }

    @Test
    void accepts() {
        assertThat(new RustAnalyzerParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("rust-analyzer.json")))).isTrue();
        assertThat(new RustAnalyzerParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("foo.txt")))).isFalse();
    }

    @Test
    void brokenInput() {
        assertThatThrownBy(() -> parse("eclipse.txt"))
                .isInstanceOf(ParsingException.class);
    }

    @Test
    void shouldProvideDescriptorMetadata() {
        var descriptor = new ParserRegistry().get("rust-analyzer");

        assertThat(descriptor.getPattern()).isEqualTo("**/rust-analyzer.json");
        assertThat(descriptor.getHelp()).contains("rust-analyzer diagnostics");
        assertThat(descriptor.getUrl()).isEqualTo("https://github.com/rust-lang/rust-analyzer");
        assertThat(descriptor.hasHelp()).isTrue();
        assertThat(descriptor.hasUrl()).isTrue();
    }
}