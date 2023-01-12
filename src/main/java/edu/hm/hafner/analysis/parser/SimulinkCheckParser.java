package edu.hm.hafner.analysis.parser;

import java.io.InputStream;
import java.io.Reader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.input.ReaderInputStream;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;

/**
 * A parser for Simulink Check tool by Mathworks results.
 * Used for .html report files.
 *
 * @author Eva Habeeb
 */
public class SimulinkCheckParser extends IssuePropertiesParser {

    private static final long serialVersionUID = -8099258658775128275L;
    static final String WARNING = "div.WarningCheck";
    static final String FAILED = "div.FailedCheck";
    static final String NOTRUN = "div.NotRunCheck";
    static final String INCOMPLETE = "div.IncompleteCheck";

    @Override
    public Report parse(final ReaderFactory readerFactory) throws ParsingException {
        try (
                IssueBuilder issueBuilder = new IssueBuilder();
                Reader reader = readerFactory.create();
                InputStream targetStream = new ReaderInputStream(reader, readerFactory.getCharset());
        ) {
            //Parse html document using Jsoup
            Document document = Jsoup.parse(targetStream, String.valueOf(readerFactory.getCharset()), "");

            Elements systemElement = document.select("div.ReportContent");
            String system = systemElement.first().id();
            Report report = new Report();

            //Select Warning items from the whole html document
            parseIssue(report, document, issueBuilder, system, WARNING);
            //Select Failed items from the whole html document
            parseIssue(report, document, issueBuilder, system, FAILED);
            //Select Not Run items from the whole html document
            parseIssue(report, document, issueBuilder, system, NOTRUN);
            //Select Incomplete items from the whole html document
            parseIssue(report, document, issueBuilder, system, INCOMPLETE);

            if (report.isEmpty()) throw new Exception("Empty File");
            return report;
        }
        catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private void parseIssue(final Report report, final Document document, final IssueBuilder issueBuilder, final String system, final String check) {

        Elements issueElements = document.select(check);
        for (Element w : issueElements) {
            Elements heading = w.select("span.CheckHeading");
            String issueTxt = heading.text();
            setSeverity(check, issueBuilder);

            String headingElement = heading.first().id();
            String[] headingSplit = headingElement.split("\\.");
            int strLen;
            strLen = headingSplit.length;
            String module;
            Pattern pattern = Pattern.compile("^(SW[0-9]*-[0-9]*)([^\\w]*)(.*)");
            Matcher matcher = pattern.matcher(issueTxt);
            boolean matches = matcher.matches();

            if (matches) {
                module = headingSplit[strLen - 1];
                issueBuilder.setModuleName(matcher.group(1) + "." + module);
                issueBuilder.setDescription(matcher.group(3));
            }
            else if (w.parent().id().contains("SW")) {
                String parentTxt = w.parent().id();
                int i = parentTxt.indexOf("SW");
                parentTxt = parentTxt.substring(i);
                module = headingSplit[strLen - 1];
                issueBuilder.setModuleName(parentTxt + "." + module);
                issueBuilder.setDescription(issueTxt);
            }
            else {
                module = headingSplit[strLen - 1] + "." + headingSplit[strLen - 2];
                issueBuilder.setModuleName(module);
                issueBuilder.setDescription(issueTxt);
                issueBuilder.setAdditionalProperties(w.parent().id());
            }
            issueBuilder.setFileName(system);
            report.add(issueBuilder.build());
        }
    }

    private void setSeverity(final String check, final IssueBuilder issueBuilder) {

        if ("div.WarningCheck".equals(check)) {
            issueBuilder.setSeverity(Severity.WARNING_NORMAL);
            issueBuilder.setCategory("Warning");
        }
        else if ("div.FailedCheck".equals(check)) {
            issueBuilder.setSeverity(Severity.ERROR);
            issueBuilder.setCategory("Failed");
        }
        else if ("div.NotRunCheck".equals(check)) {
            issueBuilder.setSeverity(Severity.WARNING_HIGH);
            issueBuilder.setCategory("Not Run");
        }
        else if ("div.IncompleteCheck".equals(check)) {
            issueBuilder.setSeverity(Severity.WARNING_LOW);
            issueBuilder.setCategory("Incomplete");
        }
    }
}
