package com.aim.project.pwp.heuristics;

import java.util.*;

import com.aim.project.pwp.interfaces.HeuristicInterface;
import com.aim.project.pwp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;


public class InversionMutation extends HeuristicOperators implements HeuristicInterface {
	
	private final Random oRandom;
	
	public InversionMutation(Random oRandom) {
	
		super();
		
		this.oRandom = oRandom;
	}

	@Override
	public double apply(PWPSolutionInterface oSolution, ObjectiveFunctionInterface oObjFunc, double dDepthOfSearch, double dIntensityOfMutation) {
		int intensity = (int)Math.floor(dIntensityOfMutation / 0.2) + 1;
		int[] permutation = oSolution.getSolutionRepresentation().getSolutionRepresentation();
		int[] prevPermutation;
		ArrayList<Integer> aloSubPermutation = new ArrayList<>();
		double newCost = 0;

		for(int k = 0; k<intensity ; k++){

			//pick two indexes
			int i = oRandom.nextInt(permutation.length);
			int j = i;
			while(j == i){ //ensures i and j are two different indexes
				j = oRandom.nextInt(permutation.length);
			}
			if(i>j){ //i will always be smaller than j
				int temp = i;
				i = j;
				j = temp;
			}

			prevPermutation = oSolution.clone().getSolutionRepresentation().getSolutionRepresentation();
			//reverse
			for (int h = i; h <= j; h++ ){
				aloSubPermutation.add(permutation[h]);
			}
			Collections.reverse(aloSubPermutation);
			for(int h = 0; h < aloSubPermutation.size(); h++ ){
				permutation[j] = aloSubPermutation.get(h);
				j--;
			}
			aloSubPermutation.clear();

			newCost = deltaEvaluation(oSolution,oObjFunc,prevPermutation,i,j);
			oSolution.setObjectiveFunctionValue(newCost);

		}//end for

		return newCost;
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

	public double deltaEvaluation(PWPSolutionInterface sol, ObjectiveFunctionInterface objFunc, int[] prev, int i, int j){
		double newCost;
		double original = sol.getObjectiveFunctionValue();
		int lastIndex = prev.length-1;
		int[] current = sol.getSolutionRepresentation().getSolutionRepresentation();

		if (i==0 && j==lastIndex){ //if first and last are chosen
			newCost = original
					- objFunc.getCostBetweenDepotAnd(prev[0]) - objFunc.getCostBetweenHomeAnd(prev[lastIndex])
					+ objFunc.getCostBetweenDepotAnd(current[0]) + objFunc.getCostBetweenHomeAnd(current[lastIndex]);
		}
		else if(i == 0){ //if first is chosen
			newCost = original
					- objFunc.getCostBetweenDepotAnd(prev[0]) - objFunc.getCost(prev[j],prev[j+1])
					+ objFunc.getCostBetweenDepotAnd(current[0])  + objFunc.getCost(current[j],current[j+1]);
		}
		else if(j == lastIndex){ //if last is chosen
			newCost = original
					- objFunc.getCostBetweenHomeAnd(prev[lastIndex]) - objFunc.getCost(prev[i],prev[i-1])
					+ objFunc.getCostBetweenHomeAnd(current[lastIndex]) + objFunc.getCost(current[i],prev[i-1]);
		}
		else{
			newCost = original
					- objFunc.getCost(prev[i],prev[i-1]) - objFunc.getCost(prev[j],prev[j+1])
					+ objFunc.getCost(current[i],current[i-1]) + objFunc.getCost(current[j],current[j+1]);
		}
		return newCost;
	}


}
