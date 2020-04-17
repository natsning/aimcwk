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
	public double apply( PWPSolutionInterface solution, ObjectiveFunctionInterface objFunc, double depthOfSearch, double intensityOfMutation) {

		int[] prevPermutation = solution.getSolutionRepresentation().clone().getSolutionRepresentation();
		int[] permutation = solution.getSolutionRepresentation().getSolutionRepresentation();
		int indexSwapped = swap(permutation);
		double newCost = deltaEvaluation(solution, objFunc, prevPermutation,indexSwapped);

		solution.setObjectiveFunctionValue(newCost);
		return  newCost;

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
		return false;
	}

	public int swap(int[] array){

		int indexToSwap = oRandom.nextInt(array.length);
		int adjacentIndex = (indexToSwap + 1) % array.length;
		int temp = array[indexToSwap];
		array[indexToSwap] = array[adjacentIndex];
		array[adjacentIndex] = temp;

		return  indexToSwap;
	}

}

