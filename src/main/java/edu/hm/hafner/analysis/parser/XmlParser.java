package edu.hm.hafner.analysis.parser;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

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
}