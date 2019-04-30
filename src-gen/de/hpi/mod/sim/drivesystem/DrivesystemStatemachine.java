package de.hpi.mod.sim.drivesystem;

import de.hpi.mod.sim.ITimer;

public class DrivesystemStatemachine implements IDrivesystemStatemachine {
	protected class SCInterfaceImpl implements SCInterface {
	
		private boolean unload;
		
		public void raiseUnload() {
			unload = true;
			runCycle();
		}
		
		private boolean unloaded;
		
		public void raiseUnloaded() {
			unloaded = true;
			runCycle();
		}
		
		private boolean stop;
		
		public void raiseStop() {
			stop = true;
			runCycle();
		}
		
		private boolean dataRefresh;
		
		public void raiseDataRefresh() {
			dataRefresh = true;
			runCycle();
		}
		
		private boolean newTarget;
		
		public void raiseNewTarget() {
			newTarget = true;
			runCycle();
		}
		
		private boolean actionCompleted;
		
		public void raiseActionCompleted() {
			actionCompleted = true;
			runCycle();
		}
		
		protected void clearEvents() {
			unload = false;
			unloaded = false;
			stop = false;
			dataRefresh = false;
			newTarget = false;
			actionCompleted = false;
		}
	}
	
	protected SCInterfaceImpl sCInterface;
	
	protected class SCIProcessorImpl implements SCIProcessor {
	
		private boolean unloaded;
		
		public boolean isRaisedUnloaded() {
			return unloaded;
		}
		
		protected void raiseUnloaded() {
			unloaded = true;
		}
		
		private boolean arrived;
		
		public boolean isRaisedArrived() {
			return arrived;
		}
		
		protected void raiseArrived() {
			arrived = true;
		}
		
		protected void clearEvents() {
		}
		protected void clearOutEvents() {
		
		unloaded = false;
		arrived = false;
		}
		
	}
	
	protected SCIProcessorImpl sCIProcessor;
	
	protected class SCIActorsImpl implements SCIActors {
	
		private boolean startUnload;
		
		public boolean isRaisedStartUnload() {
			return startUnload;
		}
		
		protected void raiseStartUnload() {
			startUnload = true;
		}
		
		private boolean driveForward;
		
		public boolean isRaisedDriveForward() {
			return driveForward;
		}
		
		protected void raiseDriveForward() {
			driveForward = true;
		}
		
		private boolean turnLeft;
		
		public boolean isRaisedTurnLeft() {
			return turnLeft;
		}
		
		protected void raiseTurnLeft() {
			turnLeft = true;
		}
		
		private boolean turnRight;
		
		public boolean isRaisedTurnRight() {
			return turnRight;
		}
		
		protected void raiseTurnRight() {
			turnRight = true;
		}
		
		protected void clearEvents() {
		}
		protected void clearOutEvents() {
		
		startUnload = false;
		driveForward = false;
		turnLeft = false;
		turnRight = false;
		}
		
	}
	
	protected SCIActorsImpl sCIActors;
	
	protected class SCIDataImpl implements SCIData {
	
		private SCIDataOperationCallback operationCallback;
		
		public void setSCIDataOperationCallback(
				SCIDataOperationCallback operationCallback) {
			this.operationCallback = operationCallback;
		}
	}
	
	protected SCIDataImpl sCIData;
	
	protected class SCIRawDataImpl implements SCIRawData {
	
		private SCIRawDataOperationCallback operationCallback;
		
		public void setSCIRawDataOperationCallback(
				SCIRawDataOperationCallback operationCallback) {
			this.operationCallback = operationCallback;
		}
	}
	
	protected SCIRawDataImpl sCIRawData;
	
	protected class SCIPositionTypeImpl implements SCIPositionType {
	
		private long wAYPOINT;
		
		public long getWAYPOINT() {
			return wAYPOINT;
		}
		
		public void setWAYPOINT(long value) {
			this.wAYPOINT = value;
		}
		
		private long sTATION;
		
		public long getSTATION() {
			return sTATION;
		}
		
		public void setSTATION(long value) {
			this.sTATION = value;
		}
		
		private long cROSSROAD;
		
		public long getCROSSROAD() {
			return cROSSROAD;
		}
		
		public void setCROSSROAD(long value) {
			this.cROSSROAD = value;
		}
		
		private long bLOCKED;
		
		public long getBLOCKED() {
			return bLOCKED;
		}
		
		public void setBLOCKED(long value) {
			this.bLOCKED = value;
		}
		
	}
	
	protected SCIPositionTypeImpl sCIPositionType;
	
	protected class SCIOrientationImpl implements SCIOrientation {
	
		private long nORTH;
		
		public long getNORTH() {
			return nORTH;
		}
		
		public void setNORTH(long value) {
			this.nORTH = value;
		}
		
		private long eAST;
		
		public long getEAST() {
			return eAST;
		}
		
		public void setEAST(long value) {
			this.eAST = value;
		}
		
		private long sOUTH;
		
		public long getSOUTH() {
			return sOUTH;
		}
		
		public void setSOUTH(long value) {
			this.sOUTH = value;
		}
		
		private long wEST;
		
		public long getWEST() {
			return wEST;
		}
		
		public void setWEST(long value) {
			this.wEST = value;
		}
		
	}
	
	protected SCIOrientationImpl sCIOrientation;
	
	protected class SCIDirectionImpl implements SCIDirection {
	
		private long lEFT;
		
		public long getLEFT() {
			return lEFT;
		}
		
		public void setLEFT(long value) {
			this.lEFT = value;
		}
		
		private long aHEAD;
		
		public long getAHEAD() {
			return aHEAD;
		}
		
		public void setAHEAD(long value) {
			this.aHEAD = value;
		}
		
		private long rIGHT;
		
		public long getRIGHT() {
			return rIGHT;
		}
		
		public void setRIGHT(long value) {
			this.rIGHT = value;
		}
		
		private long bEHIND;
		
		public long getBEHIND() {
			return bEHIND;
		}
		
		public void setBEHIND(long value) {
			this.bEHIND = value;
		}
		
	}
	
	protected SCIDirectionImpl sCIDirection;
	
	private boolean initialized = false;
	
	public enum State {
		drive_System_idle,
		drive_System_unloading,
		drive_System_driving,
		drive_System_driving__driving_on_waypoint_or_station,
		drive_System_driving__driving_waiting_in_station,
		drive_System_driving__driving_waiting_on_waypoint,
		drive_System_driving__driving_waiting_to_ensure_real_deadlock,
		drive_System_driving__driving_on_crossroad,
		drive_System_driving__driving_driving_forward_in_station,
		drive_System_driving__driving_turning_right_in_station,
		drive_System_driving__driving_turning_left_in_station,
		drive_System_driving__driving_exiting_charging_position,
		drive_System_driving__driving_exiting_charging_position__inStationExitCharger_right1,
		drive_System_driving__driving_exiting_charging_position__inStationExitCharger_right2,
		drive_System_driving__driving_exiting_charging_position__inStationExitCharger_forward,
		drive_System_driving__driving_exiting_charging_position__inStationExitCharger_right3,
		drive_System_driving__driving_entering_crossroad,
		drive_System_driving__driving_leaving_crossroad_ahead,
		drive_System_driving__driving_leaving_crossroad_ahead__onCrossroadDriveForward_forward1,
		drive_System_driving__driving_leaving_crossroad_ahead__onCrossroadDriveForward_forward2,
		drive_System_driving__driving_leaving_crossroad_to_right_side,
		drive_System_driving__driving_leaving_crossroad_to_right_side__onCrossroadDriveRight_right,
		drive_System_driving__driving_leaving_crossroad_to_right_side__onCrossroadDriveRight_forward,
		drive_System_driving__driving_leaving_crossroad_to_left_side,
		drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward1,
		drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_left,
		drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward2,
		drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward3,
		drive_System_driving__driving_turning_around_on_crossroad,
		drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_1,
		drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_1,
		drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_1,
		drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_2,
		drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_2,
		drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_2,
		$NullState$
	};
	
	private final State[] stateVector = new State[1];
	
	private int nextStateIndex;
	
	
	private ITimer timer;
	
	private final boolean[] timeEvents = new boolean[1];
	private long waitedForDeadlock;
	
	protected void setWaitedForDeadlock(long value) {
		waitedForDeadlock = value;
	}
	
	protected long getWaitedForDeadlock() {
		return waitedForDeadlock;
	}
	
	public DrivesystemStatemachine() {
		sCInterface = new SCInterfaceImpl();
		sCIProcessor = new SCIProcessorImpl();
		sCIActors = new SCIActorsImpl();
		sCIData = new SCIDataImpl();
		sCIRawData = new SCIRawDataImpl();
		sCIPositionType = new SCIPositionTypeImpl();
		sCIOrientation = new SCIOrientationImpl();
		sCIDirection = new SCIDirectionImpl();
	}
	
	public void init() {
		this.initialized = true;
		if (timer == null) {
			throw new IllegalStateException("timer not set.");
		}
		if (this.sCIData.operationCallback == null) {
			throw new IllegalStateException("Operation callback for interface sCIData must be set.");
		}
		
		if (this.sCIRawData.operationCallback == null) {
			throw new IllegalStateException("Operation callback for interface sCIRawData must be set.");
		}
		
		for (int i = 0; i < 1; i++) {
			stateVector[i] = State.$NullState$;
		}
		clearEvents();
		clearOutEvents();
		setWaitedForDeadlock(0);
		
		sCIPositionType.setWAYPOINT(0);
		
		sCIPositionType.setSTATION(1);
		
		sCIPositionType.setCROSSROAD(2);
		
		sCIPositionType.setBLOCKED(3);
		
		sCIOrientation.setNORTH(0);
		
		sCIOrientation.setEAST(1);
		
		sCIOrientation.setSOUTH(2);
		
		sCIOrientation.setWEST(3);
		
		sCIDirection.setLEFT(0);
		
		sCIDirection.setAHEAD(1);
		
		sCIDirection.setRIGHT(2);
		
		sCIDirection.setBEHIND(3);
	}
	
	public void enter() {
		if (!initialized) {
			throw new IllegalStateException(
				"The state machine needs to be initialized first by calling the init() function."
			);
		}
		if (timer == null) {
			throw new IllegalStateException("timer not set.");
		}
		enterSequence_Drive_System_default();
	}
	
	public void runCycle() {
		if (!initialized)
			throw new IllegalStateException(
					"The state machine needs to be initialized first by calling the init() function.");
		clearOutEvents();
		for (nextStateIndex = 0; nextStateIndex < stateVector.length; nextStateIndex++) {
			switch (stateVector[nextStateIndex]) {
			case drive_System_idle:
				drive_System_idle_react(true);
				break;
			case drive_System_unloading:
				drive_System_unloading_react(true);
				break;
			case drive_System_driving__driving_on_waypoint_or_station:
				drive_System_driving__driving_on_waypoint_or_station_react(true);
				break;
			case drive_System_driving__driving_waiting_in_station:
				drive_System_driving__driving_waiting_in_station_react(true);
				break;
			case drive_System_driving__driving_waiting_on_waypoint:
				drive_System_driving__driving_waiting_on_waypoint_react(true);
				break;
			case drive_System_driving__driving_waiting_to_ensure_real_deadlock:
				drive_System_driving__driving_waiting_to_ensure_real_deadlock_react(true);
				break;
			case drive_System_driving__driving_on_crossroad:
				drive_System_driving__driving_on_crossroad_react(true);
				break;
			case drive_System_driving__driving_driving_forward_in_station:
				drive_System_driving__driving_driving_forward_in_station_react(true);
				break;
			case drive_System_driving__driving_turning_right_in_station:
				drive_System_driving__driving_turning_right_in_station_react(true);
				break;
			case drive_System_driving__driving_turning_left_in_station:
				drive_System_driving__driving_turning_left_in_station_react(true);
				break;
			case drive_System_driving__driving_exiting_charging_position__inStationExitCharger_right1:
				drive_System_driving__driving_exiting_charging_position__inStationExitCharger_right1_react(true);
				break;
			case drive_System_driving__driving_exiting_charging_position__inStationExitCharger_right2:
				drive_System_driving__driving_exiting_charging_position__inStationExitCharger_right2_react(true);
				break;
			case drive_System_driving__driving_exiting_charging_position__inStationExitCharger_forward:
				drive_System_driving__driving_exiting_charging_position__inStationExitCharger_forward_react(true);
				break;
			case drive_System_driving__driving_exiting_charging_position__inStationExitCharger_right3:
				drive_System_driving__driving_exiting_charging_position__inStationExitCharger_right3_react(true);
				break;
			case drive_System_driving__driving_entering_crossroad:
				drive_System_driving__driving_entering_crossroad_react(true);
				break;
			case drive_System_driving__driving_leaving_crossroad_ahead__onCrossroadDriveForward_forward1:
				drive_System_driving__driving_leaving_crossroad_ahead__onCrossroadDriveForward_forward1_react(true);
				break;
			case drive_System_driving__driving_leaving_crossroad_ahead__onCrossroadDriveForward_forward2:
				drive_System_driving__driving_leaving_crossroad_ahead__onCrossroadDriveForward_forward2_react(true);
				break;
			case drive_System_driving__driving_leaving_crossroad_to_right_side__onCrossroadDriveRight_right:
				drive_System_driving__driving_leaving_crossroad_to_right_side__onCrossroadDriveRight_right_react(true);
				break;
			case drive_System_driving__driving_leaving_crossroad_to_right_side__onCrossroadDriveRight_forward:
				drive_System_driving__driving_leaving_crossroad_to_right_side__onCrossroadDriveRight_forward_react(true);
				break;
			case drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward1:
				drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward1_react(true);
				break;
			case drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_left:
				drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_left_react(true);
				break;
			case drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward2:
				drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward2_react(true);
				break;
			case drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward3:
				drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward3_react(true);
				break;
			case drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_1:
				drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_1_react(true);
				break;
			case drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_1:
				drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_1_react(true);
				break;
			case drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_1:
				drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_1_react(true);
				break;
			case drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_2:
				drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_2_react(true);
				break;
			case drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_2:
				drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_2_react(true);
				break;
			case drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_2:
				drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_2_react(true);
				break;
			default:
				// $NullState$
			}
		}
		clearEvents();
	}
	public void exit() {
		exitSequence_Drive_System();
	}
	
	/**
	 * @see IStatemachine#isActive()
	 */
	public boolean isActive() {
		return stateVector[0] != State.$NullState$;
	}
	
	/** 
	* Always returns 'false' since this state machine can never become final.
	*
	* @see IStatemachine#isFinal()
	*/
	public boolean isFinal() {
		return false;
	}
	/**
	* This method resets the incoming events (time events included).
	*/
	protected void clearEvents() {
		sCInterface.clearEvents();
		sCIProcessor.clearEvents();
		sCIActors.clearEvents();
		for (int i=0; i<timeEvents.length; i++) {
			timeEvents[i] = false;
		}
	}
	
	/**
	* This method resets the outgoing events.
	*/
	protected void clearOutEvents() {
		sCIProcessor.clearOutEvents();
		sCIActors.clearOutEvents();
	}
	
	/**
	* Returns true if the given state is currently active otherwise false.
	*/
	public boolean isStateActive(State state) {
	
		switch (state) {
		case drive_System_idle:
			return stateVector[0] == State.drive_System_idle;
		case drive_System_unloading:
			return stateVector[0] == State.drive_System_unloading;
		case drive_System_driving:
			return stateVector[0].ordinal() >= State.
					drive_System_driving.ordinal()&& stateVector[0].ordinal() <= State.drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_2.ordinal();
		case drive_System_driving__driving_on_waypoint_or_station:
			return stateVector[0] == State.drive_System_driving__driving_on_waypoint_or_station;
		case drive_System_driving__driving_waiting_in_station:
			return stateVector[0] == State.drive_System_driving__driving_waiting_in_station;
		case drive_System_driving__driving_waiting_on_waypoint:
			return stateVector[0] == State.drive_System_driving__driving_waiting_on_waypoint;
		case drive_System_driving__driving_waiting_to_ensure_real_deadlock:
			return stateVector[0] == State.drive_System_driving__driving_waiting_to_ensure_real_deadlock;
		case drive_System_driving__driving_on_crossroad:
			return stateVector[0] == State.drive_System_driving__driving_on_crossroad;
		case drive_System_driving__driving_driving_forward_in_station:
			return stateVector[0] == State.drive_System_driving__driving_driving_forward_in_station;
		case drive_System_driving__driving_turning_right_in_station:
			return stateVector[0] == State.drive_System_driving__driving_turning_right_in_station;
		case drive_System_driving__driving_turning_left_in_station:
			return stateVector[0] == State.drive_System_driving__driving_turning_left_in_station;
		case drive_System_driving__driving_exiting_charging_position:
			return stateVector[0].ordinal() >= State.
					drive_System_driving__driving_exiting_charging_position.ordinal()&& stateVector[0].ordinal() <= State.drive_System_driving__driving_exiting_charging_position__inStationExitCharger_right3.ordinal();
		case drive_System_driving__driving_exiting_charging_position__inStationExitCharger_right1:
			return stateVector[0] == State.drive_System_driving__driving_exiting_charging_position__inStationExitCharger_right1;
		case drive_System_driving__driving_exiting_charging_position__inStationExitCharger_right2:
			return stateVector[0] == State.drive_System_driving__driving_exiting_charging_position__inStationExitCharger_right2;
		case drive_System_driving__driving_exiting_charging_position__inStationExitCharger_forward:
			return stateVector[0] == State.drive_System_driving__driving_exiting_charging_position__inStationExitCharger_forward;
		case drive_System_driving__driving_exiting_charging_position__inStationExitCharger_right3:
			return stateVector[0] == State.drive_System_driving__driving_exiting_charging_position__inStationExitCharger_right3;
		case drive_System_driving__driving_entering_crossroad:
			return stateVector[0] == State.drive_System_driving__driving_entering_crossroad;
		case drive_System_driving__driving_leaving_crossroad_ahead:
			return stateVector[0].ordinal() >= State.
					drive_System_driving__driving_leaving_crossroad_ahead.ordinal()&& stateVector[0].ordinal() <= State.drive_System_driving__driving_leaving_crossroad_ahead__onCrossroadDriveForward_forward2.ordinal();
		case drive_System_driving__driving_leaving_crossroad_ahead__onCrossroadDriveForward_forward1:
			return stateVector[0] == State.drive_System_driving__driving_leaving_crossroad_ahead__onCrossroadDriveForward_forward1;
		case drive_System_driving__driving_leaving_crossroad_ahead__onCrossroadDriveForward_forward2:
			return stateVector[0] == State.drive_System_driving__driving_leaving_crossroad_ahead__onCrossroadDriveForward_forward2;
		case drive_System_driving__driving_leaving_crossroad_to_right_side:
			return stateVector[0].ordinal() >= State.
					drive_System_driving__driving_leaving_crossroad_to_right_side.ordinal()&& stateVector[0].ordinal() <= State.drive_System_driving__driving_leaving_crossroad_to_right_side__onCrossroadDriveRight_forward.ordinal();
		case drive_System_driving__driving_leaving_crossroad_to_right_side__onCrossroadDriveRight_right:
			return stateVector[0] == State.drive_System_driving__driving_leaving_crossroad_to_right_side__onCrossroadDriveRight_right;
		case drive_System_driving__driving_leaving_crossroad_to_right_side__onCrossroadDriveRight_forward:
			return stateVector[0] == State.drive_System_driving__driving_leaving_crossroad_to_right_side__onCrossroadDriveRight_forward;
		case drive_System_driving__driving_leaving_crossroad_to_left_side:
			return stateVector[0].ordinal() >= State.
					drive_System_driving__driving_leaving_crossroad_to_left_side.ordinal()&& stateVector[0].ordinal() <= State.drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward3.ordinal();
		case drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward1:
			return stateVector[0] == State.drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward1;
		case drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_left:
			return stateVector[0] == State.drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_left;
		case drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward2:
			return stateVector[0] == State.drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward2;
		case drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward3:
			return stateVector[0] == State.drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward3;
		case drive_System_driving__driving_turning_around_on_crossroad:
			return stateVector[0].ordinal() >= State.
					drive_System_driving__driving_turning_around_on_crossroad.ordinal()&& stateVector[0].ordinal() <= State.drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_2.ordinal();
		case drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_1:
			return stateVector[0] == State.drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_1;
		case drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_1:
			return stateVector[0] == State.drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_1;
		case drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_1:
			return stateVector[0] == State.drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_1;
		case drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_2:
			return stateVector[0] == State.drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_2;
		case drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_2:
			return stateVector[0] == State.drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_2;
		case drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_2:
			return stateVector[0] == State.drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_2;
		default:
			return false;
		}
	}
	
	/**
	* Set the {@link ITimer} for the state machine. It must be set
	* externally on a timed state machine before a run cycle can be correctly
	* executed.
	* 
	* @param timer
	*/
	public void setTimer(ITimer timer) {
		this.timer = timer;
	}
	
	/**
	* Returns the currently used timer.
	* 
	* @return {@link ITimer}
	*/
	public ITimer getTimer() {
		return timer;
	}
	
	public void timeElapsed(int eventID) {
		timeEvents[eventID] = true;
		runCycle();
	}
	
	public SCInterface getSCInterface() {
		return sCInterface;
	}
	
	public SCIProcessor getSCIProcessor() {
		return sCIProcessor;
	}
	
	public SCIActors getSCIActors() {
		return sCIActors;
	}
	
	public SCIData getSCIData() {
		return sCIData;
	}
	
	public SCIRawData getSCIRawData() {
		return sCIRawData;
	}
	
	public SCIPositionType getSCIPositionType() {
		return sCIPositionType;
	}
	
	public SCIOrientation getSCIOrientation() {
		return sCIOrientation;
	}
	
	public SCIDirection getSCIDirection() {
		return sCIDirection;
	}
	
	public void raiseUnload() {
		sCInterface.raiseUnload();
	}
	
	public void raiseUnloaded() {
		sCInterface.raiseUnloaded();
	}
	
	public void raiseStop() {
		sCInterface.raiseStop();
	}
	
	public void raiseDataRefresh() {
		sCInterface.raiseDataRefresh();
	}
	
	public void raiseNewTarget() {
		sCInterface.raiseNewTarget();
	}
	
	public void raiseActionCompleted() {
		sCInterface.raiseActionCompleted();
	}
	
	private boolean check_Drive_System_driving__driving__choice_0_tr0_tr0() {
		return sCIData.operationCallback.targetDirection()==sCIDirection.getAHEAD();
	}
	
	private boolean check_Drive_System_driving__driving__choice_0_tr1_tr1() {
		return sCIData.operationCallback.targetDirection()==sCIDirection.getRIGHT();
	}
	
	private boolean check_Drive_System_driving__driving__choice_0_tr2_tr2() {
		return sCIData.operationCallback.targetDirection()==sCIDirection.getLEFT();
	}
	
	private boolean check_Drive_System_driving__driving__choice_0_tr3_tr3() {
		return sCIData.operationCallback.targetDirection()==sCIDirection.getBEHIND();
	}
	
	private boolean check_Drive_System_driving__driving__choice_1_tr0_tr0() {
		return sCIData.operationCallback.targetDirection()==sCIDirection.getBEHIND();
	}
	
	private boolean check_Drive_System_driving__driving__choice_1_tr1_tr1() {
		return (sCIData.operationCallback.targetDirection()==sCIDirection.getRIGHT() && !sCIData.operationCallback.blockedWaypointRight());
	}
	
	private boolean check_Drive_System_driving__driving__choice_1_tr2_tr2() {
		return (sCIData.operationCallback.targetDirection()==sCIDirection.getAHEAD() && !sCIData.operationCallback.blockedWaypointAhead());
	}
	
	private boolean check_Drive_System_driving__driving__choice_1_tr3_tr3() {
		return ((sCIData.operationCallback.targetDirection()==sCIDirection.getLEFT() && !sCIData.operationCallback.blockedWaypointLeft()) && !sCIData.operationCallback.blockedCrossroadAhead());
	}
	
	private boolean check_Drive_System_driving__driving__choice_2_tr1_tr1() {
		return getWaitedForDeadlock()>5;
	}
	
	private boolean check_Drive_System_driving__driving__choice_3_tr1_tr1() {
		return ((((((!sCIData.operationCallback.blockedCrossroadAhead() && sCIData.operationCallback.blockedWaypointAhead()) && sCIData.operationCallback.blockedWaypointLeft()) && sCIData.operationCallback.blockedWaypointRight()) && sCIData.operationCallback.posOrientation()==sCIOrientation.getSOUTH())) || ((((sCIData.operationCallback.blockedCrossroadAhead() && sCIData.operationCallback.blockedRight()) && !sCIData.operationCallback.blockedFront()) && sCIData.operationCallback.targetDirection()==sCIDirection.getRIGHT())));
	}
	
	private boolean check_Drive_System_driving__driving__choice_3_tr2_tr2() {
		return (!sCIData.operationCallback.blockedCrossroadAhead() && !sCIData.operationCallback.blockedWaypointRight());
	}
	
	private boolean check_Drive_System_driving__driving__choice_5_tr0_tr0() {
		return (sCIData.operationCallback.isOnTarget() || sCIData.operationCallback.canUnloadToTarget());
	}
	
	private boolean check_Drive_System_driving__driving__choice_6_tr0_tr0() {
		return sCIData.operationCallback.posType()==sCIPositionType.getWAYPOINT();
	}
	
	private boolean check_Drive_System_driving__driving__choice_8_tr0_tr0() {
		return !sCIData.operationCallback.blockedFront();
	}
	
	private boolean check_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad__choice_0_tr0_tr0() {
		return !sCIData.operationCallback.blockedFront();
	}
	
	private boolean check_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad__choice_1_tr0_tr0() {
		return !sCIData.operationCallback.blockedFront();
	}
	
	private boolean check_Drive_System_driving__driving__choice_9_tr0_tr0() {
		return (((sCIData.operationCallback.posOrientation()==sCIOrientation.getEAST() || sCIData.operationCallback.posOrientation()==sCIOrientation.getWEST())) && !sCIData.operationCallback.blockedCrossroadAhead());
	}
	
	private boolean check_Drive_System_driving__driving__choice_9_tr1_tr1() {
		return (((sCIData.operationCallback.posOrientation()==sCIOrientation.getSOUTH() || sCIData.operationCallback.posOrientation()==sCIOrientation.getNORTH())) && !sCIData.operationCallback.blockedWaypointRight());
	}
	
	private void effect_Drive_System_driving_tr1() {
		exitSequence_Drive_System_driving();
		sCIProcessor.raiseArrived();
		
		enterSequence_Drive_System_idle_default();
		react();
	}
	
	private void effect_Drive_System_driving__driving_exiting_charging_position_tr0() {
		exitSequence_Drive_System_driving__driving_exiting_charging_position();
		react_Drive_System_driving__driving__choice_7();
	}
	
	private void effect_Drive_System_driving__driving_leaving_crossroad_ahead_tr0() {
		exitSequence_Drive_System_driving__driving_leaving_crossroad_ahead();
		enterSequence_Drive_System_driving__driving_on_waypoint_or_station_default();
		drive_System_driving_react(false);
	}
	
	private void effect_Drive_System_driving__driving_leaving_crossroad_to_right_side_tr0() {
		exitSequence_Drive_System_driving__driving_leaving_crossroad_to_right_side();
		enterSequence_Drive_System_driving__driving_on_waypoint_or_station_default();
		drive_System_driving_react(false);
	}
	
	private void effect_Drive_System_driving__driving_leaving_crossroad_to_left_side_tr0() {
		exitSequence_Drive_System_driving__driving_leaving_crossroad_to_left_side();
		enterSequence_Drive_System_driving__driving_on_waypoint_or_station_default();
		drive_System_driving_react(false);
	}
	
	private void effect_Drive_System_driving__driving_turning_around_on_crossroad_tr0() {
		exitSequence_Drive_System_driving__driving_turning_around_on_crossroad();
		enterSequence_Drive_System_driving__driving_on_waypoint_or_station_default();
		drive_System_driving_react(false);
	}
	
	private void effect_Drive_System_driving__driving__choice_0_tr0() {
		react_Drive_System_driving__driving__choice_8();
	}
	
	private void effect_Drive_System_driving__driving__choice_0_tr1() {
		enterSequence_Drive_System_driving__driving_turning_right_in_station_default();
	}
	
	private void effect_Drive_System_driving__driving__choice_0_tr2() {
		enterSequence_Drive_System_driving__driving_turning_left_in_station_default();
	}
	
	private void effect_Drive_System_driving__driving__choice_0_tr3() {
		enterSequence_Drive_System_driving__driving_exiting_charging_position_default();
	}
	
	private void effect_Drive_System_driving__driving__choice_0_tr4() {
		enterSequence_Drive_System_driving__driving_on_waypoint_or_station_default();
	}
	
	private void effect_Drive_System_driving__driving__choice_1_tr0() {
		react_Drive_System_driving__driving__choice_9();
	}
	
	private void effect_Drive_System_driving__driving__choice_1_tr1() {
		enterSequence_Drive_System_driving__driving_leaving_crossroad_to_right_side_default();
	}
	
	private void effect_Drive_System_driving__driving__choice_1_tr2() {
		enterSequence_Drive_System_driving__driving_leaving_crossroad_ahead_default();
	}
	
	private void effect_Drive_System_driving__driving__choice_1_tr3() {
		enterSequence_Drive_System_driving__driving_leaving_crossroad_to_left_side_default();
	}
	
	private void effect_Drive_System_driving__driving__choice_1_tr4() {
		enterSequence_Drive_System_driving__driving_on_crossroad_default();
	}
	
	private void effect_Drive_System_driving__driving__choice_2_tr1() {
		enterSequence_Drive_System_driving__driving_entering_crossroad_default();
	}
	
	private void effect_Drive_System_driving__driving__choice_2_tr0() {
		enterSequence_Drive_System_driving__driving_waiting_to_ensure_real_deadlock_default();
	}
	
	private void effect_Drive_System_driving__driving__choice_3_tr1() {
		react_Drive_System_driving__driving__choice_2();
	}
	
	private void effect_Drive_System_driving__driving__choice_3_tr2() {
		enterSequence_Drive_System_driving__driving_entering_crossroad_default();
	}
	
	private void effect_Drive_System_driving__driving__choice_3_tr0() {
		enterSequence_Drive_System_driving__driving_waiting_on_waypoint_default();
	}
	
	private void effect_Drive_System_driving__driving__choice_4_tr0() {
		react_Drive_System_driving__driving__choice_3();
	}
	
	private void effect_Drive_System_driving__driving__choice_5_tr0() {
		react_Drive_System_driving__driving__exit_Default();
	}
	
	private void effect_Drive_System_driving__driving__choice_5_tr1() {
		react_Drive_System_driving__driving__choice_6();
	}
	
	private void effect_Drive_System_driving__driving__choice_6_tr0() {
		react_Drive_System_driving__driving__choice_4();
	}
	
	private void effect_Drive_System_driving__driving__choice_6_tr1() {
		react_Drive_System_driving__driving__choice_0();
	}
	
	private void effect_Drive_System_driving__driving__choice_7_tr0() {
		enterSequence_Drive_System_driving__driving_on_waypoint_or_station_default();
	}
	
	private void effect_Drive_System_driving__driving__choice_8_tr0() {
		enterSequence_Drive_System_driving__driving_driving_forward_in_station_default();
	}
	
	private void effect_Drive_System_driving__driving__choice_8_tr1() {
		enterSequence_Drive_System_driving__driving_waiting_in_station_default();
	}
	
	private void effect_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad__choice_0_tr0() {
		enterSequence_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_1_default();
	}
	
	private void effect_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad__choice_0_tr1() {
		enterSequence_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_1_default();
	}
	
	private void effect_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad__choice_1_tr0() {
		enterSequence_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_2_default();
	}
	
	private void effect_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad__choice_1_tr1() {
		enterSequence_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_2_default();
	}
	
	private void effect_Drive_System_driving__driving__choice_9_tr0() {
		enterSequence_Drive_System_driving__driving_turning_around_on_crossroad_default();
	}
	
	private void effect_Drive_System_driving__driving__choice_9_tr1() {
		enterSequence_Drive_System_driving__driving_leaving_crossroad_to_right_side_default();
	}
	
	private void effect_Drive_System_driving__driving__choice_9_tr2() {
		enterSequence_Drive_System_driving__driving_on_crossroad_default();
	}
	
	/* Entry action for state 'waiting to ensure real deadlock'. */
	private void entryAction_Drive_System_driving__driving_waiting_to_ensure_real_deadlock() {
		timer.setTimer(this, 0, (1 * 1000), false);
	}
	
	/* Entry action for state 'driving forward in station'. */
	private void entryAction_Drive_System_driving__driving_driving_forward_in_station() {
		sCIActors.raiseDriveForward();
	}
	
	/* Entry action for state 'turning right in station'. */
	private void entryAction_Drive_System_driving__driving_turning_right_in_station() {
		sCIActors.raiseTurnRight();
	}
	
	/* Entry action for state 'turning left in station'. */
	private void entryAction_Drive_System_driving__driving_turning_left_in_station() {
		sCIActors.raiseTurnLeft();
	}
	
	/* Entry action for state 'right1'. */
	private void entryAction_Drive_System_driving__driving_exiting_charging_position__inStationExitCharger_right1() {
		sCIActors.raiseTurnRight();
	}
	
	/* Entry action for state 'right2'. */
	private void entryAction_Drive_System_driving__driving_exiting_charging_position__inStationExitCharger_right2() {
		sCIActors.raiseTurnRight();
	}
	
	/* Entry action for state 'forward'. */
	private void entryAction_Drive_System_driving__driving_exiting_charging_position__inStationExitCharger_forward() {
		sCIActors.raiseDriveForward();
	}
	
	/* Entry action for state 'right3'. */
	private void entryAction_Drive_System_driving__driving_exiting_charging_position__inStationExitCharger_right3() {
		sCIActors.raiseTurnRight();
	}
	
	/* Entry action for state 'entering crossroad'. */
	private void entryAction_Drive_System_driving__driving_entering_crossroad() {
		sCIActors.raiseDriveForward();
		
		setWaitedForDeadlock(0);
	}
	
	/* Entry action for state 'forward1'. */
	private void entryAction_Drive_System_driving__driving_leaving_crossroad_ahead__onCrossroadDriveForward_forward1() {
		sCIActors.raiseDriveForward();
	}
	
	/* Entry action for state 'forward2'. */
	private void entryAction_Drive_System_driving__driving_leaving_crossroad_ahead__onCrossroadDriveForward_forward2() {
		sCIActors.raiseDriveForward();
	}
	
	/* Entry action for state 'right'. */
	private void entryAction_Drive_System_driving__driving_leaving_crossroad_to_right_side__onCrossroadDriveRight_right() {
		sCIActors.raiseTurnRight();
	}
	
	/* Entry action for state 'forward'. */
	private void entryAction_Drive_System_driving__driving_leaving_crossroad_to_right_side__onCrossroadDriveRight_forward() {
		sCIActors.raiseDriveForward();
	}
	
	/* Entry action for state 'forward1'. */
	private void entryAction_Drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward1() {
		sCIActors.raiseDriveForward();
	}
	
	/* Entry action for state 'left'. */
	private void entryAction_Drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_left() {
		sCIActors.raiseTurnLeft();
	}
	
	/* Entry action for state 'forward2'. */
	private void entryAction_Drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward2() {
		sCIActors.raiseDriveForward();
	}
	
	/* Entry action for state 'forward3'. */
	private void entryAction_Drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward3() {
		sCIActors.raiseDriveForward();
	}
	
	/* Entry action for state 'turining left 1'. */
	private void entryAction_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_1() {
		sCIActors.raiseTurnLeft();
	}
	
	/* Entry action for state 'driving forward 1'. */
	private void entryAction_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_1() {
		sCIActors.raiseDriveForward();
	}
	
	/* Entry action for state 'turining left 2'. */
	private void entryAction_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_2() {
		sCIActors.raiseTurnLeft();
	}
	
	/* Entry action for state 'driving forward 2'. */
	private void entryAction_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_2() {
		sCIActors.raiseDriveForward();
	}
	
	/* Exit action for state 'waiting to ensure real deadlock'. */
	private void exitAction_Drive_System_driving__driving_waiting_to_ensure_real_deadlock() {
		timer.unsetTimer(this, 0);
	}
	
	/* 'default' enter sequence for state idle */
	private void enterSequence_Drive_System_idle_default() {
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_idle;
	}
	
	/* 'default' enter sequence for state unloading */
	private void enterSequence_Drive_System_unloading_default() {
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_unloading;
	}
	
	/* 'default' enter sequence for state driving */
	private void enterSequence_Drive_System_driving_default() {
		enterSequence_Drive_System_driving__driving_default();
	}
	
	/* 'default' enter sequence for state on waypoint or station */
	private void enterSequence_Drive_System_driving__driving_on_waypoint_or_station_default() {
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_driving__driving_on_waypoint_or_station;
	}
	
	/* 'default' enter sequence for state waiting in station */
	private void enterSequence_Drive_System_driving__driving_waiting_in_station_default() {
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_driving__driving_waiting_in_station;
	}
	
	/* 'default' enter sequence for state waiting on waypoint */
	private void enterSequence_Drive_System_driving__driving_waiting_on_waypoint_default() {
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_driving__driving_waiting_on_waypoint;
	}
	
	/* 'default' enter sequence for state waiting to ensure real deadlock */
	private void enterSequence_Drive_System_driving__driving_waiting_to_ensure_real_deadlock_default() {
		entryAction_Drive_System_driving__driving_waiting_to_ensure_real_deadlock();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_driving__driving_waiting_to_ensure_real_deadlock;
	}
	
	/* 'default' enter sequence for state on crossroad */
	private void enterSequence_Drive_System_driving__driving_on_crossroad_default() {
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_driving__driving_on_crossroad;
	}
	
	/* 'default' enter sequence for state driving forward in station */
	private void enterSequence_Drive_System_driving__driving_driving_forward_in_station_default() {
		entryAction_Drive_System_driving__driving_driving_forward_in_station();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_driving__driving_driving_forward_in_station;
	}
	
	/* 'default' enter sequence for state turning right in station */
	private void enterSequence_Drive_System_driving__driving_turning_right_in_station_default() {
		entryAction_Drive_System_driving__driving_turning_right_in_station();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_driving__driving_turning_right_in_station;
	}
	
	/* 'default' enter sequence for state turning left in station */
	private void enterSequence_Drive_System_driving__driving_turning_left_in_station_default() {
		entryAction_Drive_System_driving__driving_turning_left_in_station();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_driving__driving_turning_left_in_station;
	}
	
	/* 'default' enter sequence for state exiting charging position */
	private void enterSequence_Drive_System_driving__driving_exiting_charging_position_default() {
		enterSequence_Drive_System_driving__driving_exiting_charging_position__inStationExitCharger_default();
	}
	
	/* 'default' enter sequence for state right1 */
	private void enterSequence_Drive_System_driving__driving_exiting_charging_position__inStationExitCharger_right1_default() {
		entryAction_Drive_System_driving__driving_exiting_charging_position__inStationExitCharger_right1();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_driving__driving_exiting_charging_position__inStationExitCharger_right1;
	}
	
	/* 'default' enter sequence for state right2 */
	private void enterSequence_Drive_System_driving__driving_exiting_charging_position__inStationExitCharger_right2_default() {
		entryAction_Drive_System_driving__driving_exiting_charging_position__inStationExitCharger_right2();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_driving__driving_exiting_charging_position__inStationExitCharger_right2;
	}
	
	/* 'default' enter sequence for state forward */
	private void enterSequence_Drive_System_driving__driving_exiting_charging_position__inStationExitCharger_forward_default() {
		entryAction_Drive_System_driving__driving_exiting_charging_position__inStationExitCharger_forward();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_driving__driving_exiting_charging_position__inStationExitCharger_forward;
	}
	
	/* 'default' enter sequence for state right3 */
	private void enterSequence_Drive_System_driving__driving_exiting_charging_position__inStationExitCharger_right3_default() {
		entryAction_Drive_System_driving__driving_exiting_charging_position__inStationExitCharger_right3();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_driving__driving_exiting_charging_position__inStationExitCharger_right3;
	}
	
	/* 'default' enter sequence for state entering crossroad */
	private void enterSequence_Drive_System_driving__driving_entering_crossroad_default() {
		entryAction_Drive_System_driving__driving_entering_crossroad();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_driving__driving_entering_crossroad;
	}
	
	/* 'default' enter sequence for state leaving crossroad ahead */
	private void enterSequence_Drive_System_driving__driving_leaving_crossroad_ahead_default() {
		enterSequence_Drive_System_driving__driving_leaving_crossroad_ahead__onCrossroadDriveForward_default();
	}
	
	/* 'default' enter sequence for state forward1 */
	private void enterSequence_Drive_System_driving__driving_leaving_crossroad_ahead__onCrossroadDriveForward_forward1_default() {
		entryAction_Drive_System_driving__driving_leaving_crossroad_ahead__onCrossroadDriveForward_forward1();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_driving__driving_leaving_crossroad_ahead__onCrossroadDriveForward_forward1;
	}
	
	/* 'default' enter sequence for state forward2 */
	private void enterSequence_Drive_System_driving__driving_leaving_crossroad_ahead__onCrossroadDriveForward_forward2_default() {
		entryAction_Drive_System_driving__driving_leaving_crossroad_ahead__onCrossroadDriveForward_forward2();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_driving__driving_leaving_crossroad_ahead__onCrossroadDriveForward_forward2;
	}
	
	/* 'default' enter sequence for state leaving crossroad to right side */
	private void enterSequence_Drive_System_driving__driving_leaving_crossroad_to_right_side_default() {
		enterSequence_Drive_System_driving__driving_leaving_crossroad_to_right_side__onCrossroadDriveRight_default();
	}
	
	/* 'default' enter sequence for state right */
	private void enterSequence_Drive_System_driving__driving_leaving_crossroad_to_right_side__onCrossroadDriveRight_right_default() {
		entryAction_Drive_System_driving__driving_leaving_crossroad_to_right_side__onCrossroadDriveRight_right();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_driving__driving_leaving_crossroad_to_right_side__onCrossroadDriveRight_right;
	}
	
	/* 'default' enter sequence for state forward */
	private void enterSequence_Drive_System_driving__driving_leaving_crossroad_to_right_side__onCrossroadDriveRight_forward_default() {
		entryAction_Drive_System_driving__driving_leaving_crossroad_to_right_side__onCrossroadDriveRight_forward();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_driving__driving_leaving_crossroad_to_right_side__onCrossroadDriveRight_forward;
	}
	
	/* 'default' enter sequence for state leaving crossroad to left side */
	private void enterSequence_Drive_System_driving__driving_leaving_crossroad_to_left_side_default() {
		enterSequence_Drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_default();
	}
	
	/* 'default' enter sequence for state forward1 */
	private void enterSequence_Drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward1_default() {
		entryAction_Drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward1();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward1;
	}
	
	/* 'default' enter sequence for state left */
	private void enterSequence_Drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_left_default() {
		entryAction_Drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_left();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_left;
	}
	
	/* 'default' enter sequence for state forward2 */
	private void enterSequence_Drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward2_default() {
		entryAction_Drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward2();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward2;
	}
	
	/* 'default' enter sequence for state forward3 */
	private void enterSequence_Drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward3_default() {
		entryAction_Drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward3();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward3;
	}
	
	/* 'default' enter sequence for state turning around on crossroad */
	private void enterSequence_Drive_System_driving__driving_turning_around_on_crossroad_default() {
		enterSequence_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_default();
	}
	
	/* 'default' enter sequence for state turining left 1 */
	private void enterSequence_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_1_default() {
		entryAction_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_1();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_1;
	}
	
	/* 'default' enter sequence for state waiting to drive forward 1 */
	private void enterSequence_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_1_default() {
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_1;
	}
	
	/* 'default' enter sequence for state driving forward 1 */
	private void enterSequence_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_1_default() {
		entryAction_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_1();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_1;
	}
	
	/* 'default' enter sequence for state turining left 2 */
	private void enterSequence_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_2_default() {
		entryAction_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_2();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_2;
	}
	
	/* 'default' enter sequence for state waiting to drive forward 2 */
	private void enterSequence_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_2_default() {
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_2;
	}
	
	/* 'default' enter sequence for state driving forward 2 */
	private void enterSequence_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_2_default() {
		entryAction_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_2();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_2;
	}
	
	/* 'default' enter sequence for region Drive System */
	private void enterSequence_Drive_System_default() {
		react_Drive_System__entry_Default();
	}
	
	/* 'default' enter sequence for region _driving */
	private void enterSequence_Drive_System_driving__driving_default() {
		react_Drive_System_driving__driving__entry_Default();
	}
	
	/* 'default' enter sequence for region _inStationExitCharger */
	private void enterSequence_Drive_System_driving__driving_exiting_charging_position__inStationExitCharger_default() {
		react_Drive_System_driving__driving_exiting_charging_position__inStationExitCharger__entry_Default();
	}
	
	/* 'default' enter sequence for region _onCrossroadDriveForward */
	private void enterSequence_Drive_System_driving__driving_leaving_crossroad_ahead__onCrossroadDriveForward_default() {
		react_Drive_System_driving__driving_leaving_crossroad_ahead__onCrossroadDriveForward__entry_Default();
	}
	
	/* 'default' enter sequence for region _onCrossroadDriveRight */
	private void enterSequence_Drive_System_driving__driving_leaving_crossroad_to_right_side__onCrossroadDriveRight_default() {
		react_Drive_System_driving__driving_leaving_crossroad_to_right_side__onCrossroadDriveRight__entry_Default();
	}
	
	/* 'default' enter sequence for region _onCrossroadDriveLeft */
	private void enterSequence_Drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_default() {
		react_Drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft__entry_Default();
	}
	
	/* 'default' enter sequence for region _turningAroundOnCrossroad */
	private void enterSequence_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_default() {
		react_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad__entry_Default();
	}
	
	/* Default exit sequence for state idle */
	private void exitSequence_Drive_System_idle() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state unloading */
	private void exitSequence_Drive_System_unloading() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state driving */
	private void exitSequence_Drive_System_driving() {
		exitSequence_Drive_System_driving__driving();
	}
	
	/* Default exit sequence for state on waypoint or station */
	private void exitSequence_Drive_System_driving__driving_on_waypoint_or_station() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state waiting in station */
	private void exitSequence_Drive_System_driving__driving_waiting_in_station() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state waiting on waypoint */
	private void exitSequence_Drive_System_driving__driving_waiting_on_waypoint() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state waiting to ensure real deadlock */
	private void exitSequence_Drive_System_driving__driving_waiting_to_ensure_real_deadlock() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
		
		exitAction_Drive_System_driving__driving_waiting_to_ensure_real_deadlock();
	}
	
	/* Default exit sequence for state on crossroad */
	private void exitSequence_Drive_System_driving__driving_on_crossroad() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state driving forward in station */
	private void exitSequence_Drive_System_driving__driving_driving_forward_in_station() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state turning right in station */
	private void exitSequence_Drive_System_driving__driving_turning_right_in_station() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state turning left in station */
	private void exitSequence_Drive_System_driving__driving_turning_left_in_station() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state exiting charging position */
	private void exitSequence_Drive_System_driving__driving_exiting_charging_position() {
		exitSequence_Drive_System_driving__driving_exiting_charging_position__inStationExitCharger();
	}
	
	/* Default exit sequence for state right1 */
	private void exitSequence_Drive_System_driving__driving_exiting_charging_position__inStationExitCharger_right1() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state right2 */
	private void exitSequence_Drive_System_driving__driving_exiting_charging_position__inStationExitCharger_right2() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state forward */
	private void exitSequence_Drive_System_driving__driving_exiting_charging_position__inStationExitCharger_forward() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state right3 */
	private void exitSequence_Drive_System_driving__driving_exiting_charging_position__inStationExitCharger_right3() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state entering crossroad */
	private void exitSequence_Drive_System_driving__driving_entering_crossroad() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state leaving crossroad ahead */
	private void exitSequence_Drive_System_driving__driving_leaving_crossroad_ahead() {
		exitSequence_Drive_System_driving__driving_leaving_crossroad_ahead__onCrossroadDriveForward();
	}
	
	/* Default exit sequence for state forward1 */
	private void exitSequence_Drive_System_driving__driving_leaving_crossroad_ahead__onCrossroadDriveForward_forward1() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state forward2 */
	private void exitSequence_Drive_System_driving__driving_leaving_crossroad_ahead__onCrossroadDriveForward_forward2() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state leaving crossroad to right side */
	private void exitSequence_Drive_System_driving__driving_leaving_crossroad_to_right_side() {
		exitSequence_Drive_System_driving__driving_leaving_crossroad_to_right_side__onCrossroadDriveRight();
	}
	
	/* Default exit sequence for state right */
	private void exitSequence_Drive_System_driving__driving_leaving_crossroad_to_right_side__onCrossroadDriveRight_right() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state forward */
	private void exitSequence_Drive_System_driving__driving_leaving_crossroad_to_right_side__onCrossroadDriveRight_forward() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state leaving crossroad to left side */
	private void exitSequence_Drive_System_driving__driving_leaving_crossroad_to_left_side() {
		exitSequence_Drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft();
	}
	
	/* Default exit sequence for state forward1 */
	private void exitSequence_Drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward1() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state left */
	private void exitSequence_Drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_left() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state forward2 */
	private void exitSequence_Drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward2() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state forward3 */
	private void exitSequence_Drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward3() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state turning around on crossroad */
	private void exitSequence_Drive_System_driving__driving_turning_around_on_crossroad() {
		exitSequence_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad();
	}
	
	/* Default exit sequence for state turining left 1 */
	private void exitSequence_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_1() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state waiting to drive forward 1 */
	private void exitSequence_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_1() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state driving forward 1 */
	private void exitSequence_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_1() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state turining left 2 */
	private void exitSequence_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_2() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state waiting to drive forward 2 */
	private void exitSequence_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_2() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state driving forward 2 */
	private void exitSequence_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_2() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for region Drive System */
	private void exitSequence_Drive_System() {
		switch (stateVector[0]) {
		case drive_System_idle:
			exitSequence_Drive_System_idle();
			break;
		case drive_System_unloading:
			exitSequence_Drive_System_unloading();
			break;
		case drive_System_driving__driving_on_waypoint_or_station:
			exitSequence_Drive_System_driving__driving_on_waypoint_or_station();
			break;
		case drive_System_driving__driving_waiting_in_station:
			exitSequence_Drive_System_driving__driving_waiting_in_station();
			break;
		case drive_System_driving__driving_waiting_on_waypoint:
			exitSequence_Drive_System_driving__driving_waiting_on_waypoint();
			break;
		case drive_System_driving__driving_waiting_to_ensure_real_deadlock:
			exitSequence_Drive_System_driving__driving_waiting_to_ensure_real_deadlock();
			break;
		case drive_System_driving__driving_on_crossroad:
			exitSequence_Drive_System_driving__driving_on_crossroad();
			break;
		case drive_System_driving__driving_driving_forward_in_station:
			exitSequence_Drive_System_driving__driving_driving_forward_in_station();
			break;
		case drive_System_driving__driving_turning_right_in_station:
			exitSequence_Drive_System_driving__driving_turning_right_in_station();
			break;
		case drive_System_driving__driving_turning_left_in_station:
			exitSequence_Drive_System_driving__driving_turning_left_in_station();
			break;
		case drive_System_driving__driving_exiting_charging_position__inStationExitCharger_right1:
			exitSequence_Drive_System_driving__driving_exiting_charging_position__inStationExitCharger_right1();
			break;
		case drive_System_driving__driving_exiting_charging_position__inStationExitCharger_right2:
			exitSequence_Drive_System_driving__driving_exiting_charging_position__inStationExitCharger_right2();
			break;
		case drive_System_driving__driving_exiting_charging_position__inStationExitCharger_forward:
			exitSequence_Drive_System_driving__driving_exiting_charging_position__inStationExitCharger_forward();
			break;
		case drive_System_driving__driving_exiting_charging_position__inStationExitCharger_right3:
			exitSequence_Drive_System_driving__driving_exiting_charging_position__inStationExitCharger_right3();
			break;
		case drive_System_driving__driving_entering_crossroad:
			exitSequence_Drive_System_driving__driving_entering_crossroad();
			break;
		case drive_System_driving__driving_leaving_crossroad_ahead__onCrossroadDriveForward_forward1:
			exitSequence_Drive_System_driving__driving_leaving_crossroad_ahead__onCrossroadDriveForward_forward1();
			break;
		case drive_System_driving__driving_leaving_crossroad_ahead__onCrossroadDriveForward_forward2:
			exitSequence_Drive_System_driving__driving_leaving_crossroad_ahead__onCrossroadDriveForward_forward2();
			break;
		case drive_System_driving__driving_leaving_crossroad_to_right_side__onCrossroadDriveRight_right:
			exitSequence_Drive_System_driving__driving_leaving_crossroad_to_right_side__onCrossroadDriveRight_right();
			break;
		case drive_System_driving__driving_leaving_crossroad_to_right_side__onCrossroadDriveRight_forward:
			exitSequence_Drive_System_driving__driving_leaving_crossroad_to_right_side__onCrossroadDriveRight_forward();
			break;
		case drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward1:
			exitSequence_Drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward1();
			break;
		case drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_left:
			exitSequence_Drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_left();
			break;
		case drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward2:
			exitSequence_Drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward2();
			break;
		case drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward3:
			exitSequence_Drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward3();
			break;
		case drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_1:
			exitSequence_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_1();
			break;
		case drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_1:
			exitSequence_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_1();
			break;
		case drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_1:
			exitSequence_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_1();
			break;
		case drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_2:
			exitSequence_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_2();
			break;
		case drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_2:
			exitSequence_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_2();
			break;
		case drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_2:
			exitSequence_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_2();
			break;
		default:
			break;
		}
	}
	
	/* Default exit sequence for region _driving */
	private void exitSequence_Drive_System_driving__driving() {
		switch (stateVector[0]) {
		case drive_System_driving__driving_on_waypoint_or_station:
			exitSequence_Drive_System_driving__driving_on_waypoint_or_station();
			break;
		case drive_System_driving__driving_waiting_in_station:
			exitSequence_Drive_System_driving__driving_waiting_in_station();
			break;
		case drive_System_driving__driving_waiting_on_waypoint:
			exitSequence_Drive_System_driving__driving_waiting_on_waypoint();
			break;
		case drive_System_driving__driving_waiting_to_ensure_real_deadlock:
			exitSequence_Drive_System_driving__driving_waiting_to_ensure_real_deadlock();
			break;
		case drive_System_driving__driving_on_crossroad:
			exitSequence_Drive_System_driving__driving_on_crossroad();
			break;
		case drive_System_driving__driving_driving_forward_in_station:
			exitSequence_Drive_System_driving__driving_driving_forward_in_station();
			break;
		case drive_System_driving__driving_turning_right_in_station:
			exitSequence_Drive_System_driving__driving_turning_right_in_station();
			break;
		case drive_System_driving__driving_turning_left_in_station:
			exitSequence_Drive_System_driving__driving_turning_left_in_station();
			break;
		case drive_System_driving__driving_exiting_charging_position__inStationExitCharger_right1:
			exitSequence_Drive_System_driving__driving_exiting_charging_position__inStationExitCharger_right1();
			break;
		case drive_System_driving__driving_exiting_charging_position__inStationExitCharger_right2:
			exitSequence_Drive_System_driving__driving_exiting_charging_position__inStationExitCharger_right2();
			break;
		case drive_System_driving__driving_exiting_charging_position__inStationExitCharger_forward:
			exitSequence_Drive_System_driving__driving_exiting_charging_position__inStationExitCharger_forward();
			break;
		case drive_System_driving__driving_exiting_charging_position__inStationExitCharger_right3:
			exitSequence_Drive_System_driving__driving_exiting_charging_position__inStationExitCharger_right3();
			break;
		case drive_System_driving__driving_entering_crossroad:
			exitSequence_Drive_System_driving__driving_entering_crossroad();
			break;
		case drive_System_driving__driving_leaving_crossroad_ahead__onCrossroadDriveForward_forward1:
			exitSequence_Drive_System_driving__driving_leaving_crossroad_ahead__onCrossroadDriveForward_forward1();
			break;
		case drive_System_driving__driving_leaving_crossroad_ahead__onCrossroadDriveForward_forward2:
			exitSequence_Drive_System_driving__driving_leaving_crossroad_ahead__onCrossroadDriveForward_forward2();
			break;
		case drive_System_driving__driving_leaving_crossroad_to_right_side__onCrossroadDriveRight_right:
			exitSequence_Drive_System_driving__driving_leaving_crossroad_to_right_side__onCrossroadDriveRight_right();
			break;
		case drive_System_driving__driving_leaving_crossroad_to_right_side__onCrossroadDriveRight_forward:
			exitSequence_Drive_System_driving__driving_leaving_crossroad_to_right_side__onCrossroadDriveRight_forward();
			break;
		case drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward1:
			exitSequence_Drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward1();
			break;
		case drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_left:
			exitSequence_Drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_left();
			break;
		case drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward2:
			exitSequence_Drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward2();
			break;
		case drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward3:
			exitSequence_Drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward3();
			break;
		case drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_1:
			exitSequence_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_1();
			break;
		case drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_1:
			exitSequence_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_1();
			break;
		case drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_1:
			exitSequence_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_1();
			break;
		case drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_2:
			exitSequence_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_2();
			break;
		case drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_2:
			exitSequence_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_2();
			break;
		case drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_2:
			exitSequence_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_2();
			break;
		default:
			break;
		}
	}
	
	/* Default exit sequence for region _inStationExitCharger */
	private void exitSequence_Drive_System_driving__driving_exiting_charging_position__inStationExitCharger() {
		switch (stateVector[0]) {
		case drive_System_driving__driving_exiting_charging_position__inStationExitCharger_right1:
			exitSequence_Drive_System_driving__driving_exiting_charging_position__inStationExitCharger_right1();
			break;
		case drive_System_driving__driving_exiting_charging_position__inStationExitCharger_right2:
			exitSequence_Drive_System_driving__driving_exiting_charging_position__inStationExitCharger_right2();
			break;
		case drive_System_driving__driving_exiting_charging_position__inStationExitCharger_forward:
			exitSequence_Drive_System_driving__driving_exiting_charging_position__inStationExitCharger_forward();
			break;
		case drive_System_driving__driving_exiting_charging_position__inStationExitCharger_right3:
			exitSequence_Drive_System_driving__driving_exiting_charging_position__inStationExitCharger_right3();
			break;
		default:
			break;
		}
	}
	
	/* Default exit sequence for region _onCrossroadDriveForward */
	private void exitSequence_Drive_System_driving__driving_leaving_crossroad_ahead__onCrossroadDriveForward() {
		switch (stateVector[0]) {
		case drive_System_driving__driving_leaving_crossroad_ahead__onCrossroadDriveForward_forward1:
			exitSequence_Drive_System_driving__driving_leaving_crossroad_ahead__onCrossroadDriveForward_forward1();
			break;
		case drive_System_driving__driving_leaving_crossroad_ahead__onCrossroadDriveForward_forward2:
			exitSequence_Drive_System_driving__driving_leaving_crossroad_ahead__onCrossroadDriveForward_forward2();
			break;
		default:
			break;
		}
	}
	
	/* Default exit sequence for region _onCrossroadDriveRight */
	private void exitSequence_Drive_System_driving__driving_leaving_crossroad_to_right_side__onCrossroadDriveRight() {
		switch (stateVector[0]) {
		case drive_System_driving__driving_leaving_crossroad_to_right_side__onCrossroadDriveRight_right:
			exitSequence_Drive_System_driving__driving_leaving_crossroad_to_right_side__onCrossroadDriveRight_right();
			break;
		case drive_System_driving__driving_leaving_crossroad_to_right_side__onCrossroadDriveRight_forward:
			exitSequence_Drive_System_driving__driving_leaving_crossroad_to_right_side__onCrossroadDriveRight_forward();
			break;
		default:
			break;
		}
	}
	
	/* Default exit sequence for region _onCrossroadDriveLeft */
	private void exitSequence_Drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft() {
		switch (stateVector[0]) {
		case drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward1:
			exitSequence_Drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward1();
			break;
		case drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_left:
			exitSequence_Drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_left();
			break;
		case drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward2:
			exitSequence_Drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward2();
			break;
		case drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward3:
			exitSequence_Drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward3();
			break;
		default:
			break;
		}
	}
	
	/* Default exit sequence for region _turningAroundOnCrossroad */
	private void exitSequence_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad() {
		switch (stateVector[0]) {
		case drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_1:
			exitSequence_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_1();
			break;
		case drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_1:
			exitSequence_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_1();
			break;
		case drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_1:
			exitSequence_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_1();
			break;
		case drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_2:
			exitSequence_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_2();
			break;
		case drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_2:
			exitSequence_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_2();
			break;
		case drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_2:
			exitSequence_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_2();
			break;
		default:
			break;
		}
	}
	
	/* The reactions of state null. */
	private void react_Drive_System_driving__driving__choice_0() {
		if (check_Drive_System_driving__driving__choice_0_tr0_tr0()) {
			effect_Drive_System_driving__driving__choice_0_tr0();
		} else {
			if (check_Drive_System_driving__driving__choice_0_tr1_tr1()) {
				effect_Drive_System_driving__driving__choice_0_tr1();
			} else {
				if (check_Drive_System_driving__driving__choice_0_tr2_tr2()) {
					effect_Drive_System_driving__driving__choice_0_tr2();
				} else {
					if (check_Drive_System_driving__driving__choice_0_tr3_tr3()) {
						effect_Drive_System_driving__driving__choice_0_tr3();
					} else {
						effect_Drive_System_driving__driving__choice_0_tr4();
					}
				}
			}
		}
	}
	
	/* The reactions of state null. */
	private void react_Drive_System_driving__driving__choice_1() {
		if (check_Drive_System_driving__driving__choice_1_tr0_tr0()) {
			effect_Drive_System_driving__driving__choice_1_tr0();
		} else {
			if (check_Drive_System_driving__driving__choice_1_tr1_tr1()) {
				effect_Drive_System_driving__driving__choice_1_tr1();
			} else {
				if (check_Drive_System_driving__driving__choice_1_tr2_tr2()) {
					effect_Drive_System_driving__driving__choice_1_tr2();
				} else {
					if (check_Drive_System_driving__driving__choice_1_tr3_tr3()) {
						effect_Drive_System_driving__driving__choice_1_tr3();
					} else {
						effect_Drive_System_driving__driving__choice_1_tr4();
					}
				}
			}
		}
	}
	
	/* The reactions of state null. */
	private void react_Drive_System_driving__driving__choice_2() {
		if (check_Drive_System_driving__driving__choice_2_tr1_tr1()) {
			effect_Drive_System_driving__driving__choice_2_tr1();
		} else {
			effect_Drive_System_driving__driving__choice_2_tr0();
		}
	}
	
	/* The reactions of state null. */
	private void react_Drive_System_driving__driving__choice_3() {
		if (check_Drive_System_driving__driving__choice_3_tr1_tr1()) {
			effect_Drive_System_driving__driving__choice_3_tr1();
		} else {
			if (check_Drive_System_driving__driving__choice_3_tr2_tr2()) {
				effect_Drive_System_driving__driving__choice_3_tr2();
			} else {
				effect_Drive_System_driving__driving__choice_3_tr0();
			}
		}
	}
	
	/* The reactions of state null. */
	private void react_Drive_System_driving__driving__choice_4() {
		effect_Drive_System_driving__driving__choice_4_tr0();
	}
	
	/* The reactions of state null. */
	private void react_Drive_System_driving__driving__choice_5() {
		if (check_Drive_System_driving__driving__choice_5_tr0_tr0()) {
			effect_Drive_System_driving__driving__choice_5_tr0();
		} else {
			effect_Drive_System_driving__driving__choice_5_tr1();
		}
	}
	
	/* The reactions of state null. */
	private void react_Drive_System_driving__driving__choice_6() {
		if (check_Drive_System_driving__driving__choice_6_tr0_tr0()) {
			effect_Drive_System_driving__driving__choice_6_tr0();
		} else {
			effect_Drive_System_driving__driving__choice_6_tr1();
		}
	}
	
	/* The reactions of state null. */
	private void react_Drive_System_driving__driving__choice_7() {
		effect_Drive_System_driving__driving__choice_7_tr0();
	}
	
	/* The reactions of state null. */
	private void react_Drive_System_driving__driving__choice_8() {
		if (check_Drive_System_driving__driving__choice_8_tr0_tr0()) {
			effect_Drive_System_driving__driving__choice_8_tr0();
		} else {
			effect_Drive_System_driving__driving__choice_8_tr1();
		}
	}
	
	/* The reactions of state null. */
	private void react_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad__choice_0() {
		if (check_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad__choice_0_tr0_tr0()) {
			effect_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad__choice_0_tr0();
		} else {
			effect_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad__choice_0_tr1();
		}
	}
	
	/* The reactions of state null. */
	private void react_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad__choice_1() {
		if (check_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad__choice_1_tr0_tr0()) {
			effect_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad__choice_1_tr0();
		} else {
			effect_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad__choice_1_tr1();
		}
	}
	
	/* The reactions of state null. */
	private void react_Drive_System_driving__driving__choice_9() {
		if (check_Drive_System_driving__driving__choice_9_tr0_tr0()) {
			effect_Drive_System_driving__driving__choice_9_tr0();
		} else {
			if (check_Drive_System_driving__driving__choice_9_tr1_tr1()) {
				effect_Drive_System_driving__driving__choice_9_tr1();
			} else {
				effect_Drive_System_driving__driving__choice_9_tr2();
			}
		}
	}
	
	/* Default react sequence for initial entry  */
	private void react_Drive_System__entry_Default() {
		enterSequence_Drive_System_idle_default();
	}
	
	/* Default react sequence for initial entry  */
	private void react_Drive_System_driving__driving__entry_Default() {
		enterSequence_Drive_System_driving__driving_on_waypoint_or_station_default();
	}
	
	/* Default react sequence for initial entry  */
	private void react_Drive_System_driving__driving_exiting_charging_position__inStationExitCharger__entry_Default() {
		enterSequence_Drive_System_driving__driving_exiting_charging_position__inStationExitCharger_right1_default();
	}
	
	/* Default react sequence for initial entry  */
	private void react_Drive_System_driving__driving_leaving_crossroad_ahead__onCrossroadDriveForward__entry_Default() {
		enterSequence_Drive_System_driving__driving_leaving_crossroad_ahead__onCrossroadDriveForward_forward1_default();
	}
	
	/* Default react sequence for initial entry  */
	private void react_Drive_System_driving__driving_leaving_crossroad_to_right_side__onCrossroadDriveRight__entry_Default() {
		enterSequence_Drive_System_driving__driving_leaving_crossroad_to_right_side__onCrossroadDriveRight_right_default();
	}
	
	/* Default react sequence for initial entry  */
	private void react_Drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft__entry_Default() {
		enterSequence_Drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward1_default();
	}
	
	/* Default react sequence for initial entry  */
	private void react_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad__entry_Default() {
		enterSequence_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_1_default();
	}
	
	/* The reactions of exit default. */
	private void react_Drive_System_driving__driving_exiting_charging_position__inStationExitCharger__exit_Default() {
		effect_Drive_System_driving__driving_exiting_charging_position_tr0();
	}
	
	/* The reactions of exit default. */
	private void react_Drive_System_driving__driving_leaving_crossroad_ahead__onCrossroadDriveForward__exit_Default() {
		effect_Drive_System_driving__driving_leaving_crossroad_ahead_tr0();
	}
	
	/* The reactions of exit default. */
	private void react_Drive_System_driving__driving_leaving_crossroad_to_right_side__onCrossroadDriveRight__exit_Default() {
		effect_Drive_System_driving__driving_leaving_crossroad_to_right_side_tr0();
	}
	
	/* The reactions of exit default. */
	private void react_Drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft__exit_Default() {
		effect_Drive_System_driving__driving_leaving_crossroad_to_left_side_tr0();
	}
	
	/* The reactions of exit default. */
	private void react_Drive_System_driving__driving__exit_Default() {
		effect_Drive_System_driving_tr1();
	}
	
	/* The reactions of exit default. */
	private void react_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad__exit_Default() {
		effect_Drive_System_driving__driving_turning_around_on_crossroad_tr0();
	}
	
	private boolean react() {
		return false;
	}
	
	private boolean drive_System_idle_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (sCInterface.unload) {
				exitSequence_Drive_System_idle();
				sCIActors.raiseStartUnload();
				
				enterSequence_Drive_System_unloading_default();
				react();
			} else {
				if (sCInterface.newTarget) {
					exitSequence_Drive_System_idle();
					enterSequence_Drive_System_driving_default();
					react();
				} else {
					did_transition = false;
				}
			}
		}
		if (did_transition==false) {
			did_transition = react();
		}
		return did_transition;
	}
	
	private boolean drive_System_unloading_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (sCInterface.unloaded) {
				exitSequence_Drive_System_unloading();
				sCIProcessor.raiseUnloaded();
				
				enterSequence_Drive_System_idle_default();
				react();
			} else {
				if (sCInterface.stop) {
					exitSequence_Drive_System_unloading();
					enterSequence_Drive_System_idle_default();
					react();
				} else {
					did_transition = false;
				}
			}
		}
		if (did_transition==false) {
			did_transition = react();
		}
		return did_transition;
	}
	
	private boolean drive_System_driving_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (sCInterface.stop) {
				exitSequence_Drive_System_driving();
				enterSequence_Drive_System_idle_default();
				react();
			} else {
				did_transition = false;
			}
		}
		if (did_transition==false) {
			did_transition = react();
		}
		return did_transition;
	}
	
	private boolean drive_System_driving__driving_on_waypoint_or_station_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (sCInterface.dataRefresh) {
				exitSequence_Drive_System_driving__driving_on_waypoint_or_station();
				react_Drive_System_driving__driving__choice_5();
			} else {
				did_transition = false;
			}
		}
		if (did_transition==false) {
			did_transition = drive_System_driving_react(try_transition);
		}
		return did_transition;
	}
	
	private boolean drive_System_driving__driving_waiting_in_station_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (sCInterface.dataRefresh) {
				exitSequence_Drive_System_driving__driving_waiting_in_station();
				react_Drive_System_driving__driving__choice_8();
			} else {
				did_transition = false;
			}
		}
		if (did_transition==false) {
			did_transition = drive_System_driving_react(try_transition);
		}
		return did_transition;
	}
	
	private boolean drive_System_driving__driving_waiting_on_waypoint_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (sCInterface.dataRefresh) {
				exitSequence_Drive_System_driving__driving_waiting_on_waypoint();
				react_Drive_System_driving__driving__choice_4();
			} else {
				did_transition = false;
			}
		}
		if (did_transition==false) {
			did_transition = drive_System_driving_react(try_transition);
		}
		return did_transition;
	}
	
	private boolean drive_System_driving__driving_waiting_to_ensure_real_deadlock_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (timeEvents[0]) {
				exitSequence_Drive_System_driving__driving_waiting_to_ensure_real_deadlock();
				setWaitedForDeadlock(getWaitedForDeadlock() + 1);
				
				enterSequence_Drive_System_driving__driving_waiting_on_waypoint_default();
				drive_System_driving_react(false);
			} else {
				did_transition = false;
			}
		}
		if (did_transition==false) {
			did_transition = drive_System_driving_react(try_transition);
		}
		return did_transition;
	}
	
	private boolean drive_System_driving__driving_on_crossroad_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (sCInterface.dataRefresh) {
				exitSequence_Drive_System_driving__driving_on_crossroad();
				react_Drive_System_driving__driving__choice_1();
			} else {
				did_transition = false;
			}
		}
		if (did_transition==false) {
			did_transition = drive_System_driving_react(try_transition);
		}
		return did_transition;
	}
	
	private boolean drive_System_driving__driving_driving_forward_in_station_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (sCInterface.actionCompleted) {
				exitSequence_Drive_System_driving__driving_driving_forward_in_station();
				react_Drive_System_driving__driving__choice_7();
			} else {
				did_transition = false;
			}
		}
		if (did_transition==false) {
			did_transition = drive_System_driving_react(try_transition);
		}
		return did_transition;
	}
	
	private boolean drive_System_driving__driving_turning_right_in_station_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (sCInterface.actionCompleted) {
				exitSequence_Drive_System_driving__driving_turning_right_in_station();
				react_Drive_System_driving__driving__choice_7();
			} else {
				did_transition = false;
			}
		}
		if (did_transition==false) {
			did_transition = drive_System_driving_react(try_transition);
		}
		return did_transition;
	}
	
	private boolean drive_System_driving__driving_turning_left_in_station_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (sCInterface.actionCompleted) {
				exitSequence_Drive_System_driving__driving_turning_left_in_station();
				react_Drive_System_driving__driving__choice_7();
			} else {
				did_transition = false;
			}
		}
		if (did_transition==false) {
			did_transition = drive_System_driving_react(try_transition);
		}
		return did_transition;
	}
	
	private boolean drive_System_driving__driving_exiting_charging_position_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			did_transition = false;
		}
		if (did_transition==false) {
			did_transition = drive_System_driving_react(try_transition);
		}
		return did_transition;
	}
	
	private boolean drive_System_driving__driving_exiting_charging_position__inStationExitCharger_right1_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (sCInterface.actionCompleted) {
				exitSequence_Drive_System_driving__driving_exiting_charging_position__inStationExitCharger_right1();
				enterSequence_Drive_System_driving__driving_exiting_charging_position__inStationExitCharger_right2_default();
				drive_System_driving__driving_exiting_charging_position_react(false);
			} else {
				did_transition = false;
			}
		}
		if (did_transition==false) {
			did_transition = drive_System_driving__driving_exiting_charging_position_react(try_transition);
		}
		return did_transition;
	}
	
	private boolean drive_System_driving__driving_exiting_charging_position__inStationExitCharger_right2_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (sCInterface.actionCompleted) {
				exitSequence_Drive_System_driving__driving_exiting_charging_position__inStationExitCharger_right2();
				enterSequence_Drive_System_driving__driving_exiting_charging_position__inStationExitCharger_forward_default();
				drive_System_driving__driving_exiting_charging_position_react(false);
			} else {
				did_transition = false;
			}
		}
		if (did_transition==false) {
			did_transition = drive_System_driving__driving_exiting_charging_position_react(try_transition);
		}
		return did_transition;
	}
	
	private boolean drive_System_driving__driving_exiting_charging_position__inStationExitCharger_forward_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (sCInterface.actionCompleted) {
				exitSequence_Drive_System_driving__driving_exiting_charging_position__inStationExitCharger_forward();
				enterSequence_Drive_System_driving__driving_exiting_charging_position__inStationExitCharger_right3_default();
				drive_System_driving__driving_exiting_charging_position_react(false);
			} else {
				did_transition = false;
			}
		}
		if (did_transition==false) {
			did_transition = drive_System_driving__driving_exiting_charging_position_react(try_transition);
		}
		return did_transition;
	}
	
	private boolean drive_System_driving__driving_exiting_charging_position__inStationExitCharger_right3_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (sCInterface.actionCompleted) {
				exitSequence_Drive_System_driving__driving_exiting_charging_position__inStationExitCharger_right3();
				react_Drive_System_driving__driving_exiting_charging_position__inStationExitCharger__exit_Default();
			} else {
				did_transition = false;
			}
		}
		if (did_transition==false) {
			did_transition = drive_System_driving__driving_exiting_charging_position_react(try_transition);
		}
		return did_transition;
	}
	
	private boolean drive_System_driving__driving_entering_crossroad_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (sCInterface.actionCompleted) {
				exitSequence_Drive_System_driving__driving_entering_crossroad();
				enterSequence_Drive_System_driving__driving_on_crossroad_default();
				drive_System_driving_react(false);
			} else {
				did_transition = false;
			}
		}
		if (did_transition==false) {
			did_transition = drive_System_driving_react(try_transition);
		}
		return did_transition;
	}
	
	private boolean drive_System_driving__driving_leaving_crossroad_ahead_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			did_transition = false;
		}
		if (did_transition==false) {
			did_transition = drive_System_driving_react(try_transition);
		}
		return did_transition;
	}
	
	private boolean drive_System_driving__driving_leaving_crossroad_ahead__onCrossroadDriveForward_forward1_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (sCInterface.actionCompleted) {
				exitSequence_Drive_System_driving__driving_leaving_crossroad_ahead__onCrossroadDriveForward_forward1();
				enterSequence_Drive_System_driving__driving_leaving_crossroad_ahead__onCrossroadDriveForward_forward2_default();
				drive_System_driving__driving_leaving_crossroad_ahead_react(false);
			} else {
				did_transition = false;
			}
		}
		if (did_transition==false) {
			did_transition = drive_System_driving__driving_leaving_crossroad_ahead_react(try_transition);
		}
		return did_transition;
	}
	
	private boolean drive_System_driving__driving_leaving_crossroad_ahead__onCrossroadDriveForward_forward2_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (sCInterface.actionCompleted) {
				exitSequence_Drive_System_driving__driving_leaving_crossroad_ahead__onCrossroadDriveForward_forward2();
				react_Drive_System_driving__driving_leaving_crossroad_ahead__onCrossroadDriveForward__exit_Default();
			} else {
				did_transition = false;
			}
		}
		if (did_transition==false) {
			did_transition = drive_System_driving__driving_leaving_crossroad_ahead_react(try_transition);
		}
		return did_transition;
	}
	
	private boolean drive_System_driving__driving_leaving_crossroad_to_right_side_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			did_transition = false;
		}
		if (did_transition==false) {
			did_transition = drive_System_driving_react(try_transition);
		}
		return did_transition;
	}
	
	private boolean drive_System_driving__driving_leaving_crossroad_to_right_side__onCrossroadDriveRight_right_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (sCInterface.actionCompleted) {
				exitSequence_Drive_System_driving__driving_leaving_crossroad_to_right_side__onCrossroadDriveRight_right();
				enterSequence_Drive_System_driving__driving_leaving_crossroad_to_right_side__onCrossroadDriveRight_forward_default();
				drive_System_driving__driving_leaving_crossroad_to_right_side_react(false);
			} else {
				did_transition = false;
			}
		}
		if (did_transition==false) {
			did_transition = drive_System_driving__driving_leaving_crossroad_to_right_side_react(try_transition);
		}
		return did_transition;
	}
	
	private boolean drive_System_driving__driving_leaving_crossroad_to_right_side__onCrossroadDriveRight_forward_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (sCInterface.actionCompleted) {
				exitSequence_Drive_System_driving__driving_leaving_crossroad_to_right_side__onCrossroadDriveRight_forward();
				react_Drive_System_driving__driving_leaving_crossroad_to_right_side__onCrossroadDriveRight__exit_Default();
			} else {
				did_transition = false;
			}
		}
		if (did_transition==false) {
			did_transition = drive_System_driving__driving_leaving_crossroad_to_right_side_react(try_transition);
		}
		return did_transition;
	}
	
	private boolean drive_System_driving__driving_leaving_crossroad_to_left_side_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			did_transition = false;
		}
		if (did_transition==false) {
			did_transition = drive_System_driving_react(try_transition);
		}
		return did_transition;
	}
	
	private boolean drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward1_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (sCInterface.actionCompleted) {
				exitSequence_Drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward1();
				enterSequence_Drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_left_default();
				drive_System_driving__driving_leaving_crossroad_to_left_side_react(false);
			} else {
				did_transition = false;
			}
		}
		if (did_transition==false) {
			did_transition = drive_System_driving__driving_leaving_crossroad_to_left_side_react(try_transition);
		}
		return did_transition;
	}
	
	private boolean drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_left_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (sCInterface.actionCompleted) {
				exitSequence_Drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_left();
				enterSequence_Drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward2_default();
				drive_System_driving__driving_leaving_crossroad_to_left_side_react(false);
			} else {
				did_transition = false;
			}
		}
		if (did_transition==false) {
			did_transition = drive_System_driving__driving_leaving_crossroad_to_left_side_react(try_transition);
		}
		return did_transition;
	}
	
	private boolean drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward2_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (sCInterface.actionCompleted) {
				exitSequence_Drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward2();
				enterSequence_Drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward3_default();
				drive_System_driving__driving_leaving_crossroad_to_left_side_react(false);
			} else {
				did_transition = false;
			}
		}
		if (did_transition==false) {
			did_transition = drive_System_driving__driving_leaving_crossroad_to_left_side_react(try_transition);
		}
		return did_transition;
	}
	
	private boolean drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward3_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (sCInterface.actionCompleted) {
				exitSequence_Drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward3();
				react_Drive_System_driving__driving_leaving_crossroad_to_left_side__onCrossroadDriveLeft__exit_Default();
			} else {
				did_transition = false;
			}
		}
		if (did_transition==false) {
			did_transition = drive_System_driving__driving_leaving_crossroad_to_left_side_react(try_transition);
		}
		return did_transition;
	}
	
	private boolean drive_System_driving__driving_turning_around_on_crossroad_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			did_transition = false;
		}
		if (did_transition==false) {
			did_transition = drive_System_driving_react(try_transition);
		}
		return did_transition;
	}
	
	private boolean drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_1_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (sCInterface.actionCompleted) {
				exitSequence_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_1();
				enterSequence_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_1_default();
				drive_System_driving__driving_turning_around_on_crossroad_react(false);
			} else {
				did_transition = false;
			}
		}
		if (did_transition==false) {
			did_transition = drive_System_driving__driving_turning_around_on_crossroad_react(try_transition);
		}
		return did_transition;
	}
	
	private boolean drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_1_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (sCInterface.dataRefresh) {
				exitSequence_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_1();
				react_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad__choice_0();
			} else {
				did_transition = false;
			}
		}
		if (did_transition==false) {
			did_transition = drive_System_driving__driving_turning_around_on_crossroad_react(try_transition);
		}
		return did_transition;
	}
	
	private boolean drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_1_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (sCInterface.actionCompleted) {
				exitSequence_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_1();
				enterSequence_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_2_default();
				drive_System_driving__driving_turning_around_on_crossroad_react(false);
			} else {
				did_transition = false;
			}
		}
		if (did_transition==false) {
			did_transition = drive_System_driving__driving_turning_around_on_crossroad_react(try_transition);
		}
		return did_transition;
	}
	
	private boolean drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_2_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (sCInterface.actionCompleted) {
				exitSequence_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_2();
				enterSequence_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_2_default();
				drive_System_driving__driving_turning_around_on_crossroad_react(false);
			} else {
				did_transition = false;
			}
		}
		if (did_transition==false) {
			did_transition = drive_System_driving__driving_turning_around_on_crossroad_react(try_transition);
		}
		return did_transition;
	}
	
	private boolean drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_2_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (sCInterface.dataRefresh) {
				exitSequence_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_2();
				react_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad__choice_1();
			} else {
				did_transition = false;
			}
		}
		if (did_transition==false) {
			did_transition = drive_System_driving__driving_turning_around_on_crossroad_react(try_transition);
		}
		return did_transition;
	}
	
	private boolean drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_2_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (sCInterface.actionCompleted) {
				exitSequence_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_2();
				react_Drive_System_driving__driving_turning_around_on_crossroad__turningAroundOnCrossroad__exit_Default();
			} else {
				did_transition = false;
			}
		}
		if (did_transition==false) {
			did_transition = drive_System_driving__driving_turning_around_on_crossroad_react(try_transition);
		}
		return did_transition;
	}
	
}
