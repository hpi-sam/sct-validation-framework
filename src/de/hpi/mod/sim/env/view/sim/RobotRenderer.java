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
    private BufferedImage robotIcon, leftClickedRobotIcon, rightClickedRobotIcon, batteryIcon;


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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void render(Graphics graphic) {
        // Draw Robots
        for (Robot robot : world.getRobots()) {
            DriveManager drive = robot.getDriveManager();
            Point2D drawPos = world.toDrawPosition(drive.getX(), drive.getY());

            boolean leftClicked = robot.equals(world.getHighlightedRobot());
            boolean rightClicked = robot.equals(world.getHighlightedRobot2());

            drawRobot(graphic, drawPos, drive.getAngle(), leftClicked, rightClicked, robot.isHasPackage(), robot.getBattery() < .1);
        }

        // Render additional Info like Targets
        for (Robot r : world.getRobots()) {
            if (r.equals(world.getHighlightedRobot()) || r.equals(world.getHighlightedRobot2())) {
                DriveManager drive = r.getDriveManager();
                Point2D drawPos = world.toDrawPosition(drive.getX(), drive.getY());
                Point2D targetPos = world.toDrawPosition(r.getTarget());

                drawTarget(graphic, drawPos, targetPos);
            }
        }
    }

    private void drawRobot(Graphics graphic, Point2D drawPos, float angle, boolean leftClicked, boolean rightClicked, boolean hasPackage, boolean batteryEmpty) {
        float blockSize = world.getBlockSize();
        int translateX = (int) drawPos.getX();
        int translateY = (int) drawPos.getY();

        BufferedImage image = robotIcon;
        if(leftClicked)
        	image = leftClickedRobotIcon;
        else if(rightClicked)
        	image = rightClickedRobotIcon;

        // Rotate
        AffineTransform tx = AffineTransform.getRotateInstance(Math.toRadians(angle),
                image.getWidth() / 2f, image.getHeight() / 2f);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

        if (hasPackage) {
            graphic.setColor(Color.green);
            graphic.fillArc((int) (drawPos.getX() - blockSize * .1), (int) (drawPos.getY() - blockSize * .1),
                    (int) (blockSize * 1.2), (int) (blockSize * 1.2), 0, 360);
        }

        graphic.drawImage(op.filter(image, null), translateX, translateY, (int) blockSize, (int) blockSize,null);

        if (batteryEmpty) {
            graphic.drawImage(batteryIcon, (int) drawPos.getX(), (int) drawPos.getY(), (int) blockSize, (int) blockSize, null);
        }
    }

    private void drawTarget(Graphics graphic, Point2D drawPos, Point2D targetPos) {
        Graphics2D Graphic2D = (Graphics2D) graphic;
        graphic.setColor(Color.RED);

        int offset = (int) world.getBlockSize() / 2;
        Graphic2D.drawLine(
                (int) drawPos.getX() + offset,
                (int) drawPos.getY() + offset,
                (int) targetPos.getX() + offset,
                (int) targetPos.getY() + offset);
    }
}
