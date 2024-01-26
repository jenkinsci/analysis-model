package edu.hm.hafner.analysis.registry;

import java.io.IOException;

import org.junit.jupiter.api.Test;

/**
 * Updates the list with the supported formats.
 *
 * @author Ullrich Hafner
 */
@SuppressWarnings({"NewClassNamingConvention", "PMD.ClassNamingConventions"})
class UpdateSupportedFormats {
    @Test
    void run() throws IOException {
        ParserRegistry.main(new String[0]);
    }
}
