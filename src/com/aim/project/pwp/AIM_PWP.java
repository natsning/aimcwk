package com.aim.project.pwp;


import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import com.aim.project.pwp.heuristics.*;
import com.aim.project.pwp.instance.InitialisationMode;
import com.aim.project.pwp.instance.Location;
import com.aim.project.pwp.instance.reader.PWPInstanceReader;
import com.aim.project.pwp.interfaces.*;

import AbstractClasses.ProblemDomain;

public class AIM_PWP extends ProblemDomain implements Visualisable {

	private String[] instanceFiles = {
		"square", "libraries-15", "carparks-40", "tramstops-85", "trafficsignals-446", "streetlights-35714"
	};
	
	private PWPSolutionInterface[] aoMemoryOfSolutions;
	
	public PWPSolutionInterface oBestSolution = null;
	
	public PWPInstanceInterface oInstance;
	
	private HeuristicInterface[] aoHeuristics;
	
	private ObjectiveFunctionInterface oObjectiveFunction;
	
//	private final long seed;
	private long seed;
		
	public AIM_PWP(long seed) {
		
		super(seed);

		// set default memory size and create the array of low-level heuristics
		aoMemoryOfSolutions = new PWPSolutionInterface[2];
		aoHeuristics = new HeuristicInterface[getNumberOfHeuristics()];

		aoHeuristics[0] = new InversionMutation(rng);
		aoHeuristics[1] = new AdjacentSwap(rng);
		aoHeuristics[2] = new Reinsertion(rng);
		aoHeuristics[3] = new NextDescent(rng);
		aoHeuristics[4] = new DavissHillClimbing(rng);
		aoHeuristics[5] = new OX(rng);
		aoHeuristics[6] = new CX(rng);
		
	}
	
	public PWPSolutionInterface getSolution(int index) {
		
		return this.aoMemoryOfSolutions[index];
	}
	
	public PWPSolutionInterface getBestSolution() {
		
		return this.oBestSolution;
	}

	@Override
	public double applyHeuristic(int hIndex, int currentIndex, int candidateIndex) {
		
		// apply heuristic and return the objective value of the candidate solution
		//			remembering to keep track/update the best solution
		copySolution(currentIndex,candidateIndex);

		if(!aoHeuristics[hIndex].isCrossover()) { //if it's not crossover

			double newObjVal = aoHeuristics[hIndex].apply(getSolution(candidateIndex), depthOfSearch, intensityOfMutation);
			if (newObjVal < getBestSolutionValue()) {
				updateBestSolution(candidateIndex);
			}
		}

		return getSolution(candidateIndex).getObjectiveFunctionValue();
	}

	@Override
	public double applyHeuristic(int hIndex, int parent1Index, int parent2Index, int candidateIndex) {
		
		// apply heuristic and return the objective value of the candidate solution
		//			remembering to keep track/update the best solution
		copySolution(parent1Index,candidateIndex);

		if( (aoHeuristics[hIndex].isCrossover()) ) { //if it is crossover
			XOHeuristicInterface heuristic = (XOHeuristicInterface) aoHeuristics[hIndex];
			double newObjVal = heuristic.apply(getSolution(parent1Index), getSolution(parent2Index),getSolution(candidateIndex),
												depthOfSearch, intensityOfMutation);
			if (newObjVal < getBestSolutionValue()) {
				updateBestSolution(candidateIndex);
			}
		}
		return getSolution(candidateIndex).getObjectiveFunctionValue();
	}

	@Override
	public boolean compareSolutions(int iIndexA, int iIndexB) {

		//return true if the objective values of the two solutions are the same, else false
		return getSolution(iIndexA).getObjectiveFunctionValue()==getSolution(iIndexB).getObjectiveFunctionValue();
	}

	@Override
	public void copySolution(int iIndexSource, int iIndexDestination) {

		// BEWARE this should copy the solution, not the reference to it!
		//			That is, that if we apply a heuristic to the solution in index 'b',
		//			then it does not modify the solution in index 'a' or vice-versa.
		aoMemoryOfSolutions[iIndexDestination] = getSolution(iIndexSource).clone();
	}

	@Override
	public double getBestSolutionValue() {

		return oBestSolution.getObjectiveFunctionValue();

	}
	
	@Override
	public double getFunctionValue(int index) {
		
		return aoMemoryOfSolutions[index].getObjectiveFunctionValue();
	}

	@Override
	public int[] getHeuristicsOfType(HeuristicType type) {
		
		// TODO return an array of heuristic IDs based on the heuristic's type.
		if(type == HeuristicType.MUTATION){
			return getHeuristicsThatUseIntensityOfMutation();
		}
		else if(type == HeuristicType.LOCAL_SEARCH){
			return getHeuristicsThatUseDepthOfSearch();
		}
		else if(type == HeuristicType.CROSSOVER){
			return getHeuristicsThatAreCrossovers();
		}
		else if(type == HeuristicType.RUIN_RECREATE){

			return new int[]{};
		}else{
			return new int[]{};
		}
		
	}

	@Override
	public int[] getHeuristicsThatUseDepthOfSearch() {

		// return the array of heuristic IDs that use depth of search.
		ArrayList<Integer> arrayList = new ArrayList<>();
		for(int i=0; i<aoHeuristics.length; i++){
			if(aoHeuristics[i].usesDepthOfSearch()){
				arrayList.add(i);
			}
		}
		return Utilities.convertArrayListToArray(arrayList);
	}

	@Override
	public int[] getHeuristicsThatUseIntensityOfMutation() {
		
		// return the array of heuristic IDs that use intensity of mutation.
		ArrayList<Integer> arrayList = new ArrayList<>();
		for(int i=0; i<aoHeuristics.length; i++){
			if(aoHeuristics[i].usesIntensityOfMutation()){
				arrayList.add(i);
			}
		}
		return Utilities.convertArrayListToArray(arrayList);
	}

	public int[] getHeuristicsThatAreCrossovers(){
		ArrayList<Integer> arrayList = new ArrayList<>();
		for(int i=0; i<aoHeuristics.length; i++){
			if(aoHeuristics[i].isCrossover()){
				arrayList.add(i);
			}
		}
		return Utilities.convertArrayListToArray(arrayList);
	}

	@Override
	public int getNumberOfHeuristics() {

		//has to be hard-coded due to the design of the HyFlex framework...
		return 7;
	}

	@Override
	public int getNumberOfInstances() {

		// return the number of available instances
		return instanceFiles.length;
	}

	@Override
	public void initialiseSolution(int index) {
		
		//  initialise a solution in index 'index'
		// 		making sure that you also update the best solution!
		aoMemoryOfSolutions[index]= oInstance.createSolution(InitialisationMode.RANDOM);
		if(oBestSolution == null || getSolution(index).getObjectiveFunctionValue() < getBestSolutionValue()){
			updateBestSolution(index);
		}

	}

	// implement the instance reader that this method uses
	//		to correctly read in the PWP instance, and set up the objective function.
	@Override
	public void loadInstance(int instanceId) {

		String SEP = FileSystems.getDefault().getSeparator();
		String instanceName = "instances" + SEP + "pwp" + SEP + instanceFiles[instanceId] + ".pwp";

		Path path = Paths.get(instanceName);
		Random random = new Random(seed);
		PWPInstanceReader oPwpReader = new PWPInstanceReader();
		oInstance = oPwpReader.readPWPInstance(path, random);

		oObjectiveFunction = oInstance.getPWPObjectiveFunction();
		
		for(HeuristicInterface h : aoHeuristics) {
			h.setObjectiveFunction(oObjectiveFunction);
		}
	}

	@Override
	public void setMemorySize(int size) {

		// IF the memory size is INCREASED, then
		//		the existing solutions should be copied to the new memory at the same indices.
		// IF the memory size is DECREASED, then
		//		the first 'size' solutions are copied to the new memory.
		if(size>2){
			PWPSolutionInterface[] newArray = new PWPSolutionInterface[size];
			int limit = aoMemoryOfSolutions.length < size? aoMemoryOfSolutions.length : size;
			for(int i=0; i<limit; i++){
				if(getSolution(i)!=null){
					newArray[i] = getSolution(i).clone();
				}
			}
			aoMemoryOfSolutions = newArray;
		}
	}

	@Override
	public String solutionToString(int index) {

		int[] city_ids = getSolution(index).getSolutionRepresentation().getSolutionRepresentation();
		return cityIDToString(city_ids);
	}

	@Override
	public String bestSolutionToString() {

		// return the location IDs of the best solution including DEPOT and HOME locations
		//		e.g. "DEPOT -> 0 -> 2 -> 1 -> HOME"
		int[] city_ids = oBestSolution.getSolutionRepresentation().getSolutionRepresentation();
		return cityIDToString(city_ids);
	}

	private String cityIDToString(int[] city_ids){

		String[] strings = new String[city_ids.length+2];
		strings[0] = "DEPOT";
		strings[strings.length-1]= "HOME";

		for(int i=1;i<=city_ids.length;i++){
			strings[i] = Integer.toString(city_ids[i-1]);
		}
		return String.join(" -> ",strings);
	}

	@Override
	public String toString() {

		return "hfyst1's G52AIM PWP";
	}
	
	private void updateBestSolution(int index) {
		
		oBestSolution = getSolution(index);
		
	}
	
	@Override
	public PWPInstanceInterface getLoadedInstance() {

		return this.oInstance;
	}

	@Override
	public Location[] getRouteOrderedByLocations() {

		int[] city_ids = getBestSolution().getSolutionRepresentation().getSolutionRepresentation();
		Location[] route = Arrays.stream(city_ids).boxed().map(getLoadedInstance()::getLocationForDelivery).toArray(Location[]::new);
		return route;
	}

}
