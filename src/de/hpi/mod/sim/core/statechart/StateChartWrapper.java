package de.hpi.mod.sim.core.statechart;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import de.hpi.mod.sim.IStatechart;
import de.hpi.mod.sim.ITimer;

/**
 * Handles calls to the statechart.
 * This file and extending classes should be the only ones with logic depending on the statechard implementation.
 */
public abstract class StateChartWrapper<T> {

    /**
     * The generated Statechart
     */

    private SimulationTimerService timer = null;

    protected IStatechart chart;

    public StateChartWrapper() {
        IStatechart chart = createStateChart();
        Method methodToFind = null;
        timer = new SimulationTimerService();
        try {
            methodToFind = chart.getClass().getMethod("setTimer", new Class[] { ITimer.class });
        } catch (NoSuchMethodException | SecurityException e) {
        }
        if (methodToFind != null) {
            try {
                methodToFind.invoke(chart, new Object[] { timer });
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        this.chart = chart;
    }

    public void start() {
        chart.init();
        chart.enter();
    }

    public void close() {
        timer.cancel();
    }

    public void updateTimer() {
        if (this.timer != null) {
            this.timer.update();
            update();
        }
    }

    public String getChartState() {
    	try {
	    	List<String> activeStates = new ArrayList<>();
	    	for(T state : getStates()) {
	    		if(isActive(state)) {
	    			activeStates.add(state.toString());
	    		}		
	    	}
    		return activeStates.get(activeStates.size() - 1);
    	} catch (Exception e) { //avoid problems with version changes
    		return "";
    	}
    }

    public abstract void update();

    public abstract IStatechart createStateChart();

    protected abstract T[] getStates();
    
    protected abstract boolean isActive(T state);
}
