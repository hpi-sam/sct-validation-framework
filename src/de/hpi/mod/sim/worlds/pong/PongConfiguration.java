package de.hpi.mod.sim.worlds.pong;

public class PongConfiguration {

	public static final int stateMachineFactor = 1000;
	public static final double upperBoundary = 0.9;
	public static final double lowerBoundary = -0.9;
	
	//paddle values
    public static final double paddleWidth = 0.08, paddleHeight = 0.4;
    public final static double maxPos = upperBoundary - paddleHeight/2; 	//maxPos in the statechart should be maxPos *1000
    public final static double minPos = lowerBoundary - paddleHeight/2;				//minPos in the statechart should be minPos *1000
    public static final double stepsPerCall = 0.003;


    //ball values
    public static final double diameter = 0.04;
    

    //World values
    public static final double buffer = 0.002;
    
}
