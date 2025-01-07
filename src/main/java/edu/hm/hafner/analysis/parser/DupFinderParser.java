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
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * A parser for Reshaper Dupfinder XML files.
 *
 * @author Rafal Jasica
 */
public class DupFinderParser extends AbstractDryParser<DupFinderParser.Duplicate> {
    /** Unique ID of this class. */
    @Serial
    private static final long serialVersionUID = 1357147358617711901L;

    /**
     * Creates a new instance of {@link DupFinderParser}.
     *
     * @param highThreshold
     *         minimum number of duplicate lines for high priority warnings
     * @param normalThreshold
     *         minimum number of duplicate lines for normal priority warnings
     */
    public DupFinderParser(final int highThreshold, final int normalThreshold) {
        super(highThreshold, normalThreshold);
    }

    /**
     * Creates a new instance of {@link DupFinderParser}. The {@code highThreshold} is set to 50, the {@code normalThreshold}
     * is set to 25.
     */
    public DupFinderParser() {
        super(50, 25);
    }

    @Override
    protected void configureParser(final Digester digester) {
        var duplicationXPath = "*/DuplicatesReport/Duplicates/Duplicate";
        digester.addObjectCreate(duplicationXPath, Duplicate.class);
        digester.addSetProperties(duplicationXPath, "Cost", "cost");
        digester.addSetNext(duplicationXPath, "add");

        var fragmentXPath = duplicationXPath + "/Fragment";
        digester.addObjectCreate(fragmentXPath, Fragment.class);
        digester.addBeanPropertySetter(fragmentXPath + "/FileName", "fileName");
        digester.addBeanPropertySetter(fragmentXPath + "/Text", "text");
        digester.addSetNext(fragmentXPath, "addFragment", Fragment.class.getName());

        var lineRangeXPath = fragmentXPath + "/LineRange";
        digester.addObjectCreate(lineRangeXPath, Range.class);
        digester.addSetProperties(lineRangeXPath, "Start", "start");
        digester.addSetProperties(lineRangeXPath, "End", "end");
        digester.addSetNext(lineRangeXPath, "setLineRange", Range.class.getName());

        var offsetRangeXPath = fragmentXPath + "/OffsetRange";
        digester.addObjectCreate(offsetRangeXPath, Range.class);
        digester.addSetProperties(offsetRangeXPath, "Start", "start");
        digester.addSetProperties(offsetRangeXPath, "End", "end");
        digester.addSetNext(offsetRangeXPath, "setOffsetRange", Range.class.getName());
    }

    @Override
    protected Report convertDuplicationsToIssues(final List<Duplicate> duplications, final IssueBuilder issueBuilder) {
        var report = new Report();

        for (Duplicate duplication : duplications) {
            var group = new DuplicationGroup();
            for (Fragment fragment : duplication.getFragments()) {
                group.setCodeFragment(fragment.getText());
                var lineRange = fragment.getLineRange();
                int count = lineRange.getEnd() - lineRange.getStart() + 1;
                issueBuilder.setSeverity(getPriority(count))
                        .setLineStart(lineRange.getStart())
                        .setLineEnd(lineRange.getEnd())
                        .setFileName(fragment.getFileName())
                        .setType("DupFinder")
                        .setAdditionalProperties(group);
                var issue = issueBuilder.build();
                group.add(issue);
                report.add(issue);
            }
        }

        return report;
    }

    /**
     * Java Bean class for a Reshaper DupFinder duplicate.
     *
     * @author Rafal Jasica
     */
    public static class Duplicate {
        /** The duplicated cost. */
        private int cost;

        /** All files of this duplication. */
        private final List<Fragment> fragments = new ArrayList<>();

        /**
         * Returns the duplicate cost.
         *
         * @return the duplicate cost
         */
        public int getCost() {
            return cost;
        }

        /**
         * Sets the duplicate cost to the specified value.
         *
         * @param cost the value to set
         */
        public void setCost(final int cost) {
            this.cost = cost;
        }

        /**
         * Adds a new file to this duplication.
         *
         * @param file
         *            the new file
         */
        public void addFragment(final Fragment file) {
            fragments.add(file);
        }

        /**
         * Returns all files of the duplication. The returned collection is
         * read-only.
         *
         * @return all files
         */
        public Collection<Fragment> getFragments() {
            return Collections.unmodifiableCollection(fragments);
        }
    }

    /**
     * Java Bean class for a Reshaper DupFinder fragment.
     *
     * @author Rafal Jasica
     */
    @SuppressWarnings("PMD.DataClass")
    @SuppressFBWarnings("EI")
    public static class Fragment {
        @CheckForNull
        private String fileName;
        @CheckForNull
        private String text;
        @CheckForNull
        private Range lineRange;
        @CheckForNull
        private Range offsetRange;

        /**
         * Returns the file name.
         *
         * @return the path of this file
         */
        @CheckForNull
        public String getFileName() {
            return fileName;
        }

        /**
         * Sets the file name to the specified value.
         *
         * @param fileName the value to set
         */
        @SuppressFBWarnings("NM")
        public void setFileName(@CheckForNull final String fileName) {
            this.fileName = fileName;
        }

        /**
         * Returns the text.
         *
         * @return the text
         */
        @CheckForNull
        public String getText() {
            return text;
        }

        /**
         * Sets the text to the specified value.
         *
         * @param text the value to set
         */
        public void setText(@CheckForNull final String text) {
            this.text = text;
        }

        /**
         * Returns the line range.
         *
         * @return the line range
         */
        public Range getLineRange() {
            if (lineRange == null) {
                return new Range();
            }
            return lineRange;
        }

        /**
         * Sets the line range to the specified value.
         *
         * @param lineRange the value to set
         */
        public void setLineRange(@CheckForNull final Range lineRange) {
            this.lineRange = lineRange;
        }

        /**
         * Returns the offset range.
         *
         * @return the offset range
         */
        @CheckForNull
        public Range getOffsetRange() {
            return offsetRange;
        }

        /**
         * Sets the offset range to the specified value.
         *
         * @param offsetRange the value to set
         */
        public void setOffsetRange(@CheckForNull final Range offsetRange) {
            this.offsetRange = offsetRange;
        }
    }

    /**
     * Java Bean class for a Reshaper DupFinder range.
     *
     * @author Rafal Jasica
     */
    @SuppressWarnings("PMD.DataClass")
    public static class Range {
        private int start;
        private int end;

        /**
         * Returns the start.
         *
         * @return the start
         */
        public int getStart() {
            return start;
        }

        /**
         * Sets the start to the specified value.
         *
         * @param start the value to set
         */
        public void setStart(final int start) {
            this.start = start;
        }

        /**
         * Returns the line range start.
         *
         * @return the line range start
         */
        public int getEnd() {
            return end;
        }

        /**
         * Sets the end to the specified value.
         *
         * @param end the value to set
         */
        public void setEnd(final int end) {
            this.end = end;
        }
    }
}
