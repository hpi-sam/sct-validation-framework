package de.hpi.mod.sim.worlds.pong;

import de.hpi.mod.sim.core.scenario.EntitySpecification;

public class PaddleSpecification implements EntitySpecification<Paddle1>{

    private PongWorld world;

    public PaddleSpecification(PongWorld world) {
        this.world = world;
    }
    
    
    @Override
    public Paddle1 createEntity() {
    
    	Paddle1 paddle = new Paddle1();     		
    	world.setPaddleLeft(paddle);
        return paddle;
    }

}
