package de.hpi.mod.sim.core.view.sim;

import javax.imageio.ImageIO;

import de.hpi.mod.sim.core.simulation.Simulation;
import de.hpi.mod.sim.core.simulation.SimulatorConfig;
import de.hpi.mod.sim.setting.robot.DriveManager;
import de.hpi.mod.sim.setting.robot.Robot;

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

    private Simulation simulation;
    private SimulationWorld simulationWorld;
    private BufferedImage robotIcon, leftClickedRobotIcon, rightClickedRobotIcon, batteryIcon, packageIcon;


    public RobotRenderer(Simulation simulation, SimulationWorld simulationWorld) {
        this.simulation = simulation;
        this.simulationWorld = simulationWorld;

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
        for (Robot robot : simulation.getRobots()) {
	            DriveManager drive = robot.getDriveManager();
	            Point2D drawPosition = simulationWorld.toDrawPosition(drive.getX(), drive.getY());
	
	            boolean leftClicked = robot.equals(simulationWorld.getHighlightedEntity1());
	            boolean rightClicked = robot.equals(simulationWorld.getHighlightedEntity2());
	
	            drawRobot(graphic, drawPosition, drive.getAngle(), leftClicked, rightClicked, robot.hasPackage(), robot.getBattery() < .1);
        }

        // Render additional Info like Targets
        for (Robot r : simulation.getRobots()) {
            if (r.equals(simulationWorld.getHighlightedEntity1()) || r.equals(simulationWorld.getHighlightedEntity2())) {
	                DriveManager drive = r.getDriveManager();
	                Point2D drawPos = simulationWorld.toDrawPosition(drive.getX(), drive.getY());
	                Point2D targetPos = simulationWorld.toDrawPosition(r.getTarget());
	
	                drawTarget(graphic, drawPos, targetPos);
            }
        }
    }

    private void drawRobot(Graphics graphic, Point2D drawPosition, float angle, boolean leftClicked, boolean rightClicked, boolean hasPackage, boolean batteryEmpty) {
        float blockSize = simulationWorld.getBlockSize();
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

        int offset = (int) simulationWorld.getBlockSize() / 2;
        Graphic2D.drawLine(
                (int) drawPosition.getX() + offset,
                (int) drawPosition.getY() + offset,
                (int) targetPosition.getX() + offset,
                (int) targetPosition.getY() + offset);
    }
}
