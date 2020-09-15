package de.hpi.mod.sim.worlds.abstract_robots;

import de.hpi.mod.sim.core.simulation.Entity;
import de.hpi.mod.sim.core.simulation.IHighlightable;
import de.hpi.mod.sim.worlds.abstract_grid.GridRenderer;
import de.hpi.mod.sim.worlds.abstract_grid.GridWorld;
import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.util.ExplosionRenderer;

import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.List;

public abstract class RobotWorld extends GridWorld {

    private ExplosionRenderer explosionRenderer;

    private RobotRenderer robotRenderer;

    @Override
    public void initialize() {
        super.initialize();
        explosionRenderer = new ExplosionRenderer();
        robotRenderer = new RobotRenderer(getSimulationBlockView(), getRobotGridManager());
    }

    @Override
    public void render(Graphics graphics) {
        super.render(graphics);
        renderEntities(graphics);
        explosionRenderer.render(graphics);
    }

    @Override
    public void resetAnimationPanel() {
        explosionRenderer.reset();
    }

    public void reportCollision(Robot r1, Robot r2, String reason) {
        getFrame().displayWarningMessage(reason);
        getSimulationRunner().setRunForbidden(true);
        renderExplosion(r1);
    }
    
    public void reportDeadlock(String reason) {
        getFrame().displayWarningMessage(reason);
    }

    public void renderExplosion(Robot robot) {
        Point2D drawPos = getSimulationBlockView().toDrawPosition(robot.x(), robot.y());
        explosionRenderer.showExplosion(drawPos);
    }
    
    @Override
    public void mousePressed(java.awt.event.MouseEvent e) {
        explosionRenderer.mousePressed(e);
    }    
   
    public RobotGridManager getRobotGridManager() {
        return (RobotGridManager) getGridManager();
    }

    public List<Robot> getRobots() {
        return getRobotGridManager().getRobots();
    }
    
    @Override
    public List<? extends Entity> getEntities() {
        return getRobots();
    }
    
    @Override
    public IHighlightable getHighlightAtPosition(int x, int y) {
        Position pos = getSimulationBlockView().toGridPosition(x, y);
        for (Robot robot : getRobots()) {
            if (robot.pos().equals(pos) || robot.oldPos().equals(pos))
                return robot;
        }
        return null;
    }
    
    protected void renderEntities(Graphics graphics) {
        robotRenderer.render(graphics, getSimulationBlockView().getBlockSize());
    }
}