package edu.hm.hafner.analysis.parser;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import edu.hm.hafner.analysis.FileLocation;
import edu.hm.hafner.analysis.FileLocationList;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.util.XmlElementUtil;
import edu.hm.hafner.util.LineRange;
import edu.hm.hafner.util.LineRangeList;
import edu.hm.hafner.util.TreeString;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.io.Serial;

/**
 * Parser that reads the 1:1 XML mapping of the properties of the {@link Issue} bean.
 *
 * @author Raphael Furch
 */
public class XmlParser extends IssueParser {
    @Serial
    private static final long serialVersionUID = -8099458358775144575L;

    private static final String LINE_RANGES_PATH = "lineRanges/lineRange";
    private static final String FILE_LOCATIONS_PATH = "fileLocations/fileLocation";

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
    public boolean accepts(final ReaderFactory readerFactory) {
        return readerFactory.getFileName().endsWith(".xml");
    }

    @Override @SuppressFBWarnings("XPATH_INJECTION")
    public Report parseReport(final ReaderFactory readerFactory) {
        try (var issueBuilder = new IssueBuilder()) {
            var doc = readerFactory.readDocument();
            var xPathFactory = XPathFactory.newInstance();
            var path = xPathFactory.newXPath();
            var issues = (NodeList) path.evaluate(getXmlIssueRoot(), doc, XPathConstants.NODESET);

            var report = new Report();

            for (Element issue : XmlElementUtil.nodeListToList(issues)) {
                issueBuilder.setFileName(path.evaluate(FILE_NAME, issue))
                        .setLineStart(path.evaluate(LINE_START, issue))
                        .setLineEnd(path.evaluate(LINE_END, issue))
                        .setColumnStart(path.evaluate(COLUMN_START, issue))
                        .setColumnEnd(path.evaluate(COLUMN_END, issue))
                        .setLineRanges(readLineRanges(path,
                                (NodeList) path.evaluate(LINE_RANGES_PATH, issue, XPathConstants.NODESET)))
                        .setFileLocations(readFileLocations(path,
                                (NodeList) path.evaluate(FILE_LOCATIONS_PATH, issue, XPathConstants.NODESET)))
                        .setCategory(path.evaluate(CATEGORY, issue))
                        .setType(path.evaluate(TYPE, issue))
                        .setSeverity(Severity.valueOf(path.evaluate(SEVERITY, issue), Severity.WARNING_NORMAL))
                        .setMessage(path.evaluate(MESSAGE, issue))
                        .setDescription(path.evaluate(DESCRIPTION, issue))
                        .setPackageName(path.evaluate(PACKAGE_NAME, issue))
                        .setModuleName(path.evaluate(MODULE_NAME, issue))
                        .setFingerprint(path.evaluate(FINGERPRINT, issue))
                        .setAdditionalProperties(path.evaluate(ADDITIONAL_PROPERTIES, issue));

                report.add(issueBuilder.buildAndClean());
            }
            return report;
        }
        catch (XPathExpressionException e) {
            throw new ParsingException(e, readerFactory);
        }
    }

    /**
     * Reads line ranges from XPath.
     *
     * @param path
     *         path of the current XML file.
     * @param lineRanges
     *         list of lineRange nodes.
     *
     * @return all valid line ranges from xml file.
     * @throws XPathExpressionException
     *         for xml reading errors.
     */
    private LineRangeList readLineRanges(final XPath path, final NodeList lineRanges) throws XPathExpressionException {
        var ranges = new LineRangeList();
        for (Element lineRangeNode : XmlElementUtil.nodeListToList(lineRanges)) {
            if (lineRangeNode != null) {
                var startNode = (Element) path.evaluate(LINE_RANGE_START, lineRangeNode, XPathConstants.NODE);
                var endNode = (Element) path.evaluate(LINE_RANGE_END, lineRangeNode, XPathConstants.NODE);
                if (startNode != null && startNode.getFirstChild() != null
                        && endNode != null && endNode.getFirstChild() != null) {
                    var startValue = startNode.getFirstChild().getNodeValue().trim();
                    var endValue = endNode.getFirstChild().getNodeValue().trim();
                    try {
                        int start = Integer.parseInt(startValue);
                        int end = Integer.parseInt(endValue);
                        ranges.add(new LineRange(start, end));
                    }
                    catch (NumberFormatException e) {
                        // Ignore invalid values in xml
                    }
                }
            }
        }
        return ranges;
    }

    /**
     * Reads file locations from XPath.
     *
     * @param path
     *         path of the current XML file.
     * @param fileLocations
     *         list of fileLocation nodes.
     *
     * @return all valid file locations from xml file.
     * @throws XPathExpressionException
     *         for xml reading errors.
     */
    private FileLocationList readFileLocations(final XPath path, final NodeList fileLocations)
            throws XPathExpressionException {
        var locations = new FileLocationList();
        for (Element fileLocationNode : XmlElementUtil.nodeListToList(fileLocations)) {
            if (fileLocationNode != null) {
                var pathNode = (Element) path.evaluate(FILE_LOCATION_PATH, fileLocationNode, XPathConstants.NODE);
                var fileNameNode = (Element) path.evaluate(FILE_LOCATION_FILE_NAME, fileLocationNode,
                        XPathConstants.NODE);
                var lineStartNode = (Element) path.evaluate(FILE_LOCATION_LINE_START, fileLocationNode,
                        XPathConstants.NODE);
                var lineEndNode = (Element) path.evaluate(FILE_LOCATION_LINE_END, fileLocationNode,
                        XPathConstants.NODE);
                var columnStartNode = (Element) path.evaluate(FILE_LOCATION_COLUMN_START, fileLocationNode,
                        XPathConstants.NODE);
                var columnEndNode = (Element) path.evaluate(FILE_LOCATION_COLUMN_END, fileLocationNode,
                        XPathConstants.NODE);

                if (fileNameNode != null && fileNameNode.getFirstChild() != null) {
                    var pathName = pathNode != null && pathNode.getFirstChild() != null
                            ? pathNode.getFirstChild().getNodeValue().trim()
                            : "";
                    var fileName = fileNameNode.getFirstChild().getNodeValue().trim();

                    int lineStart = 0;
                    int lineEnd = 0;
                    int columnStart = 0;
                    int columnEnd = 0;

                    try {
                        if (lineStartNode != null && lineStartNode.getFirstChild() != null) {
                            lineStart = Integer.parseInt(lineStartNode.getFirstChild().getNodeValue().trim());
                        }
                        if (lineEndNode != null && lineEndNode.getFirstChild() != null) {
                            lineEnd = Integer.parseInt(lineEndNode.getFirstChild().getNodeValue().trim());
                        }
                        if (columnStartNode != null && columnStartNode.getFirstChild() != null) {
                            columnStart = Integer.parseInt(columnStartNode.getFirstChild().getNodeValue().trim());
                        }
                        if (columnEndNode != null && columnEndNode.getFirstChild() != null) {
                            columnEnd = Integer.parseInt(columnEndNode.getFirstChild().getNodeValue().trim());
                        }

                        locations.add(new FileLocation(pathName, TreeString.valueOf(fileName),
                                lineStart, lineEnd, columnStart, columnEnd));
                    }
                    catch (NumberFormatException e) {
                        // Ignore invalid values in xml
                    }
                }
            }
        }
        return locations;
    }
}
