package de.hpi.mod.sim.env.simulation.station;

/**
 * A charging position in a station.
 * Saves if it is blocked or reserved by a robot.
 */
public class Battery {

    private int batteryID;

    /**
     * Whether there is a robot on the battery
     */
    private boolean blocked = false;
    private boolean robotPresent = false;
    /**
     * A robot which wants to drive to the battery.
     * Is only valid if {@link #blocked} is set to true.
     */
    private int reservedForRobot;


    public Battery(int batteryID) {
        this.batteryID = batteryID;
    }

    public int getBatteryID() {
        return batteryID;
    }

    public boolean isFree() {
        return !blocked;
    }
    
    public boolean isBlocked() {
        return blocked;
    }
    
    public boolean robotPresent() {
    	return robotPresent;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public int getReservedForRobot() {
        return reservedForRobot;
    }

    /**
     * Sets the robot and sets {@link #blocked} to true.
     * @param reservedForRobot The ID of the robot
     */
    public void setReservedForRobot(int reservedForRobot) {
        setBlocked(true);
        this.reservedForRobot = reservedForRobot;
    }

	public void setRobotPresent() {
		robotPresent = true;
	}

	public void setRobotNotPresent() {
		robotPresent = false;
	}
}
