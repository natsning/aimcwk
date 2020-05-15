package com.aim.project.pwp.hyperheuristics;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;
import com.aim.project.pwp.AIM_PWP;
import com.aim.project.pwp.SolutionPrinter;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;
import com.aim.project.pwp.interfaces.Visualisable;


public class SN_HH extends HyperHeuristic {



    public SN_HH(long lSeed) {

        super(lSeed);
    }
    @Override
    protected void solve(ProblemDomain oProblem) {

        int[] aoCrossover = oProblem.getHeuristicsOfType(ProblemDomain.HeuristicType.CROSSOVER);
        int[] aoMutation = oProblem.getHeuristicsThatUseIntensityOfMutation();
        long iteration = 0;
        double current,candidate,delta;
        int h;
        int instanceSize = ((Visualisable)oProblem).getLoadedInstance().getNumberOfLocations();

        oProblem.setMemorySize(3);
        oProblem.initialiseSolution(0);
        oProblem.initialiseSolution(1);

        double initialObjVal = oProblem.getBestSolutionValue();
        LateAcceptance lAccept = new LateAcceptance(10,initialObjVal*1.125,rng);
        HeuristicScore hScore = new HeuristicScore(aoMutation.length,initialObjVal*1.2,aoCrossover);

        if(instanceSize<200){
            oProblem.setIntensityOfMutation(0.2);
            oProblem.setDepthOfSearch(0.5);
        }else{
            oProblem.setIntensityOfMutation(0.1);
            oProblem.setDepthOfSearch(1.0);
        }

        while ( !hasTimeExpired()) {

            h = rouletteWheel(aoMutation,hScore);

            if (h < 3) {

                candidate = oProblem.applyHeuristic(aoMutation[h], 0,2 );


            } else {

                candidate = oProblem.applyHeuristic(aoMutation[h], 0, 1, 2);

            }


            oProblem.applyHeuristic(oProblem.getHeuristicsThatUseDepthOfSearch()[rng.nextInt(2)],2,2);

            current = oProblem.getFunctionValue(0);
            if(candidate<= lAccept.getAverage()){

                oProblem.copySolution(2,0);
                lAccept.updateLateAcceptance(candidate);

                if(candidate<current){
                    hScore.increaseScore(h,current-candidate);
                }else{
                    hScore.updateTimeScore(h);
                }

            }else{
                delta = candidate-current;
                if(delta <= 0){
                    delta = initialObjVal*0.1;
                    oProblem.copySolution(2,1);
                }
                hScore.decreaseScore(h,delta);
            }

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

    private int rouletteWheel(int[] hSet, HeuristicScore heuristicScore){

        Double r = rng.nextDouble() * heuristicScore.getSum();
        Double cumulative = 0.0;
        for(int i =0; i<hSet.length; i++){
            cumulative+= heuristicScore.getOverallHScore(i);
            if(r<cumulative) {
                return i;
            }
        }

        return -1;
    }


}
