# Formatierung

In diesem Dokument wird die Formatierung von Java Code beschrieben. Das Dokument kann recht kurz gehalten werden,
da alle modernen Entwicklungsumgebungen vordefinierte Formatierungseinstellungen verwenden können und diese mit
einem einfachen Kommando angewendet werden können.

## Einrücken

Die öffnende Klammer eines Blocks steht immer auf der gleichen Zeile wie die Anweisung davor. Die folgenden Anweisungen
eines geschachtelten Blocks werden alle mit 4 Leerzeichen eingerückt. Die schließende Klammer steht dann genau unterhalb
der Anweisung, die die öffnende Klammer enthält.
 
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

*Achtung:* gemäß [3] wird bei einem *if-else* und *try-catch* Konstrukt die schließende Klammer immer alleine auf einer 
 Zeile:
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
