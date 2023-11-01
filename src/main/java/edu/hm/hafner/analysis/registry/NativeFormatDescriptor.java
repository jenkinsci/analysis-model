package edu.hm.hafner.analysis.registry;

import java.util.Collection;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.JsonLogParser;
import edu.hm.hafner.analysis.parser.JsonParser;
import edu.hm.hafner.analysis.parser.XmlParser;

import static j2html.TagCreator.*;

/**
 * A descriptor for the native format of the analysis model. This format is a 1:1 mapping of the properties of the
 * {@link Issue} bean.
 *
 * @author Lorenz Munsch
 */
public class NativeFormatDescriptor extends CompositeParserDescriptor {
    private static final String ID = "native";
    private static final String NAME = "Native Analysis Model Format";

    public NativeFormatDescriptor() {
        super(ID, NAME);
    }

    @Override
    protected Collection<? extends IssueParser> createParsers() {
        return asList(new XmlParser("/report/issue"), new JsonLogParser(), new JsonParser());
    }

    @Override
    public String getHelp() {
        return p().withText("Create an output file that contains issues in the native analysis-model format, "
                + "in either XML or JSON. "
                + "The parser is even capable of reading individual lines of a log file that contains issues in JSON format.").render();
    }

    @Override
    public String getUrl() {
        return "https://github.com/jenkinsci/warnings-ng-plugin/blob/master/doc/Documentation.md#export-your-issues-into-a-supported-format";
    }
}
