package edu.hm.hafner.analysis.registry;

import static org.assertj.core.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/**
 * Updates the list with the supported formats.
 *
 * @author Ullrich Hafner
 */
@SuppressWarnings({"NewClassNamingConvention", "PMD.ClassNamingConventions"})
class UpdateSupportedFormats {
    @Test
    void updateSupportedFormatsShouldUseCorrectFormatting() throws IOException {
        ParserRegistry.main();

        var formats = Files.readAllLines(Path.of("SUPPORTED-FORMATS.md"));

        assertThat(formats.get(0)).startsWith("<!--- DO NOT EDIT");
        assertThat(formats.get(1)).isEmpty();
        assertThat(formats.get(2)).isEqualTo("# Supported Report Formats");
        assertThat(formats.get(3)).isEmpty();

        assertThat(formats).filteredOn(line -> line.contains("<tr>")).hasSize(new ParserRegistry().size() + 1);
    }
}
