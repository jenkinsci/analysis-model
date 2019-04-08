package edu.hm.hafner.analysis.parser.xmlparser;

import java.util.Map;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.util.XmlElementUtil;

/**
 * A general parser for XML-Files.
 *
 * @author Raphael Furch
 */
public class XmlParser extends IssueParser {

    private static final long serialVersionUID = -8099458358775144575L;

    /**
     * Identifier for file name.
     */
    public static final String FILE_NAME = "fileName";

    /**
     * Identifier for line start.
     */
    static final String LINE_START = "lineStart";

    /**
     * Identifier for line end.
     */
    static final String LINE_END = "lineEnd";

    /**
     * Identifier for column start.
     */
    static final String COLUMN_START = "columnStart";

    /**
     * Identifier for column end.
     */
    static final String COLUMN_END = "columnEnd";

    /**
     * Identifier for category.
     */
    static final String CATEGORY = "category";

    /**
     * Identifier for type.
     */
    static final String TYPE = "type";

    /**
     * Identifier for severity.
     */
    static final String SEVERITY = "severity";

    /**
     * Identifier for message.
     */
    static final String MESSAGE = "message";

    /**
     * Identifier for description.
     */
    static final String DESCRIPTION = "description";

    /**
     * Identifier for package name.
     */
    static final String PACKAGE_NAME = "packageName";

    /**
     * Identifier for module name.
     */
    static final String MODULE_NAME = "moduleName";

    /**
     * Identifier for origin.
     */
    static final String ORIGIN = "origin";

    /**
     * Identifier for reference.
     */
    static final String REFERENCE = "reference";

    /**
     * Identifier for fingerprint.
     */
    static final String FINGERPRINT = "fingerprint";

    /**
     * Identifier for additional properties.
     */
    static final String ADDITIONAL_PROPERTIES = "additionalProperties";

    /**
     * Map between XML-Tags and Issue-Properties.
     */
    private final Map<String, String> propertyMapper;

    /**
     * Path to the issues within the XML-File.
     */
    private final String xmlIssueRoot;

    /**
     * Default path to the issues within the XML-File.
     */
    private static final String DEFAULT_ROOT_PATH = "/issues/issue";

    /**
     * Create a new ReportParser object.
     * @param root = Path in xml file to issue collection.
     * @param mapper = Tag to Property Mapper.
     */
    public XmlParser(final String root, final Map<String, String> mapper) {
        this.xmlIssueRoot = root;
        this.propertyMapper = mapper;
    }

    /**
     * Create a new ReportParser object.
     */
    public XmlParser() {
        this(DEFAULT_ROOT_PATH, new DefaultXmlMapper());
    }


    @Override
    public Report parse(final ReaderFactory readerFactory) {
        try {
            Document doc = readerFactory.readDocument();

            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath path = xPathFactory.newXPath();

            IssueBuilder issueBuilder = new IssueBuilder();
            Report report = new Report();

            NodeList issues = (NodeList)path.evaluate(xmlIssueRoot, doc, XPathConstants.NODESET);
            // Read all issues.
            for (Element issue : XmlElementUtil.nodeListToList(issues)) {
                issueBuilder
                        .setFileName(notNullableRead(path, FILE_NAME, issue))
                        .setLineStart(notNullableRead(path, LINE_START, issue))
                        .setLineEnd(notNullableRead(path, LINE_END, issue))
                        .setColumnStart(notNullableRead(path, COLUMN_START, issue))
                        .setColumnEnd(notNullableRead(path, COLUMN_END, issue))
                        .setCategory(notNullableRead(path, CATEGORY, issue))
                        .setType(notNullableRead(path, TYPE, issue))
                        .guessSeverity(notNullableRead(path, SEVERITY, issue))
                        .setMessage(notNullableRead(path, MESSAGE, issue))
                        .setDescription(notNullableRead(path, DESCRIPTION, issue))
                        .setPackageName(notNullableRead(path, PACKAGE_NAME, issue))
                        .setModuleName(notNullableRead(path, MODULE_NAME, issue))
                        .setOrigin(notNullableRead(path, ORIGIN, issue))
                        .setReference(notNullableRead(path, REFERENCE, issue))
                        .setFingerprint(notNullableRead(path, FINGERPRINT, issue))
                        .setAdditionalProperties(notNullableRead(path, ADDITIONAL_PROPERTIES, issue));
                // TODO LineRange is missing.
                report.add(issueBuilder.build());

            }
            return report;
        }
        catch (ParsingException | XPathExpressionException e) {
            throw new ParsingException(e);
        }
    }

    /**
     * Read a value from XML and set a default value if its null.
     * @param path = xpath.
     * @param value = value which should be read.
     * @param issue = current issue.
     * @return value or default value.
     * @throws XPathExpressionException error.
     */
    private String notNullableRead(final XPath path, final String value, final Element issue) throws XPathExpressionException {
        String readValue = path.evaluate(propertyMapper.get(value), issue);
        return readValue != null && !"".equals(readValue) ? readValue : "-";
    }


}
