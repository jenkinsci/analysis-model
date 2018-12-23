package edu.hm.hafner.analysis.parser.fxcop;

import edu.umd.cs.findbugs.annotations.Nullable;

/**
 * Internal model for a FxCop rule.
 *
 * @author Erik Ramfelt
 */
@SuppressWarnings({"PMD", "all", "CheckStyle"})
public class FxCopRule {
    private String typeName;
    private String category;
    private String checkId;
    @Nullable
    private String name;
    @Nullable
    private String url;
    @Nullable
    private String description;

    public FxCopRule(final String typeName, final String category, final String checkId) {
        this.typeName = typeName;
        this.category = category;
        this.checkId = checkId;
    }

    @Nullable
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Nullable
    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    @Nullable
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
