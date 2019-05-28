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
			unloaded = false;
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
	
	private boolean initialized = false;
	
	public enum State {
		_region0__1,
		_region0__0,
		$NullState$
	};
	
	private final State[] stateVector = new State[1];
	
	private int nextStateIndex;
	
	
	private ITimer timer;
	
	private final boolean[] timeEvents = new boolean[1];
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
		enterSequence__region0_default();
	}
	
	public void runCycle() {
		if (!initialized)
			throw new IllegalStateException(
					"The state machine needs to be initialized first by calling the init() function.");
		clearOutEvents();
		for (nextStateIndex = 0; nextStateIndex < stateVector.length; nextStateIndex++) {
			switch (stateVector[nextStateIndex]) {
			case _region0__1:
				_region0__1_react(true);
				break;
			case _region0__0:
				_region0__0_react(true);
				break;
			default:
				// $NullState$
			}
		}
		clearEvents();
	}
	public void exit() {
		exitSequence__region0();
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
		case _region0__1:
			return stateVector[0] == State._region0__1;
		case _region0__0:
			return stateVector[0] == State._region0__0;
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
	
	public void raiseNewUnloadingTarget() {
		sCInterface.raiseNewUnloadingTarget();
	}
	
	public void raiseNewChargingTarget() {
		sCInterface.raiseNewChargingTarget();
	}
	
	public void raiseActionCompleted() {
		sCInterface.raiseActionCompleted();
	}
	
	/* Entry action for state '1'. */
	private void entryAction__region0__1() {
		timer.setTimer(this, 0, (1 * 1000), false);
		
		sCIActors.raiseDriveForward();
	}
	
	/* Exit action for state '1'. */
	private void exitAction__region0__1() {
		timer.unsetTimer(this, 0);
	}
	
	/* 'default' enter sequence for state 1 */
	private void enterSequence__region0__1_default() {
		entryAction__region0__1();
		nextStateIndex = 0;
		stateVector[0] = State._region0__1;
	}
	
	/* 'default' enter sequence for state 0 */
	private void enterSequence__region0__0_default() {
		nextStateIndex = 0;
		stateVector[0] = State._region0__0;
	}
	
	/* 'default' enter sequence for region null */
	private void enterSequence__region0_default() {
		react__region0__entry_Default();
	}
	
	/* Default exit sequence for state 1 */
	private void exitSequence__region0__1() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
		
		exitAction__region0__1();
	}
	
	/* Default exit sequence for state 0 */
	private void exitSequence__region0__0() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for region null */
	private void exitSequence__region0() {
		switch (stateVector[0]) {
		case _region0__1:
			exitSequence__region0__1();
			break;
		case _region0__0:
			exitSequence__region0__0();
			break;
		default:
			break;
		}
	}
	
	/* Default react sequence for initial entry  */
	private void react__region0__entry_Default() {
		enterSequence__region0__0_default();
	}
	
	private boolean react() {
		return false;
	}
	
	private boolean _region0__1_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (timeEvents[0]) {
				exitSequence__region0__1();
				enterSequence__region0__1_default();
			} else {
				if (sCInterface.actionCompleted) {
					exitSequence__region0__1();
					enterSequence__region0__1_default();
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
	
	private boolean _region0__0_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (sCInterface.newTarget) {
				exitSequence__region0__0();
				enterSequence__region0__1_default();
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
	
}
