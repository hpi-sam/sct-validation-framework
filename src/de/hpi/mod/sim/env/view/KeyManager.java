package de.hpi.mod.sim.env.view;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class KeyManager {

    private Map<String, KeyStroke> keys = new HashMap<>();


    public KeyManager() {
        resetKeys();
    }

    public void resetKeys() {
        keys.clear();

        keys.put("Pause/Play", KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0));
        keys.put("Add Robot", KeyStroke.getKeyStroke(KeyEvent.VK_A, 0));
        keys.put("Zoom In", KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, InputEvent.CTRL_DOWN_MASK));
        keys.put("Zoom Out", KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, InputEvent.CTRL_DOWN_MASK));
        keys.put("Reset Zoom", KeyStroke.getKeyStroke(KeyEvent.VK_R, 0));
        keys.put("Move Left", KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0));
        keys.put("Move Right", KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0));
        keys.put("Move Up", KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0));
        keys.put("Move Down", KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0));
        keys.put("Reset Move", KeyStroke.getKeyStroke(KeyEvent.VK_R, 0));
        keys.put("Reset Simulation", KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK));
    }

    public void addKey(String name, KeyStroke key) {
        keys.put(name, key);
    }

    public void replaceKey(String name, KeyStroke key) {
        keys.replace(name, key);
    }

    public Map<String, KeyStroke> getKeys() {
        return keys;
    }

    public KeyStroke getKey(String name) {
        return keys.get(name);
    }
}
