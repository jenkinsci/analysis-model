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
import edu.hm.hafner.util.TreeString;
import edu.hm.hafner.util.TreeStringBuilder;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.io.Serial;

/**
 * Parser that reads the 1:1 XML mapping of the properties of the {@link Issue} bean.
 *
 * @author Raphael Furch
 */
@SuppressFBWarnings("XPATH_INJECTION")
public class XmlParser extends IssueParser {
    @Serial
    private static final long serialVersionUID = -8099458358775144575L;

    private static final String LINE_RANGES_PATH = "lineRanges/lineRange";
    private static final String DEFAULT_ROOT_PATH = "/issue";
    private static final String LOCATIONS_PATH = "locations/location";

    /**
     * Create a new {@link XmlParser} instance.
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

    /**
     * Path to the issues within the XML-File.
     */
    private final String xmlIssueRoot;

    private String getXmlIssueRoot() {
        return xmlIssueRoot;
    }

    @Override
    public boolean accepts(final ReaderFactory readerFactory) {
        return readerFactory.getFileName().endsWith(".xml");
    }

    @Override
    public Report parseReport(final ReaderFactory readerFactory) {
        try (var issueBuilder = new IssueBuilder()) {
            var doc = readerFactory.readDocument();
            var xPathFactory = XPathFactory.newInstance();
            var path = xPathFactory.newXPath();
            var issues = (NodeList) path.evaluate(getXmlIssueRoot(), doc, XPathConstants.NODESET);

            var report = new Report();

            var fileNames = new TreeStringBuilder(); // for interning file names
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

                readLocations(issue, path, issueBuilder, fileNames);
                if (!issueBuilder.hasLocations()) { // Fallback to the old line range format
                    readLineRanges(issue, path, issueBuilder, fileNames);
                }

                report.add(issueBuilder.buildAndClean());
            }
            return report;
        }
        catch (XPathExpressionException e) {
            throw new ParsingException(e, readerFactory);
        }
    }

    private void readLineRanges(final Element issue, final XPath path, final IssueBuilder issueBuilder,
            final TreeStringBuilder fileNames)
            throws XPathExpressionException {
        var fileName = readFileName(issue, path, fileNames);
        issueBuilder.addLocation(new Location(fileName,
                readInt(issue, path, LINE_START), readInt(issue, path, LINE_END),
                readInt(issue, path, COLUMN_START), readInt(issue, path, COLUMN_END)));
        var lineElements = XmlElementUtil.nodeListToList(
                (NodeList) path.evaluate(LINE_RANGES_PATH, issue, XPathConstants.NODESET));
        for (Element lineRange : lineElements) {
            issueBuilder.addLocation(new Location(fileName,
                    readInt(lineRange, path, LINE_RANGE_START), readInt(lineRange, path, LINE_RANGE_END)));
        }
    }

    private TreeString readFileName(final Element issue, final XPath path, final TreeStringBuilder fileNames)
            throws XPathExpressionException {
        return fileNames.intern(
                StringUtils.defaultIfEmpty(path.evaluate(FILE_NAME, issue), "-"));
    }

    private int readInt(final Element issue, final XPath path, final String elementName)
            throws XPathExpressionException {
        return IntegerParser.parseInt(path.evaluate(elementName, issue));
    }

    private void readLocations(final Element issue, final XPath path,
            final IssueBuilder issueBuilder, final TreeStringBuilder fileNames)
            throws XPathExpressionException {
        var locations = XmlElementUtil.nodeListToList(
                (NodeList) path.evaluate(LOCATIONS_PATH, issue, XPathConstants.NODESET));
        for (Element location : locations) {
            var fileName = readFileName(location, path, fileNames);
            issueBuilder.addLocation(new Location(fileName,
                    readInt(location, path, LINE_START), readInt(location, path, LINE_END),
                    readInt(location, path, COLUMN_START), readInt(location, path, COLUMN_END)));
        }
    }
}
