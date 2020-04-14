package com.aim.project.pwp;

import com.aim.project.pwp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.pwp.interfaces.PWPInstanceInterface;
import com.aim.project.pwp.interfaces.SolutionRepresentationInterface;

public class PWPObjectiveFunction implements ObjectiveFunctionInterface {
	
	private final PWPInstanceInterface oInstance;
	
	public PWPObjectiveFunction(PWPInstanceInterface oInstance) {
		
		this.oInstance = oInstance;
	}

	@Override
	public double getObjectiveFunctionValue(SolutionRepresentationInterface oSolution) {
		
	}
	
	@Override
	public double getCost(int iLocationA, int iLocationB) {
		
	}

	@Override
	public double getCostBetweenDepotAnd(int iLocation) {
		
	}

	@Override
	public double getCostBetweenHomeAnd(int iLocation) {
		
	}
}
