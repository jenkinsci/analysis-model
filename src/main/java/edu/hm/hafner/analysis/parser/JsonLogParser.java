package edu.hm.hafner.analysis.parser;

import java.io.Serial;
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
    @Serial
    private static final long serialVersionUID = 1349282064371959197L;

    @Override
    public boolean accepts(final ReaderFactory readerFactory) {
        var fileName = readerFactory.getFileName();
        return !fileName.endsWith(".xml") && !fileName.endsWith(".json");
    }

    @Override
    public Report parse(final ReaderFactory readerFactory) throws ParsingException {
        try (Stream<String> lines = readerFactory.readStream()) {
            var report = new Report();
            lines.map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .filter(line -> !line.startsWith("//"))
                    .filter(line -> line.charAt(0) != '#')
                    .map(line -> parseIssue(line, report))
                    .flatMap(Optional::stream)
                    .forEach(report::add);
            return report;
        }
    }

    private Optional<Issue> parseIssue(final String line, final Report report) {
        try {
            var jsonIssue = new JSONObject(line);
            return convertToIssue(jsonIssue);
        }
        catch (JSONException e) {
            report.logException(e, "Could not parse line: «%s»", line);
            return Optional.empty();
        }
    }
}
