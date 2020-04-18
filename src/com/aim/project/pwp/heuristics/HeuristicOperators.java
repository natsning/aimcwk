package com.aim.project.pwp.heuristics;

import com.aim.project.pwp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.pwp.interfaces.PWPInstanceInterface;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;

import java.util.Random;

public class HeuristicOperators {

	private ObjectiveFunctionInterface oObjectiveFunction;

	public HeuristicOperators() {

	}

	public void setObjectiveFunction(ObjectiveFunctionInterface f) {

		this.oObjectiveFunction = f;
	}

	public void swap(int[] array, int i,int j){

		int temp = array[i];
		array[i] = array[j];
		array[j] = temp;

	}

	public int[] chooseTwo(int range, Random oRandom){
		int i = oRandom.nextInt(range);
		int j = i;
		while(j == i){ //ensures i and j are two different indexes
			j = oRandom.nextInt(range);
		}
		int[] pair = {i,j};
		return pair;
	}


}
