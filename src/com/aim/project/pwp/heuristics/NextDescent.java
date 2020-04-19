package com.aim.project.pwp.heuristics;


import java.util.Random;

import com.aim.project.pwp.interfaces.HeuristicInterface;
import com.aim.project.pwp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;


/**
 * 
 * @author Warren G. Jackson
 * Performs adjacent swap, returning the first solution with strict improvement
 *
 */
public class NextDescent extends HeuristicOperators implements HeuristicInterface {
	
	private final Random oRandom;
	
	public NextDescent(Random oRandom) {

		super();
		this.oRandom = oRandom;
	}

	@Override
	public double apply(PWPSolutionInterface oSolution, double dDepthOfSearch, double dIntensityOfMutation) {
		double newCost = 0;
		double originalCost = oSolution.getObjectiveFunctionValue();
		int times = (int)Math.floor(dDepthOfSearch/ 0.2) + 1;
		int start = oRandom.nextInt(oSolution.getNumberOfLocations());
		int index = start;
		int[] permutation = oSolution.getSolutionRepresentation().getSolutionRepresentation();
		int[] prevPermutation = oSolution.clone().getSolutionRepresentation().getSolutionRepresentation();
		int indexToSwap,adjacentIndex;

		while(times>0 || index<permutation.length+start){

			indexToSwap = index % permutation.length;
			adjacentIndex = (index+1) % permutation.length;
			swap(permutation,indexToSwap,adjacentIndex);

			newCost = deltaEvaluation(oSolution,prevPermutation,indexToSwap);
			if(newCost < originalCost ){ //IO
				//accept
				oSolution.setObjectiveFunctionValue(newCost);
				originalCost = newCost;
				times --;
				prevPermutation = oSolution.clone().getSolutionRepresentation().getSolutionRepresentation();;

			}else{
				//reject
				swap(permutation,adjacentIndex,indexToSwap);
			}

			index++;

		}

		return oSolution.getObjectiveFunctionValue();
	}

	@Override
	public boolean isCrossover() {
		return false;
	}

	@Override
	public boolean usesIntensityOfMutation() {
		return false;
	}

	@Override
	public boolean usesDepthOfSearch() {
		return true;
	}
}
