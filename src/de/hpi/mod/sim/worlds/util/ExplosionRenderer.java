package de.hpi.mod.sim.worlds.util;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.net.URL;
import javax.swing.ImageIcon;

public class ExplosionRenderer {
	private Image explosion;
	private Point2D location;
	private boolean shouldRender = false;
	private Rectangle explosionRectangle;

	public ExplosionRenderer() {
		loadImage();
	}

	private void loadImage() {
		//As we want to display an animated gif we can not use ImageIO. Instead we need to load the image using getResource
		URL url = ExplosionRenderer.class.getResource(UtilConfiguration.getURLToExplosion());
		explosion = new ImageIcon(url).getImage();
	}
	
	public void showExplosion(Point2D location) {
		this.location = location;
		shouldRender = true;
	}
	
	public void reset() {
		shouldRender = false;
		location = null;
		explosionRectangle = null;
	}
	
	public void render(Graphics graphic) {
		if (shouldRender) {
	        int x = (int) location.getX() - explosion.getWidth(null) / 2 + 15; //the explosion gif we use doesn't start its explosion in the exact center. Thus we add 15 to center it
	        int y = (int) location.getY() - explosion.getHeight(null) / 2;
	        
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
