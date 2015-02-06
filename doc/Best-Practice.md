# Best Practice

Neben eher formellen Konventionen gibt es eine Vielzahl von Tipps und Tricks zur Verbesserung der eigenen Programmierstils.
Diese sind in diesem Dokument unsortiert aufgeführt.

## Klammern

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

Für unäre Operatoren wie die Negation `!` oder einfache binäre Bedingungen mit 2 Operanden werden keine Klammern genutzt.

```
boolean canGoAhead = !mushroomFront() && !treeFront();
```

## Variablendefinition und -initialisierung

In Java werden Variablen möglichst erst dann definiert, wenn Sie gebraucht werden. 
Dies erhöht die Übersicht und minimiert den Gültigkeitsbereich. Dadurch ist es i.A. auch immer möglich, eine Variable 
sofort zu initialisieren. (Siehe auch Item 45 in [5].)

Dies wird in anderen Programmiersprachen wie C und C++ anders gehandhabt, dort werden diese als Block am Anfang 
einer Methode definiert. 
