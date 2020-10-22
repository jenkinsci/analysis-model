package edu.hm.hafner.analysis.moss;

import java.util.ArrayList;

/**
 * Objet with all neccessary information to identify an old warning.
 */
public class WarningFingerprint {

    /**
     * an arraylist which contains the fingerprint.
     */
    private ArrayList<Integer> hashes;

    /**
     * the annotation type.
     */
    private String type;

    /**
     * the linenumber where the annotation occurs.
     */
    private int line;

    /**
     * a string with the code of the line, where the error occurs.
     */
    private String errorLine;

    /**
     * the name of the file where the error occurs.
     */
    private String fileName;

    /**
     *constructor for the WarningFingerprint
     *
     * @param hashes an arraylist which contains the fingerprint
     * @param type the annotation type
     * @param line the linenumber where the warning occurs
     * @param fileName the name of the file in which the warning occurs
     * @param errorLine a string with the code of the line, where the error occurs
     */
    public WarningFingerprint(final ArrayList<Integer> hashes, final String type, final int line, final String fileName, final String errorLine) {
        this.hashes = hashes;
        this.type = type;
        this.line = line;
        this.fileName = fileName;
        this.errorLine = errorLine;
    }

    /**
     * returns the fingerprint
     *
     * @return the fingerprint hashes
     */
    public  ArrayList<Integer> getHashes() {
        return hashes;
    }

    /**
     * returns the annotation type.
     *
     * @return the type of the annotation
     */
    public String getType() {
        return type;
    }

    /**
     * returns the line number of the annotation.
     *
     * @return the line number
     */
    public int getLine() {
        return line;
    }

    /**
     * returns the filename.
     *
     * @return filename
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * returns the String of the line where the annotation occurs.
     *
     * @return the line, where the error occurs
     */
    public String getErrorLine() {
        return errorLine;
    }
}
