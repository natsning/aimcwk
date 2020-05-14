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

        int N = oProblem.getNumberOfHeuristics();
//        int N = 5;
        int tourSize = 2, tabuLength = 2;
        int h,prevH,rmvH,p1,p2,indexToReplace;
        int initialWaterLevel = 0, waterLevel=initialWaterLevel, maxWaterLevel=0;
        boolean acceptOnce = false;
        int[] aoHIndex = oProblem.getHeuristicsOfType(ProblemDomain.HeuristicType.OTHER);
        HashSet<Integer> tabuSet = new HashSet<>();

        long iteration = 0;
        double initialIOM = 0.2,  iom = initialIOM;
        double initialDOS = 0.5, dos = initialDOS;
//        double initialTemp = 0.1, temp = initialTemp;
//        double alpha = 0.01, beta = 0.8;
        double current,candidate,delta;


        oProblem.setMemorySize(3);
        oProblem.initialiseSolution(0);
        oProblem.initialiseSolution(1);

        Double initialObjVal = oProblem.getBestSolutionValue();
        LateAcceptance lAccept = new LateAcceptance(10,initialObjVal*1.1,rng);
        HeuristicScore hScore = new HeuristicScore(N,initialObjVal*1.2);

        oProblem.setIntensityOfMutation(iom);
        oProblem.setDepthOfSearch(dos);

        while ( !hasTimeExpired()) {

            if(iteration<tabuLength){
                rmvH = -1;
            }else{
                rmvH = oProblem.getHeuristicCallRecord()[(int)(iteration-tabuLength)%N];
            }
//            h = chooseHeuristic(N,rmvH,tabuSet,hScore);
            h = rouletteWheel(aoHIndex,hScore);
            oProblem.getHeuristicCallRecord()[(int)iteration%N] = h;

            if (h < 5) {

                candidate = oProblem.applyHeuristic(h, 0,2 );


            } else {

                oProblem.initialiseSolution(1);
                candidate = oProblem.applyHeuristic(h, 0, 1, 2);
            }


            current = oProblem.getFunctionValue(0);
            if(candidate<= lAccept.getAverage()){

                oProblem.copySolution(2,0);
                lAccept.updateLateAcceptance(candidate);

                if(candidate<current){
//                            System.out.printf("%d improved to %f (%f)\n",h,candidate,current);
                    hScore.increaseScore(h,BigDecimal.valueOf(current-candidate));
//                  acceptOnce = true;
                }else{
//                            System.out.printf("%d better than avg %f\n",h,candidate);
                    hScore.updateTimeScore(h);
                }

            }else{
//                        System.out.printf("%d worsen to %f\n",h,candidate);
                delta = candidate-current;
                if(delta <= 0){
                    delta = initialObjVal*0.05;
                }
                hScore.decreaseScore(h,BigDecimal.valueOf(delta));
            }


            iteration++;

//            if (acceptOnce){
//                System.out.printf("Stucked for %d \n",waterLevel );
//                acceptOnce = false;
//                if(waterLevel>maxWaterLevel){
//                    maxWaterLevel = waterLevel;
//                }
//                waterLevel = initialWaterLevel;
////                oProblem.setIntensityOfMutation(initialIOM);
//            }else{
//                waterLevel++;
//            }

//            if(waterLevel>20){
//                iom*=1.01;
//                oProblem.setIntensityOfMutation(iom);
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
        hScore.printHScore();
        System.out.print(waterLevel);



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
        BigDecimal currentHScore = BigDecimal.valueOf(Double.MIN_VALUE);
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
                }else if(currentHScore.compareTo(bestScore) == 0){
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

    private int rouletteWheel(int[] hSet,HeuristicScore heuristicScore){
        Double[] aohScores = new Double[hSet.length];
        Double sum = 0.0;
        for(int i=0; i< hSet.length; i++){
            aohScores[i] = heuristicScore.getOverallHScore(hSet[i]).doubleValue();
            sum += aohScores[i];
        }
        Double r = rng.nextDouble() * sum;
        Double cumulative = 0.0;
        for(int i =0; i<hSet.length; i++){
            cumulative+= aohScores[i];
            if(r<cumulative) {
                return hSet[i];
            }
        }
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
