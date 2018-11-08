package de.hpi.mod.sim.env.view;

import de.hpi.mod.sim.env.robot.DriveManager;
import de.hpi.mod.sim.env.robot.Robot;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

public class RobotRenderer {

    private SimulatorView parent;
    private BufferedImage robotIcon, robotHighlightIcon;


    public RobotRenderer(SimulatorView parent) {
        this.parent = parent;

        try {
            robotIcon = ImageIO.read(new File("res/robot.png"));
            robotHighlightIcon = ImageIO.read(new File("res/robot-highlight.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update(float delta) {
        Iterator<Robot> it = parent.getSim().getRobots().iterator();
        try {
            while (it.hasNext()) {
                Robot r = it.next();
                r.getDriveManager().update(delta);
            }
        } catch (ConcurrentModificationException e) {
            e.printStackTrace();
        }
    }

    public void render(Graphics g) {
        for (Robot r : parent.getSim().getRobots()) {
            DriveManager drive = r.getDriveManager();
            Point2D drawPos = parent.toDrawPosition(drive.getX(), drive.getY());

            boolean highlighted = r.equals(parent.highlightedRobot());

            drawRobot(g, drawPos, drive.getAngle(), highlighted);
        }
    }

    private void drawRobot(Graphics g, Point2D drawPos, float angle, boolean highlighted) {
        float blockSize = parent.getBlockSize();
        int translateX = (int) drawPos.getX();
        int translateY = (int) (parent.getHeight() - drawPos.getY() - blockSize / 2);

        var image = highlighted ? robotHighlightIcon : robotIcon;

        // Rotate
        AffineTransform tx = AffineTransform.getRotateInstance(Math.toRadians(angle),
                image.getWidth() / 2f, image.getHeight() / 2f);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

        g.drawImage(op.filter(image, null), translateX, translateY, (int) blockSize, (int) blockSize,null);
    }
}
