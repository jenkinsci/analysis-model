package edu.hm.hafner.analysis.registry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Provides a {@link ParserRegistry} that returns a map for all available Parsers.
 *
 * @author Lorenz Munsch
 */
@SuppressWarnings({"checkstyle:ClassDataAbstractionCoupling", "checkstyle:ClassFanOutComplexity"})
public class ParserRegistry {
    private static final ParserDescriptor[] ALL_DESCRIPTORS = {
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
            new SpotBugsDescriptor(),
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

    private final Map<String, ParserDescriptor> descriptors;

    /**
     * Creates a new registry instance.
     */
    public ParserRegistry() {
        descriptors = Arrays.stream(ALL_DESCRIPTORS)
                .collect(Collectors.toMap(ParserDescriptor::getId, Function.identity()));
    }

    /**
     * Returns the IDs of all available parsers.
     *
     * @return a set of all IDs
     */
    public Set<String> getIds() {
        return new HashSet<>(descriptors.keySet());
    }

    /**
     * Returns the names of all available parsers.
     *
     * @return a set of all names
     */
    public Set<String> getNames() {
        return descriptors.values().stream().map(ParserDescriptor::getName).collect(Collectors.toSet());
    }

    /**
     * Returns the {@link ParserDescriptor} with the specified ID.
     *
     * @param id
     *         the ID of the parser
     *
     * @return the requested descriptor
     * @throws NoSuchElementException if no such parser exists
     */
    public ParserDescriptor get(final String id) {
        if (descriptors.containsKey(id)) {
            return descriptors.get(id);
        }
        throw new NoSuchElementException("No such parser registered: " + id);
    }

    /**
     * Returns all descriptors.
     *
     * @return all supported descriptors
     */
    public List<ParserDescriptor> getAllDescriptors() {
        return new ArrayList<>(descriptors.values());
    }
}
