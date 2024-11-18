package edu.hm.hafner.analysis.parser;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
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
import edu.hm.hafner.analysis.util.IntegerParser;
import edu.hm.hafner.analysis.util.XmlElementUtil;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * A parser for the clang-analyzer static analysis warnings.
 *
 * @author Andrey Danin
 */
public class ClangAnalyzerPlistParser extends IssueParser {
    @Serial
    private static final long serialVersionUID = 1L;
    static final String ID = "ClangAnalyzer Plist Parser";

    @Override
    public boolean accepts(final ReaderFactory readerFactory) {
        return isXmlFile(readerFactory);
    }

    @Override
    public Report parse(final ReaderFactory readerFactory) throws ParsingException {
        try (IssueBuilder issueBuilder = new IssueBuilder()) {
            Document doc = readerFactory.readDocument();

            var xPathFactory = XPathFactory.newInstance();
            var xPath = xPathFactory.newXPath();

            var filesPath = xPath.compile(
                    "/plist/dict/key[text()='files']/following-sibling::array/string");
            var diagnosticsPath = xPath.compile(
                    "/plist/dict/key[text()='diagnostics']/following-sibling::array/dict");
            var diagDescriptionPath = compileDiagStrPath(xPath, "description");
            var diagCategoryPath = compileDiagStrPath(xPath, "category");
            var diagTypePath = compileDiagStrPath(xPath, "type");
            var diagLocationLinePath = compileDiagLocationPath(xPath, "line");
            var diagLocationColPath = compileDiagLocationPath(xPath, "col");
            var diagLocationFilePath = compileDiagLocationPath(xPath, "file");

            var report = new Report();

            var files = getFilesList(doc, filesPath);

            var diagnostics = (NodeList) diagnosticsPath.evaluate(doc, XPathConstants.NODESET);
            for (Element diag : XmlElementUtil.nodeListToList(diagnostics)) {
                issueBuilder.setFileName(getFileName(files, diag, diagLocationFilePath))
                        .guessSeverity("Warning")
                        .setMessage(extractField(diag, diagDescriptionPath))
                        .setLineStart(extractIntField(diag, diagLocationLinePath))
                        .setColumnStart(extractIntField(diag, diagLocationColPath))
                        .setCategory(extractField(diag, diagCategoryPath))
                        .setType(extractField(diag, diagTypePath));

                report.add(issueBuilder.buildAndClean());
            }

            return report;
        }
        catch (XPathExpressionException e) {
            throw new ParsingException(e);
        }
    }

    private static List<String> getFilesList(final Document doc, final XPathExpression filesPath)
            throws XPathExpressionException {
        List<String> files = new ArrayList<>();

        var nodes = (NodeList) filesPath.evaluate(doc, XPathConstants.NODESET);
        for (Element filePathStr : XmlElementUtil.nodeListToList(nodes)) {
            files.add(filePathStr.getTextContent());
        }

        return files;
    }

    private static String getFileName(final List<String> files, final Element diag,
            final XPathExpression diagLocationFilePath) throws XPathExpressionException {
        int idx = extractIntField(diag, diagLocationFilePath);
        if (idx >= files.size()) {
            return "-";
        }

        return files.get(idx);
    }

    @SuppressFBWarnings(value = "XPATH_INJECTION", justification = "parameter is a constant")
    private static XPathExpression compileDiagStrPath(final XPath xPath, final String field)
            throws XPathExpressionException {
        String search = "./key[text()='" + field + "']/following-sibling::string";
        return xPath.compile(search);
    }

    @SuppressFBWarnings(value = "XPATH_INJECTION", justification = "parameter is a constant")
    private static XPathExpression compileDiagLocationPath(final XPath xPath, final String field)
            throws XPathExpressionException {
        String search = "./key[text()='location']/following-sibling::dict/key[text()='" + field
                + "']/following-sibling::integer";
        return xPath.compile(search);
    }

    private static String extractField(final Element diag, final XPathExpression expr) throws XPathExpressionException {
        var keys = (NodeList) expr.evaluate(diag, XPathConstants.NODESET);

        List<Element> elements = XmlElementUtil.nodeListToList(keys);
        if (elements.isEmpty()) {
            return "";
        }

        return elements.get(0).getTextContent();
    }

    private static int extractIntField(final Element diag, final XPathExpression expr) throws XPathExpressionException {
        return IntegerParser.parseInt(extractField(diag, expr));
    }
}
