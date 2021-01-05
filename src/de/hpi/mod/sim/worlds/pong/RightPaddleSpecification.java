<<<<<<< HEAD:simulator/src/de/hpi/mod/sim/worlds/pong/RightPaddleSpecification.java
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
=======
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
>>>>>>> 308c2f6b59a427da313ecd842c5fe5e5918e5f8c:simulator/src/de/hpi/mod/sim/worlds/pong/RightPaddleSpecification.java
