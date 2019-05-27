package edu.hm.hafner.analysis.parser;

import java.util.Optional;
import java.util.stream.Stream;

import org.json.JSONException;
import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.Report;

/**
 * Parser for logs in JSON format.
 *
 * @author Jeremie Bresson
 */
public class JsonLogParser extends JsonBaseParser {
    private static final long serialVersionUID = 1349282064371959197L;

    @Override
    public boolean accepts(final ReaderFactory readerFactory) {
        return !readerFactory.getFileName().endsWith(".xml") && !readerFactory.getFileName().endsWith(".json");
    }

    @Override
    public Report parse(final ReaderFactory readerFactory) throws ParsingException {
        try (Stream<String> lines = readerFactory.readStream()) {
            Report report = new Report();
            lines.map(String::trim)
                .filter(line -> !line.isEmpty())
                .filter(line -> !line.startsWith("//"))
                .filter(line -> line.charAt(0) != '#')
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
}