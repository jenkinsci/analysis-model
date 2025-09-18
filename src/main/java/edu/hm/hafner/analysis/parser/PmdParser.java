package edu.hm.hafner.analysis.parser;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.xml.sax.SAXException;

import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.SecureDigester;
import edu.hm.hafner.analysis.Severity;
import edu.umd.cs.findbugs.annotations.CheckForNull;

import java.io.IOException;
import java.io.Serial;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
    public Report parseReport(final ReaderFactory readerFactory) throws ParsingException {
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
        if (Strings.CS.endsWith(original, ".")) {
            return original;
        }
        else {
            return original + ".";
        }
    }

    /**
     * Java Bean class for a violation of the PMD format.
     *
     * @author Ullrich Hafner
     */
    @SuppressWarnings("all")
    public static class Violation {
        /** IssueType of warning. */
        @CheckForNull
        private String rule;
        /** Category of warning. */
        @CheckForNull
        private String ruleset;

        @CheckForNull
        private String externalInfoUrl;
        @CheckForNull
        private String javaPackage;
        private int priority;
        @CheckForNull
        private String message;
        private int beginline;
        private int endline;
        private int begincolumn;
        private int endcolumn;

        // CHECKSTYLE:OFF
        @CheckForNull
        public String getRule() {
            return rule;
        }

        public void setRule(final String rule) {
            this.rule = rule;
        }

        @CheckForNull
        public String getRuleset() {
            return ruleset;
        }

        public void setRuleset(final String ruleset) {
            this.ruleset = ruleset;
        }

        @CheckForNull
        public String getExternalInfoUrl() {
            return externalInfoUrl;
        }

        public void setExternalInfoUrl(final String externalInfoUrl) {
            this.externalInfoUrl = externalInfoUrl;
        }

        @CheckForNull
        public String getPackage() {
            return javaPackage;
        }

        public void setPackage(final String packageName) {
            javaPackage = packageName;
        }

        public int getPriority() {
            return priority;
        }

        public void setPriority(final int priority) {
            this.priority = priority;
        }

        @CheckForNull
        public String getMessage() {
            return message;
        }

        public void setMessage(final String message) {
            this.message = message;
        }

        public int getBeginline() {
            return beginline;
        }

        public void setBeginline(final int beginline) {
            this.beginline = beginline;
        }

        public int getEndline() {
            return endline;
        }

        public void setEndline(final int endline) {
            this.endline = endline;
        }

        public int getEndcolumn() {
            return endcolumn;
        }

        public void setEndcolumn(final int endcolumn) {
            this.endcolumn = endcolumn;
        }

        public int getBegincolumn() {
            return begincolumn;
        }

        public void setBegincolumn(final int begincolumn) {
            this.begincolumn = begincolumn;
        }
    }

    /**
     * Java Bean class for warnings of the PMD format.
     *
     * @author Ullrich Hafner
     */
    public static class Pmd {
        private final List<File> files = new ArrayList<>();
        private final List<PmdError> errors = new ArrayList<>();

        /**
         * Adds a new file.
         *
         * @param file
         *         the file to add
         */
        public void addFile(final File file) {
            files.add(file);
        }

        /**
         * Adds a new error.
         *
         * @param error
         *         the error to add
         */
        public void addError(final PmdError error) {
            errors.add(error);
        }

        /**
         * Returns all files. The returned collection is read-only.
         *
         * @return all files
         */
        public Collection<File> getFiles() {
            return Collections.unmodifiableCollection(files);
        }

        /**
         * Returns all errors. The returned collection is read-only.
         *
         * @return all errors
         */
        public Collection<PmdError> getErrors() {
            return Collections.unmodifiableCollection(errors);
        }
    }

    /**
     * Java Bean class for an error of the PMD format.
     *
     * @author Ullrich Hafner
     */
    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "PMD.DataClass"})
    public static class PmdError {
        @CheckForNull
        private String filename;
        @CheckForNull
        private String msg;
        @CheckForNull
        private String description;

        @CheckForNull
        public String getFilename() {
            return filename;
        }

        public void setFilename(@CheckForNull final String filename) {
            this.filename = filename;
        }

        @CheckForNull
        public String getMsg() {
            return msg;
        }

        public void setMsg(@CheckForNull final String msg) {
            this.msg = msg;
        }

        @CheckForNull
        public String getDescription() {
            return description;
        }

        public void setDescription(@CheckForNull final String description) {
            this.description = description;
        }
    }

    /**
     * Java Bean class for a file of the PMD format.
     *
     * @author Ullrich Hafner
     */
    @SuppressWarnings("InstanceVariableMayNotBeInitialized")
    public static class File {
        /** Name of the file. */
        @CheckForNull
        private String name;
        /** All violations of this file. */
        private final List<Violation> violations = new ArrayList<>();

        /**
         * Adds a new violation to this file.
         *
         * @param violation
         *            the new violation
         */
        public void addViolation(final Violation violation) {
            violations.add(violation);
        }

        /**
         * Returns all violations of this file. The returned collection is
         * read-only.
         *
         * @return all violations in this file
         */
        public Collection<Violation> getViolations() {
            return Collections.unmodifiableCollection(violations);
        }

        /**
         * Returns the name of this file.
         *
         * @return the name of this file
         */
        @CheckForNull
        public String getName() {
            return name;
        }

        /**
         * Sets the name of this file to the specified value.
         *
         * @param name the value to set
         */
        public void setName(@CheckForNull final String name) {
            this.name = name;
        }
    }
}
