package com.aim.project.pwp.heuristics;

import java.util.Random;

import com.aim.project.pwp.interfaces.HeuristicInterface;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;


/**
 * 
 * @author Warren G. Jackson
 * Performs adjacent swap, returning the first solution with strict improvement
 *
 */
public class DavissHillClimbing extends HeuristicOperators implements HeuristicInterface {
	
	private final Random oRandom;
	
	public DavissHillClimbing(Random oRandom) {
	
		super();
		
		this.oRandom = oRandom;
	}

	@Override
	public double apply(PWPSolutionInterface oSolution, double dDepthOfSearch, double dIntensityOfMutation) {
		int times = (int)Math.floor(dDepthOfSearch/ 0.2) + 1;
		int index = 0;
		int indexToSwap;
		double newCost = 0;
		double originalCost = oSolution.getObjectiveFunctionValue();
		int[] city_ids = oSolution.getSolutionRepresentation().getSolutionRepresentation();
		int[] prevCity_ids = oSolution.clone().getSolutionRepresentation().getSolutionRepresentation();
		int[] sequenceToSwap = createSequenceToSwap(city_ids.length, oRandom);

		while(times!=0 && index<city_ids.length){

			indexToSwap = sequenceToSwap[index];
			swap(city_ids,indexToSwap);

			newCost = deltaEvaluation(oSolution,prevCity_ids,indexToSwap);
			if(newCost <= originalCost ){ //IO
				//accept
				oSolution.setObjectiveFunctionValue(newCost);
				originalCost = newCost;
				times --;
				prevCity_ids = oSolution.clone().getSolutionRepresentation().getSolutionRepresentation();

			}else{
				//reject
				swap(city_ids,indexToSwap);
			}
			index++;
		}
		return newCost;
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

	private int[] createSequenceToSwap(int length, Random r){
		int[] array = new int[length];
		for(int i=0; i<length; i++){
			array[i] = i;
		}
		for (int i = 0; i <length; i++) {
			int randomIndexToSwap = r.nextInt(length);
			swap(array,i,randomIndexToSwap);
		}
		return array;
	}
}
