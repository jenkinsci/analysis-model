package edu.hm.hafner.analysis.parser;

import java.util.HashMap;
import java.util.Map;

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
        Map<String, String> want = new HashMap<>();
        want.put("OTHER", "BREAKING");
        want.put("SOURCE", "BREAKING");
        want.put("BINARY", "BREAKING");

        softly.assertThat(report).hasSize(6);
        softly.assertThat(report.get(0))
                .hasSeverity(Severity.WARNING_HIGH)
                .hasCategory("class")
                .hasPackageName("edu.hm.hafner.analysis.parser")
                .hasFileName("AquaScannerParser");
        softly.assertThat(report.get(0).getAdditionalProperties()).isInstanceOfSatisfying(RevApiInfoExtension.class, i -> softly.assertThat(i)
                .hasNewFile("null")
                .hasOldFile("class edu.hm.hafner.analysis.parser.AquaScannerParser")
                .hasIssueName("java.class.removed")
                .hasSeverities(want));

        want.replace("SOURCE", "BREAKING", "NON_BREAKING");
        want.replace("BINARY", "BREAKING", "NON_BREAKING");
        softly.assertThat(report.get(1))
                .hasSeverity(Severity.WARNING_HIGH)
                .hasCategory("class")
                .hasPackageName("edu.hm.hafner.analysis.parser")
                .hasFileName("RevApiParser");
        softly.assertThat(report.get(1).getAdditionalProperties()).isInstanceOfSatisfying(RevApiInfoExtension.class, i -> {
            softly.assertThat(i)
                    .hasNewFile("class edu.hm.hafner.analysis.parser.RevApiParser")
                    .hasOldFile("null")
                    .hasIssueName("java.class.added")
                    .hasSeverities(want);
            Map<String, String> sut = i.getSeverities();
            softly.assertThat(sut.equals(want)).isTrue();
        });

        softly.assertThat(report.get(2))
                .hasSeverity(Severity.WARNING_HIGH)
                .hasCategory("class")
                .hasPackageName("edu.hm.hafner.analysis.registry")
                .hasFileName("RevApiDescriptor");
        softly.assertThat(report.get(2).getAdditionalProperties()).isInstanceOfSatisfying(RevApiInfoExtension.class, i -> softly.assertThat(i)
                .hasNewFile("class edu.hm.hafner.analysis.registry.RevApiDescriptor")
                .hasOldFile("null")
                .hasIssueName("java.class.added")
                .hasSeverities(want));

        want.replace("SOURCE", "NON_BREAKING", "BREAKING");
        want.replace("BINARY", "NON_BREAKING", "BREAKING");
        softly.assertThat(report.get(3))
                .hasSeverity(Severity.WARNING_HIGH)
                .hasCategory("size")
                .hasPackageName("shaded.org.objectweb.asm")
                .hasFileName("ByteVector");
        softly.assertThat(report.get(3).getAdditionalProperties()).isInstanceOfSatisfying(RevApiInfoExtension.class, i -> softly.assertThat(i)
                .hasNewFile("null")
                .hasOldFile("method int shaded.org.objectweb.asm.ByteVector::size()")
                .hasIssueName("java.method.removed")
                .hasSeverities(want));

        want.replace("SOURCE", "BREAKING", "NON_BREAKING");
        want.replace("BINARY", "BREAKING", "NON_BREAKING");
        want.replace("OTHER", "BREAKING", "NON_BREAKING");
        softly.assertThat(report.get(4))
                .hasSeverity(Severity.WARNING_LOW)
                .hasCategory("hasFlags")
                .hasPackageName("shaded.org.objectweb.asm")
                .hasFileName("ClassWriter");
        softly.assertThat(report.get(4).getAdditionalProperties()).isInstanceOfSatisfying(RevApiInfoExtension.class, i -> softly.assertThat(i)
                .hasNewFile("null")
                .hasOldFile("method boolean shaded.org.objectweb.asm.ClassWriter::hasFlags(int)")
                .hasIssueName("java.method.removed")
                .hasSeverities(want));

        want.replace("SOURCE", "NON_BREAKING", "POTENTIALLY_BREAKING");
        want.replace("BINARY", "NON_BREAKING", "POTENTIALLY_BREAKING");
        want.replace("OTHER", "NON_BREAKING", "POTENTIALLY_BREAKING");
        want.put("SEMANTIC", "POTENTIALLY_BREAKING");
        softly.assertThat(report.get(5))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("V19")
                .hasPackageName("shaded.org.objectweb.asm")
                .hasFileName("Opcodes");
        softly.assertThat(report.get(5).getAdditionalProperties()).isInstanceOfSatisfying(RevApiInfoExtension.class, i -> softly.assertThat(i)
                .hasNewFile("null")
                .hasOldFile("field shaded.org.objectweb.asm.Opcodes.V19")
                .hasIssueName("java.field.removedWithConstant")
                .hasSeverities(want));
    }

    @Override
    protected IssueParser createParser() {
        return new RevApiParser();
    }
}
