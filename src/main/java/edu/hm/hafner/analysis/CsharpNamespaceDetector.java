package edu.hm.hafner.analysis;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;

/**
 * Detects the namespace of a C# workspace file.
 *
 * @author Ulli Hafner
 */
// CHECKSTYLE:CONSTANTS-OFF
public class CsharpNamespaceDetector extends AbstractPackageDetector {
    private static final Pattern NAMESPACE_PATTERN = Pattern.compile("^namespace .*$");

    @Override
    public boolean accepts(final String fileName) {
        return fileName.endsWith(".cs");
    }

    @Override
    public String detectPackageName(final InputStream stream) {
        try {
            LineIterator iterator = IOUtils.lineIterator(stream, "UTF-8");
            while (iterator.hasNext()) {
                String line = iterator.nextLine();
                if (NAMESPACE_PATTERN.matcher(line).matches()) {
                    if (line.contains("{")) {
                        return StringUtils.substringBetween(line, " ", "{").trim();
                    }
                    else {
                        return StringUtils.substringAfter(line, " ").trim();
                    }
                }
            }
        }
        catch (IOException ignored) {
            // ignore
        }
        finally {
            IOUtils.closeQuietly(stream);
        }
        return UNKNOWN_PACKAGE;
    }
}

