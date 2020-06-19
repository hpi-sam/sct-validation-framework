package de.hpi.mod.sim.core.view.sim;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.net.URL;

import javax.swing.ImageIcon;

import de.hpi.mod.sim.core.simulation.SimulatorConfig;
import de.hpi.mod.sim.setting.robot.DriveManager;
import de.hpi.mod.sim.setting.robot.Robot;

public class ExplosionRenderer {
	private SimulationWorld world;
	private Image explosion;
	private Robot robot;
	private boolean shouldRender = false;
	private Rectangle explosionRectangle;

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
		explosionRectangle = null;
	}
	
	public void render(Graphics graphic) {
		if (shouldRender) {
			DriveManager drive = robot.getDriveManager();
	        Point2D drawPos = world.toDrawPosition(drive.getX(), drive.getY());
	        int x = (int) drawPos.getX() - explosion.getWidth(null) / 2 + 15; //the explosion gif we use doesn't start its explosion in the exact center. Thus we add 15 to center it
	        int y = (int) drawPos.getY() - explosion.getHeight(null) / 2;
	        
	        if(explosionRectangle == null)
	        	explosionRectangle = new Rectangle(x, y, explosion.getWidth(null), explosion.getHeight(null));
	        
	        graphic.drawImage(explosion, x, y, null);
		}
	}

	public void mousePressed(MouseEvent e) {
		if (shouldRender && explosionRectangle.contains(e.getX(), e.getY()))
			reset();
	}
}
