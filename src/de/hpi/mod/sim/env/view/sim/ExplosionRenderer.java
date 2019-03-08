package de.hpi.mod.sim.env.view.sim;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.Point2D;
import java.net.URL;

import javax.swing.ImageIcon;

import de.hpi.mod.sim.env.SimulatorConfig;
import de.hpi.mod.sim.env.robot.DriveManager;
import de.hpi.mod.sim.env.robot.Robot;

public class ExplosionRenderer {
	private SimulationWorld world;
	private Image explosion;
	private Robot robot;
	private boolean shouldRender = false;

	public ExplosionRenderer(SimulationWorld world) {
		this.world = world;
		loadImage();
	}

	private void loadImage() {
		//As we want to display an animated gif we can not use ImageIO. Instead we need to load the image using getResource
		URL url = ExplosionRenderer.class.getResource(SimulatorConfig.getURLToExplosion());
		explosion = new ImageIcon(url).getImage();
	}
	
	public void showExplosion(Robot robot) {
		this.robot = robot;
		shouldRender = true;
	}
	
	public void reset() {
		shouldRender = false;
		robot = null;
	}
	
	public void render(Graphics graphic) {
		if (shouldRender) {
			DriveManager drive = robot.getDriveManager();
	        Point2D drawPos = world.toDrawPosition(drive.getX(), drive.getY());
	        int x = (int) drawPos.getX() - explosion.getWidth(null) / 2 + 15; //the explosion gif we use doesn't start its explosion in the exact center. Thus we add 15
	        int y = (int) drawPos.getY() - explosion.getHeight(null) / 2;
	        
	        graphic.drawImage(explosion, x, y, null);
		}
	}
}
