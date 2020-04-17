package com.aim.project.pwp;

import com.aim.project.pwp.instance.Location;
import com.aim.project.pwp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.pwp.interfaces.PWPInstanceInterface;
import com.aim.project.pwp.interfaces.SolutionRepresentationInterface;

public class PWPObjectiveFunction implements ObjectiveFunctionInterface {
	
	private final PWPInstanceInterface oInstance;
	
	public PWPObjectiveFunction(PWPInstanceInterface oInstance) {
		
		this.oInstance = oInstance;
	}

	@Override
	public double getObjectiveFunctionValue(SolutionRepresentationInterface oSolution) {
		int[] permutation = oSolution.getSolutionRepresentation();
		double totalCost = 0;

		totalCost = getCostBetweenDepotAnd(0) + getCostBetweenHomeAnd(permutation.length-1);
		// sum up all the costs between the depot addresses
		for(int i = 0; i<permutation.length-1; i++){
			totalCost += getCost(i,i+1) ;
		}

		return totalCost;
	}
	
	@Override
	public double getCost(int iLocationA, int iLocationB) {
		Location aLoc = oInstance.getLocationForDelivery(iLocationA);
		Location bLoc = oInstance.getLocationForDelivery(iLocationB);
		return getEuclideanDistance(aLoc.getX()-bLoc.getX(),aLoc.getY()-bLoc.getY());
	}

	@Override
	public double getCostBetweenDepotAnd(int iLocation) {
		Location depotLoc = oInstance.getPostalDepot();
		Location targetLoc = oInstance.getLocationForDelivery(iLocation);

		return getEuclideanDistance(depotLoc.getX()-targetLoc.getX(),depotLoc.getY()-targetLoc.getY());
	}

	@Override
	public double getCostBetweenHomeAnd(int iLocation) {
		Location homeLoc = oInstance.getHomeAddress();
		Location targetLoc = oInstance.getLocationForDelivery(iLocation);

		return getEuclideanDistance(homeLoc.getX()-targetLoc.getX(),homeLoc.getY()-targetLoc.getY());
		
	}

	private double getEuclideanDistance(double x, double y){
		// sqrt (x^2 + y^2)
		return Math.sqrt( Math.pow(x,2) + Math.pow(y,2));
	}
}
