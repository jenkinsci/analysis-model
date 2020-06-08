package edu.hm.hafner.analysis;

/**
 * Erstellt neue Instanzen der Klasse LineRange mithilfe des Builder-Patterns.
 *
 * @author Andreas Kienle, akienle@hm.edu
 * @version Last modified on 08/06/2020
 */
public class LineRangeBuilder {
    private int start;
    private int end;

    /**
     * Erstellt eine neue LineRange-Instanz ueber den Konstruktor der konkreten Implementierung und liefert diese
     * zurueck.
     *
     * @return Neu erstellte LineRange-Instanz.
     */
    public LineRange build() {
        return new LineRange(start, end);
    }

    /**
     * Setzt den Zeilenstart auf den uebergebenen Wert.
     *
     * @param start
     *         Neuer Zeilenstart.
     *
     * @return Builder-Instanz mit veraendertem Variablenwert.
     */
    public LineRangeBuilder setStart(final int start) {
        this.start = start;
        return this;
    }

    /**
     * Setzt das Zeilenende auf den uebergebenen Wert.
     *
     * @param end
     *         Neues Zeilenende.
     *
     * @return Builder-Instanz mit veraendertem Variablenwert.
     */
    public LineRangeBuilder setEnd(final int end) {
        this.end = end;
        return this;
    }
}
