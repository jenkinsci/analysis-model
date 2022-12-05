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

/**
 * A parser for Polyspace Bug Finder and Code Prover results.
 * Used for .csv files generated from Bugfinder and CodeProver tools
 *
 * @author Eva Habeeb
 */
public class PolyspaceParser extends IssueParser {

    private static final long serialVersionUID = -1251248150596418714L;
    static final int SEVERITY_INDEX = 10;
    static final int COLOR_INDEX = 3;
    static final int FAMILY_INDEX = 1;

    /**
     * Creates a new instance of {@link PolyspaceParser}.
     */

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

                String[] attributes = line.split("\\t", limit);
                builder.setFileName(attributes[8]);
                builder.setCategory(attributes[2]);
                builder.setDescription(attributes[1]);
                builder.setMessage("Check: " + attributes[5] + " " + attributes[6]);
                builder.setModuleName(attributes[7]);
                builder.setColumnStart(attributes[colNumber]);
                builder.setLineStart(attributes[lineNumber]);
                builder.setSeverity(mapPriority(attributes));

                report.add(builder.build());
            }
            return report;
        }
    }

    @SuppressWarnings({"PMD.UseVarargs", "PMD.CyclomaticComplexity" })
    private Severity mapPriority(final String[] attributes) {

        Severity priority = Severity.WARNING_NORMAL;

        if (equalsIgnoreCase(attributes[SEVERITY_INDEX], "Unset")) {

            if (equalsIgnoreCase(attributes[FAMILY_INDEX], "Defect")
                    || equalsIgnoreCase(attributes[COLOR_INDEX], "Red")) {
                priority = Severity.WARNING_HIGH;
            }
            else if (equalsIgnoreCase(attributes[COLOR_INDEX], "Orange")
                    || equalsIgnoreCase(attributes[COLOR_INDEX], "Not Applicable")) {
                priority = Severity.WARNING_NORMAL;
            }
            else if (equalsIgnoreCase(attributes[COLOR_INDEX], "Gray")
                    || equalsIgnoreCase(attributes[COLOR_INDEX], "Green")) {
                priority = Severity.WARNING_LOW;
            }
        }
        else if (equalsIgnoreCase(attributes[SEVERITY_INDEX], "High")) {
            priority = Severity.WARNING_HIGH;
        }
        else if (equalsIgnoreCase(attributes[SEVERITY_INDEX], "Medium")) {
            priority = Severity.WARNING_NORMAL;
        }
        else if (equalsIgnoreCase(attributes[SEVERITY_INDEX], "Low")) {
            priority = Severity.WARNING_LOW;
        }
        return priority;
    }
}
