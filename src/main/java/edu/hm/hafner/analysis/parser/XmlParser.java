package edu.hm.hafner.analysis.parser;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Location;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.util.IntegerParser;
import edu.hm.hafner.analysis.util.XmlElementUtil;
import edu.hm.hafner.util.LineRange;
import edu.hm.hafner.util.LineRangeList;
import edu.hm.hafner.util.TreeStringBuilder;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Parser that reads the 1:1 XML mapping of the properties of the {@link Issue} bean.
 *
 * @author Raphael Furch
 */
public class XmlParser extends IssueParser {
    @Serial
    private static final long serialVersionUID = -8099458358775144575L;

    private static final String LINE_RANGES_PATH = "lineRanges/lineRange";
    private static final String LOCATIONS_PATH = "locations/location";

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

    @Override
    @SuppressFBWarnings("XPATH_INJECTION")
    public Report parseReport(final ReaderFactory readerFactory) {
        try (var issueBuilder = new IssueBuilder()) {
            var doc = readerFactory.readDocument();
            var xPathFactory = XPathFactory.newInstance();
            var path = xPathFactory.newXPath();
            var issues = (NodeList) path.evaluate(getXmlIssueRoot(), doc, XPathConstants.NODESET);

            var report = new Report();

            var fileNames = new TreeStringBuilder();// for interning file names
            for (Element issue : XmlElementUtil.nodeListToList(issues)) {
                issueBuilder.setMessage(path.evaluate(MESSAGE, issue))
                        .setCategory(path.evaluate(CATEGORY, issue))
                        .setType(path.evaluate(TYPE, issue))
                        .setSeverity(Severity.valueOf(path.evaluate(SEVERITY, issue), Severity.WARNING_NORMAL))
                        .setDescription(path.evaluate(DESCRIPTION, issue))
                        .setPackageName(path.evaluate(PACKAGE_NAME, issue))
                        .setModuleName(path.evaluate(MODULE_NAME, issue))
                        .setFingerprint(path.evaluate(FINGERPRINT, issue))
                        .setAdditionalProperties(path.evaluate(ADDITIONAL_PROPERTIES, issue));

                var locations = readLocations(path,
                        (NodeList) path.evaluate(LOCATIONS_PATH, issue, XPathConstants.NODESET));
                if (locations.isEmpty()) { // Fallback to the old line range format
                    var adapted = new ArrayList<Location>();
                    var fileName = fileNames.intern(
                            StringUtils.defaultIfEmpty(path.evaluate(FILE_NAME, issue), "-"));
                    adapted.add(new Location(fileName,
                            IntegerParser.parseInt(path.evaluate(LINE_START, issue)),
                            IntegerParser.parseInt(path.evaluate(LINE_END, issue)),
                            IntegerParser.parseInt(path.evaluate(COLUMN_START, issue)),
                            IntegerParser.parseInt(path.evaluate(COLUMN_END, issue))));
                    var lineRanges = readLineRanges(path,
                            (NodeList) path.evaluate(LINE_RANGES_PATH, issue, XPathConstants.NODESET));
                    Objects.requireNonNull(lineRanges).stream()
                            .map(lr -> new Location(fileName, lr.getStart(), lr.getEnd()))
                            .forEach(adapted::add);
                    issueBuilder.setLocations(adapted);
                }
                else {
                    issueBuilder.setLocations(locations);
                }

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
     * @param locationNodes
     *         list of lineRange nodes.
     *
     * @return all valid locations from the XML file
     * @throws XPathExpressionException
     *         whXML reading errors.
     */
    private List<Location> readLocations(final XPath path, final NodeList locationNodes)
            throws XPathExpressionException {
        var fileNames = new TreeStringBuilder();
        var locations = new ArrayList<Location>();
        for (Element lineRangeNode : XmlElementUtil.nodeListToList(locationNodes)) {
            if (lineRangeNode != null) {
                var fileNameNode = (Element) path.evaluate(LOCATION_FILE, lineRangeNode, XPathConstants.NODE);
                var lineStartNode = (Element) path.evaluate(LOCATION_LINE_START, lineRangeNode, XPathConstants.NODE);
                var lineEndNode = (Element) path.evaluate(LOCATION_LINE_END, lineRangeNode, XPathConstants.NODE);
                var columnStartNode = (Element) path.evaluate(LOCATION_COLUMN_START, lineRangeNode,
                        XPathConstants.NODE);
                var columnEndNode = (Element) path.evaluate(LOCATION_COLUMN_END, lineRangeNode, XPathConstants.NODE);

                if (fileNameNode != null && fileNameNode.getFirstChild() != null
                        && lineStartNode != null && lineStartNode.getFirstChild() != null
                        && lineEndNode != null && lineEndNode.getFirstChild() != null
                        && columnStartNode != null && columnStartNode.getFirstChild() != null
                        && columnEndNode != null && columnEndNode.getFirstChild() != null) {
                    var fileNameValue = fileNameNode.getFirstChild().getNodeValue().trim();
                    var lineStartValue = lineStartNode.getFirstChild().getNodeValue().trim();
                    var lineEndValue = lineEndNode.getFirstChild().getNodeValue().trim();
                    var columnStartValue = columnStartNode.getFirstChild().getNodeValue().trim();
                    var columnEndValue = columnEndNode.getFirstChild().getNodeValue().trim();

                    locations.add(new Location(
                            fileNames.intern(fileNameValue),
                            IntegerParser.parseInt(lineStartValue),
                            IntegerParser.parseInt(lineEndValue),
                            IntegerParser.parseInt(columnStartValue),
                            IntegerParser.parseInt(columnEndValue)));
                }
            }
        }
        return locations;
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
}
