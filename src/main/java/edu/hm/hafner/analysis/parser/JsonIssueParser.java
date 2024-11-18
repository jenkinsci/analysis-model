package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.io.Reader;
import java.io.Serial;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.Report;

/**
 * Base class for parsers that operate on a `*.json` file that contains issues in a JSON data structure.
 *
 * @author Ullrich Hafner
 */
public abstract class JsonIssueParser extends IssueParser {
    @Serial
    private static final long serialVersionUID = -4062256623915009878L;

    @Override
    public Report parse(final ReaderFactory readerFactory) throws ParsingException {
        var report = new Report();
        try (Reader reader = readerFactory.create(); IssueBuilder issueBuilder = new IssueBuilder()) {
            var parsedValue = new JSONTokener(reader).nextValue();
            if (parsedValue instanceof JSONObject) {
                var jsonReport = (JSONObject) parsedValue;

                parseJsonObject(report, jsonReport, issueBuilder);
            }
            else if (parsedValue instanceof JSONArray) {
                var jsonReport = (JSONArray) parsedValue;

                parseJsonArray(report, jsonReport, issueBuilder);
            }
            else {
                throw new ParsingException("Cannot process parsed JSON object '%s'", parsedValue);
            }
        }
        catch (IOException | JSONException | ClassCastException e) {
            throw new ParsingException(e);
        }
        return report;
    }

    @Override
    public boolean accepts(final ReaderFactory readerFactory) {
        return readerFactory.getFileName().endsWith(".json");
    }

    /**
     * Parses the specified JSON object and populates the provided report with all issues.
     *
     * @param report
     *         the report to fill
     * @param jsonReport
     *         the input JSON report given as JSON object
     * @param issueBuilder
     *         build to be used to create issues
     */
    protected void parseJsonObject(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        // by default, no issues are reported
    }

    /**
     * Parses the specified JSON object and populates the provided report with all issues.
     *
     * @param report
     *         the report to fill
     * @param jsonReport
     *         the input JSON report given as JSON array
     * @param issueBuilder
     *         build to be used to create issues
     */
    protected void parseJsonArray(final Report report, final JSONArray jsonReport, final IssueBuilder issueBuilder) {
        // by default, no issues are reported
    }
}
