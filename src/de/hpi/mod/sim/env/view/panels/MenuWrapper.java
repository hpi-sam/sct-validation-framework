package de.hpi.mod.sim.env.view.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JPanel;

public class MenuWrapper extends JPanel {
	public MenuWrapper(int width, int height, Color color, JComponent component) {
		super(new BorderLayout());
		
		setBackground(color);
		add(component);
		setMinimumSize(new Dimension(width, height));
		setPreferredSize(new Dimension(width, height));
	}
}
