package edu.hm.hafner.analysis.descriptor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Provides a {@link DescriptorFactory} that returns a map for all available Parsers.
 *
 * @author Lorenz Munsch
 */
@SuppressWarnings("checkstyle:ClassDataAbstractionCouplingCheck")
public class DescriptorFactory {

    private ParserDescriptor[] allDescriptors = {
            new AcuCobolDescriptor(),
            new AjcDescriptor(),
            new AndroidLintDescriptor(),
            new AnsibleLintDescriptor(),
            new AntJavacDescriptor(),
            new Armcc5CompilerDescriptor(),
            new ArmccCompilerDescriptor(),
            new BuckminsterDescriptor(),
            new CadenceIncisiveDescriptor(),
            new CcmDescriptor(),
            new CheckstyleDescriptor(),
            new ClangDescriptor(),
            new ClangTidyDescriptor(),
            new CodeAnalysisDescriptor(),
            new CoolfluxChessccDescriptor(),
            new CpdDescriptor(),
            new CppLintDescriptor(),
            new DiabCDescriptor(),
            new DrMemoryDescriptor(),
            new DupfinderDescriptor(),
            new EclipseDescriptor(),
            new EclipseMavenDescriptor(),
            new EclipseXmlDescriptor(),
            new ErlcDescriptor(),
            new ErrorProneDescriptor(),
            new FindBugsDescriptor(),
            new FlexSdkDescriptor(),
            new FxcopDescriptor(),
            new Gcc4CompilerDescriptor(),
            new Gcc4LinkerDescriptor(),
            new GccDescriptor(),
            new GendarmeDescriptor(),
            new GhsMultiDescriptor(),
            new GnatDescriptor(),
            new GnuFortranDescriptor(),
            new GoLintDescriptor(),
            new GoVetDescriptor(),
            new GradleErrorProneDescriptor(),
            new IarCStatDescriptor(),
            new IarDescriptor(),
            new IdeaInspectionDescriptor(),
            new IntelDescriptor(),
            new InvalidsDescriptor(),
            new JavacDescriptor(),
            new JavaDocDescriptor(),
            new JcreportDescriptor(),
            new LintDescriptor(),
            new MavenConsoleDescriptor(),
            new MetrowerksCwCompilerDescriptor(),
            new MetrowerksCwLinkerDescriptor(),
            new MsBuildDescriptor(),
            new NagFortranDescriptor(),
            new P4Descriptor(),
            new Pep8Descriptor(),
            new PerlCriticDescriptor(),
            new PhpDescriptor(),
            new PmdDescriptor(),
            new PreFastDescriptor(),
            new PuppetLintDescriptor(),
            new PyLintDescriptor(),
            new QacSourceCodeAnalyserDescriptor(),
            new RfLintDescriptor(),
            new RoboCopyDescriptor(),
            new SbtScalacDescriptor(),
            new ScalacDescriptor(),
            new SimianDescriptor(),
            new SonarQubeDiffDescriptor(),
            new SonarQubeIssueDescriptor(),
            new SphinxBuildDescriptor(),
            new StyleCopDescriptor(),
            new SunCDescriptor(),
            new TaglistDescriptor(),
            new TaskingVxCompilerDescriptor(),
            new TiCcsDescriptor(),
            new TnsdlDescriptor(),
            new XlcCompilerDescriptor(),
            new XlcLinkerDescriptor(),
            new YuiCompressorDescriptor()
    };

    private final Map<String, ParserDescriptor> descriptors = new HashMap<>();

    /**
     * Initializes the descriptor-map.
     */
    public DescriptorFactory() {
        initialize();
    }

    /**
     * Creates the content for the descriptor-map with all available Parsers.
     */
    private void initialize() {
        for (ParserDescriptor descriptor : allDescriptors) {
            descriptors.put(descriptor.getName(), descriptor);
        }
    }

    /**
     * Gets the name identifier of every available descriptor.
     *
     * @return a set of all descriptor names
     */
    public Set<String> getDescriptorNames() {
        return descriptors.keySet();
    }

    /**
     * Used to access the desired descriptor.
     *
     * @param name
     *         the string identifier for a descriptor.
     *
     * @return the requested descriptor
     */
    public ParserDescriptor getDescriptor(final String name) {
        return descriptors.get(name);
    }

    /**
     * Returns the descriptor map.
     *
     * @return the map of all supported descriptors
     */
    public Map<String, ParserDescriptor> getAllDescriptors() {
        return descriptors;
    }
}
