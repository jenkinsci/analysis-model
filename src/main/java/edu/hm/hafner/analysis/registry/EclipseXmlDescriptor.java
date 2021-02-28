package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.EclipseXMLParser;

/**
 * A descriptor for the Eclipse compiler (XML format).
 *
 * @author Lorenz Munsch
 */
class EclipseXmlDescriptor extends ParserDescriptor {
    private static final String ID = "eclipse-xml";
    private static final String NAME = "EclipseXml";

    EclipseXmlDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser() {
        return new EclipseXMLParser();
    }

    @Override
    public String getHelp() {
        return "<p><p>Create an output file that contains Eclipse ECJ output in XML format.</p>"
                + "<p>To log in XML format, specify &quot;.xml&quot; as the file extension to the -log argument:</p>"
                + "<p>"
                + "<code>java -jar ecj.jar -log &lt;logfile&gt;.xml &lt;other arguments&gt;</code></p>";
    }
}
