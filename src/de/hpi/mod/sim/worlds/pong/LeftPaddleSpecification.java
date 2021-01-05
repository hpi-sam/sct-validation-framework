<<<<<<< HEAD:simulator/src/de/hpi/mod/sim/worlds/pong/LeftPaddleSpecification.java
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
=======
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
>>>>>>> 308c2f6b59a427da313ecd842c5fe5e5918e5f8c:simulator/src/de/hpi/mod/sim/worlds/pong/LeftPaddleSpecification.java
