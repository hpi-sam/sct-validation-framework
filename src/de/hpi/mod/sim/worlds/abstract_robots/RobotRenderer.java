package de.hpi.mod.sim.worlds.abstract_robots;

import javax.imageio.ImageIO;

import de.hpi.mod.sim.worlds.abstract_grid.SimulationBlockView;
import de.hpi.mod.sim.worlds.simpletraffic.SimpleTrafficWorldConfiguration;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Renders all Robots.
 */
public class RobotRenderer {

    private SimulationBlockView simView;
    private BufferedImage robotIcon, leftClickedRobotIcon, rightClickedRobotIcon, batteryIcon, packageIcon;
    private RobotGridManager robots;

    public RobotRenderer(SimulationBlockView simView, RobotGridManager robots) {
        this.simView = simView;
        this.robots = robots;
        loadImages();
    }

    private void loadImages() {
        try {
        	 this.robotIcon = ImageIO.read(new File(RobotConfiguration.getStringPathToRobotIcon()));
            this.leftClickedRobotIcon = ImageIO.read(new File(RobotConfiguration.getStringPathToLeftClickedRobotIcon()));
            this.rightClickedRobotIcon = ImageIO.read(new File(RobotConfiguration.getStringPathToRightClickedRobotIcon()));
            this.batteryIcon = ImageIO.read(new File(RobotConfiguration.getStringPathToEmptyBattery()));
            this.packageIcon = ImageIO.read(new File(RobotConfiguration.getStringPathToPackage()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    

    public void render(Graphics graphic, float blockSize) {
        for (Robot robot : robots.getRobots()) {
        	drawRobotAndContext(graphic, blockSize, robot);
        }
    }
    
    protected void drawRobotAndContext(Graphics graphic, float blockSize, Robot robot) {
    	Point2D drawPosition = getSimView().toDrawPosition(robot.x(), robot.y());

    	// Draw actual Robots
        boolean leftClicked = robot.equals(getSimView().getHighlighted1());
        boolean rightClicked = robot.equals(getSimView().getHighlighted2());
        drawRobot(graphic, drawPosition, blockSize, robot.getAngle(), leftClicked, rightClicked, robot.hasPackage(), robot.getBattery() < .1);

        // Render additional Info like Targets
        if (leftClicked || rightClicked) {
        	Point2D targetPosition = getSimView().toDrawPosition(robot.getTarget());
        	drawLineFromRobotToTarget(graphic, drawPosition, targetPosition, blockSize);
        }
    }
    	
    private void drawRobot(Graphics graphic, Point2D drawPosition, float size, float angle, boolean leftClicked, boolean rightClicked, boolean hasPackage, boolean batteryEmpty) {
    	
        int translateX = (int) drawPosition.getX();
        int translateY = (int) drawPosition.getY();

        // Determine Icon (based on highlight status)
        BufferedImage image = robotIcon;
        if(leftClicked)
        	image = leftClickedRobotIcon;
        else if(rightClicked)
        	image = rightClickedRobotIcon;

        // Rotate
        AffineTransform tx = AffineTransform.getRotateInstance(Math.toRadians(angle),
                image.getWidth() / 2f, image.getHeight() / 2f);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

        // Draw Robot itself
        graphic.drawImage(op.filter(image, null), translateX, translateY, (int) size, (int) size, null);

        // Draw overlays over Robot (package and/or pattery)
        if (hasPackage) 
        	graphic.drawImage(op.filter(packageIcon, null), translateX, translateY, (int) size, (int) size, null);
        if (batteryEmpty)
            graphic.drawImage(batteryIcon, (int) drawPosition.getX(), (int) drawPosition.getY(), (int) size, (int) size, null);
        
    }

    private void drawLineFromRobotToTarget(Graphics graphic, Point2D robotPosition, Point2D targetPosition, float size) {
        Graphics2D Graphic2D = (Graphics2D) graphic;
        graphic.setColor(Color.RED);

        int offset = (int) size / 2;
        Graphic2D.drawLine(
                (int) robotPosition.getX() + offset,
                (int) robotPosition.getY() + offset,
                (int) targetPosition.getX() + offset,
                (int) targetPosition.getY() + offset);
    }

	public SimulationBlockView getSimView() {
		return simView;
	}
}
