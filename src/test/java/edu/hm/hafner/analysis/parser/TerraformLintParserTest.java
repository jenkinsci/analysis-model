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
 * Tests the class {@link TerraformLintParser}.
 *
 * @author Akash Manna
 */
class TerraformLintParserTest extends AbstractParserTest {
    TerraformLintParserTest() {
        super("tflint.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(4);

        softly.assertThat(report.get(0))
                .hasFileName("main.tf")
                .hasLineStart(6)
                .hasColumnStart(5)
                .hasColumnEnd(25)
                .hasType("terraform_deprecated_index")
                .hasMessage("Terraform 0.10.x and earlier use index() function")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(report.get(1))
                .hasFileName("modules/aws/main.tf")
                .hasLineStart(10)
                .hasColumnStart(10)
                .hasColumnEnd(35)
                .hasType("aws_instance_invalid_ami")
                .hasMessage("Search base image 'ami-invalid' does not exist")
                .hasSeverity(Severity.ERROR);

        softly.assertThat(report.get(2))
                .hasFileName("variables.tf")
                .hasLineStart(3)
                .hasColumnStart(1)
                .hasColumnEnd(20)
                .hasType("terraform_naming_convention")
                .hasMessage("Resource name should match pattern '^[a-z_]*$'")
                .hasSeverity(Severity.WARNING_LOW);

        softly.assertThat(report.get(3))
                .hasFileName("security.tf")
                .hasLineStart(15)
                .hasColumnStart(12)
                .hasColumnEnd(20)
                .hasType("aws_security_group_ingress_invalid_port")
                .hasMessage("Port value must be between 0 and 65535")
                .hasSeverity(Severity.ERROR);
    }

    @Override
    protected IssueParser createParser() {
        return new TerraformLintParser();
    }

    @Test
    void accepts() {
        assertThat(new TerraformLintParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("tflint.json")))).isTrue();
        assertThat(new TerraformLintParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("foo.txt")))).isFalse();
    }

    @Test
    void brokenInput() {
        assertThatThrownBy(() -> parse("eclipse.txt"))
                .isInstanceOf(ParsingException.class);
    }

    @Test
    void emptyInput() throws ParsingException {
        var report = parse("issues-no-issues.json");

        assertThat(report).isEmpty();
    }
}
