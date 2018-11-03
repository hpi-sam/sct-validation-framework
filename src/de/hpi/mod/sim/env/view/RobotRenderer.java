package de.hpi.mod.sim.env.view;

import de.hpi.mod.sim.env.ServerGridManagement;
import de.hpi.mod.sim.env.robot.Robot;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

public class RobotRenderer {

    private SimulatorView parent;

    private BufferedImage robotIcon;


    public RobotRenderer(SimulatorView parent) {
        this.parent = parent;

        try {
            robotIcon = ImageIO.read(new File("res/robot.png"));
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
        float blockSize = parent.getBlockSize();
        float offsetX = parent.getOffsetX();
        float offsetY = parent.getOffsetY() - (ServerGridManagement.QUEUE_SIZE + 1) * blockSize;

        for (Robot r : parent.getSim().getRobots()) {
            var drive = r.getDriveManager();
            float realX = drive.getX() * blockSize - offsetX;
            float realY = (drive.getY()) * blockSize - offsetY;
            drawRobot(g, realX, realY, drive.getAngle());
        }
    }

    private void drawRobot(Graphics g, float realX, float realY, float angle) {
        float blockSize = parent.getBlockSize();
        int translateX = (int) (realX);
        int translateY = (int) (parent.getHeight() - realY - blockSize / 2);

        AffineTransform tx = AffineTransform.getRotateInstance(Math.toRadians(angle),
                robotIcon.getWidth() / 2f, robotIcon.getHeight() / 2f);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

        g.drawImage(op.filter(robotIcon, null), translateX, translateY, (int) blockSize, (int) blockSize,null);
    }
}
