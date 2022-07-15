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
public class RevapiInfoExtension implements Serializable {

    private static final long serialVersionUID = 6058160289391492934L;

    private final Map<String, String> severities = new HashMap<>();

    private String issueName = StringUtils.EMPTY;
    private String oldFile = StringUtils.EMPTY;
    private String newFile = StringUtils.EMPTY;

    /**
     * Creates an object to hold additional revapi issue information.
     * @param code of the parsed issue
     */
    public RevapiInfoExtension(@CheckForNull final String code) {
        setCode(code);
    }

    /**
     * Sets the name of the issue.
     * @param name of the issue
     */
    public void setCode(@CheckForNull final String name) {
        if (StringUtils.isBlank(this.issueName)) {
            this.issueName = StringUtils.defaultString(name);
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

    /**
     * Sets all different severities. This can be: Source, Binary, Semantic, Others.
     * @param severities of the issue
     */
    public void setSeverities(@CheckForNull final Map<String, String> severities) {
        if (severities != null && !severities.isEmpty()) {
            this.severities.putAll(severities);
        }
    }

    /**
     * Sets the fileName in which the new change occurred.
     * @param newFile in which the change occurred
     */
    public void setNewFile(@CheckForNull final String newFile) {
        this.newFile = StringUtils.defaultString(newFile);
    }

    /**
     * Sets the oldFile which is affected by the issue.
     * @param oldFile in which the change occurred
     */
    public void setOldFile(@CheckForNull final String oldFile) {
        this.oldFile = StringUtils.defaultString(oldFile);
    }

}
