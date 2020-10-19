package de.hpi.mod.sim.worlds.pong;

import de.hpi.mod.sim.core.scenario.EntitySpecification;

public class Paddle2Specification implements EntitySpecification<Paddle2>{

    private PongWorld world;

    public Paddle2Specification(PongWorld world) {
        this.world = world;
    }
    
    @Override
    public Paddle2 createEntity() {
    	
    	Paddle2 paddleRight = new Paddle2();
    	world.setPaddleRight(paddleRight);
    	return paddleRight;
    	
    }

}
