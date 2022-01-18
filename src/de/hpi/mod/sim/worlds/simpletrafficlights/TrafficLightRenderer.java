package de.hpi.mod.sim.worlds.simpletrafficlights;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import de.hpi.mod.sim.worlds.abstract_grid.SimulationBlockView;
import de.hpi.mod.sim.worlds.abstract_robots.Robot;
import de.hpi.mod.sim.worlds.abstract_robots.RobotConfiguration;
import de.hpi.mod.sim.worlds.abstract_robots.RobotGridManager;
import de.hpi.mod.sim.worlds.simpletrafficlights.entities.TrafficLight;

public class TrafficLightRenderer {

    private SimulationBlockView simView;
    private BufferedImage trafficLightGreenIcon, trafficLightRedIcon;
    private StreetNetworkManager grid;

    public TrafficLightRenderer(SimulationBlockView sim, StreetNetworkManager grid) {
        this.simView = sim;
        this.grid = grid;
        loadImages();
    }

    private void loadImages() {
        try {
        	trafficLightGreenIcon = ImageIO.read(new File(SimpleTrafficLightsConfiguration.getStringPathToGreenTrafficLightIcon()));
        	trafficLightRedIcon = ImageIO.read(new File(SimpleTrafficLightsConfiguration.getStringPathToRedTrafficLightIcon()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void render(Graphics graphic, float blockSize) {
        // Draw Robots
        for (TrafficLight trafficLight : grid.getTraffigLights()) {
        	// South
        	Point2D southTrafficLightPosition = simView.toDrawPosition(trafficLight.getBottomLeftPosition().getX()+2, trafficLight.getBottomLeftPosition().getY()-1);
        	drawTrafficLight(graphic, southTrafficLightPosition, blockSize, 0, trafficLight.isGreenSouth());
        	
        	// West
        	Point2D westTrafficLightPosition = simView.toDrawPosition(trafficLight.getBottomLeftPosition().getX()-1, trafficLight.getBottomLeftPosition().getY()-1);
        	drawTrafficLight(graphic, westTrafficLightPosition, blockSize, 90, trafficLight.isGreenWest());
        	
        	// North
        	Point2D northTrafficLightPosition = simView.toDrawPosition(trafficLight.getBottomLeftPosition().getX()-1, trafficLight.getBottomLeftPosition().getY()+2);
        	drawTrafficLight(graphic, northTrafficLightPosition, blockSize, 180, trafficLight.isGreenNorth());

        	// East
        	Point2D eastTrafficLightPosition = simView.toDrawPosition(trafficLight.getBottomLeftPosition().getX()+2, trafficLight.getBottomLeftPosition().getY()+2);
        	drawTrafficLight(graphic, eastTrafficLightPosition, blockSize, 270, trafficLight.isGreenEast());
        }
        
    }

    private void drawTrafficLight(Graphics graphic, Point2D drawPosition, float blockSize, float angle, boolean showGreen) {
        int translateX = (int) drawPosition.getX();
        int translateY = (int) drawPosition.getY();

        BufferedImage image = trafficLightRedIcon;
        if(showGreen)
        	image = trafficLightGreenIcon;

        // Rotate
        AffineTransform tx = AffineTransform.getRotateInstance(Math.toRadians(angle),
                image.getWidth() / 2f, image.getHeight() / 2f);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

        graphic.drawImage(op.filter(image, null), translateX, translateY, (int) blockSize, (int) blockSize, null);

    }

}