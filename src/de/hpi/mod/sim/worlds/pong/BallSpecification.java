package de.hpi.mod.sim.worlds.pong;


import de.hpi.mod.sim.core.scenario.EntitySpecification;
import de.hpi.mod.sim.worlds.pong.PongWorld;

public class BallSpecification implements EntitySpecification<Ball>  {
	
	 private PongWorld world;
	 private double yPos;
	 private double yDirection;

	 
	 public BallSpecification(double yPos, double yDirection, PongWorld world) {
		 this.yPos = yPos;
	     this.yDirection = yDirection;
	     this.world = world;
	 }

	 
	 @Override
	 public Ball createEntity() {
		 Ball ball = new Ball (yPos, yDirection, world); 
		 world.setBall(ball);
	     return ball;
	    }

}
