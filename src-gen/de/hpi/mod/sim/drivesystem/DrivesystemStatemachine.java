package de.hpi.mod.sim.drivesystem;


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
		$NullState$
	};
	
	private final State[] stateVector = new State[0];
	
	private int nextStateIndex;
	
	
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
		if (this.sCIData.operationCallback == null) {
			throw new IllegalStateException("Operation callback for interface sCIData must be set.");
		}
		
		if (this.sCIRawData.operationCallback == null) {
			throw new IllegalStateException("Operation callback for interface sCIRawData must be set.");
		}
		
		for (int i = 0; i < 0; i++) {
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
	}
	
	public void runCycle() {
		if (!initialized)
			throw new IllegalStateException(
					"The state machine needs to be initialized first by calling the init() function.");
		clearOutEvents();
		for (nextStateIndex = 0; nextStateIndex < stateVector.length; nextStateIndex++) {
			switch (stateVector[nextStateIndex]) {
			default:
				// $NullState$
			}
		}
		clearEvents();
	}
	public void exit() {
	}
	
	/**
	 * @see IStatemachine#isActive()
	 */
	public boolean isActive() {
		return ;
	}
	
	/** 
	* @see IStatemachine#isFinal()
	*/
	public boolean isFinal() {
		return ;
	}
	/**
	* This method resets the incoming events (time events included).
	*/
	protected void clearEvents() {
		sCInterface.clearEvents();
		sCIProcessor.clearEvents();
		sCIActors.clearEvents();
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
		default:
			return false;
		}
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
	
	private boolean react() {
		return false;
	}
	
}
