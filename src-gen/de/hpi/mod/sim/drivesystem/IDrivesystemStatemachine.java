package de.hpi.mod.sim.drivesystem;

import de.hpi.mod.sim.IStatemachine;
import de.hpi.mod.sim.ITimerCallback;

public interface IDrivesystemStatemachine extends ITimerCallback,IStatemachine {
	public interface SCInterface {
	
		public void raiseUnload();
		
		public void raiseUnloaded();
		
		public void raiseStop();
		
		public void raiseDataRefresh();
		
		public void raiseNewTarget();
		
		public void raiseNewUnloadingTarget();
		
		public void raiseNewChargingTarget();
		
		public void raiseActionCompleted();
		
	}
	
	public SCInterface getSCInterface();
	
	public interface SCIProcessor {
	
		public boolean isRaisedUnloaded();
		
		public boolean isRaisedArrived();
		
	}
	
	public SCIProcessor getSCIProcessor();
	
	public interface SCIActors {
	
		public boolean isRaisedStartUnload();
		
		public boolean isRaisedDriveForward();
		
		public boolean isRaisedDriveBackward();
		
		public boolean isRaisedTurnLeft();
		
		public boolean isRaisedTurnRight();
		
	}
	
	public SCIActors getSCIActors();
	
	public interface SCIData {
	
		public void setSCIDataOperationCallback(SCIDataOperationCallback operationCallback);
	
	}
	
	public interface SCIDataOperationCallback {
	
		public long posOrientation();
		
		public long posType();
		
		public long targetDirection();
		
		public boolean isOnTarget();
		
		public boolean canUnloadToTarget();
		
		public boolean canChargeAtTarget();
		
		public boolean blockedLeft();
		
		public boolean blockedFront();
		
		public boolean blockedRight();
		
		public boolean blockedWaypointAhead();
		
		public boolean blockedWaypointLeft();
		
		public boolean blockedWaypointRight();
		
		public boolean blockedCrossroadAhead();
		
		public boolean blockedCrossroadRight();
		
	}
	
	public SCIData getSCIData();
	
	public interface SCIRawData {
	
		public void setSCIRawDataOperationCallback(SCIRawDataOperationCallback operationCallback);
	
	}
	
	public interface SCIRawDataOperationCallback {
	
		public long posX();
		
		public long posY();
		
		public long targetX();
		
		public long targetY();
		
	}
	
	public SCIRawData getSCIRawData();
	
	public interface SCIPositionType {
	
		public long getWAYPOINT();
		
		public void setWAYPOINT(long value);
		
		public long getSTATION();
		
		public void setSTATION(long value);
		
		public long getCROSSROAD();
		
		public void setCROSSROAD(long value);
		
		public long getBLOCKED();
		
		public void setBLOCKED(long value);
		
	}
	
	public SCIPositionType getSCIPositionType();
	
	public interface SCIOrientation {
	
		public long getNORTH();
		
		public void setNORTH(long value);
		
		public long getEAST();
		
		public void setEAST(long value);
		
		public long getSOUTH();
		
		public void setSOUTH(long value);
		
		public long getWEST();
		
		public void setWEST(long value);
		
	}
	
	public SCIOrientation getSCIOrientation();
	
	public interface SCIDirection {
	
		public long getLEFT();
		
		public void setLEFT(long value);
		
		public long getAHEAD();
		
		public void setAHEAD(long value);
		
		public long getRIGHT();
		
		public void setRIGHT(long value);
		
		public long getBEHIND();
		
		public void setBEHIND(long value);
		
	}
	
	public SCIDirection getSCIDirection();
	
}
