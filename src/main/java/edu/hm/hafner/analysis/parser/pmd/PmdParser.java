package edu.hm.hafner.analysis.parser.pmd;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.Reader;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import org.xml.sax.SAXException;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.ParsingCanceledException;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.SecureDigester;

/**
 * A parser for PMD XML files.
 *
 * @author Ullrich Hafner
 */
public class PmdParser extends AbstractParser<Issue> {
    private static final long serialVersionUID = 6507147028628714706L;

    /** PMD priorities smaller than this value are mapped to {@link Priority#HIGH}. */
    private static final int PMD_PRIORITY_MAPPED_TO_HIGH_PRIORITY = 3;
    /** PMD priorities greater than this value are mapped to {@link Priority#LOW}. */
    private static final int PMD_PRIORITY_MAPPED_TO_LOW_PRIORITY = 4;

    @Override
    public Issues<Issue> parse(@Nonnull final Reader reader, @Nonnull final IssueBuilder builder,
            final Function<String, String> preProcessor) throws ParsingCanceledException, ParsingException {
        try {
            SecureDigester digester = new SecureDigester(PmdParser.class);

            String rootXPath = "pmd";
            digester.addObjectCreate(rootXPath, Pmd.class);
            digester.addSetProperties(rootXPath);

            String fileXPath = "pmd/file";
            digester.addObjectCreate(fileXPath, File.class);
            digester.addSetProperties(fileXPath);
            digester.addSetNext(fileXPath, "addFile", File.class.getName());

            String bugXPath = "pmd/file/violation";
            digester.addObjectCreate(bugXPath, Violation.class);
            digester.addSetProperties(bugXPath);
            digester.addCallMethod(bugXPath, "setMessage", 0);
            digester.addSetNext(bugXPath, "addViolation", Violation.class.getName());

            Pmd pmd = digester.parse(reader);
            if (pmd == null) {
                throw new ParsingException("Input stream is not a PMD file.");
            }

            return convert(pmd, builder);
        }
        catch (IOException | SAXException exception) {
            throw new ParsingException(exception);
        }
    }

    private Issues<Issue> convert(final Pmd pmdIssues, final IssueBuilder builder) {
        Issues<Issue> issues = new Issues<>();
        for (File file : pmdIssues.getFiles()) {
            for (Violation warning : file.getViolations()) {
                builder.setPriority(mapPriority(warning))
                        .setMessage(createMessage(warning))
                        .setCategory(warning.getRuleset())
                        .setType(warning.getRule())
                        .setLineStart(warning.getBeginline())
                        .setLineEnd(warning.getEndline())
                        .setPackageName(warning.getPackage())
                        .setFileName(file.getName())
                        .setColumnStart(warning.getBegincolumn())
                        .setColumnEnd(warning.getEndcolumn());
                issues.add(builder.build());
            }
        }
        return issues;
    }

    private Priority mapPriority(final Violation warning) {
        Priority priority;
        if (warning.getPriority() < PMD_PRIORITY_MAPPED_TO_HIGH_PRIORITY) {
            priority = Priority.HIGH;
        }
        else if (warning.getPriority() >  PMD_PRIORITY_MAPPED_TO_LOW_PRIORITY) {
            priority = Priority.LOW;
        }
        else {
            priority = Priority.NORMAL;
        }
        return priority;
    }

    private String createMessage(final Violation warning) {
        String original = warning.getMessage();
        if (StringUtils.endsWith(original, ".")) {
            return original;
        }
        else {
            return original + ".";
        }
    }
}

