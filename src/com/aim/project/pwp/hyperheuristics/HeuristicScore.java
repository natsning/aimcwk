package com.aim.project.pwp.hyperheuristics;

import com.aim.project.pwp.Utilities;

import java.util.ArrayList;


public class HeuristicScore {

    private int numHeuristic;
    private double[] indvScore;
    private double[] timeScore;
    private double phi = 0.99;
    private double upperBound;
    private double lowerBound;
    private double sum;
    private double timeDefaultValue = 1;
    private ArrayList<Integer> restricted;
    private boolean restricted_flag = true;

    public HeuristicScore(int numHeuristic,double bounds){
        this.numHeuristic = numHeuristic;
        indvScore = new double[numHeuristic];
        timeScore = new double[numHeuristic];
        lowerBound = 0;
        upperBound = bounds*1.2;
        Utilities.initialiseDoubleArray(timeScore,timeDefaultValue);
        Utilities.initialiseDoubleArray(indvScore,lowerBound);
        sum = numHeuristic*lowerBound + numHeuristic*timeDefaultValue;
        restricted_flag = false;
    }

    public HeuristicScore(int numHeuristic,double bounds, int[] restrictHeuristic){
        this.numHeuristic = numHeuristic;
        indvScore = new double[numHeuristic];
        timeScore = new double[numHeuristic];
        lowerBound = 0;
        upperBound = bounds*1.2;
        restricted = Utilities.convertArrayToArrayList(restrictHeuristic);
        Utilities.initialiseDoubleArray(timeScore,timeDefaultValue);
        Utilities.initialiseDoubleArray(indvScore,lowerBound);
        sum = numHeuristic*lowerBound + numHeuristic*timeDefaultValue;

    }

    public double getIndvScore(int hIndex){
        return indvScore[hIndex];
    }

    public void updateTimeScore(int chosenHIndex){
        for(int i = 0; i<numHeuristic; i++){
            if(i == chosenHIndex) {
                if(timeScore[chosenHIndex]>1){
                    sum -= (timeScore[chosenHIndex]-1);
                }
                timeScore[chosenHIndex] = 1;
            }else if(restricted_flag && !restricted.contains(i)){

                timeScore[i] += 1;
                sum += 1;

            }else{
                timeScore[i] += 1;
                sum += 1;
            }
        }

    }

    public void increaseScore(int hIndex, double change){

        updateTimeScore(hIndex);

        if(indvScore[hIndex]== lowerBound){
            sum += (0.5*upperBound - lowerBound);
            indvScore[hIndex] = 0.5*upperBound;

        }else {
            indvScore[hIndex] += 0.1*change;
            sum+= 0.1*change;
            if (indvScore[hIndex] > upperBound) {
                sum -= (indvScore[hIndex]-upperBound);
                indvScore[hIndex] = upperBound;
            }
        }
    }

    public void decreaseScore(int hIndex, double change){

        updateTimeScore(hIndex);

        indvScore[hIndex] -= 0.1*change;
        sum -= (0.1*change);
        if(indvScore[hIndex] < lowerBound){
            sum += (lowerBound - indvScore[hIndex]);
            indvScore[hIndex] = lowerBound;
        }

    }

    public double getOverallHScore(int hIndex){
//        return (getIndvScore(hIndex).add(getTimeScore(hIndex)));
        return getIndvScore(hIndex) + timeScore[hIndex] ;
    }

    public double getSum(){
        return sum;
    }

    public void printHScore(){
        for(int i=0; i<numHeuristic; i++){
            System.out.printf("%d: %.5f %.5f %.5f\n",i,indvScore[i],timeScore[i],getOverallHScore(i));
        }

    }

}
