package de.hpi.mod.sim;

/**
 * Basic interface for state charts.
 */
public interface IStatechart {

	/**
	 * Initializes the state chart. Used to initialize internal variables etc.
	 */
	public void init();

	/**
	 * Enters the state chart. Sets the state chart into a defined state.
	 */
	public void enter();

	/**
	 * Exits the state chart. Leaves the state chart with a defined state.
	 */
	public void exit();

	/**
	 * Checks whether the state chart is active. 
	 * A state chart is active if it has been entered. It is inactive if it has not been entered at all or if it has been exited.
	 */
	public boolean isActive();

	/**
	 * Checks whether all active states are final. 
	 * If there are no active states then the state chart is considered being incative. In this case this method returns <code>false</code>.
	 */
	public boolean isFinal();

	/**
	* Start a run-to-completion cycle.
	*/
	public void runCycle();
}
