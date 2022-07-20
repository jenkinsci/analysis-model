package edu.hm.hafner.analysis;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Stores the additional information of a parsed issue.
 * (Severities, issueName, oldFile, newFile)
 */
@SuppressWarnings("PMD.DataClass")
public final class RevApiInfoExtension implements Serializable {

    private static final long serialVersionUID = 6058160289391492934L;
    private final Map<String, String> severities = new HashMap<>();
    private final String issueName;
    private final String oldFile;
    private final String newFile;

    /**
     *
     * Creates an object to hold additional Revapi issue information.
     * @param code of the parsed issue
     * @param oldFile the oldFile where something was changed
     * @param newFile the newFile where something was changed
     * @param severities the severities of Binary and source
     */
    public RevApiInfoExtension(@CheckForNull final String code, @CheckForNull final String oldFile, @CheckForNull final String newFile, @CheckForNull final Map<String, String> severities) {
        this.issueName = StringUtils.defaultString(code);
        this.oldFile = StringUtils.defaultString(oldFile);
        this.newFile = StringUtils.defaultString(newFile);
        if (severities != null && !severities.isEmpty()) {
            this.severities.putAll(severities);
        }
    }

    public Map<String, String> getSeverities() {
        return this.severities;
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
