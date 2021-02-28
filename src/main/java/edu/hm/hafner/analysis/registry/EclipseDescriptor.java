package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.EclipseParser;

/**
 * A descriptor for the Eclipse compiler (text format).
 *
 * @author Lorenz Munsch
 */
class EclipseDescriptor extends ParserDescriptor {
    private static final String ID = "eclipse";
    private static final String NAME = "Eclipse ECJ";

    EclipseDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser() {
        return new EclipseParser();
    }

    @Override
    public String getHelp() {
        return "<p><p>Create an output file that contains Eclipse ECJ output in text format.</p>"
                + "<p>Specify any file extension except &quot;.xml&quot; to the -log argument:</p><p><code>java -jar ecj.jar -log &lt;logfile&gt;.log &lt;other arguments&gt;</code></p></p>";
    }
}
