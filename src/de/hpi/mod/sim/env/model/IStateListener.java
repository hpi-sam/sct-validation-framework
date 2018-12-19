package de.hpi.mod.sim.env.model;

import de.hpi.mod.sim.drivesystem.DrivesystemStatemachine;

public interface IStateListener {

    void onChangeState(DrivesystemStatemachine.State newState);
}
