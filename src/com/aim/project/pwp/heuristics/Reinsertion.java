package com.aim.project.pwp.heuristics;

import java.util.ArrayList;
import java.util.Random;

import com.aim.project.pwp.interfaces.HeuristicInterface;
import com.aim.project.pwp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;


public class Reinsertion extends HeuristicOperators implements HeuristicInterface {

	private final Random oRandom;
	
	public Reinsertion(Random oRandom) {

		super();
		
		this.oRandom = oRandom;
	}

	@Override
	public double apply(PWPSolutionInterface oSolution, double depthOfSearch, double intensityOfMutation) {
		int times = (int)Math.floor(intensityOfMutation / 0.2) + 1;
		double newCost = 0;
		int[] permutation = oSolution.getSolutionRepresentation().getSolutionRepresentation();
		int[] prevPermutation;
		ArrayList<Integer> aloPermutation = new ArrayList<>();
		for (int i : permutation)
			aloPermutation.add(i);

		for(int k = 0; k<times ; k++){

			int[] pair = chooseTwo(permutation.length,oRandom);
			int i = pair[0], j = pair[1];

			Integer removed = aloPermutation.get(i);
			aloPermutation.remove(i);
			aloPermutation.add(j,removed);

			prevPermutation = oSolution.clone().getSolutionRepresentation().getSolutionRepresentation();
			for(int h=0 ; h<aloPermutation.size(); h++) {
				permutation[h] = aloPermutation.get(h);
			}
			newCost = deltaEvaluation(oSolution,prevPermutation,i,j);
			oSolution.setObjectiveFunctionValue(newCost);

		}

		return  newCost;
	}

	@Override
	public boolean isCrossover() {
		return  false;
	}

	@Override
	public boolean usesIntensityOfMutation() {
		return true;
	}

	@Override
	public boolean usesDepthOfSearch() {
		return false;
	}

	public double deltaEvaluation(PWPSolutionInterface sol, int[] prev, int i, int j){
        double newCost;
        double original = sol.getObjectiveFunctionValue();
		ObjectiveFunctionInterface objFunc = getoObjectiveFunction();
        int lastIndex = prev.length-1;
        int[] current = sol.getSolutionRepresentation().getSolutionRepresentation();

        if (i==0 && j==lastIndex){ //if head is reinserted to tail
            newCost = original
                    - objFunc.getCostBetweenDepotAnd(prev[i]) - objFunc.getCost(prev[i],prev[i+1])
                    - objFunc.getCostBetweenHomeAnd(prev[j])
                    + objFunc.getCost(current[j],current[j-1]) + objFunc.getCostBetweenHomeAnd(current[j])
                    + objFunc.getCostBetweenDepotAnd(current[i]);
        }
        else if(i == lastIndex && j==0){ //if tail is reinserted to head
            newCost = original
                    - objFunc.getCost(prev[i],prev[i-1]) - objFunc.getCostBetweenHomeAnd(prev[i])
                    - objFunc.getCostBetweenDepotAnd(prev[j])
                    + objFunc.getCostBetweenDepotAnd(current[j]) + objFunc.getCost(current[j],current[j+1])
                    + objFunc.getCostBetweenDepotAnd(current[i]);
        }
        else if(i == 0){ //if head is reinserted to middle
            newCost = original
                    - objFunc.getCostBetweenDepotAnd(prev[i]) - objFunc.getCost(prev[i],prev[i+1])
                    - objFunc.getCost(prev[j],prev[j+1])
                    + objFunc.getCost(current[j-1],current[j]) + objFunc.getCost(current[j],current[j+1])
                    + objFunc.getCostBetweenDepotAnd(current[i]);
        }
        else if(i == lastIndex){ //if tail is reinserted to middle
            newCost = original
                    - objFunc.getCost(prev[i],prev[i-1]) - objFunc.getCostBetweenHomeAnd(prev[i])
                    - objFunc.getCost(prev[j],prev[j-1])
                    + objFunc.getCost(current[j],current[j-1]) + objFunc.getCost(current[j],current[j+1])
                    + objFunc.getCostBetweenHomeAnd(current[i]);

        }else if(j==0){ //if middle is reinserted to head
            newCost = original
                    - objFunc.getCost(prev[i],prev[i-1]) - objFunc.getCost(prev[i],prev[i+1])
                    - objFunc.getCostBetweenDepotAnd(prev[j])
                    + objFunc.getCostBetweenDepotAnd(current[j]) + objFunc.getCost(current[j],current[j+1])
                    + objFunc.getCost(current[i],current[i+1]);

        }else if(j==lastIndex){ //if middle is reinserted to tail
            newCost = original
                    - objFunc.getCost(prev[i],prev[i-1]) - objFunc.getCost(prev[i],prev[i+1])
                    - objFunc.getCostBetweenHomeAnd(prev[j])
                    + objFunc.getCost(current[j],current[j-1]) + objFunc.getCostBetweenHomeAnd(current[j])
                    + objFunc.getCost(current[i-1],current[i]);

        }else if(i>j){ //if middle is reinserted to somewhere behind but before tail
            newCost = original
                    - objFunc.getCost(prev[i],prev[i-1]) - objFunc.getCost(prev[i],prev[i+1])
                    - objFunc.getCost(prev[j],prev[j+1])
                    + objFunc.getCost(current[j-1],current[j]) + objFunc.getCost(current[j],current[j+1])
                    + objFunc.getCost(current[i-1],current[i]);

        }else{ //if middle is reinserted to somewhere in the front but after head
            newCost = original
                    - objFunc.getCost(prev[i],prev[i-1]) - objFunc.getCost(prev[i],prev[i+1])
                    - objFunc.getCost(prev[j],prev[j-1])
                    + objFunc.getCost(current[j-1],current[j]) + objFunc.getCost(current[j],current[j+1])
                    + objFunc.getCost(current[i],current[i+1]);
        }

        return newCost;

    }
}
