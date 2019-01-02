package edu.hm.hafner.analysis.parser.gendarme;

import edu.umd.cs.findbugs.annotations.Nullable;

@SuppressWarnings("all")
public class DotNetAssembly {
    private final String fullName;
    @Nullable
    private String name;
    @Nullable
    private String version;
    @Nullable
    private String culture;
    @Nullable
    private String publicKeyToken;

    @SuppressWarnings("StringSplitter")
    public DotNetAssembly(final String fullName) {
        this.fullName = fullName;
        String[] splitted = this.fullName.split(",");
        int cpt = 0;
        for (String s : splitted) {
            if (cpt == 0) {
                name = s.trim();
            }
            else {
                String[] keyValue = s.trim().split("=");
                switch (keyValue[0]) {
                    case "Version":
                        version = keyValue[1];
                        break;
                    case "Culture":
                        culture = keyValue[1];
                        break;
                    case "PublicKeyToken":
                        publicKeyToken = keyValue[1];
                        break;
                    default:
                        // skip
                }
            }
            cpt++;
        }
    }

    public String getFullName() {
        return fullName;
    }

    @Nullable
    public String getName() {
        return name;
    }

    @Nullable
    public String getVersion() {
        return version;
    }

    @Nullable
    public String getCulture() {
        return culture;
    }

    @Nullable
    public String getPublicKeyToken() {
        return publicKeyToken;
    }
}
