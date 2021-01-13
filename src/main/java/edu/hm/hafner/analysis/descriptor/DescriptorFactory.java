package edu.hm.hafner.analysis.descriptor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Provides a {@link DescriptorFactory} that returns a map for all available Parsers.
 *
 * @author Lorenz Munsch
 */
public class DescriptorFactory {

    ParserDescriptor[] DESCRIPTORS = {
            new AcuCobolDescriptor(),
            new AjcDescriptor(),
            new AnsibleLintDescriptor(),
            new AntJavacDescriptor(),
            new Armcc5CompilerDescriptor(),
            new ArmccCompilerDescriptor(),
            new CcmDescriptor(),
            new CheckstyleDescriptor(),
            new FxcopDescriptor(),
            new GendarmeDescriptor(),
            new JcreportDescriptor(),
            new PmdDescriptor(),
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
        for (ParserDescriptor descriptor : DESCRIPTORS) {
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
    public Map<String, ParserDescriptor> getDescriptors() {
        return descriptors;
    }
}
