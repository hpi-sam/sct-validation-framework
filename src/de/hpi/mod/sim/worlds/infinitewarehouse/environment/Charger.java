package de.hpi.mod.sim.worlds.infinitewarehouse.environment;

/**
 * A charging position in a station.
 * Saves if it is blocked or reserved by a robot.
 */
public class Charger {

    private int chargerID;

    /**
     * Whether there is a robot on the charger
     */
    private boolean blocked = false;
    private boolean robotPresent = false;
    /**
     * A robot which wants to drive to the charger. Is only valid if
     * {@link #blocked} is set to true.
     */
    private int reservedForRobot;


    public Charger(int chargerID) {
        this.chargerID = chargerID;
    }

    public int getChargerID() {
        return chargerID;
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
