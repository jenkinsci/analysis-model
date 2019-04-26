package edu.hm.hafner.analysis.parser;

import java.util.UUID;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.LineRange;
import edu.hm.hafner.analysis.LineRangeList;
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

    private static final String ID = "id";
    private static final String FILE_NAME = "fileName";
    private static final String LINE_START = "lineStart";
    private static final String LINE_END = "lineEnd";
    private static final String LINE_RANGES = "lineRanges/lineRange";
    private static final String LINE_RANGE_START = "start";
    private static final String LINE_RANGE_END = "end";
    private static final String COLUMN_START = "columnStart";
    private static final String COLUMN_END = "columnEnd";
    private static final String CATEGORY = "category";
    private static final String TYPE = "type";
    private static final String SEVERITY = "severity";
    private static final String MESSAGE = "message";
    private static final String DESCRIPTION = "description";
    private static final String PACKAGE_NAME = "packageName";
    private static final String MODULE_NAME = "moduleName";
    private static final String ORIGIN = "origin";
    private static final String REFERENCE = "reference";
    private static final String FINGERPRINT = "fingerprint";
    private static final String ADDITIONAL_PROPERTIES = "additionalProperties";

    /**
     * Path to the issues within the XML-File.
     */
    private final String xmlIssueRoot;

    private String getXmlIssueRoot() {
        return xmlIssueRoot;
    }

    /**
     * Default path to the issues within the XML-File.
     */
    private static final String DEFAULT_ROOT_PATH = "/issue";

    /**
     * Create a new ReportParser object.
     */
    public XmlParser() {
        this(DEFAULT_ROOT_PATH);
    }

    /**
     * Create a new ReportParser object.
     *
     * @param root
     *         path to issues tag.
     */
    public XmlParser(final String root) {
        xmlIssueRoot = root;
    }

    @Override
    public Report parse(final ReaderFactory readerFactory) {
        try {
            Document doc = readerFactory.readDocument();
            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath path = xPathFactory.newXPath();
            NodeList issues = (NodeList) path.evaluate(getXmlIssueRoot(), doc, XPathConstants.NODESET);

            IssueBuilder issueBuilder = new IssueBuilder();
            Report report = new Report();

            for (Element issue : XmlElementUtil.nodeListToList(issues)) {
                issueBuilder
                        .setId(uuidTryParse(path.evaluate(ID, issue)))
                        .setFileName(path.evaluate(FILE_NAME, issue))
                        .setLineStart(path.evaluate(LINE_START, issue))
                        .setLineEnd(path.evaluate(LINE_END, issue))
                        .setColumnStart(path.evaluate(COLUMN_START, issue))
                        .setColumnEnd(path.evaluate(COLUMN_END, issue))
                        .setLineRanges(readLineRanges(path,
                                (NodeList) path.evaluate(LINE_RANGES, issue, XPathConstants.NODESET)))
                        .setCategory(path.evaluate(CATEGORY, issue))
                        .setType(path.evaluate(TYPE, issue))
                        .guessSeverity(path.evaluate(SEVERITY, issue))
                        .setMessage(path.evaluate(MESSAGE, issue))
                        .setDescription(path.evaluate(DESCRIPTION, issue))
                        .setPackageName(path.evaluate(PACKAGE_NAME, issue))
                        .setModuleName(path.evaluate(MODULE_NAME, issue))
                        .setOrigin(path.evaluate(ORIGIN, issue))
                        .setReference(path.evaluate(REFERENCE, issue))
                        .setFingerprint(path.evaluate(FINGERPRINT, issue))
                        .setAdditionalProperties(path.evaluate(ADDITIONAL_PROPERTIES, issue));

                report.add(issueBuilder.build());
            }
            return report;
        }
        catch (ParsingException | XPathExpressionException e) {
            throw new ParsingException(e);
        }
    }

    /**
     * Reads line ranges from XPath.
     *
     * @param path
     *         path of current xml file.
     * @param lineRanges
     *         list of lineRange nodes.
     *
     * @return all valid line ranges from xml file.
     * @throws XPathExpressionException
     *         for xml reading errors.
     */
    private LineRangeList readLineRanges(final XPath path, final NodeList lineRanges) throws XPathExpressionException {
        LineRangeList ranges = new LineRangeList();
        for (Element lineRangeNode : XmlElementUtil.nodeListToList(lineRanges)) {
            if (lineRangeNode != null) {
                Element startNode = (Element) path.evaluate(LINE_RANGE_START, lineRangeNode, XPathConstants.NODE);
                Element endNode = (Element) path.evaluate(LINE_RANGE_END, lineRangeNode, XPathConstants.NODE);
                if (startNode != null && startNode.getFirstChild() != null
                        && endNode != null && endNode.getFirstChild() != null) {
                    String startValue = startNode.getFirstChild().getNodeValue().trim();
                    String endValue = endNode.getFirstChild().getNodeValue().trim();
                    try {
                        int start = Integer.parseInt(startValue);
                        int end = Integer.parseInt(endValue);
                        ranges.add(new LineRange(start, end));
                    }
                    catch (NumberFormatException e) {
                        // Invalid value in xml.
                    }

                }
            }
        }
        return ranges;
    }

    /**
     * Try to parse a string to a uuid.
     *
     * @param uuidString
     *         string to parse.
     *
     * @return parsed string or a new uuid.
     */
    private UUID uuidTryParse(final String uuidString) {
        try {
            return UUID.fromString(uuidString);
        }
        catch (IllegalArgumentException ex) {
            return UUID.randomUUID();
        }
    }

}

