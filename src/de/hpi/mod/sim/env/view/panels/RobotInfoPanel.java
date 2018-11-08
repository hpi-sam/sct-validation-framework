package de.hpi.mod.sim.env.view.panels;

import de.hpi.mod.sim.env.model.Position;
import de.hpi.mod.sim.env.robot.Robot;
import de.hpi.mod.sim.env.view.model.IHighlightedRobotListener;
import de.hpi.mod.sim.env.view.sim.SimulationWorld;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RobotInfoPanel extends JPanel implements IHighlightedRobotListener {

    private SimulationWorld world;
    private List<LabelRefresher> refresher = new ArrayList<>();


    public RobotInfoPanel(SimulationWorld world) {
        this.world = world;
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        setPreferredSize(new Dimension(200, -1));

        addInfo("ID", r -> Integer.toString(r.getID()));
        addInfo("Battery", r -> Integer.toString((int) r.getBattery()));
        addInfo("Pos", r -> stringify(r.pos()));
        addInfo("Target", r -> stringify(r.getTarget()));
        addInfo("Facing", r -> r.posOrientation().toString());
        addInfo("Target Direction", r -> r.targetDirection().toString());
    }

    private void addInfo(String template, RobotInformation refresh) {
        JLabel label = new JLabel();
        label.setFont(label.getFont().deriveFont(Font.PLAIN));
        var lref = new LabelRefresher(label, template, refresh);
        refresher.add(lref);
        lref.refresh();
        add(label);
    }

    @Override
    public void refresh() {
        for (LabelRefresher ref : refresher)
            ref.refresh();
        repaint();
    }

    private String stringify(Position pos) {
        return String.format("(%s, %s)", pos.getX(), pos.getY());
    }

    private class LabelRefresher {
        private JLabel label;
        private String template;
        private RobotInformation runnable;

        public LabelRefresher(JLabel label, String template, RobotInformation runnable) {
            this.label = label;
            this.template = template;
            this.runnable = runnable;
        }

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
