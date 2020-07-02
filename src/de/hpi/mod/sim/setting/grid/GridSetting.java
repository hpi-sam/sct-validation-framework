package de.hpi.mod.sim.setting.grid;

import de.hpi.mod.sim.core.model.Setting;
import de.hpi.mod.sim.core.simulation.Simulation;
import de.hpi.mod.sim.core.view.SimulatorFrame;
import de.hpi.mod.sim.core.view.sim.SimulationView;

import java.awt.Graphics;

public abstract class GridSetting extends Setting {
    
    private GridRenderer gridRenderer;
    
    private SimulationBlockView simView;

    public GridSetting() {
        simView = new SimulationBlockView(this);
    }

    public abstract IGrid getGrid();

    @Override
    public void initialize(SimulatorFrame frame, Simulation simulation) {
        super.initialize(frame, simulation);
        gridRenderer = new GridRenderer(getSimulationBlockView(), getGrid());
    }
    
    @Override
    public void render(Graphics graphics) {
        gridRenderer.render(graphics);
    }

    @Override
    public SimulationView getView() {
        return getSimulationBlockView();
    }

    public SimulationBlockView getSimulationBlockView() {
        return simView;
    }
}