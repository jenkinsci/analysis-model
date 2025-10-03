package edu.hm.hafner.analysis.parser;

import org.apache.commons.io.input.ReaderInputStream;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;

import java.io.IOException;
import java.io.Serial;
import java.util.regex.Pattern;

/**
 * A parser for Simulink Check tool by Mathworks results. Used for HTML report files.
 *
 * @author Eva Habeeb
 */
public class SimulinkCheckParser extends IssueParser {
    @Serial
    private static final long serialVersionUID = -8099258658775128275L;
    private static final String WARNING = "div.WarningCheck";
    private static final String FAILED = "div.FailedCheck";
    private static final String NOT_RUN = "div.NotRunCheck";
    private static final String INCOMPLETE = "div.IncompleteCheck";
    private static final String EMPTY_BASE_URI = "";
    private static final String REPORT_CONTENT = "div.ReportContent";
    private static final String MODEL_NAME_SELECTOR = "b:contains(Model Advisor Report - ) > font";
    private static final Pattern TEXT_PATTERN = Pattern.compile("^(SW[0-9]*-[0-9]*)(\\W*)(.*)");
    private static final String SW_PREFIX = "SW";

    @Override
    public Report parseReport(final ReaderFactory readerFactory) throws ParsingException {
        try (var issueBuilder = new IssueBuilder();
                var reader = readerFactory.create();
                var targetStream = ReaderInputStream.builder().setReader(reader).setCharset(readerFactory.getCharset()).get()) {
            var document = Jsoup.parse(targetStream, readerFactory.getCharset().name(), EMPTY_BASE_URI);

            var systemElements = document.select(REPORT_CONTENT);
            var systemElement = systemElements.first();
            if (systemElement == null) {
                throw new ParsingException(readerFactory, "No system element found");
            }
            var report = new Report();

            var modelNameElement = systemElement.selectFirst(MODEL_NAME_SELECTOR);
            var system = (modelNameElement == null) ? systemElement.id() : modelNameElement.text();
            parseIssues(report, document, issueBuilder, system, WARNING);
            parseIssues(report, document, issueBuilder, system, FAILED);
            parseIssues(report, document, issueBuilder, system, NOT_RUN);
            parseIssues(report, document, issueBuilder, system, INCOMPLETE);

            return report;
        }
        catch (IOException exception) {
            throw new ParsingException(exception, readerFactory);
        }
    }

    private void parseIssues(final Report report, final Document document, final IssueBuilder issueBuilder,
            final String system, final String check) {
        setSeverity(check, issueBuilder);

        for (Element element : document.select(check)) {
            var headings = element.select("span.CheckHeading");
            var heading = headings.first();
            if (heading != null) {
                parseHeading(report, issueBuilder, system, element, headings, heading.id());
            }
        }
    }

    private static void parseHeading(final Report report, final IssueBuilder issueBuilder, final String system,
            final Element element, final Elements headings, final String headingElement) {
        var headingSplit = headingElement.split("\\.");
        if (headingSplit.length > 0) {
            var issueTxt = headings.text();
            var textMatcher = TEXT_PATTERN.matcher(issueTxt);

            if (textMatcher.matches()) {
                issueBuilder.setModuleName(textMatcher.group(1) + "." + headingSplit[headingSplit.length - 1]);
                issueBuilder.setDescription(textMatcher.group(3));
            }
            else {
                var parent = element.parent();
                if (parent != null) {
                    parseParent(issueBuilder, headingSplit, issueTxt, parent);
                }
            }
            issueBuilder.setFileName(system);
            report.add(issueBuilder.build());
        }
    }

    private static void parseParent(final IssueBuilder issueBuilder, final String[] headingSplit, final String issueTxt,
            final Element parent) {
        var parentId = parent.id();
        int prefix = parentId.indexOf(SW_PREFIX);
        if (prefix >= 0) {
            issueBuilder.setModuleName(parentId.substring(prefix) + "." + headingSplit[headingSplit.length - 1]);
        }
        else {
            issueBuilder.setModuleName(
                    headingSplit[headingSplit.length - 1] + "." + headingSplit[headingSplit.length - 2]);
        }
        issueBuilder.setDescription(issueTxt);
    }

    private void setSeverity(final String check, final IssueBuilder issueBuilder) {
        if (FAILED.equals(check)) {
            issueBuilder.setSeverity(Severity.ERROR).setCategory("Failed");
        }
        else if (NOT_RUN.equals(check)) {
            issueBuilder.setSeverity(Severity.WARNING_HIGH).setCategory("Not Run");
        }
        else if (INCOMPLETE.equals(check)) {
            issueBuilder.setSeverity(Severity.WARNING_LOW).setCategory("Incomplete");
        }
        else {
            issueBuilder.setSeverity(Severity.WARNING_NORMAL).setCategory("Warning");
        }
    }
}
