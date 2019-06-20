# Drive System Simulator

Dieses Projekt ist ein Simulator für autonom fahrende Logistikroboter, deren Steuerung mithilfe des YAKINDU Statechart Tools implementiert werden kann.
Das Implementieren einer solchen Steuerung ist die Aufgabe im **Entwurfsprojekt II** in der Veranstaltung **Modellierung II** am Hasso-Plattner-Institut im Sommersemester 2019.

**Hinweis:** Damit der den Statecharts generierten Programmcode kompiliert und ausgeführt werden kann ist hier der komplette Quelltext der Simulationsumgebung gegeben. Dieser soll nicht verändert werden.

## Struktur

Das Projekt ist in die folgenden Ordner unterteilt:

- **`model`** beinhaltet das Modell der Robotersteuerung.
    + Das Modell befindet sich in der `drivesystem.sct`-Datei, die mit YAKINDU bearbeitet werden kann.
    + Die zugehörige `.sgen`-Datei enthält eine bereits vorbereitete Konfiguration für die Code-Generierung aus dem Statechart.
- **`src`** enthält den Quelltext der Simulationsumgebung.
    + Die Klasse `de.hpi.mod.sim.App` muss ausgeführt werden, um die Simulation zu starten.
- **`src-gen`** enthält den aus den Statecharts generierten Quelltext. Dieser wird automatisch überschrieben.
- **`res`** enthält Ressourcen für die GUI, wie z.B. Bilder.
- **`test`** enthält Tests für das Projekt.



## Setup

**[=> Hier geht es zur Installations-Anleitung für alle Betriebssysteme! <=](INSTALL.md)**

YAKUNDU Statechart Tools ist ein Eclipse-Plugin. Da es Gelegentlich zu Problemen mit einem Nachinstallierten Plugin kommt, empfehlen wir Eclipse und YAKINDU als separates Standalone-Bundle zu installieren ([siehe Anleitung](INSTALL.md)).

Es wird Mindestens Java Version 10 benötigt.

Für Mitarbeiter und Studierende des Hasso-Plattner-Institut steht eine Lizenz für die YAKINDU Statechart Tools Professional Edition bereit. Näheres dazu ebenfalls in der [Installations-Anleitung](INSTALL.md).



## Benutzung

#### Modellierung

- In der Datei `model/drivesystem.sct` kann in Eclipse/YAKINDU die Roboter-Steuerung modelliert werden.
- Die Schnittellen des Statecharts sind bereits vorgegeben ("Definition section" links). Diese dürfen **nicht verändert** werden, es können interne Variablen ("internal") ergänzt werden.
- Damit in der Simulation der aktuelle Zustand angezeigt werden kann sollte die äußerste Region "Drive System" heißen und die Namen aller Unterregionen im Diagramm mir einem "_" beginnen.
- Auf den Webseiten von YAKINDU findet sich [eine Anleitung zum Modellieren](https://www.itemis.com/en/yakindu/state-machine/documentation/user-guide/edit_editing_statecharts) und eine [Statechart-Referenz](https://www.itemis.com/en/yakindu/state-machine/documentation/user-guide/sclang_statechart_language_reference).

#### Codegenerierung

- Die beigelegte Datei `model/drivesystem.sgen` Datei enthält eine Konfiguration für die Codegenerierung. 
- Wenn in Eclipse/YAKINDU nach einem Rechtsklick auf die Datei "Generate Code Artifacts" gewählt wird, wird der zum Statechart passende Quelltext automatisch neu generiert.

#### Simulation

- Zum Starten der Simulation wird die Klasse `de.hpi.mod.sim.App` im Ordner `src` rechts geklickt und "Run as" > "Java Application" ausgewählt.
- Als Shortcut steht danach der "Run" Button oben im Eclipse-Menü zur Verfügung.
- Ist der Simulator gestartet können entweder "Szenarien" (= dynamisch generierte Abläufe mit einem oder mehreren Robotern) oder "Tests" (= vordefinierte Situationen) ausgewählt werden. Innerhalb dieser Szenarien und Tests wird jeweils für alle Roboter der generierte Quelltext des zuvor modellierten Statecharts ausgeführt.
- Sobald ein Szenario oder Test gestartet wird läuft ein Timer und die Geschwindigkeit kann verändert werden.
- Mit den Pfeiltasten kann navigiert werden, mit `Strg +` und `Strg -` gezoomt. `R` setzt die Kamera zurück. `Space` pausiert den aktuelle laufenden Test / das aktuelle Szenario.
- Mit der linken und rechten Maustaste kann jeweils ein Roboter ausgewählt werden, die dann farblich hervorgehoben sind und deren Eigenschaften rechts explizit ablesbar sind.