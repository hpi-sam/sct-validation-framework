/** Generated by YAKINDU Statechart Tools code generator. */
package de.hpi.mod.sim.statemachines.simpletrafficlights;

import com.yakindu.core.IStatemachine;
import com.yakindu.core.ITimed;
import com.yakindu.core.ITimerService;
import java.util.LinkedList;
import java.util.Queue;

public class TrafficLightStatechart implements IStatemachine, ITimed {
	public static class North {
		private boolean green;
		
		
		protected void raiseGreen() {
			green = true;
		}
		public boolean isRaisedGreen() {
			return green;
		}
		
		
		private boolean red;
		
		
		protected void raiseRed() {
			red = true;
		}
		public boolean isRaisedRed() {
			return red;
		}
		
		
	}
	
	public static class East {
		private boolean green;
		
		
		protected void raiseGreen() {
			green = true;
		}
		public boolean isRaisedGreen() {
			return green;
		}
		
		
		private boolean red;
		
		
		protected void raiseRed() {
			red = true;
		}
		public boolean isRaisedRed() {
			return red;
		}
		
		
	}
	
	public static class South {
		private boolean green;
		
		
		protected void raiseGreen() {
			green = true;
		}
		public boolean isRaisedGreen() {
			return green;
		}
		
		
		private boolean red;
		
		
		protected void raiseRed() {
			red = true;
		}
		public boolean isRaisedRed() {
			return red;
		}
		
		
	}
	
	public static class West {
		private boolean green;
		
		
		protected void raiseGreen() {
			green = true;
		}
		public boolean isRaisedGreen() {
			return green;
		}
		
		
		private boolean red;
		
		
		protected void raiseRed() {
			red = true;
		}
		public boolean isRaisedRed() {
			return red;
		}
		
		
	}
	
	protected North north;
	
	protected East east;
	
	protected South south;
	
	protected West west;
	
	public enum State {
		TRAFFIC_LIGHT_OFF,
		TRAFFIC_LIGHT_NORTH,
		TRAFFIC_LIGHT_EAST,
		TRAFFIC_LIGHT_WEST,
		TRAFFIC_LIGHT_WAITEAST,
		TRAFFIC_LIGHT_WAITSOUTH,
		TRAFFIC_LIGHT_WAITWEST,
		TRAFFIC_LIGHT_WAITNORTH,
		TRAFFIC_LIGHT_SOUTH,
		$NULLSTATE$
	};
	
	private final State[] stateVector = new State[1];
	
	private int nextStateIndex;
	
	private ITimerService timerService;
	
	private final boolean[] timeEvents = new boolean[9];
	
	private Queue<Runnable> inEventQueue = new LinkedList<Runnable>();
	private boolean isExecuting;
	
	protected boolean getIsExecuting() {
		return isExecuting;
	}
	
	protected void setIsExecuting(boolean value) {
		this.isExecuting = value;
	}
	public TrafficLightStatechart() {
		north = new North();
		east = new East();
		south = new South();
		west = new West();
		for (int i = 0; i < 1; i++) {
			stateVector[i] = State.$NULLSTATE$;
		}
		
		clearInEvents();
		clearOutEvents();
		
		
		isExecuting = false;
	}
	
	public void enter() {
		if (timerService == null) {
			throw new IllegalStateException("Timer service must be set.");
		}
		
		
		if (getIsExecuting()) {
			return;
		}
		isExecuting = true;
		enterSequence_traffic_light_default();
		isExecuting = false;
	}
	
	public void exit() {
		if (getIsExecuting()) {
			return;
		}
		isExecuting = true;
		exitSequence_traffic_light();
		isExecuting = false;
	}
	
	/**
	 * @see IStatemachine#isActive()
	 */
	public boolean isActive() {
		return stateVector[0] != State.$NULLSTATE$;
	}
	
	/** 
	* Always returns 'false' since this state machine can never become final.
	*
	* @see IStatemachine#isFinal()
	*/
	public boolean isFinal() {
		return false;
	}
	private void clearOutEvents() {
		north.green = false;
		north.red = false;
		east.green = false;
		east.red = false;
		south.green = false;
		south.red = false;
		west.green = false;
		west.red = false;
	}
	
	private void clearInEvents() {
		timeEvents[0] = false;
		timeEvents[1] = false;
		timeEvents[2] = false;
		timeEvents[3] = false;
		timeEvents[4] = false;
		timeEvents[5] = false;
		timeEvents[6] = false;
		timeEvents[7] = false;
		timeEvents[8] = false;
	}
	
	private void runCycle() {
		if (timerService == null) {
			throw new IllegalStateException("Timer service must be set.");
		}
		
		
		if (getIsExecuting()) {
			return;
		}
		isExecuting = true;
		clearOutEvents();
		nextEvent();
		do { 
			for (nextStateIndex = 0; nextStateIndex < stateVector.length; nextStateIndex++) {
				switch (stateVector[nextStateIndex]) {
				case TRAFFIC_LIGHT_OFF:
					traffic_light_Off_react(true);
					break;
				case TRAFFIC_LIGHT_NORTH:
					traffic_light_North_react(true);
					break;
				case TRAFFIC_LIGHT_EAST:
					traffic_light_East_react(true);
					break;
				case TRAFFIC_LIGHT_WEST:
					traffic_light_West_react(true);
					break;
				case TRAFFIC_LIGHT_WAITEAST:
					traffic_light_WaitEast_react(true);
					break;
				case TRAFFIC_LIGHT_WAITSOUTH:
					traffic_light_WaitSouth_react(true);
					break;
				case TRAFFIC_LIGHT_WAITWEST:
					traffic_light_WaitWest_react(true);
					break;
				case TRAFFIC_LIGHT_WAITNORTH:
					traffic_light_WaitNorth_react(true);
					break;
				case TRAFFIC_LIGHT_SOUTH:
					traffic_light_South_react(true);
					break;
				default:
					// $NULLSTATE$
				}
			}
			
			clearInEvents();
			nextEvent();
		} while (((((((((timeEvents[0] || timeEvents[1]) || timeEvents[2]) || timeEvents[3]) || timeEvents[4]) || timeEvents[5]) || timeEvents[6]) || timeEvents[7]) || timeEvents[8]));
		
		isExecuting = false;
	}
	
	protected void nextEvent() {
		if(!inEventQueue.isEmpty()) {
			inEventQueue.poll().run();
			return;
		}
	}
	/**
	* Returns true if the given state is currently active otherwise false.
	*/
	public boolean isStateActive(State state) {
	
		switch (state) {
		case TRAFFIC_LIGHT_OFF:
			return stateVector[0] == State.TRAFFIC_LIGHT_OFF;
		case TRAFFIC_LIGHT_NORTH:
			return stateVector[0] == State.TRAFFIC_LIGHT_NORTH;
		case TRAFFIC_LIGHT_EAST:
			return stateVector[0] == State.TRAFFIC_LIGHT_EAST;
		case TRAFFIC_LIGHT_WEST:
			return stateVector[0] == State.TRAFFIC_LIGHT_WEST;
		case TRAFFIC_LIGHT_WAITEAST:
			return stateVector[0] == State.TRAFFIC_LIGHT_WAITEAST;
		case TRAFFIC_LIGHT_WAITSOUTH:
			return stateVector[0] == State.TRAFFIC_LIGHT_WAITSOUTH;
		case TRAFFIC_LIGHT_WAITWEST:
			return stateVector[0] == State.TRAFFIC_LIGHT_WAITWEST;
		case TRAFFIC_LIGHT_WAITNORTH:
			return stateVector[0] == State.TRAFFIC_LIGHT_WAITNORTH;
		case TRAFFIC_LIGHT_SOUTH:
			return stateVector[0] == State.TRAFFIC_LIGHT_SOUTH;
		default:
			return false;
		}
	}
	
	public void setTimerService(ITimerService timerService) {
		this.timerService = timerService;
	}
	
	public ITimerService getTimerService() {
		return timerService;
	}
	
	public void raiseTimeEvent(int eventID) {
		inEventQueue.add(new Runnable() {
			@Override
			public void run() {
				timeEvents[eventID] = true;
			}
		});
		runCycle();
	}
	
	public North north() {
		return north;
	}
	
	public East east() {
		return east;
	}
	
	public South south() {
		return south;
	}
	
	public West west() {
		return west;
	}
	
	/* Entry action for state 'Off'. */
	private void entryAction_traffic_light_Off() {
		timerService.setTimer(this, 0, (2 * 1000), false);
	}
	
	/* Entry action for state 'North'. */
	private void entryAction_traffic_light_North() {
		timerService.setTimer(this, 1, (3 * 1000), false);
		
		north.raiseGreen();
	}
	
	/* Entry action for state 'East'. */
	private void entryAction_traffic_light_East() {
		timerService.setTimer(this, 2, (3 * 1000), false);
		
		east.raiseGreen();
	}
	
	/* Entry action for state 'West'. */
	private void entryAction_traffic_light_West() {
		timerService.setTimer(this, 3, (3 * 1000), false);
		
		west.raiseGreen();
	}
	
	/* Entry action for state 'WaitEast'. */
	private void entryAction_traffic_light_WaitEast() {
		timerService.setTimer(this, 4, (1 * 1000), false);
	}
	
	/* Entry action for state 'WaitSouth'. */
	private void entryAction_traffic_light_WaitSouth() {
		timerService.setTimer(this, 5, (1 * 1000), false);
	}
	
	/* Entry action for state 'WaitWest'. */
	private void entryAction_traffic_light_WaitWest() {
		timerService.setTimer(this, 6, (1 * 1000), false);
	}
	
	/* Entry action for state 'WaitNorth'. */
	private void entryAction_traffic_light_WaitNorth() {
		timerService.setTimer(this, 7, (1 * 1000), false);
	}
	
	/* Entry action for state 'South'. */
	private void entryAction_traffic_light_South() {
		timerService.setTimer(this, 8, (3 * 1000), false);
		
		south.raiseGreen();
	}
	
	/* Exit action for state 'Off'. */
	private void exitAction_traffic_light_Off() {
		timerService.unsetTimer(this, 0);
	}
	
	/* Exit action for state 'North'. */
	private void exitAction_traffic_light_North() {
		timerService.unsetTimer(this, 1);
		
		north.raiseRed();
	}
	
	/* Exit action for state 'East'. */
	private void exitAction_traffic_light_East() {
		timerService.unsetTimer(this, 2);
		
		east.raiseRed();
	}
	
	/* Exit action for state 'West'. */
	private void exitAction_traffic_light_West() {
		timerService.unsetTimer(this, 3);
		
		west.raiseRed();
	}
	
	/* Exit action for state 'WaitEast'. */
	private void exitAction_traffic_light_WaitEast() {
		timerService.unsetTimer(this, 4);
	}
	
	/* Exit action for state 'WaitSouth'. */
	private void exitAction_traffic_light_WaitSouth() {
		timerService.unsetTimer(this, 5);
	}
	
	/* Exit action for state 'WaitWest'. */
	private void exitAction_traffic_light_WaitWest() {
		timerService.unsetTimer(this, 6);
	}
	
	/* Exit action for state 'WaitNorth'. */
	private void exitAction_traffic_light_WaitNorth() {
		timerService.unsetTimer(this, 7);
	}
	
	/* Exit action for state 'South'. */
	private void exitAction_traffic_light_South() {
		timerService.unsetTimer(this, 8);
		
		south.raiseRed();
	}
	
	/* 'default' enter sequence for state Off */
	private void enterSequence_traffic_light_Off_default() {
		entryAction_traffic_light_Off();
		nextStateIndex = 0;
		stateVector[0] = State.TRAFFIC_LIGHT_OFF;
	}
	
	/* 'default' enter sequence for state North */
	private void enterSequence_traffic_light_North_default() {
		entryAction_traffic_light_North();
		nextStateIndex = 0;
		stateVector[0] = State.TRAFFIC_LIGHT_NORTH;
	}
	
	/* 'default' enter sequence for state East */
	private void enterSequence_traffic_light_East_default() {
		entryAction_traffic_light_East();
		nextStateIndex = 0;
		stateVector[0] = State.TRAFFIC_LIGHT_EAST;
	}
	
	/* 'default' enter sequence for state West */
	private void enterSequence_traffic_light_West_default() {
		entryAction_traffic_light_West();
		nextStateIndex = 0;
		stateVector[0] = State.TRAFFIC_LIGHT_WEST;
	}
	
	/* 'default' enter sequence for state WaitEast */
	private void enterSequence_traffic_light_WaitEast_default() {
		entryAction_traffic_light_WaitEast();
		nextStateIndex = 0;
		stateVector[0] = State.TRAFFIC_LIGHT_WAITEAST;
	}
	
	/* 'default' enter sequence for state WaitSouth */
	private void enterSequence_traffic_light_WaitSouth_default() {
		entryAction_traffic_light_WaitSouth();
		nextStateIndex = 0;
		stateVector[0] = State.TRAFFIC_LIGHT_WAITSOUTH;
	}
	
	/* 'default' enter sequence for state WaitWest */
	private void enterSequence_traffic_light_WaitWest_default() {
		entryAction_traffic_light_WaitWest();
		nextStateIndex = 0;
		stateVector[0] = State.TRAFFIC_LIGHT_WAITWEST;
	}
	
	/* 'default' enter sequence for state WaitNorth */
	private void enterSequence_traffic_light_WaitNorth_default() {
		entryAction_traffic_light_WaitNorth();
		nextStateIndex = 0;
		stateVector[0] = State.TRAFFIC_LIGHT_WAITNORTH;
	}
	
	/* 'default' enter sequence for state South */
	private void enterSequence_traffic_light_South_default() {
		entryAction_traffic_light_South();
		nextStateIndex = 0;
		stateVector[0] = State.TRAFFIC_LIGHT_SOUTH;
	}
	
	/* 'default' enter sequence for region traffic light */
	private void enterSequence_traffic_light_default() {
		react_traffic_light__entry_Default();
	}
	
	/* Default exit sequence for state Off */
	private void exitSequence_traffic_light_Off() {
		nextStateIndex = 0;
		stateVector[0] = State.$NULLSTATE$;
		
		exitAction_traffic_light_Off();
	}
	
	/* Default exit sequence for state North */
	private void exitSequence_traffic_light_North() {
		nextStateIndex = 0;
		stateVector[0] = State.$NULLSTATE$;
		
		exitAction_traffic_light_North();
	}
	
	/* Default exit sequence for state East */
	private void exitSequence_traffic_light_East() {
		nextStateIndex = 0;
		stateVector[0] = State.$NULLSTATE$;
		
		exitAction_traffic_light_East();
	}
	
	/* Default exit sequence for state West */
	private void exitSequence_traffic_light_West() {
		nextStateIndex = 0;
		stateVector[0] = State.$NULLSTATE$;
		
		exitAction_traffic_light_West();
	}
	
	/* Default exit sequence for state WaitEast */
	private void exitSequence_traffic_light_WaitEast() {
		nextStateIndex = 0;
		stateVector[0] = State.$NULLSTATE$;
		
		exitAction_traffic_light_WaitEast();
	}
	
	/* Default exit sequence for state WaitSouth */
	private void exitSequence_traffic_light_WaitSouth() {
		nextStateIndex = 0;
		stateVector[0] = State.$NULLSTATE$;
		
		exitAction_traffic_light_WaitSouth();
	}
	
	/* Default exit sequence for state WaitWest */
	private void exitSequence_traffic_light_WaitWest() {
		nextStateIndex = 0;
		stateVector[0] = State.$NULLSTATE$;
		
		exitAction_traffic_light_WaitWest();
	}
	
	/* Default exit sequence for state WaitNorth */
	private void exitSequence_traffic_light_WaitNorth() {
		nextStateIndex = 0;
		stateVector[0] = State.$NULLSTATE$;
		
		exitAction_traffic_light_WaitNorth();
	}
	
	/* Default exit sequence for state South */
	private void exitSequence_traffic_light_South() {
		nextStateIndex = 0;
		stateVector[0] = State.$NULLSTATE$;
		
		exitAction_traffic_light_South();
	}
	
	/* Default exit sequence for region traffic light */
	private void exitSequence_traffic_light() {
		switch (stateVector[0]) {
		case TRAFFIC_LIGHT_OFF:
			exitSequence_traffic_light_Off();
			break;
		case TRAFFIC_LIGHT_NORTH:
			exitSequence_traffic_light_North();
			break;
		case TRAFFIC_LIGHT_EAST:
			exitSequence_traffic_light_East();
			break;
		case TRAFFIC_LIGHT_WEST:
			exitSequence_traffic_light_West();
			break;
		case TRAFFIC_LIGHT_WAITEAST:
			exitSequence_traffic_light_WaitEast();
			break;
		case TRAFFIC_LIGHT_WAITSOUTH:
			exitSequence_traffic_light_WaitSouth();
			break;
		case TRAFFIC_LIGHT_WAITWEST:
			exitSequence_traffic_light_WaitWest();
			break;
		case TRAFFIC_LIGHT_WAITNORTH:
			exitSequence_traffic_light_WaitNorth();
			break;
		case TRAFFIC_LIGHT_SOUTH:
			exitSequence_traffic_light_South();
			break;
		default:
			break;
		}
	}
	
	/* Default react sequence for initial entry  */
	private void react_traffic_light__entry_Default() {
		enterSequence_traffic_light_Off_default();
	}
	
	private boolean react() {
		return false;
	}
	
	private boolean traffic_light_Off_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (timeEvents[0]) {
				exitSequence_traffic_light_Off();
				enterSequence_traffic_light_North_default();
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
	
	private boolean traffic_light_North_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (timeEvents[1]) {
				exitSequence_traffic_light_North();
				enterSequence_traffic_light_WaitEast_default();
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
	
	private boolean traffic_light_East_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (timeEvents[2]) {
				exitSequence_traffic_light_East();
				enterSequence_traffic_light_WaitSouth_default();
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
	
	private boolean traffic_light_West_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (timeEvents[3]) {
				exitSequence_traffic_light_West();
				enterSequence_traffic_light_WaitNorth_default();
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
	
	private boolean traffic_light_WaitEast_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (timeEvents[4]) {
				exitSequence_traffic_light_WaitEast();
				enterSequence_traffic_light_East_default();
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
	
	private boolean traffic_light_WaitSouth_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (timeEvents[5]) {
				exitSequence_traffic_light_WaitSouth();
				enterSequence_traffic_light_South_default();
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
	
	private boolean traffic_light_WaitWest_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (timeEvents[6]) {
				exitSequence_traffic_light_WaitWest();
				enterSequence_traffic_light_West_default();
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
	
	private boolean traffic_light_WaitNorth_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (timeEvents[7]) {
				exitSequence_traffic_light_WaitNorth();
				enterSequence_traffic_light_North_default();
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
	
	private boolean traffic_light_South_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (timeEvents[8]) {
				exitSequence_traffic_light_South();
				enterSequence_traffic_light_WaitWest_default();
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