package edu.hm.hafner.analysis;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import static edu.hm.hafner.analysis.assertj.Assertions.*;

/**
 * Tests the class {@link FileReaderFactory}.
 *
 * @author Michael Schmid
 */
class FileReaderFactoryTest {
    @Test
    void useDefinedEncodingUtf8() {
        FileReaderFactory sut = new FileReaderFactory(
                new File("src/test/resources/edu/hm/hafner/analysis/encoded-with-UTF8.xml").toPath(),
                StandardCharsets.UTF_8);
        assertEncoding(sut, StandardCharsets.UTF_8);
    }

    @Test
    void detectEncodingOfUtf8XmlFile() {
        FileReaderFactory sut = new FileReaderFactory(
                new File("src/test/resources/edu/hm/hafner/analysis/encoded-with-UTF8.xml").toPath());
        assertEncoding(sut, StandardCharsets.UTF_8);
    }

    @Test
    void useDefinedEncodingIso88591() {
        FileReaderFactory sut = new FileReaderFactory(
                new File("src/test/resources/edu/hm/hafner/analysis/encoded-with-ISO8859-1.xml").toPath(),
                StandardCharsets.ISO_8859_1);
        assertEncoding(sut, StandardCharsets.ISO_8859_1);
    }

    @Test
    void detectEncodingOfIso88591XmlFile() {
        FileReaderFactory sut = new FileReaderFactory(
                new File("src/test/resources/edu/hm/hafner/analysis/encoded-with-ISO8859-1.xml").toPath());
        assertEncoding(sut, StandardCharsets.ISO_8859_1);
    }

    @Test
    void detectEncodingWithoutEncodingXmlFile() {
        FileReaderFactory sut = new FileReaderFactory(
                new File("src/test/resources/edu/hm/hafner/analysis/encoded-without-encoding.xml").toPath());
        assertEncoding(sut, StandardCharsets.UTF_8);
    }

    private void assertEncoding(final FileReaderFactory sut, final Charset charset) {
        Document document = sut.readDocument();
        assertThat(sut.getCharset()).isEqualTo(charset);
        assertThat(document.getElementsByTagName("text").item(0).getChildNodes().item(0).getNodeValue())
                .isEqualTo("aä");
    }

}