package edu.hm.hafner.analysis.parser.findbugs;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import edu.hm.hafner.analysis.parser.findbugs.FindBugsMessages.Pattern;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests the class {@link FindBugsMessages}.
 */
class FindBugsMessagesTest {
    /** Bug ID for test. */
    private static final String NP_STORE_INTO_NONNULL_FIELD = "NP_STORE_INTO_NONNULL_FIELD";
    /** Expected number of patterns. */
    private static final int EXPECTED_PATTERNS = 458;
    /** Expected number of patterns in fb-contrib. */
    private static final int EXPECTED_CONTRIB_PATTERNS = 307;
    /** Expected number of patterns in find-sec-bugs. */
    private static final int EXPECTED_SECURITY_PATTERNS = 135;
    private static final String PATH_TRAVERSAL_IN = "PATH_TRAVERSAL_IN";

    @Test
    void shouldReadAllMessageFiles() {
        assertThat(new FindBugsMessages().size()).isEqualTo(
                EXPECTED_PATTERNS + EXPECTED_CONTRIB_PATTERNS + EXPECTED_SECURITY_PATTERNS);
    }

    @Test
    void shouldReadAllFindBugsMessages() {
        assertThat(readMessages("messages.xml")).hasSize(EXPECTED_PATTERNS);
    }

    @Test
    void shouldReadAllFindSecBugsMessages() {
        assertThat(readMessages("find-sec-bugs-messages.xml")).hasSize(EXPECTED_SECURITY_PATTERNS);
    }

    @Test
    void shouldReadAllFbContribMessages() {
        assertThat(readMessages("fb-contrib-messages.xml")).hasSize(EXPECTED_CONTRIB_PATTERNS);
    }

    @Test
    void shouldMapMessagesToTypes() {
        var messages = new FindBugsMessages();
        var expectedMessage = "A value that could be null is stored into a field that has been annotated as @Nonnull.";
        assertThat(messages.getMessage(NP_STORE_INTO_NONNULL_FIELD))
                .contains(expectedMessage);

        assertThat(messages.getShortMessage(NP_STORE_INTO_NONNULL_FIELD))
                .isEqualTo("Store of null value into field annotated @Nonnull");

        assertThat(messages.getMessage("NMCS_NEEDLESS_MEMBER_COLLECTION_SYNCHRONIZATION"))
                .contains("This class defines a private collection member as synchronized. It appears");
        assertThat(messages.getShortMessage("NMCS_NEEDLESS_MEMBER_COLLECTION_SYNCHRONIZATION"))
                .isEqualTo("Class defines unneeded synchronization on member collection");
    }

    @Test
    void issue55707() {
        var messages = new FindBugsMessages();
        assertThat(messages.getShortMessage(PATH_TRAVERSAL_IN))
                .isEqualTo("Potential Path Traversal (file read)");
        assertThat(messages.getMessage(PATH_TRAVERSAL_IN))
                .contains("A file is opened to read its content. The filename comes from an <b>input</b> parameter.");
    }

    private List<Pattern> readMessages(final String fileName) {
        try (var file = read(fileName)) {
            return new FindBugsMessages().parse(file);
        }
        catch (IOException | SAXException e) {
            throw new AssertionError(e);
        }
    }

    private InputStream read(final String fileName) {
        return FindBugsMessages.class.getResourceAsStream(fileName);
    }
}
