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

public class SN_HH extends HyperHeuristic {

    private int NUM_POPULATION = 1;

    public SN_HH(long lSeed) {

        super(lSeed);
    }
    @Override
    protected void solve(ProblemDomain oProblem) {


        int[] aoHIndex = oProblem.getHeuristicsOfType(ProblemDomain.HeuristicType.OTHER);
        int[] aoMutation = oProblem.getHeuristicsThatUseIntensityOfMutation();
        long iteration = 0;
        double current,candidate,delta;
        int h;

        oProblem.setMemorySize(3);
        oProblem.initialiseSolution(0);
        oProblem.initialiseSolution(1);

        Double initialObjVal = oProblem.getBestSolutionValue();
        LateAcceptance lAccept = new LateAcceptance(10,initialObjVal*1.1,rng);
        HeuristicScore hScore = new HeuristicScore(aoMutation.length,initialObjVal*1.2);

        oProblem.setIntensityOfMutation(0.2);
        oProblem.setDepthOfSearch(0.5);

        double[] aoHScore = new double[aoMutation.length];

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

    private int chooseHeuristic(int num_heuristics,int heuristicToRemoveFromTabu,HashSet<Integer> tabu, HeuristicScore hs){

        int choice = 0;
        double currentHScore = Double.MIN_VALUE;
        double bestScore = currentHScore;
        ArrayList<Integer> aloheuristic = new ArrayList<>();
        aloheuristic.add(0);

        for(int heuristic=0; heuristic<num_heuristics; heuristic++) {

            if(!(tabu.contains(heuristic))) {

                currentHScore = hs.getOverallHScore(heuristic);

                if( currentHScore>bestScore){
                    bestScore = hs.getOverallHScore(heuristic);
                    aloheuristic.clear();
                    aloheuristic.add(heuristic);
                    choice=heuristic;
                }else if(currentHScore==bestScore){
                    aloheuristic.add(heuristic);
                }
            }
        }
        if(aloheuristic.size()!=1){
            choice = aloheuristic.get(rng.nextInt(aloheuristic.size()));
        }
//
//        choice =rng.nextInt(N);
//        while(tabu.contains(choice)){
//            choice = rng.nextInt(N);
//        }
//        for(int i: tabu){
//            System.out.printf("-%d-",i);
//
//        }
        tabu.add(choice);
        if(heuristicToRemoveFromTabu!=-1){
            tabu.remove(heuristicToRemoveFromTabu);
        }
        return choice;

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

        System.out.println(r);
        System.out.println(cumulative);
        return -1;
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
        if (temp<0.00000001){
            temp = 0;
        }
        double n = Math.exp( -1* delta / temp);
//        System.out.printf("delta%f, temp%.5f, prob%.5f\n",delta,temp,n);
        return n;
//        return 0;
    }

    private double coolTemperature(double temp,double alpha){
        return (temp/(1+temp*alpha));
//        return (temp*alpha);
    }

}
