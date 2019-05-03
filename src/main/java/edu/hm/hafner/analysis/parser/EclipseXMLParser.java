package edu.hm.hafner.analysis.parser;

import java.util.Optional;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import edu.hm.hafner.analysis.Categories;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.util.XmlElementUtil;

/**
 * Parser for Eclipse Compiler output in XML format.
 * 
 * @author Jason Faust
 */
public class EclipseXMLParser extends IssueParser {

    /** {@value} category. */
    public static final String COMPLIANCE = "Compliance";
    /** {@value} category. */
    public static final String MODULE = "Module";
    /** {@value} category. */
    public static final String RESTRICTION = "Restriction";
    /** {@value} category. */
    public static final String NLS = "NLS";
    /** {@value} category. */
    public static final String UNCHECKED_RAW = "Unchecked Raw";
    /** {@value} category. */
    public static final String UNNECESSARY_CODE = "Unnecessary Code";
    /** {@value} category. */
    public static final String NAME_SHADOWING_CONFLICT = "Name Shadowing Conflict";
    /** {@value} category. */
    public static final String POTENTIAL_PROGRAMMING_PROBLEM = "Potential Programming Problem";
    /** {@value} category. */
    public static final String CODE_STYLE = "Code Style";
    /** {@value} category. */
    public static final String INTERNAL = "Internal";
    /** {@value} category. */
    public static final String MEMBER = "Member";
    /** {@value} category. */
    public static final String TYPE = "Type";
    /** {@value} category. */
    public static final String IMPORT = "Import";
    /** {@value} category. */
    public static final String SYNTAX = "Syntax";
    /** {@value} category. */
    public static final String BUILDPATH = "Buildpath";

    private static final long serialVersionUID = 1L;

    @Override
    public boolean accepts(final ReaderFactory readerFactory) {
        return isXmlFile(readerFactory);
    }

    @Override
    public Report parse(final ReaderFactory readerFactory) throws ParsingException {
        try {
            Document doc = readerFactory.readDocument();

            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xPath = xPathFactory.newXPath();
            XPathExpression sourcePath = xPath.compile("/compiler/sources/source[problems]");
            XPathExpression fileNamePath = xPath.compile("./@path");
            XPathExpression problemsPath = xPath.compile("problems/problem");

            IssueBuilder issueBuilder = new IssueBuilder();
            Report report = new Report();

            NodeList sources = (NodeList)sourcePath.evaluate(doc, XPathConstants.NODESET);
            for (Element source : XmlElementUtil.nodeListToList(sources)) {
                String fileName = fileNamePath.evaluate(source);
                issueBuilder.setFileName(fileName);

                NodeList problems = (NodeList)problemsPath.evaluate(source, XPathConstants.NODESET);
                for (Element problem : XmlElementUtil.nodeListToList(problems)) {

                    issueBuilder.guessSeverity(extractSeverity(problem))
                            .setLineStart(extractLineStart(problem))
                            .setMessage(extractMessage(problem))
                            .setCategory(decodeCategory(extractCategoryId(problem)))
                            // Use columns to make issue 'unique', range isn't useful for counting in the physical
                            // source.
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
     * These categories were taken from the ECJ source code. From March 25th, 2019, Ver. 3.18.
     * 
     * @param categoryId
     *     eclipse generated category id.
     * @return decoded category, or empty string.
     * @see https://github.com/eclipse/eclipse.jdt.core/blob/master/org.eclipse.jdt.core/compiler/org/eclipse/jdt/core/compiler/CategorizedProblem.java
     */
    private String decodeCategory(String categoryId) {
        switch (categoryId) {
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
            default:
                return "";
        }
    }

    private String extractMessage(Element problem) {
        // XPath is "message/@value"
        return XmlElementUtil.nodeListToList(problem.getChildNodes())
                .stream()
                .filter(e -> "message".equals(e.getNodeName()))
                .findFirst()
                .map(e -> e.getAttribute("value"))
                .orElseGet(() -> "");
    }

    private String extractSeverity(Element problem) {
        // XPath is "./@severity"
        return problem.getAttribute("severity");
    }

    private String extractLineStart(Element problem) {
        // XPath is "./@line"
        return problem.getAttribute("line");
    }

    private String extractColumnRange(Element problem) {
        // XPath is "source_context/@sourceStart" and "source_context/@sourceEnd"
        Optional<Element> ctx = XmlElementUtil.nodeListToList(problem.getChildNodes())
                .stream()
                .filter(e -> "source_context".equals(e.getNodeName()))
                .findFirst();

        StringBuilder range = new StringBuilder();
        ctx.map(e -> e.getAttribute("sourceStart")).ifPresent(range::append);
        range.append('-');
        ctx.map(e -> e.getAttribute("sourceEnd")).ifPresent(range::append);

        return range.toString();
    }

    private String extractCategoryId(Element problem) {
        // XPath is "./@categoryID"
        return problem.getAttribute("categoryID");
    }
}
