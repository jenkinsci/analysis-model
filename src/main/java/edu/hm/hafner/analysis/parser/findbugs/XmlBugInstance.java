package edu.hm.hafner.analysis.parser.findbugs;

/**
 * Java Bean to create the mapping of hash codes to messages using the Digester
 * XML parser.
 *
 * @author Ulli Hafner
 */
@SuppressWarnings("all")
public class XmlBugInstance {
    private String instanceHash;
    private String message;
    private String type;
    private String category;

    public String getInstanceHash() {
        return instanceHash;
    }

    public void setInstanceHash(final String instanceHash) {
        this.instanceHash = instanceHash;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(final String category) {
        this.category = category;
    }

}
