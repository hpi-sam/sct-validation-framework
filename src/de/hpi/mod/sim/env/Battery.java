package de.hpi.mod.sim.env;

/**
 * A charging position in a station.
 * Saves if it is blocked or reserved by a robot.
 */
class Battery {

    private int batteryID;

    /**
     * Whether there is a robot on the battery
     */
    private boolean blocked = false;

    /**
     * A robot which wants to drive to the battery.
     * Is only valid if {@link #blocked} is set to true.
     */
    private int reservedForRobot;


    Battery(int batteryID) {
        this.batteryID = batteryID;
    }

    int getBatteryID() {
        return batteryID;
    }

    boolean isFree() {
        return !blocked;
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

    /**
     * Sets the robot and sets {@link #blocked} to true.
     * @param reservedForRobot The ID of the robot
     */
    void setReservedForRobot(int reservedForRobot) {
        setBlocked(true);
        this.reservedForRobot = reservedForRobot;
    }
}
