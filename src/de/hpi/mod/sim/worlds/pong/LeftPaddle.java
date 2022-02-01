package de.hpi.mod.sim.worlds.pong;

import de.hpi.mod.sim.core.Configuration;
import de.hpi.mod.sim.core.simulation.IHighlightable;
import de.hpi.mod.sim.core.statechart.StateChartEntity;
import de.hpi.mod.sim.core.statechart.StateChartWrapper;
import de.hpi.mod.sim.statemachines.pong.PaddleControl1;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Arrays;
import java.util.List;

import com.yakindu.core.IStatemachine;


public class LeftPaddle extends StateChartWrapper<PaddleControl1.State> 
		implements StateChartEntity, IHighlightable, PaddleControl1.OperationCallback{
 

    private final double x;
    private double y;
    private boolean reboundBall = false;
    public int score = 0;
    
 
    public LeftPaddle() {
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
    	graphics.setColor(PongConfiguration.paddleColor);
    	graphics.drawRect(drawX, drawY, (int) (PongConfiguration.paddleWidth * totalWidth/2), (int) (PongConfiguration.paddleHeight * totalHeight/2));
    }
    
    
    private PaddleControl1 getStatemachine() {
    	return (PaddleControl1) chart;
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
    public String getTopLevelRegionName() {
        return "pong";
    }

	

    @Override
    public IStatemachine createStateMachine() {
        return new PaddleControl1();
    }

    @Override
    protected PaddleControl1.State[] getStates() {
        return PaddleControl1.State.values(); 
    }

    @Override
    protected boolean isActive(PaddleControl1.State state) {
        /*
         * This is not intended by the YAKINDU implementation and source generation.
         * Officially, the YAKINDU interface does not support this, which is why we have
         * to cast to the actual DrivesystemStateChart object.
         */
        return ((PaddleControl1) chart).isStateActive(state);
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
        getStatemachine().setOperationCallback(this);
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


	public int getScore() {
		return score;
	}


	public void increaseScore() {
		this.score++;
		
	}


	public void resetScore() {
		this.score=0;
	}	

}
