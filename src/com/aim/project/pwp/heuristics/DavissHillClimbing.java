package com.aim.project.pwp.heuristics;

import java.util.ArrayList;
import java.util.Collections;
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
		int indexToSwap,adjacentIndex;
		double newCost = 0;
		double originalCost = oSolution.getObjectiveFunctionValue();
		int[] permutation = oSolution.getSolutionRepresentation().getSolutionRepresentation();
		int[] prevPermutation = oSolution.clone().getSolutionRepresentation().getSolutionRepresentation();
		int[] sequenceToSwap = createSequenceToSwap(permutation.length, oRandom);

		while(times!=0||index<permutation.length){

			indexToSwap = sequenceToSwap[index];
			adjacentIndex = (indexToSwap+1) % permutation.length;
			swap(permutation,indexToSwap,adjacentIndex);

			newCost = deltaEvaluation(oSolution,prevPermutation,indexToSwap);
			if(newCost <= originalCost ){ //IO
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
