package de.hpi.mod.sim.env.view;

import de.hpi.mod.sim.env.model.Position;
import de.hpi.mod.sim.env.robot.Robot;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RobotInfoView extends JPanel {

    private List<LabelRefresher> refresher = new ArrayList<>();
    private Robot robot = null;


    public RobotInfoView() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        setPreferredSize(new Dimension(200, -1));

        addInfo("ID", () -> Integer.toString(robot.getID()));
        addInfo("Battery", () -> Integer.toString((int) robot.getBattery()));
        addInfo("Pos", () -> stringify(robot.pos()));
        addInfo("Target", () -> stringify(robot.getTarget()));
        addInfo("Facing", () -> robot.posOrientation().toString());
        addInfo("Target Direction", () -> robot.targetDirection().toString());
    }

    private void addInfo(String template, StringInformation refresh) {
        JLabel label = new JLabel();
        label.setFont(label.getFont().deriveFont(Font.PLAIN));
        var lref = new LabelRefresher(label, template, refresh);
        refresher.add(lref);
        lref.refresh();
        add(label);
    }

    public void setRobot(Robot robot) {
        this.robot = robot;
        refresh();
    }

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
        private StringInformation runnable;

        public LabelRefresher(JLabel label, String template, StringInformation runnable) {
            this.label = label;
            this.template = template;
            this.runnable = runnable;
        }

        public void refresh() {
            if (robot == null)
                label.setText(template + ": -");
            else
                label.setText(template + ": " + runnable.run());
        }
    }

    private interface StringInformation {
        String run();
    }
}
