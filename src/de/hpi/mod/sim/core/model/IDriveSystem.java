package de.hpi.mod.sim.core.model;

/**
 * Represents the Default interface in the Statechart.
 * It is used for communication between the Environment ({@link Robot})
 * and the Statechard-Wrapper ({@link DriveSystemWrapper})
 */
public interface IDriveSystem {

    void dataRefresh();
    void newTarget();
    void newUnloadingTarget();
    void newChargingTarget();
    void actionCompleted();
    void close();
    String getMachineState();
	void getUpdate();
	void updateTimer();
}
