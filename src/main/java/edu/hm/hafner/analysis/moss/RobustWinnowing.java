package edu.hm.hafner.analysis.moss;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Transforms the methodtext to the Fingerprint of the method.
 *
 * @author Andreas Westhoff
 */
public class RobustWinnowing {

    private int K_GRAM_LENGTH = 5;
    private int WINDOW_SIZE = 5; //garantierter grenzwert minus kgram+1

    /**
     * Constructor for RobustWinnowing
     */
    public RobustWinnowing() {
    }


    /**
     * method who runs the winnowing process
     */
    public ArrayList winnow(final String pMethodToHash) {
        //remove everything from the method except characters
        String textToHash = prepareText(pMethodToHash);
        //build hashes for each k-gram
        ArrayList allHashes = getHashes(textToHash);
        //select the smallest hash for each window and add it to the fingerprint

        return selectHashesForWindows(allHashes);
    }

    /**
     * modifies the warning text by deleting all whitespaces, punctuation etc and transform it to lower case
     *
     * @return the modified string
     */
    private String prepareText(final String pMethodToHash) {
        return pMethodToHash.replaceAll("\\W","").toLowerCase();
    }

    /**
     * creates a list with a hash for every k-gram of the method
     *
     * @return a list with a hash for each k-gram
     */
    private ArrayList<Integer> getHashes(final String pTextToHash) {
        ArrayList<Integer> a = new ArrayList<Integer>();
        for (int i = 0; i <= pTextToHash.length() - K_GRAM_LENGTH; i++) {
            String currentKGram = pTextToHash.substring(i, i + K_GRAM_LENGTH);
            int currentHashCode = currentKGram.hashCode();
            a.add(i, currentHashCode);
        }
        return a;
    }

    /**
     * select the hashes which are going to be used for the method fingerprint
     *
     * @param allHashes hashes of all k-grams of the method
     * @return the hashes for the fingerprint of the method
     */
    private ArrayList<Integer> selectHashesForWindows(final ArrayList allHashes) {
        ArrayList<Integer> fingerprint = new ArrayList<Integer>();

        for(int i = 0; i <= allHashes.size() - K_GRAM_LENGTH; i = i + WINDOW_SIZE) {
            java.util.List sublist;
            sublist = (allHashes.subList(i, i + WINDOW_SIZE)); //muss das nicht die Window size sein?
            int min = (Integer) Collections.min(sublist);
            fingerprint.add(i/WINDOW_SIZE, min);


        }
        return fingerprint;
    }
}
