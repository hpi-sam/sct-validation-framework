package de.hpi.mod.sim.env.view.panels;

import de.hpi.mod.sim.env.model.Position;
import de.hpi.mod.sim.env.robot.Robot;
import de.hpi.mod.sim.env.view.model.IHighlightedRobotListener;
import de.hpi.mod.sim.env.view.sim.SimulationWorld;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Shows informations about the currently highlighted robot
 */
public class RobotInfoPanel extends JPanel implements IHighlightedRobotListener {

    private SimulationWorld world;

    /**
     * List of refreshable information
     */
    private List<LabelRefresher> refresher = new ArrayList<>();


    public RobotInfoPanel(SimulationWorld world) {
        this.world = world;
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        setPreferredSize(new Dimension(200, -1));

        // Add information
        addInfo("ID", r -> Integer.toString(r.getID()));
        addInfo("Battery", r -> Integer.toString((int) r.getBattery()));
        addInfo("Pos", r -> stringify(r.pos()));
        addInfo("Target", r -> stringify(r.getTarget()));
        addInfo("Facing", r -> r.posOrientation().toString());
        addInfo("Target Direction", r ->
                r.isTargetReached() ? "-" : r.targetDirection().toString());
    }

    /**
     * Adds the information to the panel
     * @param template The name of the value
     * @param refresh The getter for the value
     */
    private void addInfo(String template, RobotInformation refresh) {
        JLabel label = new JLabel();
        label.setFont(label.getFont().deriveFont(Font.PLAIN));

        var lref = new LabelRefresher(label, template, refresh);
        refresher.add(lref);
        lref.refresh();

        add(label);
    }

    /**
     * Gets called if a value got changed and labels need to be updated
     */
    @Override
    public void onHighlightedRobotChange() {
        for (LabelRefresher ref : refresher)
            ref.refresh();
        repaint();
    }

    /**
     * converts a Position to a String
     * @param pos the position
     * @return a String representation
     */
    private String stringify(Position pos) {
        return String.format("(%s, %s)", pos.getX(), pos.getY());
    }

    /**
     * Stores the information needed to onHighlightedRobotChange a label
     */
    private class LabelRefresher {

        /**
         * The Label that displays the information
         */
        private JLabel label;

        /**
         * The name of the information
         */
        private String template;

        /**
         * Getter for the information
         */
        private RobotInformation runnable;


        public LabelRefresher(JLabel label, String template, RobotInformation runnable) {
            this.label = label;
            this.template = template;
            this.runnable = runnable;
        }

        /**
         * Asks the getter for the value of the highlighted Robot and renders it to the label
         */
        public void refresh() {
            if (world.getHighlightedRobot() == null)
                label.setText(template + ": -");
            else
                label.setText(template + ": " + runnable.run(world.getHighlightedRobot()));
        }
    }

    private interface RobotInformation {
        String run(Robot r);
    }
}
