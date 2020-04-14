package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Utility Klasse zum Erstellen von NullSafe Instanzen.
 *
 * @param <T>
 *         Typparameter
 *
 * @author budelmann
 */
public abstract class NullSafeCollections <T>{

    /**
     * Gibt eine neue NullSafeList zuruck.
     *
     * @param list
     *         Die Delegate Liste fuer die NullSafeList.
     *
     * @param <T>
     *         Typparameter
     *
     * @return Eine neue NullSafeList
     */
    public static <T> List<T> nullSafeList(final List<T> list) {
        return new NullSafeList<>(list);
    }

    /**
     * Gibt eine neue NullSafeList zurueck mit gegebener minimaler Kapazitaet.
     * Handelt es sich bei dem Delegate Objekt um eine ArrayList, wird die minimale Kapazität gesichert.
     *
     * @param list
     *         Die Delegate Liste fuer die NullSafeList.
     *
     * @param initialCapacity
     *         Die initiale Kapazitaet
     *
     * @param <T>
     *         Typparameter
     *
     * @return Eine neue NullSafeList
     */
    public static <T> List<T> nullSafeList(final List<T> list, final int initialCapacity) {
        if(list instanceof ArrayList<?>) {
            ((ArrayList<?>) list).ensureCapacity(initialCapacity);
        }
        return new NullSafeList<>(list);
    }

    /**
     * Erstellt eine neue NullSafeList, welche die uebergebenen Werte enthaelt.
     *
     * @param list
     *         Die Delegate Liste fuer die NullSafeList.
     *
     * @param collection
     *         Werte fuer die NullSafeList
     *
     * @param <T>
     *         Typparameter
     *
     * @return Eine neue NullSafeList
     */
    public static <T> List<T> nullSafeList(final List<T> list, final Collection<? extends T> collection) {
        NullSafeList<T> nullSafeList = new NullSafeList<>(list);
        list.addAll(collection);
        return nullSafeList;
    }
}
