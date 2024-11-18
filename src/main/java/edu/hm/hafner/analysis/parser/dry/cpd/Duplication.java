package edu.hm.hafner.analysis.parser.dry.cpd;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Java Bean class for a CPD duplication.
 *
 * @author Ullrich Hafner
 */
@SuppressWarnings("PMD.DataClass")
public class Duplication {
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
