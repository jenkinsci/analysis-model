package edu.hm.hafner.analysis.registry;

import java.util.Collection;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.EclipseMavenParser;
import edu.hm.hafner.analysis.parser.EclipseParser;
import edu.hm.hafner.analysis.parser.EclipseXMLParser;

/**
 * A descriptor for the Eclipse compiler (text format).
 *
 * @author Lorenz Munsch
 */
public class EclipseDescriptor extends CompositeParserDescriptor {
    private static final String ID = "eclipse";
    private static final String NAME = "Eclipse ECJ";

    public EclipseDescriptor() {
        super(ID, NAME);
    }

    @Override
    protected Collection<? extends IssueParser> createParsers() {
        return asList(new EclipseParser(), new EclipseMavenParser(), new EclipseXMLParser());
    }

    @Override
    public String getHelp() {
        return "<p><p>Create an output file that contains Eclipse ECJ output, in either XML or text format.</p>"
                + "<p>To log in XML format, specify &quot;.xml&quot; as the file extension to the -log argument:</p>"
                + "<p>"
                + "<code>java -jar ecj.jar -log &lt;logfile&gt;.xml &lt;other arguments&gt;</code></p>"
                + "<p>To log in text format, specify any file extension except &quot;.xml&quot; to the -log argument:</p><p><code>java -jar ecj.jar -log &lt;logfile&gt;.log &lt;other arguments&gt;</code></p></p>";
    }
}
