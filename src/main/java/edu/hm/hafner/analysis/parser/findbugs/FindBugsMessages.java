package edu.hm.hafner.analysis.parser.findbugs;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.xml.sax.SAXException;

import edu.hm.hafner.analysis.SecureDigester;
import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Parses the FindBugs pattern descriptions and provides access to these HTML messages.
 *
 * @author Ullrich Hafner
 */
@SuppressWarnings("PMD.DataClass")
public final class FindBugsMessages {
    private static final String NO_MESSAGE_FOUND = "no message found";

    /** Maps a key to HTML description. */
    private final Map<String, String> messages = new HashMap<>();
    private final Map<String, String> shortMessages = new HashMap<>();

    /**
     * Loads the available rules into a map.
     */
    @SuppressWarnings("all")
    @SuppressFBWarnings("DE_MIGHT_IGNORE")
    public FindBugsMessages() {
        try {
            loadMessages("messages.xml", messages, shortMessages);
            loadMessages("fb-contrib-messages.xml", messages, shortMessages);
            loadMessages("find-sec-bugs-messages.xml", messages, shortMessages);
        }
        catch (Exception ignored) {
            // ignore failures
        }
    }

    private void loadMessages(final String fileName, final Map<String, String> messagesCache,
            final Map<String, String> shortMessagesCache) throws IOException, SAXException {
        try (var file = FindBugsMessages.class.getResourceAsStream(fileName)) {
            List<Pattern> patterns = parse(file);
            for (Pattern pattern : patterns) {
                messagesCache.put(pattern.getType(), pattern.getDescription());
                shortMessagesCache.put(pattern.getType(), pattern.getShortDescription());
            }
        }
    }

    /**
     * Parses the FindBugs pattern description.
     *
     * @param file
     *         XML file with the messages
     *
     * @return a list of parsed patterns
     * @throws SAXException
     *         if we can't parse the file
     * @throws IOException
     *         if we can't read the file
     */
    public List<Pattern> parse(final InputStream file) throws IOException, SAXException {
        var digester = new SecureDigester(FindBugsMessages.class);
        List<Pattern> patterns = new ArrayList<>();
        digester.push(patterns);

        var startPattern = "*/BugPattern";
        digester.addObjectCreate(startPattern, Pattern.class);
        digester.addSetProperties(startPattern);
        digester.addCallMethod("*/BugPattern/Details", "setDescription", 0);
        digester.addCallMethod("*/BugPattern/ShortDescription", "setShortDescription", 0);
        digester.addSetNext(startPattern, "add");

        digester.parse(file);

        return patterns;
    }

    /**
     * Returns a HTML description for the specified bug.
     *
     * @param name
     *         name of the bug
     * @return a HTML description
     */
    public String getMessage(final String name) {
        return StringUtils.defaultIfEmpty(messages.get(name), NO_MESSAGE_FOUND);
    }

    /**
     * Returns a short description for the specified bug.
     *
     * @param name
     *         name of the bug
     * @return a HTML description for the specified bug.
     */
    public String getShortMessage(final String name) {
        return StringUtils.defaultIfEmpty(shortMessages.get(name), NO_MESSAGE_FOUND);
    }

    /**
     * Returns the size of this messages cache.
     *
     * @return the number of stored messages (English locale)
     */
    public int size() {
        return messages.size();
    }

    /**
     * Bug pattern describing a bug type.
     *
     * @author Ullrich Hafner
     */
    public static class Pattern {
        @CheckForNull
        private String type;
        @CheckForNull
        private String description;
        @CheckForNull
        private String shortDescription;

        /**
         * Returns the type.
         *
         * @return the type
         */
        public String getType() {
            return StringUtils.defaultString(type);
        }

        /**
         * Sets the type to the specified value.
         *
         * @param type
         *         the value to set
         */
        public void setType(@CheckForNull final String type) {
            this.type = type;
        }

        /**
         * Returns the description.
         *
         * @return the description
         */
        public String getDescription() {
            return StringUtils.defaultString(description);
        }

        /**
         * Sets the description to the specified value.
         *
         * @param description
         *         the value to set
         */
        public void setDescription(@CheckForNull final String description) {
            this.description = description;
        }

        /**
         * Returns the shortDescription.
         *
         * @return the shortDescription
         */
        public String getShortDescription() {
            return StringUtils.defaultString(shortDescription);
        }

        /**
         * Sets the shortDescription to the specified value.
         *
         * @param shortDescription
         *         the value to set
         */
        public void setShortDescription(@CheckForNull final String shortDescription) {
            this.shortDescription = shortDescription;
        }
    }
}
