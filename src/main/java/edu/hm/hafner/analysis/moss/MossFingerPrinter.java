package edu.hm.hafner.analysis.moss;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class MossFingerPrinter {
    public WarningFingerprint createFingerprint(final String fileName, final int line, final String warningType) {
        String builder = "";
        String errorLine = "";
        try {
            builder = new WarningText().create(fileName, line);
            List<String> lineIterator = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);

            int i = 0;
            for (String s : lineIterator) {
                errorLine = s;
                if (i == line - 1) {
                    break;
                }
                i++;
            }

        }
        catch (IOException ignored) {

        }
        RobustWinnowing r = new RobustWinnowing();
        return new WarningFingerprint(r.winnow(builder), warningType, line, fileName, errorLine);
    }

}
