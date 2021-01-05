package de.hpi.mod.sim.worlds.pong;

import de.hpi.mod.sim.core.scenario.EntitySpecification;

public class RightPaddleSpecification implements EntitySpecification<RightPaddle>{

    private PongWorld world;

    public RightPaddleSpecification(PongWorld world) {
        this.world = world;
    }
    
    @Override
    public RightPaddle createEntity() {
    	
    	RightPaddle paddleRight = new RightPaddle();
    	world.setPaddleRight(paddleRight);
    	return paddleRight;
    	
    }

}
