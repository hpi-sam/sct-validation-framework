package de.hpi.mod.sim.worlds.pong;

import de.hpi.mod.sim.IStatemachine;
import de.hpi.mod.sim.core.simulation.IHighlightable;
import de.hpi.mod.sim.core.statechart.StateChartEntity;
import de.hpi.mod.sim.core.statechart.StateChartWrapper;
import de.hpi.mod.sim.pong.IPongStatemachine;
import de.hpi.mod.sim.pong.PongStatemachine;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Arrays;
import java.util.List;

public class Paddle extends StateChartWrapper<PongStatemachine.State> 
		implements StateChartEntity, IHighlightable, IPongStatemachine.SCInterfaceOperationCallback{
 

    private final double x;
    private double y = 0 ;

    private static final double width = 0.04, height = 0.2;
    private static final int stateMachineFactor = 1000;
 
    public Paddle(double d) {
    	start();
        this.x = d;
        y = 0;
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
		return Arrays.asList("y-Position:  " + stateMachineFactor * y);
	}


	public void refresh(double ballPosition) {
		//multiplied because we are in range -1 to 1, but Yakindu can't do doubles, so the range is from -1000 to 1000
		getStatemachine().raiseBallPos((long) (stateMachineFactor * ballPosition));
		super.updateTimer();
		
	}


	@Override
	public long myPos() {
		return (long) (stateMachineFactor * y);
	}


    @Override
    public void start() {
        getStatemachine().getSCInterface().setSCInterfaceOperationCallback(this);
        super.start();
    }

}
