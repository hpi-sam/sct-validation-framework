package de.hpi.mod.sim.worlds.pong;

import de.hpi.mod.sim.IStatemachine;
import de.hpi.mod.sim.core.simulation.IHighlightable;
import de.hpi.mod.sim.core.statechart.StateChartEntity;
import de.hpi.mod.sim.core.statechart.StateChartWrapper;
import de.hpi.mod.sim.flasher.FlasherStatemachine;
import de.hpi.mod.sim.pong.PongStatemachine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Arrays;
import java.util.List;

public class Paddle extends StateChartWrapper<PongStatemachine.State> 
		implements StateChartEntity, IHighlightable {

    private final double x;
    private double y;

    private static final double width = 0.04, height = 0.2;

    public Paddle(double d) {
    	super.start();
        this.x = d;
        y = 0;
        update();
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
    
    
    private PongStatemachine getStatemachine() {
    	return (PongStatemachine) chart;
    }

    @Override
    public void update() {
    	/**
    	 * Runs a cycle of the statechart and checks if any functions got fired
    	 */
    	getStatemachine().raiseMyPos((int) y * 1000);
    	
    	if (getStatemachine().isRaisedUp())
    		goUp();
    	if (getStatemachine().isRaisedDown())
    		goDown();    
    	
    }
    
    
    private void goDown() {
    	y =  y - 0.001; 
    	// TODO Add method getPos for statemachine 
    }
    
    private void goUp() {
    	y = y + 0.001;
    }
    
    
    
    @Override
    public String getMachineState() {
        return getChartState();
    
    }

    @Override
    public String getTopStateName() {
        return "pong";
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

    
	
	public double getHeight() {
		return height;
	}	
	
	public double getYPos() {
		return y;
	}

	@Override
	public List<String> getHighlightInfo() {
		return Arrays.asList("y-Position:  " + y, "cuurent state in the Statemachine: ", getMachineState());
	}


	public void refresh(double ballPosition) {
		//multiplied because we are in range -1 to 1, but Yakindu can't do doubles
		getStatemachine().raiseMyPos((long) (1000 * y));
		getStatemachine().raiseBallPos((long) (1000 * ballPosition));
		super.updateTimer();
		
	}

}
