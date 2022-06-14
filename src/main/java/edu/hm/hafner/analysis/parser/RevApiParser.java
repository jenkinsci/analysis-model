package edu.hm.hafner.analysis.parser;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.ParsingCanceledException;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.Report;

public class RevApiParser extends JsonIssueParser {

    private static final long serialVersionUID = 5274884556966380576L;

    @Override
    public Report parse(final ReaderFactory readerFactory) throws ParsingException, ParsingCanceledException {
        return super.parse(readerFactory);
    }

    @Override
    public Report parseFile(final ReaderFactory readerFactory) throws ParsingException, ParsingCanceledException {
        return super.parseFile(readerFactory);
    }


    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        System.out.print(jsonReport);
        System.out.print(report);
        System.out.print(issueBuilder);
    }

    @Override
    protected void parseJsonArray(final Report report, final JSONArray jsonReport, final IssueBuilder issueBuilder) {
        System.out.print(jsonReport);
        System.out.print(report);
        System.out.print(issueBuilder);
    }
}
