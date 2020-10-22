package edu.hm.hafner.analysis.moss;

import java.util.List;

/**
 * Evaluates, if the new method is a duplicate or not.
 *
 * @author Aandreas Westhoff
 */
public class FingerprintMatcher {

    /**
     * list with arraylists of hashes of all old warnings
     */
    private List<WarningFingerprint> mFingerPrintOldWarning;

    /**
     * arraylist with hashes of the warning which may be a new one
     */
    private WarningFingerprint mFingerPrintNewWarning;

    /**
     * Constructor to create the matcher
     *
     * @param pFingerprintOldWarnings
     *         an arraylist with the fingerprints of all old warnings
     * @param pFingerprintNewWarning
     *         the fingerprints of the new warning
     */
    public FingerprintMatcher(final List<WarningFingerprint> pFingerprintOldWarnings,
            final WarningFingerprint pFingerprintNewWarning) {
        mFingerPrintOldWarning = pFingerprintOldWarnings;
        mFingerPrintNewWarning = pFingerprintNewWarning;
    }

    /**
     * method to find out if the warning did already exist in old builds
     */
    public boolean match() {
        //iterates over all old warnings
        boolean isMatch = false;
        for (WarningFingerprint curOldWarning : mFingerPrintOldWarning) {

            if (curOldWarning.getType().equals(mFingerPrintNewWarning.getType()) && curOldWarning.getErrorLine()
                    .equals(mFingerPrintNewWarning.getErrorLine())) {
                //can`t be used because of problems with file renaming: && curOldWarning.getFileName().equals(mFingerPrintNewWarning.getFileName())) {
                long maxSuccessiveMatches = 0;
                int currentSuccessiveMatches = 0;
                float matches = 0;

                //compares all hashes from the old warning with all hashes from the new warning and count the matches
                for (Object aFingerPrintNewWarning : mFingerPrintNewWarning.getHashes()) {
                    int curHash = (Integer) aFingerPrintNewWarning;
                    if (curOldWarning.getHashes().contains(curHash)) {
                        matches++;
                        currentSuccessiveMatches++;
                        //System.out.println(currentSuccessiveMatches);
                    }
                    else {
                        currentSuccessiveMatches = 0;
                    }
                    if (maxSuccessiveMatches < currentSuccessiveMatches) {
                        maxSuccessiveMatches = currentSuccessiveMatches;
                    }
                }
                //simply checks the accordance of the hashes and creates a matchingrate
                float matchingRate = matches / mFingerPrintNewWarning.getHashes().size();
                float successiveMatchesRate = 1;
                if (mFingerPrintNewWarning.getHashes().size() != 0) {
                    successiveMatchesRate = maxSuccessiveMatches / mFingerPrintNewWarning.getHashes().size();
                }
                System.out.println(matchingRate);

                //Kriterien für ein Match
                if (((matchingRate > 0.5 && successiveMatchesRate > 0.3) || matchingRate > 0.7)
                        && mFingerPrintNewWarning.getType().equals(curOldWarning.getType())) {
                    //System.out.println("Scheint dieselbe Warning zu sein");
                    //System.out.println("Max aufeinanderfolgende Matches:" + maxSuccessiveMatches);
                    //System.out.println("Länge Fingerabdruck" + curOldWarning.getHashes().size());
                    isMatch = true;
                    //System.out.print(mFingerPrintNewWarning.getFileName()+mFingerPrintNewWarning.getErrorLine());
                    //System.out.print(curOldWarning.getFileName()+curOldWarning.getErrorLine());
                }
            }
        }
        return isMatch;
    }
}
