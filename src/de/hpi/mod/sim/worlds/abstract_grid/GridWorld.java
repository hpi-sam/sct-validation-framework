package de.hpi.mod.sim.worlds.abstract_grid;

import de.hpi.mod.sim.core.World;
import de.hpi.mod.sim.core.view.panels.AnimationPanel;

import java.awt.Graphics;

public abstract class GridWorld extends World {
    
    private GridRenderer gridRenderer;
    
    private SimulationBlockView simView;

    private GridManager gridManager;

    public GridWorld() {
        gridManager = createGridManager();
        simView = new SimulationBlockView(this);
    }

    protected abstract GridManager createGridManager();

    @Override
    public void initialize() {
        gridRenderer = createGridRenderer(simView, getGridManager());
    }

    protected GridRenderer createGridRenderer(SimulationBlockView panel, GridManager manager) {
        return new GridRenderer(panel, manager);
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

    public GridManager getGridManager() {
        return gridManager;
    }

}