package edu.hm.hafner.analysis.parser.gendarme;

@SuppressWarnings("all")
public class DotNetAssembly {
    private final String fullName;
    private String name;
    private String version;
    private String culture;
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

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getCulture() {
        return culture;
    }

    public String getPublicKeyToken() {
        return publicKeyToken;
    }
}
