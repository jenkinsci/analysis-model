package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.parser.EclipseXMLParser;

/**
 * A Descriptor for the Eclipse Xml parser.
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
    public edu.hm.hafner.analysis.IssueParser createParser() {
        return new EclipseXMLParser();
    }
}
