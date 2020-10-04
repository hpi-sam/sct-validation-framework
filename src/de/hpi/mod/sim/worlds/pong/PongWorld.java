package de.hpi.mod.sim.worlds.pong;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


import de.hpi.mod.sim.core.World;
import de.hpi.mod.sim.core.scenario.Scenario;
import de.hpi.mod.sim.core.scenario.TestScenario;
import de.hpi.mod.sim.core.simulation.Detector;
import de.hpi.mod.sim.core.simulation.Entity;
import de.hpi.mod.sim.core.simulation.IHighlightable;
import de.hpi.mod.sim.worlds.pong.TestCaseGenerator;
public class PongWorld extends World {

	private int width, height;
	private Paddle paddle1;
	private Ball ball;
	 
	@Override
	public List<Detector> createDetectors() {
		Detector det = new Detector(this) {

		@Override
		public void update(List<? extends Entity> entities) {
			if (ball != null && ball.isOutOfBounds()) {
		 			report("The ball is out of bounds (the paddle didn't caught the ball).");
		 		}
		 	}

		 	@Override
		 	public void reset() {}
		};
		return Arrays.asList(det);
	}

	

	@Override
	protected void initialize() {
	}

	@Override
	public void updateEntities(float delta) {
		if (ball != null)
		 	ball.update(delta);		
	}

	@Override
	public void resetScenario() {
		
	}

	@Override
	public List<? extends Entity> getEntities() {
		List<Entity> list = new ArrayList<>(1);
		list.add(paddle1);
		list.add(ball);
		return list;
	}

	@Override
	public void refreshEntities() {
		double ballPosition = 0;
		if(ball != null) {
			ballPosition = ball.refresh();
			}
		if (paddle1 != null)
			paddle1.refresh(ballPosition);
	}

	@Override
	public List<Scenario> getScenarios() {
		return new ScenarioGenerator(this).getScenarios();
	}

	@Override
	public Map<String, List<TestScenario>> getTestGroups() {
		return TestCaseGenerator.getAllTestCases(this);
	}

	@Override
	public void render(Graphics graphics) {
		drawBackground(graphics);
		if (paddle1 != null)
			paddle1.render(graphics, width, height);
		if (ball != null)
			ball.render(graphics, width, height);
	}

	private void drawBackground(Graphics graphics) {
		int x = toPixel(-0.9, width);
		int y = toPixel(-0.9, height);
		graphics.setColor(Color.DARK_GRAY);		
		graphics.drawRect(x, y, (int) (0.9 * width), (int) (0.9* height) );
	}

	@Override
	public void refreshSimulationProperties(int currentHeight, int currentWidth) {
		this.width = (int) currentWidth;
		this.height = (int) currentHeight;
	}

	@Override
	public IHighlightable getHighlightAtPosition(int x, int y) {
		return null;
	}

	@Override
	public void close() {
		paddle1.close();
	}

	@Override
	public void clearEntities() {
		paddle1 = null;
		ball = null;
	}


	public void setPaddle1(Paddle paddle) {
		this.paddle1 = paddle;
	}
	
	public static int toPixel(double relative, int total) {
		return (int) ((relative + 1) * total / 2);
	}
	
	public void collision(){
		//ball collides against Paddle1
		
        if(ball.getLeftEnd() <= paddle1.getRightEnd()
        && ball.getLeftEnd() >= paddle1.getLeftEnd()
        && paddle1.getUpperEnd() > ball.getUpperEnd()
        && paddle1.getLowerEnd() < ball.getLowerEnd()){
        		
        		ball.switchXDirection();
        		ball.switchYDirection();
        		paddle1.reboundBall();
        }
	}	
	
	
	
	
	
	public Paddle getPaddle1 () {
		return paddle1;
	}



	public void setBall(Ball ball) {
		this.ball = ball;
		
	}
	
}
