package de.hpi.mod.sim.worlds.infinitewarehouse;

import javax.imageio.ImageIO;

import de.hpi.mod.sim.worlds.abstract_grid.SimulationBlockView;
import de.hpi.mod.sim.worlds.infinitewarehouse.environment.RobotManager;
import de.hpi.mod.sim.worlds.infinitewarehouse.robot.DriveManager;
import de.hpi.mod.sim.worlds.infinitewarehouse.robot.Robot;

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

    private RobotManager robots;
    private SimulationBlockView simView;
    private BufferedImage robotIcon, leftClickedRobotIcon, rightClickedRobotIcon, batteryIcon, packageIcon;


    public RobotRenderer(
            RobotManager robots, SimulationBlockView simView) {
        this.robots = robots;
        this.simView = simView;

        loadImages();
    }

    private void loadImages() {
        try {
            robotIcon = ImageIO.read(new File(InfiniteWarehouseConfiguration.getStringPathToRobotIcon()));
            leftClickedRobotIcon = ImageIO.read(new File(InfiniteWarehouseConfiguration.getStringPathToLeftClickedRobotIcon()));
            rightClickedRobotIcon = ImageIO.read(new File(InfiniteWarehouseConfiguration.getStringPathToRightClickedRobotIcon()));
            batteryIcon = ImageIO.read(new File(InfiniteWarehouseConfiguration.getStringPathToEmptyBattery()));
            packageIcon = ImageIO.read(new File(InfiniteWarehouseConfiguration.getStringPathToPackage()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void render(Graphics graphic, float size) {
        // Draw Robots
        for (Robot robot : robots.getRobots()) {
	            DriveManager drive = robot.getDriveManager();
	            Point2D drawPosition = simView.toDrawPosition(drive.getX(), drive.getY());
	
	            boolean leftClicked = robot.equals(simView.getHighlighted1());
	            boolean rightClicked = robot.equals(simView.getHighlighted2());
	
	            drawRobot(graphic, drawPosition, size, drive.getAngle(), leftClicked, rightClicked, robot.hasPackage(), robot.getBattery() < .1);
        }

        // Render additional Info like Targets
        for (Robot r : robots.getRobots()) {
            if (r.equals(simView.getHighlighted1()) || r.equals(simView.getHighlighted2())) {
	                DriveManager drive = r.getDriveManager();
	                Point2D drawPos = simView.toDrawPosition(drive.getX(), drive.getY());
	                Point2D targetPos = simView.toDrawPosition(r.getTarget());
	
	                drawTarget(graphic, drawPos, targetPos, size);
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

    private void drawTarget(Graphics graphic, Point2D drawPosition, Point2D targetPosition, float size) {
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
