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
	public double apply( PWPSolutionInterface oSolution, double depthOfSearch, double intensityOfMutation) {

		int intensity = (int)Math.pow(2,Math.floor(intensityOfMutation/0.2));
		int[] city_ids = oSolution.getSolutionRepresentation().getSolutionRepresentation();
		int[] prevPermutation;
		int indexToSwap,adjacentIndex;
		double newCost = 0;

		for(int i = 0; i < intensity; i++){

			prevPermutation = oSolution.clone().getSolutionRepresentation().getSolutionRepresentation();

			indexToSwap = oRandom.nextInt(city_ids.length);
			adjacentIndex = (indexToSwap + 1) % city_ids.length;
			swap(city_ids,indexToSwap,adjacentIndex);

			newCost= deltaEvaluation(oSolution,prevPermutation,indexToSwap);
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




}

