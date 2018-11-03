package de.hpi.mod.sim.env;

class Battery {

    private int batteryID;
    private boolean blocked = false;
    private int reservedForRobot;

    Battery(int batteryID) {
        this.batteryID = batteryID;
    }

    int getBatteryID() {
        return batteryID;
    }

    boolean isBlocked() {
        return blocked;
    }

    void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    int getReservedForRobot() {
        return reservedForRobot;
    }

    void setReservedForRobot(int reservedForRobot) {
        blocked = true;
        this.reservedForRobot = reservedForRobot;
    }
}
