package edu.hm.hafner.analysis.parser;

import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Severity;

import java.io.Serial;
import java.util.Optional;

/**
 * Parser report in JSON format as exported by DScanner.
 *
 * @author Andre Pany
 */
public class DScannerParser extends JsonParser {
    @Serial
    private static final long serialVersionUID = -3396574381502866972L;
    private static final String KEY = "key";
    private static final String LINE = "line";
    private static final String COLUMN = "column";

    @Override
    Optional<Issue> convertToIssue(final JSONObject jsonIssue, final IssueBuilder builder) {
        if (jsonIssue.has(KEY)) {
            var key = jsonIssue.getString(KEY);
            builder.setCategory(key);
            builder.setSeverity(getSeverityByKey(key));
        }
        if (jsonIssue.has(FILE_NAME)) {
            builder.setFileName(jsonIssue.getString(FILE_NAME));
        }
        if (jsonIssue.has(LINE)) {
            builder.setLineStart(jsonIssue.getInt(LINE));
        }
        if (jsonIssue.has(COLUMN)) {
            builder.setColumnStart(jsonIssue.getInt(COLUMN));
        }
        if (jsonIssue.has(MESSAGE)) {
            builder.setMessage(jsonIssue.getString(MESSAGE));
        }
        return builder.buildOptional();
    }

    private Severity getSeverityByKey(final String key) {
        var parts = key.split("\\.", -1);

        return switch (parts[1]) {
            case "suspicious", "deprecated" -> Severity.WARNING_NORMAL;
            case "bugs" -> Severity.WARNING_HIGH;
            default -> Severity.WARNING_LOW;
        };
    }
}
