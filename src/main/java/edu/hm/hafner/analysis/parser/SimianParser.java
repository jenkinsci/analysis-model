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
 * A parser for Simian XML files.
 *
 * @author Ullrich Hafner
 */
public class SimianParser extends AbstractDryParser<SimianParser.Set> {
    /** Unique ID of this class. */
    @Serial
    private static final long serialVersionUID = 6507147028628714706L;

    /**
     * Creates a new instance of {@link SimianParser}.
     *
     * @param highThreshold
     *         minimum number of duplicate lines for high priority warnings
     * @param normalThreshold
     *         minimum number of duplicate lines for normal priority warnings
     */
    public SimianParser(final int highThreshold, final int normalThreshold) {
        super(highThreshold, normalThreshold);
    }

    /**
     * Creates a new instance of {@link SimianParser}. The {@code highThreshold} is set to 50, the {@code normalThreshold}
     * is set to 25.
     */
    public SimianParser() {
        super(50, 25);
    }

    @Override
    protected void configureParser(final Digester digester) {
        var duplicationXPath = "*/simian/check/set";
        digester.addObjectCreate(duplicationXPath, Set.class);
        digester.addSetProperties(duplicationXPath);
        digester.addSetNext(duplicationXPath, "add");

        var fileXPath = duplicationXPath + "/block";
        digester.addObjectCreate(fileXPath, Block.class);
        digester.addSetProperties(fileXPath);
        digester.addSetNext(fileXPath, "addBlock", Block.class.getName());
    }

    @Override
    protected Report convertDuplicationsToIssues(final List<Set> duplications, final IssueBuilder issueBuilder) {
        var report = new Report();

        for (Set duplication : duplications) {
            var group = new DuplicationGroup();
            for (Block file : duplication.getBlocks()) {
                issueBuilder.setSeverity(getPriority(duplication.getLineCount()))
                        .setLineStart(file.getStartLineNumber())
                        .setLineEnd(file.getEndLineNumber())
                        .setFileName(file.getSourceFile())
                        .setAdditionalProperties(group)
                        .setType("Simian");
                var issue = issueBuilder.build();
                group.add(issue);
                report.add(issue);
            }
        }
        return report;
    }

    /**
     * Java Bean class for a Simian duplication set.
     *
     * @author Ullrich Hafner
     */
    @SuppressWarnings("PMD.DataClass")
    public static class Set {
        private int lineCount;
        private final List<Block> blocks = new ArrayList<>();

        /**
         * Adds a new block to this duplication set.
         *
         * @param block
         *            the new block
         */
        public void addBlock(final Block block) {
            blocks.add(block);
        }

        /**
         * Returns all blocks of this duplication set. The returned collection is
         * read-only.
         *
         * @return all files
         */
        public Collection<Block> getBlocks() {
            return Collections.unmodifiableCollection(blocks);
        }

        /**
         * Returns the number of duplicated lines.
         *
         * @return the lineCount
         */
        public int getLineCount() {
            return lineCount;
        }

        /**
         * Sets the number of duplicated lines to the specified value.
         *
         * @param value the value to set
         */
        public void setLineCount(final int value) {
            lineCount = value;
        }
    }

    /**
     * Java Bean class for a duplicated block of a Simian duplication warning.
     *
     * @author Ullrich Hafner
     */
    @SuppressWarnings("PMD.DataClass")
    public static class Block {
        @CheckForNull
        private String sourceFile;
        private int startLineNumber;
        private int endLineNumber;

        /**
         * Returns the file name.
         *
         * @return the file name
         */
        @CheckForNull
        public String getSourceFile() {
            return sourceFile;
        }

        /**
         * Sets the file name to the specified value.
         *
         * @param sourceFile
         *            the value to set
         */
        public void setSourceFile(@CheckForNull final String sourceFile) {
            this.sourceFile = sourceFile;
        }

        /**
         * Returns the line number of the start of the duplication.
         *
         * @return the line number of the start of the duplication.
         */
        public int getStartLineNumber() {
            return startLineNumber;
        }

        /**
         * Sets the line number of the start of the duplication to the specified
         * value.
         *
         * @param startLineNumber
         *            the value to set
         */
        public void setStartLineNumber(final int startLineNumber) {
            this.startLineNumber = startLineNumber;
        }

        /**
         * Returns the line number of the end of the duplication.
         *
         * @return the line number of the end of the duplication.
         */
        public int getEndLineNumber() {
            return endLineNumber;
        }

        /**
         * Sets the line number of the end of the duplication to the specified
         * value.
         *
         * @param endLineNumber
         *            the value to set
         */
        public void setEndLineNumber(final int endLineNumber) {
            this.endLineNumber = endLineNumber;
        }
    }
}
