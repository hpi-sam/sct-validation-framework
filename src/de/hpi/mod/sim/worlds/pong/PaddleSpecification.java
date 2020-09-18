package de.hpi.mod.sim.worlds.pong;

import de.hpi.mod.sim.core.scenario.EntitySpecification;

public class PaddleSpecification implements EntitySpecification<Paddle> {

    private PongWorld world;

    public PaddleSpecification(PongWorld world) {
        this.world = world;
    }

    @Override
    public Paddle createEntity() {
        Paddle paddle = new Paddle(-0.9); 
        world.setPaddle1(paddle);
        return paddle;
    }

}
