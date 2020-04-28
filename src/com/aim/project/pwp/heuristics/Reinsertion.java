package com.aim.project.pwp.heuristics;

import java.util.ArrayList;
import java.util.Random;

import com.aim.project.pwp.interfaces.HeuristicInterface;
import com.aim.project.pwp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;
import com.aim.project.pwp.solution.SolutionRepresentation;


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
		int[] city_ids = oSolution.getSolutionRepresentation().getSolutionRepresentation();
		int[] prevCity_ids;
		ArrayList<Integer> aloCity_ids = new ArrayList<>();
		for (int i : city_ids)
			aloCity_ids.add(i);

		for(int k = 0; k<times ; k++){

			int[] pair = chooseTwo(city_ids.length,oRandom);
			int i = pair[0], j = pair[1];

			Integer removed = aloCity_ids.get(i);
			aloCity_ids.remove(i);
			aloCity_ids.add(j,removed);

			prevCity_ids = oSolution.clone().getSolutionRepresentation().getSolutionRepresentation();
			for(int h=0 ; h<aloCity_ids.size(); h++) {
				city_ids[h] = aloCity_ids.get(h);
			}
			newCost = deltaEvaluation(oSolution,prevCity_ids,i,j);
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
                    + objFunc.getCostBetweenDepotAnd(current[i])
					+ objFunc.getCost(current[j],current[j-1]) + objFunc.getCostBetweenHomeAnd(current[j]);
        }
        else if(i == lastIndex && j==0){ //if tail is reinserted to head
            newCost = original
                    - objFunc.getCostBetweenDepotAnd(prev[j])
                    - objFunc.getCost(prev[i],prev[i-1]) - objFunc.getCostBetweenHomeAnd(prev[i])
                    + objFunc.getCostBetweenDepotAnd(current[j]) + objFunc.getCost(current[j],current[j+1])
                    + objFunc.getCostBetweenHomeAnd(current[i]);
        }
        else if(i == 0){ //if head is reinserted to middle
            newCost = original
                    - objFunc.getCostBetweenDepotAnd(prev[i]) - objFunc.getCost(prev[i],prev[i+1])
                    - objFunc.getCost(prev[j],prev[j+1])
                    + objFunc.getCostBetweenDepotAnd(current[i])
                    + objFunc.getCost(current[j-1],current[j]) + objFunc.getCost(current[j],current[j+1]);
        }
        else if(i == lastIndex){ //if tail is reinserted to middle
            newCost = original
                    - objFunc.getCost(prev[i],prev[i-1]) - objFunc.getCostBetweenHomeAnd(prev[i])
                    - objFunc.getCost(prev[j],prev[j-1])
                    + objFunc.getCost(current[j],current[j-1]) + objFunc.getCost(current[j],current[j+1])
                    + objFunc.getCostBetweenHomeAnd(current[i]);

        }else if(j==0){ //if middle is reinserted to head
            newCost = original
                    - objFunc.getCostBetweenDepotAnd(prev[j])
                    - objFunc.getCost(prev[i],prev[i-1]) - objFunc.getCost(prev[i],prev[i+1])
                    + objFunc.getCostBetweenDepotAnd(current[j]) + objFunc.getCost(current[j],current[j+1])
                    + objFunc.getCost(current[i],current[i+1]);

        }else if(j==lastIndex){ //if middle is reinserted to tail
            newCost = original
                    - objFunc.getCost(prev[i],prev[i-1]) - objFunc.getCost(prev[i],prev[i+1])
                    - objFunc.getCostBetweenHomeAnd(prev[j])
                    + objFunc.getCost(current[i-1],current[i])
                    + objFunc.getCost(current[j],current[j-1]) + objFunc.getCostBetweenHomeAnd(current[j]);

        }else if(i>j){ //if middle is reinserted to somewhere in the front but after head
            newCost = original
                    - objFunc.getCost(prev[i],prev[i-1]) - objFunc.getCost(prev[i],prev[i+1])
                    - objFunc.getCost(prev[j],prev[j-1])
                    + objFunc.getCost(current[j-1],current[j]) + objFunc.getCost(current[j],current[j+1])
                    + objFunc.getCost(current[i],current[i+1]);

        }else{ //if middle is reinserted to somewhere behind but before tail
            newCost = original
                    - objFunc.getCost(prev[i],prev[i-1]) - objFunc.getCost(prev[i],prev[i+1])
                    - objFunc.getCost(prev[j],prev[j+1])
                    + objFunc.getCost(current[j-1],current[j]) + objFunc.getCost(current[j],current[j+1])
                    + objFunc.getCost(current[i],current[i-1]);

        }

        return newCost;

    }
}
