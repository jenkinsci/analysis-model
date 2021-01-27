package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.EclipseMavenParser;
import edu.hm.hafner.analysis.parser.EclipseXMLParser;

/**
 * A Descriptor for the Eclipse Xml parser.
 *
 * @author Lorenz Munsch
 */
public class EclipseXmlDescriptor extends ParserDescriptor {

    private static final String ID = "eclipse-xml";
    private static final String NAME = "EclipseXml";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    public EclipseXmlDescriptor() {
        super(ID, NAME, new EclipseXMLParser());
    }
}
