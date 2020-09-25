/** Generated by YAKINDU Statechart Tools code generator. */
package de.hpi.mod.sim.pong;


public class PongStatemachine implements IPongStatemachine {
	protected class SCInterfaceImpl implements SCInterface {
	
		private boolean myPos;
		
		private long myPosValue;
		
		
		public void raiseMyPos(final long value) {
			myPosValue = value;
			myPos = true;
			runCycle();
		}
		protected long getMyPosValue() {
			if (! myPos ) 
				throw new IllegalStateException("Illegal event value access. Event MyPos is not raised!");
			return myPosValue;
		}
		
		private boolean ballPos;
		
		private long ballPosValue;
		
		
		public void raiseBallPos(final long value) {
			ballPosValue = value;
			ballPos = true;
			runCycle();
		}
		protected long getBallPosValue() {
			if (! ballPos ) 
				throw new IllegalStateException("Illegal event value access. Event BallPos is not raised!");
			return ballPosValue;
		}
		
		private boolean up;
		
		
		public boolean isRaisedUp() {
			return up;
		}
		
		protected void raiseUp() {
			up = true;
		}
		
		private boolean down;
		
		
		public boolean isRaisedDown() {
			return down;
		}
		
		protected void raiseDown() {
			down = true;
		}
		
		public long getMaxPos() {
			return maxPos;
		}
		
		public long getMinPos() {
			return minPos;
		}
		
		protected void clearEvents() {
			myPos = false;
			ballPos = false;
		}
		protected void clearOutEvents() {
		
		up = false;
		down = false;
		}
		
	}
	
	
	protected SCInterfaceImpl sCInterface;
	
	private boolean initialized = false;
	
	public enum State {
		pong_Idle_Stop,
		pong_Moving_Up,
		pong_Movin_Down,
		$NullState$
	};
	
	private final State[] stateVector = new State[1];
	
	private int nextStateIndex;
	
	public PongStatemachine() {
		sCInterface = new SCInterfaceImpl();
	}
	
	public void init() {
		this.initialized = true;
		for (int i = 0; i < 1; i++) {
			stateVector[i] = State.$NullState$;
		}
		clearEvents();
		clearOutEvents();
	}
	
	public void enter() {
		if (!initialized) {
			throw new IllegalStateException(
				"The state machine needs to be initialized first by calling the init() function."
			);
		}
		enterSequence_pong_default();
	}
	
	public void runCycle() {
		if (!initialized)
			throw new IllegalStateException(
					"The state machine needs to be initialized first by calling the init() function.");
		clearOutEvents();
		for (nextStateIndex = 0; nextStateIndex < stateVector.length; nextStateIndex++) {
			switch (stateVector[nextStateIndex]) {
			case pong_Idle_Stop:
				pong_Idle_Stop_react(true);
				break;
			case pong_Moving_Up:
				pong_Moving_Up_react(true);
				break;
			case pong_Movin_Down:
				pong_Movin_Down_react(true);
				break;
			default:
				// $NullState$
			}
		}
		clearEvents();
	}
	public void exit() {
		exitSequence_pong();
	}
	
	/**
	 * @see IStatemachine#isActive()
	 */
	public boolean isActive() {
		return stateVector[0] != State.$NullState$;
	}
	
	/** 
	* Always returns 'false' since this state machine can never become final.
	*
	* @see IStatemachine#isFinal()
	*/
	public boolean isFinal() {
		return false;
	}
	/**
	* This method resets the incoming events (time events included).
	*/
	protected void clearEvents() {
		sCInterface.clearEvents();
	}
	
	/**
	* This method resets the outgoing events.
	*/
	protected void clearOutEvents() {
		sCInterface.clearOutEvents();
	}
	
	/**
	* Returns true if the given state is currently active otherwise false.
	*/
	public boolean isStateActive(State state) {
	
		switch (state) {
		case pong_Idle_Stop:
			return stateVector[0] == State.pong_Idle_Stop;
		case pong_Moving_Up:
			return stateVector[0] == State.pong_Moving_Up;
		case pong_Movin_Down:
			return stateVector[0] == State.pong_Movin_Down;
		default:
			return false;
		}
	}
	
	public SCInterface getSCInterface() {
		return sCInterface;
	}
	
	public void raiseMyPos(long value) {
		sCInterface.raiseMyPos(value);
	}
	
	public void raiseBallPos(long value) {
		sCInterface.raiseBallPos(value);
	}
	
	public boolean isRaisedUp() {
		return sCInterface.isRaisedUp();
	}
	
	public boolean isRaisedDown() {
		return sCInterface.isRaisedDown();
	}
	
	public long getMaxPos() {
		return sCInterface.getMaxPos();
	}
	
	public long getMinPos() {
		return sCInterface.getMinPos();
	}
	
	private boolean check_pong__choice_0_tr0_tr0() {
		return (sCInterface.getBallPosValue()<sCInterface.getMyPosValue() && sCInterface.getMyPosValue()<sCInterface.getMinPos());
	}
	
	private boolean check_pong__choice_0_tr2_tr2() {
		return (sCInterface.getBallPosValue()>sCInterface.getMyPosValue() && sCInterface.getMyPosValue()>sCInterface.getMaxPos());
	}
	
	private void effect_pong__choice_0_tr0() {
		enterSequence_pong_Movin_Down_default();
	}
	
	private void effect_pong__choice_0_tr2() {
		enterSequence_pong_Moving_Up_default();
	}
	
	private void effect_pong__choice_0_tr1() {
		enterSequence_pong_Idle_Stop_default();
	}
	
	/* Entry action for state 'Moving Up'. */
	private void entryAction_pong_Moving_Up() {
		sCInterface.raiseUp();
	}
	
	/* Entry action for state 'Movin Down'. */
	private void entryAction_pong_Movin_Down() {
		sCInterface.raiseDown();
	}
	
	/* 'default' enter sequence for state Idle/Stop */
	private void enterSequence_pong_Idle_Stop_default() {
		nextStateIndex = 0;
		stateVector[0] = State.pong_Idle_Stop;
	}
	
	/* 'default' enter sequence for state Moving Up */
	private void enterSequence_pong_Moving_Up_default() {
		entryAction_pong_Moving_Up();
		nextStateIndex = 0;
		stateVector[0] = State.pong_Moving_Up;
	}
	
	/* 'default' enter sequence for state Movin Down */
	private void enterSequence_pong_Movin_Down_default() {
		entryAction_pong_Movin_Down();
		nextStateIndex = 0;
		stateVector[0] = State.pong_Movin_Down;
	}
	
	/* 'default' enter sequence for region pong */
	private void enterSequence_pong_default() {
		react_pong__entry_Default();
	}
	
	/* Default exit sequence for state Idle/Stop */
	private void exitSequence_pong_Idle_Stop() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state Moving Up */
	private void exitSequence_pong_Moving_Up() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state Movin Down */
	private void exitSequence_pong_Movin_Down() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for region pong */
	private void exitSequence_pong() {
		switch (stateVector[0]) {
		case pong_Idle_Stop:
			exitSequence_pong_Idle_Stop();
			break;
		case pong_Moving_Up:
			exitSequence_pong_Moving_Up();
			break;
		case pong_Movin_Down:
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
			if (check_pong__choice_0_tr2_tr2()) {
				effect_pong__choice_0_tr2();
			} else {
				effect_pong__choice_0_tr1();
			}
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
			if (sCInterface.ballPos) {
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
			if ((sCInterface.getMyPosValue()>=sCInterface.getMaxPos() || sCInterface.getBallPosValue()<sCInterface.getMyPosValue())) {
				exitSequence_pong_Moving_Up();
				enterSequence_pong_Idle_Stop_default();
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
	
	private boolean pong_Movin_Down_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if ((sCInterface.getMyPosValue()<=sCInterface.getMinPos() || sCInterface.getBallPosValue()>sCInterface.getMyPosValue())) {
				exitSequence_pong_Movin_Down();
				enterSequence_pong_Idle_Stop_default();
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
