package edu.hm.hafner.analysis.parser.pvsstudio;

import java.util.List;

import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingCanceledException;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;

/**
 * A parser for the PVS-Studio static analyzer.
 *
 * @author PVS-Studio Team
 */
public class PVSStudioParser extends IssueParser {
    private static final long serialVersionUID = -7777775729854832128L;

    private static Severity getSeverity(final String level) {
        if ("1".equals(level)) {
            return Severity.WARNING_HIGH;
        }
        else if ("2".equals(level)) {
            return Severity.WARNING_NORMAL;
        }
        else if ("3".equals(level)) {
            return Severity.WARNING_LOW;
        }
        else {
            return Severity.ERROR;
        }
    }

    @Override
    public Report parse(final ReaderFactory readerFactory) throws ParsingException, ParsingCanceledException {
        try (IssueBuilder issueBuilder = new IssueBuilder()) {
            List<PlogMessage> plogMessages = PlogMessage.getMessagesFromReport(readerFactory);

            Report report = new Report();

            for (PlogMessage plogMessage : plogMessages) {
                issueBuilder.setFileName(plogMessage.getFilePath());

                issueBuilder.setSeverity(getSeverity(plogMessage.getLevel()));
                issueBuilder.setMessage(plogMessage.toString());
                issueBuilder.setCategory(plogMessage.getType());

                issueBuilder.setType(AnalyzerType.fromErrorCode(plogMessage.getType()).getMessage());

                issueBuilder.setLineStart(plogMessage.getLine());

                report.add(issueBuilder.buildAndClean());
            }

            return report;
        }
    }
}

