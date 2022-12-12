package edu.hm.hafner.analysis.parser;

import java.io.File;
import java.io.IOException;
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
public class SimulinkCheckParser extends IssuePropertiesParser{

    private static final long serialVersionUID = -8099258658775128275L;

    @Override
    @SuppressWarnings({"PMD.NcssCount", "PMD.CyclomaticComplexity", "PMD.CognitiveComplexity", "PMD.NPathComplexity" })
    public Report parse(ReaderFactory readerFactory) throws ParsingException {
        String name = readerFactory.getFileName();
        File file = new File(name);

        try (IssueBuilder issueBuilder = new IssueBuilder()) {
            //Parse html document using Jsoup
            Document document = Jsoup.parse(file, "utf-8");
            Elements systemElement = document.select("div.ReportContent");
            String system = systemElement.first().id();
            Report report = new Report();

            //Select Warning items from the whole html document
            parseIssue(report, document, issueBuilder, system, "div.WarningCheck");
            //Select Failed items from the whole html document
            parseIssue(report, document, issueBuilder, system, "div.FailedCheck");
            //Select Not Run items from the whole html document
            parseIssue(report, document, issueBuilder, system, "div.NotRunCheck");
            //Select Incomplete items from the whole html document
            parseIssue(report, document, issueBuilder, system, "div.IncompleteCheck");

            return report;
        }
        catch (IOException e) {
            throw new ParsingException(e);
        }
    }
    public void parseIssue(Report report, Document document, IssueBuilder issueBuilder, String system, String check) {
        Elements heading;
        String issueTxt;
        String headingElement;
        String[] headingSplit;
        int strLen;
        String module;

        Elements issueElements = document.select(check);
        for (Element w : issueElements) {
            heading = w.select("span.CheckHeading");
            issueTxt = heading.text();
            setSeverity(check, issueBuilder);

            headingElement = heading.first().id();
            headingSplit = headingElement.split("\\.");
            strLen = headingSplit.length;

            if (issueTxt.contains("SW")) {
                String[] fileName = issueTxt.split(":", 2);
                module = headingSplit[strLen - 1];
                issueBuilder.setModuleName(fileName[0] + "." + module);
                issueBuilder.setDescription(fileName[1]);
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
    public void setSeverity(String check, IssueBuilder issueBuilder ) {

        if (check.equalsIgnoreCase("div.WarningCheck")) {
            issueBuilder.setSeverity(Severity.WARNING_NORMAL);
            issueBuilder.setCategory("Warning");
        }
        else if (check.equalsIgnoreCase("div.FailedCheck")) {
            issueBuilder.setSeverity(Severity.ERROR);
            issueBuilder.setCategory("Failed");
        }
        else if (check.equalsIgnoreCase("div.NotRunCheck")) {
            issueBuilder.setSeverity(Severity.WARNING_HIGH);
            issueBuilder.setCategory("Not Run");
        }
        else if (check.equalsIgnoreCase("div.IncompleteCheck")) {
            issueBuilder.setSeverity(Severity.WARNING_LOW);
            issueBuilder.setCategory("Incomplete");
        }
    }
}
