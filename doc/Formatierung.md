# Formatierung

Gute und sinnvolle Formatierung des Quelltextes ist eine wichtige Aufgabe, denn
Quelltext wird einmal geschrieben und zigmal gelesen. Das Layout sollte immer
der logischen Struktur des Codes folgen: Layout ist damit auch eine Kommunikationsform.

In den folgenden Abschnitten sind die wichtigsten Richtlinien zur Formatierung
von Java Code beschrieben. Eine detaillierte Beschreibung unterbleibt explizit,
da alle modernen Entwicklungsumgebungen vordefinierte Formatierungseinstellungen verwenden
können. Diese können bei Bedarf mit einem einfachen Kommando angewendet werden. D.h. um
die korrekte Formatierung eines Quelltextstücks zu sehen, ist lediglich das entsprechende
Kommando der Entwicklungsumgebung aufzurufen.

**Achtung:** Greenfoot selbst ist nicht so mächtig und korrigiert nur die Einrückung,
nicht aber die Verwendung von Leerzeichen.

## Einrücken

Die öffnende Klammer eines Blocks steht immer auf der gleichen Zeile wie die Anweisung davor. Die folgenden Anweisungen
eines geschachtelten Blocks werden alle mit 4 Leerzeichen eingerückt. Tabs dürfen nicht verwendet werden, da
diese nicht überall mit der gleichen Leerzeichenanzahl dargestellt werden (z.B. im Browser). 
Die schließende Klammer steht dann genau unterhalb der Anweisung, die die öffnende Klammer enthält.
 
An Beispielen wird das leichter deutlich, das zum Einrücken verwendete Leerzeichen wird zur besseren Lesbarkeit
durch das Sonderzeichen `⋅` hervorgehoben:

```java
if (expression1) {
⋅⋅⋅⋅statement1;
⋅⋅⋅⋅statement2;
⋅⋅⋅⋅etc.
}

while (expression2) {
⋅⋅⋅⋅statement3;
⋅⋅⋅⋅statement4;
⋅⋅⋅⋅etc.
⋅⋅⋅⋅if (expression3) {
⋅⋅⋅⋅⋅⋅⋅⋅statement5;
⋅⋅⋅⋅⋅⋅⋅⋅statement6;
⋅⋅⋅⋅⋅⋅⋅⋅etc.
⋅⋅⋅⋅}
}
```

**Achtung:** gemäß [3] steht bei einem `if-else` und `try-catch` Konstrukt die schließende Klammer immer 
alleine auf einer Zeile:

```java
if (expression1) {
⋅⋅⋅⋅statement1;
⋅⋅⋅⋅etc.
}
else if (expression2) {
⋅⋅⋅⋅statement2;
⋅⋅⋅⋅etc.
}
else {
⋅⋅⋅⋅statement3;
⋅⋅⋅⋅etc.
}

try {
⋅⋅⋅⋅statement4;
⋅⋅⋅⋅etc.
}
catch (Exception exception1) {
⋅⋅⋅⋅statement5;
⋅⋅⋅⋅etc.
}
finally {
⋅⋅⋅⋅statement6;
⋅⋅⋅⋅etc.
}
```

## Leerzeichen

Quelltext ohne Leerzeichen lässt sich deutlich schlechter lesen und verstehen. Daher nutzen wir **genau** 
ein Leerzeichen an den folgenden Stellen:
- Zwischen einer Anweisung und der folgenden öffnenden runden ( oder geschweiften { Klammer 
- Zwischen einer schließenden runden ) und einer öffnenden geschweiften { Klammer 
- Zwischen binärem Operator und seinen beiden Operanden
- Nach jedem Komma in der Parameterliste einer Methode

Für die folgenden Konstrukte wird kein Leerzeichen verwendet:
- Zwischen unärem Operator und seinem Operand
- Zwischen Methodenname und öffnender runden ( Klammer 
- Bedingung innerhalb der runden Klammern () im `if` oder `while`   

Hier einige Beispiele, in denen das Leerzeichen durch das Sonderzeichen `⋅` hervorgehoben wurde:

```java
if⋅(treeRight())⋅{
    ...
}
else⋅if⋅(treeLeft())⋅{
    ...
}
else⋅{
    ...
}

while⋅(!onLeaf()⋅&&⋅(treeFront()⋅||⋅mushroomFront()))⋅{
    ...
}
```

## Zeilenumbruch

Heutzutage ist es selten nötig, eine Quelltextzeile umzubrechen, da meistens die Monitore breit genug sind. Wird
eine Zeile doch einmal zu lang, dann muss sie umgebrochen werden (typischerweise nach 120 Zeichen Breite): so wird
horizontales Scrolling im Editor vermieden.

Bei einem Umbruch ist darauf zu achten, dass i.A. vor einem Operator umgebrochen wird. Die umgebrochene Zeile wird dann
mit 8 Zeichen zusätzlich zur vorhergehenden Zeile eingerückt.

```java
if (column >= half - limit + 1
        && column < half + limit) {
    putLeaf();
}
```

Das gleiche Schema wird verwendet beim Umbruch von Methodenparametern:

```java
    /**
     * Draws a horizontal line of the specified {@code length}. Start of the line is at ({@code x}, {@code y}).
     *
     * @param world  Karas world
     * @param x      x coordinate of start
     * @param y      y coordinate of start
     * @param length length of the line
     */
    public void drawHorizontalLine(final boolean[][] world, final int x, final int y,
            final int length) {
        drawHorizontalLine(world, x, y,
                length, true);
    }
```

## Leerzeilen

Auch Leerzeilen können die Struktur von Programmen verbessern. Zusammenhängende Anweisungen sollten gruppiert werden
und durch Leerzeilen von unzusammenhängenden Anweisungen getrennt werden. Dadurch lässt sich eine Methode in
mehrere zusammenhängende Blöcke gruppieren. Die einzelnen Blöcke einer Methode sind dann durch **genau** eine Leerzeile
voneinander getrennt.

```java
public void foo() {
    block1-anweisung1;
    block1-anweisung2;
    block1-anweisung3;

    block2-anweisung1;
    block2-anweisung2;

    block3-anweisung1;
    block3-anweisung2;
    block3-anweisung3;
}
```

Die erste Anweisung beginnt dabei direkt nach dem Methodenkopf, die letzte hört direkt vor der schließenden Klammer auf,
hier werden keinen extra Leerzeilen mehr eingefügt.

Innerhalb einer Klasse hat es sich eingebürgert, zwei Methoden oder Konstruktoren durch eine Leerzeile zu trennen.
Instanzvariablen können wie Anweisungen gruppiert werden, wenn dies thematisch sinnvoll ist. Zwischen Instanzvariablen
und Methoden bzw. Konstruktoren befindet sich wieder eine Leerzeile. I.a. werden alle Instanzvariablen direkt nach dem
Klassenkopf aufgeführt, dann alle Konstruktoren, dann alle Methoden. Am Schluss stehen dann alle inneren Klassen.

## Komplettes Beispiel

Am besten lassen sich die Regeln an einem realem Beispiel erkennen. Das folgenden Programm erzeugt einen Diamanten
in Karas Welt und zeigt dabei auch die typische Java Formatierung.

```java
/**
 * Solution for assignment 8.
 *
 * @author Ullrich Hafner
 */
public class Assignment8 extends Kara {
    /**
     * Draws a diamond into a square world of size 2n + 1 x 2n +1.
     */
    public void act() {
        int width = computeWidth();
        int half = width / 2;

        for (int line = 0; line < width; line++) {
            int limit = computeLimit(width, line);

            for (int column = 0; column < width; column++) {
                if (column >= half - limit + 1
                        && column < half + limit) {
                    putLeaf();
                }
                move();
            }

            moveDown();
        }
    }

    private int computeLimit(final int width, final int line) {
        if (line > width / 2) {
            return width - line;
        }
        else {
            return line + 1;
        }
    }

    private int computeWidth() {
        int width = 0;

        putLeaf();
        do {
            move();
            width++;
        } while (!onLeaf());
        removeLeaf();

        return width;
    }

    private void moveDown() {
        turnRight();
        move();
        turnLeft();
    }
}
```
