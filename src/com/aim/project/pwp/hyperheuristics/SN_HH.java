package com.aim.project.pwp.hyperheuristics;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;
import com.aim.project.pwp.AIM_PWP;
import com.aim.project.pwp.SolutionPrinter;
import com.aim.project.pwp.Utilities;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SN_HH extends HyperHeuristic {

    private int NUM_POPULATION = 5;

    public SN_HH(long lSeed) {

        super(lSeed);
    }
    @Override
    protected void solve(ProblemDomain oProblem) {

        int tabuLength = 3, tourSize = 3;
        int h,prevH=0,rmvH,p1,p2,initialWaterLevel = 20;
        int waterLevel=initialWaterLevel;
        int N = oProblem.getNumberOfHeuristics();

        HashSet<Integer> tabuSet = new HashSet<>();
        HeuristicScore hScore = new HeuristicScore(N);

        long iteration = 0;
        double iom = 0.2, dos = 0.2;
        double current,candidate,currentP1,currentP2;
        double initialTemp = 0.1, temp = initialTemp;


        oProblem.setMemorySize(NUM_POPULATION*2);

        for(int i = 0; i<NUM_POPULATION; i++){
            oProblem.initialiseSolution(i);
        }

        oProblem.setIntensityOfMutation(iom);
        oProblem.setDepthOfSearch(dos);

        //
        while ( !hasTimeExpired()) {

            if(iteration==0){
                prevH = -1;
                rmvH = -1;
            }else if(iteration<=tabuLength){
                prevH = oProblem.getHeuristicCallRecord()[(int)(iteration-1)%N];
                rmvH = -1;
            }else{
                prevH = oProblem.getHeuristicCallRecord()[(int)(iteration-1)%N];
                rmvH = oProblem.getHeuristicCallRecord()[(int)(iteration-tabuLength)%N];
            }
            h = chooseHeuristic(oProblem,prevH,rmvH,tabuSet,hScore);

            if (h < 5) {

                for(int i = 0; i<NUM_POPULATION; i++){

                    candidate = oProblem.applyHeuristic(h, i, NUM_POPULATION+i);
                    current = oProblem.getFunctionValue(i);

                    if (candidate <= current || rng.nextDouble() <= boltzmannProb(candidate-current,temp)) {
                        oProblem.copySolution(NUM_POPULATION + i, i);
                        if(candidate<current){
                            hScore.increaseScore(prevH,h);
                        }
                    }else{
                        waterLevel--;
                        hScore.decreaseScore(prevH,h);
                    }
                }//end generation

            } else {
            //TODO check local search's accepting mechanism

                for(int i = 0; i<NUM_POPULATION; i++){

                    p1 = tournamentSelection(tourSize,oProblem);
                    p2 = p1;
                    while(p2==p1) {
                        p2 = tournamentSelection(tourSize, oProblem);
                    }

                    candidate = oProblem.applyHeuristic(h, p1, p2, NUM_POPULATION+i);

                    currentP1 = oProblem.getFunctionValue(p1);
                    currentP2 = oProblem.getFunctionValue(p2);
                    current = currentP1<currentP2? currentP1: currentP2;
                    //if child outperforms both parents
                    if(candidate <= current || rng.nextDouble() <= boltzmannProb(candidate-current,temp)){
                        if(currentP1 < currentP2){ //p1 is better than p2
                            oProblem.copySolution(NUM_POPULATION+i,p2);
                        }else{
                            oProblem.copySolution(NUM_POPULATION+i,p1);
                        }
                        if(candidate<current){
                            hScore.increaseScore(prevH,h);
                        }
                    }else{
                        waterLevel--;
                        hScore.decreaseScore(prevH,h);
                    }//end replacing

                }//end for generation
            }//end else

            iteration++;

            if(waterLevel <0){
                temp = initialTemp;
                initialWaterLevel += 10;
                waterLevel = initialWaterLevel;
            }else{
                temp *= 0.95;
            }


        }//end while

        SolutionPrinter oSP = new SolutionPrinter("out.csv");
        PWPSolutionInterface oSolution = ((AIM_PWP) oProblem).getBestSolution();
        oSP.printSolution(((AIM_PWP) oProblem).oInstance.getSolutionAsListOfLocations(oSolution));
        System.out.println(String.format("Total iterations = %d", iteration));
    }

    @Override
    public String toString() {

        return "SN_HH";
    }

    private int chooseHeuristic(ProblemDomain oProblem,int prevH,int heuristicToRemoveFromTabu,HashSet<Integer> tabu, HeuristicScore hs){
        //TODO see if i can refactor this better

        int N=oProblem.getNumberOfHeuristics();
        int choice = 0;
        double bestScore=Integer.MIN_VALUE;

        for(int heuristic=0; heuristic<N; heuristic++) {
            if(!(tabu.contains(heuristic)) && hs.getOverallHScore(prevH,heuristic) > bestScore) {
                bestScore = hs.getOverallHScore(prevH,heuristic);
                choice = heuristic;
            }
        }

        tabu.add(choice);
        if(heuristicToRemoveFromTabu!=-1){
            tabu.remove(heuristicToRemoveFromTabu);
        }

        return choice;

    }


    private int tournamentSelection(int tourSize, ProblemDomain oProb){
        //shuffles the index of each solution
        int[] perm = new int[NUM_POPULATION];
        for (int i = 0; i < NUM_POPULATION; i++){
            perm[i] = i;
        }
        Utilities.shuffle(perm,rng);

        //get fitness of each selected opponents,
        // index of fitness array is corresponds to the shuffled sequence of perm
        ArrayList<Double> fitness_array = new ArrayList<>(tourSize);
        for (int i = 0; i < tourSize; i++){
            fitness_array.add(oProb.getFunctionValue(perm[i]));
        }
        //return the index of solution
        return perm[fitness_array.indexOf(Collections.min(fitness_array))];
    }

    private double boltzmannProb(double delta, double temp){
        double n = Math.exp( -1* delta / temp);
//        System.out.println(n);
        return n;
    }

}
