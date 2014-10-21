# Formatierung

In diesem Dokument wird die Formatierung von Java Code beschrieben. Das Dokument kann recht kurz gehalten werden,
da alle modernen Entwicklungsumgebungen vordefinierte Formatierungseinstellungen verwenden können und diese mit
einem einfachen Kommando angewendet werden können.

## Einrücken

Die öffnende Klammer eines Blocks steht immer auf der gleichen Zeile wie die Anweisung davor. Die folgenden Anweisungen
eines geschachtelten Blocks werden alle mit 4 Leerzeichen eingerückt. Die schließende Klammer steht dann genau unterhalb
der Anweisung, die die öffnende Klammer enthält.
 
Am Beispiel wird das leichter deutlich:
```java
if (expression) {
    statement1;
    statement2;
    ...
}
```