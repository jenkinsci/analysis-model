package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.ClangParser;
import edu.hm.hafner.analysis.parser.LlvmLinkerParser;

import java.util.Collection;

/**
 * A descriptor for the Clang parser (compiler + linker).
 *
 * @author Lorenz Munsch
 * @author Steven Scheffler
 */
class ClangDescriptor extends CompositeParserDescriptor {
    private static final String ID = "clang";
    private static final String NAME = "Clang (LLVM based)";

    ClangDescriptor() {
        super(ID, NAME);
    }

    @Override
    protected Collection<? extends IssueParser> createParsers() {
        return asList(new ClangParser(), new LlvmLinkerParser());
    }
}
