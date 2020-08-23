package edu.hm.hafner.analysis.parser.gendarme;

import java.net.URL;

import edu.umd.cs.findbugs.annotations.CheckForNull;

@SuppressWarnings("all")
public class GendarmeRule {
    @CheckForNull
    private String name;
    @CheckForNull
    private String typeName;
    @CheckForNull
    private GendarmeRuleType type;
    @CheckForNull
    private URL url;

    @CheckForNull
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(final String typeName) {
        this.typeName = typeName;
    }

    @CheckForNull
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @CheckForNull
    public GendarmeRuleType getType() {
        return type;
    }

    public void setType(final GendarmeRuleType type) {
        this.type = type;
    }

    @CheckForNull
    public URL getUrl() {
        return url;
    }

    public void setUrl(@CheckForNull final URL url) {
        this.url = url;
    }
}
