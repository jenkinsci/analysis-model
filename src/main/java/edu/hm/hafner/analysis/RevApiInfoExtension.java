package edu.hm.hafner.analysis;

import edu.umd.cs.findbugs.annotations.CheckForNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Stores additional information of the Revapi analysis (severities, issue name, old file, and new file).
 */
@SuppressWarnings("PMD.DataClass")
public final class RevApiInfoExtension implements Serializable {
    @Serial
    private static final long serialVersionUID = 6058160289391492934L;
    @SuppressWarnings("serial")
    private final Map<String, String> severities = new HashMap<>();
    private final String issueName;
    private final String oldFile;
    private final String newFile;

    /**
     * Creates an object to store additional information of the Revapi analysis.
     *
     * @param code
     *         of the parsed issue
     * @param oldFile
     *         the oldFile where something was changed
     * @param newFile
     *         the newFile where something was changed
     * @param severities
     *         the severities of Binary and source
     */
    public RevApiInfoExtension(@CheckForNull final String code, final String oldFile,
            final String newFile, final Map<String, String> severities) {
        this.issueName = Objects.toString(code, "-");
        this.oldFile = oldFile;
        this.newFile = newFile;
        this.severities.putAll(severities);
    }

    public Map<String, String> getSeverities() {
        return Collections.unmodifiableMap(severities);
    }

    public String getIssueName() {
        return issueName;
    }

    public String getOldFile() {
        return oldFile;
    }

    public String getNewFile() {
        return newFile;
    }
}
