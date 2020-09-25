package de.hpi.mod.sim.worlds.pong;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;


import de.hpi.mod.sim.core.World;
import de.hpi.mod.sim.core.scenario.Scenario;
import de.hpi.mod.sim.core.scenario.TestScenario;
import de.hpi.mod.sim.core.simulation.Detector;
import de.hpi.mod.sim.core.simulation.Entity;
import de.hpi.mod.sim.core.simulation.IHighlightable;
public class PongWorld extends World {

	private int width, height;
	private Paddle paddle1;
	private Ball ball;
	 
	@Override
	public List<Detector> createDetectors() {
		// TODO
		// Detector det = new Detector(this) {

		// 	@Override
		// 	public void update(List<? extends Entity> entities) {
		// 		if (bulb != null && bulb.isOn() && bulb.getTimesToBlink() == 0) {
		// 			report("The lamp was on but no flashing was requested (either no start signal or just start(0)).");
		// 		}
		// 	}

		// 	@Override
		// 	public void reset() {}
		// };
		// return Arrays.asList(det);
		return new ArrayList<>();
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
		return new Hashtable<>(); //TODO
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
		graphics.setColor(Color.DARK_GRAY);
		int x = toPixel(-0.9, width);
		int y = toPixel(-0.9, height);
		graphics.drawRect(x, y, (int) width, (int) height );
	}

	@Override
	public void refreshSimulationProperties(int currentHeight, int currentWidth) {
		this.width = (int) (currentWidth * 0.9);
		this.height = (int) (currentHeight * 0.9);
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
		//hits against Paddle1
        if(ball.getXPos() < -0.999) {
        	if((paddle1.getYPos() + paddle1.getHeight()/2 < ball.getYPos() + ball.getDiameter()/2 )
        	&& (paddle1.getYPos() - paddle1.getHeight()/2 > ball.getYPos() - ball.getDiameter()/2 )) {
        		ball.setXDirection();
        		ball.setYDirection();
        	}
        }
       
	}	
	
	
	public Paddle getPaddle1 () {
		return paddle1;
	}



	public void setBall(Ball ball) {
		this.ball = ball;
		
	}
	
}
