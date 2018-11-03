# Drive System Simulator

### Struktur
- `model` beinhaltet das Modell des Drive System als `.sct` Datei.
Sie kann mit Yakindu modifiziert werden. 
Die `.sgen` Datei ist zuständig für die Code-Generierung und wird beim Speichern in Yakindu automatisch ausgeführt.
- `res` enthält Ressourcen für die GUI, wie z.B. Bilder.
- `src-gen` enthält die generierte Statemachine
- `test` enthält Tests für das Projekt. Tests für die Statemachine werden später in einen anderen Ordner untergebracht.
- `src` Source für das Projekt
  - `.env` Umgebungs-Klassen 
    - `Simulator` Controller für die Simulation
    - `ServerGridManagement` Implementiert das Spielfeld
  - `.env.model` Interfaces, Enums und Klassen die zur Kommunikations zwischen Komponenten genutzt werden.
  - `.env.robot` Klassen die den Robot angehören
    - `Robot` Controller für den Robot
    - `DriveSystemWrapper` Wrapper um die Statemachine
    - `DriveManager` nimmt Fahrbefehle an und berechnet Position
  - `.env.view`
    - `DriveSimFrame` Oberste View
    - `SimulatorView` Zeigt die Simulation mithilfe von `RobotRenderer` und `GridRenderer` an
    - `RobotRenderer` zeichnet die Robots und Updated den DriveManager
    - `GridRenderer` zeichnet das Grid
    - `RobotInfoView` zeigt Informationen über den momentan ausgewählten Roboter an

### GUI
Die GUI zeigt die Simulation und Infos zu einen ausgewählten Roboter an.
Der Roboter kann durch Mausklick ausgewählt werden.
Standardmäßig ist der als letzte hinzugefügte Roboter ausgewählt.
Mit `A` können Roboter hinzugefügt werden.
Mit den Pfeiltasten kann die Kamera über das Spielfeld bewegt werden.