package de.hpi.mod.sim.core.view.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * This class implements a wrapper for menu objects.
 * 
 * This gives us the ability to reliably size button, text fields, etc. and also have a background color.
 *
 */
public class MenuWrapper extends JPanel {
	
	private static final long serialVersionUID = 5623569438638761729L;

	public MenuWrapper(int width, int height, Color color, JComponent component) {
		super(new BorderLayout()); //use a BordeLayout to force the component to have the size of the wrapper
		
		setBackground(color);
		add(component);
		
		//set both sizes because some Layout Managers look at different size attributes
		setMinimumSize(new Dimension(width, height));
		setPreferredSize(new Dimension(width, height));
	}
}
