package de.hpi.mod.sim.env.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class KeyDialog extends JDialog {

    private List<KeyPanel> keys = new ArrayList<>();

    public KeyDialog(KeyManager manager) {
        setLayout(new GridLayout(0, 1));

        manager.getKeys().forEach((name, key) -> {
            keys.add(new KeyPanel(name, key));
        });

        JPanel buttonsPanel = new JPanel();

        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        JButton resetButton = new JButton("Reset");

        saveButton.addActionListener(e -> {
            for (KeyPanel key : keys) {
                manager.replaceKey(key.getName(), key.getKey());
            }
            dispose();
        });
        cancelButton.addActionListener(e -> dispose());
        resetButton.addActionListener(e -> {
            manager.resetKeys();
            for (KeyPanel key : keys) {
                key.setKey(manager.getKey(key.getName()));
            }
        });

        buttonsPanel.add(saveButton);
        buttonsPanel.add(cancelButton);
        buttonsPanel.add(resetButton);

        for (KeyPanel kPanel : keys)
            add(kPanel);

        add(buttonsPanel);

        setTitle("Keymap");
        setModal(true);
        pack();
        setVisible(true);
    }

    private String keyToString(KeyStroke key) {
        String acceleratorText = "";
        if (key != null) {
            int modifiers = key.getModifiers();
            if (modifiers > 0) {
                acceleratorText = InputEvent.getModifiersExText(modifiers);
                acceleratorText += "+";
            }
            acceleratorText += KeyEvent.getKeyText(key.getKeyCode());
        }
        return acceleratorText;
    }

    private class KeyPanel extends JPanel {

        private KeyStroke original;
        private Color originalColor;
        private KeyStroke key;
        private String name;
        private JTextField keyLabel;
        private boolean waitingForKey = false;

        private KeyPanel(String name, KeyStroke key) {
            this.key = key;
            this.original = key;
            this.name = name;

            this.setLayout(new BorderLayout());

            JLabel title = new JLabel(name);
            title.setBorder(new EmptyBorder(0, 5, 0, 5));

            keyLabel = new JTextField(keyToString(key));
            keyLabel.setEditable(false);
            keyLabel.setForeground(Color.BLUE);
            originalColor = keyLabel.getBackground();

            JButton changeButton = new JButton("Change");
            changeButton.addActionListener(e -> toggleChange());
            changeButton.addKeyListener(new KeyListener() {

                @Override
                public void keyTyped(KeyEvent e) { }

                @Override
                public void keyPressed(KeyEvent e) {}

                @Override
                public void keyReleased(KeyEvent e) {
                    if (waitingForKey) {
                        setKey(KeyStroke.getKeyStroke(e.getKeyCode(), e.getModifiersEx()));
                        waitingForKey = false;
                    }
                }
            });
            changeButton.addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {}

                @Override
                public void focusLost(FocusEvent e) {
                    if (waitingForKey) loseFocus();
                }
            });

            add(title, BorderLayout.WEST);
            add(keyLabel, BorderLayout.CENTER);
            add(changeButton, BorderLayout.EAST);
        }

        private void toggleChange() {
            waitingForKey = !waitingForKey;
            if (waitingForKey)
                keyLabel.setText("?");
            else {
                keyLabel.setText(keyToString(key));
            }
        }

        private void loseFocus() {
            waitingForKey = false;
            keyLabel.setText(keyToString(key));
        }

        public void setKey(KeyStroke key) {
            this.key = key;
            keyLabel.setText(keyToString(key));
            if (!key.equals(original))
                keyLabel.setBackground(Color.YELLOW);
            else
                keyLabel.setBackground(originalColor);
        }

        public String getName() {
            return name;
        }

        private KeyStroke getKey() {
            return key;
        }
    }
}
