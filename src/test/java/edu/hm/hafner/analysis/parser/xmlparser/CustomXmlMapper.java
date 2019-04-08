package edu.hm.hafner.analysis.parser.xmlparser;

import java.util.HashMap;

/**
 *
 * Custom Mapper for Tests.
 *
 * @author Raphael Furch
 */
public class CustomXmlMapper extends HashMap<String, String> {

    /**
     * Creates a new object.
     */
    public CustomXmlMapper() {
        put(XmlParser.FILE_NAME, "custom1");
        put(XmlParser.LINE_START, "custom2");
        put(XmlParser.LINE_END, "custom3");
        put(XmlParser.COLUMN_START, "custom4");
        put(XmlParser.COLUMN_END, "custom5");

        put(XmlParser.CATEGORY, "custom7");
        put(XmlParser.TYPE, "custom8");
        put(XmlParser.SEVERITY, "custom9");
        put(XmlParser.MESSAGE, "custom10");
        put(XmlParser.DESCRIPTION, "custom11");
        put(XmlParser.PACKAGE_NAME, "custom12");
        put(XmlParser.MODULE_NAME, "custom13");
        put(XmlParser.ORIGIN, "custom14");
        put(XmlParser.REFERENCE, "custom15");
        put(XmlParser.FINGERPRINT, "custom16");
        put(XmlParser.ADDITIONAL_PROPERTIES, "custom17");
    }

}
