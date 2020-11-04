package de.hpi.mod.sim.worlds.flasher;

import java.util.List;

import de.hpi.mod.sim.core.Configuration;
import de.hpi.mod.sim.core.simulation.Entity;

public class Starter implements Entity {
    
    private FlashWorld world;
    private int posBlink = 0, posWaiting = 0;
    private double timeToWait;

    private List<Integer> blinkCounts;
    private List<Float> waitingTimes;
    private boolean onRepeat;
    private boolean running = true;

    public Starter(List<Integer> blinkCounts, List<Float> waitingTimes, boolean onRepeat, FlashWorld world) {
        this.world = world;
        this.blinkCounts = blinkCounts;
        this.waitingTimes = waitingTimes;
        timeToWait = 0;
        this.onRepeat = onRepeat;
    }

    public void update(float delta) {
        if (running) {
            timeToWait -= delta;
            if (timeToWait <= 0) {
                world.startBulb(getNextBlinkCount());
                timeToWait = getNextWaitingTime();
            }
        }
    }

    private double getNextWaitingTime() {
        if (posWaiting == waitingTimes.size())
            posWaiting = 0;
        return waitingTimes.get(posWaiting++) / Configuration.getEntitySpeedFactor();
    }
    
    private int getNextBlinkCount() {
        if (posBlink == blinkCounts.size()) {
            if (!onRepeat) {
                running = false;
                return 0;
            }
            posBlink = 0;
        }
        return blinkCounts.get(posBlink++);
    }

   @Override
   public boolean hasPassedAllTestCriteria() {
       return onRepeat || posBlink == blinkCounts.size();
   }

public int getTask() {
	if(posBlink == 0 ){
		return blinkCounts.get(posBlink);
	}
	return blinkCounts.get(posBlink-1);		
}

}