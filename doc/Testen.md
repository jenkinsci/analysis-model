# Testen

Wenn möglich, nutzen wir das Prinzip des Test Driven Development. D.h. Tests werden vorab bzw. zeitgleich mit
den zu erstellenden Klassen geschrieben. Details zu diesem Ansatz finden sich in [4] in Kapitel 9. Dies hat viele
Vorteile:
- Es werden nur die Anforderungen umgesetzt, die auch wirklich nötig sind (YAGNI: You ain‘t gonna need it, 
KISS: Keep it simple, stupid!).
- Das Softwaredesign orientiert sich an der Nutzung von Klassen, da die Tests die ersten "Anwender" des API sind.
- Testfälle dokumentieren eine Klasse (und ergänzen somit die Spezifikation).
- Testbarkeit eines Programms ist per Definition garantiert und zudem erhalten wir automatisch eine hohe Testabdeckung.

## Konventionen beim Schreiben von Modultests

Wir nutzen für Modultests (d.h. Unittests) die [JUnit](http://junit.org/) Bibliothek. Alle Modultests einer Klasse `Foo` 
legen wir in der zugehörigen Klasse `FooTest` ab. Die Tests werden im Verzeichnis *src/test/java* abgelegt, damit
sie separat von den eigentlichen Klassen liegen (diese liegen unter  *src/main/java*). 
 
Ein Modultest besteht immer aus drei Schritten, die ggf. zusammenfallen können:

1. **Given**: Das zu testende Objekt wird erzeugt (Subject Under Test: SUT). Sind dazu weitere Objekte nötig, 
so werden diese in diesem Schritt ebenso erzeugt. 
2. **When**: Die zu überprüfende Funktionalität wird aufgerufen. Sind dazu weitere Objekte nötig (z.B. als Methodenparameter),
sollten diese bereits in Schritt 1.) erzeugt werden.
3. **Then**: Es wird überprüft, ob die im letzten Schritt aufgerufene Funktionalität korrekt ist. Dazu kann z.B. der
Rückgabewert einer Methode oder der innere Zustand einer Klasse herangezogen werden. Zum Prüfen verwenden wir Assertions
des JUnit Frameworks [AssertJ](http://joel-costigliola.github.io/assertj/assertj-core-features-highlight.html)

Damit im Fehlerfall schnell die Ursache gefunden wird, benennen wir eine Testmethode mit einem sinnvollen (und langen) Namen
und ergänzen im JavaDoc in einem knappen Satz das Ziel des Tests. Testmethoden sollten i.A. immer mit dem Präfix *should* beginnen.

An einem Beispiel lassen sich diese Konventionen am besten erkennen:
```java
/**
 * Tests the class {@link MathUtils}.
 *
 * @author Ullrich Hafner
 */
public class MathUtilsTest {
    /** Verifies that {@link MathUtils#max} works with positive and negative values. */
    @Test
    public void testMaxWithPositiveAndNegativeValues() {
        // Given
        MathUtils utils = new MathUtils();
        int[] inputValues = {1, -2, 0};
        // When
        int actual = utils.max(inputValues);

        // Then
        assertThat(actual).isEqualTo(1);
    }
}
```

## Eigenschaften von Modultests
Gut geschriebene Modultests lassen sich nach dem [FIRST](https://pragprog.com/magazines/2012-01/unit-tests-are-first)
Prinzip charakterisieren, das sich folgendermaßen zusammenfassen lässt:
- Sie sind unabhängig und können in beliebiger Reihenfolgen laufen.
- Sie sind konsistent und liefern immer das gleiche Resultat.
- Sie können schnell und automatisiert ausgeführt werden.
- Sie sind verständlich und wartungsfreundlich.

## Tipps und Tricks
Hier noch einige Anregungen bei der Gestaltung von Modultests:
- Modultests greifen nie auf Datenbank, Dateisystem oder Web Services zu.
- Eine Testmethode sollte nur einen Aspekt testen: d.h. wir testen ein bestimmtes Verhalten und nur indirekt eine Methode.
- Eine Testmethoden sollten ca. 5-15 Zeilen umfassen.
- Test Klassen sollten keine Objektvariablen besitzen. 
Benötigte Objekte sollten immer neu mit passenden *create* Methoden erzeugt werden. Häufig verwendete Eingangsparameter
sollten als Konstanten definiert werden.
- Tests nutzen die selben Kodierungsrichtlinien wie normale Klassen.

## State-based vs. Interaction-based Testing

Prinzipiell gibt es 2 Varianten des Testings. Beim **State Based Testing** wird das Testobjekt nach Aufruf der zu 
testenden Methoden durch Abfrage seines internen Zustands verifiziert. Analog dazu kann natürlich auch der Zustand 
der im Test verwendeten Parameter bzw. Rückgabewerte analysiert werden. Im Gegensatz dazu wird beim 
**Interaction Based Testing** nicht der Zustand des SUT analysiert. Statt dessen werden die Aufrufe aller am Test 
beteiligten Objekte mit einem Mocking Framework überprüft.

