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
    private static final double diameter = 0.04;
    private boolean outOfBounds; 
    private static final int stateMachineFactor = 1000 ;

   
    
    public Ball(double yPos, double yDirection, double xDirection, PongWorld world) {
    	this.x = 0.9;
    	this.y = yPos;
    	this.xDirection = xDirection;
    	this.yDirection = yDirection;
		this.world = world;
    }

    
    public void render(Graphics graphics, int totalWidth, int totalHeight) {

    	int drawX = (int) (PongWorld.toPixel(x - diameter/2, totalWidth));
        int drawY = (int) (PongWorld.toPixel(-(y + diameter/2) , totalHeight));
        
        
        graphics.setColor(new Color(102,205,0));
        graphics.fillOval(drawX, drawY, (int) (diameter* totalWidth/2), (int) (diameter * totalHeight/2));
        ((Graphics2D) graphics).setStroke(new java.awt.BasicStroke(2));
        graphics.setColor(Color.DARK_GRAY);
        graphics.drawOval(drawX, drawY, (int) (diameter * totalWidth/2), (int) (diameter * totalHeight/2));
    	
    	
    }
    
    
    
    public void update(float delta) {
    	world.collision();
    	
    	//bounce the ball when it hits the edge of the screen
    	if (x > 1) {
    		switchXDirection();  	
    	}

    	if (getLeftEnd()< -1 ) {
        	x = 1;
        	outOfBounds = true;	
    	}
        
    	if (getLowerEnd() <= -0.9  || getUpperEnd() >= 0.9) {
    			switchYDirection();
    		}
    		
        x += xDirection;
        y += yDirection;
    }

    public void switchYDirection() {
		yDirection *= -1;
	}

    

   //@Override
   //public boolean hasPassedAllTestCriteria() {
   //	   return !outOfBounds;
   //}

	public double getYPos() {
		return y;
	}

	public double getXPos() {
		return x;
	}

	public double getDiameter() {
		return diameter;
	}

	public void switchXDirection() {
		xDirection *= -1;
	}


	@Override
	public List<String> getHighlightInfo() {
		return Arrays.asList("x- Position:" + (int) stateMachineFactor * x, "y-Position "+ (int) stateMachineFactor * y, "y-Direction: " + (int) stateMachineFactor * yDirection, "x-Direction: "+ (int) stateMachineFactor * xDirection);
	}



	public double refresh() {
		return y;
	}

	
	public double getUpperEnd() {
		return y + diameter/2;
	}

	public double getLowerEnd() {
		return y - diameter/2;
	}
	public double getLeftEnd() {
		return x - diameter/2;
	}
	public double getRightEnd() {
		return x + diameter/2;
	}


	public boolean isOutOfBounds() {
		return outOfBounds;
	}
	
	
	

		


}
