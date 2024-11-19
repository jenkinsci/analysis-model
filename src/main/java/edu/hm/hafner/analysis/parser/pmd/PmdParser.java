package edu.hm.hafner.analysis.parser.pmd;

import java.io.IOException;
import java.io.Serial;

import org.apache.commons.lang3.StringUtils;
import org.xml.sax.SAXException;

import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.SecureDigester;
import edu.hm.hafner.analysis.Severity;

/**
 * A parser for PMD XML files.
 *
 * @author Ullrich Hafner
 */
public class PmdParser extends IssueParser {
    @Serial
    private static final long serialVersionUID = 6507147028628714706L;

    /** PMD priorities smaller than this value are mapped to {@link Severity#WARNING_HIGH}. */
    private static final int PMD_PRIORITY_MAPPED_TO_HIGH_PRIORITY = 3;
    /** PMD priorities greater than this value are mapped to {@link Severity#WARNING_LOW}. */
    private static final int PMD_PRIORITY_MAPPED_TO_LOW_PRIORITY = 4;

    @Override
    public Report parse(final ReaderFactory readerFactory) throws ParsingException {
        var issues = parseIssues(readerFactory);
        parseErrors(readerFactory).stream().forEach(issues::add);
        return issues;
    }

    private Report parseIssues(final ReaderFactory readerFactory) {
        var digester = new SecureDigester(PmdParser.class);

        var rootXPath = "pmd";
        digester.addObjectCreate(rootXPath, Pmd.class);
        digester.addSetProperties(rootXPath);

        var fileXPath = "pmd/file";
        digester.addObjectCreate(fileXPath, File.class);
        digester.addSetProperties(fileXPath);
        digester.addSetNext(fileXPath, "addFile", File.class.getName());

        var bugXPath = "pmd/file/violation";
        digester.addObjectCreate(bugXPath, Violation.class);
        digester.addSetProperties(bugXPath);
        digester.addCallMethod(bugXPath, "setMessage", 0);
        digester.addSetNext(bugXPath, "addViolation", Violation.class.getName());

        try (var reader = readerFactory.create()) {
            Pmd pmd = digester.parse(reader);
            if (pmd == null) {
                throw new ParsingException("Input stream is not a PMD file.");
            }

            return convertIssues(pmd);
        }
        catch (IOException | SAXException exception) {
            throw new ParsingException(exception);
        }
    }

    private Report parseErrors(final ReaderFactory readerFactory) {
        var digester = new SecureDigester(PmdParser.class);

        var rootXPath = "pmd";
        digester.addObjectCreate(rootXPath, Pmd.class);
        digester.addSetProperties(rootXPath);

        var errorXPath = "pmd/error";
        digester.addObjectCreate(errorXPath, PmdError.class);
        digester.addSetProperties(errorXPath);
        digester.addSetNext(errorXPath, "addError", PmdError.class.getName());
        digester.addCallMethod(errorXPath, "setDescription", 0);

        try (var reader = readerFactory.create()) {
            Pmd pmd = digester.parse(reader);
            if (pmd == null) {
                throw new ParsingException("Input stream is not a PMD file.");
            }

            return convertErrors(pmd);
        }
        catch (IOException | SAXException exception) {
            throw new ParsingException(exception);
        }
    }

    private Report convertIssues(final Pmd pmdIssues) {
        try (var issueBuilder = new IssueBuilder()) {
            var report = new Report();
            for (File file : pmdIssues.getFiles()) {
                for (Violation warning : file.getViolations()) {
                    issueBuilder.setSeverity(mapPriority(warning))
                            .setMessage(createMessage(warning))
                            .setCategory(warning.getRuleset())
                            .setType(warning.getRule())
                            .setLineStart(warning.getBeginline())
                            .setLineEnd(warning.getEndline())
                            .setPackageName(warning.getPackage())
                            .setFileName(file.getName())
                            .setColumnStart(warning.getBegincolumn())
                            .setColumnEnd(warning.getEndcolumn());
                    report.add(issueBuilder.buildAndClean());
                }
            }
            return report;
        }
    }

    private Report convertErrors(final Pmd pmdIssues) {
        try (var issueBuilder = new IssueBuilder()) {
            var report = new Report();
            for (PmdError error : pmdIssues.getErrors()) {
                issueBuilder.setSeverity(Severity.ERROR)
                        .setMessage(error.getMsg())
                        .setDescription(error.getDescription())
                        .setFileName(error.getFilename());
                report.add(issueBuilder.buildAndClean());
            }
            return report;
        }
    }

    private Severity mapPriority(final Violation warning) {
        if (warning.getPriority() < PMD_PRIORITY_MAPPED_TO_HIGH_PRIORITY) {
            return Severity.WARNING_HIGH;
        }
        else if (warning.getPriority() > PMD_PRIORITY_MAPPED_TO_LOW_PRIORITY) {
            return Severity.WARNING_LOW;
        }
        return Severity.WARNING_NORMAL;
    }

    private String createMessage(final Violation warning) {
        var original = warning.getMessage();
        if (original == null) {
            return StringUtils.EMPTY;
        }
        if (StringUtils.endsWith(original, ".")) {
            return original;
        }
        else {
            return original + ".";
        }
    }
}
