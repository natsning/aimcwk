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

//        int N = oProblem.getNumberOfHeuristics();
        int N = 5;
        int tourSize = 2, tabuLength = 2;
        int h,prevH,rmvH,p1,p2;
        int indexLA=0;
//        int initialWaterLevel = 10, waterLevel=initialWaterLevel;
//        boolean acceptOnce = false;

        HashSet<Integer> tabuSet = new HashSet<>();
        HeuristicScore hScore = new HeuristicScore(N);
        LateAcceptance lAccept = new LateAcceptance(10,5,rng);

        long iteration = 0;
        double initialIOM = 0.2,  iom = initialIOM;
        double initialDOS = 0.5, dos = initialDOS;
//        double initialTemp = 0.1, temp = initialTemp;
//        double alpha = 0.01, beta = 0.8;
        double current,candidate,currentP1,currentP2;


        oProblem.setMemorySize(NUM_POPULATION*2);

        for(int i = 0; i<NUM_POPULATION; i++){
            oProblem.initialiseSolution(i);
        }

        oProblem.setIntensityOfMutation(iom);
        oProblem.setDepthOfSearch(dos);

        while ( !hasTimeExpired()) {

            if(iteration<tabuLength){
                rmvH = -1;
            }else{
                rmvH = oProblem.getHeuristicCallRecord()[(int)(iteration-tabuLength)%N];
            }
            h = chooseHeuristic(N,rmvH,tabuSet,hScore);
            oProblem.getHeuristicCallRecord()[(int)iteration%N] = h;

            if (h < 5) {

                for(int i = 0; i<NUM_POPULATION; i++){

                    candidate = oProblem.applyHeuristic(h, i, NUM_POPULATION+i);
                    current = oProblem.getFunctionValue(i);

                    if(candidate<= lAccept.getAverage()){

                        oProblem.copySolution(NUM_POPULATION + i, i);
                        if(candidate<current){
                            hScore.increaseScore(h,BigDecimal.valueOf(current-candidate));
                            lAccept.updateLateAcceptance(candidate,indexLA++);
        //                        acceptOnce = true;
                        }
                    }else{
                        hScore.decreaseScore(h,BigDecimal.valueOf(candidate-current));
                    }
                }//end generation

            } else {

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

                    //AM
                    if(currentP1 < currentP2){ //p1 is better than p2
                        oProblem.copySolution(NUM_POPULATION+i,p2);
                    }else{
                        oProblem.copySolution(NUM_POPULATION+i,p1);
                    }
                    if(candidate<current){
                        hScore.increaseScore(h,BigDecimal.valueOf(current-candidate));
                        lAccept.updateLateAcceptance(candidate,indexLA++);
//                        acceptOnce = true;
                    }
                    else{
                        hScore.decreaseScore(h,BigDecimal.valueOf(candidate-current));
                    }//end replacing

                }//end for generation
            }//end else

            iteration++;
//
//            if (acceptOnce){
//                acceptOnce = false;
//                waterLevel = initialWaterLevel;
//            }else{
//                waterLevel--;
//            }
//
//            if(waterLevel == 0){
//                initialWaterLevel += 10;
//                waterLevel = initialWaterLevel;
//                hScore.resetScore();
//                for (int i: oProblem.getHeuristicsThatUseIntensityOfMutation()){
//                    hScore.boostScore(i);
//                }

//            }else{
//                temp = coolTemperature(temp,alpha);
//                iom*= beta;
//                dos*= beta;
//                oProblem.setIntensityOfMutation(iom);
//                oProblem.setDepthOfSearch(dos);
//            }

            for(int rec: oProblem.getHeuristicCallRecord()){
                System.out.print(rec);
            }
            System.out.printf("\n");

        }//end while

        SolutionPrinter oSP = new SolutionPrinter("out.csv");
        PWPSolutionInterface oSolution = ((AIM_PWP) oProblem).getBestSolution();
        oSP.printSolution(((AIM_PWP) oProblem).oInstance.getSolutionAsListOfLocations(oSolution));

        System.out.println(String.format("Total iterations = %d", iteration));

        for(int i = 0;i<N; i++){
            System.out.printf("%f ",hScore.getOverallHScore(i));
        }
    }

    @Override
    public String toString() {

        return "SN_HH";
    }

    private int chooseHeuristic(int num_heuristics,int heuristicToRemoveFromTabu,HashSet<Integer> tabu, HeuristicScore hs){

        int choice = 0;
        BigDecimal currentHScore = hs.getOverallHScore(0);;
        BigDecimal bestScore = currentHScore;
        ArrayList<Integer> aloheuristic = new ArrayList<>();
        aloheuristic.add(0);

        for(int heuristic=0; heuristic<num_heuristics; heuristic++) {

            if(!(tabu.contains(heuristic))) {
                currentHScore = hs.getOverallHScore(heuristic);

                if( currentHScore.compareTo(bestScore) == 1){
                    bestScore = hs.getOverallHScore(heuristic);
                    aloheuristic.clear();
                    aloheuristic.add(heuristic);
                    choice=heuristic;
                }else if(currentHScore == bestScore){
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
        for(int i: tabu){
            System.out.printf("-%d-",i);

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
