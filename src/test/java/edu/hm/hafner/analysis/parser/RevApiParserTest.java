package edu.hm.hafner.analysis.parser;

import java.io.Serializable;

import edu.hm.hafner.analysis.RevApiInfoExtension;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;

import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report;


class RevApiParserTest extends AbstractParserTest {

    RevApiParserTest() {
        super("revapi_report.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(6);
        softly.assertThat(report.get(0))
                .hasSeverity(Severity.WARNING_HIGH)
                .hasType("class")
                .hasCategory("java.class.removed")
                .hasPackageName("edu.hm.hafner.analysis.parser")
                .hasAdditionalProperties(report.get(0).getAdditionalProperties())
                .hasFileName("AquaScannerParser");
        Serializable properties = report.get(0).getAdditionalProperties();
        softly.assertThat(properties instanceof RevApiInfoExtension);
        RevApiInfoExtension revApiInfo = (RevApiInfoExtension) properties;
        softly.assertThat(revApiInfo)
                .hasNewFile("null")
                .hasOldFile("class edu.hm.hafner.analysis.parser.AquaScannerParser")
                .hasIssueName("Incompatible with the current version: class removed");

        softly.assertThat(report.get(1))
                .hasSeverity(Severity.WARNING_HIGH)
                .hasType("class")
                .hasCategory("java.class.added")
                .hasPackageName("edu.hm.hafner.analysis.parser")
                .hasAdditionalProperties(report.get(1).getAdditionalProperties())
                .hasFileName("RevApiParser");
        properties = report.get(1).getAdditionalProperties();
        softly.assertThat(properties instanceof RevApiInfoExtension);
        revApiInfo = (RevApiInfoExtension) properties;
        softly.assertThat(revApiInfo)
                .hasNewFile("class edu.hm.hafner.analysis.parser.RevApiParser")
                .hasOldFile("null")
                .hasIssueName("Incompatible with the current version: class added");

        softly.assertThat(report.get(2))
                .hasSeverity(Severity.WARNING_HIGH)
                .hasType("class")
                .hasCategory("java.class.added")
                .hasPackageName("edu.hm.hafner.analysis.registry")
                .hasAdditionalProperties(report.get(2).getAdditionalProperties())
                .hasFileName("RevApiDescriptor");
        properties = report.get(2).getAdditionalProperties();
        softly.assertThat(properties instanceof RevApiInfoExtension);
        revApiInfo = (RevApiInfoExtension) properties;
        softly.assertThat(revApiInfo)
                .hasNewFile("class edu.hm.hafner.analysis.registry.RevApiDescriptor")
                .hasOldFile("null")
                .hasIssueName("Incompatible with the current version: class added");

        softly.assertThat(report.get(3))
                .hasSeverity(Severity.WARNING_HIGH)
                .hasType("size")
                .hasCategory("java.method.removed")
                .hasPackageName("shaded.org.objectweb.asm")
                .hasAdditionalProperties(report.get(3).getAdditionalProperties())
                .hasFileName("ByteVector");
        properties = report.get(3).getAdditionalProperties();
        softly.assertThat(properties instanceof RevApiInfoExtension);
        revApiInfo = (RevApiInfoExtension) properties;
        softly.assertThat(revApiInfo)
                .hasNewFile("null")
                .hasOldFile("method int shaded.org.objectweb.asm.ByteVector::size()")
                .hasIssueName("Incompatible with the current version: method removed");

        softly.assertThat(report.get(4))
                .hasSeverity(Severity.WARNING_LOW)
                .hasType("hasFlags")
                .hasCategory("java.method.removed")
                .hasPackageName("shaded.org.objectweb.asm")
                .hasAdditionalProperties(report.get(4).getAdditionalProperties())
                .hasFileName("ClassWriter");
        properties = report.get(4).getAdditionalProperties();
        softly.assertThat(properties instanceof RevApiInfoExtension);
        revApiInfo = (RevApiInfoExtension) properties;
        softly.assertThat(revApiInfo)
                .hasNewFile("null")
                .hasOldFile("method boolean shaded.org.objectweb.asm.ClassWriter::hasFlags(int)")
                .hasIssueName("Incompatible with the current version: method removed");

        softly.assertThat(report.get(5))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasType("V19")
                .hasCategory("java.field.removedWithConstant")
                .hasPackageName("shaded.org.objectweb.asm")
                .hasAdditionalProperties(report.get(5).getAdditionalProperties())
                .hasFileName("Opcodes");
        properties = report.get(5).getAdditionalProperties();
        softly.assertThat(properties instanceof RevApiInfoExtension);
        revApiInfo = (RevApiInfoExtension) properties;
        softly.assertThat(revApiInfo)
                .hasNewFile("null")
                .hasOldFile("field shaded.org.objectweb.asm.Opcodes.V19")
                .hasIssueName("Incompatible with the current version: field with constant removed");
    }

    @Override
    protected IssueParser createParser() {
        return new RevApiParser();
    }
}
