package edu.hm.hafner.analysis.registry;

import java.util.Collection;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.JsonLogParser;
import edu.hm.hafner.analysis.parser.JsonParser;
import edu.hm.hafner.analysis.parser.XmlParser;

/**
 * A descriptor for the Maven Console parser.
 *
 * @author Lorenz Munsch
 */
class NativeFormatDescriptor extends CompositeParserDescriptor {
    private static final String ID = "native";
    private static final String NAME = "Native Analysis Model Format";

    NativeFormatDescriptor() {
        super(ID, NAME);
    }

    @Override
    protected Collection<? extends IssueParser> createParsers() {
        return asList(new XmlParser("/report/issue"), new JsonLogParser(), new JsonParser());
    }
}
