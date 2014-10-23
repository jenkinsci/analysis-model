# Namensgebung

Bezeichner (*Identifier*) sind in Java beliebig lang und bestehen aus einer Zeichenkette 
von großen und kleinen Buchstaben, Ziffern oder dem Underscore `_`. Hierbei werden Groß und Kleinschreibung 
unterschieden (klein ist nicht Klein). Folgende Einschränkungen sind dabei zu beachten: 
Das erste Zeichen darf keine Ziffer sein und 
[ca. 50 Schlüsselwörter](http://docs.oracle.com/javase/tutorial/java/nutsandbolts/_keywords.html) 
sind vom System reserviert und für eigenen Namen verboten, z.B. class, import, public, etc.

Neben dieser formalen Syntax haben sich folgenden Konventionen eingebürgert.
 
## Methodennamen

Wir verwenden American English für Methodennamen und nutzen damit automatisch nur ASCII Zeichen (keine Umlaute). 
Methodennamen bestehen i.A. aus mehreren Teilwörtern, die die Methode aussagekräftig beschreiben. 
Eines dieser Wörter ist ein Verb im Aktiv, z.B. computeSum, moveForward, turnRight, compareToIgnoreCase. 
Methodennamen beginnen mit einem kleinen Buchstaben - jedes weitere Teilwort wird dann wieder mit einem Großbuchstaben 
begonnen. Diese Notation wird auch [CamelCase](http://c2.com/cgi/wiki?CamelCase) genannt.
