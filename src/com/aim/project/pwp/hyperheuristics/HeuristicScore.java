package com.aim.project.pwp.hyperheuristics;

import com.aim.project.pwp.Utilities;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class HeuristicScore {

    private int numHeuristic;
    private double[] indvScore;
    private double[] timeScore;
    private double phi = 0.99;
    private double upperBound;
    private double lowerBound;

    public HeuristicScore(int numHeuristic,double bounds){
        this.numHeuristic = numHeuristic;
        indvScore = new double[numHeuristic];
        timeScore = new double[numHeuristic];
        Utilities.initialiseDoubleArray(timeScore,1);
        upperBound = bounds*1.2;
        lowerBound = 0;

    }

    public double getIndvScore(int hIndex){
        return indvScore[hIndex]/timeScore[hIndex];
    }

    public void updateTimeScore(int chosenHIndex){
        for(int i = 0; i<numHeuristic; i++){
            if(i == chosenHIndex) {
                timeScore[chosenHIndex] = 1;
            }else if(i==3 || i==4){
                continue;
            }else{
                timeScore[i] += 1;
            }
        }
    }



    public void increaseScore(int hIndex, double change){

//        indvScore[hIndex] = indvScore[hIndex].add(indvWeight);
        if(indvScore[hIndex]==lowerBound){
            indvScore[hIndex] = 0.5*upperBound;
        }else {
            indvScore[hIndex] += 0.1*change;
            if (indvScore[hIndex] > upperBound) {
                indvScore[hIndex] = upperBound;
            }
        }
        updateTimeScore(hIndex);
    }

    public void decreaseScore(int hIndex, double change){
//        indvScore[hIndex] = indvScore[hIndex].subtract(indvWeight);
        indvScore[hIndex] -= 0.1*change;
        if(indvScore[hIndex] < lowerBound){
            indvScore[hIndex] = lowerBound;
        }
        updateTimeScore(hIndex);
    }

    public double getOverallHScore(int hIndex){
//        return (getIndvScore(hIndex).add(getTimeScore(hIndex)));
        return getIndvScore(hIndex) + timeScore[hIndex] ;
    }

    public void printHScore(){
        for(int i=0; i<numHeuristic; i++){
            System.out.printf("%d: %.5f %.5f %.5f\n",i,indvScore[i],timeScore[i],getOverallHScore(i));
        }
    }

}
