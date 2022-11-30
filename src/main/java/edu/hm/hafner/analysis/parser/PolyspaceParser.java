package edu.hm.hafner.analysis.parser;

import java.io.UncheckedIOException;
import java.util.Iterator;
import java.util.stream.Stream;

import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import static edu.hm.hafner.util.StringContainsUtils.*;

/**
 * A parser for Polyspace Bug Finder and Code Prover results.
 * Used for .csv files generated from Bugfinder and CodeProver tools
 *
 * @author Eva Habeeb
 */
public class PolyspaceParser extends IssueParser {

    private static final long serialVersionUID = -1251248150596418714L;

    /**
     * Creates a new instance of {@link PolyspaceParser}.
     */
    public PolyspaceParser() { }

    @Override
    public Report parse(final ReaderFactory reader) throws ParsingException {
        try (Stream<String> lines = reader.readStream()) {
            return parse(lines);
        }
        catch (UncheckedIOException e) {
            throw new ParsingException(e);
        }
    }

    private Report parse(final Stream<String> lines) {

        try (IssueBuilder builder = new IssueBuilder()) {
            int lineNumber;
            int colNumber;
            int limit;
            Report report = new Report();
            Iterator<String> lineIterator = lines.iterator();

            while (lineIterator.hasNext()) {
                String line = lineIterator.next();
                //System.out.println(line);
                /* Checks whether "CWE" field is found, which defines the difference between
                 a BugFinder file and a CodeProver report */
                if (line.contains("CWE")) {
                    // BugFinder result file has 16 columns
                    limit = 16;
                    lineNumber = 14;
                    colNumber = 15;
                }
                else {
                    // CodePRover file has 15 columns
                    limit = 15;
                    lineNumber = 13;
                    colNumber = 14;
                }

                String[] arrOfStr = line.split("\\t", limit);
                builder.setFileName(arrOfStr[8]);
                builder.setCategory(arrOfStr[2]);
                builder.setDescription(arrOfStr[1]);
                builder.setMessage("Check: " + arrOfStr[5] + " " + arrOfStr[6]);
                builder.setModuleName(arrOfStr[7]);
                builder.setColumnStart(arrOfStr[colNumber]);
                builder.setLineStart(arrOfStr[lineNumber]);
                builder.setSeverity(mapPriority(arrOfStr));

                report.add(builder.build());
            }
            return report;
        }
    }

    private Severity mapPriority(final String[] arrOfStr) {

        Severity priority = Severity.WARNING_NORMAL;

        if (equalsIgnoreCase(arrOfStr[10], "Unset") && containsAnyIgnoreCase(arrOfStr[1], "Defect")
                || equalsIgnoreCase(arrOfStr[10], "Unset") && containsAnyIgnoreCase(arrOfStr[3], "Red")
                || equalsIgnoreCase(arrOfStr[10], "High")) {
            priority = Severity.WARNING_HIGH;
        }
        else if (equalsIgnoreCase(arrOfStr[10], "Unset") && containsAnyIgnoreCase(arrOfStr[3], "Orange")
                || equalsIgnoreCase(arrOfStr[10], "Unset") && containsAnyIgnoreCase(arrOfStr[3], "Not Applicable")
                || (equalsIgnoreCase(arrOfStr[10], "Medium"))) {
            priority = Severity.WARNING_NORMAL;
        }
        else if (equalsIgnoreCase(arrOfStr[10], "Unset") && containsAnyIgnoreCase(arrOfStr[3], "Gray")
                || equalsIgnoreCase(arrOfStr[10], "Unset") && containsAnyIgnoreCase(arrOfStr[3], "Green")
                || equalsIgnoreCase(arrOfStr[10], "Low")) {
            priority = Severity.WARNING_LOW;
        }
        return priority;
    }
}
