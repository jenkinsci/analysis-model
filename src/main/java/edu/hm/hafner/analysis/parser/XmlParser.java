package edu.hm.hafner.analysis.parser;

import java.util.UUID;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.LineRange;
import edu.hm.hafner.analysis.LineRangeList;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.util.XmlElementUtil;

/**
 * A general parser for XML-Files.
 *
 * @author Raphael Furch
 */
public class XmlParser extends IssueParser {
    private static final long serialVersionUID = -8099458358775144575L;

    private static final String LINE_RANGES_PATH = "lineRanges/lineRange";

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
     * Create a new {@link XmlParser} object.
     */
    public XmlParser() {
        this(DEFAULT_ROOT_PATH);
    }

    /**
     * Create a new {@link XmlParser} instance.
     *
     * @param root
     *         path to issues tag.
     */
    public XmlParser(final String root) {
        super();

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
                        .setId(uuidTryParse(path.evaluate(Issue.ID, issue)))
                        .setFileName(path.evaluate(Issue.FILE_NAME, issue))
                        .setLineStart(path.evaluate(Issue.LINE_START, issue))
                        .setLineEnd(path.evaluate(Issue.LINE_END, issue))
                        .setColumnStart(path.evaluate(Issue.COLUMN_START, issue))
                        .setColumnEnd(path.evaluate(Issue.COLUMN_END, issue))
                        .setLineRanges(readLineRanges(path,
                                (NodeList) path.evaluate(LINE_RANGES_PATH, issue, XPathConstants.NODESET)))
                        .setCategory(path.evaluate(Issue.CATEGORY, issue))
                        .setType(path.evaluate(Issue.TYPE, issue))
                        .setSeverity(Severity.valueOf(path.evaluate(Issue.SEVERITY, issue), Severity.WARNING_NORMAL))
                        .setMessage(path.evaluate(Issue.MESSAGE, issue))
                        .setDescription(path.evaluate(Issue.DESCRIPTION, issue))
                        .setPackageName(path.evaluate(Issue.PACKAGE_NAME, issue))
                        .setModuleName(path.evaluate(Issue.MODULE_NAME, issue))
                        .setOrigin(path.evaluate(Issue.ORIGIN, issue))
                        .setReference(path.evaluate(Issue.REFERENCE, issue))
                        .setFingerprint(path.evaluate(Issue.FINGERPRINT, issue))
                        .setAdditionalProperties(path.evaluate(Issue.ADDITIONAL_PROPERTIES, issue));

                report.add(issueBuilder.build());
            }
            return report;
        }
        catch (XPathExpressionException e) {
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
                Element startNode = (Element) path.evaluate(Issue.LINE_RANGE_START, lineRangeNode, XPathConstants.NODE);
                Element endNode = (Element) path.evaluate(Issue.LINE_RANGE_END, lineRangeNode, XPathConstants.NODE);
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

