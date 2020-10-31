package de.hpi.mod.sim.worlds.pong;


import de.hpi.mod.sim.core.scenario.EntitySpecification;
import de.hpi.mod.sim.worlds.pong.PongWorld;

public class BallSpecification implements EntitySpecification<Ball>  {
	
	 private PongWorld world;
	 private double xPos = -2;
	 private double yPos;
	 private double yDirection;
	 private double xDirection;

	 
	 public BallSpecification(double yPos, double xDirection, double yDirection, PongWorld world) {
		 this.yPos = yPos;
		 this.xDirection = xDirection;
	     this.yDirection = yDirection;
	     this.world = world;
	 }

	 public BallSpecification(double xPos, double yPos, double xDirection, double yDirection, PongWorld world) {
		 this.yPos = yPos;
		 this.xPos = xPos;
		 this.xDirection = xDirection;
	     this.yDirection = yDirection;
	     this.world = world;
	 }
	 
	 
	 @Override
	 public Ball createEntity() {
		 if(xPos != -2) {
			 Ball ball = new Ball (xPos, yPos, yDirection, xDirection, world); 
			 world.setBall(ball);
		     return ball;
		 }
		 Ball ball = new Ball (yPos, yDirection, xDirection, world); 
		 world.setBall(ball);
	     return ball;
	    }

}
