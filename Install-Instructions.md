# Installation von YAKINDU
## Windows 7/10

1. Java 11 herunterladen (https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html)
2. Die Exe ausfuehren und den Installationsanweisungen folgen.
3. Java zum Path hinzufuegen. Dafuer in der Suchleiste "Systemumgebungsvariablen bearbeiten" suchen, dann "Umgebungsvariablen" klicken. Nun Doppelklick auf Path. Falls der Pfad zu Java noch nicht hinzugefuegt ist, "C:\Program Files\Java\jdk-11.0.2\bin" hinzufuegen.
4. Yakindu herunterladen (https://www.itemis.com/en/yakindu/state-machine/download-options/). Professional Edition auswaehlen. Waehlt die 32 bzw. 64 Bit Version je nachdem welches System ihr verwendet. 
5. Heruntergeladene zip entpacken. (Es scheint nicht wichtig zu sein wohin, sucht euch was aus.) Dieser Schritt dauert meist leider lange.
6. Waehrend das zip entpackt wird, kann schon das Git Repository geklont werden. (LINK)
7. Wenn das zip fertig entpackt wurde, im entpackten Ordner die SCT.exe ausfuehren. (Auch das Starten kann leider ein wenig dauern, da Yakindu ein gesamtes Eclipse mit sich bringt...)
8. Yakindu zeigt zuerst einen Welcome Screen an. Diesen schliesen und dann links im Project Explorer einen Rechtsklick machen und Import auswaehlen.
9. "Projects from Folder or Archive" auswaehlen. Dann oben rechts aus Directory klicken und geklonten Ordner auswaehlen. Dabei bis in den Ordner simulator navigieren. Dann Finish klicken.
10. Yakindu sollte nun mit der Probelizenz schon voll funktionsfaehig sein. Wir wollen aber noch eine Lizenz hinzufuegen, damit es nach 30 Tagen weiter verwendet werden kann. Dazu unter "Window" "Preferences" auswaehlen. Dann auf "YAKINDU Licenses" und dort auf "Import License File" klicken. Jetzt die Lizenzdatei auswählen und auf OK und dann Apply klicken.

## MacOS X

1. Java 11 herunterladen (https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html)
2. Die heruntergeladene .dmg oeffnen und den Installationsanweisungen folgen.
3. Yakindu herunterladen (https://www.itemis.com/en/yakindu/state-machine/download-options/). Professional Edition auswaehlen.
4. Heruntergeladene .app in den Programme Ordner verschieben und oeffnen.
5. Yakindu zeigt zuerst einen Welcome Screen an. Diesen schliesen und dann links im Project Explorer einen Rechtsklick machen und Import auswaehlen.
6. "Projects from Folder or Archive" auswaehlen. Dann oben rechts aus Directory klicken und geklonten Ordner auswaehlen. Dabei bis in den Ordner simulator navigieren. Dann Finish klicken.
7. Yakindu sollte nun mit der Probelizenz schon voll funktionsfaehig sein. Wir wollen aber noch eine Lizenz hinzufuegen, damit es nach 30 Tagen weiter verwendet werden kann. Dazu unter "YAKINDU SCT Professional Edition" "Einstellungen" auswaehlen. Dann auf "YAKINDU Licenses" und dort auf "Import License File" klicken. Jetzt die Lizenzdatei auswaehlen und auf OK und dann Apply klicken.

## Ubuntu

### 1. Weg PPA benutzen
Hinweis: Sich vorher sudo Rechte zu geben, laesst die Notwendigkeit fuer sudo vor jedem Befehl natuerlich entfallen.

Hinweis 2: Wer eine neuere Ubuntu-Version als Ubuntu 18.04 benutzt, kann Schritt 2 ueberspringen, aber schaden tut es nicht

1. sudo add-apt-repository ppa:linuxuprising/java
2. sudo apt-get update
3. sudo apt-get install oracle-java11-installer
4. sudo apt-get install oracle-java11-set-default (Falls mehrere Java-Versionen installiert sind)
5. Installation mit javac -version pruefen (Antwort sollte javac 11.x.x sein)
Hinweis 3: Die Darstellung bis hierhin folgt: http://ubuntuhandbook.org/index.php/2018/11/how-to-install-oracle-java-11-in-ubuntu-18-04-18-10/
6. Yakindu herunterladen (https://www.itemis.com/en/yakindu/state-machine/download-options/). Professional Edition auswaehlen.
7. tar.gz entpacken. Der Zielordner scheint nicht wichtig zu sein. (Ich habe ihn in "Documents" entpackt)
8. Yakindu zeigt zuerst einen Welcome Screen an. Diesen schliesen und dann links im Project Explorer einen Rechtsklick machen und Import auswaehlen.
9. "Projects from Folder or Archive" auswaehlen. Dann oben rechts aus Directory klicken und geklonten Ordner auswaehlen. Dabei bis in den Ordner simulator navigieren. Dann Finish klicken.
10. Yakindu sollte nun mit der Probelizenz schon voll funktionsfaehig sein. Da Yakindu kein Hinzufuegen von Lizenzen in einer VM akzeptiert, muss selbststaendig getestet werden, ob das Hinzufuegen der richtigen Lizenz analog zu dem Schritt 10 von Windows bzw. Schritt 7 von MacOS funktioniert.

### 2. Weg Direktdownload

1. Java 11 herunterladen (https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html) . Bevorzugt die .tar.gz
2. Entpacken und einrichten von Java unter Beachtung der README manuell einrichten.
3. Ab hier ist die Prozedur analog zum 1. Weg ab Schritt 5.

## Allgemeine Anmerkungen

Eclipse hat manchmal kleine Bugs mit Java 11. Falls der Error 'Polling news feed' immer mal wieder ein kleines Fenster oeffnet empfehlen wir den folgenden Artikel als work around:
https://stackoverflow.com/questions/52528693/eclipse-internal-error-polling-news-feeds

Solltet ihr die clone-repository Funktion von Eclipse/Yakindu nutzen, so muesst ihr in den Ort als Workspace auswaehlen, in der das Repsoitory geklont wurde und nicht den Standardworkspace. Dies ist ein weiterer bekannter Eclipse/Yakindu-Bug. 

Achtet immer darauf, dass euer Eclipse/Yakindu tatsaechlich Java 10/11 nutzt (einsehbar unter Run Configurations), da ihr ansonsten die Simulation nicht ausfuehren koennt. 

Die Mainmethode liegt in src/de/hpi/mod/sim/app/App.java . 

Yakindu baut auf Eclipse, daher koennt ihr viele Funktionen wie gewohnt aus einem reinen Eclipse benutzen. Wir empfehlen explizit nicht, ein normales Eclipse ueber den Marketplace mit Yakindu nachzuruesten, da dies bei uns zu mehreren massiven Installationsproblemen fuehrte.

Wenn ihr noch nie Eclipse benutzt habt, wundert euch nicht, dass Eclipse/Yakindu bei der ersten Interaktion fast abzustuerzen scheint. Dies ist normal und noch einiger Wartezeit funktioniert Eclipse/Yakindu dann auch ohne Probleme.

Yakindus Lizenzen funktionieren nicht in virtuellen Maschinen!
