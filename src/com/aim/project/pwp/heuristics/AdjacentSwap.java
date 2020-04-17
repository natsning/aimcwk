package com.aim.project.pwp.heuristics;

import java.util.*;

import com.aim.project.pwp.interfaces.HeuristicInterface;
import com.aim.project.pwp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.pwp.interfaces.PWPInstanceInterface;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;


public class AdjacentSwap extends HeuristicOperators implements HeuristicInterface {

	private final Random oRandom;
	
	public AdjacentSwap(Random oRandom) {

		super();
		
		this.oRandom = oRandom;
	}

	@Override
	public double apply( PWPSolutionInterface oSolution, ObjectiveFunctionInterface oObjFunc, double depthOfSearch, double intensityOfMutation) {

		int intensity = (int)Math.pow(2,Math.floor(intensityOfMutation/0.2));
		int[] permutation = oSolution.getSolutionRepresentation().getSolutionRepresentation();
		int[] prevPermutation;
		int indexToSwap = oRandom.nextInt(permutation.length);
		int adjacentIndex = (indexToSwap + 1) % permutation.length;
		double newCost = 0;

		for(int i = 0; i < intensity; i++){

			prevPermutation = oSolution.clone().getSolutionRepresentation().getSolutionRepresentation();
			swap(permutation,indexToSwap,adjacentIndex);

			newCost= deltaEvaluation(oSolution, oObjFunc, prevPermutation,indexToSwap);
			oSolution.setObjectiveFunctionValue(newCost);
		}
		return  newCost;

	}

	@Override
	public boolean isCrossover() {
		return false;
	}

	@Override
	public boolean usesIntensityOfMutation() {
		return true;
	}

	@Override
	public boolean usesDepthOfSearch() {
		return false;
	}

	public double deltaEvaluation(PWPSolutionInterface sol, ObjectiveFunctionInterface objFunc, int[] prev, int i){
		double newCost = 0;
		double original = sol.getObjectiveFunctionValue();
		int lastIndex = prev.length-1;
		int[] current = sol.getSolutionRepresentation().getSolutionRepresentation();

		if (i == lastIndex){ //if first and last are swapped
			newCost = original
					- objFunc.getCostBetweenDepotAnd(prev[0]) - objFunc.getCostBetweenHomeAnd(prev[lastIndex])
					- objFunc.getCost(prev[0],prev[1]) - objFunc.getCost(prev[lastIndex-1],prev[lastIndex])
					+ objFunc.getCostBetweenDepotAnd(current[0]) + objFunc.getCostBetweenHomeAnd(current[lastIndex])
					+ objFunc.getCost(current[0],current[1]) + objFunc.getCost(current[lastIndex-1], current[lastIndex]);
		}
		else if(i == 0){ //if first and second are swapped
			newCost = original
					- objFunc.getCostBetweenDepotAnd(prev[0]) - objFunc.getCost(prev[1],prev[2])
					+ objFunc.getCostBetweenDepotAnd(current[0])  + objFunc.getCost(current[1],current[2]);
		}
		else if(i == lastIndex-1){ //if second last and last are swapped
			newCost = original
					- objFunc.getCostBetweenHomeAnd(prev[lastIndex]) - objFunc.getCost(prev[lastIndex-2],prev[lastIndex-1])
					+ objFunc.getCostBetweenHomeAnd(current[lastIndex]) + objFunc.getCost(current[lastIndex-2],prev[lastIndex-1]);
		}
		else{
			newCost = original
					- objFunc.getCost(prev[i],prev[i-1]) - objFunc.getCost(prev[i+1],prev[i+2])
					+ objFunc.getCost(current[i],current[i-1]) + objFunc.getCost(current[i+1],current[i+2]);
		}
		return newCost;
	}


}

