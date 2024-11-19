package edu.hm.hafner.analysis.parser;

import java.io.Serial;
import java.util.Optional;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import edu.hm.hafner.analysis.Categories;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.util.XmlElementUtil;

/**
 * Parser for Eclipse Compiler output in XML format.
 *
 * @author Jason Faust
 */
public class EclipseXMLParser extends IssueParser {
    static final String PREVIEW_RELATED = "Preview Related";
    static final String COMPLIANCE = "Compliance";
    static final String MODULE = "Module";
    static final String RESTRICTION = "Restriction";
    static final String NLS = "NLS";
    static final String UNCHECKED_RAW = "Unchecked Raw";
    static final String UNNECESSARY_CODE = "Unnecessary Code";
    static final String NAME_SHADOWING_CONFLICT = "Name Shadowing Conflict";
    static final String POTENTIAL_PROGRAMMING_PROBLEM = "Potential Programming Problem";
    static final String CODE_STYLE = "Code Style";
    static final String INTERNAL = "Internal";
    static final String MEMBER = "Member";
    static final String TYPE = "Type";
    static final String IMPORT = "Import";
    static final String SYNTAX = "Syntax";
    static final String BUILDPATH = "Buildpath";
    static final String UNSPECIFIED = "Unspecified";

    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    public boolean accepts(final ReaderFactory readerFactory) {
        return isXmlFile(readerFactory);
    }

    @Override
    public Report parse(final ReaderFactory readerFactory) throws ParsingException {
        try (var issueBuilder = new IssueBuilder()) {
            var doc = readerFactory.readDocument();

            var xPathFactory = XPathFactory.newInstance();
            var xPath = xPathFactory.newXPath();
            var sourcePath = xPath.compile("/compiler/sources/source[problems]");
            var fileNamePath = xPath.compile("./@path");
            var problemsPath = xPath.compile("problems/problem");

            var report = new Report();

            var sources = (NodeList) sourcePath.evaluate(doc, XPathConstants.NODESET);
            for (Element source : XmlElementUtil.nodeListToList(sources)) {
                var fileName = fileNamePath.evaluate(source);
                issueBuilder.setFileName(fileName);

                var problems = (NodeList) problemsPath.evaluate(source, XPathConstants.NODESET);
                for (Element problem : XmlElementUtil.nodeListToList(problems)) {
                    issueBuilder.guessSeverity(extractSeverity(problem))
                            .setLineStart(extractLineStart(problem))
                            .setMessage(extractMessage(problem))
                            .setCategory(decodeCategory(extractCategoryId(problem)))
                            .setAdditionalProperties(extractColumnRange(problem));

                    report.add(issueBuilder.build());
                }
            }

            return report;
        }
        catch (XPathExpressionException e) {
            throw new ParsingException(e);
        }
    }

    /**
     * These categories were taken from the ECJ source code. From January 15th, 2020, Ver. 3.20.
     *
     * @param categoryId
     *     eclipse generated category id.
     * @return decoded category, or empty string.
     * @see <a href="https://github.com/eclipse/eclipse.jdt.core/blob/master/org.eclipse.jdt.core/compiler/org/eclipse/jdt/core/compiler/CategorizedProblem.java">Eclipse Source Code</a>
     */
    @SuppressWarnings("PMD.CyclomaticComplexity")
    private String decodeCategory(final String categoryId) {
        switch (categoryId) {
            case "0":
                return UNSPECIFIED;
            case "10":
                return BUILDPATH;
            case "20":
                return SYNTAX;
            case "30":
                return IMPORT;
            case "40":
                return TYPE;
            case "50":
                return MEMBER;
            case "60":
                return INTERNAL;
            case "70":
                return Categories.JAVADOC;
            case "80":
                return CODE_STYLE;
            case "90":
                return POTENTIAL_PROGRAMMING_PROBLEM;
            case "100":
                return NAME_SHADOWING_CONFLICT;
            case "110":
                return Categories.DEPRECATION;
            case "120":
                return UNNECESSARY_CODE;
            case "130":
                return UNCHECKED_RAW;
            case "140":
                return NLS;
            case "150":
                return RESTRICTION;
            case "160":
                return MODULE;
            case "170":
                return COMPLIANCE;
            case "180":
                return PREVIEW_RELATED;
            default:
                return Categories.OTHER;
        }
    }

    private String extractMessage(final Element problem) {
        // XPath is "message/@value"
        return XmlElementUtil.nodeListToList(problem.getChildNodes())
                .stream()
                .filter(e -> "message".equals(e.getNodeName()))
                .findFirst()
                .map(e -> e.getAttribute("value"))
                .orElse("");
    }

    private String extractSeverity(final Element problem) {
        // XPath is "./@severity"
        return problem.getAttribute("severity");
    }

    private String extractLineStart(final Element problem) {
        // XPath is "./@line"
        return problem.getAttribute("line");
    }

    /*
     * Use columns to make issue 'unique', range isn't useful for counting in the physical source.
     */
    private String extractColumnRange(final Element problem) {
        // XPath is "source_context/@sourceStart" and "source_context/@sourceEnd"
        Optional<Element> ctx = XmlElementUtil.nodeListToList(problem.getChildNodes())
                .stream()
                .filter(e -> "source_context".equals(e.getNodeName()))
                .findFirst();

        var range = new StringBuilder();
        ctx.map(e -> e.getAttribute("sourceStart")).ifPresent(range::append);
        range.append('-');
        ctx.map(e -> e.getAttribute("sourceEnd")).ifPresent(range::append);

        return range.toString();
    }

    private String extractCategoryId(final Element problem) {
        // XPath is "./@categoryID"
        return problem.getAttribute("categoryID");
    }
}
