package de.hpi.mod.sim.worlds.flasher;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Bulb {
	
	private BufferedImage bulbOn, bulbOff;
	private boolean bulbIsLightning = false; 
	
	
	public Bulb() {
		loadImages();
	}
	
	public void bulbRender(Graphics graphics) {
		if (bulbIsLightning) {
			graphics.drawImage(bulbOn,0, 0, null);
		}else {
			graphics.drawImage(bulbOff,0, 0, null);
		}
		
	}
	
	
	 private void loadImages() {
	        try {
	            bulbOn = ImageIO.read(new File(FlasherConfiguration.getStringPathBulbOn()));
	            bulbOff = ImageIO.read(new File(FlasherConfiguration.getStringPathBulbOff()));
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
}
