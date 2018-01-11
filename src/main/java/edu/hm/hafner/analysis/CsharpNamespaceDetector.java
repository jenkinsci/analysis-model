package edu.hm.hafner.analysis;

import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

/**
 * Detects the namespace of a C# workspace file.
 *
 * @author Ullrich Hafner
 */
// CHECKSTYLE:CONSTANTS-OFF
public class CsharpNamespaceDetector extends AbstractPackageDetector {
    private static final Pattern NAMESPACE_PATTERN = Pattern.compile("^namespace .*$");

    @Override
    public boolean accepts(final String fileName) {
        return fileName.endsWith(".cs");
    }

    @Override
    public String detectPackageName(final Stream<String> lines) {
        Optional<String> namespace = lines.filter(NAMESPACE_PATTERN.asPredicate()).findFirst();
        if (namespace.isPresent()) {
            String line = namespace.get();
            if (line.contains("{")) {
                return StringUtils.substringBetween(line, " ", "{").trim();
            }
            else {
                return StringUtils.substringAfter(line, " ").trim();
            }
        }
        return UNKNOWN_PACKAGE;
    }
}

