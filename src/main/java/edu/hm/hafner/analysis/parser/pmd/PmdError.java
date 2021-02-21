package edu.hm.hafner.analysis.parser.pmd;

import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Java Bean class for an error of the PMD format.
 *
 * @author Ullrich Hafner
 */
@SuppressWarnings({"InstanceVariableMayNotBeInitialized", "PMD.DataClass"})
public class PmdError {
    @CheckForNull
    private String filename;
    @CheckForNull
    private String msg;
    @CheckForNull
    private String description;

    @CheckForNull
    @SuppressFBWarnings("NM")
    public String getFilename() {
        return filename;
    }

    @SuppressFBWarnings("NM")
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
