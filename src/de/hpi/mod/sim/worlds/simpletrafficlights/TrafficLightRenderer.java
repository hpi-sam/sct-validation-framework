package de.hpi.mod.sim.worlds.simpletrafficlights;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import de.hpi.mod.sim.worlds.abstract_grid.ICellType;
import de.hpi.mod.sim.worlds.abstract_grid.Orientation;
import de.hpi.mod.sim.worlds.abstract_grid.Position;
import de.hpi.mod.sim.worlds.abstract_grid.SimulationBlockView;
import de.hpi.mod.sim.worlds.abstract_robots.RobotConfiguration;
import de.hpi.mod.sim.worlds.simpletrafficlights.entities.TrafficLight;

public class TrafficLightRenderer {

	private SimulationBlockView simView;
	private StreetNetworkManager grid;
	private BufferedImage leftClickedAreaImage;
	private BufferedImage rightClickedAreaImage;

	public TrafficLightRenderer(SimulationBlockView sim, StreetNetworkManager grid) {
		this.simView = sim;
		this.grid = grid;
		this.loadImages();
	}

	private void loadImages() {
        try {
            this.leftClickedAreaImage = ImageIO.read(new File(SimpleTrafficLightsConfiguration.getStringPathToLeftClickedAreaHighlight()));
        	this.rightClickedAreaImage = ImageIO.read(new File(SimpleTrafficLightsConfiguration.getStringPathToRightClickedAreaHighlight()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void render(Graphics graphic, float blockSize) {
		// Draw Traffic Lights
		for (TrafficLight trafficLight : grid.getTraffigLights()) {
			
			// South
			Point2D southTrafficLightPosition = simView.toDrawPosition(trafficLight.getBottomLeftPosition().getX() + 2,
					trafficLight.getBottomLeftPosition().getY() - 1);
			drawTrafficLight(graphic, southTrafficLightPosition, blockSize, Orientation.SOUTH,
					trafficLight.isGreenSouth());

			// West
			Point2D westTrafficLightPosition = simView.toDrawPosition(trafficLight.getBottomLeftPosition().getX() - 1,
					trafficLight.getBottomLeftPosition().getY() - 1);
			drawTrafficLight(graphic, westTrafficLightPosition, blockSize, Orientation.WEST,
					trafficLight.isGreenWest());

			// North
			Point2D northTrafficLightPosition = simView.toDrawPosition(trafficLight.getBottomLeftPosition().getX() - 1,
					trafficLight.getBottomLeftPosition().getY() + 2);
			drawTrafficLight(graphic, northTrafficLightPosition, blockSize, Orientation.NORTH,
					trafficLight.isGreenNorth());

			// East
			Point2D eastTrafficLightPosition = simView.toDrawPosition(trafficLight.getBottomLeftPosition().getX() + 2,
					trafficLight.getBottomLeftPosition().getY() + 2);
			drawTrafficLight(graphic, eastTrafficLightPosition, blockSize, Orientation.EAST,
					trafficLight.isGreenEast());
			
			// Highlight Crossroad Area
            if(trafficLight.equals(simView.getHighlighted1()))
            	drawHighlightedCrossroadField(graphic, trafficLight, blockSize, true);
            if(trafficLight.equals(simView.getHighlighted2()))
            	drawHighlightedCrossroadField(graphic, trafficLight, blockSize, false);
            
		}

	}
	
	private void drawHighlightedCrossroadField(Graphics graphic, TrafficLight trafficLight, float blockSize, boolean leftClicked) {
		for(Position position : trafficLight.getCrossroadPositions()) {
			Point2D drawPosition = simView.toDrawPosition(position);
	        int x = (int) drawPosition.getX();
	        int y = (int) drawPosition.getY();
	        
	        Image image = leftClicked ? leftClickedAreaImage : rightClickedAreaImage; 
            graphic.drawImage(image, x, y, (int) blockSize, (int) blockSize, null);
		}

	}
	
	private Color darken(Color color) {
		float darken = 0.95f;
		return new Color((int) (color.getRed() * darken), 
	    		(int) (color.getGreen() * darken), 
	    		(int) (color.getBlue() * darken));
	}

	private void drawTrafficLight(Graphics graphic, Point2D drawPosition, float blockSize, Orientation orientation,
			boolean showGreen) {

		// Todo: Different variants for
		double topLightX = drawPosition.getX();
		double topLightY = drawPosition.getY();
		double bottomLightX = drawPosition.getX();
		double bottomLightY = drawPosition.getY();

		if (orientation == Orientation.SOUTH) {
			topLightX += (0.35 * blockSize);
			topLightY += (0.65 * blockSize);
			bottomLightX += (0.35 * blockSize);
			bottomLightY += (0.35 * blockSize);
		} else if (orientation == Orientation.WEST) {
			topLightX += (0.35 * blockSize);
			topLightY += (0.35 * blockSize);
			bottomLightX += (0.65 * blockSize);
			bottomLightY += (0.35 * blockSize);
		} else if (orientation == Orientation.NORTH) {
			topLightX += (0.65 * blockSize);
			topLightY += (0.35 * blockSize);
			bottomLightX += (0.65 * blockSize);
			bottomLightY += (0.65 * blockSize);
		} else if (orientation == Orientation.EAST) {
			topLightX += (0.65 * blockSize);
			topLightY += (0.65 * blockSize);
			bottomLightX += (0.35 * blockSize);
			bottomLightY += (0.65 * blockSize);
		}

		// int x = (int) drawPosition.getX();
		// int y = (int) drawPosition.getY();

		graphic.setColor(Color.BLACK);
		graphic.fillRect((int) (topLightX - 0.15 * blockSize), (int) (topLightY - 0.15 * blockSize),
				(int) (0.3 * blockSize), (int) (0.3 * blockSize));
		graphic.fillRect((int) (bottomLightX - 0.15 * blockSize), (int) (bottomLightY - 0.15 * blockSize),
				(int) (0.3 * blockSize), (int) (0.3 * blockSize));

		if (showGreen) {
			graphic.setColor(Color.GREEN);
			graphic.fillOval((int) (topLightX - 0.1 * blockSize), (int) (topLightY - 0.1 * blockSize),
					(int) (0.2 * blockSize), (int) (0.2 * blockSize));
		} else {
			graphic.setColor(Color.RED);
			graphic.fillOval((int) (bottomLightX - 0.1 * blockSize), (int) (bottomLightY - 0.1 * blockSize),
					(int) (0.2 * blockSize), (int) (0.2 * blockSize));
		}

	}

}