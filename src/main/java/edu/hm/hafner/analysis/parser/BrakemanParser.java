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

public class BrakemanParser extends IssueParser {
    private static final long serialVersionUID = 1374428573878091300L;

    @Override
    public boolean accepts(final ReaderFactory readerFactory) {
        return readerFactory.getFileName().endsWith(".json");
    }

    @Override
    public Report parse(final ReaderFactory readerFactory) throws ParsingException {
        final Report report = new Report();
        try (Reader reader = readerFactory.create()) {
            final JSONObject jsonReport = (JSONObject) new JSONTokener(reader).nextValue();

            final JSONArray warnings = jsonReport.getJSONArray("warnings");
            for (Object warning : warnings) {
                report.add(convertToIssue((JSONObject) warning));
            }
        }
        catch (IOException | JSONException | ClassCastException e) {
            throw new ParsingException(e);
        }
        return report;
    }

    private Issue convertToIssue(JSONObject warning) throws JSONException {
        String fileName = warning.getString("file");
        String category = warning.getString("warning_type");
        Severity severity = getSeverity(warning.getString("confidence"));
        String fingerprint = warning.getString("fingerprint");
        String warning_type = warning.getString("check_name");
        StringBuilder message = new StringBuilder();
        message.append(warning.getString("message"));

        if(warning.has("code")) {
            String code = warning.optString("code", "");

            if(!code.isEmpty()) {
                message.
                    append(": ").
                    append(warning.getString("code"));
            }
        }

        int line = warning.optInt("line", 1);

        return new IssueBuilder()
            .setMessage(message.toString())
            .setCategory(category)
            .setType(warning_type)
            .setSeverity(severity)
            .setFileName(fileName)
            .setLineStart(line)
            .setFingerprint(fingerprint)
            .build();
    }

    private Severity getSeverity(String confidence) {
        if ("Medium".equalsIgnoreCase(confidence)) {
            return Severity.WARNING_NORMAL;
        } else if ("High".equalsIgnoreCase(confidence)) {
            return Severity.WARNING_HIGH;
        } else if ("Weak".equalsIgnoreCase(confidence)) {
            return Severity.WARNING_LOW;
        }
        else {
            return Severity.WARNING_HIGH;
        }
    }
}
