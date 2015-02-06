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

```
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

## Komplettes Beispiel

Am besten lassen sich die Regeln an einem realem Beispiel erkennen. Das folgenden Programm erzeugt einen Diamanten
in Karas Welt und zeigt dabei auch die typische Java Formatierung.

```
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