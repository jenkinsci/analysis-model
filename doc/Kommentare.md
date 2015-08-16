# Kommentare

Java kennt drei verschiedene Varianten von Kommentaren:
 - der einzeilige Kommentar wird mit `//` eingeleitet und geht bis zum Zeilenende
 - der mehrzeilige Kommentar wird mit `/*` gestartet und mit `*/` beendet
 - der JavaDoc Kommentar wird mit `/**` gestartet und mit `*/` beendet. 
      
## Kommentare zum Quelltext

Wohlüberlegte Kommentare *können* die Qualität des Quelltextes steigern. Sie sind i.A. ein notwendiges Übel und 
helfen uns darüber hinweg, dass wir nicht alles durch Code alleine ausdrücken können. Dafür kann eine der beiden
ersten Varianten genutzt werden. Diese erklären das Ziel des Quelltextes und verdeutlichen damit unsere Absicht.

Wichtig zu beachten sind aber die folgenden Aussagen von Brian W. Kernighan:
 - Make sure comments and code agree.
 - Don't just echo the code with comments - make every comment count.
 - Don't comment bad code - rewrite it.

(Aus dem Klassiker: B. W. Kernighan and P. J. Plauger, The Elements of Programming Style, McGraw-Hill, New York, 1974)

Insgesamt gilt: so wenig Kommentare wie nötig verwenden. Besser Bezeichner passend auswählen und komplexen Code vereinfachen oder
z.B. durch zusätzliche Methoden strukturieren. D.h. bevor ein Kommentar für einen komplexen
Programmausschnitt erstellt wird, sollte dieser Ausschnitt in eine Methode ausgelagert werden. Die Methode selbst wird dann
mit dem beabsichtigten Kommentar benannt. Eine ausführlichere Behandlung dieses Themas findet sich in [4], dort
sind  viele Beispiele und Negativbeispiele aufgeführt.


## Spezielle Kommentare

Häufig findet man auch Kommentare zur Kennzeichnung des Copyrights. Auch wenn diese den obigen Regeln widersprechen,
müssen diese aus juristischen Gründen in vielen Dateien vorhanden sein.
 
Werden in einem Programmabschnitt kleine Verbesserungsmöglichkeiten entdeckt, die im Moment nicht behoben werden können,
so können diese ebenso mit einem Kommentar beschrieben werden. Hier ist wichtig, die Kommentare mit einer Markierung
wie TODO oder FIXME zu versehen. In den meisten Projekten gilt: mit FIXME werden Stellen markiert, die noch vor
der Veröffentlichung eines Programms zu beheben sind. Lediglich mit TODO markierte Stellen können länger im Programm
verbleiben. Wichtig bleibt: größere Änderungswünsche sollten immer in einem Issue Tracker verwaltet werden, damit diese
auch mit in die Planung einfließen können.

Auf der anderen Seite sind Kommentare zur Versionshistorie einer Datei nicht sinnvoll, diese werden sowieso in der
Versionsverwaltung abgelegt und sind somit redundant. 

## JavaDoc

JavaDoc wird genutzt um die öffentliche Schnittstelle eines Programms zu dokumentieren. Diese Kommentare sind unerlässlich 
und müssen für alle Klassen und Methoden verfasst werden, die mindestens die Sichtbarkeit `protected` haben. Wird eine Klasse
serialisiert (z.B. wenn sie die Schnittstelle `Serializable` implementiert), dann müssen auch
private Attribute kommentiert werden, da in diesem Fall auch diese zur öffentlichen Schnittstelle einer Klasse gehören. 

Die [Java Bibliotheken](http://docs.oracle.com/javase/8/docs/api/) selbst bieten schöne Beispiele, wie solche Kommentare 
auszusehen haben und wie nützlich diese sind. Eine kleine Einführung zu diesem Thema ist auf den
[Oracle Seiten](http://www.oracle.com/technetwork/java/javase/documentation/javadoc-137458.html) zu finden.

JavaDoc Kommentare werden im aktiv geschrieben. Der erste Satz (der mit einem Punkt abgeschlossen wird) muss eine
Zusammenfassung sein. Dieser wird im generierten HTML Dokument als Überschrift dargestellt. Das zu beschreibende Element 
wird dabei nicht noch einmal wiederholt, der Kommentar wird dadurch möglichst knapp.
D.h. **"An ordered collection"** statt **"This interface defines an ordered collection"**, oder 
**"Returns whether this list contains no elements"** statt **"This method returns whether this list contains no elements"**.
Die folgenden Sätze können dann das Element genauer beschreiben, hilfreich ist hierbei oft die Angabe eines 
Anwendungsbeispiels. Werden dabei Codestücke bzw. Variablen in die Beschreibung eingebettet, so müssen diese die Syntax 
`{@code ...}` nutzen. Werden Klassen oder Methoden referenziert, so werden diese mit der Syntax `{@link ...}` bzw. 
`{@linkplain ...}` eingefügt, nur so kann die IDE die Kommentare mit entsprechenden Hyperlinks anzeigen (*linkplain* verwendet
einen Zeichensatz mit variabler, *link* mit fester Breite). 
Ganze Quelltext-Abschnitte, die über mehrere Zeilen gehen, werden mit der Syntax `<pre>{@code ...}</pre>` eingebettet.

Beispiel:

```java
/**
 * Checks if a {@link CharSequence} is empty ("") or {@code null}.
 *
 * <pre>{@code
 * StringUtils.isEmpty(null)      = true
 * StringUtils.isEmpty("")        = true
 * StringUtils.isEmpty(" ")       = false
 * StringUtils.isEmpty("bob")     = false
 * StringUtils.isEmpty("  bob  ") = false
 * }</pre>
 *
 * @param cs  the CharSequence to check, may be {@code null}
 * @return {@code true} if the CharSequence is empty or null
 * @since 3.0 Changed signature from isEmpty(String) to isEmpty(CharSequence)
 */
public static boolean isEmpty(final CharSequence cs) {
    return cs == null || cs.length() == 0;
}

```
