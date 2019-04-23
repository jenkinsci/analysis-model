package edu.hm.hafner.analysis.parser.pmd;

import edu.umd.cs.findbugs.annotations.Nullable;

/**
 * Java Bean class for an error of the PMD format.
 *
 * @author Ullrich Hafner
 */
@SuppressWarnings("InstanceVariableMayNotBeInitialized")
public class Error {
    @Nullable
    private String filename;
    @Nullable
    private String msg;
    @Nullable
    private String description;

    @Nullable
    public String getFilename() {
        return filename;
    }

    public void setFilename(@Nullable final String filename) {
        this.filename = filename;
    }

    @Nullable
    public String getMsg() {
        return msg;
    }

    public void setMsg(@Nullable final String msg) {
        this.msg = msg;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(@Nullable final String description) {
        this.description = description;
    }
}
