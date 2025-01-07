package edu.hm.hafner.analysis.parser;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.digester3.Digester;

import edu.hm.hafner.analysis.DuplicationGroup;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * A parser for PMD's CPD XML files.
 *
 * @author Ullrich Hafner
 */
public class CpdParser extends AbstractDryParser<CpdParser.Duplication> {
    /** Unique ID of this class. */
    @Serial
    private static final long serialVersionUID = 6507147028628714706L;

    /**
     * Creates a new instance of {@link CpdParser}.
     *
     * @param highThreshold
     *         minimum number of duplicate lines for high priority warnings
     * @param normalThreshold
     *         minimum number of duplicate lines for normal priority warnings
     */
    public CpdParser(final int highThreshold, final int normalThreshold) {
        super(highThreshold, normalThreshold);
    }

    /**
     * Creates a new instance of {@link CpdParser}. The {@code highThreshold} is set to 50, the {@code normalThreshold}
     * is set to 25.
     */
    public CpdParser() {
        super(50, 25);
    }

    @Override
    protected void configureParser(final Digester digester) {
        var duplicationXPath = "*/pmd-cpd/duplication";
        digester.addObjectCreate(duplicationXPath, Duplication.class);
        digester.addSetProperties(duplicationXPath);
        digester.addCallMethod(duplicationXPath + "/codefragment", "setCodeFragment", 0);
        digester.addSetNext(duplicationXPath, "add");

        var fileXPath = duplicationXPath + "/file";
        digester.addObjectCreate(fileXPath, SourceFile.class);
        digester.addSetProperties(fileXPath);
        digester.addSetNext(fileXPath, "addFile", SourceFile.class.getName());
    }

    @Override
    protected Report convertDuplicationsToIssues(final List<Duplication> duplications, final IssueBuilder issueBuilder) {
        var report = new Report();

        for (Duplication duplication : duplications) {
            var group = new DuplicationGroup(duplication.getCodeFragment());
            for (SourceFile file : duplication.getFiles()) {
                issueBuilder.setSeverity(getPriority(duplication.getLines()))
                        .setLineStart(file.getLine())
                        .setLineEnd(file.getLine() + duplication.getLines() - 1)
                        .setFileName(file.getPath())
                        .setType("CPD")
                        .setAdditionalProperties(group);
                var issue = issueBuilder.build();
                group.add(issue);
                report.add(issue);
            }
        }
        return report;
    }

    /**
     * Java Bean class for a file of the PMD CPD format.
     *
     * @author Ullrich Hafner
     */
    @SuppressWarnings("PMD.DataClass")
    public static class SourceFile {
        /** Starting line number in file. */
        private int line;
        /** Path of the file. */
        @CheckForNull
        private String path;

        /**
         * Returns the path of this file.
         *
         * @return the path of this file
         */
        @CheckForNull
        public String getPath() {
            return path;
        }

        /**
         * Sets the path of this file to the specified value.
         *
         * @param path
         *         the value to set
         */
        public void setPath(@CheckForNull final String path) {
            this.path = path;
        }

        /**
         * Returns the line of the duplication.
         *
         * @return the line of the duplication
         */
        public int getLine() {
            return line;
        }

        /**
         * Sets the line of the duplication to the specified value.
         *
         * @param line
         *         the value to set
         */
        public void setLine(final int line) {
            this.line = line;
        }
    }

    /**
     * Java Bean class for a CPD duplication.
     *
     * @author Ullrich Hafner
     */
    @SuppressWarnings("PMD.DataClass")
    public static class Duplication {
        /** Number of duplicate lines. */
        private int lines;
        /** Number of duplicate tokens. */
        private int tokens;
        /** The duplicated code fragment. */
        @CheckForNull
        private String codeFragment;

        /** All files of this duplication. */
        private final List<SourceFile> files = new ArrayList<>();

        /**
         * Adds a new file to this duplication.
         *
         * @param file
         *            the new file
         */
        public void addFile(final SourceFile file) {
            files.add(file);
        }

        /**
         * Returns all files of the duplication. The returned collection is
         * read-only.
         *
         * @return all files
         */
        public Collection<SourceFile> getFiles() {
            return Collections.unmodifiableCollection(files);
        }

        /**
         * Returns the number of duplicate lines.
         *
         * @return the lines
         */
        public int getLines() {
            return lines;
        }

        /**
         * Sets the number of duplicate lines to the specified value.
         *
         * @param lines the value to set
         */
        public void setLines(final int lines) {
            this.lines = lines;
        }

        /**
         * Returns the number of duplicate tokens.
         *
         * @return the tokens
         */
        public int getTokens() {
            return tokens;
        }

        /**
         * Sets the number of duplicate tokens to the specified value.
         *
         * @param tokens the value to set
         */
        public void setTokens(final int tokens) {
            this.tokens = tokens;
        }

        /**
         * Returns the duplicate code fragment.
         *
         * @return the duplicate code fragment
         */
        @CheckForNull
        public String getCodeFragment() {
            return codeFragment;
        }

        /**
         * Sets the duplicate code fragment to the specified value.
         *
         * @param codeFragment the value to set
         */
        public void setCodeFragment(@CheckForNull final String codeFragment) {
            this.codeFragment = codeFragment;
        }
    }
}
