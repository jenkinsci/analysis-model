# Namensgebung

Bezeichner (*Identifier*) sind in Java beliebig lang und bestehen aus einer Zeichenkette 
von großen und kleinen Buchstaben, Ziffern oder dem Underscore `_`. Hierbei werden Groß und Kleinschreibung 
unterschieden (klein ist nicht Klein). Folgende Einschränkungen sind dabei zu beachten: 
Das erste Zeichen darf keine Ziffer sein und 
[ca. 50 Schlüsselwörter](http://docs.oracle.com/javase/tutorial/java/nutsandbolts/_keywords.html) 
sind vom System reserviert und für eigenen Namen verboten, z.B. class, import, public, etc.

Neben dieser formalen Syntax haben sich folgenden Konventionen eingebürgert. 

## Allgemeine Konventionen
 
Bezeichner in Java verwenden American English und nutzen damit automatisch nur ASCII Zeichen (keine Umlaute). Der
Underscore `_` wird i.A. nicht verwendet. Auch angehängte Zahlen sind untypisch und meistens ein Zeichen für schlechten
Stil (*Code Smell* [6]).
 
Bezeichner sind stets aussagekräftig und bestehen damit oft aus mehreren Teilwörtern. Wir verwenden dann die Schreibweise 
[CamelCase](http://c2.com/cgi/wiki?CamelCase). Bezeichner nutzen i.A. keine Abkürzungen, die Autovervollständigung der 
Entwicklungsumgebungen ergänzt lange Bezeichner komfortabel. 

Zum Thema Abkürzung noch ein schönes Zitat von Ken Thompson auf die Frage was er ändern würde, wenn er UNIX 
nochmals erfinden dürfte: “I‘d spell creat with an e.“

Zum Thema Namensgebung finden sich einige schöne Anti-Beispiele im Essay
["How To Write Unmaintainable Code"](https://www.thc.org/root/phun/unmaintain.html) von Roedy Green.
                   
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

### Methodennamen

Methodennamen enthalten ein Verb im Aktiv, z.B. `computeSum`, `moveForward`, `turnRight`, `compareToIgnoreCase`. Sie beginnen
immer mit einem kleinen Buchstaben. Liefert eine Methode einen `boolean` zurück, dann beginnt der Name i.A. mit einem
`is`, z.B. `isEmpty`, `isTreeFront`, `isNotRunning`, etc. Macht das grammatikalisch keinen Sinn, kann statt dessen auch
`can`, `has`, `should` oder ähnliches verwendet werden. Hauptsache ist, dass sich boolesche Methoden wie eine Frage lesen: d.h. `equals`,
`exists`, `contains` etc. sind auch in Ordnung.

## Variablennamen

Variablennamen beginnen mit einem kleinen Buchstaben. Variablen vom Typ `boolean` nutzen meist den Präfix `is`, siehe 
Abschnitt zu booleschen Methodennamen. Alle anderen Variablennamen sind im Allgemeinen ein Substantiv, da ein Objekt
gespeichert wird. Werden in einer Variablen mehrere Objekte gespeichert (Array, Listen, etc.), dann wird die Mehrzahl
verwendet. Beispiele: counter, isLeaf, numberOfTrees, months, etc.

