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

    private static Severity getSeverity(String level)
    {
        if (level.equals("1"))
            return Severity.WARNING_HIGH;
        else if (level.equals("2"))
            return Severity.WARNING_NORMAL;
        else if (level.equals("3"))
            return Severity.WARNING_LOW;
        else
            return Severity.ERROR;
    }

    private String getAnalyzerMessage(String errorCode)
    {
        AnalyzerType analyzerType = AnalyzerType.GetAnalyzerType(errorCode);

        switch(analyzerType){
            case General:
                return "General Analysis";
            case Optimization:
                return "Micro-Optimization";
            case CustomerSpecific:
                return "Specific Requests";
            case Viva64:
                return "64-bit";
            case MISRA:
                return "Misra";
            default:
                return "Unknown";
        }
    }

    @Override
    public Report parse(final ReaderFactory readerFactory) throws ParsingException, ParsingCanceledException {

        List<PlogMessage> result = PlogMessage.GetMessagesFromReport(
                new File(readerFactory.getFileName()));

        Report report = new Report();

        for(PlogMessage pMessage : result)
        {
            IssueBuilder builder = new IssueBuilder();
            builder.setFileName(pMessage.file);

            builder.setSeverity(getSeverity(pMessage.level));
            builder.setMessage(pMessage.message);
            builder.setCategory(pMessage.errorCode);
            builder.setType(getAnalyzerMessage(pMessage.errorCode));

            builder.setLineStart(pMessage.lineNumber);

            report.add(builder.build());
        }

        return report;
    }
}

