package de.hpi.mod.sim.env.view.sim;

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

public class RobotRenderer {

    private SimulationWorld world;
    private BufferedImage robotIcon, robotHighlightIcon;


    public RobotRenderer(SimulationWorld world) {
        this.world = world;

        try {
            robotIcon = ImageIO.read(new File("res/robot.png"));
            robotHighlightIcon = ImageIO.read(new File("res/robot-highlight.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void render(Graphics g) {
        for (Robot r : world.getRobots()) {
            DriveManager drive = r.getDriveManager();
            Point2D drawPos = world.toDrawPosition(drive.getX(), drive.getY());

            boolean highlighted = r.equals(world.getHighlightedRobot());

            drawRobot(g, drawPos, drive.getAngle(), highlighted);
        }

        // Render additional Info like Targets
        for (Robot r : world.getRobots()) {
            if (r.equals(world.getHighlightedRobot())) {
                DriveManager drive = r.getDriveManager();
                Point2D drawPos = world.toDrawPosition(drive.getX(), drive.getY());
                Point2D targetPos = world.toDrawPosition(r.getTarget());

                drawTarget(g, drawPos, targetPos);
            }
        }
    }

    private void drawRobot(Graphics g, Point2D drawPos, float angle, boolean highlighted) {
        float blockSize = world.getBlockSize();
        int translateX = (int) drawPos.getX();
        int translateY = (int) drawPos.getY();

        var image = highlighted ? robotHighlightIcon : robotIcon;

        // Rotate
        AffineTransform tx = AffineTransform.getRotateInstance(Math.toRadians(angle),
                image.getWidth() / 2f, image.getHeight() / 2f);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

        g.drawImage(op.filter(image, null), translateX, translateY, (int) blockSize, (int) blockSize,null);
    }

    private void drawTarget(Graphics g, Point2D drawPos, Point2D targetPos) {
        Graphics2D g2d = (Graphics2D) g;
        g.setColor(Color.RED);

        int offset = (int) world.getBlockSize() / 2;
        g2d.drawLine(
                (int) drawPos.getX() + offset,
                (int) drawPos.getY() + offset,
                (int) targetPos.getX() + offset,
                (int) targetPos.getY() + offset);
    }
}
