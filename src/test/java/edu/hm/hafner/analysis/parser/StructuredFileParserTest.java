package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

import static org.assertj.core.api.Assertions.*;

/**
 * A base class for parsers that cannot process simple text files.
 *
 * @author Ullrich Hafner
 */
public abstract class StructuredFileParserTest extends AbstractParserTest {
    StructuredFileParserTest(final String fileWithIssuesName) {
        super(fileWithIssuesName);
    }

    @Test
    void shouldNotAcceptTextFiles() {
        assertThat(createParser().accepts(createReaderFactory("gcc.txt"))).isFalse();
    }

    @Test
    void shouldThrowParserException() {
        assertThatThrownBy(() -> createParser().parse(createReaderFactory("issues-invalid.json")))
                .isInstanceOf(ParsingException.class);
        assertThatThrownBy(() -> createParser().parse(createReaderFactory("issues-broken.json")))
                .isInstanceOf(ParsingException.class);
    }
}
