# Best Practice

Neben eher formellen Konventionen gibt es eine Vielzahl von Tipps und Tricks zur Verbesserung der eigenen Programmierstils.
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
allerdings keine Klammern genutzt werden.

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
sowie viele Instanzen bestehender JDK Klassen (z.B. `Date`) sind immutable und dürfen daher nur als Kopien gespeichert
werden.

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
