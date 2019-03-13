#Installation von YAKINDU
##Windows 7/10

1. Java 11 herunterladen (https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html)
2. Die Exe ausführen und den Installationsanweisungen folgen.
3. Java zum Path hinzufügen. Dafür in der Suchleiste "Systemumgebungsvariablen bearbeiten" suchen, dann "Umgebungsvariablen" klicken. Nun Doppelklick auf Path. Falls der Pfad zu Java noch nicht hinzugefügt ist, "C:\Program Files\Java\jdk-11.0.2\bin" hinzufügen.
4. Yakindu herunterladen (https://www.itemis.com/en/yakindu/state-machine/download-options/). Professional Edition auswählen. Richtige Bitanzahl je nach Betriebssystem herunteladen. 
5. Heruntergeladene zip entpacken. (Es scheint nicht wichtig zu sein wohin, sucht euch was aus.) Dieser Schritt dauert meist leider lange.
6. Während das zip entpackt wird, kann schon das Git Repository geklont werden. (LINK)
7. Wenn das zip fertig entpackt wurde, im entpackten Ordner die SCT.exe ausführen. (Auch das Starten kann leider ein wenig dauern, da Yakindu ein gesamtes Eclipse mit sich bringt...)
8. Yakindu zeigt zuerst einen Welcome Screen an. Diesen schließen und dann links im Project Explorer einen Rechtsklick machen und Import auswählen.
9. "Projects from Folder or Archive" auswählen. Dann oben rechts aus Directory klicken und geklonten Ordner auswählen. Dabei bis in den Ordner simulator navigieren. Dann Finish klicken.
10. Yakindu sollte nun mit der Probelizenz schon voll funktionsfähig sein. Wir wollen aber noch eine Lizenz hinzufügen, damit es nach 30 Tagen weiter verwendet werden kann. Dazu unter Window Preferences auswählen. Dann auf "YAKINDU Licenses" und dort auf "Import License File" klicken. Jetzt die Lizenzdatei auswählen und auf OK und dann Apply klicken.

##MacOS X



##Anmerkungen

Eclipse hat manchmal kleine Bugs mit Java 11. Falls der Error 'Polling news feed' immer mal wieder ein kleines Fenster öffnet empfehlen wir den folgenden Artikel als work around: https://stackoverflow.com/questions/52528693/eclipse-internal-error-polling-news-feeds
