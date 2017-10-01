This 'analysis-model' project is a library to read static analysis reports into a Java object model. 
Currently it is used only by [Jenkins' warnings plug-in](https://wiki.jenkins.io/display/JENKINS/Warnings+Plugin). 
Since this library has no dependencies to the Jenkins project it might be used by other static analysis visualization 
tools as well in the future.

All source code is licensed under the MIT license.


Die automatisch prüfbaren Richtlinien können teilweise direkt als Warnungen in der Entwicklungsumgebung 
[IntelliJ](https://www.jetbrains.com/idea/) angezeigt werden (Checkstyle und IntelliJ Inspections). Somit ist sichergestellt,
dass immer die gleichen Warnungen angezeigt werden - egal wie und wo die Java Dateien weiterverarbeitet werden. 
Für FindBugs und PMD ist der Umweg über das Build Management Tool [Maven](http://maven.apache.org/) erforderlich 
(die entsprechenden IntelliJ Plugins sind leider noch nicht ausgereift genug). 
Die Verwendung von Maven hat zudem den Vorteil, dass die Ergebnisse hinterher leicht in den Continuous Integration Server 
[Jenkins](https://jenkins.io/) eingebunden werden können. 

Die Richtlinien sind in den Vorlesungen 2014/2015 entstanden und werden laufend ergänzt.
Aktuell bestehen diese aus den folgenden Abschnitten:

- [Formatierung](../master/doc/Formatierung.md)
- [Namensgebung](../master/doc/Namensgebung.md)
- [Kommentare](../master/doc/Kommentare.md)
- [Testen](../master/doc/Testen.md)
- [Testen von Schnittstellen und Basisklassen](../master/doc/Abstract-Test-Pattern.md)
- [Fehlerbehandlung](../master/doc/Fehlerbehandlung.md)
- [Best Practice](../master/doc/Best-Practice.md)

Geplant sind u.a. noch folgende Themen, die im Rahmen der Vorlesung Testen mit objektorientierten Sprachen behandelt werden:
- Testen mit Stubs und Mocks
- Verwenden von Annotations zum Markieren von Schnittstellen-Verträgen

Zur besseren Verdeutlichung der angesprochenen Themen sind diesem Projekt auch [Java Beispiele](../master/src/) angefügt, 
die sich möglichst genau an diese Richtlinien halten.

Ideen und Inhalte für diesen Styleguide lieferten verschiedene Bücher, insbesondere aber das Buch 
"The Elements of Java Style" [1]. Diese Bücher sind allesamt wegweisend für die Softwareentwicklung und sind 
damit Pflichtlektüre für Berufstätige in der Softwareentwicklung:
- [1] "The Elements of Java Style", Vermeulen, Ambler, Bumgardner, Metz, Misfeldt, Shur und Thompson, Cambridge University Press, 2000
- [2] "The Pragmatic Programmer. From Journeyman to Master", Andrew Hunt, David Thomas, Ward Cunningham, Addison Wesley, 1999
- [3] "Code Complete: A Practical Handbook of Software Construction", Steve McConnell, Microsoft Press, 2004
- [4] "Clean Code: A Handbook of Agile Software Craftsmanship", Robert C. Martin, Prentice Hall, 2008
- [5] "Effective Java", Second Edition, Joshua Bloch, Addison Wesley, 2008
- [6] "Refactoring: Improving the Design of Existing Code", Martin Fowler, Addison Wesley, 1999 

Die gesamten Dokumente dieser Kodierungsrichtlinien unterliegen der
[Creative Commons Attribution 4.0 International Lizenz](http://creativecommons.org/licenses/by/4.0/). Der 
Quelltext aller Beispiele und Klassen unterliegt der [MIT Lizenz](http://opensource.org/licenses/MIT).