package de.hpi.mod.sim.env.model;

public interface IRobot {
    int getID();
    Position pos();
    void refresh();
    void stop();
    void close();
}
