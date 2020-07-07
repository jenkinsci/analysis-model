package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.io.Reader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;

/**
 * A parser for hadolint json output.
 * <p>
 * See <a href='https://github.com/hadolint/hadolint'>hadolint</a> for project details. Possible usage via docker is:
 * {@code docker run --rm -i hadolint/hadolint hadolint -f json - < Dockerfile | jq}.
 *
 * @author Andreas Mandel
 */
public class HadoLintParser extends IssueParser {
    private static final long serialVersionUID = 42L;

    @Override
    public boolean accepts(final ReaderFactory readerFactory) {
        return readerFactory.getFileName().endsWith(".json");
    }

    @Override
    public Report parse(final ReaderFactory readerFactory) throws ParsingException {
        try (Reader reader = readerFactory.create()) {
            JSONArray jsonReport = (JSONArray) new JSONTokener(reader).nextValue();

            Report report = new Report();
            for (Object issue : jsonReport) {
                if (issue instanceof JSONObject) {
                    report.add(convertToIssue((JSONObject) issue));
                }
            }
            return report;
        }
        catch (IOException | JSONException | ClassCastException e) {
            throw new ParsingException(e);
        }
    }

    Issue convertToIssue(final JSONObject jsonIssue) {
        IssueBuilder builder = new IssueBuilder();
        if (jsonIssue.has("code")) {
            builder.setCategory(jsonIssue.getString("code"));
        }
        if (jsonIssue.has("level")) {
            builder.setSeverity(toSeverity(jsonIssue.getString("level")));
        }
        if (jsonIssue.has("line")) {
            builder.setLineStart(jsonIssue.getInt("line"));
        }
        if (jsonIssue.has("column")) {
            builder.setColumnStart(jsonIssue.getInt("column"));
        }
        if (jsonIssue.has("message")) {
            builder.setMessage(jsonIssue.getString("message"));
        }
        if (jsonIssue.has("file")) {
            builder.setFileName(jsonIssue.getString("file"));
        }
        return builder.build();
    }

    private Severity toSeverity(final String level) {
        switch (level) {
            case "style":
                return Severity.WARNING_LOW;
            case "info":
                return Severity.WARNING_NORMAL;
            case "warning":
                return Severity.WARNING_HIGH;
            case "error":
                return Severity.ERROR;
            default:
                return Severity.WARNING_LOW;
        }
    }
}
