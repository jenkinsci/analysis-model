package edu.hm.hafner.analysis;

/**
 * Erstellt neue Instanzen der Klasse IssueDifference mithilfe des Builder-Patterns.
 *
 * @author Andreas Kienle, akienle@hm.edu
 * @version Last modified on 08/06/2020
 * @see edu.hm.hafner.analysis.IssueDifference
 */
public class IssueDifferenceBuilder {
    private Report currentIssues;
    private String referenceId;
    private Report referenceIssues;

    /**
     * Erstellt eine neue IssueDifference-Instanz ueber den Konstruktor der konkreten Implementierung und liefert diese
     * zurueck.
     *
     * @return Neu erstellte IssueDifference-Instanz.
     */
    public IssueDifference build() {
        return new IssueDifference(currentIssues, referenceId, referenceIssues);
    }

    /**
     * Ueberschreibt den derzeit gespeicherten Bericht mit dem Uebergebenen.
     *
     * @param currentIssues
     *         Neuer Bericht.
     *
     * @return Builder-Instanz mit veraendertem Variablenwert.
     */
    public IssueDifferenceBuilder setCurrentIssues(final Report currentIssues) {
        this.currentIssues = currentIssues;
        return this;
    }

    /**
     * Setzt die Referenz-ID auf den uebergebenen Wert.
     *
     * @param referenceId
     *         Neue Referenz-ID.
     *
     * @return Builder-Instanz mit veraendertem Variablenwert.
     */
    public IssueDifferenceBuilder setReferenceId(final String referenceId) {
        this.referenceId = referenceId;
        return this;
    }

    /**
     * Ueberschreibt den derzeit gespeicherten Referenz-Bericht mit dem Uebergebenen.
     *
     * @param referenceIssues
     *         Neuer Referenz-Bericht.
     *
     * @return Builder-Instanz mit veraendertem Variablenwert.
     */
    public IssueDifferenceBuilder setReferenceIssues(final Report referenceIssues) {
        this.referenceIssues = referenceIssues;
        return this;
    }
}
