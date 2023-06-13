package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.input.ReaderInputStream;
import org.junit.jupiter.api.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link JavacParser}.
 */
class ReaderTest {
    @Test
    void shouldThrowIoException() throws IOException, ParserConfigurationException, SAXException {
        var reader = new StringReader("");

        var inputStream = ReaderInputStream.builder().setCharset(StandardCharsets.UTF_8).setReader(reader).get();
        var inputSource = new InputSource(inputStream);

        assertThat(DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputSource)).isNotNull();
    }

    @Test
    void shouldHandleEmptyReader() {
        var reader = new StringReader("");

        var inputSource = new InputSource(reader);

        assertThatExceptionOfType(SAXException.class).isThrownBy(
                () -> DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputSource)).isNotNull();
    }
}

