package edu.hm.hafner.analysis.parser.jcreport;

import java.io.IOException;
import java.io.Reader;

import org.apache.commons.lang3.StringUtils;
import org.xml.sax.SAXException;

import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.SecureDigester;
import edu.hm.hafner.analysis.Severity;

/**
 * JcReportParser-Class. This class parses from the jcReport.xml and creates warnings from its content.
 *
 * @author Johann Vierthaler, johann.vierthaler@web.de
 */
public class JcReportParser extends IssueParser {
    private static final long serialVersionUID = -1302787609831475403L;

    @Override
    public Report parse(final ReaderFactory reader) {
        edu.hm.hafner.analysis.parser.jcreport.Report report = createReport(reader);
        Report warnings = new Report();
        for (int i = 0; i < report.getFiles().size(); i++) {
            File file = report.getFiles().get(i);

            for (int j = 0; j < file.getItems().size(); j++) {
                Item item = file.getItems().get(j);
                IssueBuilder builder = new IssueBuilder().setFileName(file.getName())
                        .setLineStart(item.getLine())
                        .setColumnStart(item.getColumn())
                        .setColumnEnd(item.getEndcolumn())
                        .setCategory(item.getFindingtype())
                        .setPackageName(file.getPackageName())
                        .setMessage(item.getMessage())
                        .setSeverity(getPriority(item.getSeverity()));

                warnings.add(builder.build());
            }
        }
        return warnings;
    }

    /**
     * The severity-level parsed from the JcReport will be matched with a priority.
     *
     * @param issueLevel
     *         the severity-level parsed from the JcReport.
     *
     * @return the priority-enum matching with the issueLevel.
     */
    private Severity getPriority(final String issueLevel) {
        if (StringUtils.isEmpty(issueLevel)) {
            return Severity.WARNING_HIGH;
        }

        if (issueLevel.contains("Error") || issueLevel.contains("Critical")) {
            return Severity.WARNING_HIGH;
        }
        if (issueLevel.contains("Warning")) {
            return Severity.WARNING_NORMAL;
        }
        return Severity.WARNING_LOW;
    }

    /**
     * Creates a Report-Object out of the content within the JcReport.xml.
     *
     * @param readerFactory
     *         the Reader-object that is the source to build the Report-Object.
     *
     * @return the finished Report-Object that creates the Warnings.
     * @throws ParsingException
     *         due to digester.parse(new InputSource(source))
     */
    public edu.hm.hafner.analysis.parser.jcreport.Report createReport(final ReaderFactory readerFactory)
            throws ParsingException {
        SecureDigester digester = new SecureDigester(JcReportParser.class);

        String report = "report";
        digester.addObjectCreate(report, edu.hm.hafner.analysis.parser.jcreport.Report.class);
        digester.addSetProperties(report);

        String file = "report/file";
        digester.addObjectCreate(file, File.class);
        digester.addSetProperties(file, "package", "packageName");
        digester.addSetProperties(file, "src-dir", "srcdir");
        digester.addSetProperties(file);
        digester.addSetNext(file, "addFile", File.class.getName());

        String item = "report/file/item";
        digester.addObjectCreate(item, Item.class);
        digester.addSetProperties(item);
        digester.addSetProperties(item, "finding-type", "findingtype");
        digester.addSetProperties(item, "end-line", "endline");
        digester.addSetProperties(item, "end-column", "endcolumn");
        digester.addSetNext(item, "addItem", Item.class.getName());

        try (Reader reader = readerFactory.create()) {
            return digester.parse(reader);
        }
        catch (IOException | SAXException e) {
            throw new ParsingException(e);
        }
    }
}
