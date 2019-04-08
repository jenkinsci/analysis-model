package edu.hm.hafner.analysis.parser.xmlparser;

import java.util.HashMap;

/**
 *
 * This class tells the xml parser which xml-tag (value) contains the value for a specified issue property (key).
 *
 * @author Raphael Furch
 */
public class DefaultXmlMapper extends HashMap<String, String> {

    /**
     * Creates a new object.
     */
    public DefaultXmlMapper() {
        put(XmlParser.FILE_NAME, XmlParser.FILE_NAME);
        put(XmlParser.LINE_START, XmlParser.LINE_START);
        put(XmlParser.LINE_END, XmlParser.LINE_END);
        put(XmlParser.COLUMN_START, XmlParser.COLUMN_START);
        put(XmlParser.COLUMN_END, XmlParser.COLUMN_END);
        put(XmlParser.MESSAGE, XmlParser.MESSAGE);
        put(XmlParser.CATEGORY, XmlParser.CATEGORY);
        put(XmlParser.TYPE, XmlParser.TYPE);
        put(XmlParser.SEVERITY, XmlParser.SEVERITY);
        put(XmlParser.DESCRIPTION, XmlParser.DESCRIPTION);
        put(XmlParser.PACKAGE_NAME, XmlParser.PACKAGE_NAME);
        put(XmlParser.MODULE_NAME, XmlParser.MODULE_NAME);
        put(XmlParser.ORIGIN, XmlParser.ORIGIN);
        put(XmlParser.REFERENCE, XmlParser.REFERENCE);
        put(XmlParser.FINGERPRINT, XmlParser.FINGERPRINT);
        put(XmlParser.ADDITIONAL_PROPERTIES, XmlParser.ADDITIONAL_PROPERTIES);
    }

}
