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
 * A parser for ot-docker-linter json output.
 * <p>
 * See <a href='https://github.com/opstree/OT-Dockerlinter'>ot-docker-linter</a> for project details.
 *
 * @author Abhishek Dubey
 */

public class OTDockerLintParser extends IssueParser {
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
        if (jsonIssue.has("severity")) {
            builder.setSeverity(toSeverity(jsonIssue.getString("severity")));
        }
        if (jsonIssue.has("line")) {
            builder.setLineStart(jsonIssue.getInt("line_number"));
        }
        if (jsonIssue.has("line")) {
            builder.setModuleName(jsonIssue.getString("line"));
        }
        if (jsonIssue.has("description")) {
            builder.setDescription(jsonIssue.getString("description"));
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
            case "Info":
                return Severity.WARNING_NORMAL;
            case "Warning":
                return Severity.WARNING_HIGH;
            case "Error":
                return Severity.ERROR;
            default:
                return Severity.WARNING_LOW;
        }
    }
}
