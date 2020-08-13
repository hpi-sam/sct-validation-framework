package de.hpi.mod.sim.worlds.abstract_grid;

import de.hpi.mod.sim.core.World;
import de.hpi.mod.sim.core.simulation.SimulationRunner;
import de.hpi.mod.sim.core.view.SimulatorFrame;
import de.hpi.mod.sim.core.view.panels.AnimationPanel;

import java.awt.Graphics;

public abstract class GridWorld extends World {
    
    private GridRenderer gridRenderer;
    
    private SimulationBlockView simView;

    public GridWorld() {
        simView = new SimulationBlockView(this);
    }

    public abstract IGrid getGrid();

    @Override
    public void initialize(SimulatorFrame frame, SimulationRunner simulationRunner) {
        super.initialize(frame, simulationRunner);
        gridRenderer = new GridRenderer(getSimulationBlockView(), getGrid());
    }
    
    @Override
    public void render(Graphics graphics) {
        gridRenderer.render(graphics);
    }

    @Override
    public AnimationPanel getAnimationPanel() {
        return getSimulationBlockView();
    }

    public SimulationBlockView getSimulationBlockView() {
        return simView;
    }
}