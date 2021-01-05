/** Generated by YAKINDU Statechart Tools code generator. */
package de.hpi.mod.sim;

import com.yakindu.core.IStatemachine;
import java.util.LinkedList;
import java.util.Queue;

public class Paddle2 implements IStatemachine {
	public enum State {
		PONG_IDLE_STOP,
		PONG_MOVING_UP,
		PONG_MOVIN_DOWN,
		$NULLSTATE$
	};
	
	private final State[] stateVector = new State[1];
	
	private int nextStateIndex;
	
	private Queue<Runnable> inEventQueue = new LinkedList<Runnable>();
	private boolean isExecuting;
	
	protected boolean getIsExecuting() {
		return isExecuting;
	}
	
	protected void setIsExecuting(boolean value) {
		this.isExecuting = value;
	}
	public Paddle2() {
		for (int i = 0; i < 1; i++) {
			stateVector[i] = State.$NULLSTATE$;
		}
		
		clearInEvents();
		clearOutEvents();
		
		
		isExecuting = false;
	}
	
	public void enter() {
		if (this.operationCallback == null) {
			throw new IllegalStateException("Operation callback must be set.");
		}
		
		
		if (getIsExecuting()) {
			return;
		}
		isExecuting = true;
		enterSequence_pong_default();
		isExecuting = false;
	}
	
	public void exit() {
		if (getIsExecuting()) {
			return;
		}
		isExecuting = true;
		exitSequence_pong();
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
		up = false;
		down = false;
	}
	
	private void clearInEvents() {
		ballPos = false;
	}
	
	private void runCycle() {
		if (this.operationCallback == null) {
			throw new IllegalStateException("Operation callback must be set.");
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
				case PONG_IDLE_STOP:
					pong_Idle_Stop_react(true);
					break;
				case PONG_MOVING_UP:
					pong_Moving_Up_react(true);
					break;
				case PONG_MOVIN_DOWN:
					pong_Movin_Down_react(true);
					break;
				default:
					// $NULLSTATE$
				}
			}
			
			clearInEvents();
			nextEvent();
		} while (ballPos);
		
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
		case PONG_IDLE_STOP:
			return stateVector[0] == State.PONG_IDLE_STOP;
		case PONG_MOVING_UP:
			return stateVector[0] == State.PONG_MOVING_UP;
		case PONG_MOVIN_DOWN:
			return stateVector[0] == State.PONG_MOVIN_DOWN;
		default:
			return false;
		}
	}
	
	public interface OperationCallback {
	
		public long myPos();
		
	}
	
	private OperationCallback operationCallback;
	
	public void setOperationCallback(OperationCallback operationCallback) {
		this.operationCallback = operationCallback;
	}
	
	private boolean ballPos;
	
	private long ballPosValue;
	
	
	public void raiseBallPos(final long value) {
		inEventQueue.add(new Runnable() {
			@Override
			public void run() {
				ballPosValue = value;
				ballPos = true;
			}
		});
		runCycle();
	}
	protected long getBallPosValue() {
		if (! ballPos ) 
			throw new IllegalStateException("Illegal event value access. Event BallPos is not raised!");
		return ballPosValue;
	}
	
	private boolean up;
	
	
	protected void raiseUp() {
		up = true;
	}
	public boolean isRaisedUp() {
		return up;
	}
	
	
	private boolean down;
	
	
	protected void raiseDown() {
		down = true;
	}
	public boolean isRaisedDown() {
		return down;
	}
	
	
	public static final long maxPos = 700;
	
	public long getMaxPos() {
		return maxPos;
	}
	
	public static final long minPos = -700;
	
	public long getMinPos() {
		return minPos;
	}
	
	private boolean check_pong__choice_0_tr0_tr0() {
		return (operationCallback.myPos()>getBallPosValue() && operationCallback.myPos()>getMinPos());
	}
	
	private boolean check_pong__choice_0_tr1_tr1() {
		return (operationCallback.myPos()<getBallPosValue() && operationCallback.myPos()<getMaxPos());
	}
	
	private boolean check_pong__choice_1_tr0_tr0() {
		return (operationCallback.myPos()>=getMaxPos() || operationCallback.myPos()>getBallPosValue());
	}
	
	private boolean check_pong__choice_2_tr0_tr0() {
		return (operationCallback.myPos()<=getMinPos() || getBallPosValue()>operationCallback.myPos());
	}
	
	private void effect_pong__choice_0_tr0() {
		enterSequence_pong_Movin_Down_default();
	}
	
	private void effect_pong__choice_0_tr1() {
		enterSequence_pong_Moving_Up_default();
	}
	
	private void effect_pong__choice_0_tr2() {
		enterSequence_pong_Idle_Stop_default();
	}
	
	private void effect_pong__choice_1_tr0() {
		enterSequence_pong_Idle_Stop_default();
	}
	
	private void effect_pong__choice_1_tr1() {
		enterSequence_pong_Moving_Up_default();
	}
	
	private void effect_pong__choice_2_tr0() {
		enterSequence_pong_Idle_Stop_default();
	}
	
	private void effect_pong__choice_2_tr1() {
		enterSequence_pong_Movin_Down_default();
	}
	
	/* Entry action for state 'Moving Up'. */
	private void entryAction_pong_Moving_Up() {
		raiseUp();
	}
	
	/* Entry action for state 'Movin Down'. */
	private void entryAction_pong_Movin_Down() {
		raiseDown();
	}
	
	/* 'default' enter sequence for state Idle/Stop */
	private void enterSequence_pong_Idle_Stop_default() {
		nextStateIndex = 0;
		stateVector[0] = State.PONG_IDLE_STOP;
	}
	
	/* 'default' enter sequence for state Moving Up */
	private void enterSequence_pong_Moving_Up_default() {
		entryAction_pong_Moving_Up();
		nextStateIndex = 0;
		stateVector[0] = State.PONG_MOVING_UP;
	}
	
	/* 'default' enter sequence for state Movin Down */
	private void enterSequence_pong_Movin_Down_default() {
		entryAction_pong_Movin_Down();
		nextStateIndex = 0;
		stateVector[0] = State.PONG_MOVIN_DOWN;
	}
	
	/* 'default' enter sequence for region pong */
	private void enterSequence_pong_default() {
		react_pong__entry_Default();
	}
	
	/* Default exit sequence for state Idle/Stop */
	private void exitSequence_pong_Idle_Stop() {
		nextStateIndex = 0;
		stateVector[0] = State.$NULLSTATE$;
	}
	
	/* Default exit sequence for state Moving Up */
	private void exitSequence_pong_Moving_Up() {
		nextStateIndex = 0;
		stateVector[0] = State.$NULLSTATE$;
	}
	
	/* Default exit sequence for state Movin Down */
	private void exitSequence_pong_Movin_Down() {
		nextStateIndex = 0;
		stateVector[0] = State.$NULLSTATE$;
	}
	
	/* Default exit sequence for region pong */
	private void exitSequence_pong() {
		switch (stateVector[0]) {
		case PONG_IDLE_STOP:
			exitSequence_pong_Idle_Stop();
			break;
		case PONG_MOVING_UP:
			exitSequence_pong_Moving_Up();
			break;
		case PONG_MOVIN_DOWN:
			exitSequence_pong_Movin_Down();
			break;
		default:
			break;
		}
	}
	
	/* The reactions of state null. */
	private void react_pong__choice_0() {
		if (check_pong__choice_0_tr0_tr0()) {
			effect_pong__choice_0_tr0();
		} else {
			if (check_pong__choice_0_tr1_tr1()) {
				effect_pong__choice_0_tr1();
			} else {
				effect_pong__choice_0_tr2();
			}
		}
	}
	
	/* The reactions of state null. */
	private void react_pong__choice_1() {
		if (check_pong__choice_1_tr0_tr0()) {
			effect_pong__choice_1_tr0();
		} else {
			effect_pong__choice_1_tr1();
		}
	}
	
	/* The reactions of state null. */
	private void react_pong__choice_2() {
		if (check_pong__choice_2_tr0_tr0()) {
			effect_pong__choice_2_tr0();
		} else {
			effect_pong__choice_2_tr1();
		}
	}
	
	/* Default react sequence for initial entry  */
	private void react_pong__entry_Default() {
		enterSequence_pong_Idle_Stop_default();
	}
	
	private boolean react() {
		return false;
	}
	
	private boolean pong_Idle_Stop_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (ballPos) {
				exitSequence_pong_Idle_Stop();
				react_pong__choice_0();
			} else {
				did_transition = false;
			}
		}
		if (did_transition==false) {
			did_transition = react();
		}
		return did_transition;
	}
	
	private boolean pong_Moving_Up_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (ballPos) {
				exitSequence_pong_Moving_Up();
				react_pong__choice_1();
			} else {
				did_transition = false;
			}
		}
		if (did_transition==false) {
			did_transition = react();
		}
		return did_transition;
	}
	
	private boolean pong_Movin_Down_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (ballPos) {
				exitSequence_pong_Movin_Down();
				react_pong__choice_2();
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
