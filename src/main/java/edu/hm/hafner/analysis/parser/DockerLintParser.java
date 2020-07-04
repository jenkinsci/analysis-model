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
 * A parser for dockerlint json output.
 * <p>
 * See <a href='https://github.com/projectatomic/dockerfile_lint'>dockerlint</a> for project details. Possible usage via docker is:
 * {@code  docker run -it --rm -v $PWD:/root/ \
 *                            projectatomic/dockerfile-lint \
 *                            dockerfile_lint -j -f Dockerfile}.
 *
 * @author Andreas Mandel
 */
public class DockerLintParser extends IssueParser {
    private static final long serialVersionUID = 42L;

    @Override
    public boolean accepts(final ReaderFactory readerFactory) {
        return readerFactory.getFileName().endsWith(".json");
    }

    @Override
    public Report parse(final ReaderFactory readerFactory) throws ParsingException {
        Report report = new Report();
        try (Reader reader = readerFactory.create()) {
            JSONObject jsonReport = (JSONObject) new JSONTokener(reader).nextValue();
            for (String severityGroupName : jsonReport.keySet()) {
                Object severityGroup = jsonReport.get(severityGroupName);
                if (severityGroup instanceof JSONObject) {
                    JSONArray data = ((JSONObject) severityGroup).optJSONArray("data");
                    for (Object issue : data) {
                        if (issue instanceof JSONObject) {
                            report.add(convertToIssue((JSONObject) issue));
                        }
                    }
                }
            }
        }
        catch (IOException | JSONException | ClassCastException e) {
            throw new ParsingException(e);
        }
        return report;
    }

    private Issue convertToIssue(final JSONObject jsonIssue) {
        IssueBuilder builder = new IssueBuilder();

        StringBuilder message = new StringBuilder();
        message.append(jsonIssue.optString("message"));
        if (jsonIssue.has("description")) {
            message.append(" - ");
            message.append(jsonIssue.getString("description"));
        }
        if (jsonIssue.has("reference_url")) {
            Object refUrl = jsonIssue.get("reference_url");
            message.append(" See ");
            message.append(colapseReferenceUrl(refUrl));
        }
        builder.setMessage(message.toString());

        builder.setSeverity(toSeverity(jsonIssue.optString("level")));
        builder.setLineStart(jsonIssue.optInt("line", -1));
        builder.setCategory(jsonIssue.optString("label", null));
        // Lazy
        builder.setFileName("Dockerfile");
        // not reading "lineContent" & "instruction" & "count" & "regex"
        return builder.build();
    }

    private String colapseReferenceUrl(final Object refUrl) {
        StringBuilder referenceUrl = new StringBuilder();
        if (refUrl instanceof JSONArray) {
            for (Object part : (JSONArray) refUrl) {
                referenceUrl.append(part);
            }
        } else {
            referenceUrl.append(refUrl);
        }
        return referenceUrl.toString();
    }

    private Severity toSeverity(final String level) {
        switch (level) {
            case "warn":
                return Severity.WARNING_NORMAL;
            case "error":
                return Severity.WARNING_HIGH;
            default:
                return Severity.WARNING_LOW;
        }
    }
}
