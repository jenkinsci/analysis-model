package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

/**
 * Tests the class {@link EmbeddedEngineerParser}.
 *
 *  @author Eva Habeeb
 */
class EmbeddedEngineerParserTest extends AbstractParserTest {
    EmbeddedEngineerParserTest() {
        super("ea.log");
    }

    @Override
    protected EmbeddedEngineerParser createParser() {
        return new EmbeddedEngineerParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(10);
        softly.assertThat(report.get(0))
                .hasModuleName("index_module")
                .hasDescription("Complex type definition without referenced element found 'index_module' (uint8_t); {98CF1FE6-EC9C-43f1-e476-40EFCD63cA8D}")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("Complex type definition without referenced element")
                .hasFileName("'01_first_sw'; {EF31393-1234-5678-8710-03561AD6E297}");
        softly.assertThat(report.get(3))
                .hasCategory("Code generation skipped")
                .hasSeverity(Severity.WARNING_NORMAL);
        softly.assertThat(report.get(4))
                .hasCategory("Code generation failed")
                .hasSeverity(Severity.WARNING_HIGH);
        softly.assertThat(report.get(5))
                .hasCategory("No Category")
                .hasSeverity(Severity.WARNING_NORMAL);
        softly.assertThat(report.get(6))
                .hasCategory("SampleValidation")
                .hasSeverity(Severity.WARNING_NORMAL);
        softly.assertThat(report.get(7))
                .hasCategory("Error loading plugins from")
                .hasDescription("Error loading plugins from C:\\file1\\idc\\sample_ext.x64.dll")
                .hasSeverity(Severity.ERROR);
        softly.assertThat(report.get(8))
                .hasCategory("Out parameters")
                .hasDescription("Out parameters 'Model_ptr_2345') are not supported. Please use 'return' or 'inout' parameters; {98CF1FE6-EC9C-43f1-e476-40EFCD63cA8D}")
                .hasSeverity(Severity.WARNING_NORMAL);
        softly.assertThat(report.get(9))
                .hasCategory("Error loading plugins from")
                .hasDescription("Error loading plugins from C:\\file1\\idc\\sample_ext.x64.dll File name: "
                + "'file:///C:\\AA\\BB\\System.Text.Json.dll' ---> System.NotSupportedException: "
                + "An attempt was made to load an assembly from a network location which would have caused the "
                + "assembly to be sandboxed in previous versions of the .NET Framework. "
                + "This release of the .NET Framework does not enable CAS policy by default, "
                + "so this load may be dangerous. If this load is not intended to sandbox the assembly,"
                + " please enable the loadFromRemoteSources switch. "
                + "See http://go.microsoft.com/fwlink/?LinkId=155569 for more information.   "
                + " at System.Reflection.RuntimeAssembly._nLoad(AssemblyName fileName, String codeBase,"
                + " Evidence assemblySecurity, RuntimeAssembly locationHint, StackCrawlMark& stackMark, "
                + "IntPtr pPrivHostBinder, Boolean throwOnFileNotFound, Boolean forIntrospection, "
                + "Boolean suppressSecurityChecks)    at System.Reflection.RuntimeAssembly."
                + "InternalLoadAssemblyName(AssemblyName assemblyRef, Evidence assemblySecurity, "
                + "RuntimeAssembly reqAssembly, StackCrawlMark& stackMark, IntPtr pPrivHostBinder, "
                + "Boolean throwOnFileNotFound, Boolean forIntrospection, Boolean suppressSecurityChecks)    "
                + "at System.Reflection.RuntimeAssembly.InternalLoadFrom(String assemblyFile, "
                + "Evidence securityEvidence, Byte[] hashValue, AssemblyHashAlgorithm hashAlgorithm, "
                + "Boolean forIntrospection, Boolean suppressSecurityChecks, StackCrawlMark& stackMark)   "
                + " at System.Reflection.Assembly.LoadFrom(String assemblyFile)    "
                + "at A.A`1.a(String , Predicate`1 , Object[] )    "
                + "at A.A`1.A(String , Predicate`1 , Object[] )")
                .hasSeverity(Severity.ERROR);
    }
}

