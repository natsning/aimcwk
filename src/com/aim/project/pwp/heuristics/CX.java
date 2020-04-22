package com.aim.project.pwp.heuristics;

import java.util.HashMap;
import java.util.Random;

import com.aim.project.pwp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;
import com.aim.project.pwp.interfaces.XOHeuristicInterface;
import com.aim.project.pwp.solution.PWPSolution;
import com.aim.project.pwp.solution.SolutionRepresentation;


public class CX implements XOHeuristicInterface {
	
	private final Random oRandom;
	
	private ObjectiveFunctionInterface oObjectiveFunction;

	public CX(Random oRandom) {
		
		this.oRandom = oRandom;
	}

	@Override
	public double apply(PWPSolutionInterface solution, double depthOfSearch, double intensityOfMutation) {
		return solution.getObjectiveFunctionValue();
	}

	@Override
	public double apply(PWPSolutionInterface p1, PWPSolutionInterface p2,
			PWPSolutionInterface c, double depthOfSearch, double intensityOfMutation) {

		int times = (int)Math.floor(intensityOfMutation / 0.2) + 1;
		int length = p1.getNumberOfLocations()-2;
		int[] cityID_p1 = p1.clone().getSolutionRepresentation().getSolutionRepresentation();
		int[] cityID_p2 = p2.clone().getSolutionRepresentation().getSolutionRepresentation();
		int[] backup_cityID_p1 = new int[length], backup_cityID_p2 = new int[length];
		int valueIndex = 0, flagIndex = 1;
		int startID,key;
		HashMap<Integer,int[]> hash;


		for(int loop =0; loop<times; loop++) {

			System.arraycopy(cityID_p1,0,backup_cityID_p1,0,length);
			System.arraycopy(cityID_p2,0,backup_cityID_p2,0,length);
			// HashMap = < p1cityID , [p2cityID, flag] >
			hash = createHash(cityID_p1, cityID_p2);
			startID = oRandom.nextInt(length);
			key = startID;

			//find 1st cycle
			while (hash.get(key)[flagIndex] == 0) {
				hash.get(key)[flagIndex] = 1;
				key = hash.get(key)[valueIndex];
			}

			//copy non cycles
			for (int i = 0; i < length; i++) {
				if (hash.get(cityID_p1[i])[flagIndex] == 1) {
					cityID_p1[i] = backup_cityID_p1[i];
					cityID_p2[i] = backup_cityID_p2[i];
				} else {
					cityID_p1[i] = backup_cityID_p2[i];
					cityID_p2[i] = backup_cityID_p1[i];
				}
			}
		}//end outer for

		SolutionRepresentation solRep = new SolutionRepresentation(oRandom.nextBoolean()? cityID_p1 : cityID_p2);
		c = new PWPSolution(solRep,oObjectiveFunction.getObjectiveFunctionValue(solRep));

		return c.getObjectiveFunctionValue();
	}

	@Override
	public boolean isCrossover() {
		return true;
	}

	@Override
	public boolean usesIntensityOfMutation() {
		return true;
	}

	@Override
	public boolean usesDepthOfSearch() {
		return false;
	}


	@Override
	public void setObjectiveFunction(ObjectiveFunctionInterface oObjectiveFunction) {
		
		this.oObjectiveFunction = oObjectiveFunction;
	}

	private HashMap<Integer,int[]> createHash(int[] p1,int[] p2){
		HashMap<Integer,int[]> hash = new HashMap<>();
		int length = p1.length;
		int[] p2pair;
		int valueIndex = 0, flagIndex = 1;

		for(int i=0; i<length; i++){
			p2pair = new int[2];
			p2pair[valueIndex] = p2[i];
			p2pair[flagIndex] = 0;
			hash.put(p1[i],p2pair);
		}

		return hash;
	}
}
