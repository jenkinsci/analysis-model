# Kommentare

Java kennt drei verschiedene Varianten von Kommentaren:
 - der einzeilige Kommentar wird mit `//` eingeleitet und geht bis zum Zeilenende
 - der mehrzeilige Kommentar wird mit `/*` gestartet und mit '*/' beendet
 - der JavaDoc Kommentar wird mit `/**` gestartet und mit '*/' beendet. 
      
## Kommentare zum Quelltext

Wohlüberlegte Kommentare *können* die Qualität des Quelltextes steigern. Sie sind i.A. ein notwendiges Übel und 
helfen uns darüber hinweg, dass wir nicht alles durch Code alleine ausdrücken können. Dafür kann eine der beiden
ersten Varianten genutzt werden. Diese erklären des Ziel des Quelltextes und verdeutlichen damit unsere Absicht.

Wichtig zu beachten sind aber die folgenden Aussagen von Brian W. Kernighan:
 - Make sure comments and code agree.
 - Don't just echo the code with comments -- make every comment count.
 - Don't comment bad code -- rewrite it.

(Aus dem Klassiker: B. W. Kernighan and P. J. Plauger, The Elements of Programming Style, McGraw-Hill, New York, 1974)

Insgesamt gilt: so wenig Kommentare wie nötig verwenden. Besser Bezeichner passend auswählen und komplexen Code 
vereinfachen. In [4] findet sich ein ganzes Kapitel, das sich diesem Thema widmet.

## Spezielle Kommentare

Häufig findet man auch Kommentare zur Kennzeichnung des Copyrights. Auch wenn diese den obigen Regeln widersprechen,
müssen diese aus juristischen Gründen in vielen Dateien vorhanden sein.
 
Werden in einem Programmabschnitt kleine Verbesserungsmöglichkeiten entdeckt, die im Moment nicht behoben werden können,
so können diese ebenso mit einem Kommentar beschrieben werden. Hier ist wichtig, die Kommentare mit einer Markierung
wie TODO oder FIXME zu versehen. In den meisten Projekten gilt: mit FIXME werden Stellen markiert, die noch vor
der Veröffentlichung eines Programms zu beheben sind. Lediglich mit TODO markierte Stellen können länger im Programm
verbleiben. Wichtig bleibt: größere Änderungswünsche sollten immer in einem Issue Tracker verwaltet werden, damit diese
auch mit in die Planung einfließen können.

Auf der anderen Seite sind Kommentare zur Versionshistorie einer Datei nicht sinnvoll, diese werden sowieso in der
Versionsverwaltung abgelegt und sind somit redundant. 

## JavaDoc

JavaDoc wird genutzt um die öffentliche Schnittstelle eines Programms zu dokumentieren. Diese sind unerlässlich und müssen
für alle Klassen und Methoden verfasst werden, die mindestens die Sichtbarkeit `protected` haben. Die 
[Java Bibliotheken](http://docs.oracle.com/javase/8/docs/api/) selbst bieten schöne Beispiele, wie solche Kommentare 
auszusehen haben und wie nützlich diese sind.  

