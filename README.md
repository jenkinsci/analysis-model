Java Kodierungsrichtlinien
==========================

In jedem Java Projekt sollte der gesamte Quelltext die gleichen Kriterien bei Stil, Formatierung, etc.
verwenden. In diesem Projekt werden die Kodierungsrichtlinien der Vorlesung Softwareentwicklung an der Hochschule
München zusammengefasst. 

Das Projekt enthält neben der Dokumentation der Kodierungsrichtlinien auch gleichzeitig eine sinnvolle Konfiguration
der wichtigsten statischen Java Codeanalysetools, die diese Richtlinien soweit wie möglich prüfen:
- [CheckStyle](http://checkstyle.sourceforge.net/)
- [PMD](http://pmd.sourceforge.net/)
- [FindBugs](http://findbugs.sourceforge.net/)

Die Richtlinien können entweder direkt als Warnungen in der Entwicklungsumgebung angezeigt werden (unterstützt
werden [Eclipse](http://www.eclipse.org/) und [IntelliJ](https://www.jetbrains.com/idea/)) 
oder aber mit dem Build Management Tool Maven überprüft werden. Somit ist sichergestellt,
dass egal wie die Sourcen weiterverarbeitet werden, immer die gleichen Warnungen angezeigt werden. Die Verwendung
von Maven hat zudem den Vorteil, dass die Ergebnisse hinterher leicht in den Continuous Integration Server 
[Jenkins](http://jenkins-ci.org/) eingebunden werden können. 

