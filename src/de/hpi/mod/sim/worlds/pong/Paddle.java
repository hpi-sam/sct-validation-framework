package de.hpi.mod.sim.worlds.pong;

import de.hpi.mod.sim.IStatemachine;
import de.hpi.mod.sim.core.statechart.StateChartEntity;
import de.hpi.mod.sim.core.statechart.StateChartWrapper;
import de.hpi.mod.sim.pong.PongStatemachine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Paddle extends StateChartWrapper<PongStatemachine.State> implements StateChartEntity {

    private final double x;
    private double y;

    private static final double width = 0.04, height = 0.2;

    public Paddle(double d) {
        this.x = d;
        y = 0;
    }

    @Override
    public String getMachineState() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getTopStateName() {
        return "pong";
    }

    public void render(Graphics graphics, int totalWidth, int totalHeight) {

        
        int drawX = (int) (PongWorld.toPixel(x, totalWidth) - totalWidth * width / 2);
        int drawY = (int) (PongWorld.toPixel(-y, totalHeight) - totalHeight * height / 2);
        graphics.setColor(new Color(0, 20, 198));
        graphics.fillRect(drawX, drawY, (int) (width * totalWidth), (int) (height * totalHeight));
        ((Graphics2D) graphics).setStroke(new java.awt.BasicStroke(2));
        graphics.setColor(Color.DARK_GRAY);
        graphics.drawRect(drawX, drawY, (int) (width * totalWidth), (int) (height * totalHeight));
    }

    @Override
    public void update() {
        // TODO Auto-generated method stub

    }

    @Override
    public IStatemachine createStateMachine() {
        return new PongStatemachine();
    }

    @Override
    protected PongStatemachine.State[] getStates() {
        return PongStatemachine.State.values(); 
    }

    @Override
    protected boolean isActive(PongStatemachine.State state) {
        /*
         * This is not intended by the YAKINDU implementation and source generation.
         * Officially, the YAKINDU interface does not support this, which is why we have
         * to cast to the actual DrivesystemStateChart object.
         */
        return ((PongStatemachine) chart).isStateActive(state);
    }


    
}
