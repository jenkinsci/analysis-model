package edu.hm.hafner.analysis.parser;

import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.LookaheadParser;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.util.LookaheadStream;

import static edu.hm.hafner.util.IntegerParser.*;

/**
 * A parser for Polyspace Bug Finder and Code Prover results.
 *
 * @author Eva Habeeb
 */
public class PolyspaceParser extends LookaheadParser {

    private static final long serialVersionUID = -1251248150596418714L;

    private static final String WARNING_PATTERN = "^([0-9]+)\\t([^\\t]*)\\t([^\\t]*)\\t([^\\t]*)\\t([^\\t]*)"
            + "\\t([^\\t]*)\\t([^\\t]*)\\t([^\\t]*)\\t([^\\t]*)\\t([^\\t]*)\\t([^\\t]*)\\t([^\\t]*)\\t([^\\t]*)"
            + "\\t([^\\t]*)\\t([^\\t]*)(\\t([^\\t]*))?";

    /**
     * Creates a new instance of {@link PolyspaceParser}.
     */
    public PolyspaceParser() {
        super(WARNING_PATTERN);
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) {


        int lineNumber = 0;
        int colNumber = 0;
        String cwe = matcher.group(13);
        if(cwe.contains("CWE")){
            lineNumber = parseInt(matcher.group(15));
            colNumber = parseInt(matcher.group(16));
        }
        else{
            lineNumber = parseInt(matcher.group(14));
            colNumber = parseInt(matcher.group(15));
        }


        String ID = matcher.group(1);
        return builder.setFileName(matcher.group(9))
                //.setId(UUID.fromString(ID))
                .setCategory(matcher.group(3))
                .setDescription(matcher.group(2))
                .setMessage("Check: "+matcher.group(6)+" "+matcher.group(7))
                .setModuleName(matcher.group(8))
                .setColumnStart(colNumber)
                .setLineStart(lineNumber)
                .setSeverity(mapPriority(matcher))
                .buildOptional();
    }

    private Severity mapPriority(final Matcher matcher) {

        Severity priority = null;

        if (equalsIgnoreCase(matcher.group(11), "Unset")) {

            if (equalsIgnoreCase(matcher.group(2), "Defect")) {
                priority = Severity.WARNING_HIGH;
            }
            else {
                if (equalsIgnoreCase(matcher.group(4), "Red")){
                    priority = Severity.WARNING_HIGH;
                }
                else if (equalsIgnoreCase(matcher.group(4), "Orange")){
                    priority = Severity.WARNING_NORMAL;
                }
                else if (equalsIgnoreCase(matcher.group(4), "Gray")){
                    priority = Severity.WARNING_LOW;
                }
                else if (equalsIgnoreCase(matcher.group(4), "Green")){
                    priority = Severity.WARNING_LOW;
                }
                else if (equalsIgnoreCase(matcher.group(4), "Not Applicable")){
                    priority = Severity.WARNING_NORMAL;
                }
            }

        }
        else if (equalsIgnoreCase(matcher.group(11), "High")) {
            priority = Severity.WARNING_HIGH;
        }
        else if (equalsIgnoreCase(matcher.group(11), "Medium")) {
            priority = Severity.WARNING_NORMAL;
        }
        else if (equalsIgnoreCase(matcher.group(11), "Low")) {
            priority = Severity.WARNING_LOW;
        }
        return priority;
    }


}
