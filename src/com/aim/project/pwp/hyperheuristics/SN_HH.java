package com.aim.project.pwp.hyperheuristics;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;
import com.aim.project.pwp.AIM_PWP;
import com.aim.project.pwp.SolutionPrinter;
import com.aim.project.pwp.Utilities;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;

import java.util.ArrayList;
import java.util.Collections;

public class SN_HH extends HyperHeuristic {

    private int NUM_POPULATION = 5;

    public SN_HH(long lSeed) {

        super(lSeed);
    }
    @Override
    protected void solve(ProblemDomain oProblem) {

        int tabuLength = 2, tourSize = 3;

        int h,p1,p2;
        double iom = 0.2, dos = 0.2;
        boolean iomFlag = false, dosFlag = false;
        long iteration = 0;
        double current,candidate,currentP1,currentP2;


        oProblem.setMemorySize(NUM_POPULATION*2);

        for(int i = 0; i<NUM_POPULATION; i++){
            oProblem.initialiseSolution(i);
        }

        oProblem.setIntensityOfMutation(iom);
        oProblem.setDepthOfSearch(dos);

        //
        while ( !hasTimeExpired() && oProblem.getBestSolutionValue()!= 35) {

            h = chooseHeuristic(oProblem,iteration,tabuLength);

            if (h < 5) {

                for(int i = 0; i<NUM_POPULATION; i++){

                    candidate = oProblem.applyHeuristic(h, i, NUM_POPULATION+i);
                    current = oProblem.getFunctionValue(i);

                    if (candidate <= current) {
                        oProblem.copySolution(NUM_POPULATION + i, i);
                    }
                }//end generation

            } else {
            //check local search's accepting mechanism

                for(int i = 0; i<NUM_POPULATION; i++){

                    p1 = tournamentSelection(tourSize,oProblem);
                    p2 = p1;
                    while(p2==p1) {
                        p2 = tournamentSelection(tourSize, oProblem);
                    }
//                    p1 =0; p2 = 1;

                    candidate = oProblem.applyHeuristic(h, p1, p2, NUM_POPULATION+i);

                    currentP1 = oProblem.getFunctionValue(p1);
                    currentP2 = oProblem.getFunctionValue(p2);
                    //if child outperforms both parents
                    if(candidate <= currentP1 && candidate <= currentP2){
                        if(currentP1 < currentP2){ //p1 is better than p2
                            oProblem.copySolution(NUM_POPULATION+i,p2);
                        }else{
                            oProblem.copySolution(NUM_POPULATION+i,p1);
                        }
                    }//end replacing
                }//end for generation
            }//end else

//            replacePopulation(oProblem);
            iteration++;

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

    private int chooseHeuristic(ProblemDomain oProblem, long iteration,int tabuLength){
        int N=oProblem.getNumberOfHeuristics();
        int h = rng.nextInt(N);
        while(isTabu(oProblem.getHeuristicCallRecord(),iteration,h,tabuLength)){
            h = rng.nextInt(N);
        }
        oProblem.getHeuristicCallRecord()[(int)iteration%N] = h;
        return h;
    }

    private boolean isTabu(int[]record, long iteration, int choice, int tabuLength) {
        if(iteration<tabuLength){
            tabuLength = (int)iteration;
        }

        for (int i = 1; i <= tabuLength; i++) {
            if (record[((int) iteration - i) % record.length] == choice) {
                return true;
            }
        }
        return false;
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

    private void replacePopulation(ProblemDomain oProblem){
        for(int i=0; i<NUM_POPULATION; i++){
            oProblem.copySolution(NUM_POPULATION+i,i);
        }
    }
}
