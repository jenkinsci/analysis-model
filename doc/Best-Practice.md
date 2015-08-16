# Best Practice

Neben eher formellen Konventionen gibt es eine Vielzahl von Tipps und Tricks zur Verbesserung des eigenen Programmierstils.
Diese sind in diesem Dokument unsortiert aufgeführt.

## Verwendung von redundanten Klammern

Runde Klammern steigern die Lesbarkeit, wenn in einer boolesche Bedingungen verschiedene Operatoren verwendet werden:  
```java
if (onLeaf() || (treeLeft() && treeRight())) {
    ...
}
```
Klammern helfen die Intention zu verdeutlichen, auch wenn diese - wie in diesem Beispiel - nicht nötig wären, 
da über die Priorität der Operatoren das selbe Resultat erzielt
würde. Aber nicht jeder hat die 
[Operatorreihenfolgetabelle](http://docs.oracle.com/javase/tutorial/java/nutsandbolts/operators.html) im Kopf. 

Für unäre Operatoren wie die Negation `!` oder einfache binäre Bedingungen mit 2 Operanden sollten
allerdings möglichst keine Klammern genutzt werden.

```java
boolean canGoAhead = !mushroomFront() && !treeFront();
```

## Vereinfachung komplexer Bedingungen und Anweisungsfolgen

Neben der oben beschriebenen Vereinfachung von booleschen Bedingungen über die Verwendung von zusätzlichen Klammern bietet
sich häufiger an, diese Bedingung durch Auslagern in kleine Methoden lesbar zu machen. Es gilt: kleine Methoden kosten
nichts extra, sondern erhöhen die Lesbarkeit des Codes. Dies gilt natürlich nicht nur für boolesche Bedingungen sondern
für alle Methoden. Zusammenhängende Anweisungen sollten daher zusammengefasst und in Methoden ausgelagert werden.

Hier ein Beispiel, in dem Bedingung und Anweisung im `if` jeweils in eine Methode ausgelagert wurde. Dadurch wird
der eigentliche Algorithmus leichter lesbar. Interessieren Details, kann man immer noch in die entsprechende Methode
navigieren.

```java
    public void act() {
        ...
        if (isObstacleInFront()) {
            walkAroundObstacle();
        }
        ...
    }

    private boolean isObstacleInFront() {
        return treeFront() || mushroomFront()
    }

    private void walkAroundObstacle() {
        turnRight();
        move();
        turnLeft();
        move();
        move();
        turnLeft();
        move();
        turnRight();
    }

```

## Gestaltung von Methoden

Die wichtigste Regel beim Erstellen von Methoden lautet: in der Kürze liegt die Würze! D.h., Methoden sollten immer
möglichst kurz sein. Es ist schwierig ein absolutes Maß dafür zu definieren, aber generell sollte eine Methode immer
auf eine Bildschirmseite passen. D.h. Scrolling ist weder horizontal noch vertikal erforderlich. Meistens sind Methoden
daher zwischen 1 und 10 Zeilen lang. Hin und wieder kann sich auch mal eine Methode mit 20 Zeilen einschleichen...

Hier ein schönes Beispiel:
```java
boolean isEven(final long value) {
    return value % 2 == 0;
}
```

## Immutable Classes

Klassen sollten wenn möglich immer als unveränderlich (*immutable*) gestaltet werden. D.h. alle Eigenschaften einer
Instanz werden bei der Erzeugung festgelegt und sind dann konstant während der Lebensdauer dieses Objekts. Dies
hilft Fehler zu minimieren und Programme leichter zu parallelisieren.

In [5] beschreibt Joshua Bloch fünf Schritte, um zu einer unveränderlichen Klasse zu kommen. Auch im
[Java Tutorial](http://docs.oracle.com/javase/tutorial/essential/concurrency/imstrat.html) wird dies vorgeführt.
Hier die wichtigsten Regeln auf einen Blick:

1. Alle Instanzvariablen müssen als `private` und `final` deklariert sein.
2. Keine Methode darf den Zustand (d.h. die Eigenschaften) einer Instanz verändern.
3. Keine Methode (auch kein Konstruktor) darf veränderbare Parameter in Instanzvariablen ablegen.
4. Keine Methode darf veränderbare Instanzvariablen als Rückgabewerte haben.

Die letzten beiden Punkte klingen auf den ersten Blick recht unscheinbar, haben es aber in sich: Arrays, Collections
sowie viele Instanzen bestehender JDK Klassen (z.B. `Date`) sind mutable und müssen daher immer kopiert werden!

Hier ein Beispiel, das diese Vorgabe korrekt umsetzt: eine `Queue` kann mit einer vorgegebenen Liste an Elementen
erzeugt werden. Da das übergebene Array veränderbar ist (der **Inhalt**, nicht die Länge), **muss** der Parameter
kopiert werden. Sonst könnte der Aufrufer später dieses Array ändern und damit die Werte in der `Queue` überschreiben.

```java
@Immutable
public class Queue {
    private final int[] elements;

    public Queue(final int[] initialElements) {
        elements = Arrays.copyOf(initialElements, initialElements.length);
    }
    ...
}
```

## Variablendefinition und -initialisierung

In Java werden Variablen möglichst erst dann definiert, wenn Sie gebraucht werden. 
Dies erhöht die Übersicht und minimiert den Gültigkeitsbereich. Dadurch ist es i.A. auch immer möglich, eine Variable 
sofort zu initialisieren. (Siehe auch Item 45 in [5].)

Dies wird in anderen Programmiersprachen wie C und C++ anders gehandhabt, dort werden diese als Block am Anfang 
einer Methode definiert. 

## Nutzung von final

Das Schlüsselwort `final` wird in Java an zwei Stellen verwendet. 
Es können damit Variablen bzw. Methoden und Klassen als unveränderlich markiert werden.

### final für unveränderliche Variablenreferenzen bzw. -werte

Wird eine Variable mit `final` ausgezeichnet (Objektvariable, lokale Variable oder Parameter), dann ist der Wert der Variable
nach der ersten Zuweisung nicht mehr änderbar. Gerade Java Neulinge interpretieren dies oft nur bei primitiven Datentypen
richtig: hier ist tatsächlich der Wert nicht mehr änderbar. Bei Variablen, die ein Objekt referenzieren, ist allerdings
nur gesichert, dass die bestehende Objektreferenz nicht mehr geändert wird. D.h. der Zustand des referenzierten Objektes kann 
sich trotzdem ändern. Nur bei immutable Klassen ist auch das Objekt nicht mehr veränderbar, dies muss aber wie bei 
[Immutable Classes](#immutable-classes) beschrieben, selbst sicher gestellt werden. Java selbst bietet dazu kein Sprachmittel
an, um neben der Objektreferenz auch den Inhalt als unveränderlich zu markieren.

Folgende Richtlinien haben sich in Java als sinnvoll herausgestellt:
- Objektvariablen (d.h. Fields) **sollten immer** mit `final` ausgezeichnet werden, wenn dies möglich ist. 
- Parameter **müssen immer** mit `final` ausgezeichnet werden, nur so ist beim Lesen des Quelltextes (Code Review, Debugging)
 sofort klar, welchen Wert die Variablen z.B. am Ende einer Methode haben.
- Lokale Variable **werden nie** mit `final` ausgezeichnet. Andernfalls geht der Blick auf das Wesentliche verloren. Im 
 Englischen spricht man hier häufig von *clutter* oder *noise*, die die Verwendung von `final` an jeder möglichen Stelle
 erzeugt. Die [Scala](http://www.scala-lang.org/) Erfinder haben dies besser gemacht: 
 hier gibt die Sprache gleich zwei verschiedene Schlüsselwörter
 für die zwei Varianten vor (`var` und `val`). 
 
### final für Methoden und Klassen
 
Wird eine Methode mit `final` ausgezeichnet, so ist ein Überschreiben dieser Methode in einer Subklasse nicht möglich. 
Ist die gesamte Klasse mit `final` ausgezeichnet, so kann von dieser Klasse gar nicht abgeleitet werden. 

Während in [5] empfohlen wird, Klassen oder Methoden möglichst immer mit `final` zu kennzeichnen, wenn man sich nicht 
wirklich Gedanken über die Nutzung in Subklassen gemacht hat, sehen viele andere Java Architekten dies nicht so 
puristisch. Daher lautet die pragmatische Empfehlung:
- Klassen sollten in den seltensten Fällen als `final` gekennzeichnet werden. Durch TDD lassen sich durch Vererbung
 verursachte Fehler recht schnell finden. Ein "Versiegeln" von Klassen ist nicht wirklich erforderlich und hemmt die
 Wiederverwendung. 
- Methoden sollten nur dann als `final` gekennzeichnet, wenn durch das Überschreiben tatsächlich ein Fehler entstehen wird. 
 Z.B. dürfen in Konstruktoren **niemals** Methoden aufgerufen werden, die nicht `final` sind!
 
## Nutzung von anonymen Klassen

In Java hat es sich gerade in Lehrbüchern eingebürgert, anonyme Klassen für Callbacks zu verwenden: man spart  
sich einige Zeilen Quelltext und das Buch wird wohl dadurch einige Cents billiger. 

Anonyme Klassen machen den Quelltext leider schwer lesbar und unübersichtlich. Daher gilt grundsätzlich, dass diese nur in
wenigen Ausnahmefällen verwendet werden sollten. Wenn trotzdem eine anonyme Klasse benötigt wird, dann sollte diese genau
eine Methode implementieren und die Implementierung selbst sollte wenn möglich genau eine Anweisung enthalten. Mit den 
[Lambda-Ausdrücken](http://www.oracle.com/webfolder/technetwork/tutorials/obe/java/Lambda-QuickStart/index.html) aus Java 8
lassen sich solche Anforderungen deutlich eleganter umsetzen.

## Nutzung von Methodenreferenzen (d.h. Delegates)

Häufig muss sich eine Klasse als Listener für Events registrieren. Beispielsweise registrieren sich UI Actions immer am 
Modell, um den eigenen Zustand zu aktualisieren. Java bietet seit Java 8 endlich die Möglichkeit, an dieser Stelle
Methoden-Referenzen zu verwenden (siehe auch Delegates in C#). Damit ist es nicht mehr erforderlich, dass die registrierende 
Klasse das erforderliche Interface selbst implementiert: es reicht wenn eine Referenz auf eine private Methode übergeben wird,
die die Schnittstelle umsetzt.

Bisher wurde gerade in vielen Java Lehrbüchern das folgende Anti-Pattern benutzt, das konzeptionell falsch ist: 

```java
public class BrokenObserverImplementation implements Observer {
    public void attach(final Observable observable) {
        observable.addObserver(this);
    }

    @Override
    public void update(final Observable o, final Object arg) {
        ...    
    }
}
```

In dieser unsauberen Variante wird die Methode `update` Teil des API, da sie wegen des Interfaces `public` sein muss. 
Die Methode kann daher später nie wieder entfernt werden. Weitere Nachteile dieses Anti-Patterns:
- Die Methode kann von Nutzern der Klasse zu jedem Zeitpunkt aufgerufen werden, was unerwartete Seiteneffekte nach sich ziehen kann.
- Muss auf mehrere Events reagiert werden, funktioniert das Pattern nur mit Einsatz von `if-else` Kaskaden, was wiederum die
Lesbarkeit reduziert.

Mit Java 8 lässt sich das gleiche erreichen, indem die Klasse folgendermaßen gestaltet wird:

```java
public class CorrectObserverImplementation {
    public void attach(final Observable observable) {
        observable.addObserver(this::delegate);
    }

    private void delegate(final Observable o, final Object arg) {
        ...
    }
}
```
D.h. die Callback Methode kann nun als private markiert werden und ist nach außen nicht mehr sichtbar. 
Je nach Anwendungsfall kann statt der Methoden-Referenz auch ein Lambda Ausdruck verwendet werden.