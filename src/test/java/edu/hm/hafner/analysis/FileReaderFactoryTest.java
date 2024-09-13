package edu.hm.hafner.analysis;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import com.google.errorprone.annotations.MustBeClosed;

import edu.hm.hafner.util.ResourceTest;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link FileReaderFactory}.
 *
 * @author Michael Schmid
 */
class FileReaderFactoryTest extends ResourceTest {
    private static final Charset UTF_8 = StandardCharsets.UTF_8;

    @Test
    void shouldRemoveColorCodesAfterAllLineMappers() {
        var factory = new ConsoleLogReaderFactory(
                getResourceAsFile("ath-colored.log"));

        assertThat(factory.readString()).isEqualTo("[WARNING]\n"
                + "[WARNING] Some problems were encountered while building the effective model for edu.hm.hafner.irrelevant.groupId:random-artifactId:jar:1.0\n"
                + "[WARNING] 'build.plugins.plugin.version' for org.apache.maven.plugins:maven-compiler-plugin is missing. @ line 13, column 15\n"
                + "[WARNING] \n"
                + "[WARNING] It is highly recommended to fix these problems because they threaten the stability of your build.\n"
                + "[WARNING] \n"
                + "[WARNING] For this reason, future Maven versions might no longer support building such malformed projects.\n"
                + "[WARNING] ");
    }

    @Test
    void shouldNotAccessInternet() {
        FileReaderFactory factory = createFactory("eclipse-withinfo.xml", UTF_8);
        Document document = factory.readDocument();

        assertThat(document).isNotNull();
    }

    @Test
    void useDefinedEncodingUtf8() {
        FileReaderFactory factory = createFactory("encoded-with-UTF8.xml", UTF_8);

        assertEncoding(factory, UTF_8);
    }

    @Test
    void detectEncodingOfUtf8XmlFile() {
        FileReaderFactory factory = createFactory("encoded-with-UTF8.xml");
        assertEncoding(factory, UTF_8);
    }

    @Test
    void useDefinedEncodingIso88591() {
        FileReaderFactory factory = createFactory("encoded-with-ISO8859-1.xml", StandardCharsets.ISO_8859_1);
        assertEncoding(factory, StandardCharsets.ISO_8859_1);
    }

    @Test
    void detectEncodingOfIso88591XmlFile() {
        FileReaderFactory factory = createFactory("encoded-with-ISO8859-1.xml");
        assertEncoding(factory, StandardCharsets.ISO_8859_1);
    }

    @Test
    void detectEncodingWithoutEncodingXmlFile() {
        FileReaderFactory factory = createFactory("encoded-without-encoding.xml");
        assertEncoding(factory, UTF_8);
    }

    @Test
    void detectEncodingOfTextFile() {
        FileReaderFactory factory = createFactory("context.txt");
        factory.readString();
        assertThat(factory.getCharset()).isEqualTo(UTF_8);
    }

    private void assertEncoding(final FileReaderFactory factory, final Charset charset) {
        Document document = factory.readDocument();
        assertThat(factory.getCharset()).isEqualTo(charset);
        assertThat(document.getElementsByTagName("text").item(0).getChildNodes().item(0).getNodeValue())
                .isEqualTo("aä");
    }

    private FileReaderFactory createFactory(final String fileName) {
        return new FileReaderFactory(getResourceAsFile(fileName));
    }

    private FileReaderFactory createFactory(final String fileName, final Charset charset) {
        return new FileReaderFactory(getResourceAsFile(fileName), charset);
    }

    /** Removes Jenkins Console Log notes. */
    static class ConsoleLogReaderFactory extends ReaderFactory {
        private final Path log;

        ConsoleLogReaderFactory(final Path log) {
            super(StandardCharsets.UTF_8, ConsoleLogReaderFactory::removeNotes);
            this.log = log;
        }

        private static final String PREAMBLE_STR = "\u001B[8mha:";
        private static final String POSTAMBLE_STR = "\u001B[0m";

        private static String removeNotes(final String wholeLine) {
            String line = wholeLine;
            while (true) {
                int start = line.indexOf(PREAMBLE_STR);
                if (start < 0) {
                    return line;
                }
                int end = line.indexOf(POSTAMBLE_STR, start);
                if (end < 0) {
                    return line;
                }
                line = line.substring(0, start) + line.substring(end + POSTAMBLE_STR.length());
            }
        }

        @Override
        public String getFileName() {
            return "-";
        }

        @Override
        @MustBeClosed
        public Reader create() {
            try {
                return Files.newBufferedReader(log);
            }
            catch (IOException e) {
                throw new ParsingException(e);
            }
        }
    }
}
