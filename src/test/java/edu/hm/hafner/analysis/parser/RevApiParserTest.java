package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.RevApiInfoExtension;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;

import java.util.HashMap;
import java.util.Map;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

class RevApiParserTest extends StructuredFileParserTest {
    RevApiParserTest() {
        super("revapi_report.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        Map<String, String> expectedSeverities = new HashMap<>();
        expectedSeverities.put("OTHER", "BREAKING");
        expectedSeverities.put("SOURCE", "BREAKING");
        expectedSeverities.put("BINARY", "BREAKING");

        softly.assertThat(report).hasSize(6);
        softly.assertThat(report.get(0))
                .hasSeverity(Severity.WARNING_HIGH)
                .hasCategory("Class")
                .hasType("java.class.removed")
                .hasPackageName("edu.hm.hafner.analysis.parser")
                .hasFileName("edu/hm/hafner/analysis/parser/AquaScannerParser.java")
                .hasMessage("Class was removed. (breaks semantic versioning)");
        softly.assertThat(report.get(0).getDescription()).isEqualToIgnoringWhitespace("""
                <table>
                     <tr>
                         <td> Class: </td>
                         <td> edu.hm.hafner.analysis.parser.AquaScannerParser </td>
                     </tr>
                     <tr>
                         <td> Code: </td>
                         <td> java.class.removed </td>
                     </tr>
                     <tr>
                         <td> Name: </td>
                         <td> Incompatible with the current version: class removed </td>
                     </tr>
                     <tr>
                         <td> New Element: </td>
                         <td> - </td>
                     </tr>
                     <tr>
                         <td> Old Element: </td>
                         <td> class edu.hm.hafner.analysis.parser.AquaScannerParser </td>
                     </tr>
                     <tr>
                         <td> Justification: </td>
                         <td> - </td>
                     </tr>
                     <tr>
                         <td> Classification: </td>
                         <td>
                             <dl>
                                 <dt>OTHER</dt> <dd>BREAKING</dd>
                                 <dt>SOURCE</dt> <dd>BREAKING</dd>
                                 <dt>BINARY</dt> <dd>BREAKING</dd>
                             </dl>
                         </td>
                     </tr>
                 </table>
                """);
        softly.assertThat(report.get(0).getAdditionalProperties()).isInstanceOfSatisfying(RevApiInfoExtension.class,
                i -> softly.assertThat(i)
                        .hasNewFile("-")
                        .hasOldFile("class edu.hm.hafner.analysis.parser.AquaScannerParser")
                        .hasIssueName("java.class.removed")
                        .hasSeverities(expectedSeverities));

        expectedSeverities.replace("SOURCE", "BREAKING", "NON_BREAKING");
        expectedSeverities.replace("BINARY", "BREAKING", "NON_BREAKING");
        softly.assertThat(report.get(1))
                .hasSeverity(Severity.WARNING_HIGH)
                .hasCategory("Class")
                .hasPackageName("edu.hm.hafner.analysis.parser")
                .hasFileName("edu/hm/hafner/analysis/parser/RevApiParser.java");
        softly.assertThat(report.get(1).getAdditionalProperties())
                .isInstanceOfSatisfying(RevApiInfoExtension.class, i -> {
                    softly.assertThat(i)
                            .hasNewFile("class edu.hm.hafner.analysis.parser.RevApiParser")
                            .hasOldFile("-")
                            .hasIssueName("java.class.added")
                            .hasSeverities(expectedSeverities);
                    Map<String, String> sut = i.getSeverities();
                    softly.assertThat(sut.equals(expectedSeverities)).isTrue();
                });

        softly.assertThat(report.get(2))
                .hasSeverity(Severity.WARNING_HIGH)
                .hasCategory("Class")
                .hasPackageName("edu.hm.hafner.analysis.registry")
                .hasFileName("edu/hm/hafner/analysis/registry/RevApiDescriptor.java");
        softly.assertThat(report.get(2).getAdditionalProperties())
                .isInstanceOfSatisfying(RevApiInfoExtension.class, i -> softly.assertThat(i)
                        .hasNewFile("class edu.hm.hafner.analysis.registry.RevApiDescriptor")
                        .hasOldFile("-")
                        .hasIssueName("java.class.added")
                        .hasSeverities(expectedSeverities));

        expectedSeverities.replace("SOURCE", "NON_BREAKING", "BREAKING");
        expectedSeverities.replace("BINARY", "NON_BREAKING", "BREAKING");
        softly.assertThat(report.get(3))
                .hasSeverity(Severity.WARNING_HIGH)
                .hasCategory("Method")
                .hasPackageName("shaded.org.objectweb.asm")
                .hasFileName("shaded/org/objectweb/asm/ByteVector.java");
        softly.assertThat(report.get(3).getAdditionalProperties())
                .isInstanceOfSatisfying(RevApiInfoExtension.class, i -> softly.assertThat(i)
                        .hasNewFile("-")
                        .hasOldFile("method int shaded.org.objectweb.asm.ByteVector::size()")
                        .hasIssueName("java.method.removed")
                        .hasSeverities(expectedSeverities));

        expectedSeverities.replace("SOURCE", "BREAKING", "NON_BREAKING");
        expectedSeverities.replace("BINARY", "BREAKING", "NON_BREAKING");
        expectedSeverities.replace("OTHER", "BREAKING", "NON_BREAKING");
        softly.assertThat(report.get(4))
                .hasSeverity(Severity.WARNING_LOW)
                .hasCategory("Method")
                .hasPackageName("shaded.org.objectweb.asm")
                .hasFileName("shaded/org/objectweb/asm/ClassWriter.java");
        softly.assertThat(report.get(4).getAdditionalProperties())
                .isInstanceOfSatisfying(RevApiInfoExtension.class, i -> softly.assertThat(i)
                        .hasNewFile("-")
                        .hasOldFile("method boolean shaded.org.objectweb.asm.ClassWriter::hasFlags(int)")
                        .hasIssueName("java.method.removed")
                        .hasSeverities(expectedSeverities));

        expectedSeverities.replace("SOURCE", "NON_BREAKING", "POTENTIALLY_BREAKING");
        expectedSeverities.replace("BINARY", "NON_BREAKING", "POTENTIALLY_BREAKING");
        expectedSeverities.replace("OTHER", "NON_BREAKING", "POTENTIALLY_BREAKING");
        expectedSeverities.put("SEMANTIC", "POTENTIALLY_BREAKING");
        softly.assertThat(report.get(5))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("Field")
                .hasPackageName("shaded.org.objectweb.asm")
                .hasFileName("shaded/org/objectweb/asm/Opcodes.java");
        softly.assertThat(report.get(5).getAdditionalProperties())
                .isInstanceOfSatisfying(RevApiInfoExtension.class, i -> softly.assertThat(i)
                        .hasNewFile("-")
                        .hasOldFile("field shaded.org.objectweb.asm.Opcodes.V19")
                        .hasIssueName("java.field.removedWithConstant")
                        .hasSeverities(expectedSeverities));
    }

    @Test
    void shouldRenderWithJustification() {
        var report = parse("revapi-result.json");

        assertThat(report).hasSize(2);
        assertThat(report.get(0))
                .hasCategory("Method")
                .hasType("java.method.removed")
                .hasPackageName("edu.hm.hafner.coverage")
                .hasFileName("edu/hm/hafner/coverage/CoverageParser.java")
                .hasMessage("Method was removed.");
        assertThat(report.get(0).getDescription()).isEqualToIgnoringWhitespace("""
                <table>
                    <tr>
                        <td> Class: </td>
                        <td> edu.hm.hafner.coverage.CoverageParser </td>
                    </tr>
                    <tr>
                        <td> Code: </td>
                        <td> java.method.removed </td>
                    </tr>
                    <tr>
                        <td> Name: </td>
                        <td> method removed </td>
                    </tr>
                    <tr>
                        <td> New Element: </td>
                        <td> - </td>
                    </tr>
                    <tr>
                        <td> Old Element: </td>
                        <td> method edu.hm.hafner.coverage.Value edu.hm.hafner.coverage.CoverageParser::createValue(java.lang.String, int, int) </td>
                    </tr>
                    <tr>
                        <td>
                            Justification:
                        </td>
                        <td>
                            Allowed by the rules of semantic versioning.
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Classification:
                        </td>
                        <td>
                            <dl>
                                <dt>BINARY</dt> <dd>BREAKING</dd>
                                <dt>SOURCE</dt> <dd>BREAKING</dd>
                            </dl>
                        </td>
                    </tr>
                </table>
                """);
    }

    @Override
    protected IssueParser createParser() {
        return new RevApiParser();
    }
}
