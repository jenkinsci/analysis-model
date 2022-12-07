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
            Elements warningsElements = document.select("div.WarningCheck");
            for (Element w : warningsElements) {
                Elements heading = w.select("span.CheckHeading");
                String warningTxt = heading.text();
                issueBuilder.setSeverity(Severity.WARNING_NORMAL);
                issueBuilder.setCategory("Warning");

                if (warningTxt.contains("SW")) {
                    String[] fileName = warningTxt.split(":", 2);
                    issueBuilder.setModuleName(fileName[0]);
                    issueBuilder.setDescription(fileName[1]);
                }
                else if (w.parent().id().contains("SW")) {
                    String parentTxt = w.parent().id();
                    int i = parentTxt.indexOf("SW");
                    parentTxt = parentTxt.substring(i);

                    issueBuilder.setModuleName(parentTxt);
                    issueBuilder.setDescription(warningTxt);
                }
                else {
                    issueBuilder.setDescription(warningTxt);
                    issueBuilder.setAdditionalProperties(w.parent().id());
                }
                issueBuilder.setFileName(system);
                report.add(issueBuilder.build());
            }

            //Select Failed items from the whole html document
            Elements failedElements = document.select("div.FailedCheck");
            for (Element f : failedElements) {
                Elements heading = f.select("span.CheckHeading");
                String failedTxt = heading.text();
                issueBuilder.setSeverity(Severity.ERROR);
                issueBuilder.setCategory("Failed");

                if (failedTxt.contains("SW")) {
                    String[] fileName = failedTxt.split(":", 2);
                    issueBuilder.setModuleName(fileName[0]);
                    issueBuilder.setDescription(fileName[1]);
                }
                else if (f.parent().id().contains("SW")) {
                    String parentTxt = f.parent().id();
                    int i = parentTxt.indexOf("SW");
                    parentTxt = parentTxt.substring(i);

                    issueBuilder.setModuleName(parentTxt);
                    issueBuilder.setDescription(failedTxt);
                }
                else {
                    issueBuilder.setDescription(failedTxt);
                    issueBuilder.setAdditionalProperties(f.parent().id());
                }
                issueBuilder.setFileName(system);
                report.add(issueBuilder.build());
            }

            //Select Not Run items from the whole html document
            Elements notRunElements = document.select("div.NotRunCheck");
            for (Element n : notRunElements) {
                Elements heading = n.select("span.CheckHeading");
                String notRunTxt = heading.text();
                issueBuilder.setSeverity(Severity.WARNING_HIGH);
                issueBuilder.setCategory("Not Run");

                if (notRunTxt.contains("SW")) {
                    String[] fileName = notRunTxt.split(":", 2);
                    issueBuilder.setModuleName(fileName[0]);
                    issueBuilder.setDescription(fileName[1]);
                }
                else if (n.parent().id().contains("SW")) {
                    String parentTxt = n.parent().id();
                    int i = parentTxt.indexOf("SW");
                    parentTxt = parentTxt.substring(i);

                    issueBuilder.setModuleName(parentTxt);
                    issueBuilder.setDescription(notRunTxt);
                }
                else {
                    issueBuilder.setDescription(notRunTxt);
                    issueBuilder.setAdditionalProperties(n.parent().id());
                }
                issueBuilder.setFileName(system);
                report.add(issueBuilder.build());
            }

            //Select Incomplete items from the whole html document
            Elements incompleteElements = document.select("div.IncompleteCheck");
            for (Element p : incompleteElements) {
                Elements heading = p.select("span.CheckHeading");
                String incompleteTxt = heading.text();
                issueBuilder.setSeverity(Severity.WARNING_LOW);
                issueBuilder.setCategory("Incomplete");

                if (incompleteTxt.contains("SW")) {
                    String[] fileName = incompleteTxt.split(":", 2);
                    issueBuilder.setModuleName(fileName[0]);
                    issueBuilder.setDescription(fileName[1]);
                }
                else if (p.parent().id().contains("SW")) {
                    String parentTxt = p.parent().id();
                    int i = parentTxt.indexOf("SW");
                    parentTxt = parentTxt.substring(i);

                    issueBuilder.setModuleName(parentTxt);
                    issueBuilder.setDescription(incompleteTxt);
                }
                else {
                    issueBuilder.setDescription(incompleteTxt);
                    issueBuilder.setAdditionalProperties(p.parent().id());
                }
                issueBuilder.setFileName(system);
                report.add(issueBuilder.build());
            }
            return report;
        } // end try
        catch (IOException e) {
            throw new ParsingException(e);
        }
    }
}
