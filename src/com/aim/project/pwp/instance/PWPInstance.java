package com.aim.project.pwp.instance;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.stream.IntStream;

import com.aim.project.pwp.PWPObjectiveFunction;
import com.aim.project.pwp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.pwp.interfaces.PWPInstanceInterface;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;
import com.aim.project.pwp.solution.SolutionRepresentation;
import com.aim.project.pwp.solution.PWPSolution;


public class PWPInstance implements PWPInstanceInterface {
	
	private final Location[] aoLocations;
	
	private final Location oPostalDepotLocation;
	
	private final Location oHomeAddressLocation;
	
	private final int iNumberOfLocations;
	
	private final Random oRandom;
	
	private ObjectiveFunctionInterface oObjectiveFunction = null;
	
	/**
	 * 
	 * @param numberOfLocations The TOTAL number of locations (including DEPOT and HOME).
	 * @param aoLocations The delivery locations.
	 * @param oPostalDepotLocation The DEPOT location.
	 * @param oHomeAddressLocation The HOME location.
	 * @param random The random number generator to use.
	 */
	public PWPInstance(int numberOfLocations, Location[] aoLocations, Location oPostalDepotLocation, Location oHomeAddressLocation, Random random) {
		
		this.iNumberOfLocations = numberOfLocations;
		this.oRandom = random;
		this.aoLocations = aoLocations;
		this.oPostalDepotLocation = oPostalDepotLocation;
		this.oHomeAddressLocation = oHomeAddressLocation;
	}

	@Override
	public PWPSolution createSolution(InitialisationMode mode) {
		
		// construct a new 'PWPSolution' using RANDOM initialisation

		if(mode == InitialisationMode.RANDOM) {
			int[] city_ids = new int[getNumberOfLocations() - 2];
			for (int i = 0; i < getNumberOfLocations() - 2; i++) {
				city_ids[i] = i;
			}
			//random swap
			for (int i = 0; i < city_ids.length; i++) {
				int randomIndexToSwap = oRandom.nextInt(city_ids.length);
				int temp = city_ids[randomIndexToSwap];
				city_ids[randomIndexToSwap] = city_ids[i];
				city_ids[i] = temp;
			}

			SolutionRepresentation solRep = new SolutionRepresentation(city_ids);
			return new PWPSolution(solRep, getPWPObjectiveFunction().getObjectiveFunctionValue(solRep));
		}

		return null;
	}
	
	@Override
	public ObjectiveFunctionInterface getPWPObjectiveFunction() {
		
		if(oObjectiveFunction == null) {
			this.oObjectiveFunction = new PWPObjectiveFunction(this);
		}

		return oObjectiveFunction;
	}

	@Override
	public int getNumberOfLocations() {

		return iNumberOfLocations;
	}

	@Override
	public Location getLocationForDelivery(int deliveryId) {

		return aoLocations[deliveryId];
	}

	@Override
	public Location getPostalDepot() {
		
		return this.oPostalDepotLocation;
	}

	@Override
	public Location getHomeAddress() {
		
		return this.oHomeAddressLocation;
	}
	
	@Override
	public ArrayList<Location> getSolutionAsListOfLocations(PWPSolutionInterface oSolution) {

		//return an 'ArrayList' of ALL LOCATIONS in the solution.
		int[] city_ids = oSolution.getSolutionRepresentation().getSolutionRepresentation();
		ArrayList<Location> aloLoc = new ArrayList<>(city_ids.length);
		for (int i = 0; i < city_ids.length; i++){
			aloLoc.add(aoLocations[city_ids[i]]);
		}

		return aloLoc;
	}

}
