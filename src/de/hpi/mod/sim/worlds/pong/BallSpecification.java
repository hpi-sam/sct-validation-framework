package de.hpi.mod.sim.worlds.pong;


import de.hpi.mod.sim.core.scenario.EntitySpecification;
import de.hpi.mod.sim.worlds.pong.PongWorld;

public class BallSpecification implements EntitySpecification<Ball>  {
	
	 private PongWorld world;
	 private double yPos;
	 private double yDirection;
	 private double xDirection;

	 
	 public BallSpecification(double yPos, double xDirection, double yDirection, PongWorld world) {
		 this.yPos = yPos;
		 this.xDirection = xDirection;
	     this.yDirection = yDirection;
	     this.world = world;
	 }

	 
	 @Override
	 public Ball createEntity() {
		 Ball ball = new Ball (yPos, yDirection, xDirection, world); 
		 world.setBall(ball);
	     return ball;
	    }

}
