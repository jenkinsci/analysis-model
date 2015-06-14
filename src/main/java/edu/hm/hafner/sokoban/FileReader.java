package edu.hm.hafner.sokoban;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;

/**
 * Reads a file from disk and returns the contents as an array of strings.
 *
 * @author Ullrich Hafner
 */
public class FileReader {
    /**
     * Opens the specified file and reads all lines into an array of strings.
     *
     * @param fileName the file to read
     * @return the content of the file, stored in an array of lines
     * @throws IllegalArgumentException if the file could not be read
     */
    public String[] readLines(final String fileName) {
        try (InputStream level = FileReader.class.getResourceAsStream("/" + fileName)) {
            List<String> lines = IOUtils.readLines(level);
            return lines.toArray(new String[lines.size()]);
        }
        catch (IOException exception) {
            throw new IllegalArgumentException("Can't read level file " + fileName, exception);
        }
    }
}
