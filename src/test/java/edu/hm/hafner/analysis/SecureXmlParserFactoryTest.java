package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests the class {@link SecureXmlParserFactory}.
 *
 * @author Ullrich Hafner
 */
class SecureXmlParserFactoryTest {
    @Test
    void shouldCreateDocumentBuilder() {
        SecureXmlParserFactory factory = new SecureXmlParserFactory();

        assertThat(factory.createDocumentBuilder()).isNotNull();
    }

    @Test
    void shouldCreateSaxParser() {
        SecureXmlParserFactory factory = new SecureXmlParserFactory();

        assertThat(factory.createSaxParser()).isNotNull();
    }

    @Test
    void shouldCreateXmlInputFactory() {
        SecureXmlParserFactory factory = new SecureXmlParserFactory();

        assertThat(factory.createXmlInputFactory()).isNotNull();
    }
}
