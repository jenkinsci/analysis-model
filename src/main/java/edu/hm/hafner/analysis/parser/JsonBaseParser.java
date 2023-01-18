package edu.hm.hafner.analysis.parser;

import java.util.Optional;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.LineRange;
import edu.hm.hafner.analysis.LineRangeList;
import edu.hm.hafner.analysis.Severity;

/**
 * Base Parser JSON format.
 *
 * @author Jeremie Bresson
 */
abstract class JsonBaseParser extends IssueParser {
    private static final long serialVersionUID = -2318844382394973833L;

    /**
     * Deserialize an Issue from a JSON object.
     *
     * @param jsonIssue
     *         the issue as JSON object
     *
     * @return issue instance
     */
    Optional<Issue> convertToIssue(final JSONObject jsonIssue) {
        try (IssueBuilder builder = new IssueBuilder()) {
            return convertToIssue(jsonIssue, builder);
        }
    }

    /**
     * Deserialize an Issue from a JSON object. Properties that are not part of equals like {@code reference} or
     * {@code directory} will be skipped.
     *
     * @param jsonIssue
     *         the issue as JSON object
     * @param builder
     *         the issue builder to use
     *
     * @return issue instance
     */
    @SuppressWarnings({"PMD.CyclomaticComplexity", "PMD.CognitiveComplexity", "PMD.NPathComplexity"})
    Optional<Issue> convertToIssue(final JSONObject jsonIssue, final IssueBuilder builder) {
        if (jsonIssue.has(ADDITIONAL_PROPERTIES)) {
            builder.setAdditionalProperties(jsonIssue.getString(ADDITIONAL_PROPERTIES));
        }
        if (jsonIssue.has(CATEGORY)) {
            builder.setCategory(jsonIssue.getString(CATEGORY));
        }
        if (jsonIssue.has(COLUMN_END)) {
            builder.setColumnEnd(jsonIssue.getInt(COLUMN_END));
        }
        if (jsonIssue.has(COLUMN_START)) {
            builder.setColumnStart(jsonIssue.getInt(COLUMN_START));
        }
        if (jsonIssue.has(DESCRIPTION)) {
            builder.setDescription(jsonIssue.getString(DESCRIPTION));
        }
        if (jsonIssue.has(FINGERPRINT)) { // even though not part of equals it makes sense to read if available
            builder.setFingerprint(jsonIssue.getString(FINGERPRINT));
        }
        if (jsonIssue.has(FILE_NAME)) {
            builder.setFileName(jsonIssue.getString(FILE_NAME));
        }
        if (jsonIssue.has(ID)) {
            builder.setId(UUID.fromString(jsonIssue.getString(ID)));
        }
        if (jsonIssue.has(LINE_END)) {
            builder.setLineEnd(jsonIssue.getInt(LINE_END));
        }
        if (jsonIssue.has(LINE_RANGES)) {
            JSONArray jsonRanges = jsonIssue.getJSONArray(LINE_RANGES);
            LineRangeList lineRanges = convertToLineRangeList(jsonRanges);
            builder.setLineRanges(lineRanges);
        }
        if (jsonIssue.has(LINE_START)) {
            builder.setLineStart(jsonIssue.getInt(LINE_START));
        }
        if (jsonIssue.has(MESSAGE)) {
            builder.setMessage(jsonIssue.getString(MESSAGE));
        }
        if (jsonIssue.has(MODULE_NAME)) {
            builder.setModuleName(jsonIssue.getString(MODULE_NAME));
        }
        if (jsonIssue.has(ORIGIN)) {
            builder.setOrigin(jsonIssue.getString(ORIGIN));
        }
        if (jsonIssue.has(PACKAGE_NAME)) {
            builder.setPackageName(jsonIssue.getString(PACKAGE_NAME));
        }
        if (jsonIssue.has(SEVERITY)) {
            builder.setSeverity(Severity.valueOf(jsonIssue.getString(SEVERITY)));
        }
        if (jsonIssue.has(TYPE)) {
            builder.setType(jsonIssue.getString(TYPE));
        }
        return builder.buildOptional();
    }

    private LineRangeList convertToLineRangeList(final JSONArray jsonRanges) {
        LineRangeList lineRanges = new LineRangeList();
        for (int i = 0; i < jsonRanges.length(); i++) {
            JSONObject jsonRange = jsonRanges.getJSONObject(i);
            if (jsonRange.has(LINE_RANGE_START)) {
                if (jsonRange.has(LINE_RANGE_END)) {
                    lineRanges.add(new LineRange(jsonRange.getInt(LINE_RANGE_START),
                            jsonRange.getInt(LINE_RANGE_END)));
                }
                else {
                    lineRanges.add(new LineRange(jsonRange.getInt(LINE_RANGE_START)));
                }
            }
            else if (jsonRange.has(LINE_RANGE_END)) {
                lineRanges.add(new LineRange(jsonRange.getInt(LINE_RANGE_END),
                        jsonRange.getInt(LINE_RANGE_END)));
            }
        }
        return lineRanges;
    }
}
