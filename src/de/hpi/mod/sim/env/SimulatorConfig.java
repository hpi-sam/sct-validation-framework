package de.hpi.mod.sim.env;

/**
 * TODO: Configuration data should be removed from Simulator and added here
 * TODO: Should be an Instance and not static
 *
 * Contains all "magic-numbers" set in the Simulation.
 * Can be set by the view.
 */
public class SimulatorConfig {

    public static final float DEFAULT_ROBOT_MOVE_SPEED = .005f;

    private static float robotMoveSpeed = DEFAULT_ROBOT_MOVE_SPEED;

    public static float getRobotMoveSpeed() {
        return robotMoveSpeed;
    }

    public static void setRobotMoveSpeed(float robotMoveSpeed) {
        SimulatorConfig.robotMoveSpeed = robotMoveSpeed;
    }
}
