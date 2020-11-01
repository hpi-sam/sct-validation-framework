# Statechart Simulator

This project is a simulating environment which allows to run simulations in which it is possible to define some behavior by YAKINDU Statechart tools.

## Project Structure

The project is devided into the following directories:
- **`model`** contains the statechart models
    + The models are contained in the `.sct` - files. They can be edited by YAKINDU.
    + The belonging `.sgen`-file contains a predefined configuration for generating executable code from the statechart.
- **`src-gen`** contains the code generated from the statecharts. It is automatically updated after choosing "Generate Code Artifacts" on the `.sgen`-file.
- **`res`** contains resources for the GUI, as for example images
- **`test`** contains tests for the project
- **`src`** contains the source code of the simulation environment.

## Simulator Code Structure (in directory `src`)
- To start the simulator, execute the class `de.hpi.mod.sim.App`
- The simulator code is devided in two packages, `de.hpi.mod.sim.core` and `de.hpi.mod.sim.worlds`.
  + The `core` package covers everything that is needed for *every* simulation, e.g. the setting up the frame and a framework for executing scenarios and test cases. When adding new applications of the simulator, the core package should not have to be touched. See details in **Core**.
  + In the `world` package, code is placed to specify the environment and entities for ones application. That is, what the environment, in which the statechart is simulated, looks like. See details in **Worlds**.

### Core
The core contains 2 classes and 4 packages.
- `core.World` is the abstract class which is the base of every specific environment. It allows to hold entities and gives a list of abstract methods to be implemented in extending classes. See `Worlds`.
- `core.Configuration` holds execution parameters (*magic numbers*) used for the simulation. If you need to adapt these or add more parameters, best do it by creating a subclass of this class in your world package, as done in `worlds.abstract_grid.GridConfiguration` or `worlds.infinitewarehouse.InfiniteWarehouseConfiguration`.
- The package `core.scenario` holds classes to manage the execution of *scenarios* that is certain set ups of the environment. By default (`core.scenario.Scenario`), scenarios offer an open-end simulation. However, test cases (`core.scenario.TestScenario`) allow to simulate small situations and fail or pass depending on individually defined conditions, e.g. to verify the correct behavior of the statecharts. `core.scenario.ScenarioManager` manages the list of defined scenarios and test cases and their execution.
- The package `core.simulation` handles running the simulation (starting and refreshing) and provides the interfaces `Entity` (active objects in the simulation) and `IHighlightable` (objects that can be clicked to view information). The concept of detectors (`core.simulation.Detector`) can be used to react to certain conditions in the simulation, e.g. fail the simulation if an entity goes to a place where it should not be able to go (e.g., a wall) and thereby testing the statechart implenentation.
- The package `statechart` provides utilities to include the statechart behavior into the environment. 
- The package `view` holds all used UI elements.

### Worlds
A `world` is the definition of the environment for a specific application. To set up an environment, one thus has to overwrite the abstract class `de.hpi.mod.sim.core.World`.
+ One can use inheritance to build a structure of abstract and more and more specific environments
+ Currently there are 6 worlds
  - `abstract_grid.GridWorld` introduces an abstract environment with a grid-based 2D surface and provides useful utilities like the concept of positions and orientations in that grid
  - `abstract_robots.RobotWorld` is a still abstract extension of the `GridWorld` and introduces robots to be moved around on the grid
  - `infinite_warehouse` is a ready-to-use implementation of a `RobotWorld` where robots transport packages from stations to unloading stations, have to avoid collisions and be recharged. The exact behavior of the robots drivesystem is specified by a YAKINDU statechart.
  - ` pong` is leand on the traditionally Pong- Game. There is a one player and two player- mode. You have to catch the ball with your paddle. The exact behavior of the paddles are specified by a YAKINDU statechart.
  - `flasher` contains a world with a flashlight. In the beginning you get a number. This number represents the amount the bulb has to blink. The exact behavior of the bulb is specified by a YAKINDU statechart.
  - `flash-light-world` is another implementation of a RobotWorld where robots transport packages from stations to unloading stations, have to avoid collisions. There are flash lights to help avoiding collisions. The exact behavior of the robots and flashlights are specified by a YAKINDU statechart.
 
#### Setting up your world
   - When there is the need to add/edit static parameters (magic numbers), do so by extend the class `core.Configuration`
   - To set up a world, you have to implement a subclass of `core.World` which implements all its abstract methods. Inheritance hierachies can be used. To get a feeling for this, have a look at the hierachy of `worlds.infinitewarhouse.InfiniteWarehouse`. The required methods are
     - `getDetectors()` should return a list of implementations of `core.simulation.Detector` to trigger events on certain conditions. Can be empty.
     - `resetScenario()` is called when a scenario is ended and should thus contain code to reset the environment such that a new scenario can be started.
     - `getEntities()` should return the list of active elements in the environment (which should be implementations of `core.Simulation.Entity`).
     - `updateEntities(float delta)` should define the behaviour of entities when the simulation is updated (e.g., by passing the update call to the entities). `delta` is the number of miliseconds since the last frame. Entities should *do the next step*.
     - `refreshEntities()` called when data changes such that entities might to be redrawn or react.
     - `getScenarios()` the list of executable (open-end) scenarios
     - `getTestGroups()` the map of named groups of executable test cases.
     - `getAnimationPanel()` should return an implementation of `core.view.panels.AnimationPanel` in which the environment is drawn.
     - `render(java.awt.Graphics graphics)` should perform all necessary rendering of the simulated environment. Takes place inside of the `AnimationPanel`.
     - `refreshSimulationProperties(int currentHeight, int currentWidth)` called when the size of the frame changes. React to change or leave blank.
     - `getHighlightAtPosition(int x, int y)` is called after a click on the coordinates in the parameters. If something implementing `IHighlightable` is clicked, this should be returned to display its information. If `null` is returned, the current selection is kept.
     - `close()` is called when the simulator is closed. Clean up everything that needs to be cleaned up.

## Setup

**[=> Hier geht es zur Installations-Anleitung für alle Betriebssysteme! <=](INSTALL.md)**

YAKUNDU Statechart Tools ist ein Eclipse-Plugin. Da es Gelegentlich zu Problemen mit einem Nachinstallierten Plugin kommt, empfehlen wir Eclipse und YAKINDU als separates Standalone-Bundle zu installieren ([siehe Anleitung](INSTALL.md)).

Es wird Mindestens Java Version 10 benötigt.

Für Mitarbeiter und Studierende des Hasso-Plattner-Institut steht eine Lizenz für die YAKINDU Statechart Tools Professional Edition bereit. Näheres dazu ebenfalls in der [Installations-Anleitung](INSTALL.md).



## Benutzung

#### Modellierung

- In dem Ordner model können Eclipse/YAKINDU Steuerungen modelliert werden
	- Für die InfinityWarehouse world wird die Roboter-Steuerung in der Datei `model/drivesystem.sct` modelliert.
	- Für die FlashWorld wird die Glühbirnen-Steuerung in der Datei `model/flasher.sct` modelliert.
	- Die Pong World kann mit einem paddle geöffnet werden. Die Steuerung des 1. paddles(dieses wird in der Welt links angezeigt) wird in der Datei `model/paddle1.sct` modelliert. Die des 2. paddles(das paddle wird in der Welt rechts angezeigt) wird in der Datei `model/paddle2.sct` modelliert.
	- Die flash-light world benötigt ein Modell für die Ampel- und eines für die Roboter Steuerung. Die Steuerung der Ampeln wird in der Datei `model/trafficlight.sct`modelliert, die Roboter in der Datei `tlRobot.sct`.
- Die Schnittellen des Statecharts sind bereits vorgegeben ("Definition section" links). Diese dürfen **nicht verändert** werden, es können interne Variablen ("internal") ergänzt werden.
- Auf den Webseiten von YAKINDU findet sich [eine Anleitung zum Modellieren](https://www.itemis.com/en/yakindu/state-machine/documentation/user-guide/edit_editing_statecharts) und eine [Statechart-Referenz](https://www.itemis.com/en/yakindu/state-machine/documentation/user-guide/sclang_statechart_language_reference).

#### Namenskonventionen für Zustände

* Damit der aktuelle Zustand im Simulator angezeigt werden kann, müssen bei der Benennung der Zustände einige Bedingungen eingehalten werden.
* Die **äußerste Region** muss in den einzelnen Welten spezielle Namen haben:
	* InfinityWarehouse world: Die  Region muss ``Drive System`` heißen bzw zumindest genau 13 Zeichen lang sein (inkl. Leerzeichen).
	* FlashWorld: Die Region muss ``flasher``
	* PongWorld: Die Region muss ``pong``
	* Traffic light World: 
		* Im Modell des Roboters muss die äußerste Region ``tlRobot`` heißen
		* Im Modell der Ampel (traffic-light) muss die äußerste Region ``trafficlight`` heißen

* Wenn **Unterregionen** verwendet werden müssen sie entweder unbenannt sein oder mit einem Unterstich ("_") beginnen. 
Sie dürfen keine Lehrzeichen und keine weiteren Unterstiche enthalten.

* In **Zustandsnamen** dürfen *keine* Unterstiche ("_") benutzt werden.


#### Codegenerierung

- Die beigelegten `.sgen`-Dateien enthalten die Konfigurationen für die Codegenerierung. So ist z.B. drivesystem.sgen die Konfiguration für das Modell in drivesystem.sct. 
- Um den Code aus den Modellen zu generieren, muss in Eclipse/YAKINDU ein Rechtsklick auf die .sgen Datei gemacht werden um dann "Generate Code Artifacts" auszuwählen. Daraufhin wird der zum Statechart passende Quelltext automatisch neu generiert.

#### Simulation

- Zum Starten der Simulation wird die Klasse `de.hpi.mod.sim.App` im Ordner `src` rechts geklickt und "Run as" > "Java Application" ausgewählt.
- Als Shortcut steht danach der "Run" Button oben im Eclipse-Menü zur Verfügung.
- Zu Beginn kann die Welt ausgewählt werden, die simuliert werden soll. Dabei sind zur Auswahl: 
	* InfinityWarehouse world
	* FlashWorld
	* PongWorld
- Ist der Simulator gestartet können entweder "Szenarien" (= dynamisch generierte Abläufe mit einem oder mehreren Robotern) oder "Tests" (= vordefinierte Situationen) ausgewählt werden. Innerhalb dieser Szenarien und Tests wird jeweils für alle Entitäten der generierte Quelltext des zuvor modellierten Statecharts ausgeführt.
- Sobald ein Szenario oder Test gestartet wird läuft ein Timer und die Geschwindigkeit kann verändert werden.
- Mit den Pfeiltasten kann navigiert werden, mit `Strg +` und `Strg -` gezoomt. `R` setzt die Kamera zurück. `Space` pausiert den aktuelle laufenden Test / das aktuelle Szenario.
- Mit der linken und rechten Maustaste kann jeweils ein Entität ausgewählt werden, deren Eigenschaften dann in einer info-box ablesbar sind.