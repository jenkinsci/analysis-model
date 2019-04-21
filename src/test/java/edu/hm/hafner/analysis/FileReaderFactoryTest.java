package edu.hm.hafner.analysis;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import edu.hm.hafner.util.ResourceTest;

import static edu.hm.hafner.analysis.assertj.Assertions.*;

/**
 * Tests the class {@link FileReaderFactory}.
 *
 * @author Michael Schmid
 */
class FileReaderFactoryTest extends ResourceTest {
    private static final Charset UTF_8 = StandardCharsets.UTF_8;

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
}