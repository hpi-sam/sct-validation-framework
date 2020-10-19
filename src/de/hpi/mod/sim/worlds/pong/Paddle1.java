package de.hpi.mod.sim.worlds.pong;

import de.hpi.mod.sim.IStatemachine;
import de.hpi.mod.sim.core.Configuration;
import de.hpi.mod.sim.core.simulation.IHighlightable;
import de.hpi.mod.sim.core.statechart.StateChartEntity;
import de.hpi.mod.sim.core.statechart.StateChartWrapper;
import de.hpi.mod.sim.paddle1.IPaddle1Statemachine;
import de.hpi.mod.sim.paddle1.Paddle1Statemachine;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Arrays;
import java.util.List;


public class Paddle1 extends StateChartWrapper<Paddle1Statemachine.State> 
		implements StateChartEntity, IHighlightable, IPaddle1Statemachine.SCInterfaceOperationCallback{
 

    private final double x;
    private double y;
    private boolean reboundBall = false;
    private int points = 0;
    
 
    public Paddle1() {
    	start();
        this.x = PongConfiguration.lowerBoundary;
        y = 0;
    }

    
    public void render(Graphics graphics, int totalWidth, int totalHeight) {
    	
    	int drawX = (int) (PongWorld.toPixel(getLeftEnd(), totalWidth) );
    	int drawY = (int) (PongWorld.toPixel(-(getUpperEnd()), totalHeight));
    	graphics.setColor(new Color(192,255,62));
    	graphics.fillRect(drawX, drawY, (int) (PongConfiguration.paddleWidth * totalWidth/2), (int) (PongConfiguration.paddleHeight * totalHeight/2));
    	((Graphics2D) graphics).setStroke(new java.awt.BasicStroke(2));
    	graphics.setColor(new Color(102,205,0));
    	graphics.drawRect(drawX, drawY, (int) (PongConfiguration.paddleWidth * totalWidth/2), (int) (PongConfiguration.paddleHeight * totalHeight/2));
    }
    
    
    private Paddle1Statemachine getStatemachine() {
    	return (Paddle1Statemachine) chart;
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
    	y -= PongConfiguration.stepsPerCall * Configuration.getEntitySpeedFactor(); 
    }
    
    private void goUp() {
    	y += PongConfiguration.stepsPerCall * Configuration.getEntitySpeedFactor();
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
        return new Paddle1Statemachine();
    }

    @Override
    protected Paddle1Statemachine.State[] getStates() {
        return Paddle1Statemachine.State.values(); 
    }

    @Override
    protected boolean isActive(Paddle1Statemachine.State state) {
        /*
         * This is not intended by the YAKINDU implementation and source generation.
         * Officially, the YAKINDU interface does not support this, which is why we have
         * to cast to the actual DrivesystemStateChart object.
         */
        return ((Paddle1Statemachine) chart).isStateActive(state);
    }

    
	
	public double getHeight() {
		return PongConfiguration.paddleHeight;
	}	
	
	public double getUpperEnd() {
		return y + getHeight()/2;
	}
	
	
	public double getLowerEnd() {
		return y - getHeight()/2;
	}
	
	public double getYPos() {
		return y;
	}
	
	public double getRightEnd() {
		return x + (getWidth()/2);
	}
	public double getLeftEnd() {
		return x - getWidth()/2;
	}

	@Override
	public List<String> getHighlightInfo() {
		return Arrays.asList("y-Position:  " + myPos());
	}


	public void refresh(double ballPosition) {
		//multiplied because we are in range -1 to 1, but Yakindu can't do doubles, so the range is from -1000 to 1000
		getStatemachine().raiseBallPos((long) (PongConfiguration.stateMachineFactor * ballPosition));
		super.updateTimer();
		
	}
	
	@Override
	public boolean hasPassedAllTestCriteria() {
		return reboundBall;
	}


	@Override
	public long myPos() {
		return (long) (PongConfiguration.stateMachineFactor  * y);
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
		return PongConfiguration.paddleWidth;
	}


	public void reboundBall() {
		reboundBall = true;
	}


	public int getPoints() {
		return points;
	}


	public void setPoints(int points) {
		this.points = points;
	}
	

}
