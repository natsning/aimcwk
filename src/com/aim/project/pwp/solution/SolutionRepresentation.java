package com.aim.project.pwp.solution;

import com.aim.project.pwp.interfaces.SolutionRepresentationInterface;

/**
 * 
 * @author Warren G. Jackson
 * 
 *
 */
public class SolutionRepresentation implements SolutionRepresentationInterface {

	private int[] aiSolutionRepresentation;

	public SolutionRepresentation(int[] aiRepresentation) {

		this.aiSolutionRepresentation = aiRepresentation;
	}

	@Override
	public int[] getSolutionRepresentation() {

		return aiSolutionRepresentation;
	}

	@Override
	public void setSolutionRepresentation(int[] aiSolutionRepresentation) {

		this.aiSolutionRepresentation = aiSolutionRepresentation;
	}

	@Override
	public int getNumberOfLocations() {
		//return the total number of locations in this instance (includes DEPOT and HOME).
		return this.aiSolutionRepresentation.length+2;
	}

	@Override
	public SolutionRepresentationInterface clone() {
		// perform a DEEP clone of the solution representation!
		int[] aoClone = new int[aiSolutionRepresentation.length];
		System.arraycopy(aiSolutionRepresentation, 0, aoClone, 0, aiSolutionRepresentation.length);

		return new SolutionRepresentation(aoClone);
	}

}
