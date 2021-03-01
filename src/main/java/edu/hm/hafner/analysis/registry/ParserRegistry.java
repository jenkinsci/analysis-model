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
            new ArmccCompilerDescriptor(),
            new BrakemanDescriptor(),
            new BuckminsterDescriptor(),
            new CadenceIncisiveDescriptor(),
            new CargoDescriptor(),
            new CcmDescriptor(),
            new CheckstyleDescriptor(),
            new ClairDescriptor(),
            new ClangAnalyzerDescriptor(),
            new ClangDescriptor(),
            new ClangTidyDescriptor(),
            new CmakeDescriptor(),
            new CodeAnalysisDescriptor(),
            new CodeNarcDescriptor(),
            new CoolfluxChessccDescriptor(),
            new CpdDescriptor(),
            new CppCheckDescriptor(),
            new CppLintDescriptor(),
            new CssLintDescriptor(),
            new DetektDescriptor(),
            new DiabCDescriptor(),
            new DocFxDescriptor(),
            new DockerLintDescriptor(),
            new DoxygenDescriptor(),
            new DrMemoryDescriptor(),
            new DScannerDescriptor(),
            new DupfinderDescriptor(),
            new EclipseDescriptor(),
            new ErlcDescriptor(),
            new ErrorProneDescriptor(),
            new EsLintDescriptor(),
            new FindBugsDescriptor(),
            new Flake8Descriptor(),
            new FlawfinderDescriptor(),
            new FlexSdkDescriptor(),
            new FxcopDescriptor(),
            new Gcc4Descriptor(),
            new Gcc4LinkerDescriptor(),
            new GccDescriptor(),
            new GendarmeDescriptor(),
            new GhsMultiDescriptor(),
            new GnatDescriptor(),
            new GnuFortranDescriptor(),
            new GoLintDescriptor(),
            new GoVetDescriptor(),
            new HadoLintDescriptor(),
            new IarCstatDescriptor(),
            new IarDescriptor(),
            new IbLinterDescriptor(),
            new IdeaInspectionDescriptor(),
            new InferDescriptor(),
            new IntelDescriptor(),
            new InvalidsDescriptor(),
            new JavaDescriptor(),
            new JavaDocDescriptor(),
            new JcreportDescriptor(),
            new JsHintDescriptor(),
            new JsLintDescriptor(),
            new JUnitDescriptor(),
            new KlocWorkDescriptor(),
            new KotlinDescriptor(),
            new KtLintDescriptor(),
            new MavenConsoleDescriptor(),
            new MentorGraphicsDescriptor(),
            new MetrowerksCodeWarriorDescriptor(),
            new MsBuildDescriptor(),
            new MyPyDescriptor(),
            new NagFortranDescriptor(),
            new OtDockerLintDescriptor(),
            new PcLintDescriptor(),
            new Pep8Descriptor(),
            new PerforceDescriptor(),
            new PerlCriticDescriptor(),
            new PhpCodeSnifferDescriptor(),
            new PhpDescriptor(),
            new PhpStanDescriptor(),
            new PitDescriptor(),
            new PmdDescriptor(),
            new PreFastDescriptor(),
            new ProtoLintDescriptor(),
            new PuppetLintDescriptor(),
            new PvsStudioDescriptor(),
            new PyDocStyleDescriptor(),
            new PyLintDescriptor(),
            new QacSourceCodeAnalyserDescriptor(),
            new QtTranslationDescriptor(),
            new ResharperDescriptor(),
            new RfLintDescriptor(),
            new RoboCopyDescriptor(),
            new RuboCopDescriptor(),
            new ScalaDescriptor(),
            new SimianDescriptor(),
            new SonarQubeDescriptor(),
            new SphinxBuildDescriptor(),
            new SpotBugsDescriptor(),
            new StyleCopDescriptor(),
            new SunCDescriptor(),
            new SwiftLintDescriptor(),
            new TaglistDescriptor(),
            new TaskingVxCompilerDescriptor(),
            new TiCcsDescriptor(),
            new TnsdlDescriptor(),
            new TrivyDescriptor(),
            new TsLintDescriptor(),
            new XlcDescriptor(),
            new YamlLintDescriptor(),
            new XmlLintDescriptor(),
            new YuiCompressorDescriptor(),
            new ZptLintDescriptor()
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
