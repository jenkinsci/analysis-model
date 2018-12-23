package edu.hm.hafner.analysis.parser.gendarme;

import java.net.URL;

import edu.umd.cs.findbugs.annotations.Nullable;

@SuppressWarnings("all")
public class GendarmeRule {
    @Nullable
    private String name;
    @Nullable
    private String typeName;
    @Nullable
    private GendarmeRuleType type;
    @Nullable
    private URL url;

    @Nullable
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(final String typeName) {
        this.typeName = typeName;
    }

    @Nullable
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Nullable
    public GendarmeRuleType getType() {
        return type;
    }

    public void setType(final GendarmeRuleType type) {
        this.type = type;
    }

    @Nullable
    public URL getUrl() {
        return url;
    }

    public void setUrl(@Nullable final URL url) {
        this.url = url;
    }
}
