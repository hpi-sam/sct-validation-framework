<<<<<<< HEAD
package de.hpi.mod.sim.worlds.pong;

import java.util.ArrayList;
import java.util.List;

import de.hpi.mod.sim.core.scenario.EntitySpecification;
import de.hpi.mod.sim.core.scenario.Scenario;
import de.hpi.mod.sim.core.simulation.Entity;

public class ScenarioGenerator {

    private PongWorld world;
    
    public ScenarioGenerator(PongWorld world) {
        this.world = world;
       
    }


    private class StraightScenario extends Scenario {
    	public StraightScenario() {
    		name = "STRAIGHT";
    	}
    	
    	@Override
    	public List<EntitySpecification<? extends Entity>> getScenarioEntities() {
    		List<EntitySpecification<? extends Entity>> list = new ArrayList<>();
    		LeftPaddleSpecification paddle = new LeftPaddleSpecification(world);
    		BallSpecification ball = new BallSpecification(0, -0.008 , 0, world, false);
    		list.add(paddle);
    		list.add(ball);
    		return list;
    	}
    }
    
    
    private class EasyScenario extends Scenario {
        public EasyScenario() {
            name = "EASY";
        }

        @Override
        public List<EntitySpecification<? extends Entity>> getScenarioEntities() {
            List<EntitySpecification<? extends Entity>> list = new ArrayList<>();
            LeftPaddleSpecification paddle = new LeftPaddleSpecification(world);
            BallSpecification ball = new BallSpecification(0, -0.001, 0.003, world, false);
            list.add(paddle);
            list.add(ball);
            return list;
        }
    }
    
    private class MiddleScenario extends Scenario {
	    public MiddleScenario() {
	        name = "MIDDLE";
	    }
	
	    @Override
	    public List<EntitySpecification<? extends Entity>> getScenarioEntities() {
	        List<EntitySpecification<? extends Entity>> list = new ArrayList<>();
	        LeftPaddleSpecification paddle = new LeftPaddleSpecification(world);
	        BallSpecification ball = new BallSpecification(0.1, -0.002, -0.003, world, false);
	        list.add(paddle);
	        list.add(ball);
	        return list;
	    }
	}
    
    
    
    
    private class HardScenario extends Scenario {
	    public HardScenario() {
	        name = "HARD";
	    }
	
	    @Override
	    public List<EntitySpecification<? extends Entity>> getScenarioEntities() {
	        List<EntitySpecification<? extends Entity>> list = new ArrayList<>();
	        LeftPaddleSpecification paddle = new LeftPaddleSpecification(world);
	        BallSpecification ball = new BallSpecification(0.1, -0.004, 0.005, world, false);
	        list.add(paddle);
	        list.add(ball);
	        return list;
	    }
    }
	
	    private class DuellScenario extends Scenario {
		    public DuellScenario() {
		        name = "DUELL";
		    }
		
		    @Override
		    public List<EntitySpecification<? extends Entity>> getScenarioEntities() {
		        List<EntitySpecification<? extends Entity>> list = new ArrayList<>();
		        LeftPaddleSpecification paddle = new LeftPaddleSpecification(world);
		        RightPaddleSpecification paddle2 = new RightPaddleSpecification(world);
		        BallSpecification ball = new BallSpecification(0, 0.1, -0.002, -0.003, world);
		        list.add(paddle);
		        list.add(paddle2);
		        list.add(ball);
		        return list;
		    }
    
    }
    
    
   
    public List<Scenario> getScenarios() {
        List<Scenario> scenarios = new ArrayList<>();
        scenarios.add(new StraightScenario());
        scenarios.add(new EasyScenario());
        scenarios.add(new MiddleScenario());
        scenarios.add(new HardScenario());
        scenarios.add(new DuellScenario());
        return scenarios;
    }

}
=======
package de.hpi.mod.sim.worlds.pong;

import java.util.ArrayList;
import java.util.List;

import de.hpi.mod.sim.core.scenario.EntitySpecification;
import de.hpi.mod.sim.core.scenario.Scenario;
import de.hpi.mod.sim.core.simulation.Entity;

public class ScenarioGenerator {

    private PongWorld world;
    
    public ScenarioGenerator(PongWorld world) {
        this.world = world;
       
    }


    private class StraightScenario extends Scenario {
    	public StraightScenario() {
    		name = "STRAIGHT";
    	}
    	
    	@Override
    	public List<EntitySpecification<? extends Entity>> getScenarioEntities() {
    		List<EntitySpecification<? extends Entity>> list = new ArrayList<>();
    		LeftPaddleSpecification paddle = new LeftPaddleSpecification(world);
    		BallSpecification ball = new BallSpecification(0, -0.008 , 0, world, false);
    		list.add(paddle);
    		list.add(ball);
    		return list;
    	}
    }
    
    
    private class EasyScenario extends Scenario {
        public EasyScenario() {
            name = "EASY";
        }

        @Override
        public List<EntitySpecification<? extends Entity>> getScenarioEntities() {
            List<EntitySpecification<? extends Entity>> list = new ArrayList<>();
            LeftPaddleSpecification paddle = new LeftPaddleSpecification(world);
            BallSpecification ball = new BallSpecification(0, -0.001, 0.003, world, false);
            list.add(paddle);
            list.add(ball);
            return list;
        }
    }
    
    private class MiddleScenario extends Scenario {
	    public MiddleScenario() {
	        name = "MIDDLE";
	    }
	
	    @Override
	    public List<EntitySpecification<? extends Entity>> getScenarioEntities() {
	        List<EntitySpecification<? extends Entity>> list = new ArrayList<>();
	        LeftPaddleSpecification paddle = new LeftPaddleSpecification(world);
	        BallSpecification ball = new BallSpecification(0.1, -0.002, -0.003, world, false);
	        list.add(paddle);
	        list.add(ball);
	        return list;
	    }
	}
    
    
    
    
    private class HardScenario extends Scenario {
	    public HardScenario() {
	        name = "HARD";
	    }
	
	    @Override
	    public List<EntitySpecification<? extends Entity>> getScenarioEntities() {
	        List<EntitySpecification<? extends Entity>> list = new ArrayList<>();
	        LeftPaddleSpecification paddle = new LeftPaddleSpecification(world);
	        BallSpecification ball = new BallSpecification(0.1, -0.004, 0.005, world, false);
	        list.add(paddle);
	        list.add(ball);
	        return list;
	    }
    }
	
	    private class DuellScenario extends Scenario {
		    public DuellScenario() {
		        name = "DUELL";
		    }
		
		    @Override
		    public List<EntitySpecification<? extends Entity>> getScenarioEntities() {
		        List<EntitySpecification<? extends Entity>> list = new ArrayList<>();
		        LeftPaddleSpecification paddle = new LeftPaddleSpecification(world);
		        RightPaddleSpecification paddle2 = new RightPaddleSpecification(world);
		        BallSpecification ball = new BallSpecification(0, 0.1, -0.002, -0.003, world);
		        list.add(paddle);
		        list.add(paddle2);
		        list.add(ball);
		        return list;
		    }
    
    }
    
    
   
    public List<Scenario> getScenarios() {
        List<Scenario> scenarios = new ArrayList<>();
        scenarios.add(new StraightScenario());
        scenarios.add(new EasyScenario());
        scenarios.add(new MiddleScenario());
        scenarios.add(new HardScenario());
        scenarios.add(new DuellScenario());
        return scenarios;
    }

}
>>>>>>> 308c2f6b59a427da313ecd842c5fe5e5918e5f8c
