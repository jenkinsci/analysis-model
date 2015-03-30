Java Kodierungsrichtlinien
==========================

In jedem Java Projekt sollte der gesamte Quelltext die gleichen Kriterien bei Stil, Formatierung, etc.
verwenden. In diesem Projekt werden die Kodierungsrichtlinien der Vorlesung Softwareentwicklung an der Hochschule
München zusammengefasst. 

Das Projekt enthält neben der Dokumentation der Kodierungsrichtlinien auch gleichzeitig eine sinnvolle Konfiguration
der wichtigsten statischen Java Codeanalysetools, die diese Richtlinien soweit wie möglich prüfen:
- [Checkstyle](http://checkstyle.sourceforge.net/)
- [PMD](http://pmd.sourceforge.net/)
- [FindBugs](http://findbugs.sourceforge.net/)

Die Richtlinien können entweder direkt als Warnungen in der Entwicklungsumgebung angezeigt werden (unterstützt
werden [Eclipse](http://www.eclipse.org/) und [IntelliJ](https://www.jetbrains.com/idea/)) 
oder aber mit dem Build Management Tool [Maven](http://maven.apache.org/) überprüft werden. Somit ist sichergestellt,
dass egal wie die Sourcen weiterverarbeitet werden, immer die gleichen Warnungen angezeigt werden. Die Verwendung
von Maven hat zudem den Vorteil, dass die Ergebnisse hinterher leicht in den Continuous Integration Server 
[Jenkins](http://jenkins-ci.org/) eingebunden werden können. 

Der Styleguide entsteht erst in diesem Semester und ist daher noch nicht vollumfänglich. Aktuell besteht er aus den 
folgenden Abschnitten:

- [Formatierung](../master/doc/Formatierung.md)
- [Namensgebung](../master/doc/Namensgebung.md)
- [Kommentare](../master/doc/Kommentare.md)
- [Testen](../master/doc/Testen.md)
- [Best Practice](../master/doc/Best-Practice.md)

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