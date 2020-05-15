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
		double newCost ;
		double originalCost = oSolution.getObjectiveFunctionValue();
		int times = (int)Math.floor(dDepthOfSearch/ 0.2) + 1;
		int offset = oRandom.nextInt(oSolution.getNumberOfLocations()-2);
		int index = offset;
		int[] city_ids = oSolution.getSolutionRepresentation().getSolutionRepresentation();
		int[] prevCity_ids = oSolution.clone().getSolutionRepresentation().getSolutionRepresentation();
		int indexToSwap;

		while(times!=0 && index<city_ids.length+offset){

			indexToSwap = index % city_ids.length;
			swap(city_ids,indexToSwap);

			newCost = deltaEvaluation(oSolution,prevCity_ids,indexToSwap);
			if(newCost < originalCost ){ //IO
				//accept
				oSolution.setObjectiveFunctionValue(newCost);
				originalCost = newCost;
				times --;
				prevCity_ids = oSolution.clone().getSolutionRepresentation().getSolutionRepresentation();
				offset = index;

			}else{
				//reject
				swap(city_ids,indexToSwap);
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
