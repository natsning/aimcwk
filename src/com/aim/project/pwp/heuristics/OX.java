package com.aim.project.pwp.heuristics;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import com.aim.project.pwp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;
import com.aim.project.pwp.interfaces.XOHeuristicInterface;
import com.aim.project.pwp.solution.PWPSolution;
import com.aim.project.pwp.solution.SolutionRepresentation;


public class OX implements XOHeuristicInterface {
	
	private final Random oRandom;
	
	private ObjectiveFunctionInterface oObjectiveFunction;

	public OX(Random oRandom) {
		
		this.oRandom = oRandom;
	}

	@Override
	public double apply(PWPSolutionInterface oSolution, double dDepthOfSearch, double dIntensityOfMutation) {
		return oSolution.getObjectiveFunctionValue();
	}

	@Override
	public double apply(PWPSolutionInterface p1, PWPSolutionInterface p2,
			PWPSolutionInterface c, double depthOfSearch, double intensityOfMutation) {

		int length = p1.getNumberOfLocations()-2;
		int times = (int)Math.floor(intensityOfMutation / 0.2) + 1;

		int[] cityID_p1 = new int[length],cityID_p2 = new int[length];
		int i,j,indexP1,indexP2;
		System.arraycopy(p2.getSolutionRepresentation().getSolutionRepresentation(),0,cityID_p2,0,length);
		System.arraycopy(p1.getSolutionRepresentation().getSolutionRepresentation(),0,cityID_p1,0,length);

		int[] backup_cityID_p1 = new int[length], backup_cityID_p2 = new int[length];
		Set<Integer> setP1 = new HashSet<>(), setP2 = new HashSet<>();

		for(int loop =0; loop<times; loop++){

			setP1.clear();
			setP2.clear();
			System.arraycopy(cityID_p1,0,backup_cityID_p1,0,length);
			System.arraycopy(cityID_p2,0,backup_cityID_p2,0,length);
			i = oRandom.nextInt(length);
			j = i;
			while(j == i || Math.abs(j-i)==length-1){
				//ensures i and j are two different indexes and not head and tail
				j = oRandom.nextInt(length);
			}
			if (i > j) { //i will always be smaller than j
				int temp = i;
				i = j;
				j = temp;
			}
			indexP1 = j+1; indexP2 = j+1;
			for(int k=i+1; k<=j; k++ ){
				setP1.add(cityID_p1[k]);
				setP2.add(cityID_p2[k]);
			}
			for(int index = j+1; index <= length+j; index++) {

				if (!setP2.contains(backup_cityID_p1[index % length])) {
					cityID_p2[indexP2 % length] = backup_cityID_p1[index % length];
					indexP2++;
				}
				if (!setP1.contains(backup_cityID_p2[index % length])) {
					cityID_p1[indexP1 % length] = backup_cityID_p2[index % length];
					indexP1++;
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
	public void setObjectiveFunction(ObjectiveFunctionInterface f) {
		
		this.oObjectiveFunction = f;
	}
}
