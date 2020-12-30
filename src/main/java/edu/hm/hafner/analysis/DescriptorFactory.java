package edu.hm.hafner.analysis;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import edu.hm.hafner.analysis.descriptor.AcuCobolDescriptor;
import edu.hm.hafner.analysis.descriptor.AjcDescriptor;
import edu.hm.hafner.analysis.descriptor.AnsibleLintDescriptor;
import edu.hm.hafner.analysis.descriptor.AntJavacDescriptor;
import edu.hm.hafner.analysis.descriptor.Armcc5CompilerDescriptor;
import edu.hm.hafner.analysis.descriptor.ArmccCompilerDescriptor;
import edu.hm.hafner.analysis.descriptor.CcmDescriptor;
import edu.hm.hafner.analysis.descriptor.CheckstyleDescriptor;
import edu.hm.hafner.analysis.descriptor.Descriptor;
import edu.hm.hafner.analysis.descriptor.FxcopDescriptor;
import edu.hm.hafner.analysis.descriptor.GendarmeDescriptor;
import edu.hm.hafner.analysis.descriptor.JcreportDescriptor;
import edu.hm.hafner.analysis.descriptor.PmdDescriptor;
import edu.hm.hafner.analysis.descriptor.PvsStudioDescriptor;

/**
 * Provides a {@link DescriptorFactory} that returns a map for all available Parsers.
 *
 * @author Lorenz Munsch
 */
@SuppressWarnings("checkstyle:ClassDataAbstractionCoupling")
public class DescriptorFactory {

    private final Map<String, Descriptor> descriptors = new HashMap<>();

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
        AcuCobolDescriptor acuCobolDescriptor = new AcuCobolDescriptor();
        descriptors.put(acuCobolDescriptor.getName(), acuCobolDescriptor);

        AjcDescriptor ajcDescriptor = new AjcDescriptor();
        descriptors.put(ajcDescriptor.getName(), ajcDescriptor);

        AnsibleLintDescriptor ansibleLintDescriptor = new AnsibleLintDescriptor();
        descriptors.put(ansibleLintDescriptor.getName(), ansibleLintDescriptor);

        AntJavacDescriptor antJavacDescriptor = new AntJavacDescriptor();
        descriptors.put(antJavacDescriptor.getName(), antJavacDescriptor);

        Armcc5CompilerDescriptor armcc5CompilerDescriptor = new Armcc5CompilerDescriptor();
        descriptors.put(armcc5CompilerDescriptor.getName(), armcc5CompilerDescriptor);

        ArmccCompilerDescriptor armccCompilerDescriptor = new ArmccCompilerDescriptor();
        descriptors.put(armccCompilerDescriptor.getName(), armccCompilerDescriptor);

        CcmDescriptor ccmDescriptor = new CcmDescriptor();
        descriptors.put(ccmDescriptor.getName(), ccmDescriptor);

        CheckstyleDescriptor checkstyleDescriptor = new CheckstyleDescriptor();
        descriptors.put(checkstyleDescriptor.getName(), checkstyleDescriptor);

        FxcopDescriptor fxcopDescriptor = new FxcopDescriptor();
        descriptors.put(fxcopDescriptor.getName(), fxcopDescriptor);

        GendarmeDescriptor gendarmeDescriptor = new GendarmeDescriptor();
        descriptors.put(gendarmeDescriptor.getName(), gendarmeDescriptor);

        JcreportDescriptor jcreportDescriptor = new JcreportDescriptor();
        descriptors.put(jcreportDescriptor.getName(), jcreportDescriptor);

        PmdDescriptor pmdDescriptor = new PmdDescriptor();
        descriptors.put(pmdDescriptor.getName(), pmdDescriptor);

        PvsStudioDescriptor pvsstudioDescriptor = new PvsStudioDescriptor();
        descriptors.put(pvsstudioDescriptor.getName(), pvsstudioDescriptor);
    }

    /**
     * Gets the name identifier of every available descriptor.
     *
     * @return a set of all descriptor names
     */
    public Set<String> getAllPossibleDescriptorNamesAsSet() {
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
    public Descriptor getDescriptorByName(String name) {
        return descriptors.get(name);
    }

}
