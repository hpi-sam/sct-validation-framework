# Drive System Simulator

### Struktur
- `model` beinhaltet das Modell des Drive System als `.sct` Datei.
  Sie kann mit Yakindu modifiziert werden. 
  Die `.sgen` Datei ist zuständig für die Code-Generierung und wird beim Speichern in Yakindu automatisch ausgeführt.
- `res` enthält Ressourcen für die GUI, wie z.B. Bilder.
- `src-gen` enthält die generierte Statemachine. Diese darf nicht per Hand modifiziert werden.
- `test` enthält Tests für das Projekt.
- `src` Source für das Projekt
  - `de.hpi.mod.sim` Von Yakindu generierte Interfaces
  - `.env` Umgebungs-Klassen 
    - `Simulator` Controller für die Simulation
    - `ServerGridManagement` Implementiert das Spielfeld
    - `StationManager` Kontrolliert die Stationen und die Kommunikation mit Robotern in der Station. Mehr Infos in `stations.md`
      - `Station` Speichert Daten über eine Station, wie ID, DriveLock, Batterien und Queue
      - `Battery` Speichert Daten über eine Batterie in einer Station
    - `PackageManager` Kontrolliert Abladepunkte und zu sendene Packete
  - `.env.model` Interfaces, Enums und Klassen die zur Kommunikations zwischen Komponenten genutzt werden. Diese sind größtenteils gleich oder ähnlich der ursprünglichen Spezifikation, haben aber auch alle eine eigene Dokumentation
  - `.env.robot` Klassen die den Robot angehören
    - `Robot` Controller für den Robot
    - `DriveSystemWrapper` Wrapper um die Statemachine
    - `DriveManager` nimmt Fahrbefehle an und berechnet Position
  - `.env.view`
    - `DriveSimFrame` Oberste View
    - `SimulatorView` Zeigt die Simulation mithilfe von `RobotRenderer` und `GridRenderer` an
    - `RobotRenderer` zeichnet die Robots und Updated den DriveManager
    - `GridRenderer` zeichnet das Grid

### GUI
Die GUI zeigt die Simulation und Infos zu einen ausgewählten Roboter an.
Der Roboter kann durch Mausklick ausgewählt werden.
Standardmäßig ist der als letzte hinzugefügte Roboter ausgewählt.
Mit `A` können Roboter hinzugefügt werden.
Mit den Pfeiltasten kann die Kamera über das Spielfeld bewegt werden.

#Setup

Either download eclipse from YAKINDU website directly or download latest version from https://www.eclipse.org/downloads/. After succesfully installed go to Help->InstallNewSoftware
and provide the link to the download repository of YAKINDU state chart tools (Install everything and accept every license). Then checkout the Git repository from source with the option in the Eclipse start screen (third option on the left side). After specifying repository and credentials (SSH only) choose "Use new project wizard window".

IMPORTANT NOTICE: You CAN'T use the default workspace location you have to switch to the place, where Eclipse cloned your repository (Usually C://[Local user]/git/[project name]). This is a known bug in Eclipse and maybe will be fixed at some point. But if you get an error, when using the default workspace, do the step specified above.

Name the local project and press finish. The project exactly needs JRE version 10 (validated with version 10.0.2). Please note, that neither JRE 8 or older nor the newer JDKs (11.0.2) will work! We have not tested if it may work with JRE 9.

After finished these steps you should be able to execute the main in de.hpi.mod.sim.env.view.DriveSimFrame

NOTE: It is strongly suggested to use the latest Eclipse (current version 2018-12) IDE. If you use other IDEs like IntelliJ the resources may be included in a different hierachy and will cause some errors therefore. Especially InteeliJ is known for using some Macros who interfere with the YAKINDU state chart engine, so we suggest again to not use this IDE.