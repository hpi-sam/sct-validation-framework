package de.hpi.mod.sim.drivesystem;
import de.hpi.mod.sim.IStatemachine;
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
		drive_System_unloading_r1_waiting_for_orientation_check,
		drive_System_unloading_r1_unloading,
		drive_System_unloading_r1_turning_left,
		drive_System_driving,
		drive_System_driving__region0_on_waypoint_or_station,
		drive_System_driving__region0_waiting_in_station,
		drive_System_driving__region0_waiting_on_waypoint,
		drive_System_driving__region0_waiting_to_ensure_real_deadlock,
		drive_System_driving__region0_on_crossroad,
		drive_System_driving__region0_driving_forward_in_station,
		drive_System_driving__region0_turning_right_in_station,
		drive_System_driving__region0_turning_left_in_station,
		drive_System_driving__region0_exiting_charging_position,
		drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right1,
		drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right2,
		drive_System_driving__region0_exiting_charging_position_inStationExitCharger_forward,
		drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right3,
		drive_System_driving__region0_turning_around_on_waypoint,
		drive_System_driving__region0_turning_around_on_waypoint__region0_turning_left_to_change_lane,
		drive_System_driving__region0_turning_around_on_waypoint__region0_waiting_for_free_crossroad,
		drive_System_driving__region0_turning_around_on_waypoint__region0_switching_lane,
		drive_System_driving__region0_turning_around_on_waypoint__region0_turning_left_on_new_lane,
		drive_System_driving__region0_entering_crossroad,
		drive_System_driving__region0_leaving_crossroad_ahead,
		drive_System_driving__region0_leaving_crossroad_ahead_onCrossroadDriveForward_forward1,
		drive_System_driving__region0_leaving_crossroad_ahead_onCrossroadDriveForward_forward2,
		drive_System_driving__region0_leaving_crossroad_to_right_side,
		drive_System_driving__region0_leaving_crossroad_to_right_side_onCrossroadDriveRight_right,
		drive_System_driving__region0_leaving_crossroad_to_right_side_onCrossroadDriveRight_forward,
		drive_System_driving__region0_leaving_crossroad_to_left_side,
		drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward1,
		drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_left,
		drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward2,
		drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward3,
		$NullState$
	};
	
	private final State[] stateVector = new State[1];
	
	private int nextStateIndex;
	
	private ITimer timer;
	
	private final boolean[] timeEvents = new boolean[1];
	private boolean waitedForDeadlock;
	
	protected void setWaitedForDeadlock(boolean value) {
		waitedForDeadlock = value;
	}
	
	protected boolean getWaitedForDeadlock() {
		return waitedForDeadlock;
	}
	
	public DrivesystemStatemachine() {
		sCInterface = new SCInterfaceImpl();
		sCIProcessor = new SCIProcessorImpl();
		sCIActors = new SCIActorsImpl();
		sCIData = new SCIDataImpl();
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
		
		for (int i = 0; i < 1; i++) {
			stateVector[i] = State.$NullState$;
		}
		clearEvents();
		clearOutEvents();
		setWaitedForDeadlock(false);
		
		sCIPositionType.setWAYPOINT(0);
		
		sCIPositionType.setSTATION(1);
		
		sCIPositionType.setCROSSROAD(2);
		
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
					"The state machine needs to be initialized first by calling the init() function.");
		}
		if (timer == null) {
			throw new IllegalStateException("timer not set.");
		}
		enterSequence_Drive_System_default();
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
			return stateVector[0].ordinal() >= State.
					drive_System_unloading.ordinal()&& stateVector[0].ordinal() <= State.drive_System_unloading_r1_turning_left.ordinal();
		case drive_System_unloading_r1_waiting_for_orientation_check:
			return stateVector[0] == State.drive_System_unloading_r1_waiting_for_orientation_check;
		case drive_System_unloading_r1_unloading:
			return stateVector[0] == State.drive_System_unloading_r1_unloading;
		case drive_System_unloading_r1_turning_left:
			return stateVector[0] == State.drive_System_unloading_r1_turning_left;
		case drive_System_driving:
			return stateVector[0].ordinal() >= State.
					drive_System_driving.ordinal()&& stateVector[0].ordinal() <= State.drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward3.ordinal();
		case drive_System_driving__region0_on_waypoint_or_station:
			return stateVector[0] == State.drive_System_driving__region0_on_waypoint_or_station;
		case drive_System_driving__region0_waiting_in_station:
			return stateVector[0] == State.drive_System_driving__region0_waiting_in_station;
		case drive_System_driving__region0_waiting_on_waypoint:
			return stateVector[0] == State.drive_System_driving__region0_waiting_on_waypoint;
		case drive_System_driving__region0_waiting_to_ensure_real_deadlock:
			return stateVector[0] == State.drive_System_driving__region0_waiting_to_ensure_real_deadlock;
		case drive_System_driving__region0_on_crossroad:
			return stateVector[0] == State.drive_System_driving__region0_on_crossroad;
		case drive_System_driving__region0_driving_forward_in_station:
			return stateVector[0] == State.drive_System_driving__region0_driving_forward_in_station;
		case drive_System_driving__region0_turning_right_in_station:
			return stateVector[0] == State.drive_System_driving__region0_turning_right_in_station;
		case drive_System_driving__region0_turning_left_in_station:
			return stateVector[0] == State.drive_System_driving__region0_turning_left_in_station;
		case drive_System_driving__region0_exiting_charging_position:
			return stateVector[0].ordinal() >= State.
					drive_System_driving__region0_exiting_charging_position.ordinal()&& stateVector[0].ordinal() <= State.drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right3.ordinal();
		case drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right1:
			return stateVector[0] == State.drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right1;
		case drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right2:
			return stateVector[0] == State.drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right2;
		case drive_System_driving__region0_exiting_charging_position_inStationExitCharger_forward:
			return stateVector[0] == State.drive_System_driving__region0_exiting_charging_position_inStationExitCharger_forward;
		case drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right3:
			return stateVector[0] == State.drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right3;
		case drive_System_driving__region0_turning_around_on_waypoint:
			return stateVector[0].ordinal() >= State.
					drive_System_driving__region0_turning_around_on_waypoint.ordinal()&& stateVector[0].ordinal() <= State.drive_System_driving__region0_turning_around_on_waypoint__region0_turning_left_on_new_lane.ordinal();
		case drive_System_driving__region0_turning_around_on_waypoint__region0_turning_left_to_change_lane:
			return stateVector[0] == State.drive_System_driving__region0_turning_around_on_waypoint__region0_turning_left_to_change_lane;
		case drive_System_driving__region0_turning_around_on_waypoint__region0_waiting_for_free_crossroad:
			return stateVector[0] == State.drive_System_driving__region0_turning_around_on_waypoint__region0_waiting_for_free_crossroad;
		case drive_System_driving__region0_turning_around_on_waypoint__region0_switching_lane:
			return stateVector[0] == State.drive_System_driving__region0_turning_around_on_waypoint__region0_switching_lane;
		case drive_System_driving__region0_turning_around_on_waypoint__region0_turning_left_on_new_lane:
			return stateVector[0] == State.drive_System_driving__region0_turning_around_on_waypoint__region0_turning_left_on_new_lane;
		case drive_System_driving__region0_entering_crossroad:
			return stateVector[0] == State.drive_System_driving__region0_entering_crossroad;
		case drive_System_driving__region0_leaving_crossroad_ahead:
			return stateVector[0].ordinal() >= State.
					drive_System_driving__region0_leaving_crossroad_ahead.ordinal()&& stateVector[0].ordinal() <= State.drive_System_driving__region0_leaving_crossroad_ahead_onCrossroadDriveForward_forward2.ordinal();
		case drive_System_driving__region0_leaving_crossroad_ahead_onCrossroadDriveForward_forward1:
			return stateVector[0] == State.drive_System_driving__region0_leaving_crossroad_ahead_onCrossroadDriveForward_forward1;
		case drive_System_driving__region0_leaving_crossroad_ahead_onCrossroadDriveForward_forward2:
			return stateVector[0] == State.drive_System_driving__region0_leaving_crossroad_ahead_onCrossroadDriveForward_forward2;
		case drive_System_driving__region0_leaving_crossroad_to_right_side:
			return stateVector[0].ordinal() >= State.
					drive_System_driving__region0_leaving_crossroad_to_right_side.ordinal()&& stateVector[0].ordinal() <= State.drive_System_driving__region0_leaving_crossroad_to_right_side_onCrossroadDriveRight_forward.ordinal();
		case drive_System_driving__region0_leaving_crossroad_to_right_side_onCrossroadDriveRight_right:
			return stateVector[0] == State.drive_System_driving__region0_leaving_crossroad_to_right_side_onCrossroadDriveRight_right;
		case drive_System_driving__region0_leaving_crossroad_to_right_side_onCrossroadDriveRight_forward:
			return stateVector[0] == State.drive_System_driving__region0_leaving_crossroad_to_right_side_onCrossroadDriveRight_forward;
		case drive_System_driving__region0_leaving_crossroad_to_left_side:
			return stateVector[0].ordinal() >= State.
					drive_System_driving__region0_leaving_crossroad_to_left_side.ordinal()&& stateVector[0].ordinal() <= State.drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward3.ordinal();
		case drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward1:
			return stateVector[0] == State.drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward1;
		case drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_left:
			return stateVector[0] == State.drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_left;
		case drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward2:
			return stateVector[0] == State.drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward2;
		case drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward3:
			return stateVector[0] == State.drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward3;
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
	
	private boolean check_Drive_System_idle_tr0_tr0() {
		return sCInterface.unload;
	}
	
	private boolean check_Drive_System_idle_tr1_tr1() {
		return sCInterface.newTarget;
	}
	
	private boolean check_Drive_System_unloading_tr1_tr1() {
		return sCInterface.stop;
	}
	
	private boolean check_Drive_System_unloading_r1_waiting_for_orientation_check_tr0_tr0() {
		return sCInterface.dataRefresh;
	}
	
	private boolean check_Drive_System_unloading_r1_unloading_tr0_tr0() {
		return sCInterface.unloaded;
	}
	
	private boolean check_Drive_System_unloading_r1_turning_left_tr0_tr0() {
		return sCInterface.actionCompleted;
	}
	
	private boolean check_Drive_System_driving_tr1_tr1() {
		return sCInterface.stop;
	}
	
	private boolean check_Drive_System_driving__region0_on_waypoint_or_station_tr0_tr0() {
		return sCInterface.dataRefresh;
	}
	
	private boolean check_Drive_System_driving__region0_waiting_in_station_tr0_tr0() {
		return sCInterface.dataRefresh;
	}
	
	private boolean check_Drive_System_driving__region0_waiting_on_waypoint_tr0_tr0() {
		return sCInterface.dataRefresh;
	}
	
	private boolean check_Drive_System_driving__region0_waiting_to_ensure_real_deadlock_tr0_tr0() {
		return timeEvents[0];
	}
	
	private boolean check_Drive_System_driving__region0_on_crossroad_tr0_tr0() {
		return sCInterface.dataRefresh;
	}
	
	private boolean check_Drive_System_driving__region0_driving_forward_in_station_tr0_tr0() {
		return sCInterface.actionCompleted;
	}
	
	private boolean check_Drive_System_driving__region0_turning_right_in_station_tr0_tr0() {
		return sCInterface.actionCompleted;
	}
	
	private boolean check_Drive_System_driving__region0_turning_left_in_station_tr0_tr0() {
		return sCInterface.actionCompleted;
	}
	
	private boolean check_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right1_tr0_tr0() {
		return sCInterface.actionCompleted;
	}
	
	private boolean check_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right2_tr0_tr0() {
		return sCInterface.actionCompleted;
	}
	
	private boolean check_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger_forward_tr0_tr0() {
		return sCInterface.actionCompleted;
	}
	
	private boolean check_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right3_tr0_tr0() {
		return sCInterface.actionCompleted;
	}
	
	private boolean check_Drive_System_driving__region0_turning_around_on_waypoint__region0_turning_left_to_change_lane_tr0_tr0() {
		return sCInterface.actionCompleted;
	}
	
	private boolean check_Drive_System_driving__region0_turning_around_on_waypoint__region0_waiting_for_free_crossroad_tr0_tr0() {
		return (sCInterface.dataRefresh) && (!sCIData.operationCallback.blockedCrossroadRight() && !sCIData.operationCallback.blockedFront());
	}
	
	private boolean check_Drive_System_driving__region0_turning_around_on_waypoint__region0_switching_lane_tr0_tr0() {
		return sCInterface.actionCompleted;
	}
	
	private boolean check_Drive_System_driving__region0_turning_around_on_waypoint__region0_turning_left_on_new_lane_tr0_tr0() {
		return sCInterface.actionCompleted;
	}
	
	private boolean check_Drive_System_driving__region0_entering_crossroad_tr0_tr0() {
		return sCInterface.actionCompleted;
	}
	
	private boolean check_Drive_System_driving__region0_leaving_crossroad_ahead_onCrossroadDriveForward_forward1_tr0_tr0() {
		return sCInterface.actionCompleted;
	}
	
	private boolean check_Drive_System_driving__region0_leaving_crossroad_ahead_onCrossroadDriveForward_forward2_tr0_tr0() {
		return sCInterface.actionCompleted;
	}
	
	private boolean check_Drive_System_driving__region0_leaving_crossroad_to_right_side_onCrossroadDriveRight_right_tr0_tr0() {
		return sCInterface.actionCompleted;
	}
	
	private boolean check_Drive_System_driving__region0_leaving_crossroad_to_right_side_onCrossroadDriveRight_forward_tr0_tr0() {
		return sCInterface.actionCompleted;
	}
	
	private boolean check_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward1_tr0_tr0() {
		return sCInterface.actionCompleted;
	}
	
	private boolean check_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_left_tr0_tr0() {
		return sCInterface.actionCompleted;
	}
	
	private boolean check_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward2_tr0_tr0() {
		return sCInterface.actionCompleted;
	}
	
	private boolean check_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward3_tr0_tr0() {
		return sCInterface.actionCompleted;
	}
	
	private boolean check_Drive_System_unloading_r1__choice_0_tr0_tr0() {
		return sCIData.operationCallback.posOrientation()==sCIOrientation.getEAST();
	}
	
	private boolean check_Drive_System_unloading_r1__choice_0_tr1_tr1() {
		return true;
	}
	
	private boolean check_Drive_System_driving__region0__choice_0_tr1_tr1() {
		return sCIData.operationCallback.targetDirection()==sCIDirection.getAHEAD() && !sCIData.operationCallback.blockedFront();
	}
	
	private boolean check_Drive_System_driving__region0__choice_0_tr2_tr2() {
		return sCIData.operationCallback.targetDirection()==sCIDirection.getRIGHT();
	}
	
	private boolean check_Drive_System_driving__region0__choice_0_tr3_tr3() {
		return sCIData.operationCallback.targetDirection()==sCIDirection.getLEFT();
	}
	
	private boolean check_Drive_System_driving__region0__choice_0_tr4_tr4() {
		return sCIData.operationCallback.targetDirection()==sCIDirection.getBEHIND();
	}
	
	private boolean check_Drive_System_driving__region0__choice_0_tr0_tr0() {
		return true;
	}
	
	private boolean check_Drive_System_driving__region0__choice_1_tr1_tr1() {
		return sCIData.operationCallback.targetDirection()==sCIDirection.getAHEAD() && !sCIData.operationCallback.blockedWaypointAhead();
	}
	
	private boolean check_Drive_System_driving__region0__choice_1_tr2_tr2() {
		return sCIData.operationCallback.targetDirection()==sCIDirection.getRIGHT() && !sCIData.operationCallback.blockedWaypointRight();
	}
	
	private boolean check_Drive_System_driving__region0__choice_1_tr3_tr3() {
		return sCIData.operationCallback.targetDirection()==sCIDirection.getLEFT() && !sCIData.operationCallback.blockedWaypointLeft() && !sCIData.operationCallback.blockedWaypointAhead();
	}
	
	private boolean check_Drive_System_driving__region0__choice_1_tr0_tr0() {
		return true;
	}
	
	private boolean check_Drive_System_driving__region0__choice_2_tr1_tr1() {
		return getWaitedForDeadlock();
	}
	
	private boolean check_Drive_System_driving__region0__choice_2_tr0_tr0() {
		return true;
	}
	
	private boolean check_Drive_System_driving__region0__choice_3_tr1_tr1() {
		return !sCIData.operationCallback.blockedCrossroadAhead() && sCIData.operationCallback.blockedWaypointAhead() && sCIData.operationCallback.blockedWaypointLeft() && sCIData.operationCallback.blockedWaypointRight() && sCIData.operationCallback.posOrientation()==sCIOrientation.getSOUTH();
	}
	
	private boolean check_Drive_System_driving__region0__choice_3_tr2_tr2() {
		return !sCIData.operationCallback.blockedCrossroadAhead() && !sCIData.operationCallback.blockedWaypointRight();
	}
	
	private boolean check_Drive_System_driving__region0__choice_3_tr0_tr0() {
		return true;
	}
	
	private boolean check_Drive_System_driving__region0__choice_4_tr0() {
		return true;
	}
	
	private boolean check_Drive_System_driving__region0__choice_5_tr1_tr1() {
		return sCIData.operationCallback.targetDirection()==sCIDirection.getBEHIND();
	}
	
	private boolean check_Drive_System_driving__region0__choice_5_tr0_tr0() {
		return true;
	}
	
	private boolean check_Drive_System_driving__region0__choice_6_tr0() {
		return true;
	}
	
	private boolean check_Drive_System_driving__region0__choice_7_tr1_tr1() {
		return sCIData.operationCallback.isTargetReached();
	}
	
	private boolean check_Drive_System_driving__region0__choice_7_tr0_tr0() {
		return true;
	}
	
	private boolean check_Drive_System_driving__region0__choice_8_tr1_tr1() {
		return sCIData.operationCallback.posType()==sCIPositionType.getWAYPOINT();
	}
	
	private boolean check_Drive_System_driving__region0__choice_8_tr0_tr0() {
		return true;
	}
	
	private boolean check_Drive_System_driving__region0__choice_9_tr0() {
		return true;
	}
	
	private void effect_Drive_System_idle_tr0() {
		exitSequence_Drive_System_idle();
		enterSequence_Drive_System_unloading_default();
	}
	
	private void effect_Drive_System_idle_tr1() {
		exitSequence_Drive_System_idle();
		enterSequence_Drive_System_driving_default();
	}
	
	private void effect_Drive_System_unloading_tr0() {
		exitSequence_Drive_System_unloading();
		sCIProcessor.raiseUnloaded();
		
		enterSequence_Drive_System_idle_default();
	}
	
	private void effect_Drive_System_unloading_tr1() {
		exitSequence_Drive_System_unloading();
		enterSequence_Drive_System_idle_default();
	}
	
	private void effect_Drive_System_unloading_r1_waiting_for_orientation_check_tr0() {
		exitSequence_Drive_System_unloading_r1_waiting_for_orientation_check();
		react_Drive_System_unloading_r1__choice_0();
	}
	
	private void effect_Drive_System_unloading_r1_unloading_tr0() {
		exitSequence_Drive_System_unloading_r1_unloading();
		react_Drive_System_unloading_r1__exit_Default();
	}
	
	private void effect_Drive_System_unloading_r1_turning_left_tr0() {
		exitSequence_Drive_System_unloading_r1_turning_left();
		enterSequence_Drive_System_unloading_r1_waiting_for_orientation_check_default();
	}
	
	private void effect_Drive_System_driving_tr0() {
		exitSequence_Drive_System_driving();
		sCIProcessor.raiseArrived();
		
		enterSequence_Drive_System_idle_default();
	}
	
	private void effect_Drive_System_driving_tr1() {
		exitSequence_Drive_System_driving();
		enterSequence_Drive_System_idle_default();
	}
	
	private void effect_Drive_System_driving__region0_on_waypoint_or_station_tr0() {
		exitSequence_Drive_System_driving__region0_on_waypoint_or_station();
		react_Drive_System_driving__region0__choice_7();
	}
	
	private void effect_Drive_System_driving__region0_waiting_in_station_tr0() {
		exitSequence_Drive_System_driving__region0_waiting_in_station();
		react_Drive_System_driving__region0__choice_6();
	}
	
	private void effect_Drive_System_driving__region0_waiting_on_waypoint_tr0() {
		exitSequence_Drive_System_driving__region0_waiting_on_waypoint();
		react_Drive_System_driving__region0__choice_4();
	}
	
	private void effect_Drive_System_driving__region0_waiting_to_ensure_real_deadlock_tr0() {
		exitSequence_Drive_System_driving__region0_waiting_to_ensure_real_deadlock();
		setWaitedForDeadlock(true);
		
		enterSequence_Drive_System_driving__region0_waiting_on_waypoint_default();
	}
	
	private void effect_Drive_System_driving__region0_on_crossroad_tr0() {
		exitSequence_Drive_System_driving__region0_on_crossroad();
		react_Drive_System_driving__region0__choice_1();
	}
	
	private void effect_Drive_System_driving__region0_driving_forward_in_station_tr0() {
		exitSequence_Drive_System_driving__region0_driving_forward_in_station();
		react_Drive_System_driving__region0__choice_9();
	}
	
	private void effect_Drive_System_driving__region0_turning_right_in_station_tr0() {
		exitSequence_Drive_System_driving__region0_turning_right_in_station();
		react_Drive_System_driving__region0__choice_9();
	}
	
	private void effect_Drive_System_driving__region0_turning_left_in_station_tr0() {
		exitSequence_Drive_System_driving__region0_turning_left_in_station();
		react_Drive_System_driving__region0__choice_9();
	}
	
	private void effect_Drive_System_driving__region0_exiting_charging_position_tr0() {
		exitSequence_Drive_System_driving__region0_exiting_charging_position();
		react_Drive_System_driving__region0__choice_9();
	}
	
	private void effect_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right1_tr0() {
		exitSequence_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right1();
		enterSequence_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right2_default();
	}
	
	private void effect_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right2_tr0() {
		exitSequence_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right2();
		enterSequence_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger_forward_default();
	}
	
	private void effect_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger_forward_tr0() {
		exitSequence_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger_forward();
		enterSequence_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right3_default();
	}
	
	private void effect_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right3_tr0() {
		exitSequence_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right3();
		react_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger__exit_Default();
	}
	
	private void effect_Drive_System_driving__region0_turning_around_on_waypoint_tr0() {
		exitSequence_Drive_System_driving__region0_turning_around_on_waypoint();
		react_Drive_System_driving__region0__choice_9();
	}
	
	private void effect_Drive_System_driving__region0_turning_around_on_waypoint__region0_turning_left_to_change_lane_tr0() {
		exitSequence_Drive_System_driving__region0_turning_around_on_waypoint__region0_turning_left_to_change_lane();
		enterSequence_Drive_System_driving__region0_turning_around_on_waypoint__region0_waiting_for_free_crossroad_default();
	}
	
	private void effect_Drive_System_driving__region0_turning_around_on_waypoint__region0_waiting_for_free_crossroad_tr0() {
		exitSequence_Drive_System_driving__region0_turning_around_on_waypoint__region0_waiting_for_free_crossroad();
		enterSequence_Drive_System_driving__region0_turning_around_on_waypoint__region0_switching_lane_default();
	}
	
	private void effect_Drive_System_driving__region0_turning_around_on_waypoint__region0_switching_lane_tr0() {
		exitSequence_Drive_System_driving__region0_turning_around_on_waypoint__region0_switching_lane();
		enterSequence_Drive_System_driving__region0_turning_around_on_waypoint__region0_turning_left_on_new_lane_default();
	}
	
	private void effect_Drive_System_driving__region0_turning_around_on_waypoint__region0_turning_left_on_new_lane_tr0() {
		exitSequence_Drive_System_driving__region0_turning_around_on_waypoint__region0_turning_left_on_new_lane();
		react_Drive_System_driving__region0_turning_around_on_waypoint__region0__exit_Default();
	}
	
	private void effect_Drive_System_driving__region0_entering_crossroad_tr0() {
		exitSequence_Drive_System_driving__region0_entering_crossroad();
		enterSequence_Drive_System_driving__region0_on_crossroad_default();
	}
	
	private void effect_Drive_System_driving__region0_leaving_crossroad_ahead_tr0() {
		exitSequence_Drive_System_driving__region0_leaving_crossroad_ahead();
		enterSequence_Drive_System_driving__region0_on_waypoint_or_station_default();
	}
	
	private void effect_Drive_System_driving__region0_leaving_crossroad_ahead_onCrossroadDriveForward_forward1_tr0() {
		exitSequence_Drive_System_driving__region0_leaving_crossroad_ahead_onCrossroadDriveForward_forward1();
		enterSequence_Drive_System_driving__region0_leaving_crossroad_ahead_onCrossroadDriveForward_forward2_default();
	}
	
	private void effect_Drive_System_driving__region0_leaving_crossroad_ahead_onCrossroadDriveForward_forward2_tr0() {
		exitSequence_Drive_System_driving__region0_leaving_crossroad_ahead_onCrossroadDriveForward_forward2();
		react_Drive_System_driving__region0_leaving_crossroad_ahead_onCrossroadDriveForward__exit_Default();
	}
	
	private void effect_Drive_System_driving__region0_leaving_crossroad_to_right_side_tr0() {
		exitSequence_Drive_System_driving__region0_leaving_crossroad_to_right_side();
		enterSequence_Drive_System_driving__region0_on_waypoint_or_station_default();
	}
	
	private void effect_Drive_System_driving__region0_leaving_crossroad_to_right_side_onCrossroadDriveRight_right_tr0() {
		exitSequence_Drive_System_driving__region0_leaving_crossroad_to_right_side_onCrossroadDriveRight_right();
		enterSequence_Drive_System_driving__region0_leaving_crossroad_to_right_side_onCrossroadDriveRight_forward_default();
	}
	
	private void effect_Drive_System_driving__region0_leaving_crossroad_to_right_side_onCrossroadDriveRight_forward_tr0() {
		exitSequence_Drive_System_driving__region0_leaving_crossroad_to_right_side_onCrossroadDriveRight_forward();
		react_Drive_System_driving__region0_leaving_crossroad_to_right_side_onCrossroadDriveRight__exit_Default();
	}
	
	private void effect_Drive_System_driving__region0_leaving_crossroad_to_left_side_tr0() {
		exitSequence_Drive_System_driving__region0_leaving_crossroad_to_left_side();
		enterSequence_Drive_System_driving__region0_on_waypoint_or_station_default();
	}
	
	private void effect_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward1_tr0() {
		exitSequence_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward1();
		enterSequence_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_left_default();
	}
	
	private void effect_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_left_tr0() {
		exitSequence_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_left();
		enterSequence_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward2_default();
	}
	
	private void effect_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward2_tr0() {
		exitSequence_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward2();
		enterSequence_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward3_default();
	}
	
	private void effect_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward3_tr0() {
		exitSequence_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward3();
		react_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft__exit_Default();
	}
	
	private void effect_Drive_System_unloading_r1__choice_0_tr0() {
		enterSequence_Drive_System_unloading_r1_unloading_default();
	}
	
	private void effect_Drive_System_unloading_r1__choice_0_tr1() {
		enterSequence_Drive_System_unloading_r1_turning_left_default();
	}
	
	private void effect_Drive_System_driving__region0__choice_0_tr1() {
		enterSequence_Drive_System_driving__region0_driving_forward_in_station_default();
	}
	
	private void effect_Drive_System_driving__region0__choice_0_tr2() {
		enterSequence_Drive_System_driving__region0_turning_right_in_station_default();
	}
	
	private void effect_Drive_System_driving__region0__choice_0_tr3() {
		enterSequence_Drive_System_driving__region0_turning_left_in_station_default();
	}
	
	private void effect_Drive_System_driving__region0__choice_0_tr4() {
		enterSequence_Drive_System_driving__region0_exiting_charging_position_default();
	}
	
	private void effect_Drive_System_driving__region0__choice_0_tr0() {
		enterSequence_Drive_System_driving__region0_waiting_in_station_default();
	}
	
	private void effect_Drive_System_driving__region0__choice_1_tr1() {
		enterSequence_Drive_System_driving__region0_leaving_crossroad_ahead_default();
	}
	
	private void effect_Drive_System_driving__region0__choice_1_tr2() {
		enterSequence_Drive_System_driving__region0_leaving_crossroad_to_right_side_default();
	}
	
	private void effect_Drive_System_driving__region0__choice_1_tr3() {
		enterSequence_Drive_System_driving__region0_leaving_crossroad_to_left_side_default();
	}
	
	private void effect_Drive_System_driving__region0__choice_1_tr0() {
		enterSequence_Drive_System_driving__region0_on_crossroad_default();
	}
	
	private void effect_Drive_System_driving__region0__choice_2_tr1() {
		setWaitedForDeadlock(false);
		
		enterSequence_Drive_System_driving__region0_entering_crossroad_default();
	}
	
	private void effect_Drive_System_driving__region0__choice_2_tr0() {
		enterSequence_Drive_System_driving__region0_waiting_to_ensure_real_deadlock_default();
	}
	
	private void effect_Drive_System_driving__region0__choice_3_tr1() {
		react_Drive_System_driving__region0__choice_2();
	}
	
	private void effect_Drive_System_driving__region0__choice_3_tr2() {
		enterSequence_Drive_System_driving__region0_entering_crossroad_default();
	}
	
	private void effect_Drive_System_driving__region0__choice_3_tr0() {
		enterSequence_Drive_System_driving__region0_waiting_on_waypoint_default();
	}
	
	private void effect_Drive_System_driving__region0__choice_4_tr0() {
		react_Drive_System_driving__region0__choice_3();
	}
	
	private void effect_Drive_System_driving__region0__choice_5_tr1() {
		enterSequence_Drive_System_driving__region0_turning_around_on_waypoint_default();
	}
	
	private void effect_Drive_System_driving__region0__choice_5_tr0() {
		react_Drive_System_driving__region0__choice_4();
	}
	
	private void effect_Drive_System_driving__region0__choice_6_tr0() {
		react_Drive_System_driving__region0__choice_0();
	}
	
	private void effect_Drive_System_driving__region0__choice_7_tr1() {
		react_Drive_System_driving__region0__exit_Default();
	}
	
	private void effect_Drive_System_driving__region0__choice_7_tr0() {
		react_Drive_System_driving__region0__choice_8();
	}
	
	private void effect_Drive_System_driving__region0__choice_8_tr1() {
		react_Drive_System_driving__region0__choice_5();
	}
	
	private void effect_Drive_System_driving__region0__choice_8_tr0() {
		react_Drive_System_driving__region0__choice_6();
	}
	
	private void effect_Drive_System_driving__region0__choice_9_tr0() {
		enterSequence_Drive_System_driving__region0_on_waypoint_or_station_default();
	}
	
	/* Entry action for state 'unloading'. */
	private void entryAction_Drive_System_unloading_r1_unloading() {
		sCIActors.raiseStartUnload();
	}
	
	/* Entry action for state 'turning left'. */
	private void entryAction_Drive_System_unloading_r1_turning_left() {
		sCIActors.raiseTurnLeft();
	}
	
	/* Entry action for state 'waiting to ensure real deadlock'. */
	private void entryAction_Drive_System_driving__region0_waiting_to_ensure_real_deadlock() {
		timer.setTimer(this, 0, 5 * 1000, false);
	}
	
	/* Entry action for state 'driving forward in station'. */
	private void entryAction_Drive_System_driving__region0_driving_forward_in_station() {
		sCIActors.raiseDriveForward();
	}
	
	/* Entry action for state 'turning right in station'. */
	private void entryAction_Drive_System_driving__region0_turning_right_in_station() {
		sCIActors.raiseTurnRight();
	}
	
	/* Entry action for state 'turning left in station'. */
	private void entryAction_Drive_System_driving__region0_turning_left_in_station() {
		sCIActors.raiseTurnLeft();
	}
	
	/* Entry action for state 'right1'. */
	private void entryAction_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right1() {
		sCIActors.raiseTurnRight();
	}
	
	/* Entry action for state 'right2'. */
	private void entryAction_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right2() {
		sCIActors.raiseTurnRight();
	}
	
	/* Entry action for state 'forward'. */
	private void entryAction_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger_forward() {
		sCIActors.raiseDriveForward();
	}
	
	/* Entry action for state 'right3'. */
	private void entryAction_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right3() {
		sCIActors.raiseTurnRight();
	}
	
	/* Entry action for state 'turning left to change lane'. */
	private void entryAction_Drive_System_driving__region0_turning_around_on_waypoint__region0_turning_left_to_change_lane() {
		sCIActors.raiseTurnLeft();
	}
	
	/* Entry action for state 'switching lane'. */
	private void entryAction_Drive_System_driving__region0_turning_around_on_waypoint__region0_switching_lane() {
		sCIActors.raiseDriveForward();
	}
	
	/* Entry action for state 'turning left on new lane'. */
	private void entryAction_Drive_System_driving__region0_turning_around_on_waypoint__region0_turning_left_on_new_lane() {
		sCIActors.raiseTurnLeft();
	}
	
	/* Entry action for state 'entering crossroad'. */
	private void entryAction_Drive_System_driving__region0_entering_crossroad() {
		sCIActors.raiseDriveForward();
	}
	
	/* Entry action for state 'forward1'. */
	private void entryAction_Drive_System_driving__region0_leaving_crossroad_ahead_onCrossroadDriveForward_forward1() {
		sCIActors.raiseDriveForward();
	}
	
	/* Entry action for state 'forward2'. */
	private void entryAction_Drive_System_driving__region0_leaving_crossroad_ahead_onCrossroadDriveForward_forward2() {
		sCIActors.raiseDriveForward();
	}
	
	/* Entry action for state 'right'. */
	private void entryAction_Drive_System_driving__region0_leaving_crossroad_to_right_side_onCrossroadDriveRight_right() {
		sCIActors.raiseTurnRight();
	}
	
	/* Entry action for state 'forward'. */
	private void entryAction_Drive_System_driving__region0_leaving_crossroad_to_right_side_onCrossroadDriveRight_forward() {
		sCIActors.raiseDriveForward();
	}
	
	/* Entry action for state 'forward1'. */
	private void entryAction_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward1() {
		sCIActors.raiseDriveForward();
	}
	
	/* Entry action for state 'left'. */
	private void entryAction_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_left() {
		sCIActors.raiseTurnLeft();
	}
	
	/* Entry action for state 'forward2'. */
	private void entryAction_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward2() {
		sCIActors.raiseDriveForward();
	}
	
	/* Entry action for state 'forward3'. */
	private void entryAction_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward3() {
		sCIActors.raiseDriveForward();
	}
	
	/* Exit action for state 'waiting to ensure real deadlock'. */
	private void exitAction_Drive_System_driving__region0_waiting_to_ensure_real_deadlock() {
		timer.unsetTimer(this, 0);
	}
	
	/* 'default' enter sequence for state idle */
	private void enterSequence_Drive_System_idle_default() {
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_idle;
	}
	
	/* 'default' enter sequence for state unloading */
	private void enterSequence_Drive_System_unloading_default() {
		enterSequence_Drive_System_unloading_r1_default();
	}
	
	/* 'default' enter sequence for state waiting for orientation check */
	private void enterSequence_Drive_System_unloading_r1_waiting_for_orientation_check_default() {
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_unloading_r1_waiting_for_orientation_check;
	}
	
	/* 'default' enter sequence for state unloading */
	private void enterSequence_Drive_System_unloading_r1_unloading_default() {
		entryAction_Drive_System_unloading_r1_unloading();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_unloading_r1_unloading;
	}
	
	/* 'default' enter sequence for state turning left */
	private void enterSequence_Drive_System_unloading_r1_turning_left_default() {
		entryAction_Drive_System_unloading_r1_turning_left();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_unloading_r1_turning_left;
	}
	
	/* 'default' enter sequence for state driving */
	private void enterSequence_Drive_System_driving_default() {
		enterSequence_Drive_System_driving__region0_default();
	}
	
	/* 'default' enter sequence for state on waypoint or station */
	private void enterSequence_Drive_System_driving__region0_on_waypoint_or_station_default() {
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_driving__region0_on_waypoint_or_station;
	}
	
	/* 'default' enter sequence for state waiting in station */
	private void enterSequence_Drive_System_driving__region0_waiting_in_station_default() {
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_driving__region0_waiting_in_station;
	}
	
	/* 'default' enter sequence for state waiting on waypoint */
	private void enterSequence_Drive_System_driving__region0_waiting_on_waypoint_default() {
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_driving__region0_waiting_on_waypoint;
	}
	
	/* 'default' enter sequence for state waiting to ensure real deadlock */
	private void enterSequence_Drive_System_driving__region0_waiting_to_ensure_real_deadlock_default() {
		entryAction_Drive_System_driving__region0_waiting_to_ensure_real_deadlock();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_driving__region0_waiting_to_ensure_real_deadlock;
	}
	
	/* 'default' enter sequence for state on crossroad */
	private void enterSequence_Drive_System_driving__region0_on_crossroad_default() {
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_driving__region0_on_crossroad;
	}
	
	/* 'default' enter sequence for state driving forward in station */
	private void enterSequence_Drive_System_driving__region0_driving_forward_in_station_default() {
		entryAction_Drive_System_driving__region0_driving_forward_in_station();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_driving__region0_driving_forward_in_station;
	}
	
	/* 'default' enter sequence for state turning right in station */
	private void enterSequence_Drive_System_driving__region0_turning_right_in_station_default() {
		entryAction_Drive_System_driving__region0_turning_right_in_station();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_driving__region0_turning_right_in_station;
	}
	
	/* 'default' enter sequence for state turning left in station */
	private void enterSequence_Drive_System_driving__region0_turning_left_in_station_default() {
		entryAction_Drive_System_driving__region0_turning_left_in_station();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_driving__region0_turning_left_in_station;
	}
	
	/* 'default' enter sequence for state exiting charging position */
	private void enterSequence_Drive_System_driving__region0_exiting_charging_position_default() {
		enterSequence_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger_default();
	}
	
	/* 'default' enter sequence for state right1 */
	private void enterSequence_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right1_default() {
		entryAction_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right1();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right1;
	}
	
	/* 'default' enter sequence for state right2 */
	private void enterSequence_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right2_default() {
		entryAction_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right2();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right2;
	}
	
	/* 'default' enter sequence for state forward */
	private void enterSequence_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger_forward_default() {
		entryAction_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger_forward();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_driving__region0_exiting_charging_position_inStationExitCharger_forward;
	}
	
	/* 'default' enter sequence for state right3 */
	private void enterSequence_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right3_default() {
		entryAction_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right3();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right3;
	}
	
	/* 'default' enter sequence for state turning around on waypoint */
	private void enterSequence_Drive_System_driving__region0_turning_around_on_waypoint_default() {
		enterSequence_Drive_System_driving__region0_turning_around_on_waypoint__region0_default();
	}
	
	/* 'default' enter sequence for state turning left to change lane */
	private void enterSequence_Drive_System_driving__region0_turning_around_on_waypoint__region0_turning_left_to_change_lane_default() {
		entryAction_Drive_System_driving__region0_turning_around_on_waypoint__region0_turning_left_to_change_lane();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_driving__region0_turning_around_on_waypoint__region0_turning_left_to_change_lane;
	}
	
	/* 'default' enter sequence for state waiting for free crossroad */
	private void enterSequence_Drive_System_driving__region0_turning_around_on_waypoint__region0_waiting_for_free_crossroad_default() {
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_driving__region0_turning_around_on_waypoint__region0_waiting_for_free_crossroad;
	}
	
	/* 'default' enter sequence for state switching lane */
	private void enterSequence_Drive_System_driving__region0_turning_around_on_waypoint__region0_switching_lane_default() {
		entryAction_Drive_System_driving__region0_turning_around_on_waypoint__region0_switching_lane();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_driving__region0_turning_around_on_waypoint__region0_switching_lane;
	}
	
	/* 'default' enter sequence for state turning left on new lane */
	private void enterSequence_Drive_System_driving__region0_turning_around_on_waypoint__region0_turning_left_on_new_lane_default() {
		entryAction_Drive_System_driving__region0_turning_around_on_waypoint__region0_turning_left_on_new_lane();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_driving__region0_turning_around_on_waypoint__region0_turning_left_on_new_lane;
	}
	
	/* 'default' enter sequence for state entering crossroad */
	private void enterSequence_Drive_System_driving__region0_entering_crossroad_default() {
		entryAction_Drive_System_driving__region0_entering_crossroad();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_driving__region0_entering_crossroad;
	}
	
	/* 'default' enter sequence for state leaving crossroad ahead */
	private void enterSequence_Drive_System_driving__region0_leaving_crossroad_ahead_default() {
		enterSequence_Drive_System_driving__region0_leaving_crossroad_ahead_onCrossroadDriveForward_default();
	}
	
	/* 'default' enter sequence for state forward1 */
	private void enterSequence_Drive_System_driving__region0_leaving_crossroad_ahead_onCrossroadDriveForward_forward1_default() {
		entryAction_Drive_System_driving__region0_leaving_crossroad_ahead_onCrossroadDriveForward_forward1();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_driving__region0_leaving_crossroad_ahead_onCrossroadDriveForward_forward1;
	}
	
	/* 'default' enter sequence for state forward2 */
	private void enterSequence_Drive_System_driving__region0_leaving_crossroad_ahead_onCrossroadDriveForward_forward2_default() {
		entryAction_Drive_System_driving__region0_leaving_crossroad_ahead_onCrossroadDriveForward_forward2();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_driving__region0_leaving_crossroad_ahead_onCrossroadDriveForward_forward2;
	}
	
	/* 'default' enter sequence for state leaving crossroad to right side */
	private void enterSequence_Drive_System_driving__region0_leaving_crossroad_to_right_side_default() {
		enterSequence_Drive_System_driving__region0_leaving_crossroad_to_right_side_onCrossroadDriveRight_default();
	}
	
	/* 'default' enter sequence for state right */
	private void enterSequence_Drive_System_driving__region0_leaving_crossroad_to_right_side_onCrossroadDriveRight_right_default() {
		entryAction_Drive_System_driving__region0_leaving_crossroad_to_right_side_onCrossroadDriveRight_right();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_driving__region0_leaving_crossroad_to_right_side_onCrossroadDriveRight_right;
	}
	
	/* 'default' enter sequence for state forward */
	private void enterSequence_Drive_System_driving__region0_leaving_crossroad_to_right_side_onCrossroadDriveRight_forward_default() {
		entryAction_Drive_System_driving__region0_leaving_crossroad_to_right_side_onCrossroadDriveRight_forward();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_driving__region0_leaving_crossroad_to_right_side_onCrossroadDriveRight_forward;
	}
	
	/* 'default' enter sequence for state leaving crossroad to left side */
	private void enterSequence_Drive_System_driving__region0_leaving_crossroad_to_left_side_default() {
		enterSequence_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_default();
	}
	
	/* 'default' enter sequence for state forward1 */
	private void enterSequence_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward1_default() {
		entryAction_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward1();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward1;
	}
	
	/* 'default' enter sequence for state left */
	private void enterSequence_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_left_default() {
		entryAction_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_left();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_left;
	}
	
	/* 'default' enter sequence for state forward2 */
	private void enterSequence_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward2_default() {
		entryAction_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward2();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward2;
	}
	
	/* 'default' enter sequence for state forward3 */
	private void enterSequence_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward3_default() {
		entryAction_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward3();
		nextStateIndex = 0;
		stateVector[0] = State.drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward3;
	}
	
	/* 'default' enter sequence for region Drive System */
	private void enterSequence_Drive_System_default() {
		react_Drive_System__entry_Default();
	}
	
	/* 'default' enter sequence for region r1 */
	private void enterSequence_Drive_System_unloading_r1_default() {
		react_Drive_System_unloading_r1__entry_Default();
	}
	
	/* 'default' enter sequence for region null */
	private void enterSequence_Drive_System_driving__region0_default() {
		react_Drive_System_driving__region0__entry_Default();
	}
	
	/* 'default' enter sequence for region inStationExitCharger */
	private void enterSequence_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger_default() {
		react_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger__entry_Default();
	}
	
	/* 'default' enter sequence for region null */
	private void enterSequence_Drive_System_driving__region0_turning_around_on_waypoint__region0_default() {
		react_Drive_System_driving__region0_turning_around_on_waypoint__region0__entry_Default();
	}
	
	/* 'default' enter sequence for region onCrossroadDriveForward */
	private void enterSequence_Drive_System_driving__region0_leaving_crossroad_ahead_onCrossroadDriveForward_default() {
		react_Drive_System_driving__region0_leaving_crossroad_ahead_onCrossroadDriveForward__entry_Default();
	}
	
	/* 'default' enter sequence for region onCrossroadDriveRight */
	private void enterSequence_Drive_System_driving__region0_leaving_crossroad_to_right_side_onCrossroadDriveRight_default() {
		react_Drive_System_driving__region0_leaving_crossroad_to_right_side_onCrossroadDriveRight__entry_Default();
	}
	
	/* 'default' enter sequence for region onCrossroadDriveLeft */
	private void enterSequence_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_default() {
		react_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft__entry_Default();
	}
	
	/* Default exit sequence for state idle */
	private void exitSequence_Drive_System_idle() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state unloading */
	private void exitSequence_Drive_System_unloading() {
		exitSequence_Drive_System_unloading_r1();
	}
	
	/* Default exit sequence for state waiting for orientation check */
	private void exitSequence_Drive_System_unloading_r1_waiting_for_orientation_check() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state unloading */
	private void exitSequence_Drive_System_unloading_r1_unloading() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state turning left */
	private void exitSequence_Drive_System_unloading_r1_turning_left() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state driving */
	private void exitSequence_Drive_System_driving() {
		exitSequence_Drive_System_driving__region0();
	}
	
	/* Default exit sequence for state on waypoint or station */
	private void exitSequence_Drive_System_driving__region0_on_waypoint_or_station() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state waiting in station */
	private void exitSequence_Drive_System_driving__region0_waiting_in_station() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state waiting on waypoint */
	private void exitSequence_Drive_System_driving__region0_waiting_on_waypoint() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state waiting to ensure real deadlock */
	private void exitSequence_Drive_System_driving__region0_waiting_to_ensure_real_deadlock() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
		
		exitAction_Drive_System_driving__region0_waiting_to_ensure_real_deadlock();
	}
	
	/* Default exit sequence for state on crossroad */
	private void exitSequence_Drive_System_driving__region0_on_crossroad() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state driving forward in station */
	private void exitSequence_Drive_System_driving__region0_driving_forward_in_station() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state turning right in station */
	private void exitSequence_Drive_System_driving__region0_turning_right_in_station() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state turning left in station */
	private void exitSequence_Drive_System_driving__region0_turning_left_in_station() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state exiting charging position */
	private void exitSequence_Drive_System_driving__region0_exiting_charging_position() {
		exitSequence_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger();
	}
	
	/* Default exit sequence for state right1 */
	private void exitSequence_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right1() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state right2 */
	private void exitSequence_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right2() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state forward */
	private void exitSequence_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger_forward() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state right3 */
	private void exitSequence_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right3() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state turning around on waypoint */
	private void exitSequence_Drive_System_driving__region0_turning_around_on_waypoint() {
		exitSequence_Drive_System_driving__region0_turning_around_on_waypoint__region0();
	}
	
	/* Default exit sequence for state turning left to change lane */
	private void exitSequence_Drive_System_driving__region0_turning_around_on_waypoint__region0_turning_left_to_change_lane() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state waiting for free crossroad */
	private void exitSequence_Drive_System_driving__region0_turning_around_on_waypoint__region0_waiting_for_free_crossroad() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state switching lane */
	private void exitSequence_Drive_System_driving__region0_turning_around_on_waypoint__region0_switching_lane() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state turning left on new lane */
	private void exitSequence_Drive_System_driving__region0_turning_around_on_waypoint__region0_turning_left_on_new_lane() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state entering crossroad */
	private void exitSequence_Drive_System_driving__region0_entering_crossroad() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state leaving crossroad ahead */
	private void exitSequence_Drive_System_driving__region0_leaving_crossroad_ahead() {
		exitSequence_Drive_System_driving__region0_leaving_crossroad_ahead_onCrossroadDriveForward();
	}
	
	/* Default exit sequence for state forward1 */
	private void exitSequence_Drive_System_driving__region0_leaving_crossroad_ahead_onCrossroadDriveForward_forward1() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state forward2 */
	private void exitSequence_Drive_System_driving__region0_leaving_crossroad_ahead_onCrossroadDriveForward_forward2() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state leaving crossroad to right side */
	private void exitSequence_Drive_System_driving__region0_leaving_crossroad_to_right_side() {
		exitSequence_Drive_System_driving__region0_leaving_crossroad_to_right_side_onCrossroadDriveRight();
	}
	
	/* Default exit sequence for state right */
	private void exitSequence_Drive_System_driving__region0_leaving_crossroad_to_right_side_onCrossroadDriveRight_right() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state forward */
	private void exitSequence_Drive_System_driving__region0_leaving_crossroad_to_right_side_onCrossroadDriveRight_forward() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state leaving crossroad to left side */
	private void exitSequence_Drive_System_driving__region0_leaving_crossroad_to_left_side() {
		exitSequence_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft();
	}
	
	/* Default exit sequence for state forward1 */
	private void exitSequence_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward1() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state left */
	private void exitSequence_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_left() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state forward2 */
	private void exitSequence_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward2() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state forward3 */
	private void exitSequence_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward3() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for region Drive System */
	private void exitSequence_Drive_System() {
		switch (stateVector[0]) {
		case drive_System_idle:
			exitSequence_Drive_System_idle();
			break;
		case drive_System_unloading_r1_waiting_for_orientation_check:
			exitSequence_Drive_System_unloading_r1_waiting_for_orientation_check();
			break;
		case drive_System_unloading_r1_unloading:
			exitSequence_Drive_System_unloading_r1_unloading();
			break;
		case drive_System_unloading_r1_turning_left:
			exitSequence_Drive_System_unloading_r1_turning_left();
			break;
		case drive_System_driving__region0_on_waypoint_or_station:
			exitSequence_Drive_System_driving__region0_on_waypoint_or_station();
			break;
		case drive_System_driving__region0_waiting_in_station:
			exitSequence_Drive_System_driving__region0_waiting_in_station();
			break;
		case drive_System_driving__region0_waiting_on_waypoint:
			exitSequence_Drive_System_driving__region0_waiting_on_waypoint();
			break;
		case drive_System_driving__region0_waiting_to_ensure_real_deadlock:
			exitSequence_Drive_System_driving__region0_waiting_to_ensure_real_deadlock();
			break;
		case drive_System_driving__region0_on_crossroad:
			exitSequence_Drive_System_driving__region0_on_crossroad();
			break;
		case drive_System_driving__region0_driving_forward_in_station:
			exitSequence_Drive_System_driving__region0_driving_forward_in_station();
			break;
		case drive_System_driving__region0_turning_right_in_station:
			exitSequence_Drive_System_driving__region0_turning_right_in_station();
			break;
		case drive_System_driving__region0_turning_left_in_station:
			exitSequence_Drive_System_driving__region0_turning_left_in_station();
			break;
		case drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right1:
			exitSequence_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right1();
			break;
		case drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right2:
			exitSequence_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right2();
			break;
		case drive_System_driving__region0_exiting_charging_position_inStationExitCharger_forward:
			exitSequence_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger_forward();
			break;
		case drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right3:
			exitSequence_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right3();
			break;
		case drive_System_driving__region0_turning_around_on_waypoint__region0_turning_left_to_change_lane:
			exitSequence_Drive_System_driving__region0_turning_around_on_waypoint__region0_turning_left_to_change_lane();
			break;
		case drive_System_driving__region0_turning_around_on_waypoint__region0_waiting_for_free_crossroad:
			exitSequence_Drive_System_driving__region0_turning_around_on_waypoint__region0_waiting_for_free_crossroad();
			break;
		case drive_System_driving__region0_turning_around_on_waypoint__region0_switching_lane:
			exitSequence_Drive_System_driving__region0_turning_around_on_waypoint__region0_switching_lane();
			break;
		case drive_System_driving__region0_turning_around_on_waypoint__region0_turning_left_on_new_lane:
			exitSequence_Drive_System_driving__region0_turning_around_on_waypoint__region0_turning_left_on_new_lane();
			break;
		case drive_System_driving__region0_entering_crossroad:
			exitSequence_Drive_System_driving__region0_entering_crossroad();
			break;
		case drive_System_driving__region0_leaving_crossroad_ahead_onCrossroadDriveForward_forward1:
			exitSequence_Drive_System_driving__region0_leaving_crossroad_ahead_onCrossroadDriveForward_forward1();
			break;
		case drive_System_driving__region0_leaving_crossroad_ahead_onCrossroadDriveForward_forward2:
			exitSequence_Drive_System_driving__region0_leaving_crossroad_ahead_onCrossroadDriveForward_forward2();
			break;
		case drive_System_driving__region0_leaving_crossroad_to_right_side_onCrossroadDriveRight_right:
			exitSequence_Drive_System_driving__region0_leaving_crossroad_to_right_side_onCrossroadDriveRight_right();
			break;
		case drive_System_driving__region0_leaving_crossroad_to_right_side_onCrossroadDriveRight_forward:
			exitSequence_Drive_System_driving__region0_leaving_crossroad_to_right_side_onCrossroadDriveRight_forward();
			break;
		case drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward1:
			exitSequence_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward1();
			break;
		case drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_left:
			exitSequence_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_left();
			break;
		case drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward2:
			exitSequence_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward2();
			break;
		case drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward3:
			exitSequence_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward3();
			break;
		default:
			break;
		}
	}
	
	/* Default exit sequence for region r1 */
	private void exitSequence_Drive_System_unloading_r1() {
		switch (stateVector[0]) {
		case drive_System_unloading_r1_waiting_for_orientation_check:
			exitSequence_Drive_System_unloading_r1_waiting_for_orientation_check();
			break;
		case drive_System_unloading_r1_unloading:
			exitSequence_Drive_System_unloading_r1_unloading();
			break;
		case drive_System_unloading_r1_turning_left:
			exitSequence_Drive_System_unloading_r1_turning_left();
			break;
		default:
			break;
		}
	}
	
	/* Default exit sequence for region null */
	private void exitSequence_Drive_System_driving__region0() {
		switch (stateVector[0]) {
		case drive_System_driving__region0_on_waypoint_or_station:
			exitSequence_Drive_System_driving__region0_on_waypoint_or_station();
			break;
		case drive_System_driving__region0_waiting_in_station:
			exitSequence_Drive_System_driving__region0_waiting_in_station();
			break;
		case drive_System_driving__region0_waiting_on_waypoint:
			exitSequence_Drive_System_driving__region0_waiting_on_waypoint();
			break;
		case drive_System_driving__region0_waiting_to_ensure_real_deadlock:
			exitSequence_Drive_System_driving__region0_waiting_to_ensure_real_deadlock();
			break;
		case drive_System_driving__region0_on_crossroad:
			exitSequence_Drive_System_driving__region0_on_crossroad();
			break;
		case drive_System_driving__region0_driving_forward_in_station:
			exitSequence_Drive_System_driving__region0_driving_forward_in_station();
			break;
		case drive_System_driving__region0_turning_right_in_station:
			exitSequence_Drive_System_driving__region0_turning_right_in_station();
			break;
		case drive_System_driving__region0_turning_left_in_station:
			exitSequence_Drive_System_driving__region0_turning_left_in_station();
			break;
		case drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right1:
			exitSequence_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right1();
			break;
		case drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right2:
			exitSequence_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right2();
			break;
		case drive_System_driving__region0_exiting_charging_position_inStationExitCharger_forward:
			exitSequence_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger_forward();
			break;
		case drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right3:
			exitSequence_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right3();
			break;
		case drive_System_driving__region0_turning_around_on_waypoint__region0_turning_left_to_change_lane:
			exitSequence_Drive_System_driving__region0_turning_around_on_waypoint__region0_turning_left_to_change_lane();
			break;
		case drive_System_driving__region0_turning_around_on_waypoint__region0_waiting_for_free_crossroad:
			exitSequence_Drive_System_driving__region0_turning_around_on_waypoint__region0_waiting_for_free_crossroad();
			break;
		case drive_System_driving__region0_turning_around_on_waypoint__region0_switching_lane:
			exitSequence_Drive_System_driving__region0_turning_around_on_waypoint__region0_switching_lane();
			break;
		case drive_System_driving__region0_turning_around_on_waypoint__region0_turning_left_on_new_lane:
			exitSequence_Drive_System_driving__region0_turning_around_on_waypoint__region0_turning_left_on_new_lane();
			break;
		case drive_System_driving__region0_entering_crossroad:
			exitSequence_Drive_System_driving__region0_entering_crossroad();
			break;
		case drive_System_driving__region0_leaving_crossroad_ahead_onCrossroadDriveForward_forward1:
			exitSequence_Drive_System_driving__region0_leaving_crossroad_ahead_onCrossroadDriveForward_forward1();
			break;
		case drive_System_driving__region0_leaving_crossroad_ahead_onCrossroadDriveForward_forward2:
			exitSequence_Drive_System_driving__region0_leaving_crossroad_ahead_onCrossroadDriveForward_forward2();
			break;
		case drive_System_driving__region0_leaving_crossroad_to_right_side_onCrossroadDriveRight_right:
			exitSequence_Drive_System_driving__region0_leaving_crossroad_to_right_side_onCrossroadDriveRight_right();
			break;
		case drive_System_driving__region0_leaving_crossroad_to_right_side_onCrossroadDriveRight_forward:
			exitSequence_Drive_System_driving__region0_leaving_crossroad_to_right_side_onCrossroadDriveRight_forward();
			break;
		case drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward1:
			exitSequence_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward1();
			break;
		case drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_left:
			exitSequence_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_left();
			break;
		case drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward2:
			exitSequence_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward2();
			break;
		case drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward3:
			exitSequence_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward3();
			break;
		default:
			break;
		}
	}
	
	/* Default exit sequence for region inStationExitCharger */
	private void exitSequence_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger() {
		switch (stateVector[0]) {
		case drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right1:
			exitSequence_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right1();
			break;
		case drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right2:
			exitSequence_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right2();
			break;
		case drive_System_driving__region0_exiting_charging_position_inStationExitCharger_forward:
			exitSequence_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger_forward();
			break;
		case drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right3:
			exitSequence_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right3();
			break;
		default:
			break;
		}
	}
	
	/* Default exit sequence for region null */
	private void exitSequence_Drive_System_driving__region0_turning_around_on_waypoint__region0() {
		switch (stateVector[0]) {
		case drive_System_driving__region0_turning_around_on_waypoint__region0_turning_left_to_change_lane:
			exitSequence_Drive_System_driving__region0_turning_around_on_waypoint__region0_turning_left_to_change_lane();
			break;
		case drive_System_driving__region0_turning_around_on_waypoint__region0_waiting_for_free_crossroad:
			exitSequence_Drive_System_driving__region0_turning_around_on_waypoint__region0_waiting_for_free_crossroad();
			break;
		case drive_System_driving__region0_turning_around_on_waypoint__region0_switching_lane:
			exitSequence_Drive_System_driving__region0_turning_around_on_waypoint__region0_switching_lane();
			break;
		case drive_System_driving__region0_turning_around_on_waypoint__region0_turning_left_on_new_lane:
			exitSequence_Drive_System_driving__region0_turning_around_on_waypoint__region0_turning_left_on_new_lane();
			break;
		default:
			break;
		}
	}
	
	/* Default exit sequence for region onCrossroadDriveForward */
	private void exitSequence_Drive_System_driving__region0_leaving_crossroad_ahead_onCrossroadDriveForward() {
		switch (stateVector[0]) {
		case drive_System_driving__region0_leaving_crossroad_ahead_onCrossroadDriveForward_forward1:
			exitSequence_Drive_System_driving__region0_leaving_crossroad_ahead_onCrossroadDriveForward_forward1();
			break;
		case drive_System_driving__region0_leaving_crossroad_ahead_onCrossroadDriveForward_forward2:
			exitSequence_Drive_System_driving__region0_leaving_crossroad_ahead_onCrossroadDriveForward_forward2();
			break;
		default:
			break;
		}
	}
	
	/* Default exit sequence for region onCrossroadDriveRight */
	private void exitSequence_Drive_System_driving__region0_leaving_crossroad_to_right_side_onCrossroadDriveRight() {
		switch (stateVector[0]) {
		case drive_System_driving__region0_leaving_crossroad_to_right_side_onCrossroadDriveRight_right:
			exitSequence_Drive_System_driving__region0_leaving_crossroad_to_right_side_onCrossroadDriveRight_right();
			break;
		case drive_System_driving__region0_leaving_crossroad_to_right_side_onCrossroadDriveRight_forward:
			exitSequence_Drive_System_driving__region0_leaving_crossroad_to_right_side_onCrossroadDriveRight_forward();
			break;
		default:
			break;
		}
	}
	
	/* Default exit sequence for region onCrossroadDriveLeft */
	private void exitSequence_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft() {
		switch (stateVector[0]) {
		case drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward1:
			exitSequence_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward1();
			break;
		case drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_left:
			exitSequence_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_left();
			break;
		case drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward2:
			exitSequence_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward2();
			break;
		case drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward3:
			exitSequence_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward3();
			break;
		default:
			break;
		}
	}
	
	/* The reactions of state idle. */
	private void react_Drive_System_idle() {
		if (check_Drive_System_idle_tr0_tr0()) {
			effect_Drive_System_idle_tr0();
		} else {
			if (check_Drive_System_idle_tr1_tr1()) {
				effect_Drive_System_idle_tr1();
			}
		}
	}
	
	/* The reactions of state waiting for orientation check. */
	private void react_Drive_System_unloading_r1_waiting_for_orientation_check() {
		if (check_Drive_System_unloading_r1_waiting_for_orientation_check_tr0_tr0()) {
			effect_Drive_System_unloading_r1_waiting_for_orientation_check_tr0();
		} else {
			if (check_Drive_System_unloading_tr1_tr1()) {
				effect_Drive_System_unloading_tr1();
			}
		}
	}
	
	/* The reactions of state unloading. */
	private void react_Drive_System_unloading_r1_unloading() {
		if (check_Drive_System_unloading_r1_unloading_tr0_tr0()) {
			effect_Drive_System_unloading_r1_unloading_tr0();
		} else {
			if (check_Drive_System_unloading_tr1_tr1()) {
				effect_Drive_System_unloading_tr1();
			}
		}
	}
	
	/* The reactions of state turning left. */
	private void react_Drive_System_unloading_r1_turning_left() {
		if (check_Drive_System_unloading_r1_turning_left_tr0_tr0()) {
			effect_Drive_System_unloading_r1_turning_left_tr0();
		} else {
			if (check_Drive_System_unloading_tr1_tr1()) {
				effect_Drive_System_unloading_tr1();
			}
		}
	}
	
	/* The reactions of state on waypoint or station. */
	private void react_Drive_System_driving__region0_on_waypoint_or_station() {
		if (check_Drive_System_driving__region0_on_waypoint_or_station_tr0_tr0()) {
			effect_Drive_System_driving__region0_on_waypoint_or_station_tr0();
		} else {
			if (check_Drive_System_driving_tr1_tr1()) {
				effect_Drive_System_driving_tr1();
			}
		}
	}
	
	/* The reactions of state waiting in station. */
	private void react_Drive_System_driving__region0_waiting_in_station() {
		if (check_Drive_System_driving__region0_waiting_in_station_tr0_tr0()) {
			effect_Drive_System_driving__region0_waiting_in_station_tr0();
		} else {
			if (check_Drive_System_driving_tr1_tr1()) {
				effect_Drive_System_driving_tr1();
			}
		}
	}
	
	/* The reactions of state waiting on waypoint. */
	private void react_Drive_System_driving__region0_waiting_on_waypoint() {
		if (check_Drive_System_driving__region0_waiting_on_waypoint_tr0_tr0()) {
			effect_Drive_System_driving__region0_waiting_on_waypoint_tr0();
		} else {
			if (check_Drive_System_driving_tr1_tr1()) {
				effect_Drive_System_driving_tr1();
			}
		}
	}
	
	/* The reactions of state waiting to ensure real deadlock. */
	private void react_Drive_System_driving__region0_waiting_to_ensure_real_deadlock() {
		if (check_Drive_System_driving__region0_waiting_to_ensure_real_deadlock_tr0_tr0()) {
			effect_Drive_System_driving__region0_waiting_to_ensure_real_deadlock_tr0();
		} else {
			if (check_Drive_System_driving_tr1_tr1()) {
				effect_Drive_System_driving_tr1();
			}
		}
	}
	
	/* The reactions of state on crossroad. */
	private void react_Drive_System_driving__region0_on_crossroad() {
		if (check_Drive_System_driving__region0_on_crossroad_tr0_tr0()) {
			effect_Drive_System_driving__region0_on_crossroad_tr0();
		} else {
			if (check_Drive_System_driving_tr1_tr1()) {
				effect_Drive_System_driving_tr1();
			}
		}
	}
	
	/* The reactions of state driving forward in station. */
	private void react_Drive_System_driving__region0_driving_forward_in_station() {
		if (check_Drive_System_driving__region0_driving_forward_in_station_tr0_tr0()) {
			effect_Drive_System_driving__region0_driving_forward_in_station_tr0();
		} else {
			if (check_Drive_System_driving_tr1_tr1()) {
				effect_Drive_System_driving_tr1();
			}
		}
	}
	
	/* The reactions of state turning right in station. */
	private void react_Drive_System_driving__region0_turning_right_in_station() {
		if (check_Drive_System_driving__region0_turning_right_in_station_tr0_tr0()) {
			effect_Drive_System_driving__region0_turning_right_in_station_tr0();
		} else {
			if (check_Drive_System_driving_tr1_tr1()) {
				effect_Drive_System_driving_tr1();
			}
		}
	}
	
	/* The reactions of state turning left in station. */
	private void react_Drive_System_driving__region0_turning_left_in_station() {
		if (check_Drive_System_driving__region0_turning_left_in_station_tr0_tr0()) {
			effect_Drive_System_driving__region0_turning_left_in_station_tr0();
		} else {
			if (check_Drive_System_driving_tr1_tr1()) {
				effect_Drive_System_driving_tr1();
			}
		}
	}
	
	/* The reactions of state right1. */
	private void react_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right1() {
		if (check_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right1_tr0_tr0()) {
			effect_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right1_tr0();
		} else {
			if (check_Drive_System_driving_tr1_tr1()) {
				effect_Drive_System_driving_tr1();
			}
		}
	}
	
	/* The reactions of state right2. */
	private void react_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right2() {
		if (check_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right2_tr0_tr0()) {
			effect_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right2_tr0();
		} else {
			if (check_Drive_System_driving_tr1_tr1()) {
				effect_Drive_System_driving_tr1();
			}
		}
	}
	
	/* The reactions of state forward. */
	private void react_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger_forward() {
		if (check_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger_forward_tr0_tr0()) {
			effect_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger_forward_tr0();
		} else {
			if (check_Drive_System_driving_tr1_tr1()) {
				effect_Drive_System_driving_tr1();
			}
		}
	}
	
	/* The reactions of state right3. */
	private void react_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right3() {
		if (check_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right3_tr0_tr0()) {
			effect_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right3_tr0();
		} else {
			if (check_Drive_System_driving_tr1_tr1()) {
				effect_Drive_System_driving_tr1();
			}
		}
	}
	
	/* The reactions of state turning left to change lane. */
	private void react_Drive_System_driving__region0_turning_around_on_waypoint__region0_turning_left_to_change_lane() {
		if (check_Drive_System_driving__region0_turning_around_on_waypoint__region0_turning_left_to_change_lane_tr0_tr0()) {
			effect_Drive_System_driving__region0_turning_around_on_waypoint__region0_turning_left_to_change_lane_tr0();
		} else {
			if (check_Drive_System_driving_tr1_tr1()) {
				effect_Drive_System_driving_tr1();
			}
		}
	}
	
	/* The reactions of state waiting for free crossroad. */
	private void react_Drive_System_driving__region0_turning_around_on_waypoint__region0_waiting_for_free_crossroad() {
		if (check_Drive_System_driving__region0_turning_around_on_waypoint__region0_waiting_for_free_crossroad_tr0_tr0()) {
			effect_Drive_System_driving__region0_turning_around_on_waypoint__region0_waiting_for_free_crossroad_tr0();
		} else {
			if (check_Drive_System_driving_tr1_tr1()) {
				effect_Drive_System_driving_tr1();
			}
		}
	}
	
	/* The reactions of state switching lane. */
	private void react_Drive_System_driving__region0_turning_around_on_waypoint__region0_switching_lane() {
		if (check_Drive_System_driving__region0_turning_around_on_waypoint__region0_switching_lane_tr0_tr0()) {
			effect_Drive_System_driving__region0_turning_around_on_waypoint__region0_switching_lane_tr0();
		} else {
			if (check_Drive_System_driving_tr1_tr1()) {
				effect_Drive_System_driving_tr1();
			}
		}
	}
	
	/* The reactions of state turning left on new lane. */
	private void react_Drive_System_driving__region0_turning_around_on_waypoint__region0_turning_left_on_new_lane() {
		if (check_Drive_System_driving__region0_turning_around_on_waypoint__region0_turning_left_on_new_lane_tr0_tr0()) {
			effect_Drive_System_driving__region0_turning_around_on_waypoint__region0_turning_left_on_new_lane_tr0();
		} else {
			if (check_Drive_System_driving_tr1_tr1()) {
				effect_Drive_System_driving_tr1();
			}
		}
	}
	
	/* The reactions of state entering crossroad. */
	private void react_Drive_System_driving__region0_entering_crossroad() {
		if (check_Drive_System_driving__region0_entering_crossroad_tr0_tr0()) {
			effect_Drive_System_driving__region0_entering_crossroad_tr0();
		} else {
			if (check_Drive_System_driving_tr1_tr1()) {
				effect_Drive_System_driving_tr1();
			}
		}
	}
	
	/* The reactions of state forward1. */
	private void react_Drive_System_driving__region0_leaving_crossroad_ahead_onCrossroadDriveForward_forward1() {
		if (check_Drive_System_driving__region0_leaving_crossroad_ahead_onCrossroadDriveForward_forward1_tr0_tr0()) {
			effect_Drive_System_driving__region0_leaving_crossroad_ahead_onCrossroadDriveForward_forward1_tr0();
		} else {
			if (check_Drive_System_driving_tr1_tr1()) {
				effect_Drive_System_driving_tr1();
			}
		}
	}
	
	/* The reactions of state forward2. */
	private void react_Drive_System_driving__region0_leaving_crossroad_ahead_onCrossroadDriveForward_forward2() {
		if (check_Drive_System_driving__region0_leaving_crossroad_ahead_onCrossroadDriveForward_forward2_tr0_tr0()) {
			effect_Drive_System_driving__region0_leaving_crossroad_ahead_onCrossroadDriveForward_forward2_tr0();
		} else {
			if (check_Drive_System_driving_tr1_tr1()) {
				effect_Drive_System_driving_tr1();
			}
		}
	}
	
	/* The reactions of state right. */
	private void react_Drive_System_driving__region0_leaving_crossroad_to_right_side_onCrossroadDriveRight_right() {
		if (check_Drive_System_driving__region0_leaving_crossroad_to_right_side_onCrossroadDriveRight_right_tr0_tr0()) {
			effect_Drive_System_driving__region0_leaving_crossroad_to_right_side_onCrossroadDriveRight_right_tr0();
		} else {
			if (check_Drive_System_driving_tr1_tr1()) {
				effect_Drive_System_driving_tr1();
			}
		}
	}
	
	/* The reactions of state forward. */
	private void react_Drive_System_driving__region0_leaving_crossroad_to_right_side_onCrossroadDriveRight_forward() {
		if (check_Drive_System_driving__region0_leaving_crossroad_to_right_side_onCrossroadDriveRight_forward_tr0_tr0()) {
			effect_Drive_System_driving__region0_leaving_crossroad_to_right_side_onCrossroadDriveRight_forward_tr0();
		} else {
			if (check_Drive_System_driving_tr1_tr1()) {
				effect_Drive_System_driving_tr1();
			}
		}
	}
	
	/* The reactions of state forward1. */
	private void react_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward1() {
		if (check_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward1_tr0_tr0()) {
			effect_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward1_tr0();
		} else {
			if (check_Drive_System_driving_tr1_tr1()) {
				effect_Drive_System_driving_tr1();
			}
		}
	}
	
	/* The reactions of state left. */
	private void react_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_left() {
		if (check_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_left_tr0_tr0()) {
			effect_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_left_tr0();
		} else {
			if (check_Drive_System_driving_tr1_tr1()) {
				effect_Drive_System_driving_tr1();
			}
		}
	}
	
	/* The reactions of state forward2. */
	private void react_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward2() {
		if (check_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward2_tr0_tr0()) {
			effect_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward2_tr0();
		} else {
			if (check_Drive_System_driving_tr1_tr1()) {
				effect_Drive_System_driving_tr1();
			}
		}
	}
	
	/* The reactions of state forward3. */
	private void react_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward3() {
		if (check_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward3_tr0_tr0()) {
			effect_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward3_tr0();
		} else {
			if (check_Drive_System_driving_tr1_tr1()) {
				effect_Drive_System_driving_tr1();
			}
		}
	}
	
	/* The reactions of state null. */
	private void react_Drive_System_unloading_r1__choice_0() {
		if (check_Drive_System_unloading_r1__choice_0_tr0_tr0()) {
			effect_Drive_System_unloading_r1__choice_0_tr0();
		} else {
			effect_Drive_System_unloading_r1__choice_0_tr1();
		}
	}
	
	/* The reactions of state null. */
	private void react_Drive_System_driving__region0__choice_0() {
		if (check_Drive_System_driving__region0__choice_0_tr1_tr1()) {
			effect_Drive_System_driving__region0__choice_0_tr1();
		} else {
			if (check_Drive_System_driving__region0__choice_0_tr2_tr2()) {
				effect_Drive_System_driving__region0__choice_0_tr2();
			} else {
				if (check_Drive_System_driving__region0__choice_0_tr3_tr3()) {
					effect_Drive_System_driving__region0__choice_0_tr3();
				} else {
					if (check_Drive_System_driving__region0__choice_0_tr4_tr4()) {
						effect_Drive_System_driving__region0__choice_0_tr4();
					} else {
						effect_Drive_System_driving__region0__choice_0_tr0();
					}
				}
			}
		}
	}
	
	/* The reactions of state null. */
	private void react_Drive_System_driving__region0__choice_1() {
		if (check_Drive_System_driving__region0__choice_1_tr1_tr1()) {
			effect_Drive_System_driving__region0__choice_1_tr1();
		} else {
			if (check_Drive_System_driving__region0__choice_1_tr2_tr2()) {
				effect_Drive_System_driving__region0__choice_1_tr2();
			} else {
				if (check_Drive_System_driving__region0__choice_1_tr3_tr3()) {
					effect_Drive_System_driving__region0__choice_1_tr3();
				} else {
					effect_Drive_System_driving__region0__choice_1_tr0();
				}
			}
		}
	}
	
	/* The reactions of state null. */
	private void react_Drive_System_driving__region0__choice_2() {
		if (check_Drive_System_driving__region0__choice_2_tr1_tr1()) {
			effect_Drive_System_driving__region0__choice_2_tr1();
		} else {
			effect_Drive_System_driving__region0__choice_2_tr0();
		}
	}
	
	/* The reactions of state null. */
	private void react_Drive_System_driving__region0__choice_3() {
		if (check_Drive_System_driving__region0__choice_3_tr1_tr1()) {
			effect_Drive_System_driving__region0__choice_3_tr1();
		} else {
			if (check_Drive_System_driving__region0__choice_3_tr2_tr2()) {
				effect_Drive_System_driving__region0__choice_3_tr2();
			} else {
				effect_Drive_System_driving__region0__choice_3_tr0();
			}
		}
	}
	
	/* The reactions of state null. */
	private void react_Drive_System_driving__region0__choice_4() {
		effect_Drive_System_driving__region0__choice_4_tr0();
	}
	
	/* The reactions of state null. */
	private void react_Drive_System_driving__region0__choice_5() {
		if (check_Drive_System_driving__region0__choice_5_tr1_tr1()) {
			effect_Drive_System_driving__region0__choice_5_tr1();
		} else {
			effect_Drive_System_driving__region0__choice_5_tr0();
		}
	}
	
	/* The reactions of state null. */
	private void react_Drive_System_driving__region0__choice_6() {
		effect_Drive_System_driving__region0__choice_6_tr0();
	}
	
	/* The reactions of state null. */
	private void react_Drive_System_driving__region0__choice_7() {
		if (check_Drive_System_driving__region0__choice_7_tr1_tr1()) {
			effect_Drive_System_driving__region0__choice_7_tr1();
		} else {
			effect_Drive_System_driving__region0__choice_7_tr0();
		}
	}
	
	/* The reactions of state null. */
	private void react_Drive_System_driving__region0__choice_8() {
		if (check_Drive_System_driving__region0__choice_8_tr1_tr1()) {
			effect_Drive_System_driving__region0__choice_8_tr1();
		} else {
			effect_Drive_System_driving__region0__choice_8_tr0();
		}
	}
	
	/* The reactions of state null. */
	private void react_Drive_System_driving__region0__choice_9() {
		effect_Drive_System_driving__region0__choice_9_tr0();
	}
	
	/* Default react sequence for initial entry  */
	private void react_Drive_System__entry_Default() {
		enterSequence_Drive_System_idle_default();
	}
	
	/* Default react sequence for initial entry  */
	private void react_Drive_System_unloading_r1__entry_Default() {
		enterSequence_Drive_System_unloading_r1_waiting_for_orientation_check_default();
	}
	
	/* Default react sequence for initial entry  */
	private void react_Drive_System_driving__region0__entry_Default() {
		enterSequence_Drive_System_driving__region0_on_waypoint_or_station_default();
	}
	
	/* Default react sequence for initial entry  */
	private void react_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger__entry_Default() {
		enterSequence_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right1_default();
	}
	
	/* Default react sequence for initial entry  */
	private void react_Drive_System_driving__region0_turning_around_on_waypoint__region0__entry_Default() {
		enterSequence_Drive_System_driving__region0_turning_around_on_waypoint__region0_turning_left_to_change_lane_default();
	}
	
	/* Default react sequence for initial entry  */
	private void react_Drive_System_driving__region0_leaving_crossroad_ahead_onCrossroadDriveForward__entry_Default() {
		enterSequence_Drive_System_driving__region0_leaving_crossroad_ahead_onCrossroadDriveForward_forward1_default();
	}
	
	/* Default react sequence for initial entry  */
	private void react_Drive_System_driving__region0_leaving_crossroad_to_right_side_onCrossroadDriveRight__entry_Default() {
		enterSequence_Drive_System_driving__region0_leaving_crossroad_to_right_side_onCrossroadDriveRight_right_default();
	}
	
	/* Default react sequence for initial entry  */
	private void react_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft__entry_Default() {
		enterSequence_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward1_default();
	}
	
	/* The reactions of exit default. */
	private void react_Drive_System_unloading_r1__exit_Default() {
		effect_Drive_System_unloading_tr0();
	}
	
	/* The reactions of exit default. */
	private void react_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger__exit_Default() {
		effect_Drive_System_driving__region0_exiting_charging_position_tr0();
	}
	
	/* The reactions of exit default. */
	private void react_Drive_System_driving__region0_turning_around_on_waypoint__region0__exit_Default() {
		effect_Drive_System_driving__region0_turning_around_on_waypoint_tr0();
	}
	
	/* The reactions of exit default. */
	private void react_Drive_System_driving__region0_leaving_crossroad_ahead_onCrossroadDriveForward__exit_Default() {
		effect_Drive_System_driving__region0_leaving_crossroad_ahead_tr0();
	}
	
	/* The reactions of exit default. */
	private void react_Drive_System_driving__region0_leaving_crossroad_to_right_side_onCrossroadDriveRight__exit_Default() {
		effect_Drive_System_driving__region0_leaving_crossroad_to_right_side_tr0();
	}
	
	/* The reactions of exit default. */
	private void react_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft__exit_Default() {
		effect_Drive_System_driving__region0_leaving_crossroad_to_left_side_tr0();
	}
	
	/* The reactions of exit default. */
	private void react_Drive_System_driving__region0__exit_Default() {
		effect_Drive_System_driving_tr0();
	}
	
	public void runCycle() {
		if (!initialized)
			throw new IllegalStateException(
					"The state machine needs to be initialized first by calling the init() function.");
		clearOutEvents();
		for (nextStateIndex = 0; nextStateIndex < stateVector.length; nextStateIndex++) {
			switch (stateVector[nextStateIndex]) {
			case drive_System_idle:
				react_Drive_System_idle();
				break;
			case drive_System_unloading_r1_waiting_for_orientation_check:
				react_Drive_System_unloading_r1_waiting_for_orientation_check();
				break;
			case drive_System_unloading_r1_unloading:
				react_Drive_System_unloading_r1_unloading();
				break;
			case drive_System_unloading_r1_turning_left:
				react_Drive_System_unloading_r1_turning_left();
				break;
			case drive_System_driving__region0_on_waypoint_or_station:
				react_Drive_System_driving__region0_on_waypoint_or_station();
				break;
			case drive_System_driving__region0_waiting_in_station:
				react_Drive_System_driving__region0_waiting_in_station();
				break;
			case drive_System_driving__region0_waiting_on_waypoint:
				react_Drive_System_driving__region0_waiting_on_waypoint();
				break;
			case drive_System_driving__region0_waiting_to_ensure_real_deadlock:
				react_Drive_System_driving__region0_waiting_to_ensure_real_deadlock();
				break;
			case drive_System_driving__region0_on_crossroad:
				react_Drive_System_driving__region0_on_crossroad();
				break;
			case drive_System_driving__region0_driving_forward_in_station:
				react_Drive_System_driving__region0_driving_forward_in_station();
				break;
			case drive_System_driving__region0_turning_right_in_station:
				react_Drive_System_driving__region0_turning_right_in_station();
				break;
			case drive_System_driving__region0_turning_left_in_station:
				react_Drive_System_driving__region0_turning_left_in_station();
				break;
			case drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right1:
				react_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right1();
				break;
			case drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right2:
				react_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right2();
				break;
			case drive_System_driving__region0_exiting_charging_position_inStationExitCharger_forward:
				react_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger_forward();
				break;
			case drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right3:
				react_Drive_System_driving__region0_exiting_charging_position_inStationExitCharger_right3();
				break;
			case drive_System_driving__region0_turning_around_on_waypoint__region0_turning_left_to_change_lane:
				react_Drive_System_driving__region0_turning_around_on_waypoint__region0_turning_left_to_change_lane();
				break;
			case drive_System_driving__region0_turning_around_on_waypoint__region0_waiting_for_free_crossroad:
				react_Drive_System_driving__region0_turning_around_on_waypoint__region0_waiting_for_free_crossroad();
				break;
			case drive_System_driving__region0_turning_around_on_waypoint__region0_switching_lane:
				react_Drive_System_driving__region0_turning_around_on_waypoint__region0_switching_lane();
				break;
			case drive_System_driving__region0_turning_around_on_waypoint__region0_turning_left_on_new_lane:
				react_Drive_System_driving__region0_turning_around_on_waypoint__region0_turning_left_on_new_lane();
				break;
			case drive_System_driving__region0_entering_crossroad:
				react_Drive_System_driving__region0_entering_crossroad();
				break;
			case drive_System_driving__region0_leaving_crossroad_ahead_onCrossroadDriveForward_forward1:
				react_Drive_System_driving__region0_leaving_crossroad_ahead_onCrossroadDriveForward_forward1();
				break;
			case drive_System_driving__region0_leaving_crossroad_ahead_onCrossroadDriveForward_forward2:
				react_Drive_System_driving__region0_leaving_crossroad_ahead_onCrossroadDriveForward_forward2();
				break;
			case drive_System_driving__region0_leaving_crossroad_to_right_side_onCrossroadDriveRight_right:
				react_Drive_System_driving__region0_leaving_crossroad_to_right_side_onCrossroadDriveRight_right();
				break;
			case drive_System_driving__region0_leaving_crossroad_to_right_side_onCrossroadDriveRight_forward:
				react_Drive_System_driving__region0_leaving_crossroad_to_right_side_onCrossroadDriveRight_forward();
				break;
			case drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward1:
				react_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward1();
				break;
			case drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_left:
				react_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_left();
				break;
			case drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward2:
				react_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward2();
				break;
			case drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward3:
				react_Drive_System_driving__region0_leaving_crossroad_to_left_side_onCrossroadDriveLeft_forward3();
				break;
			default:
				// $NullState$
			}
		}
		clearEvents();
	}
}
