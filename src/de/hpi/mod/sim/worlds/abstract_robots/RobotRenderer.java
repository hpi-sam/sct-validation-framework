package de.hpi.mod.sim.worlds.abstract_robots;

import javax.imageio.ImageIO;

import de.hpi.mod.sim.worlds.abstract_grid.SimulationBlockView;

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
            robotIcon = ImageIO.read(new File(RobotConfiguration.getStringPathToRobotIcon()));
            leftClickedRobotIcon = ImageIO.read(new File(RobotConfiguration.getStringPathToLeftClickedRobotIcon()));
            rightClickedRobotIcon = ImageIO.read(new File(RobotConfiguration.getStringPathToRightClickedRobotIcon()));
            batteryIcon = ImageIO.read(new File(RobotConfiguration.getStringPathToEmptyBattery()));
            packageIcon = ImageIO.read(new File(RobotConfiguration.getStringPathToPackage()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void render(Graphics graphic, float size) {
        // Draw Robots
        for (Robot robot : robots.getRobots()) {
            Point2D drawPosition = simView.toDrawPosition(robot.x(), robot.y());
	
	            boolean leftClicked = robot.equals(simView.getHighlighted1());
	            boolean rightClicked = robot.equals(simView.getHighlighted2());
	
	            drawRobot(graphic, drawPosition, size, robot.getAngle(), leftClicked, rightClicked, robot.hasPackage(), robot.getBattery() < .1);
        }

        // Render additional Info like Targets
        for (Robot robot : robots.getRobots()) {
            if (robot.equals(simView.getHighlighted1()) || robot.equals(simView.getHighlighted2())) {
	                Point2D drawPos = simView.toDrawPosition(robot.x(), robot.y());
	                Point2D targetPos = simView.toDrawPosition(robot.getTarget());
	
	                drawLineToTarget(graphic, drawPos, targetPos, size);
            }
        }
    }

    private void drawRobot(Graphics graphic, Point2D drawPosition, float size, float angle, boolean leftClicked, boolean rightClicked, boolean hasPackage, boolean batteryEmpty) {
        int translateX = (int) drawPosition.getX();
        int translateY = (int) drawPosition.getY();

        BufferedImage image = robotIcon;
        if(leftClicked)
        	image = leftClickedRobotIcon;
        else if(rightClicked)
        	image = rightClickedRobotIcon;

        // Rotate
        AffineTransform tx = AffineTransform.getRotateInstance(Math.toRadians(angle),
                image.getWidth() / 2f, image.getHeight() / 2f);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

        graphic.drawImage(op.filter(image, null), translateX, translateY, (int) size, (int) size, null);

        if (hasPackage) 
        	graphic.drawImage(op.filter(packageIcon, null), translateX, translateY, (int) size, (int) size, null);
        
        if (batteryEmpty)
            graphic.drawImage(batteryIcon, (int) drawPosition.getX(), (int) drawPosition.getY(), (int) size, (int) size, null);
    }

    private void drawLineToTarget(Graphics graphic, Point2D drawPosition, Point2D targetPosition, float size) {
        Graphics2D Graphic2D = (Graphics2D) graphic;
        graphic.setColor(Color.RED);

        int offset = (int) size / 2;
        Graphic2D.drawLine(
                (int) drawPosition.getX() + offset,
                (int) drawPosition.getY() + offset,
                (int) targetPosition.getX() + offset,
                (int) targetPosition.getY() + offset);
    }
}
