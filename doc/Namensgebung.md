# Namensgebung

Bezeichner (*Identifier*) sind in Java beliebig lang und bestehen aus einer Zeichenkette 
von großen und kleinen Buchstaben, Ziffern oder dem Underscore `_`. Hierbei werden Groß und Kleinschreibung 
unterschieden (klein ist nicht Klein). Folgende Einschränkungen sind dabei zu beachten: 
Das erste Zeichen darf keine Ziffer sein und 
[ca. 50 Schlüsselwörter](http://docs.oracle.com/javase/tutorial/java/nutsandbolts/_keywords.html) 
sind vom System reserviert und für eigenen Namen verboten, z.B. class, import, public, etc.

Neben dieser formalen Syntax haben sich folgende Konventionen eingebürgert. 

## Allgemeine Konventionen
 
Bezeichner in Java verwenden American English und nutzen damit automatisch nur ASCII Zeichen (keine Umlaute). Der
Underscore `_` wird i.A. nicht verwendet. Auch angehängte Zahlen sind untypisch und meistens ein Zeichen für schlechten
Stil (*Code Smell* [6]).
 
Bezeichner sind stets aussagekräftig und bestehen damit oft aus mehreren Teilwörtern. Wir verwenden dann die Schreibweise 
[CamelCase](http://c2.com/cgi/wiki?CamelCase). Bezeichner nutzen i.A. keine Abkürzungen, die Autovervollständigung der 
Entwicklungsumgebungen ergänzt lange Bezeichner komfortabel. Wenn ein Bezeichner doch einmal eine Abkürzung enthält,
dann wird auch hier nur der erste Buchstabe groß geschrieben, z.B. `loadXmlDocument`, `writeAsJson`.

Zum Thema Abkürzung noch ein schönes Zitat von Ken Thompson auf die Frage was er ändern würde, wenn er UNIX 
nochmals erfinden dürfte: “I‘d spell creat with an e.“

Zum Thema Namensgebung finden sich einige schöne Anti-Beispiele im Essay
["How To Write Unmaintainable Code"](https://www.thc.org/root/phun/unmaintain.html) von Roedy Green.
                  
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

## Klassennamen

Klassennamen sind ein Substantiv und beginnen mit einem großen Buchstaben. Vor oder nach dem Substantiv können
ggf. weitere beschreibende Wörter verwendet werden, z.B. `Counter`, `LimitedCounter`, `OpenCounter`, `HashMap`, 
`ConcurrentHashMap`. Abstrakte Klassen halten sich i.A. auch an dieses Schema - manchmal macht es aber auch Sinn
diese durch den Präfix `Abstract` als solche zu markieren, z.B. `AbstractList` oder `AbstractDocument`. Testklassen
haben immer den Suffix `Test` nach dem eigentlichen Namen der Klasse, die getestet werden soll, z.B. `CounterTest`
oder `HashMapTest`.

## Interfacenamen

Interfacenamen sind entweder ein Substantiv (siehe Abschnitt Klassennamen) oder ein Adverb
und beginnen mit einem großen Buchstaben. Vor oder nach dem Substantiv bzw. Adverb können
ggf. weitere beschreibende Wörter verwendet werden, z.B. `Counter`, `Observable`, `WeakListener`, 
`Set`, `SortedSet`. Manche Projekte (z.B. Eclipse) verwenden das Anti-Pattern der
[Ungarischen Notation](http://msdn.microsoft.com/de-de/library/aa260976(VS.60).aspx) 
und stellen jedem Interface den Präfix `I` voraus. Das ist nur in seltenen Fällen sinnvoll und sollte 
vermieden werden.
