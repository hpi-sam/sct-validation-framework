package de.hpi.mod.sim.drivesystem;

import de.hpi.mod.sim.ITimer;

public class DrivesystemStatemachine implements IDrivesystemStatemachine {
	protected class SCInterfaceImpl implements SCInterface {
	
		private boolean unload;
		
		public void raiseUnload() {
			unload = true;
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
		
		private boolean newUnloadingTarget;
		
		public void raiseNewUnloadingTarget() {
			newUnloadingTarget = true;
			runCycle();
		}
		
		private boolean newChargingTarget;
		
		public void raiseNewChargingTarget() {
			newChargingTarget = true;
			runCycle();
		}
		
		private boolean actionCompleted;
		
		public void raiseActionCompleted() {
			actionCompleted = true;
			runCycle();
		}
		
		protected void clearEvents() {
			unload = false;
			stop = false;
			dataRefresh = false;
			newTarget = false;
			newUnloadingTarget = false;
			newChargingTarget = false;
			actionCompleted = false;
		}
	}
	
	protected SCInterfaceImpl sCInterface;
	
	protected class SCIProcessorImpl implements SCIProcessor {
	
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
		
		private boolean driveBackward;
		
		public boolean isRaisedDriveBackward() {
			return driveBackward;
		}
		
		protected void raiseDriveBackward() {
			driveBackward = true;
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
		driveBackward = false;
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
	
	protected class SCIDriveModeImpl implements SCIDriveMode {
	
		private long uNSET;
		
		public long getUNSET() {
			return uNSET;
		}
		
		public void setUNSET(long value) {
			this.uNSET = value;
		}
		
		private long dRIVE;
		
		public long getDRIVE() {
			return dRIVE;
		}
		
		public void setDRIVE(long value) {
			this.dRIVE = value;
		}
		
		private long cHARGE;
		
		public long getCHARGE() {
			return cHARGE;
		}
		
		public void setCHARGE(long value) {
			this.cHARGE = value;
		}
		
		private long uNLOAD;
		
		public long getUNLOAD() {
			return uNLOAD;
		}
		
		public void setUNLOAD(long value) {
			this.uNLOAD = value;
		}
		
	}
	
	protected SCIDriveModeImpl sCIDriveMode;
	
	private boolean initialized = false;
	
	public enum State {
		drive_System_idle,
		drive_System_waiting_on_waypoint_or_station,
		drive_System_leaving_corassroad,
		drive_System_leaving_corassroad__onCrossroadExiting_waiting,
		drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_ahead,
		drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_ahead__onCrossroadDriveForward_forward1,
		drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_ahead__onCrossroadDriveForward_forward2,
		drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_right_side,
		drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_right_side__onCrossroadDriveRight_right,
		drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_right_side__onCrossroadDriveRight_forward,
		drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side,
		drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward1,
		drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_left,
		drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward2,
		drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward3,
		drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad,
		drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_1,
		drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_1,
		drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_1,
		drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_2,
		drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_2,
		drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_2,
		drive_System_entering_crossroad,
		drive_System_entering_crossroad__onWaypointEnteringCrossroad_waiting_on_waypoint,
		drive_System_entering_crossroad__onWaypointEnteringCrossroad_waiting_to_ensure_real_deadlock,
		drive_System_entering_crossroad__onWaypointEnteringCrossroad_entering_crossroad,
		drive_System_driving_in_station,
		drive_System_driving_in_station__inStation_turn_right,
		drive_System_driving_in_station__inStation_turn_left,
		drive_System_driving_in_station__inStation_drive,
		drive_System_driving_in_station__inStation_wait_to_drive,
		drive_System_unloading,
		drive_System_unloading__unloading_unloading,
		drive_System_unloading__unloading_turning,
		drive_System_unloading__unloading_waiting_for_data,
		drive_System_entering_charger,
		drive_System_entering_charger__startCharging_unloading,
		drive_System_entering_charger__startCharging_turning,
		drive_System_entering_charger__startCharging_waiting_for_data,
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
	
	private long driveMode;
	
	protected void setDriveMode(long value) {
		driveMode = value;
	}
	
	protected long getDriveMode() {
		return driveMode;
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
		sCIDriveMode = new SCIDriveModeImpl();
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
		
		setDriveMode(0);
		
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
		
		sCIDriveMode.setUNSET(0);
		
		sCIDriveMode.setDRIVE(1);
		
		sCIDriveMode.setCHARGE(2);
		
		sCIDriveMode.setUNLOAD(3);
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
			case drive_System_waiting_on_waypoint_or_station:
				drive_System_waiting_on_waypoint_or_station_react(true);
				break;
			case drive_System_leaving_corassroad__onCrossroadExiting_waiting:
				drive_System_leaving_corassroad__onCrossroadExiting_waiting_react(true);
				break;
			case drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_ahead__onCrossroadDriveForward_forward1:
				drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_ahead__onCrossroadDriveForward_forward1_react(true);
				break;
			case drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_ahead__onCrossroadDriveForward_forward2:
				drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_ahead__onCrossroadDriveForward_forward2_react(true);
				break;
			case drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_right_side__onCrossroadDriveRight_right:
				drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_right_side__onCrossroadDriveRight_right_react(true);
				break;
			case drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_right_side__onCrossroadDriveRight_forward:
				drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_right_side__onCrossroadDriveRight_forward_react(true);
				break;
			case drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward1:
				drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward1_react(true);
				break;
			case drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_left:
				drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_left_react(true);
				break;
			case drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward2:
				drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward2_react(true);
				break;
			case drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward3:
				drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward3_react(true);
				break;
			case drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_1:
				drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_1_react(true);
				break;
			case drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_1:
				drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_1_react(true);
				break;
			case drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_1:
				drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_1_react(true);
				break;
			case drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_2:
				drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_2_react(true);
				break;
			case drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_2:
				drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_2_react(true);
				break;
			case drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_2:
				drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_2_react(true);
				break;
			case drive_System_entering_crossroad__onWaypointEnteringCrossroad_waiting_on_waypoint:
				drive_System_entering_crossroad__onWaypointEnteringCrossroad_waiting_on_waypoint_react(true);
				break;
			case drive_System_entering_crossroad__onWaypointEnteringCrossroad_waiting_to_ensure_real_deadlock:
				drive_System_entering_crossroad__onWaypointEnteringCrossroad_waiting_to_ensure_real_deadlock_react(true);
				break;
			case drive_System_entering_crossroad__onWaypointEnteringCrossroad_entering_crossroad:
				drive_System_entering_crossroad__onWaypointEnteringCrossroad_entering_crossroad_react(true);
				break;
			case drive_System_driving_in_station__inStation_turn_right:
				drive_System_driving_in_station__inStation_turn_right_react(true);
				break;
			case drive_System_driving_in_station__inStation_turn_left:
				drive_System_driving_in_station__inStation_turn_left_react(true);
				break;
			case drive_System_driving_in_station__inStation_drive:
				drive_System_driving_in_station__inStation_drive_react(true);
				break;
			case drive_System_driving_in_station__inStation_wait_to_drive:
				drive_System_driving_in_station__inStation_wait_to_drive_react(true);
				break;
			case drive_System_unloading__unloading_unloading:
				drive_System_unloading__unloading_unloading_react(true);
				break;
			case drive_System_unloading__unloading_turning:
				drive_System_unloading__unloading_turning_react(true);
				break;
			case drive_System_unloading__unloading_waiting_for_data:
				drive_System_unloading__unloading_waiting_for_data_react(true);
				break;
			case drive_System_entering_charger__startCharging_unloading:
				drive_System_entering_charger__startCharging_unloading_react(true);
				break;
			case drive_System_entering_charger__startCharging_turning:
				drive_System_entering_charger__startCharging_turning_react(true);
				break;
			case drive_System_entering_charger__startCharging_waiting_for_data:
				drive_System_entering_charger__startCharging_waiting_for_data_react(true);
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
		case drive_System_waiting_on_waypoint_or_station:
			return stateVector[0] == State.drive_System_waiting_on_waypoint_or_station;
		case drive_System_leaving_corassroad:
			return stateVector[0].ordinal() >= State.
					drive_System_leaving_corassroad.ordinal()&& stateVector[0].ordinal() <= State.drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_2.ordinal();
		case drive_System_leaving_corassroad__onCrossroadExiting_waiting:
			return stateVector[0] == State.drive_System_leaving_corassroad__onCrossroadExiting_waiting;
		case drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_ahead:
			return stateVector[0].ordinal() >= State.
					drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_ahead.ordinal()&& stateVector[0].ordinal() <= State.drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_ahead__onCrossroadDriveForward_forward2.ordinal();
		case drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_ahead__onCrossroadDriveForward_forward1:
			return stateVector[0] == State.drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_ahead__onCrossroadDriveForward_forward1;
		case drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_ahead__onCrossroadDriveForward_forward2:
			return stateVector[0] == State.drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_ahead__onCrossroadDriveForward_forward2;
		case drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_right_side:
			return stateVector[0].ordinal() >= State.
					drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_right_side.ordinal()&& stateVector[0].ordinal() <= State.drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_right_side__onCrossroadDriveRight_forward.ordinal();
		case drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_right_side__onCrossroadDriveRight_right:
			return stateVector[0] == State.drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_right_side__onCrossroadDriveRight_right;
		case drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_right_side__onCrossroadDriveRight_forward:
			return stateVector[0] == State.drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_right_side__onCrossroadDriveRight_forward;
		case drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side:
			return stateVector[0].ordinal() >= State.
					drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side.ordinal()&& stateVector[0].ordinal() <= State.drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward3.ordinal();
		case drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward1:
			return stateVector[0] == State.drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward1;
		case drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_left:
			return stateVector[0] == State.drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_left;
		case drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward2:
			return stateVector[0] == State.drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward2;
		case drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward3:
			return stateVector[0] == State.drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward3;
		case drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad:
			return stateVector[0].ordinal() >= State.
					drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad.ordinal()&& stateVector[0].ordinal() <= State.drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_2.ordinal();
		case drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_1:
			return stateVector[0] == State.drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_1;
		case drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_1:
			return stateVector[0] == State.drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_1;
		case drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_1:
			return stateVector[0] == State.drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_1;
		case drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_2:
			return stateVector[0] == State.drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_2;
		case drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_2:
			return stateVector[0] == State.drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_2;
		case drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_2:
			return stateVector[0] == State.drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_2;
		case drive_System_entering_crossroad:
			return stateVector[0].ordinal() >= State.
					drive_System_entering_crossroad.ordinal()&& stateVector[0].ordinal() <= State.drive_System_entering_crossroad__onWaypointEnteringCrossroad_entering_crossroad.ordinal();
		case drive_System_entering_crossroad__onWaypointEnteringCrossroad_waiting_on_waypoint:
			return stateVector[0] == State.drive_System_entering_crossroad__onWaypointEnteringCrossroad_waiting_on_waypoint;
		case drive_System_entering_crossroad__onWaypointEnteringCrossroad_waiting_to_ensure_real_deadlock:
			return stateVector[0] == State.drive_System_entering_crossroad__onWaypointEnteringCrossroad_waiting_to_ensure_real_deadlock;
		case drive_System_entering_crossroad__onWaypointEnteringCrossroad_entering_crossroad:
			return stateVector[0] == State.drive_System_entering_crossroad__onWaypointEnteringCrossroad_entering_crossroad;
		case drive_System_driving_in_station:
			return stateVector[0].ordinal() >= State.
					drive_System_driving_in_station.ordinal()&& stateVector[0].ordinal() <= State.drive_System_driving_in_station__inStation_wait_to_drive.ordinal();
		case drive_System_driving_in_station__inStation_turn_right:
			return stateVector[0] == State.drive_System_driving_in_station__inStation_turn_right;
		case drive_System_driving_in_station__inStation_turn_left:
			return stateVector[0] == State.drive_System_driving_in_station__inStation_turn_left;
		case drive_System_driving_in_station__inStation_drive:
			return stateVector[0] == State.drive_System_driving_in_station__inStation_drive;
		case drive_System_driving_in_station__inStation_wait_to_drive:
			return stateVector[0] == State.drive_System_driving_in_station__inStation_wait_to_drive;
		case drive_System_unloading:
			return stateVector[0].ordinal() >= State.
					drive_System_unloading.ordinal()&& stateVector[0].ordinal() <= State.drive_System_unloading__unloading_waiting_for_data.ordinal();
		case drive_System_unloading__unloading_unloading:
			return stateVector[0] == State.drive_System_unloading__unloading_unloading;
		case drive_System_unloading__unloading_turning:
			return stateVector[0] == State.drive_System_unloading__unloading_turning;
		case drive_System_unloading__unloading_waiting_for_data:
			return stateVector[0] == State.drive_System_unloading__unloading_waiting_for_data;
		case drive_System_entering_charger:
			return stateVector[0].ordinal() >= State.
					drive_System_entering_charger.ordinal()&& stateVector[0].ordinal() <= State.drive_System_entering_charger__startCharging_waiting_for_data.ordinal();
		case drive_System_entering_charger__startCharging_unloading:
			return stateVector[0] == State.drive_System_entering_charger__startCharging_unloading;
		case drive_System_entering_charger__startCharging_turning:
			return stateVector[0] == State.drive_System_entering_charger__startCharging_turning;
		case drive_System_entering_charger__startCharging_waiting_for_data:
			return stateVector[0] == State.drive_System_entering_charger__startCharging_waiting_for_data;
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
	
	public SCIDriveMode getSCIDriveMode() {
		return sCIDriveMode;
	}
	
	public void raiseUnload() {
		sCInterface.raiseUnload();
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
	
	public void raiseNewUnloadingTarget() {
		sCInterface.raiseNewUnloadingTarget();
	}
	
	public void raiseNewChargingTarget() {
		sCInterface.raiseNewChargingTarget();
	}
	
	public void raiseActionCompleted() {
		sCInterface.raiseActionCompleted();
	}
	
	private boolean check_Drive_System__choice_0_tr0_tr0() {
		return (getDriveMode()!=sCIDriveMode.getUNLOAD() && sCIData.operationCallback.canUnloadToTarget());
	}
	
	private boolean check_Drive_System_leaving_corassroad__onCrossroadExiting__choice_0_tr0_tr0() {
		return sCIData.operationCallback.targetDirection()==sCIDirection.getBEHIND();
	}
	
	private boolean check_Drive_System_leaving_corassroad__onCrossroadExiting__choice_0_tr1_tr1() {
		return (sCIData.operationCallback.targetDirection()==sCIDirection.getRIGHT() && !sCIData.operationCallback.blockedWaypointRight());
	}
	
	private boolean check_Drive_System_leaving_corassroad__onCrossroadExiting__choice_0_tr2_tr2() {
		return ((sCIData.operationCallback.targetDirection()==sCIDirection.getAHEAD() && !sCIData.operationCallback.blockedWaypointAhead()) && !sCIData.operationCallback.blockedCrossroadAhead());
	}
	
	private boolean check_Drive_System_leaving_corassroad__onCrossroadExiting__choice_0_tr3_tr3() {
		return ((sCIData.operationCallback.targetDirection()==sCIDirection.getLEFT() && !sCIData.operationCallback.blockedWaypointLeft()) && !sCIData.operationCallback.blockedCrossroadAhead());
	}
	
	private boolean check_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad__choice_0_tr0_tr0() {
		return !sCIData.operationCallback.blockedFront();
	}
	
	private boolean check_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad__choice_1_tr0_tr0() {
		return !sCIData.operationCallback.blockedFront();
	}
	
	private boolean check_Drive_System_leaving_corassroad__onCrossroadExiting__choice_1_tr0_tr0() {
		return (((sCIData.operationCallback.posOrientation()==sCIOrientation.getEAST() || sCIData.operationCallback.posOrientation()==sCIOrientation.getWEST())) && !sCIData.operationCallback.blockedCrossroadAhead());
	}
	
	private boolean check_Drive_System_leaving_corassroad__onCrossroadExiting__choice_1_tr1_tr1() {
		return (((sCIData.operationCallback.posOrientation()==sCIOrientation.getSOUTH() || sCIData.operationCallback.posOrientation()==sCIOrientation.getNORTH())) && !sCIData.operationCallback.blockedWaypointRight());
	}
	
	private boolean check_Drive_System_entering_crossroad__onWaypointEnteringCrossroad__choice_0_tr1_tr1() {
		return (getWaitedForDeadlock()>5 && ((sCIData.operationCallback.targetDirection()==sCIDirection.getRIGHT() || sCIData.operationCallback.targetDirection()==sCIDirection.getBEHIND())));
	}
	
	private boolean check_Drive_System_entering_crossroad__onWaypointEnteringCrossroad__choice_0_tr2_tr2() {
		return (getWaitedForDeadlock()>10 && sCIData.operationCallback.targetDirection()==sCIDirection.getAHEAD());
	}
	
	private boolean check_Drive_System_entering_crossroad__onWaypointEnteringCrossroad__choice_1_tr1_tr1() {
		return ((((((!sCIData.operationCallback.blockedCrossroadAhead() && sCIData.operationCallback.blockedWaypointAhead()) && sCIData.operationCallback.blockedWaypointLeft()) && sCIData.operationCallback.blockedWaypointRight()) && sCIData.operationCallback.posOrientation()==sCIOrientation.getSOUTH())) || ((sCIData.operationCallback.blockedCrossroadAhead() && !sCIData.operationCallback.blockedFront())));
	}
	
	private boolean check_Drive_System_entering_crossroad__onWaypointEnteringCrossroad__choice_1_tr2_tr2() {
		return (!sCIData.operationCallback.blockedCrossroadAhead() && !sCIData.operationCallback.blockedWaypointRight());
	}
	
	private boolean check_Drive_System_driving_in_station__inStation__choice_0_tr0_tr0() {
		return sCIData.operationCallback.targetDirection()==sCIDirection.getAHEAD();
	}
	
	private boolean check_Drive_System_driving_in_station__inStation__choice_2_tr0_tr0() {
		return sCIData.operationCallback.targetDirection()==sCIDirection.getLEFT();
	}
	
	private boolean check_Drive_System_driving_in_station__inStation__choice_3_tr0_tr0() {
		return !sCIData.operationCallback.blockedFront();
	}
	
	private boolean check_Drive_System_driving_in_station__inStation__choice_4_tr0_tr0() {
		return !sCIData.operationCallback.blockedFront();
	}
	
	private boolean check_Drive_System__choice_1_tr0_tr0() {
		return (getDriveMode()==sCIDriveMode.getDRIVE() && sCIData.operationCallback.isOnTarget());
	}
	
	private boolean check_Drive_System__choice_2_tr0_tr0() {
		return sCIData.operationCallback.posType()==sCIPositionType.getSTATION();
	}
	
	private boolean check_Drive_System__choice_2_tr1_tr1() {
		return sCIData.operationCallback.posType()==sCIPositionType.getWAYPOINT();
	}
	
	private boolean check_Drive_System_unloading__unloading__choice_1_tr1_tr1() {
		return sCIData.operationCallback.targetDirection()==sCIDirection.getRIGHT();
	}
	
	private boolean check_Drive_System__choice_3_tr0_tr0() {
		return (getDriveMode()!=sCIDriveMode.getCHARGE() && sCIData.operationCallback.canChargeAtTarget());
	}
	
	private boolean check_Drive_System_entering_charger__startCharging__choice_1_tr1_tr1() {
		return sCIData.operationCallback.targetDirection()==sCIDirection.getBEHIND();
	}
	
	private void effect_Drive_System_leaving_corassroad_tr0() {
		exitSequence_Drive_System_leaving_corassroad();
		react_Drive_System__choice_4();
	}
	
	private void effect_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_ahead_tr0() {
		exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_ahead();
		react_Drive_System_leaving_corassroad__onCrossroadExiting__choice_2();
	}
	
	private void effect_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_right_side_tr0() {
		exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_right_side();
		react_Drive_System_leaving_corassroad__onCrossroadExiting__choice_2();
	}
	
	private void effect_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side_tr0() {
		exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side();
		react_Drive_System_leaving_corassroad__onCrossroadExiting__choice_2();
	}
	
	private void effect_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad_tr0() {
		exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad();
		react_Drive_System_leaving_corassroad__onCrossroadExiting__choice_2();
	}
	
	private void effect_Drive_System_entering_crossroad_tr0() {
		exitSequence_Drive_System_entering_crossroad();
		enterSequence_Drive_System_leaving_corassroad_default();
		react();
	}
	
	private void effect_Drive_System_driving_in_station_tr0() {
		exitSequence_Drive_System_driving_in_station();
		react_Drive_System__choice_4();
	}
	
	private void effect_Drive_System_unloading_tr0() {
		exitSequence_Drive_System_unloading();
		react_Drive_System__choice_5();
	}
	
	private void effect_Drive_System_entering_charger_tr0() {
		exitSequence_Drive_System_entering_charger();
		react_Drive_System__choice_5();
	}
	
	private void effect_Drive_System__choice_0_tr0() {
		enterSequence_Drive_System_unloading_default();
	}
	
	private void effect_Drive_System__choice_0_tr1() {
		react_Drive_System__choice_3();
	}
	
	private void effect_Drive_System_leaving_corassroad__onCrossroadExiting__choice_0_tr0() {
		react_Drive_System_leaving_corassroad__onCrossroadExiting__choice_1();
	}
	
	private void effect_Drive_System_leaving_corassroad__onCrossroadExiting__choice_0_tr1() {
		enterSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_right_side_default();
	}
	
	private void effect_Drive_System_leaving_corassroad__onCrossroadExiting__choice_0_tr2() {
		enterSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_ahead_default();
	}
	
	private void effect_Drive_System_leaving_corassroad__onCrossroadExiting__choice_0_tr3() {
		enterSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side_default();
	}
	
	private void effect_Drive_System_leaving_corassroad__onCrossroadExiting__choice_0_tr4() {
		enterSequence_Drive_System_leaving_corassroad__onCrossroadExiting_waiting_default();
	}
	
	private void effect_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad__choice_0_tr0() {
		enterSequence_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_1_default();
	}
	
	private void effect_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad__choice_0_tr1() {
		enterSequence_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_1_default();
	}
	
	private void effect_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad__choice_1_tr0() {
		enterSequence_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_2_default();
	}
	
	private void effect_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad__choice_1_tr1() {
		enterSequence_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_2_default();
	}
	
	private void effect_Drive_System_leaving_corassroad__onCrossroadExiting__choice_1_tr0() {
		enterSequence_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad_default();
	}
	
	private void effect_Drive_System_leaving_corassroad__onCrossroadExiting__choice_1_tr1() {
		enterSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_right_side_default();
	}
	
	private void effect_Drive_System_leaving_corassroad__onCrossroadExiting__choice_1_tr2() {
		enterSequence_Drive_System_leaving_corassroad__onCrossroadExiting_waiting_default();
	}
	
	private void effect_Drive_System_leaving_corassroad__onCrossroadExiting__choice_2_tr0() {
		react_Drive_System_leaving_corassroad__onCrossroadExiting__exit_Default();
	}
	
	private void effect_Drive_System_entering_crossroad__onWaypointEnteringCrossroad__choice_0_tr1() {
		enterSequence_Drive_System_entering_crossroad__onWaypointEnteringCrossroad_entering_crossroad_default();
	}
	
	private void effect_Drive_System_entering_crossroad__onWaypointEnteringCrossroad__choice_0_tr2() {
		enterSequence_Drive_System_entering_crossroad__onWaypointEnteringCrossroad_entering_crossroad_default();
	}
	
	private void effect_Drive_System_entering_crossroad__onWaypointEnteringCrossroad__choice_0_tr0() {
		enterSequence_Drive_System_entering_crossroad__onWaypointEnteringCrossroad_waiting_to_ensure_real_deadlock_default();
	}
	
	private void effect_Drive_System_entering_crossroad__onWaypointEnteringCrossroad__choice_1_tr1() {
		react_Drive_System_entering_crossroad__onWaypointEnteringCrossroad__choice_0();
	}
	
	private void effect_Drive_System_entering_crossroad__onWaypointEnteringCrossroad__choice_1_tr2() {
		enterSequence_Drive_System_entering_crossroad__onWaypointEnteringCrossroad_entering_crossroad_default();
	}
	
	private void effect_Drive_System_entering_crossroad__onWaypointEnteringCrossroad__choice_1_tr0() {
		enterSequence_Drive_System_entering_crossroad__onWaypointEnteringCrossroad_waiting_on_waypoint_default();
	}
	
	private void effect_Drive_System_entering_crossroad__onWaypointEnteringCrossroad__choice_2_tr0() {
		react_Drive_System_entering_crossroad__onWaypointEnteringCrossroad__choice_1();
	}
	
	private void effect_Drive_System_driving_in_station__inStation__choice_0_tr0() {
		react_Drive_System_driving_in_station__inStation__choice_4();
	}
	
	private void effect_Drive_System_driving_in_station__inStation__choice_0_tr1() {
		react_Drive_System_driving_in_station__inStation__choice_3();
	}
	
	private void effect_Drive_System_driving_in_station__inStation__choice_1_tr0() {
		react_Drive_System_driving_in_station__inStation__exit_Default();
	}
	
	private void effect_Drive_System_driving_in_station__inStation__choice_2_tr0() {
		enterSequence_Drive_System_driving_in_station__inStation_turn_left_default();
	}
	
	private void effect_Drive_System_driving_in_station__inStation__choice_2_tr1() {
		enterSequence_Drive_System_driving_in_station__inStation_turn_right_default();
	}
	
	private void effect_Drive_System_driving_in_station__inStation__choice_3_tr0() {
		enterSequence_Drive_System_driving_in_station__inStation_drive_default();
	}
	
	private void effect_Drive_System_driving_in_station__inStation__choice_3_tr1() {
		react_Drive_System_driving_in_station__inStation__choice_2();
	}
	
	private void effect_Drive_System_driving_in_station__inStation__choice_4_tr0() {
		enterSequence_Drive_System_driving_in_station__inStation_drive_default();
	}
	
	private void effect_Drive_System_driving_in_station__inStation__choice_4_tr1() {
		enterSequence_Drive_System_driving_in_station__inStation_wait_to_drive_default();
	}
	
	private void effect_Drive_System__choice_1_tr0() {
		react_Drive_System__choice_6();
	}
	
	private void effect_Drive_System__choice_1_tr1() {
		react_Drive_System__choice_0();
	}
	
	private void effect_Drive_System__choice_2_tr0() {
		enterSequence_Drive_System_driving_in_station_default();
	}
	
	private void effect_Drive_System__choice_2_tr1() {
		enterSequence_Drive_System_entering_crossroad_default();
	}
	
	private void effect_Drive_System__choice_2_tr2() {
		enterSequence_Drive_System_waiting_on_waypoint_or_station_default();
	}
	
	private void effect_Drive_System_unloading__unloading__choice_0_tr0() {
		react_Drive_System_unloading__unloading__choice_1();
	}
	
	private void effect_Drive_System_unloading__unloading__choice_1_tr1() {
		enterSequence_Drive_System_unloading__unloading_unloading_default();
	}
	
	private void effect_Drive_System_unloading__unloading__choice_1_tr0() {
		enterSequence_Drive_System_unloading__unloading_turning_default();
	}
	
	private void effect_Drive_System__choice_3_tr0() {
		enterSequence_Drive_System_entering_charger_default();
	}
	
	private void effect_Drive_System__choice_3_tr1() {
		react_Drive_System__choice_2();
	}
	
	private void effect_Drive_System_entering_charger__startCharging__choice_0_tr0() {
		react_Drive_System_entering_charger__startCharging__choice_1();
	}
	
	private void effect_Drive_System_entering_charger__startCharging__choice_1_tr1() {
		enterSequence_Drive_System_entering_charger__startCharging_unloading_default();
	}
	
	private void effect_Drive_System_entering_charger__startCharging__choice_1_tr0() {
		enterSequence_Drive_System_entering_charger__startCharging_turning_default();
	}
	
	private void effect_Drive_System__choice_4_tr0() {
		enterSequence_Drive_System_waiting_on_waypoint_or_station_default();
	}
	
	private void effect_Drive_System__choice_5_tr0() {
		react_Drive_System__choice_6();
	}
	
	private void effect_Drive_System__choice_6_tr0() {
		sCIProcessor.raiseArrived();
		
		setDriveMode(sCIDriveMode.uNSET);
		
		enterSequence_Drive_System_idle_default();
	}
	
	/* Entry action for state 'forward1'. */
	private void entryAction_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_ahead__onCrossroadDriveForward_forward1() {
		sCIActors.raiseDriveForward();
	}
	
	/* Entry action for state 'forward2'. */
	private void entryAction_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_ahead__onCrossroadDriveForward_forward2() {
		sCIActors.raiseDriveForward();
	}
	
	/* Entry action for state 'right'. */
	private void entryAction_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_right_side__onCrossroadDriveRight_right() {
		sCIActors.raiseTurnRight();
	}
	
	/* Entry action for state 'forward'. */
	private void entryAction_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_right_side__onCrossroadDriveRight_forward() {
		sCIActors.raiseDriveForward();
	}
	
	/* Entry action for state 'forward1'. */
	private void entryAction_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward1() {
		sCIActors.raiseDriveForward();
	}
	
	/* Entry action for state 'left'. */
	private void entryAction_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_left() {
		sCIActors.raiseTurnLeft();
	}
	
	/* Entry action for state 'forward2'. */
	private void entryAction_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward2() {
		sCIActors.raiseDriveForward();
	}
	
	/* Entry action for state 'forward3'. */
	private void entryAction_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward3() {
		sCIActors.raiseDriveForward();
	}
	
	/* Entry action for state 'turining left 1'. */
	private void entryAction_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_1() {
		sCIActors.raiseTurnLeft();
	}
	
	/* Entry action for state 'driving forward 1'. */
	private void entryAction_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_1() {
		sCIActors.raiseDriveForward();
	}
	
	/* Entry action for state 'turining left 2'. */
	private void entryAction_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_2() {
		sCIActors.raiseTurnLeft();
	}
	
	/* Entry action for state 'driving forward 2'. */
	private void entryAction_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_2() {
		sCIActors.raiseDriveForward();
	}
	
	/* Entry action for state 'waiting to ensure real deadlock'. */
	private void entryAction_Drive_System_entering_crossroad__onWaypointEnteringCrossroad_waiting_to_ensure_real_deadlock() {
		timer.setTimer(this, 0, (1 * 1000), false);
	}
	
	/* Entry action for state 'entering crossroad'. */
	private void entryAction_Drive_System_entering_crossroad__onWaypointEnteringCrossroad_entering_crossroad() {
		sCIActors.raiseDriveForward();
		
		setWaitedForDeadlock(0);
	}
	
	/* Entry action for state 'turn right'. */
	private void entryAction_Drive_System_driving_in_station__inStation_turn_right() {
		sCIActors.raiseTurnRight();
	}
	
	/* Entry action for state 'turn left'. */
	private void entryAction_Drive_System_driving_in_station__inStation_turn_left() {
		sCIActors.raiseTurnLeft();
	}
	
	/* Entry action for state 'drive'. */
	private void entryAction_Drive_System_driving_in_station__inStation_drive() {
		sCIActors.raiseDriveForward();
	}
	
	/* Entry action for state 'unloading'. */
	private void entryAction_Drive_System_unloading__unloading_unloading() {
		sCIActors.raiseStartUnload();
	}
	
	/* Entry action for state 'turning'. */
	private void entryAction_Drive_System_unloading__unloading_turning() {
		sCIActors.raiseTurnLeft();
	}
	
	/* Entry action for state 'unloading'. */
	private void entryAction_Drive_System_entering_charger__startCharging_unloading() {
		sCIActors.raiseDriveBackward();
	}
	
	/* Entry action for state 'turning'. */
	private void entryAction_Drive_System_entering_charger__startCharging_turning() {
		sCIActors.raiseTurnLeft();
	}
	
	/* Exit action for state 'waiting to ensure real deadlock'. */
	private void exitAction_Drive_System_entering_crossroad__onWaypointEnteringCrossroad_waiting_to_ensure_real_deadlock() {
		timer.unsetTimer(this, 0);
	}
	
	/* 'default' enter sequence for state idle */
	private void enterSequence_Drive_System_idle_default() {
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_idle;
	}
	
	/* 'default' enter sequence for state waiting on waypoint or station */
	private void enterSequence_Drive_System_waiting_on_waypoint_or_station_default() {
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_waiting_on_waypoint_or_station;
	}
	
	/* 'default' enter sequence for state leaving corassroad */
	private void enterSequence_Drive_System_leaving_corassroad_default() {
		enterSequence_Drive_System_leaving_corassroad__onCrossroadExiting_default();
	}
	
	/* 'default' enter sequence for state waiting */
	private void enterSequence_Drive_System_leaving_corassroad__onCrossroadExiting_waiting_default() {
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_leaving_corassroad__onCrossroadExiting_waiting;
	}
	
	/* 'default' enter sequence for state leaving crossroad ahead */
	private void enterSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_ahead_default() {
		enterSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_ahead__onCrossroadDriveForward_default();
	}
	
	/* 'default' enter sequence for state forward1 */
	private void enterSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_ahead__onCrossroadDriveForward_forward1_default() {
		entryAction_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_ahead__onCrossroadDriveForward_forward1();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_ahead__onCrossroadDriveForward_forward1;
	}
	
	/* 'default' enter sequence for state forward2 */
	private void enterSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_ahead__onCrossroadDriveForward_forward2_default() {
		entryAction_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_ahead__onCrossroadDriveForward_forward2();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_ahead__onCrossroadDriveForward_forward2;
	}
	
	/* 'default' enter sequence for state leaving crossroad to right side */
	private void enterSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_right_side_default() {
		enterSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_right_side__onCrossroadDriveRight_default();
	}
	
	/* 'default' enter sequence for state right */
	private void enterSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_right_side__onCrossroadDriveRight_right_default() {
		entryAction_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_right_side__onCrossroadDriveRight_right();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_right_side__onCrossroadDriveRight_right;
	}
	
	/* 'default' enter sequence for state forward */
	private void enterSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_right_side__onCrossroadDriveRight_forward_default() {
		entryAction_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_right_side__onCrossroadDriveRight_forward();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_right_side__onCrossroadDriveRight_forward;
	}
	
	/* 'default' enter sequence for state leaving crossroad to left side */
	private void enterSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side_default() {
		enterSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_default();
	}
	
	/* 'default' enter sequence for state forward1 */
	private void enterSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward1_default() {
		entryAction_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward1();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward1;
	}
	
	/* 'default' enter sequence for state left */
	private void enterSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_left_default() {
		entryAction_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_left();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_left;
	}
	
	/* 'default' enter sequence for state forward2 */
	private void enterSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward2_default() {
		entryAction_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward2();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward2;
	}
	
	/* 'default' enter sequence for state forward3 */
	private void enterSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward3_default() {
		entryAction_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward3();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward3;
	}
	
	/* 'default' enter sequence for state turning around on crossroad */
	private void enterSequence_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad_default() {
		enterSequence_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_default();
	}
	
	/* 'default' enter sequence for state turining left 1 */
	private void enterSequence_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_1_default() {
		entryAction_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_1();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_1;
	}
	
	/* 'default' enter sequence for state waiting to drive forward 1 */
	private void enterSequence_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_1_default() {
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_1;
	}
	
	/* 'default' enter sequence for state driving forward 1 */
	private void enterSequence_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_1_default() {
		entryAction_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_1();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_1;
	}
	
	/* 'default' enter sequence for state turining left 2 */
	private void enterSequence_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_2_default() {
		entryAction_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_2();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_2;
	}
	
	/* 'default' enter sequence for state waiting to drive forward 2 */
	private void enterSequence_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_2_default() {
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_2;
	}
	
	/* 'default' enter sequence for state driving forward 2 */
	private void enterSequence_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_2_default() {
		entryAction_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_2();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_2;
	}
	
	/* 'default' enter sequence for state entering crossroad */
	private void enterSequence_Drive_System_entering_crossroad_default() {
		enterSequence_Drive_System_entering_crossroad__onWaypointEnteringCrossroad_default();
	}
	
	/* 'default' enter sequence for state waiting on waypoint */
	private void enterSequence_Drive_System_entering_crossroad__onWaypointEnteringCrossroad_waiting_on_waypoint_default() {
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_entering_crossroad__onWaypointEnteringCrossroad_waiting_on_waypoint;
	}
	
	/* 'default' enter sequence for state waiting to ensure real deadlock */
	private void enterSequence_Drive_System_entering_crossroad__onWaypointEnteringCrossroad_waiting_to_ensure_real_deadlock_default() {
		entryAction_Drive_System_entering_crossroad__onWaypointEnteringCrossroad_waiting_to_ensure_real_deadlock();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_entering_crossroad__onWaypointEnteringCrossroad_waiting_to_ensure_real_deadlock;
	}
	
	/* 'default' enter sequence for state entering crossroad */
	private void enterSequence_Drive_System_entering_crossroad__onWaypointEnteringCrossroad_entering_crossroad_default() {
		entryAction_Drive_System_entering_crossroad__onWaypointEnteringCrossroad_entering_crossroad();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_entering_crossroad__onWaypointEnteringCrossroad_entering_crossroad;
	}
	
	/* 'default' enter sequence for state driving in station */
	private void enterSequence_Drive_System_driving_in_station_default() {
		enterSequence_Drive_System_driving_in_station__inStation_default();
	}
	
	/* 'default' enter sequence for state turn right */
	private void enterSequence_Drive_System_driving_in_station__inStation_turn_right_default() {
		entryAction_Drive_System_driving_in_station__inStation_turn_right();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_driving_in_station__inStation_turn_right;
	}
	
	/* 'default' enter sequence for state turn left */
	private void enterSequence_Drive_System_driving_in_station__inStation_turn_left_default() {
		entryAction_Drive_System_driving_in_station__inStation_turn_left();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_driving_in_station__inStation_turn_left;
	}
	
	/* 'default' enter sequence for state drive */
	private void enterSequence_Drive_System_driving_in_station__inStation_drive_default() {
		entryAction_Drive_System_driving_in_station__inStation_drive();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_driving_in_station__inStation_drive;
	}
	
	/* 'default' enter sequence for state wait to drive */
	private void enterSequence_Drive_System_driving_in_station__inStation_wait_to_drive_default() {
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_driving_in_station__inStation_wait_to_drive;
	}
	
	/* 'default' enter sequence for state unloading */
	private void enterSequence_Drive_System_unloading_default() {
		enterSequence_Drive_System_unloading__unloading_default();
	}
	
	/* 'default' enter sequence for state unloading */
	private void enterSequence_Drive_System_unloading__unloading_unloading_default() {
		entryAction_Drive_System_unloading__unloading_unloading();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_unloading__unloading_unloading;
	}
	
	/* 'default' enter sequence for state turning */
	private void enterSequence_Drive_System_unloading__unloading_turning_default() {
		entryAction_Drive_System_unloading__unloading_turning();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_unloading__unloading_turning;
	}
	
	/* 'default' enter sequence for state waiting for data */
	private void enterSequence_Drive_System_unloading__unloading_waiting_for_data_default() {
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_unloading__unloading_waiting_for_data;
	}
	
	/* 'default' enter sequence for state entering charger */
	private void enterSequence_Drive_System_entering_charger_default() {
		enterSequence_Drive_System_entering_charger__startCharging_default();
	}
	
	/* 'default' enter sequence for state unloading */
	private void enterSequence_Drive_System_entering_charger__startCharging_unloading_default() {
		entryAction_Drive_System_entering_charger__startCharging_unloading();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_entering_charger__startCharging_unloading;
	}
	
	/* 'default' enter sequence for state turning */
	private void enterSequence_Drive_System_entering_charger__startCharging_turning_default() {
		entryAction_Drive_System_entering_charger__startCharging_turning();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_entering_charger__startCharging_turning;
	}
	
	/* 'default' enter sequence for state waiting for data */
	private void enterSequence_Drive_System_entering_charger__startCharging_waiting_for_data_default() {
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_entering_charger__startCharging_waiting_for_data;
	}
	
	/* 'default' enter sequence for region Drive System */
	private void enterSequence_Drive_System_default() {
		react_Drive_System__entry_Default();
	}
	
	/* 'default' enter sequence for region _onCrossroadExiting */
	private void enterSequence_Drive_System_leaving_corassroad__onCrossroadExiting_default() {
		react_Drive_System_leaving_corassroad__onCrossroadExiting__entry_Default();
	}
	
	/* 'default' enter sequence for region _onCrossroadDriveForward */
	private void enterSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_ahead__onCrossroadDriveForward_default() {
		react_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_ahead__onCrossroadDriveForward__entry_Default();
	}
	
	/* 'default' enter sequence for region _onCrossroadDriveRight */
	private void enterSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_right_side__onCrossroadDriveRight_default() {
		react_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_right_side__onCrossroadDriveRight__entry_Default();
	}
	
	/* 'default' enter sequence for region _onCrossroadDriveLeft */
	private void enterSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_default() {
		react_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft__entry_Default();
	}
	
	/* 'default' enter sequence for region _turningAroundOnCrossroad */
	private void enterSequence_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_default() {
		react_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad__entry_Default();
	}
	
	/* 'default' enter sequence for region _onWaypointEnteringCrossroad */
	private void enterSequence_Drive_System_entering_crossroad__onWaypointEnteringCrossroad_default() {
		react_Drive_System_entering_crossroad__onWaypointEnteringCrossroad__entry_Default();
	}
	
	/* 'default' enter sequence for region _inStation */
	private void enterSequence_Drive_System_driving_in_station__inStation_default() {
		react_Drive_System_driving_in_station__inStation__entry_Default();
	}
	
	/* 'default' enter sequence for region _unloading */
	private void enterSequence_Drive_System_unloading__unloading_default() {
		react_Drive_System_unloading__unloading__entry_Default();
	}
	
	/* 'default' enter sequence for region _startCharging */
	private void enterSequence_Drive_System_entering_charger__startCharging_default() {
		react_Drive_System_entering_charger__startCharging__entry_Default();
	}
	
	/* Default exit sequence for state idle */
	private void exitSequence_Drive_System_idle() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state waiting on waypoint or station */
	private void exitSequence_Drive_System_waiting_on_waypoint_or_station() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state leaving corassroad */
	private void exitSequence_Drive_System_leaving_corassroad() {
		exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting();
	}
	
	/* Default exit sequence for state waiting */
	private void exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_waiting() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state leaving crossroad ahead */
	private void exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_ahead() {
		exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_ahead__onCrossroadDriveForward();
	}
	
	/* Default exit sequence for state forward1 */
	private void exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_ahead__onCrossroadDriveForward_forward1() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state forward2 */
	private void exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_ahead__onCrossroadDriveForward_forward2() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state leaving crossroad to right side */
	private void exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_right_side() {
		exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_right_side__onCrossroadDriveRight();
	}
	
	/* Default exit sequence for state right */
	private void exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_right_side__onCrossroadDriveRight_right() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state forward */
	private void exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_right_side__onCrossroadDriveRight_forward() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state leaving crossroad to left side */
	private void exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side() {
		exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft();
	}
	
	/* Default exit sequence for state forward1 */
	private void exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward1() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state left */
	private void exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_left() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state forward2 */
	private void exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward2() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state forward3 */
	private void exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward3() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state turning around on crossroad */
	private void exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad() {
		exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad();
	}
	
	/* Default exit sequence for state turining left 1 */
	private void exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_1() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state waiting to drive forward 1 */
	private void exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_1() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state driving forward 1 */
	private void exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_1() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state turining left 2 */
	private void exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_2() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state waiting to drive forward 2 */
	private void exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_2() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state driving forward 2 */
	private void exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_2() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state entering crossroad */
	private void exitSequence_Drive_System_entering_crossroad() {
		exitSequence_Drive_System_entering_crossroad__onWaypointEnteringCrossroad();
	}
	
	/* Default exit sequence for state waiting on waypoint */
	private void exitSequence_Drive_System_entering_crossroad__onWaypointEnteringCrossroad_waiting_on_waypoint() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state waiting to ensure real deadlock */
	private void exitSequence_Drive_System_entering_crossroad__onWaypointEnteringCrossroad_waiting_to_ensure_real_deadlock() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
		
		exitAction_Drive_System_entering_crossroad__onWaypointEnteringCrossroad_waiting_to_ensure_real_deadlock();
	}
	
	/* Default exit sequence for state entering crossroad */
	private void exitSequence_Drive_System_entering_crossroad__onWaypointEnteringCrossroad_entering_crossroad() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state driving in station */
	private void exitSequence_Drive_System_driving_in_station() {
		exitSequence_Drive_System_driving_in_station__inStation();
	}
	
	/* Default exit sequence for state turn right */
	private void exitSequence_Drive_System_driving_in_station__inStation_turn_right() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state turn left */
	private void exitSequence_Drive_System_driving_in_station__inStation_turn_left() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state drive */
	private void exitSequence_Drive_System_driving_in_station__inStation_drive() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state wait to drive */
	private void exitSequence_Drive_System_driving_in_station__inStation_wait_to_drive() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state unloading */
	private void exitSequence_Drive_System_unloading() {
		exitSequence_Drive_System_unloading__unloading();
	}
	
	/* Default exit sequence for state unloading */
	private void exitSequence_Drive_System_unloading__unloading_unloading() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state turning */
	private void exitSequence_Drive_System_unloading__unloading_turning() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state waiting for data */
	private void exitSequence_Drive_System_unloading__unloading_waiting_for_data() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state entering charger */
	private void exitSequence_Drive_System_entering_charger() {
		exitSequence_Drive_System_entering_charger__startCharging();
	}
	
	/* Default exit sequence for state unloading */
	private void exitSequence_Drive_System_entering_charger__startCharging_unloading() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state turning */
	private void exitSequence_Drive_System_entering_charger__startCharging_turning() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state waiting for data */
	private void exitSequence_Drive_System_entering_charger__startCharging_waiting_for_data() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for region Drive System */
	private void exitSequence_Drive_System() {
		switch (stateVector[0]) {
		case drive_System_idle:
			exitSequence_Drive_System_idle();
			break;
		case drive_System_waiting_on_waypoint_or_station:
			exitSequence_Drive_System_waiting_on_waypoint_or_station();
			break;
		case drive_System_leaving_corassroad__onCrossroadExiting_waiting:
			exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_waiting();
			break;
		case drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_ahead__onCrossroadDriveForward_forward1:
			exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_ahead__onCrossroadDriveForward_forward1();
			break;
		case drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_ahead__onCrossroadDriveForward_forward2:
			exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_ahead__onCrossroadDriveForward_forward2();
			break;
		case drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_right_side__onCrossroadDriveRight_right:
			exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_right_side__onCrossroadDriveRight_right();
			break;
		case drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_right_side__onCrossroadDriveRight_forward:
			exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_right_side__onCrossroadDriveRight_forward();
			break;
		case drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward1:
			exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward1();
			break;
		case drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_left:
			exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_left();
			break;
		case drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward2:
			exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward2();
			break;
		case drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward3:
			exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward3();
			break;
		case drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_1:
			exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_1();
			break;
		case drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_1:
			exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_1();
			break;
		case drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_1:
			exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_1();
			break;
		case drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_2:
			exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_2();
			break;
		case drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_2:
			exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_2();
			break;
		case drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_2:
			exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_2();
			break;
		case drive_System_entering_crossroad__onWaypointEnteringCrossroad_waiting_on_waypoint:
			exitSequence_Drive_System_entering_crossroad__onWaypointEnteringCrossroad_waiting_on_waypoint();
			break;
		case drive_System_entering_crossroad__onWaypointEnteringCrossroad_waiting_to_ensure_real_deadlock:
			exitSequence_Drive_System_entering_crossroad__onWaypointEnteringCrossroad_waiting_to_ensure_real_deadlock();
			break;
		case drive_System_entering_crossroad__onWaypointEnteringCrossroad_entering_crossroad:
			exitSequence_Drive_System_entering_crossroad__onWaypointEnteringCrossroad_entering_crossroad();
			break;
		case drive_System_driving_in_station__inStation_turn_right:
			exitSequence_Drive_System_driving_in_station__inStation_turn_right();
			break;
		case drive_System_driving_in_station__inStation_turn_left:
			exitSequence_Drive_System_driving_in_station__inStation_turn_left();
			break;
		case drive_System_driving_in_station__inStation_drive:
			exitSequence_Drive_System_driving_in_station__inStation_drive();
			break;
		case drive_System_driving_in_station__inStation_wait_to_drive:
			exitSequence_Drive_System_driving_in_station__inStation_wait_to_drive();
			break;
		case drive_System_unloading__unloading_unloading:
			exitSequence_Drive_System_unloading__unloading_unloading();
			break;
		case drive_System_unloading__unloading_turning:
			exitSequence_Drive_System_unloading__unloading_turning();
			break;
		case drive_System_unloading__unloading_waiting_for_data:
			exitSequence_Drive_System_unloading__unloading_waiting_for_data();
			break;
		case drive_System_entering_charger__startCharging_unloading:
			exitSequence_Drive_System_entering_charger__startCharging_unloading();
			break;
		case drive_System_entering_charger__startCharging_turning:
			exitSequence_Drive_System_entering_charger__startCharging_turning();
			break;
		case drive_System_entering_charger__startCharging_waiting_for_data:
			exitSequence_Drive_System_entering_charger__startCharging_waiting_for_data();
			break;
		default:
			break;
		}
	}
	
	/* Default exit sequence for region _onCrossroadExiting */
	private void exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting() {
		switch (stateVector[0]) {
		case drive_System_leaving_corassroad__onCrossroadExiting_waiting:
			exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_waiting();
			break;
		case drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_ahead__onCrossroadDriveForward_forward1:
			exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_ahead__onCrossroadDriveForward_forward1();
			break;
		case drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_ahead__onCrossroadDriveForward_forward2:
			exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_ahead__onCrossroadDriveForward_forward2();
			break;
		case drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_right_side__onCrossroadDriveRight_right:
			exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_right_side__onCrossroadDriveRight_right();
			break;
		case drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_right_side__onCrossroadDriveRight_forward:
			exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_right_side__onCrossroadDriveRight_forward();
			break;
		case drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward1:
			exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward1();
			break;
		case drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_left:
			exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_left();
			break;
		case drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward2:
			exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward2();
			break;
		case drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward3:
			exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward3();
			break;
		case drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_1:
			exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_1();
			break;
		case drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_1:
			exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_1();
			break;
		case drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_1:
			exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_1();
			break;
		case drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_2:
			exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_2();
			break;
		case drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_2:
			exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_2();
			break;
		case drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_2:
			exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_2();
			break;
		default:
			break;
		}
	}
	
	/* Default exit sequence for region _onCrossroadDriveForward */
	private void exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_ahead__onCrossroadDriveForward() {
		switch (stateVector[0]) {
		case drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_ahead__onCrossroadDriveForward_forward1:
			exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_ahead__onCrossroadDriveForward_forward1();
			break;
		case drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_ahead__onCrossroadDriveForward_forward2:
			exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_ahead__onCrossroadDriveForward_forward2();
			break;
		default:
			break;
		}
	}
	
	/* Default exit sequence for region _onCrossroadDriveRight */
	private void exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_right_side__onCrossroadDriveRight() {
		switch (stateVector[0]) {
		case drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_right_side__onCrossroadDriveRight_right:
			exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_right_side__onCrossroadDriveRight_right();
			break;
		case drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_right_side__onCrossroadDriveRight_forward:
			exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_right_side__onCrossroadDriveRight_forward();
			break;
		default:
			break;
		}
	}
	
	/* Default exit sequence for region _onCrossroadDriveLeft */
	private void exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft() {
		switch (stateVector[0]) {
		case drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward1:
			exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward1();
			break;
		case drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_left:
			exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_left();
			break;
		case drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward2:
			exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward2();
			break;
		case drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward3:
			exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward3();
			break;
		default:
			break;
		}
	}
	
	/* Default exit sequence for region _turningAroundOnCrossroad */
	private void exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad() {
		switch (stateVector[0]) {
		case drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_1:
			exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_1();
			break;
		case drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_1:
			exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_1();
			break;
		case drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_1:
			exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_1();
			break;
		case drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_2:
			exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_2();
			break;
		case drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_2:
			exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_2();
			break;
		case drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_2:
			exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_2();
			break;
		default:
			break;
		}
	}
	
	/* Default exit sequence for region _onWaypointEnteringCrossroad */
	private void exitSequence_Drive_System_entering_crossroad__onWaypointEnteringCrossroad() {
		switch (stateVector[0]) {
		case drive_System_entering_crossroad__onWaypointEnteringCrossroad_waiting_on_waypoint:
			exitSequence_Drive_System_entering_crossroad__onWaypointEnteringCrossroad_waiting_on_waypoint();
			break;
		case drive_System_entering_crossroad__onWaypointEnteringCrossroad_waiting_to_ensure_real_deadlock:
			exitSequence_Drive_System_entering_crossroad__onWaypointEnteringCrossroad_waiting_to_ensure_real_deadlock();
			break;
		case drive_System_entering_crossroad__onWaypointEnteringCrossroad_entering_crossroad:
			exitSequence_Drive_System_entering_crossroad__onWaypointEnteringCrossroad_entering_crossroad();
			break;
		default:
			break;
		}
	}
	
	/* Default exit sequence for region _inStation */
	private void exitSequence_Drive_System_driving_in_station__inStation() {
		switch (stateVector[0]) {
		case drive_System_driving_in_station__inStation_turn_right:
			exitSequence_Drive_System_driving_in_station__inStation_turn_right();
			break;
		case drive_System_driving_in_station__inStation_turn_left:
			exitSequence_Drive_System_driving_in_station__inStation_turn_left();
			break;
		case drive_System_driving_in_station__inStation_drive:
			exitSequence_Drive_System_driving_in_station__inStation_drive();
			break;
		case drive_System_driving_in_station__inStation_wait_to_drive:
			exitSequence_Drive_System_driving_in_station__inStation_wait_to_drive();
			break;
		default:
			break;
		}
	}
	
	/* Default exit sequence for region _unloading */
	private void exitSequence_Drive_System_unloading__unloading() {
		switch (stateVector[0]) {
		case drive_System_unloading__unloading_unloading:
			exitSequence_Drive_System_unloading__unloading_unloading();
			break;
		case drive_System_unloading__unloading_turning:
			exitSequence_Drive_System_unloading__unloading_turning();
			break;
		case drive_System_unloading__unloading_waiting_for_data:
			exitSequence_Drive_System_unloading__unloading_waiting_for_data();
			break;
		default:
			break;
		}
	}
	
	/* Default exit sequence for region _startCharging */
	private void exitSequence_Drive_System_entering_charger__startCharging() {
		switch (stateVector[0]) {
		case drive_System_entering_charger__startCharging_unloading:
			exitSequence_Drive_System_entering_charger__startCharging_unloading();
			break;
		case drive_System_entering_charger__startCharging_turning:
			exitSequence_Drive_System_entering_charger__startCharging_turning();
			break;
		case drive_System_entering_charger__startCharging_waiting_for_data:
			exitSequence_Drive_System_entering_charger__startCharging_waiting_for_data();
			break;
		default:
			break;
		}
	}
	
	/* The reactions of state null. */
	private void react_Drive_System__choice_0() {
		if (check_Drive_System__choice_0_tr0_tr0()) {
			effect_Drive_System__choice_0_tr0();
		} else {
			effect_Drive_System__choice_0_tr1();
		}
	}
	
	/* The reactions of state null. */
	private void react_Drive_System_leaving_corassroad__onCrossroadExiting__choice_0() {
		if (check_Drive_System_leaving_corassroad__onCrossroadExiting__choice_0_tr0_tr0()) {
			effect_Drive_System_leaving_corassroad__onCrossroadExiting__choice_0_tr0();
		} else {
			if (check_Drive_System_leaving_corassroad__onCrossroadExiting__choice_0_tr1_tr1()) {
				effect_Drive_System_leaving_corassroad__onCrossroadExiting__choice_0_tr1();
			} else {
				if (check_Drive_System_leaving_corassroad__onCrossroadExiting__choice_0_tr2_tr2()) {
					effect_Drive_System_leaving_corassroad__onCrossroadExiting__choice_0_tr2();
				} else {
					if (check_Drive_System_leaving_corassroad__onCrossroadExiting__choice_0_tr3_tr3()) {
						effect_Drive_System_leaving_corassroad__onCrossroadExiting__choice_0_tr3();
					} else {
						effect_Drive_System_leaving_corassroad__onCrossroadExiting__choice_0_tr4();
					}
				}
			}
		}
	}
	
	/* The reactions of state null. */
	private void react_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad__choice_0() {
		if (check_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad__choice_0_tr0_tr0()) {
			effect_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad__choice_0_tr0();
		} else {
			effect_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad__choice_0_tr1();
		}
	}
	
	/* The reactions of state null. */
	private void react_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad__choice_1() {
		if (check_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad__choice_1_tr0_tr0()) {
			effect_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad__choice_1_tr0();
		} else {
			effect_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad__choice_1_tr1();
		}
	}
	
	/* The reactions of state null. */
	private void react_Drive_System_leaving_corassroad__onCrossroadExiting__choice_1() {
		if (check_Drive_System_leaving_corassroad__onCrossroadExiting__choice_1_tr0_tr0()) {
			effect_Drive_System_leaving_corassroad__onCrossroadExiting__choice_1_tr0();
		} else {
			if (check_Drive_System_leaving_corassroad__onCrossroadExiting__choice_1_tr1_tr1()) {
				effect_Drive_System_leaving_corassroad__onCrossroadExiting__choice_1_tr1();
			} else {
				effect_Drive_System_leaving_corassroad__onCrossroadExiting__choice_1_tr2();
			}
		}
	}
	
	/* The reactions of state null. */
	private void react_Drive_System_leaving_corassroad__onCrossroadExiting__choice_2() {
		effect_Drive_System_leaving_corassroad__onCrossroadExiting__choice_2_tr0();
	}
	
	/* The reactions of state null. */
	private void react_Drive_System_entering_crossroad__onWaypointEnteringCrossroad__choice_0() {
		if (check_Drive_System_entering_crossroad__onWaypointEnteringCrossroad__choice_0_tr1_tr1()) {
			effect_Drive_System_entering_crossroad__onWaypointEnteringCrossroad__choice_0_tr1();
		} else {
			if (check_Drive_System_entering_crossroad__onWaypointEnteringCrossroad__choice_0_tr2_tr2()) {
				effect_Drive_System_entering_crossroad__onWaypointEnteringCrossroad__choice_0_tr2();
			} else {
				effect_Drive_System_entering_crossroad__onWaypointEnteringCrossroad__choice_0_tr0();
			}
		}
	}
	
	/* The reactions of state null. */
	private void react_Drive_System_entering_crossroad__onWaypointEnteringCrossroad__choice_1() {
		if (check_Drive_System_entering_crossroad__onWaypointEnteringCrossroad__choice_1_tr1_tr1()) {
			effect_Drive_System_entering_crossroad__onWaypointEnteringCrossroad__choice_1_tr1();
		} else {
			if (check_Drive_System_entering_crossroad__onWaypointEnteringCrossroad__choice_1_tr2_tr2()) {
				effect_Drive_System_entering_crossroad__onWaypointEnteringCrossroad__choice_1_tr2();
			} else {
				effect_Drive_System_entering_crossroad__onWaypointEnteringCrossroad__choice_1_tr0();
			}
		}
	}
	
	/* The reactions of state null. */
	private void react_Drive_System_entering_crossroad__onWaypointEnteringCrossroad__choice_2() {
		effect_Drive_System_entering_crossroad__onWaypointEnteringCrossroad__choice_2_tr0();
	}
	
	/* The reactions of state null. */
	private void react_Drive_System_driving_in_station__inStation__choice_0() {
		if (check_Drive_System_driving_in_station__inStation__choice_0_tr0_tr0()) {
			effect_Drive_System_driving_in_station__inStation__choice_0_tr0();
		} else {
			effect_Drive_System_driving_in_station__inStation__choice_0_tr1();
		}
	}
	
	/* The reactions of state null. */
	private void react_Drive_System_driving_in_station__inStation__choice_1() {
		effect_Drive_System_driving_in_station__inStation__choice_1_tr0();
	}
	
	/* The reactions of state null. */
	private void react_Drive_System_driving_in_station__inStation__choice_2() {
		if (check_Drive_System_driving_in_station__inStation__choice_2_tr0_tr0()) {
			effect_Drive_System_driving_in_station__inStation__choice_2_tr0();
		} else {
			effect_Drive_System_driving_in_station__inStation__choice_2_tr1();
		}
	}
	
	/* The reactions of state null. */
	private void react_Drive_System_driving_in_station__inStation__choice_3() {
		if (check_Drive_System_driving_in_station__inStation__choice_3_tr0_tr0()) {
			effect_Drive_System_driving_in_station__inStation__choice_3_tr0();
		} else {
			effect_Drive_System_driving_in_station__inStation__choice_3_tr1();
		}
	}
	
	/* The reactions of state null. */
	private void react_Drive_System_driving_in_station__inStation__choice_4() {
		if (check_Drive_System_driving_in_station__inStation__choice_4_tr0_tr0()) {
			effect_Drive_System_driving_in_station__inStation__choice_4_tr0();
		} else {
			effect_Drive_System_driving_in_station__inStation__choice_4_tr1();
		}
	}
	
	/* The reactions of state null. */
	private void react_Drive_System__choice_1() {
		if (check_Drive_System__choice_1_tr0_tr0()) {
			effect_Drive_System__choice_1_tr0();
		} else {
			effect_Drive_System__choice_1_tr1();
		}
	}
	
	/* The reactions of state null. */
	private void react_Drive_System__choice_2() {
		if (check_Drive_System__choice_2_tr0_tr0()) {
			effect_Drive_System__choice_2_tr0();
		} else {
			if (check_Drive_System__choice_2_tr1_tr1()) {
				effect_Drive_System__choice_2_tr1();
			} else {
				effect_Drive_System__choice_2_tr2();
			}
		}
	}
	
	/* The reactions of state null. */
	private void react_Drive_System_unloading__unloading__choice_0() {
		effect_Drive_System_unloading__unloading__choice_0_tr0();
	}
	
	/* The reactions of state null. */
	private void react_Drive_System_unloading__unloading__choice_1() {
		if (check_Drive_System_unloading__unloading__choice_1_tr1_tr1()) {
			effect_Drive_System_unloading__unloading__choice_1_tr1();
		} else {
			effect_Drive_System_unloading__unloading__choice_1_tr0();
		}
	}
	
	/* The reactions of state null. */
	private void react_Drive_System__choice_3() {
		if (check_Drive_System__choice_3_tr0_tr0()) {
			effect_Drive_System__choice_3_tr0();
		} else {
			effect_Drive_System__choice_3_tr1();
		}
	}
	
	/* The reactions of state null. */
	private void react_Drive_System_entering_charger__startCharging__choice_0() {
		effect_Drive_System_entering_charger__startCharging__choice_0_tr0();
	}
	
	/* The reactions of state null. */
	private void react_Drive_System_entering_charger__startCharging__choice_1() {
		if (check_Drive_System_entering_charger__startCharging__choice_1_tr1_tr1()) {
			effect_Drive_System_entering_charger__startCharging__choice_1_tr1();
		} else {
			effect_Drive_System_entering_charger__startCharging__choice_1_tr0();
		}
	}
	
	/* The reactions of state null. */
	private void react_Drive_System__choice_4() {
		effect_Drive_System__choice_4_tr0();
	}
	
	/* The reactions of state null. */
	private void react_Drive_System__choice_5() {
		effect_Drive_System__choice_5_tr0();
	}
	
	/* The reactions of state null. */
	private void react_Drive_System__choice_6() {
		effect_Drive_System__choice_6_tr0();
	}
	
	/* Default react sequence for initial entry  */
	private void react_Drive_System__entry_Default() {
		enterSequence_Drive_System_idle_default();
	}
	
	/* Default react sequence for initial entry  */
	private void react_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_ahead__onCrossroadDriveForward__entry_Default() {
		enterSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_ahead__onCrossroadDriveForward_forward1_default();
	}
	
	/* Default react sequence for initial entry  */
	private void react_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_right_side__onCrossroadDriveRight__entry_Default() {
		enterSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_right_side__onCrossroadDriveRight_right_default();
	}
	
	/* Default react sequence for initial entry  */
	private void react_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft__entry_Default() {
		enterSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward1_default();
	}
	
	/* Default react sequence for initial entry  */
	private void react_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad__entry_Default() {
		enterSequence_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_1_default();
	}
	
	/* Default react sequence for initial entry  */
	private void react_Drive_System_leaving_corassroad__onCrossroadExiting__entry_Default() {
		enterSequence_Drive_System_leaving_corassroad__onCrossroadExiting_waiting_default();
	}
	
	/* Default react sequence for initial entry  */
	private void react_Drive_System_entering_crossroad__onWaypointEnteringCrossroad__entry_Default() {
		react_Drive_System_entering_crossroad__onWaypointEnteringCrossroad__choice_2();
	}
	
	/* Default react sequence for initial entry  */
	private void react_Drive_System_driving_in_station__inStation__entry_Default() {
		react_Drive_System_driving_in_station__inStation__choice_0();
	}
	
	/* Default react sequence for initial entry  */
	private void react_Drive_System_unloading__unloading__entry_Default() {
		react_Drive_System_unloading__unloading__choice_0();
	}
	
	/* Default react sequence for initial entry  */
	private void react_Drive_System_entering_charger__startCharging__entry_Default() {
		react_Drive_System_entering_charger__startCharging__choice_0();
	}
	
	/* The reactions of exit default. */
	private void react_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_ahead__onCrossroadDriveForward__exit_Default() {
		effect_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_ahead_tr0();
	}
	
	/* The reactions of exit default. */
	private void react_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_right_side__onCrossroadDriveRight__exit_Default() {
		effect_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_right_side_tr0();
	}
	
	/* The reactions of exit default. */
	private void react_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft__exit_Default() {
		effect_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side_tr0();
	}
	
	/* The reactions of exit default. */
	private void react_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad__exit_Default() {
		effect_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad_tr0();
	}
	
	/* The reactions of exit default. */
	private void react_Drive_System_leaving_corassroad__onCrossroadExiting__exit_Default() {
		effect_Drive_System_leaving_corassroad_tr0();
	}
	
	/* The reactions of exit default. */
	private void react_Drive_System_entering_crossroad__onWaypointEnteringCrossroad__exit_Default() {
		effect_Drive_System_entering_crossroad_tr0();
	}
	
	/* The reactions of exit default. */
	private void react_Drive_System_driving_in_station__inStation__exit_Default() {
		effect_Drive_System_driving_in_station_tr0();
	}
	
	/* The reactions of exit default. */
	private void react_Drive_System_unloading__unloading__exit_Default() {
		effect_Drive_System_unloading_tr0();
	}
	
	/* The reactions of exit default. */
	private void react_Drive_System_entering_charger__startCharging__exit_Default() {
		effect_Drive_System_entering_charger_tr0();
	}
	
	private boolean react() {
		return false;
	}
	
	private boolean drive_System_idle_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (sCInterface.newTarget) {
				exitSequence_Drive_System_idle();
				setDriveMode(sCIDriveMode.dRIVE);
				
				enterSequence_Drive_System_waiting_on_waypoint_or_station_default();
				react();
			} else {
				if (sCInterface.newChargingTarget) {
					exitSequence_Drive_System_idle();
					setDriveMode(sCIDriveMode.cHARGE);
					
					enterSequence_Drive_System_waiting_on_waypoint_or_station_default();
					react();
				} else {
					if (sCInterface.newUnloadingTarget) {
						exitSequence_Drive_System_idle();
						setDriveMode(sCIDriveMode.uNLOAD);
						
						enterSequence_Drive_System_waiting_on_waypoint_or_station_default();
						react();
					} else {
						did_transition = false;
					}
				}
			}
		}
		if (did_transition==false) {
			did_transition = react();
		}
		return did_transition;
	}
	
	private boolean drive_System_waiting_on_waypoint_or_station_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (sCInterface.dataRefresh) {
				exitSequence_Drive_System_waiting_on_waypoint_or_station();
				react_Drive_System__choice_1();
			} else {
				did_transition = false;
			}
		}
		if (did_transition==false) {
			did_transition = react();
		}
		return did_transition;
	}
	
	private boolean drive_System_leaving_corassroad_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			did_transition = false;
		}
		if (did_transition==false) {
			did_transition = react();
		}
		return did_transition;
	}
	
	private boolean drive_System_leaving_corassroad__onCrossroadExiting_waiting_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (sCInterface.dataRefresh) {
				exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_waiting();
				react_Drive_System_leaving_corassroad__onCrossroadExiting__choice_0();
			} else {
				did_transition = false;
			}
		}
		if (did_transition==false) {
			did_transition = drive_System_leaving_corassroad_react(try_transition);
		}
		return did_transition;
	}
	
	private boolean drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_ahead_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			did_transition = false;
		}
		if (did_transition==false) {
			did_transition = drive_System_leaving_corassroad_react(try_transition);
		}
		return did_transition;
	}
	
	private boolean drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_ahead__onCrossroadDriveForward_forward1_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (sCInterface.actionCompleted) {
				exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_ahead__onCrossroadDriveForward_forward1();
				enterSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_ahead__onCrossroadDriveForward_forward2_default();
				drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_ahead_react(false);
			} else {
				did_transition = false;
			}
		}
		if (did_transition==false) {
			did_transition = drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_ahead_react(try_transition);
		}
		return did_transition;
	}
	
	private boolean drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_ahead__onCrossroadDriveForward_forward2_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (sCInterface.actionCompleted) {
				exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_ahead__onCrossroadDriveForward_forward2();
				react_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_ahead__onCrossroadDriveForward__exit_Default();
			} else {
				did_transition = false;
			}
		}
		if (did_transition==false) {
			did_transition = drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_ahead_react(try_transition);
		}
		return did_transition;
	}
	
	private boolean drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_right_side_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			did_transition = false;
		}
		if (did_transition==false) {
			did_transition = drive_System_leaving_corassroad_react(try_transition);
		}
		return did_transition;
	}
	
	private boolean drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_right_side__onCrossroadDriveRight_right_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (sCInterface.actionCompleted) {
				exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_right_side__onCrossroadDriveRight_right();
				enterSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_right_side__onCrossroadDriveRight_forward_default();
				drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_right_side_react(false);
			} else {
				did_transition = false;
			}
		}
		if (did_transition==false) {
			did_transition = drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_right_side_react(try_transition);
		}
		return did_transition;
	}
	
	private boolean drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_right_side__onCrossroadDriveRight_forward_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (sCInterface.actionCompleted) {
				exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_right_side__onCrossroadDriveRight_forward();
				react_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_right_side__onCrossroadDriveRight__exit_Default();
			} else {
				did_transition = false;
			}
		}
		if (did_transition==false) {
			did_transition = drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_right_side_react(try_transition);
		}
		return did_transition;
	}
	
	private boolean drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			did_transition = false;
		}
		if (did_transition==false) {
			did_transition = drive_System_leaving_corassroad_react(try_transition);
		}
		return did_transition;
	}
	
	private boolean drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward1_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (sCInterface.actionCompleted) {
				exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward1();
				enterSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_left_default();
				drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side_react(false);
			} else {
				did_transition = false;
			}
		}
		if (did_transition==false) {
			did_transition = drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side_react(try_transition);
		}
		return did_transition;
	}
	
	private boolean drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_left_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (sCInterface.actionCompleted) {
				exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_left();
				enterSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward2_default();
				drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side_react(false);
			} else {
				did_transition = false;
			}
		}
		if (did_transition==false) {
			did_transition = drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side_react(try_transition);
		}
		return did_transition;
	}
	
	private boolean drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward2_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (sCInterface.actionCompleted) {
				exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward2();
				enterSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward3_default();
				drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side_react(false);
			} else {
				did_transition = false;
			}
		}
		if (did_transition==false) {
			did_transition = drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side_react(try_transition);
		}
		return did_transition;
	}
	
	private boolean drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward3_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (sCInterface.actionCompleted) {
				exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft_forward3();
				react_Drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side__onCrossroadDriveLeft__exit_Default();
			} else {
				did_transition = false;
			}
		}
		if (did_transition==false) {
			did_transition = drive_System_leaving_corassroad__onCrossroadExiting_leaving_crossroad_to_left_side_react(try_transition);
		}
		return did_transition;
	}
	
	private boolean drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			did_transition = false;
		}
		if (did_transition==false) {
			did_transition = drive_System_leaving_corassroad_react(try_transition);
		}
		return did_transition;
	}
	
	private boolean drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_1_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (sCInterface.actionCompleted) {
				exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_1();
				enterSequence_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_1_default();
				drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad_react(false);
			} else {
				did_transition = false;
			}
		}
		if (did_transition==false) {
			did_transition = drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad_react(try_transition);
		}
		return did_transition;
	}
	
	private boolean drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_1_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (sCInterface.dataRefresh) {
				exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_1();
				react_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad__choice_0();
			} else {
				did_transition = false;
			}
		}
		if (did_transition==false) {
			did_transition = drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad_react(try_transition);
		}
		return did_transition;
	}
	
	private boolean drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_1_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (sCInterface.actionCompleted) {
				exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_1();
				enterSequence_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_2_default();
				drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad_react(false);
			} else {
				did_transition = false;
			}
		}
		if (did_transition==false) {
			did_transition = drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad_react(try_transition);
		}
		return did_transition;
	}
	
	private boolean drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_2_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (sCInterface.actionCompleted) {
				exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_turining_left_2();
				enterSequence_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_2_default();
				drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad_react(false);
			} else {
				did_transition = false;
			}
		}
		if (did_transition==false) {
			did_transition = drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad_react(try_transition);
		}
		return did_transition;
	}
	
	private boolean drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_2_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (sCInterface.dataRefresh) {
				exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_waiting_to_drive_forward_2();
				react_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad__choice_1();
			} else {
				did_transition = false;
			}
		}
		if (did_transition==false) {
			did_transition = drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad_react(try_transition);
		}
		return did_transition;
	}
	
	private boolean drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_2_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (sCInterface.actionCompleted) {
				exitSequence_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad_driving_forward_2();
				react_Drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad__turningAroundOnCrossroad__exit_Default();
			} else {
				did_transition = false;
			}
		}
		if (did_transition==false) {
			did_transition = drive_System_leaving_corassroad__onCrossroadExiting_turning_around_on_crossroad_react(try_transition);
		}
		return did_transition;
	}
	
	private boolean drive_System_entering_crossroad_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			did_transition = false;
		}
		if (did_transition==false) {
			did_transition = react();
		}
		return did_transition;
	}
	
	private boolean drive_System_entering_crossroad__onWaypointEnteringCrossroad_waiting_on_waypoint_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (sCInterface.dataRefresh) {
				exitSequence_Drive_System_entering_crossroad__onWaypointEnteringCrossroad_waiting_on_waypoint();
				react_Drive_System_entering_crossroad__onWaypointEnteringCrossroad__choice_2();
			} else {
				did_transition = false;
			}
		}
		if (did_transition==false) {
			did_transition = drive_System_entering_crossroad_react(try_transition);
		}
		return did_transition;
	}
	
	private boolean drive_System_entering_crossroad__onWaypointEnteringCrossroad_waiting_to_ensure_real_deadlock_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (timeEvents[0]) {
				exitSequence_Drive_System_entering_crossroad__onWaypointEnteringCrossroad_waiting_to_ensure_real_deadlock();
				setWaitedForDeadlock(getWaitedForDeadlock() + 1);
				
				enterSequence_Drive_System_entering_crossroad__onWaypointEnteringCrossroad_waiting_on_waypoint_default();
				drive_System_entering_crossroad_react(false);
			} else {
				did_transition = false;
			}
		}
		if (did_transition==false) {
			did_transition = drive_System_entering_crossroad_react(try_transition);
		}
		return did_transition;
	}
	
	private boolean drive_System_entering_crossroad__onWaypointEnteringCrossroad_entering_crossroad_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (sCInterface.actionCompleted) {
				exitSequence_Drive_System_entering_crossroad__onWaypointEnteringCrossroad_entering_crossroad();
				react_Drive_System_entering_crossroad__onWaypointEnteringCrossroad__exit_Default();
			} else {
				did_transition = false;
			}
		}
		if (did_transition==false) {
			did_transition = drive_System_entering_crossroad_react(try_transition);
		}
		return did_transition;
	}
	
	private boolean drive_System_driving_in_station_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			did_transition = false;
		}
		if (did_transition==false) {
			did_transition = react();
		}
		return did_transition;
	}
	
	private boolean drive_System_driving_in_station__inStation_turn_right_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (sCInterface.actionCompleted) {
				exitSequence_Drive_System_driving_in_station__inStation_turn_right();
				react_Drive_System_driving_in_station__inStation__choice_1();
			} else {
				did_transition = false;
			}
		}
		if (did_transition==false) {
			did_transition = drive_System_driving_in_station_react(try_transition);
		}
		return did_transition;
	}
	
	private boolean drive_System_driving_in_station__inStation_turn_left_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (sCInterface.actionCompleted) {
				exitSequence_Drive_System_driving_in_station__inStation_turn_left();
				react_Drive_System_driving_in_station__inStation__choice_1();
			} else {
				did_transition = false;
			}
		}
		if (did_transition==false) {
			did_transition = drive_System_driving_in_station_react(try_transition);
		}
		return did_transition;
	}
	
	private boolean drive_System_driving_in_station__inStation_drive_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (sCInterface.actionCompleted) {
				exitSequence_Drive_System_driving_in_station__inStation_drive();
				react_Drive_System_driving_in_station__inStation__choice_1();
			} else {
				did_transition = false;
			}
		}
		if (did_transition==false) {
			did_transition = drive_System_driving_in_station_react(try_transition);
		}
		return did_transition;
	}
	
	private boolean drive_System_driving_in_station__inStation_wait_to_drive_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (sCInterface.dataRefresh) {
				exitSequence_Drive_System_driving_in_station__inStation_wait_to_drive();
				react_Drive_System_driving_in_station__inStation__choice_4();
			} else {
				did_transition = false;
			}
		}
		if (did_transition==false) {
			did_transition = drive_System_driving_in_station_react(try_transition);
		}
		return did_transition;
	}
	
	private boolean drive_System_unloading_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			did_transition = false;
		}
		if (did_transition==false) {
			did_transition = react();
		}
		return did_transition;
	}
	
	private boolean drive_System_unloading__unloading_unloading_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (sCInterface.actionCompleted) {
				exitSequence_Drive_System_unloading__unloading_unloading();
				react_Drive_System_unloading__unloading__exit_Default();
			} else {
				did_transition = false;
			}
		}
		if (did_transition==false) {
			did_transition = drive_System_unloading_react(try_transition);
		}
		return did_transition;
	}
	
	private boolean drive_System_unloading__unloading_turning_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (sCInterface.actionCompleted) {
				exitSequence_Drive_System_unloading__unloading_turning();
				enterSequence_Drive_System_unloading__unloading_waiting_for_data_default();
				drive_System_unloading_react(false);
			} else {
				did_transition = false;
			}
		}
		if (did_transition==false) {
			did_transition = drive_System_unloading_react(try_transition);
		}
		return did_transition;
	}
	
	private boolean drive_System_unloading__unloading_waiting_for_data_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (sCInterface.dataRefresh) {
				exitSequence_Drive_System_unloading__unloading_waiting_for_data();
				react_Drive_System_unloading__unloading__choice_0();
			} else {
				did_transition = false;
			}
		}
		if (did_transition==false) {
			did_transition = drive_System_unloading_react(try_transition);
		}
		return did_transition;
	}
	
	private boolean drive_System_entering_charger_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			did_transition = false;
		}
		if (did_transition==false) {
			did_transition = react();
		}
		return did_transition;
	}
	
	private boolean drive_System_entering_charger__startCharging_unloading_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (sCInterface.actionCompleted) {
				exitSequence_Drive_System_entering_charger__startCharging_unloading();
				react_Drive_System_entering_charger__startCharging__exit_Default();
			} else {
				did_transition = false;
			}
		}
		if (did_transition==false) {
			did_transition = drive_System_entering_charger_react(try_transition);
		}
		return did_transition;
	}
	
	private boolean drive_System_entering_charger__startCharging_turning_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (sCInterface.actionCompleted) {
				exitSequence_Drive_System_entering_charger__startCharging_turning();
				enterSequence_Drive_System_entering_charger__startCharging_waiting_for_data_default();
				drive_System_entering_charger_react(false);
			} else {
				did_transition = false;
			}
		}
		if (did_transition==false) {
			did_transition = drive_System_entering_charger_react(try_transition);
		}
		return did_transition;
	}
	
	private boolean drive_System_entering_charger__startCharging_waiting_for_data_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (sCInterface.dataRefresh) {
				exitSequence_Drive_System_entering_charger__startCharging_waiting_for_data();
				react_Drive_System_entering_charger__startCharging__choice_0();
			} else {
				did_transition = false;
			}
		}
		if (did_transition==false) {
			did_transition = drive_System_entering_charger_react(try_transition);
		}
		return did_transition;
	}
	
}
