package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Companion-Klasse fuer die Klasse NullSafeList. Stellt Factory-Methoden zur Verfuegung.
 *
 * @author Andreas Kienle, akienle@hm.edu
 * @version Last modified on 06/07/2020
 * @see edu.hm.hafner.analysis.NullSafeList
 */
public class NullSafeCollections {
    /**
     * Privater Default-Konstruktor, um eine Instanziierung dieser Klasse zu verhindern.
     */
    private NullSafeCollections() {
    }

    /**
     * Factory-Methode fuer eine leere NullSafeList-Instanz. Instanziiert mithilfe einer ArrayList-Instanz.
     *
     * @param <T>
     *         Typ der zurueckgegebenen Liste.
     *
     * @return Leere NullSafeList-Instanz.
     */
    public static <T> NullSafeList<T> emptyNullSafeList() {
        return new NullSafeList<>(new ArrayList<>());
    }

    /**
     * Factory-Methode fuer eine neue NullSafeList-Instanz, welche die Elemente der uebergebenen Collection enhaelt.
     * Instanziiert mithilfe einer ArrayList-Instanz.
     *
     * @param <T>
     *         Typ der zurueckgegebenen Liste.
     * @param collection
     *         Collection, dessen Elemente die zurueckgegebene NullSafeList-Instanz enthaelt. Nicht null.
     *
     * @return NullSafeList-Instanz mit den Elementen der uebergebenen Collection.
     * @throws NullPointerException
     *         falls die uebergebene Collection gleich null ist.
     */
    public static <T> NullSafeList<T> nullSafeList(final Collection<T> collection) {
        Objects.requireNonNull(collection);
        return new NullSafeList<>(new ArrayList<>(collection));
    }

    /**
     * Factory-Methode fuer eine neue NullSafeList-Instanz mit einer gegebenen Kapazitaet. Instanziiert mithilfe einer
     * ArrayList-Instanz.
     *
     * @param <T>
     *         Typ der zurueckgegebenen Liste.
     * @param initialCapacity
     *         Initiale Kapazitaet der Liste. Nicht negativ.
     *
     * @return NullSafeList-Instanz mit der ubergebenen Kapazitaet.
     * @throws IllegalArgumentException
     *         falls uebergebene Kapazitaet negativ ist.
     */
    public static <T> NullSafeList<T> nullSafeList(final int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("The initial capacity can not be negative.");
        }

        return new NullSafeList<>(new ArrayList<>(initialCapacity));
    }
}
