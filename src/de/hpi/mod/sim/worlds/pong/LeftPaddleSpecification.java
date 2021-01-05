package de.hpi.mod.sim.worlds.pong;

import de.hpi.mod.sim.core.scenario.EntitySpecification;

public class LeftPaddleSpecification implements EntitySpecification<LeftPaddle>{

    private PongWorld world;

    public LeftPaddleSpecification(PongWorld world) {
        this.world = world;
    }
    
    
    @Override
    public LeftPaddle createEntity() {
    
    	LeftPaddle paddle = new LeftPaddle();     		
    	world.setPaddleLeft(paddle);
        return paddle;
    }

}
