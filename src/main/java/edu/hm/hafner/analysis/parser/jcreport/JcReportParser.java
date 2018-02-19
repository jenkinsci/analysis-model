package edu.hm.hafner.analysis.parser.jcreport;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.Reader;
import java.util.function.Function;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.ParsingCanceledException;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.SecureDigester;

/**
 * JcReportParser-Class. This class parses from the jcReport.xml and creates warnings from its content.
 *
 * @author Johann Vierthaler, johann.vierthaler@web.de
 */
public class JcReportParser extends AbstractParser<Issue> {
    private static final long serialVersionUID = -1302787609831475403L;

    @Override
    public Issues<Issue> parse(@Nonnull final Reader reader, final Function<String, String> preProcessor)
            throws ParsingCanceledException {
        Report report = createReport(reader);
        Issues<Issue> warnings = new Issues<>();
        for (int i = 0; i < report.getFiles().size(); i++) {
            File file = report.getFiles().get(i);

            for (int j = 0; j < file.getItems().size(); j++) {
                Item item = file.getItems().get(j);
                IssueBuilder builder = new IssueBuilder().setFileName(file.getName())
                        .setLineStart(parseInt(item.getLine()))
                        .setColumnStart(parseInt(item.getColumn()))
                        .setColumnEnd(parseInt(item.getEndcolumn()))
                        .setCategory(item.getFindingtype())
                        .setPackageName(file.getPackageName())
                        .setMessage(item.getMessage())
                        .setPriority(getPriority(item.getSeverity()));

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
    private Priority getPriority(final String issueLevel) {
        if (StringUtils.isEmpty(issueLevel)) {
            return Priority.HIGH;
        }

        if (issueLevel.contains("CriticalError")) {
            return Priority.HIGH;
        }
        else if (issueLevel.contains("Error")) {
            return Priority.HIGH;
        }
        else if (issueLevel.contains("CriticalWarning")) {
            return Priority.HIGH;
        }
        else if (issueLevel.contains("Warning")) {
            return Priority.NORMAL;
        }
        else {
            return Priority.LOW;
        }
    }

    /**
     * Creates a Report-Object out of the content within the JcReport.xml.
     *
     * @param source
     *         the Reader-object that is the source to build the Report-Object.
     *
     * @return the finished Report-Object that creates the Warnings.
     * @throws ParsingException
     *         due to digester.parse(new InputSource(source))
     */
    public Report createReport(final Reader source) throws ParsingException {
        try {
            SecureDigester digester = new SecureDigester(JcReportParser.class);

            String report = "report";
            digester.addObjectCreate(report, Report.class);
            digester.addSetProperties(report);

            String file = "report/file";
            digester.addObjectCreate(file, File.class);
            digester.addSetProperties(file,  "package", "packageName");
            digester.addSetProperties(file,  "src-dir", "srcdir");
            digester.addSetProperties(file);
            digester.addSetNext(file, "addFile", File.class.getName());

            String item = "report/file/item";
            digester.addObjectCreate(item, Item.class);
            digester.addSetProperties(item);
            digester.addSetProperties(item,  "finding-type", "findingtype");
            digester.addSetProperties(item,  "end-line", "endline");
            digester.addSetProperties(item,  "end-column", "endcolumn");
            digester.addSetNext(item, "addItem", Item.class.getName());

            return digester.parse(new InputSource(source));
        }
        catch (IOException | SAXException e) {
            throw new ParsingException(e);
        }
        finally {
            IOUtils.closeQuietly(source);
        }
    }
}
