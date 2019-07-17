package edu.hm.hafner.analysis.parser.pvsstudio;

import java.io.File;

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

    private static Severity getSeverity(final String level)
    {
        if ("1".equals(level))
        {
            return Severity.WARNING_HIGH;
        }
        else if ("2".equals(level))
        {
            return Severity.WARNING_NORMAL;
        }
        else if ("3".equals(level))
        {
            return Severity.WARNING_LOW;
        }
        else
        {
            return Severity.ERROR;
        }
    }

    private String getAnalyzerMessage(final String errorCode)
    {
        AnalyzerType analyzerType = AnalyzerType.getAnalyzerType(errorCode);

        switch(analyzerType) {
            case GENERAL:
                return "GENERAL Analysis";
            case OPTIMIZATION:
                return "Micro-OPTIMIZATION";
            case CUSTOMER_SPECIFIC:
                return "Specific Requests";
            case VIVA_64:
                return "64-bit";
            case MISRA:
                return "Misra";
            default:
                return "Unknown";
        }
    }

    @Override
    public Report parse(final ReaderFactory readerFactory) throws ParsingException, ParsingCanceledException {

        List<PlogMessage> result = PlogMessage.getMessagesFromReport(
                new File(readerFactory.getFileName()));

        Report report = new Report();

        for (PlogMessage pMessage : result) {
            IssueBuilder builder = new IssueBuilder();
            builder.setFileName(pMessage.getFilePath());

            builder.setSeverity(getSeverity(pMessage.getLevel()));
            builder.setMessage(pMessage.toString());
            builder.setCategory(pMessage.getType());
            builder.setType(getAnalyzerMessage(pMessage.getType()));

            builder.setLineStart(pMessage.getLine());

            report.add(builder.build());
        }

        return report;
    }
}

