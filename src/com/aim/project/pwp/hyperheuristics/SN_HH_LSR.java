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

public class SN_HH_LSR extends HyperHeuristic {

    private int NUM_POPULATION = 5;

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

        HashSet<Integer> tabuSet = new HashSet<>();
        HeuristicScore hScore = new HeuristicScore(N);

        long iteration = 0;
        double initialIOM = 0.2,  iom = initialIOM;
        double crossIOM = 1.0;
        double initialDOS = 0.5, dos = initialDOS;
        double initialTemp = 0.1, temp = initialTemp;
        double alpha = 0.01, beta = 0.9;
        double current,candidate,currentP1,currentP2;


        oProblem.setMemorySize(NUM_POPULATION*2);

        for(int i = 0; i<NUM_POPULATION; i++){
            oProblem.initialiseSolution(i);
        }

        oProblem.setIntensityOfMutation(iom);
        oProblem.setDepthOfSearch(dos);

        while ( !hasTimeExpired()) {

            h = chooseHeuristic(oProblem.getHeuristicsOfType(ProblemDomain.HeuristicType.MUTATION));
            oProblem.getHeuristicCallRecord()[(int)(iteration)%N] = h;

            for(int i = 0; i<NUM_POPULATION; i++) {

                candidate = oProblem.applyHeuristic(h, i, NUM_POPULATION + i);
                current = oProblem.getFunctionValue(i);

                if (candidate <= current || rng.nextDouble() <= boltzmannProb(candidate - current, temp)) {

                    oProblem.copySolution(NUM_POPULATION + i, i);
                    acceptOnce = true;

                }
            }

            iteration++;

            if (acceptOnce && rng.nextDouble()>=crossIOM){
                acceptOnce = false;
            }else{

                oProblem.setIntensityOfMutation(crossIOM);
                for(int i = 0; i<NUM_POPULATION; i++){

                    p1 = tournamentSelection(tourSize,oProblem);
                    p2 = p1;
                    while(p2==p1) {
                        p2 = tournamentSelection(tourSize, oProblem);
                    }

                    h = chooseHeuristic(oProblem.getHeuristicsOfType(ProblemDomain.HeuristicType.CROSSOVER));
                    candidate = oProblem.applyHeuristic(h, p1, p2, NUM_POPULATION+i);
                    h = chooseHeuristic(oProblem.getHeuristicsOfType(ProblemDomain.HeuristicType.LOCAL_SEARCH));
                    candidate = oProblem.applyHeuristic(h,i,NUM_POPULATION+i);
                    currentP1 = oProblem.getFunctionValue(p1);
                    currentP2 = oProblem.getFunctionValue(p2);
                    current = currentP1<currentP2? currentP1: currentP2;

                    if(candidate <= current || rng.nextDouble() <= boltzmannProb(candidate-current,temp)){

                        acceptOnce = true;
                        replaceWorst(oProblem,NUM_POPULATION,NUM_POPULATION+i);
                    }//end replacing
                }//end for generation
                crossIOM*=beta;
                oProblem.setIntensityOfMutation(iom);
            }

            temp = coolTemperature(temp,alpha);
            for(int i=0; i<NUM_POPULATION; i++){
                h = chooseHeuristic(oProblem.getHeuristicsOfType(ProblemDomain.HeuristicType.LOCAL_SEARCH));
                candidate = oProblem.applyHeuristic(h,i,NUM_POPULATION+i);
                current = oProblem.getFunctionValue(i);
                if(candidate>current){
                    oProblem.copySolution(NUM_POPULATION + i, i);
                }
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

    private int chooseHeuristic(int[] heuristicSet){

        int N=heuristicSet.length;
        return heuristicSet[rng.nextInt(N)];

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

    private void replaceWorst(ProblemDomain oProb,int N, int c){
        int worstIndex = 0;
        double worstValue = Double.MIN_VALUE;
        double value;
        for(int i = 0; i<NUM_POPULATION; i++){
            value = oProb.getFunctionValue(i);
            if(value>worstValue){
                worstIndex = i;
            }
        }
        oProb.copySolution(c,worstIndex);
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
