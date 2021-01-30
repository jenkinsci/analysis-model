package edu.hm.hafner.analysis.descriptor;

import edu.hm.hafner.analysis.parser.EclipseXMLParser;

/**
 * A Descriptor for the Eclipse Xml parser.
 *
 * @author Lorenz Munsch
 */
class EclipseXmlDescriptor extends ParserDescriptor {

    private static final String ID = "eclipse-xml";
    private static final String NAME = "EclipseXml";

    /**
     * ctor for the abstract Parser Descriptor class.
     */
    EclipseXmlDescriptor() {
        super(ID, NAME, new EclipseXMLParser());
    }
}
