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

    private static final double width = 0.08, height = 0.4;
    private static final int stateMachineFactor = 1000;
    private boolean reboundBall = false; 
 
    public Paddle(double d) {
    	start();
        this.x = d;
        y = 0;
    }

    
    public void render(Graphics graphics, int totalWidth, int totalHeight) {
    	
    	int drawX = (int) (PongWorld.toPixel(getLeftEnd(), totalWidth) );
    	int drawY = (int) (PongWorld.toPixel(-(getUpperEnd()), totalHeight));
    	graphics.setColor(new Color(192,255,62));
    	graphics.fillRect(drawX, drawY, (int) (width * totalWidth/2), (int) (height * totalHeight/2));
    	((Graphics2D) graphics).setStroke(new java.awt.BasicStroke(2));
    	graphics.setColor(new Color(102,205,0));
    	graphics.drawRect(drawX, drawY, (int) (width * totalWidth/2), (int) (height * totalHeight/2));
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
    	y =  y - 0.003; 
    }
    
    private void goUp() {
    	y = y + 0.003;
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
	
	public double getUpperEnd() {
		return y + height/2;
	}
	
	
	public double getLowerEnd() {
		return y - height/2;
	}
	
	public double getYPos() {
		return y;
	}
	
	public double getRightEnd() {
		return x + width/2;
	}
	public double getLeftEnd() {
		return x - width/2;
	}

	@Override
	public List<String> getHighlightInfo() {
		return Arrays.asList("y-Position:  " + myPos());
	}


	public void refresh(double ballPosition) {
		//multiplied because we are in range -1 to 1, but Yakindu can't do doubles, so the range is from -1000 to 1000
		getStatemachine().raiseBallPos((long) (stateMachineFactor * ballPosition));
		super.updateTimer();
		
	}
	
	@Override
	public boolean hasPassedAllTestCriteria() {
		return reboundBall;
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


	public double getXPos() {
		return x;
	}


	public double getWidth() {
		return width;
	}


	public void reboundBall() {
		reboundBall = true;
	}


}
