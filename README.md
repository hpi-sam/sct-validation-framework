# Statechart Simulator

This project is a simulating environment which allows to run simulations in which it is possible to define some behavior by YAKINDU Statechart tools.

## Project Structure

The project is devided into the following directories:
- **`model`** contains the statechart model
    + The model is contained in the file `drivesystem.sct`, which can be edited by YAKINDU
    + The belonging `.sgen`-file contains a predefined configuration for generating executable code from the statechart.
- **`src`** contains the source code of the simulation environment
- **`src-gen`** contains the code generated from the statecharts. It is automatically updated after running the `.sgen`-file.
- **`res`** contains resources for the GUI, as for example images
- **`test`** contains tests for the project

## Simulator Code Structure
- To start the simulator, execute the class `de.hpi.mod.sim.App`
- The simulator code is devided in two packages, `de.hpi.mod.sim.core` and `de.hpi.mod.sim.core`.
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
+ Currently there are 3 worlds
  - `abstract_grid.GridWorld` introduces an abstract environment with a grid-based 2D surface and provides useful utilities like the concept of positions and orientations in that grid
  - `abstract_robots.RobotWorld` is a still abstract extension of the `GridWorld` and introduces robots to be moved around on the grid
  - `infinite_warehouse` is a ready-to-use implementation of a `RobotWorld` where robots transport package from stations to unloading stations, have to avoid collisions and be recharged. The exact behavior of the robots drivesystem is specified by a YAKINDU statechart.

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
   - To make your world accessible in the simulator, add the whole package-path of the class extending `core.World` to the list `POSSIBLE_WORLDS` in `App.java`. Then your world will appear in the list when starting the simulator. If there is only one world in the list, the simulator will choose this world automatically.   

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