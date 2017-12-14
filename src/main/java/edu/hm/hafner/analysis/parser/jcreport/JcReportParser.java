package edu.hm.hafner.analysis.parser.jcreport;

import java.io.IOException;
import java.io.Reader;

import org.apache.commons.digester3.Digester;
import org.apache.commons.digester3.binder.DigesterLoader;
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

/**
 * JcReportParser-Class. This class parses from the jcReport.xml and creates warnings from its content.
 *
 * @author Johann Vierthaler, johann.vierthaler@web.de
 */
public class JcReportParser extends AbstractParser {
    private static final long serialVersionUID = -1302787609831475403L;

    /**
     * Creates a new instance of {@link JcReportParser}.
     */
    public JcReportParser() {
        super();
    }

    @Override
    public Issues<Issue> parse(final Reader reader, final IssueBuilder builder) throws ParsingCanceledException {
        Report report = createReport(reader);
        Issues<Issue> warnings = new Issues<>();
        for (int i = 0; i < report.getFiles().size(); i++) {
            File file = report.getFiles().get(i);

            for (int j = 0; j < file.getItems().size(); j++) {
                Item item = file.getItems().get(j);
                builder.setFileName(file.getName())
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
            DigesterLoader digesterLoader = DigesterLoader.newLoader(new JcReportModule());
            digesterLoader.setClassLoader(JcReportModule.class.getClassLoader());

            Digester digester = digesterLoader.newDigester();
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
