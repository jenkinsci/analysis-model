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

Die Idee für diesen Styleguide lieferte das Buch "The Elements of Java Style" von Vermeulen, Ambler, Bumgardner, Metz, 
Misfeldt, Shur und Thompson [1]. Es ist bereits im Jahr 2000 in der Cambridge University Press erschienen. Anregungen für 
zusätzliche Inhalte kommen auch aus den folgenden Büchern, die allesamt Standardwerke in der Softwareentwicklung sind:
- [2] "The Pragmatic Programmer. From Journeyman to Master", Andrew Hunt, David Thomas, Ward Cunningham, Addison Wesley, 1999
- [3] "Code Complete: A Practical Handbook of Software Construction: A Practical Handbook of Software Costruction", 
    Steve McConnell, Microsoft Press, 2004
- [4] "Clean Code: A Handbook of Agile Software Craftsmanship", Robert C. Martin, Prentice Hall, 2008

Der Styleguide entsteht erst in diesem Semester und ist daher noch nicht vollumfänglich. Aktuell besteht er aus den 
folgenden Abschnitten:

[Formatierung](../master/doc/Formatierung.md)
