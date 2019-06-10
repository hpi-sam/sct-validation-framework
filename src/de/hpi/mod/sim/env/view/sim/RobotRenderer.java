package de.hpi.mod.sim.env.view.sim;

import de.hpi.mod.sim.env.SimulatorConfig;
import de.hpi.mod.sim.env.robot.DriveManager;
import de.hpi.mod.sim.env.robot.Robot;
import de.hpi.mod.sim.env.view.sim.SimulationWorld;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Render all Robots.
 */
public class RobotRenderer {

    private SimulationWorld world;
    private BufferedImage robotIcon, leftClickedRobotIcon, rightClickedRobotIcon, batteryIcon, packageIcon;


    public RobotRenderer(SimulationWorld world) {
        this.world = world;

        loadImages();
    }

    private void loadImages() {
        try {
            robotIcon = ImageIO.read(new File(SimulatorConfig.getStringPathToRobotIcon()));
            leftClickedRobotIcon = ImageIO.read(new File(SimulatorConfig.getStringPathToLeftClickedRobotIcon()));
            rightClickedRobotIcon = ImageIO.read(new File(SimulatorConfig.getStringPathToRightClickedRobotIcon()));
            batteryIcon = ImageIO.read(new File(SimulatorConfig.getStringPathToEmptyBattery()));
            packageIcon = ImageIO.read(new File(SimulatorConfig.getStringPathToPackage()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void render(Graphics graphic) {
        // Draw Robots
        for (Robot robot : world.getRobots()) {
        		robot.simulationStarted();
	            DriveManager drive = robot.getDriveManager();
	            Point2D drawPosition = world.toDrawPosition(drive.getX(), drive.getY());
	
	            boolean leftClicked = robot.equals(world.getHighlightedRobot1());
	            boolean rightClicked = robot.equals(world.getHighlightedRobot2());
	
	            drawRobot(graphic, drawPosition, drive.getAngle(), leftClicked, rightClicked, robot.isHasPackage(), robot.getBattery() < .1);
        }

        // Render additional Info like Targets
        for (Robot r : world.getRobots()) {
            if (r.equals(world.getHighlightedRobot1()) || r.equals(world.getHighlightedRobot2())) {
	                DriveManager drive = r.getDriveManager();
	                Point2D drawPos = world.toDrawPosition(drive.getX(), drive.getY());
	                Point2D targetPos = world.toDrawPosition(r.getTarget());
	
	                drawTarget(graphic, drawPos, targetPos);
            }
            r.simulationCompleted();
        }
    }

    private void drawRobot(Graphics graphic, Point2D drawPosition, float angle, boolean leftClicked, boolean rightClicked, boolean hasPackage, boolean batteryEmpty) {
        float blockSize = world.getBlockSize();
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

        graphic.drawImage(op.filter(image, null), translateX, translateY, (int) blockSize, (int) blockSize, null);

        if (hasPackage) 
        	graphic.drawImage(op.filter(packageIcon, null), translateX, translateY, (int) blockSize, (int) blockSize, null);
        
        if (batteryEmpty)
            graphic.drawImage(batteryIcon, (int) drawPosition.getX(), (int) drawPosition.getY(), (int) blockSize, (int) blockSize, null);
    }

    private void drawTarget(Graphics graphic, Point2D drawPosition, Point2D targetPosition) {
        Graphics2D Graphic2D = (Graphics2D) graphic;
        graphic.setColor(Color.RED);

        int offset = (int) world.getBlockSize() / 2;
        Graphic2D.drawLine(
                (int) drawPosition.getX() + offset,
                (int) drawPosition.getY() + offset,
                (int) targetPosition.getX() + offset,
                (int) targetPosition.getY() + offset);
    }
}
