package de.hpi.mod.sim.worlds.simpletraffic.entities.rendering;

import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import de.hpi.mod.sim.worlds.abstract_grid.Direction;
import de.hpi.mod.sim.worlds.abstract_grid.SimulationBlockView;
import de.hpi.mod.sim.worlds.abstract_robots.Robot;
import de.hpi.mod.sim.worlds.abstract_robots.RobotRenderer;
import de.hpi.mod.sim.worlds.simpletraffic.SimpleTrafficWorldConfiguration;
import de.hpi.mod.sim.worlds.simpletraffic.TrafficGridManager;

public class SimpleTrafficRobotRenderer extends RobotRenderer {

	private BufferedImage rightIndicatorImage, leftIndicatorImage;
	private TrafficGridManager manager;
	
	private long lastUpdateSystemTime;
	private int waitingTime;
	private boolean indicatorOn;

	public SimpleTrafficRobotRenderer(SimulationBlockView simView, TrafficGridManager manager) {
		super(simView, manager);
		this.manager = manager;
		this.loadImages();
	}

	private void loadImages() {
		try {
			leftIndicatorImage = ImageIO.read(new File(SimpleTrafficWorldConfiguration.getStringPathToLeftIndicatorImage()));
			rightIndicatorImage = ImageIO.read(new File(SimpleTrafficWorldConfiguration.getStringPathToRightIndicatorImage()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@Override
    public void render(Graphics graphic, float blockSize) {
		// Overwrite to ensure indicator time increase.
		
    	// IF indicators are enabled...
		if(SimpleTrafficWorldConfiguration.showIndicator())
			// ...update timer and indicator state
			updateIndicatorTimer();
		
		// Call normal rendering function that iterates over robots.
		super.render(graphic, blockSize);
    }
		
	@Override
	protected void drawRobotAndContext(Graphics graphic, float blockSize, Robot robot) {
		// Overwrite to ensure indicators are drawn.
        
		// Draw Robot and Line to Target (normal behaviour, via super's function)
		super.drawRobotAndContext(graphic, blockSize, robot);
		
		// IF indicators are enabled....
		if(SimpleTrafficWorldConfiguration.showIndicator()) {
						
			// ...and IF indicator should be on, ...
			if(this.indicatorOn && this.manager.isInFrontOfTrafficLight(robot.pos()) && this.manager.isInFrontOfTrafficLight(robot.oldPos())) {
				
				// ...get robot target directions...
				Point2D position = getSimView().toDrawPosition(robot.x(), robot.y());
				List<Direction> directions = manager.getTargetDirections(robot.pos(), robot.facing(), robot.getTarget());
				
				// ...and draw indicators above robot, IF Robot can NOT drive ahead.
				if(!directions.contains(Direction.AHEAD))
					this.drawRobotIndicator(graphic, position, blockSize, robot.getAngle(), directions.contains(Direction.LEFT), directions.contains(Direction.RIGHT));				
			}	
		}
		
    }
	
	private void updateIndicatorTimer() {
		
		// Calculate delta
	    float systemTimeDelta = System.currentTimeMillis() - this.lastUpdateSystemTime;
	    this.lastUpdateSystemTime = System.currentTimeMillis();
	    float simulationTimeDelta =  (float) Math.floor(systemTimeDelta * SimpleTrafficWorldConfiguration.getEntitySpeedFactor());

	    // Update Wait Time
	    this.waitingTime += simulationTimeDelta;
	    
	    // If Wait time 
	    if(this.waitingTime >= SimpleTrafficWorldConfiguration.getIndicatorFlashInterval()) {
	    	this.waitingTime = this.waitingTime % SimpleTrafficWorldConfiguration.getIndicatorFlashInterval(); // Reset Wait Time
	    	this.indicatorOn = !this.indicatorOn; // Invert
	    }
	        
	}
    
    


	private void drawRobotIndicator(Graphics graphic, Point2D drawPosition, float blockSize, float angle, boolean leftIndicator, boolean rightIndicator) {
    	
        int translateX = (int) drawPosition.getX();
        int translateY = (int) drawPosition.getY();

        // Determine Icon (based on highlight status)
        BufferedImage indicatorImage;
		if(leftIndicator)
        	indicatorImage = leftIndicatorImage;
        else if(rightIndicator)
        	indicatorImage = rightIndicatorImage;
        else
        	return;

        // Rotate
        AffineTransform tx = AffineTransform.getRotateInstance(Math.toRadians(angle),
        		indicatorImage.getWidth() / 2f, indicatorImage.getHeight() / 2f);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

        // Draw Robot itself
        graphic.drawImage(op.filter(indicatorImage, null), translateX, translateY, (int) blockSize, (int) blockSize, null);
        
    }
}
