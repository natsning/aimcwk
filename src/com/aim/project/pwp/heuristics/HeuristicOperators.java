package com.aim.project.pwp.heuristics;

import com.aim.project.pwp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;

import java.util.Random;

public class HeuristicOperators {

	private ObjectiveFunctionInterface oObjectiveFunction;

	public HeuristicOperators() {

	}

	public void setObjectiveFunction(ObjectiveFunctionInterface f) {

		this.oObjectiveFunction = f;
	}

	public ObjectiveFunctionInterface getoObjectiveFunction() {
		return oObjectiveFunction;
	}

	public void swap(int[] array, int i,int j){

		int temp = array[i];
		array[i] = array[j];
		array[j] = temp;

	}

	public int[] chooseTwo(int range, Random oRandom){
		int i = oRandom.nextInt(range);
		int j = i;
		while(j == i){ //ensures i and j are two different indexes
			j = oRandom.nextInt(range);
		}
		int[] pair = {i,j};
		return pair;
	}

	/**
	 * Delta Evaluation for adjacent swaps
	 * @param sol
	 * @param prev
	 * @param i
	 * @return
	 */
	public double deltaEvaluation(PWPSolutionInterface sol, int[] prev, int i){
		double newCost = 0;
		double original = sol.getObjectiveFunctionValue();
		int lastIndex = prev.length-1;
		int[] current = sol.getSolutionRepresentation().getSolutionRepresentation();
		ObjectiveFunctionInterface objFunc = getoObjectiveFunction();

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
