package edu.hm.hafner.analysis.parser;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.LineRange;
import edu.hm.hafner.analysis.LineRangeList;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;

/**
 * Parser for logs in JSON format.
 *
 * @author Jeremie Bresson
 */
public class JsonLogParser extends IssuePropertiesParser {
    private static final long serialVersionUID = 1349282064371959197L;

    @Override
    public boolean accepts(final ReaderFactory readerFactory) {
        return !readerFactory.getFileName().endsWith(".xml");
    }

    @Override
    public Report parse(final ReaderFactory readerFactory) throws ParsingException {
        try (Stream<String> lines = readerFactory.readStream()) {
            Report report = new Report();
            lines.map(String::trim)
                .filter(line -> !line.isEmpty())
                .map(line -> parseIssue(line, report))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(report::add);
            return report;
        }
    }

    private Optional<Issue> parseIssue(final String line, final Report report) {
        try {
            JSONObject jsonIssue = new JSONObject(line);

            return convertToIssue(jsonIssue);
        }
        catch (JSONException e) {
            report.logException(e, "Could not parse line: «%s»", line);
            return Optional.empty();
        }
    }

    private Optional<Issue> convertToIssue(final JSONObject jsonIssue) {
        IssueBuilder builder = new IssueBuilder();
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
        if (jsonIssue.has(DIRECTORY)) {
            builder.setDirectory(jsonIssue.getString(DIRECTORY));
        }
        if (jsonIssue.has(FINGERPRINT)) {
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
        if (jsonIssue.has(REFERENCE)) {
            builder.setReference(jsonIssue.getString(REFERENCE));
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
                    lineRanges.add(new LineRange(jsonRange.getInt(LINE_RANGE_START), jsonRange.getInt(
                            LINE_RANGE_END)));
                }
                else {
                    lineRanges.add(new LineRange(jsonRange.getInt(LINE_RANGE_START)));
                }
            }
            else if (jsonRange.has(LINE_RANGE_END)) {
                lineRanges.add(new LineRange(jsonRange.getInt(LINE_RANGE_END), jsonRange.getInt(
                        LINE_RANGE_END)));
            }
        }
        return lineRanges;
    }
}