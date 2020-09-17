package de.hpi.mod.sim.worlds.flasher;

import java.util.List;

import de.hpi.mod.sim.core.scenario.EntitySpecification;
import de.hpi.mod.sim.core.simulation.Entity;

public abstract class Starter implements Entity, EntitySpecification<Starter> {
    
    private Bulb bulb;
    private int posBlink = 0, posWaiting = 0;
    private float timeToWait;

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
            timeToWait = getNextWaitingTime();
        }
    }

    private float getNextWaitingTime() {
        if (posWaiting == waitingTimes.size())
            posWaiting = 0;
        return waitingTimes.get(posWaiting++);
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