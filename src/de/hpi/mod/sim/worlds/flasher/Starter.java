package de.hpi.mod.sim.worlds.flasher;

import java.util.List;

import de.hpi.mod.sim.core.Configuration;
import de.hpi.mod.sim.core.scenario.EntitySpecification;
import de.hpi.mod.sim.core.simulation.Entity;

public abstract class Starter implements Entity, EntitySpecification<Starter> {
    
    private Bulb bulb;
    private int posBlink = 0, posWaiting = 0;
    private double timeToWait;

    private List<Integer> blinkCounts;
    private List<Float> waitingTimes;

    public Starter(Bulb bulb) {
        this.bulb = bulb;
        blinkCounts = getBlinkCounts();
        waitingTimes = getWaitingTimes();
        timeToWait = getNextWaitingTime();
    }

    public void update(float delta) {
        timeToWait -= delta;
        if (timeToWait <= 0) {
            bulb.start(getNextBlinkCount());
            System.out.println("Started bulb");
            timeToWait = getNextWaitingTime();
        }
    }

    private double getNextWaitingTime() {
        if (posWaiting == waitingTimes.size())
            posWaiting = 0;
            // If the level is 5, the waiting time equals the given time. For each increased/decreased level, the waiting time halfs/doubles.
        // return waitingTimes.get(posWaiting++) * Math.pow(2, -Configuration.getEntitySpeedLevel() + 5);
        return waitingTimes.get(posWaiting++) / Configuration.getEntitySpeedFactor();
    }
    
    private int getNextBlinkCount() {
        if (posBlink == blinkCounts.size())
            posBlink = 0;
        return blinkCounts.get(posBlink++);
    }
    
    protected abstract List<Integer> getBlinkCounts();

    protected abstract List<Float> getWaitingTimes();

    @Override
    public Starter get() {
        return this;
    }

}