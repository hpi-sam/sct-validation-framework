package de.hpi.mod.sim.setting.infinitewarehouses;

import java.util.concurrent.CopyOnWriteArrayList;


public abstract class AbstractStation {

    private int stationID;

	private CopyOnWriteArrayList<Integer> requestedLocks = new CopyOnWriteArrayList<Integer>();


    public AbstractStation(int stationID) {
        this.stationID = stationID;
    }

    public int getStationID() {
        return stationID;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        AbstractStation station = (AbstractStation) object;

        return stationID == station.stationID;
    }

    public void releaseLocks() {
        requestedLocks.clear();
        clear();
    }
    
    protected abstract void clear();

	public void registerRequestLock(int robotID) {
		if(!requestedLocks .contains(robotID)) {
			requestedLocks.add(0, robotID);
		}
	}

    public boolean requestLock(int robotID) {
        if (requestedLocks.get(0) == robotID && lockAllowed()) {
            requestedLocks.remove(0);
            onLock();
            return true;
        } else {
            return false;
        }
    }
    
    protected abstract boolean lockAllowed();

    protected abstract void onLock();
}
