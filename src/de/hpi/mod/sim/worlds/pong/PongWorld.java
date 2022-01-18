package de.hpi.mod.sim.worlds.pong;

import java.awt.Color;
import java.awt.Font;
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
public class PongWorld extends World {

	private int width, height;
	private LeftPaddle paddle1;
	private RightPaddle paddle2;
	private Ball ball;
	
	public PongWorld() {
		super();
		publicName = "Ping Pong Game World";
	}	

	@Override
	public List<Detector> createDetectors() {
		return Arrays.asList(new Detector(this) {
			@Override
			public void update(List<? extends Entity> entities) {
				if(!isEnabled())
					return;
				if (ball != null && paddle2 == null && ball.isOutOfBounds() && ball.getIsInTests()) {
			 		reportDetectedProblem("The ball is out of bounds (the paddle didn't caught the ball).");
			 		}
				if (paddle1 != null && paddle1.getHeight() + PongConfiguration.buffer > PongConfiguration.maxPos) {
					reportDetectedProblem("The paddle is above maxPos.");
				}
				if (paddle1 != null && paddle1.getHeight() - PongConfiguration.buffer < PongConfiguration.minPos) {
					reportDetectedProblem("The paddle is beneath minPos.");
			 	}
			}
		});
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
		List<Entity> list = new ArrayList<>(2);
		list.add(paddle1);
		list.add(ball);
		list.add(paddle2);
		return list;
	}

	@Override
	public void refreshEntities() {
		double ballPosition = 0;
		if(ball != null) {
			ballPosition = ball.refresh();
			}
		if (paddle1 != null) {
			paddle1.refresh(ballPosition);			
		}
		if (paddle2 != null) {
			paddle2.refresh(ballPosition);
		}
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
		
		if (paddle2 != null) {
			paddle2.render(graphics, width, height);
			drawCounter(graphics);
		}
		if (ball != null){
			ball.render(graphics, width, height);
	
			if (!ball.isInTests)
				drawCounterForPadlle1(graphics);
			}
	}
	
	private void drawCounterForPadlle1(Graphics graphics){
	    graphics.setFont(new Font("Candara", Font.BOLD, height/50));
	    graphics.setColor(Color.black);
		graphics.drawString("PLAYER1: " + paddle1.getScore(), width/20, height- height/40);
	}
	
	private void drawCounter(Graphics graphics){
		drawCounterForPadlle1(graphics);
		graphics.drawString("PLAYER2: " + paddle2.getScore(), width- width/5, height- height/40);
	}

	private void drawBackground(Graphics graphics) {
		int x = toPixel(PongConfiguration.lowerBoundary, width);
		int y = toPixel(PongConfiguration.lowerBoundary, height);
		graphics.setColor(Color.DARK_GRAY);		
		graphics.drawRect(x, y, (int) (PongConfiguration.upperBoundary * width), (int) (PongConfiguration.upperBoundary * height) );
		
	}

	@Override
	public void refreshSimulationProperties(int currentHeight, int currentWidth) {
		this.width = (int) currentWidth;
		this.height = (int) currentHeight;
	}

	@Override
	public IHighlightable getHighlightAtPosition(int x, int y) {

		if (getSimulationRunner().isRunning()){
				
			//click in Paddle1
			if (paddle1 != null) {
				if(x <= toPixel(paddle1.getRightEnd(), width)
						&& x >= toPixel(paddle1.getLeftEnd(), width)
						&& y > (height- (toPixel(paddle1.getUpperEnd(), height)))
						&& y < (height - (toPixel(paddle1.getLowerEnd(), height)))){
					return paddle1;
				}
			}
			
			//click in paddle2
			if (paddle2 != null) {
				if(x <= toPixel(paddle2.getRightEnd(), width)
						&& x >= toPixel(paddle2.getLeftEnd(), width)
						&& y > (height- (toPixel(paddle2.getUpperEnd(), height)))
						&& y < (height - (toPixel(paddle2.getLowerEnd(), height)))){
					return paddle2;
				}
			}
			 
			//click in ball
			if(ball != null) {
				if(x <= toPixel(ball.getRightEnd() + PongConfiguration.bufferForMouseClick, width)
						&& x >= toPixel(ball.getLeftEnd()- PongConfiguration.bufferForMouseClick, width)
						&& y > (height- (toPixel(ball.getUpperEnd()+ PongConfiguration.bufferForMouseClick, height)))
						&& y < (height - (toPixel(ball.getLowerEnd()- PongConfiguration.bufferForMouseClick, height)))){
					 return ball;
				}
			}
		}
	 
		return null;
	}

	@Override
	public void close() {
		paddle1.stop();
		paddle2.stop();
	}

	@Override
	public void clearEntities() {
		paddle1 = null;
		paddle2 = null;
		ball = null;
	}


	public void setPaddleLeft(LeftPaddle paddle) {
		this.paddle1 = paddle;
	}
	
	public static int toPixel(double relative, int total) {
		return (int) ((relative + 1) * total / 2);
	}
	
	public void collision(){
		
		if (paddle2 != null) {
			this.collisionWith2Paddles();
		}
		
		else {
			//ball collides against Paddle1
	        if(ball.getLeftEnd() <= paddle1.getRightEnd()
	        && ball.getLeftEnd() >= paddle1.getLeftEnd()
	        && paddle1.getUpperEnd() > ball.getUpperEnd()
	        && paddle1.getLowerEnd() < ball.getLowerEnd()){
	        		
	        		ball.switchXDirection();
	        		paddle1.increaseScore();
	        		paddle1.reboundBall();
	        }
	      //bounce the ball when it hits the edge of the screen
	    	if (ball.getXPos() > 1) {
	    		ball.switchXDirection();  	
	    	}
	
	    	if (ball.getLeftEnd()< -1 ) {
	        	ball.setXPos(1);
	        	paddle1.resetScore();
	        	ball.setOutOfBounds();	
	    	}
		}
		
        
	}	

	
	public void collisionWith2Paddles() {
		
		//ball collides against Paddle1
        if(ball.getLeftEnd() <= paddle1.getRightEnd()
        && ball.getLeftEnd() >= paddle1.getLeftEnd()
        && paddle1.getUpperEnd() > ball.getUpperEnd()
        && paddle1.getLowerEnd() < ball.getLowerEnd()){
        		
        		ball.switchXDirection();
        		paddle1.reboundBall();
        }
		
		//ball collides against paddle2(right paddle)
		if(paddle2 != null) {
			
			if(ball.getRightEnd() >= paddle2.getLeftEnd()
					&& ball.getRightEnd() <= paddle2.getRightEnd()
					&& paddle2.getUpperEnd() > ball.getUpperEnd()
					&& paddle2.getLowerEnd() < ball.getLowerEnd()) {
				
				ball.increaseXDirection();
				ball.increaseYDirection();
				ball.switchXDirection();
			}
		}
		
	    //increases the counter and starts with the ball in the middle with start speed 
	    	if (ball.getXPos() > 1) {
	    		paddle1.increaseScore(); 
	    		ball.setXPos(0);
	    		ball.setNewStartSpeed();
	    	}

	    	if (ball.getLeftEnd()< -1 ) {
	    		paddle2.increaseScore(); 
	        	ball.setXPos(0);
	        	ball.setNewStartSpeed();	
	    	}
	}

	public void setBall(Ball ball) {
		this.ball = ball;
		
	}



	public void setPaddleRight(RightPaddle paddle) {
		this.paddle2 = paddle;
		
	}
	
}