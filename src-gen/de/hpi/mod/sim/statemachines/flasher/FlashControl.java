/** Generated by YAKINDU Statechart Tools code generator. */
package de.hpi.mod.sim.statemachines.flasher;

import com.yakindu.core.IStatemachine;
import com.yakindu.core.ITimed;
import com.yakindu.core.ITimerService;
import java.util.LinkedList;
import java.util.Queue;

public class FlashControl implements IStatemachine, ITimed {
	public enum State {
		FLASHER_OFF,
		FLASHER_ON,
		$NULLSTATE$
	};
	
	private final State[] stateVector = new State[1];
	
	private int nextStateIndex;
	
	private ITimerService timerService;
	
	private final boolean[] timeEvents = new boolean[2];
	
	private Queue<Runnable> inEventQueue = new LinkedList<Runnable>();
	private long count;
	
	protected long getCount() {
		return count;
	}
	
	protected void setCount(long value) {
		this.count = value;
	}
	
	
	private boolean isExecuting;
	
	protected boolean getIsExecuting() {
		return isExecuting;
	}
	
	protected void setIsExecuting(boolean value) {
		this.isExecuting = value;
	}
	public FlashControl() {
		for (int i = 0; i < 1; i++) {
			stateVector[i] = State.$NULLSTATE$;
		}
		
		clearInEvents();
		clearOutEvents();
		
		setCount(0);
		
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
		enterSequence_flasher_default();
		isExecuting = false;
	}
	
	public void exit() {
		if (getIsExecuting()) {
			return;
		}
		isExecuting = true;
		exitSequence_flasher();
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
		on = false;
		off = false;
	}
	
	private void clearInEvents() {
		start = false;
		timeEvents[0] = false;
		timeEvents[1] = false;
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
				case FLASHER_OFF:
					flasher_Off_react(true);
					break;
				case FLASHER_ON:
					flasher_On_react(true);
					break;
				default:
					// $NULLSTATE$
				}
			}
			
			clearInEvents();
			nextEvent();
		} while (((start || timeEvents[0]) || timeEvents[1]));
		
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
		case FLASHER_OFF:
			return stateVector[0] == State.FLASHER_OFF;
		case FLASHER_ON:
			return stateVector[0] == State.FLASHER_ON;
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
	
	private boolean start;
	
	private long startValue;
	
	
	public void raiseStart(final long value) {
		inEventQueue.add(new Runnable() {
			@Override
			public void run() {
				startValue = value;
				start = true;
			}
		});
		runCycle();
	}
	protected long getStartValue() {
		if (! start ) 
			throw new IllegalStateException("Illegal event value access. Event Start is not raised!");
		return startValue;
	}
	
	private boolean on;
	
	
	protected void raiseOn() {
		on = true;
	}
	public boolean isRaisedOn() {
		return on;
	}
	
	
	private boolean off;
	
	
	protected void raiseOff() {
		off = true;
	}
	public boolean isRaisedOff() {
		return off;
	}
	
	
	private boolean check_flasher__choice_0_tr0_tr0() {
		return getCount()>0;
	}
	
	private boolean check_flasher__choice_0_tr1_tr1() {
		return getCount()<=0;
	}
	
	private void effect_flasher__choice_0_tr0() {
		enterSequence_flasher_On_default();
	}
	
	private void effect_flasher__choice_0_tr1() {
		enterSequence_flasher_Off_default();
	}
	
	/* Entry action for state 'Off'. */
	private void entryAction_flasher_Off() {
		timerService.setTimer(this, 0, 500, false);
	}
	
	/* Entry action for state 'On'. */
	private void entryAction_flasher_On() {
		timerService.setTimer(this, 1, 500, false);
		
		raiseOn();
		
		setCount((count - 1));
	}
	
	/* Exit action for state 'Off'. */
	private void exitAction_flasher_Off() {
		timerService.unsetTimer(this, 0);
	}
	
	/* Exit action for state 'On'. */
	private void exitAction_flasher_On() {
		timerService.unsetTimer(this, 1);
		
		raiseOff();
	}
	
	/* 'default' enter sequence for state Off */
	private void enterSequence_flasher_Off_default() {
		entryAction_flasher_Off();
		nextStateIndex = 0;
		stateVector[0] = State.FLASHER_OFF;
	}
	
	/* 'default' enter sequence for state On */
	private void enterSequence_flasher_On_default() {
		entryAction_flasher_On();
		nextStateIndex = 0;
		stateVector[0] = State.FLASHER_ON;
	}
	
	/* 'default' enter sequence for region flasher */
	private void enterSequence_flasher_default() {
		react_flasher__entry_Default();
	}
	
	/* Default exit sequence for state Off */
	private void exitSequence_flasher_Off() {
		nextStateIndex = 0;
		stateVector[0] = State.$NULLSTATE$;
		
		exitAction_flasher_Off();
	}
	
	/* Default exit sequence for state On */
	private void exitSequence_flasher_On() {
		nextStateIndex = 0;
		stateVector[0] = State.$NULLSTATE$;
		
		exitAction_flasher_On();
	}
	
	/* Default exit sequence for region flasher */
	private void exitSequence_flasher() {
		switch (stateVector[0]) {
		case FLASHER_OFF:
			exitSequence_flasher_Off();
			break;
		case FLASHER_ON:
			exitSequence_flasher_On();
			break;
		default:
			break;
		}
	}
	
	/* The reactions of state null. */
	private void react_flasher__choice_0() {
		if (check_flasher__choice_0_tr0_tr0()) {
			effect_flasher__choice_0_tr0();
		} else {
			if (check_flasher__choice_0_tr1_tr1()) {
				effect_flasher__choice_0_tr1();
			}
		}
	}
	
	/* Default react sequence for initial entry  */
	private void react_flasher__entry_Default() {
		enterSequence_flasher_Off_default();
	}
	
	private boolean react() {
		return false;
	}
	
	private boolean flasher_Off_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (start) {
				exitSequence_flasher_Off();
				setCount(getStartValue());
				
				react_flasher__choice_0();
			} else {
				if (((timeEvents[0]) && (getCount()>0))) {
					exitSequence_flasher_Off();
					enterSequence_flasher_On_default();
					react();
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
	
	private boolean flasher_On_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (timeEvents[1]) {
				exitSequence_flasher_On();
				enterSequence_flasher_Off_default();
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
