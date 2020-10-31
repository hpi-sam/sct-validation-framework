package de.hpi.mod.sim.worlds.pong;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Arrays;
import java.util.List;

import de.hpi.mod.sim.core.Configuration;
import de.hpi.mod.sim.core.simulation.Entity;
import de.hpi.mod.sim.core.simulation.IHighlightable;
import de.hpi.mod.sim.worlds.pong.PongWorld;

public class Ball implements Entity, IHighlightable{

    private double x, y;
    private PongWorld world;
    private double yDirection, xDirection = -0.002;
    private boolean outOfBounds;
    boolean isInTests;
    private double yDirectionFactor = 0.003 , xDirectionFactor = 0.001;
    private double yDirectionDuell, xDirectionDuell;

   
    

	 public Ball(double yPos, double yDirection, double xDirection, PongWorld world, boolean isInTests) {
	    	this.x = PongConfiguration.upperBoundary;
	    	this.y = yPos;
	    	this.xDirection = xDirection;
	    	this.yDirection = yDirection;
			this.world = world;
			this.isInTests = isInTests;
	    }
    

   //constructor for a ball in a world with 2 paddles
    public Ball(double xPos, double yPos, double yDirection, double xDirection, PongWorld world) {
    	this.x = xPos;
    	this.y = yPos;
    	this.xDirection = xDirection;
    	this.yDirection = yDirection;
    	this.xDirectionDuell = xDirection;
    	this.yDirectionDuell = yDirection;
		this.world = world;	
	}


	public void render(Graphics graphics, int totalWidth, int totalHeight) {

    	int drawX = (int) (PongWorld.toPixel(x - getDiameter()/2, totalWidth));
        int drawY = (int) (PongWorld.toPixel(-(y + getDiameter()/2) , totalHeight));
        
        
        graphics.setColor(PongConfiguration.ballColor);
        graphics.fillOval(drawX, drawY, (int) (getDiameter()* totalWidth/2), (int) (getDiameter() * totalHeight/2));
        ((Graphics2D) graphics).setStroke(new java.awt.BasicStroke(2));
        graphics.setColor(Color.DARK_GRAY);
        graphics.drawOval(drawX, drawY, (int) (getDiameter() * totalWidth/2), (int) (getDiameter() * totalHeight/2));
    	
    	
    }
    
    
    
    public void update(float delta) {
    	world.collision();
    	
    	
        
    	if (getLowerEnd() <= PongConfiguration.lowerBoundary  || getUpperEnd() >= PongConfiguration.upperBoundary) {
    			switchYDirection();
    		}
    		
        x += getXDirection();
        y += getYDirection();
    }
    
    
     private double getXDirection(){
    	 return xDirection * Configuration.getEntitySpeedFactor();
     }
     
     private double getYDirection(){
    	 return yDirection * Configuration.getEntitySpeedFactor();
     }
    
    

    public void switchYDirection() {
		yDirection *= -1;
	}

    

   @Override
   public boolean hasPassedAllTestCriteria() {
   	   return !outOfBounds;
   }

	public double getYPos() {
		return y;
	}

	public double getXPos() {
		return x;
	}
	

	public double getDiameter() {
		return PongConfiguration.diameter;
	}

	public void switchXDirection() {
		xDirection *= -1;
	}


	@Override
	public List<String> getHighlightInfo() {
		return Arrays.asList("x- Position:" + (int) (PongConfiguration.stateMachineFactor * x),
				"y-Position "+ (int) (PongConfiguration.stateMachineFactor * y),
				"y-Direction: " + (int) (PongConfiguration.stateMachineFactor * yDirection),
				"x-Direction: "+ (int) (PongConfiguration.stateMachineFactor * xDirection));
	}



	public double refresh() {
		return y;
	}

	
	public double getUpperEnd() {
		return y + getDiameter()/2;
	}

	public double getLowerEnd() {
		return y - getDiameter()/2;
	}
	public double getLeftEnd() {
		return x - getDiameter()/2;
	}
	public double getRightEnd() {
		return x + getDiameter()/2;
	}


	public boolean isOutOfBounds() {
		return outOfBounds;
	}
	
	public void increaseXDirection(){
		this.xDirection += xDirectionFactor;
	}


	public void increaseYDirection() {
		if(yDirection < 0) {
			yDirection -= yDirectionFactor;
		}
		else{
			this.yDirection += yDirectionFactor;
		}
		
	}

	public void setXPos(int newX) {
		this.x = newX;
		
	}

	public void setOutOfBounds() {
		this.outOfBounds = true;
		
	}
	
	public void setNewStartSpeed() {
		//the speed is set back to 8 speedFactors before the point was made 
		xDirection = xDirectionDuell;
		yDirection = yDirectionDuell;
		}


	public boolean getIsInTests() {
		return isInTests;
	}
}
