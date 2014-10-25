# Formatierung

In diesem Dokument wird die Formatierung von Java Code beschrieben. Das Dokument kann recht kurz gehalten werden,
da alle modernen Entwicklungsumgebungen vordefinierte Formatierungseinstellungen verwenden können und diese mit
einem einfachen Kommando angewendet werden können.

**Achtung:** Greenfoot korrigiert nur die Einrückung, nicht aber die Verwendung von Leerzeichen. 

Gute und sinnvolle Formatierung des Quelltextes ist eine wichtige Aufgabe, denn 
Quelltext wird einmal geschrieben und zigmal gelesen. Das Layout sollte immer 
der logischen Struktur des Codes folgen: Layout ist damit auch eine Kommunikationsform. 

## Einrücken

Die öffnende Klammer eines Blocks steht immer auf der gleichen Zeile wie die Anweisung davor. Die folgenden Anweisungen
eines geschachtelten Blocks werden alle mit 4 Leerzeichen eingerückt. Tabs dürfen nicht verwendet werden, da
diese nicht überall mit der gleichen Leerzeichenanzahl dargestellt werden (z.B. im Browser). 
Die schließende Klammer steht dann genau unterhalb der Anweisung, die die öffnende Klammer enthält.
 
An Beispielen wird das leichter deutlich:

```java
if (expression1) {
    statement1;
    statement2;
    ...
}

while (expression2) {
    statement3;
    if (expression3) {
        statement3;
        statement4;
        ...
    }
}
```

**Achtung:** gemäß [3] steht bei einem `if-else` und `try-catch` Konstrukt die schließende Klammer immer 
alleine auf einer Zeile:

```java
if (expression1) {
    statement1;
    ...
}
else if (expression2) {
    statement2;
    ...
}
else {
    statement3;
    ...
}

try {
    statement1;
    ...
}
catch (Exception exception1) {
    statement2;
    ...
}
finally {
    statement3;
    ...
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

