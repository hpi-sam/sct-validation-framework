package de.hpi.mod.sim.env;

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
