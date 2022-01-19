package de.hpi.mod.sim.worlds.simpletrafficlights;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import de.hpi.mod.sim.worlds.abstract_grid.Orientation;
import de.hpi.mod.sim.worlds.abstract_grid.SimulationBlockView;
import de.hpi.mod.sim.worlds.abstract_robots.Robot;
import de.hpi.mod.sim.worlds.abstract_robots.RobotConfiguration;
import de.hpi.mod.sim.worlds.abstract_robots.RobotGridManager;
import de.hpi.mod.sim.worlds.simpletrafficlights.entities.TrafficLight;

public class TrafficLightRenderer {

    private SimulationBlockView simView;
    private StreetNetworkManager grid;

    public TrafficLightRenderer(SimulationBlockView sim, StreetNetworkManager grid) {
        this.simView = sim;
        this.grid = grid;
    }

    public void render(Graphics graphic, float blockSize) {
        // Draw Robots
        for (TrafficLight trafficLight : grid.getTraffigLights()) {
        	// South
        	Point2D southTrafficLightPosition = simView.toDrawPosition(trafficLight.getBottomLeftPosition().getX()+2, trafficLight.getBottomLeftPosition().getY()-1);
        	drawTrafficLight(graphic, southTrafficLightPosition, blockSize, Orientation.SOUTH, trafficLight.isGreenSouth());
        	
        	// West
        	Point2D westTrafficLightPosition = simView.toDrawPosition(trafficLight.getBottomLeftPosition().getX()-1, trafficLight.getBottomLeftPosition().getY()-1);
        	drawTrafficLight(graphic, westTrafficLightPosition, blockSize, Orientation.WEST, trafficLight.isGreenWest());
        	
        	// North
        	Point2D northTrafficLightPosition = simView.toDrawPosition(trafficLight.getBottomLeftPosition().getX()-1, trafficLight.getBottomLeftPosition().getY()+2);
        	drawTrafficLight(graphic, northTrafficLightPosition, blockSize, Orientation.NORTH, trafficLight.isGreenNorth());

        	// East
        	Point2D eastTrafficLightPosition = simView.toDrawPosition(trafficLight.getBottomLeftPosition().getX()+2, trafficLight.getBottomLeftPosition().getY()+2);
        	drawTrafficLight(graphic, eastTrafficLightPosition, blockSize, Orientation.EAST, trafficLight.isGreenEast());
        }
        
    }

    private void drawTrafficLight(Graphics graphic, Point2D drawPosition, float blockSize, Orientation orientation, boolean showGreen) {
    	
		// Todo: Different variants for 
    	double topLightX = drawPosition.getX();
    	double topLightY = drawPosition.getY();
    	double bottomLightX = drawPosition.getX();
    	double bottomLightY = drawPosition.getY();
    	
    	if(orientation == Orientation.SOUTH) {
        	topLightX += (0.35*blockSize);
        	topLightY += (0.65*blockSize);
        	bottomLightX += (0.35*blockSize);
        	bottomLightY += (0.35*blockSize);
    	}else if(orientation == Orientation.WEST) {
        	topLightX += (0.35*blockSize);
        	topLightY += (0.35*blockSize);
        	bottomLightX += (0.65*blockSize);
        	bottomLightY += (0.35*blockSize);
    	}else if(orientation == Orientation.NORTH) {
        	topLightX += (0.65*blockSize);
        	topLightY += (0.35*blockSize);
        	bottomLightX += (0.65*blockSize);
        	bottomLightY += (0.65*blockSize);
    	}else if(orientation == Orientation.EAST) {
        	topLightX += (0.65*blockSize);
        	topLightY += (0.65*blockSize);
        	bottomLightX += (0.35*blockSize);
        	bottomLightY += (0.65*blockSize);
    	}
    	


        int x = (int) drawPosition.getX();
        int y = (int) drawPosition.getY();

        graphic.setColor(Color.BLACK);
        graphic.fillRect((int) (topLightX - 0.15*blockSize), (int) (topLightY - 0.15*blockSize), (int) (0.3 * blockSize), (int) (0.3*blockSize));
        graphic.fillRect((int) (bottomLightX - 0.15*blockSize), (int) (bottomLightY - 0.15*blockSize), (int) (0.3 * blockSize), (int) (0.3*blockSize));
        
        if(showGreen) {
            graphic.setColor(Color.GREEN);
        	graphic.fillOval((int) (topLightX - 0.1*blockSize), (int) (topLightY - 0.1*blockSize), (int) (0.2 * blockSize), (int) (0.2*blockSize));
        }else{
            graphic.setColor(Color.RED);
        	graphic.fillOval((int) (bottomLightX - 0.1*blockSize), (int) (bottomLightY - 0.1*blockSize), (int) (0.2 * blockSize), (int) (0.2*blockSize));
        }
    	
    }

}