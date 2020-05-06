package com.aim.project.pwp.hyperheuristics;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;
import com.aim.project.pwp.AIM_PWP;
import com.aim.project.pwp.SolutionPrinter;
import com.aim.project.pwp.Utilities;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class SN_HH_LSR extends HyperHeuristic {

    private int NUM_POPULATION = 1;

    public SN_HH_LSR(long lSeed) {

        super(lSeed);
    }
    @Override
    protected void solve(ProblemDomain oProblem) {

        int N = oProblem.getNumberOfHeuristics();
        int tourSize = 2;
        int h,prevH,rmvH,p1,p2,initialWaterLevel = 10;
        int waterLevel=initialWaterLevel;
        boolean acceptOnce = false;

        int[] mutationSet = oProblem.getHeuristicsOfType(ProblemDomain.HeuristicType.MUTATION);
        int[] localSearchSet = oProblem.getHeuristicsOfType(ProblemDomain.HeuristicType.LOCAL_SEARCH);
        int[] crossoverSet = oProblem.getHeuristicsOfType(ProblemDomain.HeuristicType.CROSSOVER);

        long iteration = 0;
        double initialIOM = 0.2,  iom = initialIOM;
        double crossIOM = 1.0;
        double initialDOS = 0.5, dos = initialDOS;
        double initialTemp = 0.1, temp = initialTemp;
        double alpha = 0.01, beta = 0.9;
        double current,candidate,currentP1,currentP2;


        oProblem.setMemorySize(3);

        for(int i = 0; i<2; i++){
            oProblem.initialiseSolution(i);
        }

        Double initialObjVal = oProblem.getBestSolutionValue();
        LateAcceptance lAccept = new LateAcceptance(10,initialObjVal*1.1,rng);
        HeuristicScore mutationScore = new HeuristicScore(mutationSet.length,initialObjVal*1.2);
        HeuristicScore ilsScore = new HeuristicScore(localSearchSet.length,initialObjVal*1.2);
        HeuristicScore crossoverScore = new HeuristicScore(crossoverSet.length,initialObjVal*1.2);

        oProblem.setIntensityOfMutation(iom);
        oProblem.setDepthOfSearch(dos);

        while ( !hasTimeExpired()) {

            h = chooseHeuristic(mutationSet,mutationScore);
            oProblem.getHeuristicCallRecord()[(int)(iteration)%N] = h;


            candidate = oProblem.applyHeuristic(h, 0,2);
            current = oProblem.getFunctionValue(0);

            if (candidate <= lAccept.getAverage()){

                oProblem.copySolution(2, 0);
                acceptOnce = true;
                if(candidate<current){
                    mutationScore.increaseScore(h,BigDecimal.valueOf(current-candidate));
                }else{
                    mutationScore.updateTimeScore(h);
                }

            }else{
                mutationScore.decreaseScore(h,BigDecimal.valueOf(candidate-current));
            }


            iteration++;

            if (acceptOnce&&rng.nextBoolean()){
                acceptOnce = false;
            }else{

                prevH = chooseHeuristic(crossoverSet,crossoverScore);
                candidate = oProblem.applyHeuristic(crossoverSet[prevH], 0, 1, 2);
                h = chooseHeuristic(localSearchSet,ilsScore);
                candidate = oProblem.applyHeuristic(localSearchSet[h],0,2);
                currentP1 = oProblem.getFunctionValue(0);
                currentP2 = oProblem.getFunctionValue(1);
                current = currentP1<currentP2? currentP1: currentP2;

                if(candidate<= lAccept.getAverage()){

                    lAccept.updateLateAcceptance(candidate);
                    if (currentP1 < currentP2) { //p1 is better than p2
                        oProblem.copySolution(2, 0);
                    } else {
                        oProblem.copySolution(2, 1);
                    }

                    if(candidate<current){
//                            System.out.printf("%d improved to %f (%f)\n",h,candidate,current);
                        crossoverScore.increaseScore(prevH,BigDecimal.valueOf(current-candidate));
                        ilsScore.increaseScore(h,BigDecimal.valueOf(current-candidate));
//                  acceptOnce = true;
                    }else{
//                            System.out.printf("%d better than avg %f\n",h,candidate);
                        crossoverScore.updateTimeScore(prevH);
                        ilsScore.updateTimeScore(h);
                    }
                    acceptOnce = true;
                }else{
//                        System.out.printf("%d worsen to %f\n",h,candidate);
                    crossoverScore.decreaseScore(prevH,BigDecimal.valueOf(candidate-current));
                    ilsScore.decreaseScore(h,BigDecimal.valueOf(candidate-current));
                }

            }

            h = chooseHeuristic(localSearchSet,ilsScore);
            candidate = oProblem.applyHeuristic(localSearchSet[h],0,2);
            current = oProblem.getFunctionValue(0);
            if (candidate <= lAccept.getAverage()){

                oProblem.copySolution(2, 0);
                acceptOnce = true;
                if(candidate<current){
                    ilsScore.increaseScore(h,BigDecimal.valueOf(current-candidate));
                }else{
                    ilsScore.updateTimeScore(h);
                }

            }else{
                ilsScore.decreaseScore(h,BigDecimal.valueOf(candidate-current));
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

    private int chooseHeuristic(int[] heuristicSet, HeuristicScore hs){

        int num_heuristics=heuristicSet.length;
        int choice = 0;
        BigDecimal currentHScore = BigDecimal.valueOf(Double.MIN_VALUE);
        BigDecimal bestScore = currentHScore;
        ArrayList<Integer> aloheuristic = new ArrayList<>();
        aloheuristic.add(0);

        for(int heuristic=0; heuristic<num_heuristics; heuristic++) {

            currentHScore = hs.getOverallHScore(heuristic);

            if( currentHScore.compareTo(bestScore) == 1){
                bestScore = hs.getOverallHScore(heuristic);
                aloheuristic.clear();
                aloheuristic.add(heuristic);
                choice=heuristic;
            }else if(currentHScore.compareTo(bestScore) == 0){
                aloheuristic.add(heuristic);
            }

        }
        if(aloheuristic.size()!=1){
            choice = aloheuristic.get(rng.nextInt(aloheuristic.size()));
        }

        return choice;
    }


//    private void replaceWorst(ProblemDomain oProb,int N, int c){
//        int worstIndex = 0;
//        double worstValue = Double.MIN_VALUE;
//        double value;
//        for(int i = 0; i<NUM_POPULATION; i++){
//            value = oProb.getFunctionValue(i);
//            if(value>worstValue){
//                worstIndex = i;
//            }
//        }
//        oProb.copySolution(c,worstIndex);
//    }
//    private double boltzmannProb(double delta, double temp){
//        if (temp<0.00000001){
//            temp = 0;
//        }
//        double n = Math.exp( -1* delta / temp);
////        System.out.printf("delta%f, temp%.5f, prob%.5f\n",delta,temp,n);
//        return n;
////        return 0;
//    }

//    private double coolTemperature(double temp,double alpha){
//        return (temp/(1+temp*alpha));
////        return (temp*alpha);
//    }

}
