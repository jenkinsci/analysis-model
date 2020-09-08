package edu.hm.hafner.analysis.parser.fxcop;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Internal model for a FxCop rule.
 *
 * @author Erik Ramfelt
 */
@SuppressWarnings({"PMD", "all", "CheckStyle"})
public class FxCopRule {
    private final String typeName;
    private final String category;
    private final String checkId;
    @CheckForNull
    private String name;
    @CheckForNull
    private String url;
    @CheckForNull
    private String description;

    public FxCopRule(final String typeName, final String category, final String checkId) {
        this.typeName = typeName;
        this.category = category;
        this.checkId = checkId;
    }

    @CheckForNull
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @CheckForNull
    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    @CheckForNull
    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getCategory() {
        return category;
    }

    public String getCheckId() {
        return checkId;
    }
}
